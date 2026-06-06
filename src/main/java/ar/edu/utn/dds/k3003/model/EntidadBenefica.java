package ar.edu.utn.dds.k3003.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "EntidadBenefica")
public class EntidadBenefica extends Persistable {
  @Column(name = "razon_social")
  private String razonSocial;

  @Column(name = "domicilio")
  private String domicilio;

  @Column(name = "telefono")
  private String telefono;

  @Column(name = "email")
  private String correo;

  @OneToMany(mappedBy = "entidad", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<NecesidadMaterial> necesidades = new ArrayList<>();

  public EntidadBenefica() {
    // Constructor vacío requerido por JPA
  }

  public EntidadBenefica(
      String id, String razonSocial, String domicilio, String telefono, String correo) {
    super(id);
    this.razonSocial = razonSocial;
    this.domicilio = domicilio;
    this.telefono = telefono;
    this.correo = correo;
  }
}
