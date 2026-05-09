package ar.edu.utn.dds.k3003.model;

public class NecesidadExtraordinaria extends NecesidadMaterial {
    public NecesidadExtraordinaria(String id,
                                   String entidadID,
                                   Integer nivelDeUrgencia,
                                   String descripcion,
                                   Integer cantidadObjetivo,
                                   String productoSolicitadoID) {
        super(id, entidadID, nivelDeUrgencia, descripcion, cantidadObjetivo, productoSolicitadoID);
    }

    public void satisfacer(Integer cantidad) {
        if (cantidad <= 0)
            throw new IllegalArgumentException("La cantidad a donar no puede ser menor o igual a cero");
        this.cantidadDonada += cantidad;
    }
}
