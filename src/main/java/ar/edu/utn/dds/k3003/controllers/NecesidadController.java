package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.NecesidadMaterialDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/necesidades")
public class NecesidadController {

    private final Fachada fachada;

    public NecesidadController(Fachada fachada) {
        this.fachada = fachada;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<NecesidadMaterialDTO> addNeed(@RequestBody NecesidadMaterialDTO need) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.fachada.registrarNecesidad(need));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<NecesidadMaterialDTO>> getAllNeedsByProduct(@RequestParam String productoID) {
        return ResponseEntity.status(HttpStatus.OK).body(this.fachada.obtenerNecesidadesInsatisfechasDe(productoID));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{id}/satisfaccion")
    public ResponseEntity<NecesidadMaterialDTO> satisfy(@PathVariable String id, @RequestBody Map<String, Integer> request) {
        Integer quantity = request.get("cantidad");

        if (quantity == null) throw new IllegalArgumentException("El campo 'cantidad' es requerido");
        if (request.size() > 1) throw new IllegalArgumentException("Solo se permite modificar el campo 'cantidad'");

        return ResponseEntity.status(HttpStatus.OK).body(this.fachada.satisfacerNecesidad(id, quantity));
    }
}
