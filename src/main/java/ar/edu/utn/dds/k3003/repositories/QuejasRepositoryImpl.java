package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Donador;
import ar.edu.utn.dds.k3003.model.Queja;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class QuejasRepositoryImpl implements QuejasRepository {

  private final QuejasJpaRepository jpaRepository;
  private final AtomicLong idSecuencial = new AtomicLong(1);

  @Autowired
  public QuejasRepositoryImpl(QuejasJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Override
  public Optional<Queja> findById(String id) {
    return jpaRepository.findById(id);
  }

  @Override
  public Queja save(Queja queja) {
    if (queja == null) throw new IllegalArgumentException("La queja no puede ser nula");
    if (queja.getId() != null)
      throw new IllegalArgumentException("El ID no debe ser proporcionado al guardar un nuevo elemento");

    queja.setId(String.valueOf(idSecuencial.getAndIncrement()));
    return jpaRepository.save(queja);
  }

  @Override
  public Queja update(Queja queja) {
    if (queja == null) throw new IllegalArgumentException("La queja no puede ser nula");
    if (queja.getId() == null)
      throw new IllegalArgumentException("El ID debe ser proporcionado al actualizar un elemento");
    if (!jpaRepository.existsById(queja.getId()))
      throw new NoSuchElementException("No existe un elemento con ese ID");

    return jpaRepository.save(queja);
  }

  @Override
  public Queja deleteById(String id) {
    if (id == null || id.isBlank()) throw new IllegalArgumentException("El ID no puede ser nulo o vacío");
    Optional<Queja> removed = jpaRepository.findById(id);
    if (removed.isEmpty()) throw new NoSuchElementException("El elemento a eliminar no existe");

    jpaRepository.delete(removed.get());
    return removed.get();
  }

  @Override
  public List<Queja> findAll() {
    return jpaRepository.findAll();
  }

  @Override
  public void deleteAll() {
    jpaRepository.deleteAll();
    idSecuencial.set(1);
  }
}

