package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EntidadControllerTest {

  @Mock Fachada fachada;

  EntidadController controller;

  @BeforeEach
  void setUp() {
	controller = new EntidadController(fachada);
  }

  @Test
  void addEntityReturnsCreatedAndDelegatesToFachada() {
	EntidadBeneficaDTO request = new EntidadBeneficaDTO(null, "Fundación Test", "Calle 1", "123456", "test@example.com");
	EntidadBeneficaDTO response = new EntidadBeneficaDTO("entidad-1", "Fundación Test", "Calle 1", "123456", "test@example.com");

	when(fachada.agregarEntidad(request)).thenReturn(response);

	var result = controller.addEntity(request);

	Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
	Assertions.assertEquals(response, result.getBody());
	verify(fachada).agregarEntidad(request);
  }

  @Test
  void getAllEntitiesReturnsOkWithFacadeResponse() {
	List<EntidadBeneficaDTO> response = List.of(
		new EntidadBeneficaDTO("entidad-1", "Fundación Test", "Calle 1", "123456", "test@example.com")
	);

	when(fachada.obtenerEntidades()).thenReturn(response);

	var result = controller.getAllEntities();

	Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
	Assertions.assertEquals(response, result.getBody());
	verify(fachada).obtenerEntidades();
  }

  @Test
  void getEntityByIdReturnsOkWithFacadeResponse() {
	EntidadBeneficaDTO response = new EntidadBeneficaDTO("entidad-1", "Fundación Test", "Calle 1", "123456", "test@example.com");

	when(fachada.buscarEntidadPorID("entidad-1")).thenReturn(response);

	var result = controller.getEntityById("entidad-1");

	Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
	Assertions.assertEquals(response, result.getBody());
	verify(fachada).buscarEntidadPorID("entidad-1");
  }
}
