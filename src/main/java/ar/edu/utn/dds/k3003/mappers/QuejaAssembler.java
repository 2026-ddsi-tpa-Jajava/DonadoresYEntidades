package ar.edu.utn.dds.k3003.mappers;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import ar.edu.utn.dds.k3003.model.Queja;

public class QuejaAssembler implements Assembler<Queja, QuejaDTO> {
  @Override
  public Queja toDomain(QuejaDTO quejaDTO) {

    return new Queja(
        IdUtils.parse(quejaDTO.id()),
        quejaDTO.donacionID(),
        IdUtils.parse(quejaDTO.donadorID()),
        quejaDTO.fecha() != null ? quejaDTO.fecha().atStartOfDay() : null,
        quejaDTO.descripcion());
  }

  @Override
  public QuejaDTO toDTO(Queja queja) {
    return new QuejaDTO(
        IdUtils.stringify(queja.getId()),
        queja.getDonacionID(),
        IdUtils.stringify(queja.getDonadorID()),
        queja.getFecha() != null ? queja.getFecha().toLocalDate() : null,
        queja.getDescripcion());
  }
}
