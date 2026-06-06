package ar.edu.utn.dds.k3003.mappers;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import ar.edu.utn.dds.k3003.model.Queja;

public class QuejaAssembler implements Assembler<Queja, QuejaDTO> {
  @Override
  public Queja toDomain(QuejaDTO quejaDTO) {

    return new Queja(
        quejaDTO.id(),
        quejaDTO.donacionID(),
        quejaDTO.donadorID(),
        quejaDTO.fecha() != null ? quejaDTO.fecha().atStartOfDay() : null,
        quejaDTO.descripcion());
  }

  @Override
  public QuejaDTO toDTO(Queja queja) {
    return new QuejaDTO(
        queja.getId(),
        queja.getDonacionID(),
        queja.getDonadorID(),
        queja.getFecha() != null ? queja.getFecha().toLocalDate() : null,
        queja.getDescripcion());
  }
}
