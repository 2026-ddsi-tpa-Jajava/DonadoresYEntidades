package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
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

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<NecesidadMaterialDTO> getNeedById(@PathVariable String id) {
        return ResponseEntity.ok(this.fachada.buscarNecesidadPorId(id));
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

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public ResponseEntity<NecesidadMaterialDTO> editNeed(@PathVariable String id, @RequestBody Map<String, String> request) {
        List<String> camposPermitidos = List.of("nivelDeUrgencia", "descripcion", "cantidadObjetivo", "productoSolicitadoID");

        if (request.isEmpty())
            throw new IllegalArgumentException("Debe indicarse al menos uno de los campos: 'nivelDeUrgencia', 'descripcion', 'cantidadObjetivo' o 'productoSolicitadoID'");
        if (!camposPermitidos.containsAll(request.keySet()))
            throw new IllegalArgumentException("Solo se permiten los campos 'nivelDeUrgencia', 'descripcion', 'cantidadObjetivo' y 'productoSolicitadoID'");

        String nivelDeUrgencia = request.get("nivelDeUrgencia");
        String cantidadObjetivo = request.get("cantidadObjetivo");


        return ResponseEntity.status(HttpStatus.OK).body(this.fachada.modificarNecesidad(
                id,
                nivelDeUrgencia != null ? Integer.valueOf(nivelDeUrgencia) : null,
                request.get("descripcion"),
                cantidadObjetivo != null ? Integer.valueOf(cantidadObjetivo) : null,
                request.get("productoSolicitadoID")));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<NecesidadMaterialDTO> deleteNeed(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.fachada.eliminarNecesidad(id));
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAllNeeds() {
        this.fachada.eliminarTodasLasNecesidades();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
