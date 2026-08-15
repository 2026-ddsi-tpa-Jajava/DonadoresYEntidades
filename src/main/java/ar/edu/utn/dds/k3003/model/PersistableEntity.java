package ar.edu.utn.dds.k3003.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
public abstract class PersistableEntity {
  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  public PersistableEntity(Long id) {
    this.id = id;
  }

  public PersistableEntity() {
    // Constructor vacío requerido por JPA
  }
}
