package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Queja;
import java.util.List;
import java.util.Optional;

public interface QuejasRepository {
  Optional<Queja> findById(Long id);

  Queja save(Queja queja);

  Queja update(Queja queja);

  Queja deleteById(Long id);

  List<Queja> findAll();

  void deleteAll();
}
