package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.catedra.dtos.donaciones.DonacionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.*;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaDonadoresYEntidades;
import ar.edu.utn.dds.k3003.catedra.fachadas.FachadaIncentivos;
import ar.edu.utn.dds.k3003.clients.DonacionesApiClient;
import ar.edu.utn.dds.k3003.clients.IncentivosApiClient;
import ar.edu.utn.dds.k3003.clients.LogisticaApiClient;
import ar.edu.utn.dds.k3003.exceptions.DonadorNoEncontradoException;
import ar.edu.utn.dds.k3003.exceptions.EntidadNoEncontradaException;
import ar.edu.utn.dds.k3003.exceptions.NecesidadNoEncontradaException;
import ar.edu.utn.dds.k3003.mappers.*;
import ar.edu.utn.dds.k3003.model.*;
import ar.edu.utn.dds.k3003.repositories.*;
import io.micrometer.core.instrument.Metrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class Fachada implements FachadaDonadoresYEntidades {

    private final DonadoresRepository donadoresRepository;
    private final EntidadesRepository entidadesRepository;
    private final QuejasRepository quejasRepository;
    private final NecesidadesRepository necesidadesRepository;
    private final NuevoDonadorMapper nuevoDonadorMapper = new NuevoDonadorMapper();
    private final DonadorAssembler donadorAssembler = new DonadorAssembler();
    private final QuejaAssembler quejaAssembler = new QuejaAssembler();
    private final EntidadBeneficaAssembler entidadAssembler = new EntidadBeneficaAssembler();
    private final NecesidadMaterialAssembler necesidadAssembler = new NecesidadMaterialAssembler();
    private final DonadorStatsTransformer donadorStatsTransformer = new DonadorStatsTransformer();
    private final InsigniaMapper insigniaMapper = new InsigniaMapper();
    private final MisionMapper misionMapper = new MisionMapper();
    private final DonadorStatsDTOMapper donadorStatsDTOMapper = new DonadorStatsDTOMapper();
    private final IncentivosApiClient incentivosApiClient;
    private final DonacionesApiClient donacionesApiClient;
    private final LogisticaApiClient logisticaApiClient;

    @Autowired
    public Fachada(
            DonadoresRepository donadoresRepository,
            EntidadesRepository entidadesRepository,
            QuejasRepository quejasRepository,
            NecesidadesRepository necesidadesRepository,
            IncentivosApiClient incentivosApiClient,
            DonacionesApiClient donacionesApiClient,
            LogisticaApiClient logisticaApiClient) {
        this.donadoresRepository = donadoresRepository;
        this.entidadesRepository = entidadesRepository;
        this.quejasRepository = quejasRepository;
        this.necesidadesRepository = necesidadesRepository;
        this.incentivosApiClient = incentivosApiClient;
        this.donacionesApiClient = donacionesApiClient;
        this.logisticaApiClient = logisticaApiClient;
    }

    // Constructor por defecto para uso en tests o ejecución sin Spring
    public Fachada() {
        this.donacionesApiClient = new DonacionesApiClient();
        this.incentivosApiClient = new IncentivosApiClient();
        this.logisticaApiClient = new LogisticaApiClient();
        this.donadoresRepository = new InMemoryDonadoresRepo();
        this.entidadesRepository = new InMemoryEntidadesRepo();
        this.quejasRepository = new InMemoryQuejasRepo();
        this.necesidadesRepository = new InMemoryNecesidadesRepo();
    }

    @Override
    public DonadorDTO agregarDonador(DonadorDTO donadorDTO) {
        log.info("🧑 Agregando donador...");
        log.info("🧑 Request agregarDonador: {}", donadorDTO);

        if (donadorDTO == null) {
            log.error("🧑 No se pudo agregar el donador: el donador no puede ser nulo");
            throw new IllegalArgumentException("El donador no puede ser nulo");
        }

        Donador donador = this.donadoresRepository.save(this.nuevoDonadorMapper.map(donadorDTO));
        Metrics.counter("donadores.registrados").increment();
        DonadorDTO resultado = this.donadorAssembler.toDTO(donador);

        log.info("🧑 Donador {} agregado correctamente", resultado.id());
        log.info("🧑 Response agregarDonador: {}", resultado);
        return resultado;
    }

    public List<DonadorDTO> obtenerDonadores() {
        log.info("🧑 Obteniendo todos los donadores...");

        List<DonadorDTO> resultado = this.donadoresRepository.findAll().stream()
                .map(this.donadorAssembler::toDTO)
                .toList();

        log.info("🧑 Se obtuvieron {} donadores", resultado.size());
        log.info("🧑 Response obtenerDonadores: {}", resultado);
        return resultado;
    }

    public void eliminarTodosLosDonadores() {
        log.info("🧑 Eliminando todos los donadores...");
        this.donadoresRepository.deleteAll();
        log.info("🧑 Todos los donadores fueron eliminados");
    }

    @Override
    public DonadorDTO buscarDonadorPorID(String donadorID) {
        log.info("🧑 Buscando donador {}...", donadorID);

        DonadorDTO resultado = this.donadorAssembler.toDTO(this.obtenerDonador(donadorID));

        log.info("🧑 Donador {} encontrado", donadorID);
        log.info("🧑 Response buscarDonadorPorID: {}", resultado);
        return resultado;
    }

    @Override
    public DonadorDTO modificarEstado(String donadorID, EstadoDonadorEnum estado) {
        log.info("🧑 Modificando estado del donador {}...", donadorID);
        log.info("🧑 Request modificarEstado: donadorID={}, estado={}", donadorID, estado);

        if (estado == null) {
            log.error("🧑 No se pudo modificar el estado del donador {}: el estado no puede ser nulo", donadorID);
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }

        Donador donador = this.obtenerDonador(donadorID);
        donador.setEstado(estado);

        DonadorDTO resultado = this.donadorAssembler.toDTO(this.donadoresRepository.update(donador));

        log.info("🧑 Estado del donador {} modificado a {}", donadorID, estado);
        log.info("🧑 Response modificarEstado: {}", resultado);
        return resultado;
    }

    @Override
    public DonadorDTO modificarCategoria(String donadorID, String categoria) {
        log.info("🧑 Modificando categoría del donador {}...", donadorID);
        log.info("🧑 Request modificarCategoria: donadorID={}, categoria={}", donadorID, categoria);

        if (categoria == null) {
            log.error("🧑 No se pudo modificar la categoría del donador {}: la categoría no puede ser nula", donadorID);
            throw new IllegalArgumentException("La categoría no puede ser nula");
        }

        Donador donador = this.obtenerDonador(donadorID);
        donador.setCategoria(categoria);

        DonadorDTO resultado = this.donadorAssembler.toDTO(this.donadoresRepository.update(donador));

        log.info("🧑 Categoría del donador {} modificada a {}", donadorID, categoria);
        log.info("🧑 Response modificarCategoria: {}", resultado);
        return resultado;
    }

    @Override
    public void setFachadaIncentivos(FachadaIncentivos fachadaIncentivos) {
        log.info("⚙️ Seteando fachada de incentivos...");
    }

    @Override
    public Boolean puedeDonar(String donadorID) {
        log.info("🧑 Consultando si el donador {} puede donar...", donadorID);

        Boolean resultado = this.obtenerDonador(donadorID).puedeDonar();

        if (!resultado) {
            log.warn("🧑 El donador {} no puede donar", donadorID);
        }

        log.info("🧑 Resultado puedeDonar para el donador {}: {}", donadorID, resultado);
        log.info("🧑 Response puedeDonar: {}", resultado);
        return resultado;
    }

    @Override
    public List<NecesidadMaterialDTO> obtenerNecesidadesInsatisfechasDe(String productoSolicitado) {
        log.info("📦 Obteniendo necesidades insatisfechas del producto {}...", productoSolicitado);

        if (productoSolicitado == null || productoSolicitado.isBlank()) {
            log.error("📦 No se pudieron obtener las necesidades insatisfechas: el producto solicitado no puede ser nulo o vacío");
            throw new IllegalArgumentException("El producto solicitado no puede ser nulo o vacío");
        }

        List<NecesidadMaterialDTO> resultado = this.necesidadesRepository.findAll().stream()
                .filter(necesidadMaterial -> necesidadMaterial.esDeProducto(productoSolicitado) && !necesidadMaterial.estaSatisfecha())
                .map(this.necesidadAssembler::toDTO)
                .toList();

        if (resultado.isEmpty()) {
            log.warn("📦 No se encontraron necesidades insatisfechas para el producto {}", productoSolicitado);
        }

        log.info("📦 Se obtuvieron {} necesidades insatisfechas del producto {}", resultado.size(), productoSolicitado);
        log.info("📦 Response obtenerNecesidadesInsatisfechasDe: {}", resultado);
        return resultado;
    }

    @Override
    public List<QuejaDTO> obtenerQuejasDe(String donadorID) {
        log.info("📢 Obteniendo quejas del donador {}...", donadorID);

        Donador donador = this.obtenerDonador(donadorID);
        List<QuejaDTO> resultado = this.quejasRepository.findAll().stream()
                .filter(queja -> queja.esDeDonador(donador))
                .map(this.quejaAssembler::toDTO)
                .toList();

        if (resultado.isEmpty()) {
            log.warn("📢 El donador {} no tiene quejas registradas", donadorID);
        }

        log.info("📢 Se obtuvieron {} quejas del donador {}", resultado.size(), donadorID);
        log.info("📢 Response obtenerQuejasDe: {}", resultado);
        return resultado;
    }

    @Override
    public NecesidadMaterialDTO satisfacerNecesidad(String necesidadID, Integer cantidad) {
        log.info("📦 Satisfaciendo necesidad {}...", necesidadID);
        log.info("📦 Request satisfacerNecesidad: necesidadID={}, cantidad={}", necesidadID, cantidad);

        NecesidadMaterial necesidad =
                this.necesidadesRepository
                        .findById(IdUtils.parse(necesidadID))
                        .orElseThrow(
                                () -> {
                                    log.error("📦 No se pudo satisfacer la necesidad {}: no existe una necesidad con ese ID", necesidadID);
                                    return new NecesidadNoEncontradaException("No existe una necesidad con ese ID");
                                });

        necesidad.satisfacer(cantidad);
        Metrics.counter("necesidades.satisfechas").increment();
        NecesidadMaterialDTO resultado = this.necesidadAssembler.toDTO(this.necesidadesRepository.update(necesidad));

        log.info("📦 Necesidad {} satisfecha con cantidad {}", necesidadID, cantidad);
        log.info("📦 Response satisfacerNecesidad: {}", resultado);
        return resultado;
    }

    @Override
    public DonadorStatsDTO estadisticasDonador(String donadorID) {
        log.info("🧑 Obteniendo estadísticas del donador {}...", donadorID);

        Donador donador = this.obtenerDonador(donadorID);

        log.info("🧑 Solicitando insignias del donador {} a incentivosApiClient", donadorID);
        List<Insignia> insignias = this.incentivosApiClient.obtenerInsigniasDeDonador(donadorID).stream()
                .map(this.insigniaMapper::map)
                .toList();
        log.info("🧑 Insignias recibidas del donador {}: {}", donadorID, insignias);

        log.info("🧑 Solicitando misión actual del donador {} a incentivosApiClient", donadorID);
        MisionDTO misionDTO = this.incentivosApiClient.obtenerMisionActualDeDonador(donadorID);
        log.info("🧑 Misión recibida del donador {}: {}", donadorID, misionDTO);

        if (misionDTO == null) {
            log.warn("🧑 El donador {} no tiene una misión actual asignada", donadorID);
        }

        Mision mision = this.misionMapper.map(misionDTO);

        DonadorStats donadorStats = this.donadorStatsTransformer.crearDonadorStatsCon(donador, mision, insignias);
        DonadorStatsDTO resultado = this.donadorStatsDTOMapper.map(donadorStats);

        log.info("🧑 Estadísticas del donador {} obtenidas correctamente", donadorID);
        log.info("🧑 Response estadisticasDonador: {}", resultado);
        return resultado;
    }

    @Override
    public EntidadBeneficaDTO agregarEntidad(EntidadBeneficaDTO entidadBeneficaDTO) {
        log.info("🏭 Agregando entidad benéfica...");
        log.info("🏭 Request agregarEntidad: {}", entidadBeneficaDTO);

        if (entidadBeneficaDTO == null) {
            log.error("🏭 No se pudo agregar la entidad benéfica: la entidad benéfica no puede ser nula");
            throw new IllegalArgumentException("La entidad benéfica no puede ser nula");
        }

        EntidadBenefica entidadBeneficaGuardada =
                this.entidadesRepository.save(this.entidadAssembler.toDomain(entidadBeneficaDTO));
        Metrics.counter("entidades.registradas").increment();
        EntidadBeneficaDTO resultado = this.entidadAssembler.toDTO(entidadBeneficaGuardada);

        log.info("🏭 Entidad benéfica {} agregada correctamente", resultado.id());
        log.info("🏭 Response agregarEntidad: {}", resultado);
        return resultado;
    }

    public List<EntidadBeneficaDTO> obtenerEntidades() {
        log.info("🏭 Obteniendo todas las entidades benéficas...");

        List<EntidadBeneficaDTO> resultado = this.entidadesRepository.findAll().stream()
                .map(this.entidadAssembler::toDTO)
                .toList();

        log.info("🏭 Se obtuvieron {} entidades benéficas", resultado.size());
        log.info("🏭 Response obtenerEntidades: {}", resultado);
        return resultado;
    }

    public void eliminarTodasLasEntidades() {
        log.info("🏭 Eliminando todas las entidades benéficas...");
        this.entidadesRepository.deleteAll();
        log.info("🏭 Todas las entidades benéficas fueron eliminadas");
    }

    @Override
    public EntidadBeneficaDTO buscarEntidadPorID(String entidadID) {
        log.info("🏭 Buscando entidad benéfica {}...", entidadID);

        EntidadBeneficaDTO resultado = this.entidadAssembler.toDTO(this.obtenerEntidadBenefica(entidadID));

        log.info("🏭 Entidad benéfica {} encontrada", entidadID);
        log.info("🏭 Response buscarEntidadPorID: {}", resultado);
        return resultado;
    }

    public EntidadBeneficaDTO modificarEntidad(
            String entidadID, String razonSocial, String domicilio, String telefono, String correo) {
        log.info("🏭 Modificando entidad benéfica {}...", entidadID);
        log.info(
                "🏭 Request modificarEntidad: entidadID={}, razonSocial={}, domicilio={}, telefono={}, correo={}",
                entidadID, razonSocial, domicilio, telefono, correo);

        EntidadBenefica entidad = this.obtenerEntidadBenefica(entidadID);
        if (razonSocial != null) entidad.setRazonSocial(razonSocial);
        if (domicilio != null) entidad.setDomicilio(domicilio);
        if (telefono != null) entidad.setTelefono(telefono);
        if (correo != null) entidad.setCorreo(correo);

        EntidadBeneficaDTO resultado = this.entidadAssembler.toDTO(this.entidadesRepository.update(entidad));

        log.info("🏭 Entidad benéfica {} modificada correctamente", entidadID);
        log.info("🏭 Response modificarEntidad: {}", resultado);
        return resultado;
    }

    @Override
    public NecesidadMaterialDTO registrarNecesidad(NecesidadMaterialDTO necesidadMaterialDTO) {
        log.info("📦 Registrando necesidad...");
        log.info("📦 Request registrarNecesidad: {}", necesidadMaterialDTO);

        if (necesidadMaterialDTO == null) {
            log.error("📦 No se pudo registrar la necesidad: la necesidad no puede ser nula");
            throw new IllegalArgumentException("La necesidad no puede ser nula");
        }

        String productoID = necesidadMaterialDTO.productoSolicitadoID();

        log.info("📦 Consultando validez del producto {} a donacionesApiClient", productoID);
        boolean esProductoValido = this.donacionesApiClient
                .esProductoValido(productoID);
        log.info("📦 Producto {} válido: {}", productoID, esProductoValido);

        if (!esProductoValido) {
            log.error("📦 No se pudo registrar la necesidad: el producto {} no es válido", productoID);
            throw new IllegalArgumentException("El producto solicitado no es válido");
        }

        NecesidadMaterial necesidad = this.necesidadAssembler.toDomain(necesidadMaterialDTO);

        log.info("📦 Consultando stock disponible del producto {} a logisticaApiClient", productoID);
        int cantidadEnStock = this.logisticaApiClient.cuantoStockHayDe(productoID);
        log.info("📦 Stock disponible del producto {}: {}", productoID, cantidadEnStock);

        if (cantidadEnStock == 0) {
            log.warn("📦 No hay stock disponible del producto {}, no se generará asignación al registrar la necesidad", productoID);
        }

        NecesidadMaterial necesidadGuardada = this.necesidadesRepository.save(necesidad);
        Metrics.counter("necesidades.registradas").increment();
        if (necesidad instanceof NecesidadRecurrente && cantidadEnStock >= necesidad.getCantidadObjetivo()) {
            log.info(
                    "📦 Creando asignación de stock para necesidad recurrente: necesidadID={}, productoID={}, cantidad={}",
                    necesidad.getId(), productoID, necesidad.getCantidadObjetivo());
            this.logisticaApiClient.crearAsignacionStock(IdUtils.stringify(necesidad.getId()), necesidad.getProductoSolicitadoID(), necesidad.getCantidadObjetivo());
        } else if (necesidad instanceof NecesidadExtraordinaria && cantidadEnStock > 0) {
            int cantidadAAsignar = Math.min(cantidadEnStock, necesidad.getCantidadObjetivo());
            log.info(
                    "📦 Creando asignación de stock para necesidad extraordinaria: necesidadID={}, productoID={}, cantidad={}",
                    necesidad.getId(), productoID, cantidadAAsignar);
            this.logisticaApiClient.crearAsignacionStock(IdUtils.stringify(necesidad.getId()), necesidad.getProductoSolicitadoID(), cantidadAAsignar);
        }

        NecesidadMaterialDTO resultado = this.necesidadAssembler.toDTO(necesidadGuardada);

        log.info("📦 Necesidad {} registrada correctamente", resultado.id());
        log.info("📦 Response registrarNecesidad: {}", resultado);
        return resultado;
    }

    public NecesidadMaterialDTO buscarNecesidadPorId(String id) {
        log.info("📦 Buscando necesidad {}...", id);

        NecesidadMaterial necesidadMaterial = this.necesidadesRepository.findById(IdUtils.parse(id))
                .orElseThrow(() -> {
                    log.error("📦 No se pudo buscar la necesidad {}: no existe una necesidad con ese ID", id);
                    return new NecesidadNoEncontradaException("No existe una necesidad con ese ID");
                });
        NecesidadMaterialDTO resultado = this.necesidadAssembler.toDTO(necesidadMaterial);

        log.info("📦 Necesidad {} encontrada", id);
        log.info("📦 Response buscarNecesidadPorId: {}", resultado);
        return resultado;
    }

    public NecesidadMaterialDTO modificarNecesidad(
            String necesidadID,
            Integer nivelDeUrgencia,
            String descripcion,
            Integer cantidadObjetivo,
            String productoSolicitadoID) {
        log.info("📦 Modificando necesidad {}...", necesidadID);
        log.info(
                "📦 Request modificarNecesidad: necesidadID={}, nivelDeUrgencia={}, descripcion={}, cantidadObjetivo={}, productoSolicitadoID={}",
                necesidadID, nivelDeUrgencia, descripcion, cantidadObjetivo, productoSolicitadoID);

        NecesidadMaterial necesidad =
                this.necesidadesRepository
                        .findById(IdUtils.parse(necesidadID))
                        .orElseThrow(
                                () -> {
                                    log.error("📦 No se pudo modificar la necesidad {}: no existe una necesidad con ese ID", necesidadID);
                                    return new NecesidadNoEncontradaException("No existe una necesidad con ese ID");
                                });

        if (productoSolicitadoID != null) {
            log.info("📦 Consultando validez del producto {} a donacionesApiClient", productoSolicitadoID);
            boolean esProductoValido = this.donacionesApiClient.esProductoValido(productoSolicitadoID);
            log.info("📦 Producto {} válido: {}", productoSolicitadoID, esProductoValido);

            if (!esProductoValido) {
                log.error("📦 No se pudo modificar la necesidad {}: el producto {} no es válido", necesidadID, productoSolicitadoID);
                throw new IllegalArgumentException("El producto solicitado no es válido");
            }
            necesidad.setProductoSolicitadoID(productoSolicitadoID);
        }
        if (nivelDeUrgencia != null) necesidad.setNivelDeUrgencia(nivelDeUrgencia);
        if (descripcion != null) necesidad.setDescripcion(descripcion);
        if (cantidadObjetivo != null) necesidad.setCantidadObjetivo(cantidadObjetivo);

        NecesidadMaterialDTO resultado = this.necesidadAssembler.toDTO(this.necesidadesRepository.update(necesidad));

        log.info("📦 Necesidad {} modificada correctamente", necesidadID);
        log.info("📦 Response modificarNecesidad: {}", resultado);
        return resultado;
    }

    public NecesidadMaterialDTO eliminarNecesidad(String id) {
        log.info("📦 Eliminando necesidad {}...", id);

        NecesidadMaterial necesidad = this.necesidadesRepository.deleteById(IdUtils.parse(id));
        Metrics.counter("necesidades.eliminadas").increment();
        NecesidadMaterialDTO resultado = this.necesidadAssembler.toDTO(necesidad);

        log.info("📦 Necesidad {} eliminada correctamente", id);
        log.info("📦 Response eliminarNecesidad: {}", resultado);
        return resultado;
    }

    public void eliminarTodasLasNecesidades() {
        log.info("📦 Eliminando todas las necesidades...");
        this.necesidadesRepository.deleteAll();
        log.info("📦 Todas las necesidades fueron eliminadas");
    }

    @Override
    public QuejaDTO agregarQueja(QuejaDTO quejaDTO) {
        log.info("📢 Agregando queja...");
        log.info("📢 Request agregarQueja: {}", quejaDTO);

        if (quejaDTO == null) {
            log.error("📢 No se pudo agregar la queja: la queja no puede ser nula");
            throw new IllegalArgumentException("La queja no puede ser nula");
        }

        Queja queja = this.quejasRepository.save(this.quejaAssembler.toDomain(quejaDTO));
        Donador donador = this.obtenerDonador(IdUtils.stringify(queja.getDonadorID()));
        donador.agregarQueja();
        this.donadoresRepository.update(donador);
        Metrics.counter("quejas.registradas").increment();
        QuejaDTO resultado = this.quejaAssembler.toDTO(queja);

        log.info("📢 Queja {} agregada correctamente para el donador {}", resultado.id(), queja.getDonadorID());
        log.info("📢 Response agregarQueja: {}", resultado);
        return resultado;
    }

    public void eliminarTodasLasQuejas() {
        log.info("📢 Eliminando todas las quejas...");
        this.quejasRepository.deleteAll();
        log.info("📢 Todas las quejas fueron eliminadas");
    }

    private Donador obtenerDonador(String donadorID) {
        log.info("🧑 Obteniendo donador {} del repositorio...", donadorID);

        if (donadorID == null || donadorID.isBlank()) {
            log.error("🧑 No se pudo obtener el donador: el ID del donador no puede ser nulo o vacío");
            throw new IllegalArgumentException("El ID del donador no puede ser nulo o vacío");
        }

        Donador donador = this.donadoresRepository
                .findById(IdUtils.parse(donadorID))
                .orElseThrow(() -> {
                    log.error("🧑 No se pudo obtener el donador {}: no existe un donador con ese ID", donadorID);
                    return new DonadorNoEncontradoException("No existe un donador con ese ID");
                });

        log.info("🧑 Donador {} obtenido: {}", donadorID, donador);
        return donador;
    }

    private EntidadBenefica obtenerEntidadBenefica(String entidadID) {
        log.info("🏭 Obteniendo entidad benéfica {} del repositorio...", entidadID);

        if (entidadID == null || entidadID.isBlank()) {
            log.error("🏭 No se pudo obtener la entidad benéfica: el ID de la entidad benéfica no puede ser nulo o vacío");
            throw new IllegalArgumentException("El ID de la entidad benéfica no puede ser nulo o vacío");
        }

        EntidadBenefica entidad = this.entidadesRepository
                .findById(IdUtils.parse(entidadID))
                .orElseThrow(() -> {
                    log.error("🏭 No se pudo obtener la entidad benéfica {}: no existe una entidad benéfica con ese ID", entidadID);
                    return new EntidadNoEncontradaException("No existe una entidad benéfica con ese ID");
                });

        log.info("🏭 Entidad benéfica {} obtenida: {}", entidadID, entidad);
        return entidad;
    }
}
