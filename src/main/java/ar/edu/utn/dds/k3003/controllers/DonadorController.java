package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorStatsDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/donadores")
public class DonadorController {

    private final Fachada fachada;

    public DonadorController(Fachada fachada) {
        this.fachada = fachada;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<DonadorDTO> addDonor(@RequestBody DonadorDTO donadorDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.fachada.agregarDonador(donadorDTO));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<DonadorDTO>> getAllDonors() {
        return ResponseEntity.status(HttpStatus.OK).body(this.fachada.obtenerDonadores());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<DonadorDTO> getDonorById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.fachada.buscarDonadorPorID(id));
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}/estado")
    public ResponseEntity<DonadorDTO> modifyStatus(@PathVariable String id, @RequestBody Map<String, String> request) {
        String status = request.get("estado");
        if (status == null) throw new IllegalArgumentException("El campo 'estado' es requerido");
        if (request.size() > 1) throw new IllegalArgumentException("Solo se permite modificar el campo 'estado'");
        try {
            return ResponseEntity.status(HttpStatus.OK).body(this.fachada.modificarEstado(id, EstadoDonadorEnum.valueOf(status)));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El campo 'estado' debe ser uno de los siguientes valores: 'VERIFICADO', 'SOSPECHOSO' o 'BANEADO'");
        }
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}/categoria")
    public ResponseEntity<DonadorDTO> modifyCategory(@PathVariable String id, @RequestBody Map<String, String> request) {
        String category = request.get("categoria");

        if (category == null) throw new IllegalArgumentException("El campo 'categoria' es requerido");
        if (request.size() > 1) throw new IllegalArgumentException("Solo se permite modificar el campo 'categoria'");

        try {
            return ResponseEntity.status(HttpStatus.OK).body(this.fachada.modificarCategoria(id, CategoriaDonadorEnum.valueOf(category).name()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("El campo 'categoria' debe ser uno de los siguientes valores: 'OCASIONAL', 'COLABORADOR','TRANSFORMADOR', 'SALVADOR' o 'REVOLUCIONARIO'");
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/puede-donar")
    public ResponseEntity<Map<String, Boolean>> canDonate(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("puedeDonar", this.fachada.puedeDonar(id)));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/estadisticas")
    public ResponseEntity<DonadorStatsDTO> getDonorStats(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.fachada.estadisticasDonador(id));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{id}/quejas")
    public ResponseEntity<QuejaDTO> addComplaint(@PathVariable String id, @RequestBody Map<String, String> request) {

        String donationId = request.get("donacionID");
        String description = request.get("descripcion");

        if (donationId == null) throw new IllegalArgumentException("El campo 'donacionID' es requerido");
        if (description == null) throw new IllegalArgumentException("El campo 'descripcion' es requerido");
        if (request.size() > 2)
            throw new IllegalArgumentException("Solo se permiten los campos 'donacionID' y 'descripcion'");

        QuejaDTO complaint = new QuejaDTO(null, donationId, id, LocalDate.now(), description);
        return ResponseEntity.status(HttpStatus.OK).body(this.fachada.agregarQueja(complaint));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}/quejas")
    public ResponseEntity<List<QuejaDTO>> getComplaints(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.fachada.obtenerQuejasDe(id));
    }
}
