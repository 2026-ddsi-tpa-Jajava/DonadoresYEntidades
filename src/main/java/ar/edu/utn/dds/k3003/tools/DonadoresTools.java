package ar.edu.utn.dds.k3003.tools;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorStatsDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/** Tools MCP sobre donadores. Delegan en la Fachada: no duplican reglas de negocio. */
@Component
public class DonadoresTools {

    private final Fachada fachada;

    public DonadoresTools(Fachada fachada) {
        this.fachada = fachada;
    }

    @Tool(description = "Lista todos los donadores registrados")
    public List<DonadorDTO> listarDonadores() {
        return fachada.obtenerDonadores();
    }

    @Tool(description = "Obtiene un donador por su ID")
    public DonadorDTO obtenerDonador(@ToolParam(description = "ID del donador") String donadorId) {
        return fachada.buscarDonadorPorID(donadorId);
    }

    @Tool(description = "Crea un donador nuevo. Queda VERIFICADO y con categoría OCASIONAL por defecto")
    public DonadorDTO crearDonador(
            @ToolParam(description = "Nombre") String nombre,
            @ToolParam(description = "Apellido") String apellido,
            @ToolParam(description = "Edad, mayor a 0") Integer edad,
            @ToolParam(description = "Email") String email,
            @ToolParam(description = "Número de documento") String nroDocumento,
            @ToolParam(description = "Domicilio") String domicilio) {
        return fachada.agregarDonador(
                new DonadorDTO(null, nombre, apellido, edad, email, nroDocumento, domicilio, null, null));
    }

    @Tool(description = "Cambia el estado de un donador: VERIFICADO, SOSPECHOSO o BANEADO")
    public DonadorDTO cambiarEstadoDonador(
            @ToolParam(description = "ID del donador") String donadorId,
            @ToolParam(description = "Nuevo estado: VERIFICADO, SOSPECHOSO o BANEADO") String estado) {
        EstadoDonadorEnum nuevoEstado;
        try {
            nuevoEstado = EstadoDonadorEnum.valueOf(estado == null ? "" : estado.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El estado debe ser 'VERIFICADO', 'SOSPECHOSO' o 'BANEADO'");
        }
        return fachada.modificarEstado(donadorId, nuevoEstado);
    }

    @Tool(description = "Indica si un donador puede donar según su estado (los SOSPECHOSOS pueden donar con 50% de probabilidad)")
    public boolean puedeDonar(@ToolParam(description = "ID del donador") String donadorId) {
        return fachada.puedeDonar(donadorId);
    }

    @Tool(description = "Estadísticas del donador: misión actual e insignias obtenidas (consulta al módulo de Incentivos)")
    public DonadorStatsDTO estadisticasDonador(@ToolParam(description = "ID del donador") String donadorId) {
        return fachada.estadisticasDonador(donadorId);
    }

    @Tool(description = "Registra una queja contra un donador. Con el umbral configurado pasa a SOSPECHOSO y luego a BANEADO")
    public QuejaDTO registrarQueja(
            @ToolParam(description = "ID del donador") String donadorId,
            @ToolParam(description = "ID de la donación sobre la que se queja") String donacionId,
            @ToolParam(description = "Descripción de la queja") String descripcion) {
        return fachada.agregarQueja(new QuejaDTO(null, donacionId, donadorId, LocalDate.now(), descripcion));
    }

    @Tool(description = "Lista las quejas de un donador")
    public List<QuejaDTO> listarQuejas(@ToolParam(description = "ID del donador") String donadorId) {
        return fachada.obtenerQuejasDe(donadorId);
    }
}
