package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.EntidadBenefica;

import java.util.List;
import java.util.Optional;

public interface EntidadesRepository {
  Optional<EntidadBenefica> findById(Long id);

  EntidadBenefica save(EntidadBenefica entidadBenefica);

  EntidadBenefica update(EntidadBenefica entidadBenefica);

  EntidadBenefica deleteById(Long id);

  List<EntidadBenefica> findAll();

  void deleteAll();
}
