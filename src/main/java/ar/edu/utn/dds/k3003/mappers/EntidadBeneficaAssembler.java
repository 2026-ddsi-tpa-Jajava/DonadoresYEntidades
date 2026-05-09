package ar.edu.utn.dds.k3003.mappers;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import ar.edu.utn.dds.k3003.model.EntidadBenefica;

public class EntidadBeneficaAssembler implements Assembler<EntidadBenefica, EntidadBeneficaDTO> {
  @Override
  public EntidadBenefica toDomain(EntidadBeneficaDTO entidadBeneficaDTO) {
    return new EntidadBenefica(
        entidadBeneficaDTO.id(),
        entidadBeneficaDTO.razonSocial(),
        entidadBeneficaDTO.domicilio(),
        entidadBeneficaDTO.telefono(),
        entidadBeneficaDTO.correo());
  }

  @Override
  public EntidadBeneficaDTO toDTO(EntidadBenefica entidadBenefica) {
    return new EntidadBeneficaDTO(
        entidadBenefica.getId(),
        entidadBenefica.getRazonSocial(),
        entidadBenefica.getDomicilio(),
        entidadBenefica.getTelefono(),
        entidadBenefica.getCorreo());
  }
}
