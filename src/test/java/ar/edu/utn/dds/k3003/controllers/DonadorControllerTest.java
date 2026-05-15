package ar.edu.utn.dds.k3003.controllers;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorStatsDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.QuejaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DonadorControllerTest {

  @Mock Fachada fachada;

  DonadorController controller;

  @BeforeEach
  void setUp() {
	controller = new DonadorController(fachada);
  }

  @Test
  void addDonorReturnsCreatedAndDelegatesToFachada() {
	DonadorDTO request = new DonadorDTO(null, "Juan", "Pérez", 30, "juan@example.com", "12345678", "Calle 1", EstadoDonadorEnum.VERIFICADO, CategoriaDonadorEnum.OCASIONAL.name());
	DonadorDTO response = new DonadorDTO("donador-1", "Juan", "Pérez", 30, "juan@example.com", "12345678", "Calle 1", EstadoDonadorEnum.VERIFICADO, CategoriaDonadorEnum.OCASIONAL.name());

	when(fachada.agregarDonador(request)).thenReturn(response);

	var result = controller.addDonor(request);

	Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
	Assertions.assertEquals(response, result.getBody());
	verify(fachada).agregarDonador(request);
  }

  @Test
  void getAllDonorsReturnsOkWithFacadeResponse() {
	List<DonadorDTO> response = List.of(
		new DonadorDTO("donador-1", "Juan", "Pérez", 30, "juan@example.com", "12345678", "Calle 1", EstadoDonadorEnum.VERIFICADO, CategoriaDonadorEnum.OCASIONAL.name())
	);

	when(fachada.obtenerDonadores()).thenReturn(response);

	var result = controller.getAllDonors();

	Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
	Assertions.assertEquals(response, result.getBody());
	verify(fachada).obtenerDonadores();
  }

  @Test
  void getDonorByIdReturnsOkWithFacadeResponse() {
	DonadorDTO response = new DonadorDTO("donador-1", "Juan", "Pérez", 30, "juan@example.com", "12345678", "Calle 1", EstadoDonadorEnum.VERIFICADO, CategoriaDonadorEnum.OCASIONAL.name());

	when(fachada.buscarDonadorPorID("donador-1")).thenReturn(response);

	var result = controller.getDonorById("donador-1");

	Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
	Assertions.assertEquals(response, result.getBody());
	verify(fachada).buscarDonadorPorID("donador-1");
  }

  @Test
  void modifyStatusReturnsOkAndDelegatesParsedEnum() {
	DonadorDTO response = new DonadorDTO("donador-1", "Juan", "Pérez", 30, "juan@example.com", "12345678", "Calle 1", EstadoDonadorEnum.SOSPECHOSO, CategoriaDonadorEnum.OCASIONAL.name());

	when(fachada.modificarEstado("donador-1", EstadoDonadorEnum.SOSPECHOSO)).thenReturn(response);

	var result = controller.modifyStatus("donador-1", Map.of("estado", "SOSPECHOSO"));

	Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
	Assertions.assertEquals(response, result.getBody());
	verify(fachada).modificarEstado("donador-1", EstadoDonadorEnum.SOSPECHOSO);
  }

  @Test
  void modifyStatusThrowsWhenEstadoIsMissing() {
	IllegalArgumentException exception = Assertions.assertThrows(
		IllegalArgumentException.class,
		() -> controller.modifyStatus("donador-1", Map.of("otraCosa", "VERIFICADO")));

	Assertions.assertEquals("El campo 'estado' es requerido", exception.getMessage());
  }

  @Test
  void modifyStatusThrowsWhenEstadoIsInvalid() {
	IllegalArgumentException exception = Assertions.assertThrows(
		IllegalArgumentException.class,
		() -> controller.modifyStatus("donador-1", Map.of("estado", "INVALIDO")));

	Assertions.assertEquals("El campo 'estado' debe ser uno de los siguientes valores: 'VERIFICADO', 'SOSPECHOSO' o 'BANEADO'", exception.getMessage());
  }

  @Test
  void modifyStatusThrowsWhenMoreThanOneField() {
	IllegalArgumentException exception = Assertions.assertThrows(
		IllegalArgumentException.class,
		() -> controller.modifyStatus("donador-1", Map.of("estado", "VERIFICADO", "otroCampo", "valor")));

	Assertions.assertEquals("Solo se permite modificar el campo 'estado'", exception.getMessage());
  }

  @Test
  void modifyCategoryReturnsOkAndDelegatesParsedCategory() {
	DonadorDTO response = new DonadorDTO("donador-1", "Juan", "Pérez", 30, "juan@example.com", "12345678", "Calle 1", EstadoDonadorEnum.VERIFICADO, CategoriaDonadorEnum.COLABORADOR.name());

	when(fachada.modifcarCategoria("donador-1", CategoriaDonadorEnum.COLABORADOR.name())).thenReturn(response);

	var result = controller.modifyCategory("donador-1", Map.of("categoria", "COLABORADOR"));

	Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
	Assertions.assertEquals(response, result.getBody());
	verify(fachada).modifcarCategoria("donador-1", CategoriaDonadorEnum.COLABORADOR.name());
  }

  @Test
  void modifyCategoryThrowsWhenCategoriaIsMissing() {
	IllegalArgumentException exception = Assertions.assertThrows(
		IllegalArgumentException.class,
		() -> controller.modifyCategory("donador-1", Map.of("otraCosa", "OCASIONAL")));

	Assertions.assertEquals("El campo 'categoria' es requerido", exception.getMessage());
  }

  @Test
  void modifyCategoryThrowsWhenMoreThanOneField() {
	IllegalArgumentException exception = Assertions.assertThrows(
		IllegalArgumentException.class,
		() -> controller.modifyCategory("donador-1", Map.of("categoria", "OCASIONAL", "otroCampo", "valor")));

	Assertions.assertEquals("Solo se permite modificar el campo 'categoria'", exception.getMessage());
  }

  @Test
  void modifyCategoryThrowsWhenCategoriaIsInvalid() {
	IllegalArgumentException exception = Assertions.assertThrows(
		IllegalArgumentException.class,
		() -> controller.modifyCategory("donador-1", Map.of("categoria", "INVALIDA")));

	Assertions.assertEquals("El campo 'categoria' debe ser uno de los siguientes valores: 'OCASIONAL', 'COLABORADOR','TRANSFORMADOR', 'SALVADOR' o 'REVOLUCIONARIO'", exception.getMessage());
  }

  @Test
  void addComplaintReturnsOkAndDelegatesComplaintWithTodayDate() {
	QuejaDTO response = new QuejaDTO("queja-1", "donacion-1", "donador-1", LocalDate.now(), "descripcion");

	when(fachada.agregarQueja(org.mockito.ArgumentMatchers.any(QuejaDTO.class))).thenReturn(response);

	var result = controller.addComplaint("donador-1", Map.of("donacionID", "donacion-1", "descripcion", "descripcion"));

	Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
	Assertions.assertEquals(response, result.getBody());
	verify(fachada).agregarQueja(org.mockito.ArgumentMatchers.argThat(queja ->
		queja != null
			&& "donacion-1".equals(queja.donacionID())
			&& "donador-1".equals(queja.donadorID())
			&& "descripcion".equals(queja.descripcion())
			&& queja.fecha() != null));
  }

  @Test
  void addComplaintThrowsWhenDonacionIDIsMissing() {
	IllegalArgumentException exception = Assertions.assertThrows(
		IllegalArgumentException.class,
		() -> controller.addComplaint("donador-1", Map.of("descripcion", "descripcion")));

	Assertions.assertEquals("El campo 'donacionID' es requerido", exception.getMessage());
  }

  @Test
  void addComplaintThrowsWhenDescripcionIsMissing() {
	IllegalArgumentException exception = Assertions.assertThrows(
		IllegalArgumentException.class,
		() -> controller.addComplaint("donador-1", Map.of("donacionID", "donacion-1")));

	Assertions.assertEquals("El campo 'descripcion' es requerido", exception.getMessage());
  }

  @Test
  void addComplaintThrowsWhenMoreThanTwoFields() {
	IllegalArgumentException exception = Assertions.assertThrows(
		IllegalArgumentException.class,
		() -> controller.addComplaint("donador-1", Map.of("donacionID", "donacion-1", "descripcion", "descripcion", "otroCampo", "valor")));

	Assertions.assertEquals("Solo se permiten los campos 'donacionID' y 'descripcion'", exception.getMessage());
  }

  @Test
  void canDonateReturnsOkWithFacadeResponse() {
	when(fachada.puedeDonar("donador-1")).thenReturn(true);

	var result = controller.canDonate("donador-1");

	Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
	Assertions.assertEquals(Map.of("puedeDonar", true), result.getBody());
	verify(fachada).puedeDonar("donador-1");
  }

  @Test
  void getDonorStatsReturnsOkWithFacadeResponse() {
	DonadorStatsDTO response = new DonadorStatsDTO("donador-1", "Juan", "Pérez", 30, EstadoDonadorEnum.VERIFICADO, CategoriaDonadorEnum.OCASIONAL.name(), null, List.of("insignia-1"));

	when(fachada.estadisticasDonador("donador-1")).thenReturn(response);

	var result = controller.getDonorStats("donador-1");

	Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
	Assertions.assertEquals(response, result.getBody());
	verify(fachada).estadisticasDonador("donador-1");
  }

  @Test
  void getComplaintsReturnsOkWithFacadeResponse() {
	List<QuejaDTO> response = List.of(new QuejaDTO("queja-1", "donacion-1", "donador-1", LocalDate.now(), "descripcion"));

	when(fachada.obtenerQuejasDe("donador-1")).thenReturn(response);

	var result = controller.getComplaints("donador-1");

	Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
	Assertions.assertEquals(response, result.getBody());
	verify(fachada).obtenerQuejasDe("donador-1");
  }
}
