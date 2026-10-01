package ar.edu.utn.dds.k3003.tools;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

/** Tools MCP sobre entidades benéficas. */
@Component
public class EntidadesTools {

    private final Fachada fachada;

    public EntidadesTools(Fachada fachada) {
        this.fachada = fachada;
    }

    @Tool(description = "Lista todas las entidades benéficas")
    public List<EntidadBeneficaDTO> listarEntidades() {
        return fachada.obtenerEntidades();
    }

    @Tool(description = "Obtiene una entidad benéfica por su ID")
    public EntidadBeneficaDTO obtenerEntidad(@ToolParam(description = "ID de la entidad") String entidadId) {
        return fachada.buscarEntidadPorID(entidadId);
    }

    @Tool(description = "Crea una entidad benéfica nueva")
    public EntidadBeneficaDTO crearEntidad(
            @ToolParam(description = "Razón social") String razonSocial,
            @ToolParam(description = "Domicilio") String domicilio,
            @ToolParam(description = "Teléfono") String telefono,
            @ToolParam(description = "Correo electrónico") String correo) {
        return fachada.agregarEntidad(new EntidadBeneficaDTO(null, razonSocial, domicilio, telefono, correo));
    }

    @Tool(description = "Actualiza datos de una entidad benéfica. Solo se modifican los campos informados")
    public EntidadBeneficaDTO actualizarEntidad(
            @ToolParam(description = "ID de la entidad") String entidadId,
            @ToolParam(description = "Nueva razón social") String razonSocial,
            @ToolParam(description = "Nuevo domicilio") String domicilio,
            @ToolParam(description = "Nuevo teléfono") String telefono,
            @ToolParam(description = "Nuevo correo") String correo) {
        return fachada.modificarEntidad(entidadId, razonSocial, domicilio, telefono, correo);
    }
}
