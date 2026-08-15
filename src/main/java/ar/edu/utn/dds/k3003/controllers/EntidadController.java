package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/entidades")
public class EntidadController {

    private final Fachada fachada;

    public EntidadController(Fachada fachada) {
        this.fachada = fachada;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<EntidadBeneficaDTO> addEntity(@RequestBody EntidadBeneficaDTO entity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.fachada.agregarEntidad(entity));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<EntidadBeneficaDTO>> getAllEntities() {
        return ResponseEntity.status(HttpStatus.OK).body(this.fachada.obtenerEntidades());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<EntidadBeneficaDTO> getEntityById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.fachada.buscarEntidadPorID(id));
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public ResponseEntity<EntidadBeneficaDTO> editEntity(@PathVariable String id, @RequestBody Map<String, String> request) {
        List<String> camposPermitidos = List.of("razonSocial", "domicilio", "telefono", "correo");

        if (request.isEmpty())
            throw new IllegalArgumentException("Debe indicarse al menos uno de los campos: 'razonSocial', 'domicilio', 'telefono' o 'correo'");
        if (!camposPermitidos.containsAll(request.keySet()))
            throw new IllegalArgumentException("Solo se permiten los campos 'razonSocial', 'domicilio', 'telefono' y 'correo'");

        return ResponseEntity.status(HttpStatus.OK).body(this.fachada.modificarEntidad(
                id,
                request.get("razonSocial"),
                request.get("domicilio"),
                request.get("telefono"),
                request.get("correo")));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAllEntities() {
        this.fachada.eliminarTodasLasEntidades();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
