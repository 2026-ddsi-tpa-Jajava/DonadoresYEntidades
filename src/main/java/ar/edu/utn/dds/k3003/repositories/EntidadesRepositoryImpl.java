package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.EntidadBenefica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class EntidadesRepositoryImpl implements EntidadesRepository {

  private final EntidadesJpaRepository jpaRepository;

  @Autowired
  public EntidadesRepositoryImpl(EntidadesJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Optional<EntidadBenefica> findById(String id) {
    return jpaRepository.findById(id);
  }

  @Override
  public EntidadBenefica save(EntidadBenefica entidadBenefica) {
    if (entidadBenefica == null)
      throw new IllegalArgumentException("La entidad benéfica no puede ser nula");
    if (entidadBenefica.getId() != null)
      throw new IllegalArgumentException("El ID no debe ser proporcionado al guardar un nuevo elemento");

    return jpaRepository.save(entidadBenefica);
  }

  @Override
  public EntidadBenefica update(EntidadBenefica entidadBenefica) {
    if (entidadBenefica == null)
      throw new IllegalArgumentException("La entidad benéfica no puede ser nula");
    if (entidadBenefica.getId() == null)
      throw new IllegalArgumentException("El ID debe ser proporcionado al actualizar un elemento");
    if (!jpaRepository.existsById(entidadBenefica.getId()))
      throw new NoSuchElementException("No existe un elemento con ese ID");

    return jpaRepository.save(entidadBenefica);
  }

  @Override
  public EntidadBenefica deleteById(String id) {
    if (id == null || id.isBlank()) throw new IllegalArgumentException("El ID no puede ser nulo o vacío");
    Optional<EntidadBenefica> removed = jpaRepository.findById(id);
    if (removed.isEmpty()) throw new NoSuchElementException("El elemento a eliminar no existe");

    jpaRepository.delete(removed.get());
    return removed.get();
  }

  @Override
  public List<EntidadBenefica> findAll() {
    return jpaRepository.findAll();
  }
}

