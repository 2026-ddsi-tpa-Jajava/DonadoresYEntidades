package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Persistable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public abstract class InMemoryRepo<T extends Persistable> {
  private final Map<String, T> storage = new ConcurrentHashMap<>();
  private final AtomicLong incrementalId = new AtomicLong(1);

  protected abstract String getObjectName();

  public Optional<T> findById(String id) {
    if (id == null || id.isBlank())
      throw new IllegalArgumentException("El ID no puede ser nulo o vacío");
    return Optional.ofNullable(storage.get(id));
  }

  public T save(T obj) {
    if (obj == null) throw new IllegalArgumentException("El objeto a guardar no puede ser nulo");
    if (obj.getId() != null)
      throw new IllegalArgumentException(
          "El ID no debe ser proporcionado al guardar un nuevo elemento");

    obj.setId(this.getObjectName() + incrementalId.getAndIncrement());
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

  public T deleteById(String id) {
    if (id == null || id.isBlank())
      throw new IllegalArgumentException("El ID no puede ser nulo o vacío");

    T removed = this.storage.remove(id);
    if (removed == null) throw new NoSuchElementException("El elemento a eliminar no existe");

    return removed;
  }

  public List<T> findAll() {
    return new ArrayList<>(this.storage.values());
  }
}
