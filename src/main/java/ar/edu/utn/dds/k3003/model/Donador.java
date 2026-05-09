package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Setter
@Getter
public class Donador extends Persistable {
  private String nombre;
  private String apellido;
  private Integer edad;
  private String email;
  private String nroDocumento;
  private String domicilio;
  private EstadoDonadorEnum estado;
  private String categoria;

  @Setter(AccessLevel.NONE)
  private List<EstadoDonadorEnum> historialEstados = new ArrayList<>();

  @Setter(AccessLevel.NONE)
  private List<Queja> quejas = new ArrayList<>();

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private final Random random = new Random();

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

    this.agregarEstadoAHistorial();
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
    this.agregarEstadoAHistorial();
  }

  public void agregarQueja(Queja queja) {
    this.quejas.add(queja);
    this.validarCantidadQuejas();
  }

  private void validarCantidadQuejas() {
    int cantidadQuejas = this.quejas.size();

    if (cantidadQuejas >= 10) {
      this.estado = EstadoDonadorEnum.BANEADO;
      this.agregarEstadoAHistorial();
      return;
    }

    if (cantidadQuejas >= 5) {
      this.estado = EstadoDonadorEnum.SOSPECHOSO;
      this.agregarEstadoAHistorial();
    }
  }

  private void agregarEstadoAHistorial() {
    this.historialEstados.add(this.estado);
  }
}
