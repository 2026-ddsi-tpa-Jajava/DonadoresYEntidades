package ar.edu.utn.dds.k3003.model;

0000000000000000000import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
public abstract class Persistable {
  @Id
  @Column(name = "id")
  private String id;

  public Persistable(String id) {
    this.id = id;
  }

  public Persistable() {
    // Constructor vacío requerido por JPA
  }
}
