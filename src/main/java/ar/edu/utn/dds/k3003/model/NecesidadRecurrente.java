package ar.edu.utn.dds.k3003.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("recurrente")
public class NecesidadRecurrente extends NecesidadMaterial {
    public NecesidadRecurrente() {
        // Constructor vacío requerido por JPA
    }

    public NecesidadRecurrente(String id,
                               String entidadID,
                               Integer nivelDeUrgencia,
                               String descripcion,
                               Integer cantidadObjetivo,
                               String productoSolicitadoID) {
        super(id, entidadID, nivelDeUrgencia, descripcion, cantidadObjetivo, productoSolicitadoID);
    }

    public void satisfacer(Integer cantidad) {
        if (cantidad < this.getCantidadObjetivo())
            throw new IllegalArgumentException("La cantidad a donar no puede ser menor a la cantidad objetivo");
        this.cantidadDonada += cantidad;
        this.actualizarSatisfaccion();
    }
}
