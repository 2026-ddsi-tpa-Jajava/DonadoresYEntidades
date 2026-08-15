package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Donador;

import java.util.List;
import java.util.Optional;

public interface DonadoresRepository {
  Optional<Donador> findById(Long id);

  Donador save(Donador donador);

  Donador update(Donador donador);

  Donador deleteById(Long id);

  List<Donador> findAll();

  void deleteAll();
}
