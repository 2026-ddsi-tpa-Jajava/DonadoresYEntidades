package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Donador;
import ar.edu.utn.dds.k3003.model.NecesidadMaterial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class NecesidadesRepositoryImpl implements NecesidadesRepository {

  private final NecesidadesJpaRepository jpaRepository;
  private final AtomicLong idSecuencial = new AtomicLong(1);

  @Autowired
  public NecesidadesRepositoryImpl(NecesidadesJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Optional<NecesidadMaterial> findById(String id) {
    return jpaRepository.findById(id);
  }

  @Override
  public NecesidadMaterial save(NecesidadMaterial necesidad) {
    if (necesidad == null) throw new IllegalArgumentException("La necesidad no puede ser nula");
    if (necesidad.getId() != null)
      throw new IllegalArgumentException("El ID no debe ser proporcionado al guardar un nuevo elemento");

    necesidad.setId(String.valueOf(idSecuencial.getAndIncrement()));
    return jpaRepository.save(necesidad);
  }

  @Override
  public NecesidadMaterial update(NecesidadMaterial necesidad) {
    if (necesidad == null) throw new IllegalArgumentException("La necesidad no puede ser nula");
    if (necesidad.getId() == null)
      throw new IllegalArgumentException("El ID debe ser proporcionado al actualizar un elemento");
    if (!jpaRepository.existsById(necesidad.getId()))
      throw new NoSuchElementException("No existe un elemento con ese ID");

    return jpaRepository.save(necesidad);
  }

  @Override
  public NecesidadMaterial deleteById(String id) {
    if (id == null || id.isBlank()) throw new IllegalArgumentException("El ID no puede ser nulo o vacío");
    Optional<NecesidadMaterial> removed = jpaRepository.findById(id);
    if (removed.isEmpty()) throw new NoSuchElementException("El elemento a eliminar no existe");

    jpaRepository.delete(removed.get());
    return removed.get();
  }

  @Override
  public List<NecesidadMaterial> findAll() {
    return jpaRepository.findAll();
  }

  @Override
  public void deleteAll() {
    jpaRepository.deleteAll();
  }
}

