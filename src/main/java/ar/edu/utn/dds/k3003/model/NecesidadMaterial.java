package ar.edu.utn.dds.k3003.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
abstract public class NecesidadMaterial extends Persistable {
  private String entidadID;
  private Integer nivelDeUrgencia;
  private String descripcion;
  private Integer cantidadObjetivo;
  private String productoSolicitadoID;

  @Setter(AccessLevel.NONE)
  @Getter(AccessLevel.NONE)
  protected Integer cantidadDonada = 0;

  public NecesidadMaterial(
      String id,
      String entidadID,
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
    return this.cantidadObjetivo <= this.cantidadDonada;
  }

  abstract public void satisfacer(Integer cantidad);
}
