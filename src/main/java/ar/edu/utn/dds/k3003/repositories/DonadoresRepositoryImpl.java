package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Donador;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Implementación de DonadoresRepository que usa DonadoresJpaRepository para persistencia en base
 * de datos.
 */
@Component
public class DonadoresRepositoryImpl implements DonadoresRepository {

  private final DonadoresJpaRepository jpaRepository;

  public DonadoresRepositoryImpl(DonadoresJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Optional<Donador> findById(Long id) {
    return jpaRepository.findById(id);
  }

  @Override
  public Donador save(Donador donador) {
    if (donador == null) throw new IllegalArgumentException("El donador no puede ser nulo");
    if (donador.getId() != null)
      throw new IllegalArgumentException("El ID no debe ser proporcionado al guardar un nuevo elemento");

    return jpaRepository.save(donador);
  }

  @Override
  public Donador update(Donador donador) {
    if (donador == null) throw new IllegalArgumentException("El donador no puede ser nulo");
    if (donador.getId() == null)
      throw new IllegalArgumentException("El ID debe ser proporcionado al actualizar un elemento");
    if (!jpaRepository.existsById(donador.getId()))
      throw new NoSuchElementException("No existe un elemento con ese ID");

    return jpaRepository.save(donador);
  }

  @Override
  public Donador deleteById(Long id) {
    if (id == null) throw new IllegalArgumentException("El ID no puede ser nulo");

    Optional<Donador> donador = jpaRepository.findById(id);
    if (donador.isEmpty()) throw new NoSuchElementException("El elemento a eliminar no existe");

    jpaRepository.delete(donador.get());
    return donador.get();
  }

  @Override
  public List<Donador> findAll() {
    return jpaRepository.findAll();
  }

  @Override
  public void deleteAll() {
    jpaRepository.deleteAll();
  }
}
