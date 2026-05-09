package ar.edu.utn.dds.k3003.mappers;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorStatsDTO;
import ar.edu.utn.dds.k3003.model.DonadorStats;

public class DonadorStatsDTOMapper implements Mapper<DonadorStats, DonadorStatsDTO> {

  @Override
  public DonadorStatsDTO map(DonadorStats donadorStats) {
    return new DonadorStatsDTO(
        donadorStats.getId(),
        donadorStats.getNombre(),
        donadorStats.getApellido(),
        donadorStats.getEdad(),
        donadorStats.getEstado(),
        donadorStats.getCategoria(),
        donadorStats.getMisionActualID(),
        donadorStats.getInsigniasID());
  }
}
