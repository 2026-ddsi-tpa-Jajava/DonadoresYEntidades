package ar.edu.utn.dds.k3003.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "Queja")
public class Queja extends PersistableEntity {
  @Column(name = "donacion_id")
  private String donacionID;

  @Column(name = "donador_id")
  private Long donadorID;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "donador_id", referencedColumnName = "id", insertable = false, updatable = false)
  private Donador donador;

  @Column(name = "fecha")
  private LocalDateTime fecha;

  @Column(name = "motivo")
  private String descripcion;

  public Queja() {
    // Constructor vacío requerido por JPA
  }

  public Queja(
      Long id, String donacionID, Long donadorID, LocalDate fecha, String descripcion) {
    this(id, donacionID, donadorID, fecha != null ? fecha.atStartOfDay() : null, descripcion);
  }

  public Queja(
      Long id, String donacionID, Long donadorID, LocalDateTime fecha, String descripcion) {
    super(id);
    this.donacionID = donacionID;
    this.donadorID = donadorID;
    this.fecha = fecha;
    this.descripcion = descripcion;
  }

  public boolean esDeDonador(Donador donador) {
    return Objects.equals(this.getDonadorID(), donador.getId());
  }
}
