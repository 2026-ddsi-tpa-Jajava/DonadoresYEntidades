package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.NecesidadMaterial;
import java.util.List;
import java.util.Optional;

public interface NecesidadesRepository {
  Optional<NecesidadMaterial> findById(Long id);

  NecesidadMaterial save(NecesidadMaterial necesidad);

  NecesidadMaterial update(NecesidadMaterial necesidad);

  NecesidadMaterial deleteById(Long id);

  List<NecesidadMaterial> findAll();

  void deleteAll();
}
