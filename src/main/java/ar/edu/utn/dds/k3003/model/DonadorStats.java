package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DonadorStats extends Persistable {
  private String nombre;
  private String apellido;
  private Integer edad;
  private EstadoDonadorEnum estado;
  private String categoria;
  private String misionActualID;
  private List<String> insigniasID;

  public DonadorStats(
      String id,
      String nombre,
      String apellido,
      Integer edad,
      EstadoDonadorEnum estado,
      String categoria,
      String misionActualID,
      List<String> insigniasID) {
    super(id);
    this.nombre = nombre;
    this.apellido = apellido;
    this.edad = edad;
    this.estado = estado;
    this.categoria = categoria;
    this.misionActualID = misionActualID;
    this.insigniasID = insigniasID;
  }
}
