package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.PersistableEntity;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class InMemoryRepo<T extends PersistableEntity> {
  private final Map<Long, T> storage = new ConcurrentHashMap<>();
  private final AtomicLong incrementalId = new AtomicLong(1);

  public Optional<T> findById(Long id) {
    if (id == null) throw new IllegalArgumentException("El ID no puede ser nulo");
    return Optional.ofNullable(storage.get(id));
  }

  public T save(T obj) {
    if (obj == null) throw new IllegalArgumentException("El objeto a guardar no puede ser nulo");
    if (obj.getId() != null)
      throw new IllegalArgumentException(
          "El ID no debe ser proporcionado al guardar un nuevo elemento");

    obj.setId(incrementalId.getAndIncrement());
    this.storage.put(obj.getId(), obj);

    return this.storage.get(obj.getId());
  }

  public T update(T obj) {
    if (obj == null) throw new IllegalArgumentException("El objeto a actualizar no puede ser nulo");
    if (obj.getId() == null)
      throw new IllegalArgumentException("El ID debe ser proporcionado al actualizar un elemento");
    if (!this.storage.containsKey(obj.getId()))
      throw new NoSuchElementException("No existe un elemento con ese ID");

    this.storage.put(obj.getId(), obj);

    return this.storage.get(obj.getId());
  }

  public T deleteById(Long id) {
    if (id == null) throw new IllegalArgumentException("El ID no puede ser nulo");

    T removed = this.storage.remove(id);
    if (removed == null) throw new NoSuchElementException("El elemento a eliminar no existe");

    return removed;
  }

  public List<T> findAll() {
    return new ArrayList<>(this.storage.values());
  }

  public void deleteAll() {
    this.storage.clear();
  }
}
