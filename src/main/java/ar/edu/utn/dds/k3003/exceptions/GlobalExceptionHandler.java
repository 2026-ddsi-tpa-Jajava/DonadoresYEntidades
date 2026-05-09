package ar.edu.utn.dds.k3003.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DonadorNoEncontradoException.class, EntidadNoEncontradaException.class, NecesidadNoEncontradaException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(RuntimeException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(HttpStatus.NOT_FOUND, req, ex));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(IllegalArgumentException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.of(HttpStatus.BAD_REQUEST, req, ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest req) {
        String message = "Ocurrió un error en nuestros servicios. Por favor intente nuevamente más tarde.";
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, req, message));
    }
}
