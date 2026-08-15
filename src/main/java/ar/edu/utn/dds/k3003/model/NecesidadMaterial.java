package ar.edu.utn.dds.k3003.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "Necesidad")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public abstract class NecesidadMaterial extends PersistableEntity {
  @Column(name = "entidad_id")
  private Long entidadID;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "entidad_id", referencedColumnName = "id", insertable = false, updatable = false)
  private EntidadBenefica entidad;

  @Column(name = "nivel_urgencia")
  private Integer nivelDeUrgencia;

  @Column(name = "descripcion")
  private String descripcion;

  @Column(name = "cantidad_objetivo")
  private Integer cantidadObjetivo;

  @Column(name = "producto_id")
  private String productoSolicitadoID;

  @Setter(AccessLevel.NONE)
  @Getter(AccessLevel.NONE)
  @Column(name = "cantidad_recibida")
  protected Integer cantidadDonada = 0;

  @Column(name = "satisfecha")
  private Boolean satisfecha = false;

  protected NecesidadMaterial() {
    // Constructor vacío requerido por JPA
  }

  public NecesidadMaterial(
      Long id,
      Long entidadID,
      Integer nivelDeUrgencia,
      String descripcion,
      Integer cantidadObjetivo,
      String productoSolicitadoID) {
    super(id);
    this.entidadID = entidadID;
    if (nivelDeUrgencia < 1 || nivelDeUrgencia > 10)
      throw new IllegalArgumentException("El nivel de urgencia debe ser un número entre 1 y 10");
    this.nivelDeUrgencia = nivelDeUrgencia;
    this.descripcion = descripcion;
    this.cantidadObjetivo = cantidadObjetivo;
    this.productoSolicitadoID = productoSolicitadoID;
    this.actualizarSatisfaccion();
  }

  public void setNivelDeUrgencia(Integer nivelDeUrgencia) {
    if (nivelDeUrgencia < 1 || nivelDeUrgencia > 10)
      throw new IllegalArgumentException("El nivel de urgencia debe ser un número entre 1 y 10");
    this.nivelDeUrgencia = nivelDeUrgencia;
  }

  public boolean esDeProducto(String producto) {
    return this.getProductoSolicitadoID().equalsIgnoreCase(producto);
  }

  public boolean estaSatisfecha() {
    return Boolean.TRUE.equals(this.satisfecha);
  }

  abstract public void satisfacer(Integer cantidad);

  protected void actualizarSatisfaccion() {
    this.satisfecha = Objects.requireNonNullElse(this.cantidadDonada, 0) >= this.cantidadObjetivo;
  }
}
