package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.EntidadBenefica;

public class InMemoryEntidadesRepo extends InMemoryRepo<EntidadBenefica>
    implements EntidadesRepository {
  @Override
  protected String getObjectName() {
    return "entidad";
  }
}
