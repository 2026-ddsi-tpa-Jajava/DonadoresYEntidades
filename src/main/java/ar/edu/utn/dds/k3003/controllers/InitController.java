package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/db")
public class InitController {

    private final Fachada fachada;

    public InitController(Fachada fachada) {
        this.fachada = fachada;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteAll() {
        this.fachada.eliminarTodasLasQuejas();
        this.fachada.eliminarTodosLosDonadores();
        this.fachada.eliminarTodasLasNecesidades();
        this.fachada.eliminarTodasLasEntidades();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
