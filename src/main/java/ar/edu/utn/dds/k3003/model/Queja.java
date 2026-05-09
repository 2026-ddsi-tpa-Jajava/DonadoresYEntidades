package ar.edu.utn.dds.k3003.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class Queja extends Persistable {
  private String donacionID;
  private String donadorID;
  private LocalDate fecha;
  private String descripcion;

  public Queja(
      String id, String donacionID, String donadorID, LocalDate fecha, String descripcion) {
    super(id);
    this.donacionID = donacionID;
    this.donadorID = donadorID;
    this.fecha = fecha;
    this.descripcion = descripcion;
  }

  public boolean esDeDonador(Donador donador) {
    return this.getDonadorID().equalsIgnoreCase(donador.getId());
  }
}
