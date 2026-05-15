package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.NecesidadMaterialDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.TipoNecesidadMaterialEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NecesidadControllerTest {

  @Mock Fachada fachada;

  NecesidadController controller;

  @BeforeEach
  void setUp() {
	controller = new NecesidadController(fachada);
  }

  @Test
  void addNeedReturnsCreatedAndDelegatesToFachada() {
	NecesidadMaterialDTO request = new NecesidadMaterialDTO(null, "entidad-1", 2, "Descripción", 10, "producto-1", TipoNecesidadMaterialEnum.RECURRENTE);
	NecesidadMaterialDTO response = new NecesidadMaterialDTO("necesidad-1", "entidad-1", 2, "Descripción", 10, "producto-1", TipoNecesidadMaterialEnum.RECURRENTE);

	when(fachada.registrarNecesidad(request)).thenReturn(response);

	var result = controller.addNeed(request);

	Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
	Assertions.assertEquals(response, result.getBody());
	verify(fachada).registrarNecesidad(request);
  }

  @Test
  void getAllNeedsByProductReturnsOkWithFacadeResponse() {
	List<NecesidadMaterialDTO> response = List.of(
		new NecesidadMaterialDTO("necesidad-1", "entidad-1", 2, "Descripción", 10, "producto-1", TipoNecesidadMaterialEnum.RECURRENTE)
	);

	when(fachada.obtenerNecesidadesInsatisfechasDe("producto-1")).thenReturn(response);

	var result = controller.getAllNeedsByProduct("producto-1");

	Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
	Assertions.assertEquals(response, result.getBody());
	verify(fachada).obtenerNecesidadesInsatisfechasDe("producto-1");
  }

  @Test
  void satisfyReturnsOkAndDelegatesCantidadToFachada() {
	NecesidadMaterialDTO response = new NecesidadMaterialDTO("necesidad-1", "entidad-1", 2, "Descripción", 10, "producto-1", TipoNecesidadMaterialEnum.RECURRENTE);

	when(fachada.satisfacerNecesidad("necesidad-1", 5)).thenReturn(response);

	var result = controller.satisfy("necesidad-1", Map.of("cantidad", 5));

	Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
	Assertions.assertEquals(response, result.getBody());
	verify(fachada).satisfacerNecesidad("necesidad-1", 5);
  }

  @Test
  void satisfyThrowsWhenCantidadIsMissing() {
	IllegalArgumentException exception = Assertions.assertThrows(
		IllegalArgumentException.class,
		() -> controller.satisfy("necesidad-1", Map.of("otraCosa", 5)));

	Assertions.assertEquals("El campo 'cantidad' es requerido", exception.getMessage());
  }

  @Test
  void satisfyThrowsWhenRequestHasExtraFields() {
	IllegalArgumentException exception = Assertions.assertThrows(
		IllegalArgumentException.class,
		() -> controller.satisfy("necesidad-1", Map.of("cantidad", 5, "otraCosa", 10)));

	Assertions.assertEquals("Solo se permite modificar el campo 'cantidad'", exception.getMessage());
  }
}
