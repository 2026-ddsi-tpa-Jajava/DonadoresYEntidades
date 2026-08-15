package ar.edu.utn.dds.k3003.mappers;

import ar.edu.utn.dds.k3003.model.Donador;
import ar.edu.utn.dds.k3003.model.DonadorStats;
import ar.edu.utn.dds.k3003.model.Insignia;
import ar.edu.utn.dds.k3003.model.Mision;

import java.util.List;

public class DonadorStatsTransformer {
  public DonadorStats crearDonadorStatsCon(
      Donador donador, Mision mision, List<Insignia> insignias) {
    return new DonadorStats(
        IdUtils.stringify(donador.getId()),
        donador.getNombre(),
        donador.getApellido(),
        donador.getEdad(),
        donador.getEstado(),
        donador.getCategoria(),
        mision.getId(),
        this.obtenerInsigniasIds(insignias));
  }

  private List<String> obtenerInsigniasIds(List<Insignia> insignias) {
    return insignias.stream().map(Insignia::getId).toList();
  }
}
