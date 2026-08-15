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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        if (donadorDTO == null) throw new IllegalArgumentException("El donador no puede ser nulo");
        Donador donador = this.donadoresRepository.save(this.nuevoDonadorMapper.map(donadorDTO));
        Metrics.counter("donadores.registrados").increment();
        return this.donadorAssembler.toDTO(donador);
    }

    public List<DonadorDTO> obtenerDonadores() {
        return this.donadoresRepository.findAll().stream()
                .map(this.donadorAssembler::toDTO)
                .toList();
    }

    public void eliminarTodosLosDonadores() {
        this.donadoresRepository.deleteAll();
    }

    @Override
    public DonadorDTO buscarDonadorPorID(String donadorID) {
        return this.donadorAssembler.toDTO(this.obtenerDonador(donadorID));
    }

    @Override
    public DonadorDTO modificarEstado(String donadorID, EstadoDonadorEnum estado) {
        if (estado == null) throw new IllegalArgumentException("El estado no puede ser nulo");

        Donador donador = this.obtenerDonador(donadorID);
        donador.setEstado(estado);

        return this.donadorAssembler.toDTO(this.donadoresRepository.update(donador));
    }

    @Override
    public DonadorDTO modificarCategoria(String donadorID, String categoria) {
        if (categoria == null) throw new IllegalArgumentException("La categoría no puede ser nula");

        Donador donador = this.obtenerDonador(donadorID);
        donador.setCategoria(categoria);

        return this.donadorAssembler.toDTO(this.donadoresRepository.update(donador));
    }

    @Override
    public void setFachadaIncentivos(FachadaIncentivos fachadaIncentivos) {}

    @Override
    public Boolean puedeDonar(String donadorID) {
        return this.obtenerDonador(donadorID).puedeDonar();
    }

    @Override
    public List<NecesidadMaterialDTO> obtenerNecesidadesInsatisfechasDe(String productoSolicitado) {
        if (productoSolicitado == null || productoSolicitado.isBlank())
            throw new IllegalArgumentException("El producto solicitado no puede ser nulo o vacío");

        return this.necesidadesRepository.findAll().stream()
                .filter(necesidadMaterial -> necesidadMaterial.esDeProducto(productoSolicitado) && !necesidadMaterial.estaSatisfecha())
                .map(this.necesidadAssembler::toDTO)
                .toList();
    }

    @Override
    public List<QuejaDTO> obtenerQuejasDe(String donadorID) {
        Donador donador = this.obtenerDonador(donadorID);
        return this.quejasRepository.findAll().stream()
                .filter(queja -> queja.esDeDonador(donador))
                .map(this.quejaAssembler::toDTO)
                .toList();
    }

    @Override
    public NecesidadMaterialDTO satisfacerNecesidad(String necesidadID, Integer cantidad) {
        NecesidadMaterial necesidad =
                this.necesidadesRepository
                        .findById(necesidadID)
                        .orElseThrow(
                                () -> new NecesidadNoEncontradaException("No existe una necesidad con ese ID"));

        necesidad.satisfacer(cantidad);
        Metrics.counter("necesidades.satisfechas").increment();
        return this.necesidadAssembler.toDTO(this.necesidadesRepository.update(necesidad));
    }

    @Override
    public DonadorStatsDTO estadisticasDonador(String donadorID) {
        Donador donador = this.obtenerDonador(donadorID);

        List<Insignia> insignias = this.incentivosApiClient.obtenerInsigniasDeDonador(donadorID).stream()
                .map(this.insigniaMapper::map)
                .toList();

        MisionDTO misionDTO = this.incentivosApiClient.obtenerMisionActualDeDonador(donadorID);
        Mision mision = this.misionMapper.map(misionDTO);

        DonadorStats donadorStats = this.donadorStatsTransformer.crearDonadorStatsCon(donador, mision, insignias);
         return this.donadorStatsDTOMapper.map(donadorStats);
    }


    @Override
    public EntidadBeneficaDTO agregarEntidad(EntidadBeneficaDTO entidadBeneficaDTO) {
        if (entidadBeneficaDTO == null)
            throw new IllegalArgumentException("La entidad benéfica no puede ser nula");

        EntidadBenefica entidadBeneficaGuardada =
                this.entidadesRepository.save(this.entidadAssembler.toDomain(entidadBeneficaDTO));
        Metrics.counter("entidades.registradas").increment();
        return this.entidadAssembler.toDTO(entidadBeneficaGuardada);
    }

    public List<EntidadBeneficaDTO> obtenerEntidades() {
        return this.entidadesRepository.findAll().stream()
                .map(this.entidadAssembler::toDTO)
                .toList();
    }

    public void eliminarTodasLasEntidades() {
        this.entidadesRepository.deleteAll();
    }

    @Override
    public EntidadBeneficaDTO buscarEntidadPorID(String entidadID) {
        return this.entidadAssembler.toDTO(this.obtenerEntidadBenefica(entidadID));
    }

    @Override
    public NecesidadMaterialDTO registrarNecesidad(NecesidadMaterialDTO necesidadMaterialDTO) {
        if (necesidadMaterialDTO == null)
            throw new IllegalArgumentException("La necesidad no puede ser nula");

        String productoID = necesidadMaterialDTO.productoSolicitadoID();

        boolean esProductoValido = this.donacionesApiClient
                .esProductoValido(productoID);

        if (!esProductoValido)
            throw new IllegalArgumentException("El producto solicitado no es válido");

        NecesidadMaterial necesidad = this.necesidadAssembler.toDomain(necesidadMaterialDTO);
        int cantidadEnStock = this.logisticaApiClient.cuantoStockHayDe(productoID);

        if (necesidad instanceof NecesidadRecurrente && cantidadEnStock >= necesidad.getCantidadObjetivo()) {
            this.logisticaApiClient.crearAsignacionStock(necesidad.getId(), necesidad.getProductoSolicitadoID(), necesidad.getCantidadObjetivo());
        } else if (necesidad instanceof NecesidadExtraordinaria && cantidadEnStock > 0) {
            this.logisticaApiClient.crearAsignacionStock(necesidad.getId(), necesidad.getProductoSolicitadoID(), Math.min(cantidadEnStock, necesidad.getCantidadObjetivo()));
        }

        NecesidadMaterial necesidadGuardada = this.necesidadesRepository.save(necesidad);
        Metrics.counter("necesidades.registradas").increment();
        return this.necesidadAssembler.toDTO(necesidadGuardada);
    }

    public void eliminarTodasLasNecesidades() {
        this.necesidadesRepository.deleteAll();
    }

    @Override
    public QuejaDTO agregarQueja(QuejaDTO quejaDTO) {
        if (quejaDTO == null) throw new IllegalArgumentException("La queja no puede ser nula");

        Queja queja = this.quejasRepository.save(this.quejaAssembler.toDomain(quejaDTO));
        Donador donador = this.obtenerDonador(queja.getDonadorID());
        donador.agregarQueja();
        this.donadoresRepository.update(donador);
        Metrics.counter("quejas.registradas").increment();
        return this.quejaAssembler.toDTO(queja);
    }

    public void eliminarTodasLasQuejas() {
        this.quejasRepository.deleteAll();
    }

    private Donador obtenerDonador(String donadorID) {
        if (donadorID == null || donadorID.isBlank())
            throw new IllegalArgumentException("El ID del donador no puede ser nulo o vacío");

        return this.donadoresRepository
                .findById(donadorID)
                .orElseThrow(() -> new DonadorNoEncontradoException("No existe un donador con ese ID"));
    }

    private EntidadBenefica obtenerEntidadBenefica(String entidadID) {
        if (entidadID == null || entidadID.isBlank())
            throw new IllegalArgumentException("El ID de la entidad benéfica no puede ser nulo o vacío");

        return this.entidadesRepository
                .findById(entidadID)
                .orElseThrow(() -> new EntidadNoEncontradaException("No existe una entidad benéfica con ese ID"));
    }
}
