package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import jakarta.persistence.*;
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
public class Donador extends PersistableEntity {
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

  @ElementCollection
  @CollectionTable(name = "historial_estados", joinColumns = @JoinColumn(name = "donador_id"))
  @Column(name = "estado")
  @Enumerated(EnumType.STRING)
  @OrderColumn(name = "orden")
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
      Long id,
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
      Long id,
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

  public static final int UMBRAL_SOSPECHOSO_POR_DEFECTO = 5;
  public static final int UMBRAL_BANEADO_POR_DEFECTO = 10;

  public void agregarQueja() {
    this.agregarQueja(UMBRAL_SOSPECHOSO_POR_DEFECTO, UMBRAL_BANEADO_POR_DEFECTO);
  }

  public void agregarQueja(int umbralSospechoso, int umbralBaneado) {
    this.cantidadQuejas += 1;
    this.validarCantidadQuejas(umbralSospechoso, umbralBaneado);
  }

  private void validarCantidadQuejas(int umbralSospechoso, int umbralBaneado) {
    if (this.cantidadQuejas >= umbralBaneado) {
      this.setEstado(EstadoDonadorEnum.BANEADO);
      return;
    }

    if (this.cantidadQuejas >= umbralSospechoso) {
      this.setEstado(EstadoDonadorEnum.SOSPECHOSO);
    }
  }

  private void agregarEstadoAHistorial(EstadoDonadorEnum estado) {
    if (!this.historialEstados.isEmpty() && this.historialEstados.getLast().equals(estado)) return;
    this.historialEstados.add(estado);
  }
}
