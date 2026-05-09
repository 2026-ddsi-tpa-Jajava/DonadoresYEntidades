package ar.edu.utn.dds.k3003.mappers;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.model.Donador;

public class NuevoDonadorMapper implements Mapper<DonadorDTO, Donador> {
  @Override
  public Donador map(DonadorDTO donador) {
    return new Donador(
        donador.id(),
        donador.nombre(),
        donador.apellido(),
        donador.edad(),
        donador.email(),
        donador.nroDocumento(),
        donador.domicilio());
  }
}
