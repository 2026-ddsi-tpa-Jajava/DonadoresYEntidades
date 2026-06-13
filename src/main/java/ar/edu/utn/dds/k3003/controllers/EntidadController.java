package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAllEntities() {
        this.fachada.eliminarTodasLasEntidades();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
