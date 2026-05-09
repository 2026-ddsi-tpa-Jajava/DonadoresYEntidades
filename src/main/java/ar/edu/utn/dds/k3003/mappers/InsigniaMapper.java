package ar.edu.utn.dds.k3003.mappers;

import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.model.Insignia;

public class InsigniaMapper implements Mapper<InsigniaDTO, Insignia> {
  @Override
  public Insignia map(InsigniaDTO dto) {
    return new Insignia(dto.id());
  }
}
