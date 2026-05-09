package ar.edu.utn.dds.k3003.mappers;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.model.Mision;

public class MisionMapper implements Mapper<MisionDTO, Mision> {
  @Override
  public Mision map(MisionDTO dto) {
    return new Mision(dto.id());
  }
}
