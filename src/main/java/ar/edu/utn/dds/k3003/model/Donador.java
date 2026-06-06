package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
0000000000000import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Setter
@Getter
@Entity
@Table(name = "Donador")
public class Donador extends Persistable {
  @Column(name = "nombre")
  private String nombre;

  @Column(name = "apellido")
  private String apellido;

  @Column(name = "edad")
  private Integer edad;

  @Column(name = "email")
  private String email;

  @Column(name = "documento")
  private String nroDocumento;

  @Column(name = "domicilio")
  private String domicilio;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado")
  private EstadoDonadorEnum estado;

  @Column(name = "categoria")
  private String categoria;

  @OneToMany(mappedBy = "donador", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Queja> quejas = new ArrayList<>();

  @Transient
  @Setter(AccessLevel.NONE)
  private List<EstadoDonadorEnum> historialEstados = new ArrayList<>();

  @Column(name = "cantidad_quejas")
  @Setter(AccessLevel.NONE)
  @Getter(AccessLevel.NONE)
  private int cantidadQuejas = 0;

  @Transient
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private final Random random = new Random();

  public Donador() {
    // Constructor vacío requerido por JPA
    super();
  }

  public Donador(
      String id,
      String nombre,
      String apellido,
      Integer edad,
      String email,
      String nroDocumento,
      String domicilio) {
    super(id);
    this.nombre = nombre;
    this.apellido = apellido;
    this.edad = edad;
    this.email = email;
    this.nroDocumento = nroDocumento;
    this.domicilio = domicilio;
    this.estado = EstadoDonadorEnum.VERIFICADO;
    this.categoria = CategoriaDonadorEnum.OCASIONAL.name();

    this.agregarEstadoAHistorial(estado);
  }

  public Donador(
      String id,
      String nombre,
      String apellido,
      Integer edad,
      String email,
      String nroDocumento,
      String domicilio,
      EstadoDonadorEnum estado,
      String categoria) {
    super(id);

    this.nombre = nombre;
    this.apellido = apellido;
    this.edad = edad;
    this.email = email;
    this.nroDocumento = nroDocumento;
    this.domicilio = domicilio;
    this.estado = estado;
    this.categoria = categoria;

    this.agregarEstadoAHistorial(estado);
  }

  public boolean puedeDonar() {
    return switch (this.estado) {
      case VERIFICADO -> true;
      case SOSPECHOSO -> random.nextBoolean(); // 50% de probabilidad de poder donar
      case BANEADO -> false;
    };
  }

  public void setEstado(EstadoDonadorEnum estado) {
    this.estado = estado;
    this.agregarEstadoAHistorial(estado);
  }

  public void agregarQueja() {
    this.cantidadQuejas += 1;
    this.validarCantidadQuejas();
  }

  private void validarCantidadQuejas() {
    if (this.cantidadQuejas >= 10) {
      this.setEstado(EstadoDonadorEnum.BANEADO);
      return;
    }

    if (this.cantidadQuejas >= 5) {
      this.setEstado(EstadoDonadorEnum.SOSPECHOSO);
    }
  }

  private void agregarEstadoAHistorial(EstadoDonadorEnum estado) {
    if (!this.historialEstados.isEmpty() && this.historialEstados.getLast().equals(estado)) return;
    this.historialEstados.add(this.estado);
  }
}
