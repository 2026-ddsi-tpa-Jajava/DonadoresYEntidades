package ar.edu.utn.dds.k3003.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class EntidadBenefica extends Persistable {
  private String razonSocial;
  private String domicilio;
  private String telefono;
  private String correo;

  public EntidadBenefica(
      String id, String razonSocial, String domicilio, String telefono, String correo) {
    super(id);
    this.razonSocial = razonSocial;
    this.domicilio = domicilio;
    this.telefono = telefono;
    this.correo = correo;
  }
}
