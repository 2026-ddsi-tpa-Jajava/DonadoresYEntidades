package ar.edu.utn.dds.k3003.mappers;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.NecesidadMaterialDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.TipoNecesidadMaterialEnum;
import ar.edu.utn.dds.k3003.model.NecesidadExtraordinaria;
import ar.edu.utn.dds.k3003.model.NecesidadMaterial;
import ar.edu.utn.dds.k3003.model.NecesidadRecurrente;

public class NecesidadMaterialAssembler
        implements Assembler<NecesidadMaterial, NecesidadMaterialDTO> {

    @Override
    public NecesidadMaterial toDomain(NecesidadMaterialDTO dto) {
        switch (dto.tipo()) {
            case EXTRAORDINARIA -> {
                return new NecesidadExtraordinaria(
                        dto.id(),
                        dto.entidadID(),
                        dto.nivelDeUrgencia(),
                        dto.descripcion(),
                        dto.cantidadObjetivo(),
                        dto.productoSolicitadoID());
            }
            case RECURRENTE -> {
                return new NecesidadRecurrente(
                        dto.id(),
                        dto.entidadID(),
                        dto.nivelDeUrgencia(),
                        dto.descripcion(),
                        dto.cantidadObjetivo(),
                        dto.productoSolicitadoID());
            }
            default -> throw new IllegalArgumentException("Tipo de necesidad material no reconocido");
        }
    }

    @Override
    public NecesidadMaterialDTO toDTO(NecesidadMaterial domain) {
        TipoNecesidadMaterialEnum tipo;
        if (domain instanceof NecesidadExtraordinaria) {
            tipo = TipoNecesidadMaterialEnum.EXTRAORDINARIA;
        } else if (domain instanceof NecesidadRecurrente) {
            tipo = TipoNecesidadMaterialEnum.RECURRENTE;
        } else {
            throw new IllegalArgumentException("Tipo de necesidad material no reconocido");
        }
        return new NecesidadMaterialDTO(
                domain.getId(),
                domain.getEntidadID(),
                domain.getNivelDeUrgencia(),
                domain.getDescripcion(),
                domain.getCantidadObjetivo(),
                domain.getProductoSolicitadoID(),
                tipo);
    }
}
