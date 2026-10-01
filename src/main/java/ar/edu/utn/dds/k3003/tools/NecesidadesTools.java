package ar.edu.utn.dds.k3003.tools;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.NecesidadMaterialDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.TipoNecesidadMaterialEnum;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

/** Tools MCP sobre necesidades materiales de las entidades. */
@Component
public class NecesidadesTools {

    private final Fachada fachada;

    public NecesidadesTools(Fachada fachada) {
        this.fachada = fachada;
    }

    @Tool(description = "Registra una necesidad material de una entidad. Valida el producto en Donaciones y asigna stock en Logística")
    public NecesidadMaterialDTO registrarNecesidad(
            @ToolParam(description = "ID de la entidad que la necesita") String entidadId,
            @ToolParam(description = "ID del producto solicitado") String productoSolicitadoId,
            @ToolParam(description = "Nivel de urgencia, de 1 a 10") Integer nivelDeUrgencia,
            @ToolParam(description = "Cantidad objetivo, mayor a 0") Integer cantidadObjetivo,
            @ToolParam(description = "Tipo: RECURRENTE o EXTRAORDINARIA") String tipo,
            @ToolParam(description = "Descripción") String descripcion) {
        TipoNecesidadMaterialEnum tipoNecesidad;
        try {
            tipoNecesidad = TipoNecesidadMaterialEnum.valueOf(tipo == null ? "" : tipo.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El tipo debe ser 'RECURRENTE' o 'EXTRAORDINARIA'");
        }
        return fachada.registrarNecesidad(new NecesidadMaterialDTO(
                null, entidadId, nivelDeUrgencia, descripcion, cantidadObjetivo, productoSolicitadoId, tipoNecesidad));
    }

    @Tool(description = "Obtiene una necesidad por su ID")
    public NecesidadMaterialDTO obtenerNecesidad(@ToolParam(description = "ID de la necesidad") String necesidadId) {
        return fachada.buscarNecesidadPorId(necesidadId);
    }

    @Tool(description = "Lista las necesidades todavía insatisfechas de un producto")
    public List<NecesidadMaterialDTO> listarNecesidadesInsatisfechasPorProducto(
            @ToolParam(description = "ID del producto") String productoId) {
        return fachada.obtenerNecesidadesInsatisfechasDe(productoId);
    }

    @Tool(description = "Reporta una entrega: suma una cantidad donada a la necesidad y la marca satisfecha si alcanza el objetivo")
    public NecesidadMaterialDTO satisfacerNecesidad(
            @ToolParam(description = "ID de la necesidad") String necesidadId,
            @ToolParam(description = "Cantidad entregada, mayor a 0") Integer cantidad) {
        return fachada.satisfacerNecesidad(necesidadId, cantidad);
    }
}
