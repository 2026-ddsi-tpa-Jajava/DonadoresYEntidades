package ar.edu.utn.dds.k3003.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Persistable {
  private String id;

  public Persistable(String id) {
    this.id = id;
  }
}
