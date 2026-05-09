package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.NecesidadMaterialDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.TipoNecesidadMaterialEnum;
import ar.edu.utn.dds.k3003.exceptions.NecesidadNoEncontradaException;
import ar.edu.utn.dds.k3003.model.NecesidadMaterial;
import ar.edu.utn.dds.k3003.repositories.NecesidadesRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class FachadaUnitTest {

  private Fachada fachada;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    fachada = new Fachada();
  }

  @Test
  void testFachada_obtenerDonadorThrowsExceptionWhenDonadorIdIsBlankOrNull() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> fachada.buscarDonadorPorID(null));
    Assertions.assertThrows(IllegalArgumentException.class, () -> fachada.buscarDonadorPorID(""));
  }

  @Test
  void testFachada_obtenerEntidadBeneficaThrowsExceptionWhenEntidadBeneficaIdIsBlankOrNull() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> fachada.buscarEntidadPorID(null));
    Assertions.assertThrows(IllegalArgumentException.class, () -> fachada.buscarEntidadPorID(""));
  }

  @Test
  void testFachada_satisfacerNecesidadExitosa() throws NoSuchFieldException, IllegalAccessException {
    NecesidadMaterialDTO necesidadMaterialDTO = new NecesidadMaterialDTO(
            null,
            "charity-id",
            8,
            "need description",
            100,
            "product-id",
            TipoNecesidadMaterialEnum.EXTRAORDINARIA
            );

    NecesidadMaterialDTO necesidadGuardadaDTO = fachada.registrarNecesidad(necesidadMaterialDTO);
    NecesidadMaterialDTO necesidadSatisfechaDTO = fachada.satisfacerNecesidad(necesidadGuardadaDTO.id(), 100);

    // Ingresar al repository para buscar la necesidad en memoria
    Field f = Fachada.class.getDeclaredField("necesidadesRepository");
    f.setAccessible(true);
    NecesidadesRepository repo = (NecesidadesRepository) f.get(fachada);
    NecesidadMaterial necesidadMaterial = repo.findById(necesidadSatisfechaDTO.id())
            .orElseThrow(() -> new NecesidadNoEncontradaException("No existe una necesidad con ese ID"));

    Assertions.assertEquals(necesidadGuardadaDTO.id(), necesidadSatisfechaDTO.id());
    Assertions.assertTrue(necesidadMaterial.estaSatisfecha());
  }

  @Test
  void testFachada_estadisticasDonadorThrowsExceptionWhenFachadaIncentivosIsNotDefined() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> fachada.estadisticasDonador("donor-id"));
  }

  @Test
  void testFachada_obtenerNecesidadesInsatisfechasDeThrowsExceptionWhenProductIdIsBlankOrNull() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> fachada.obtenerNecesidadesInsatisfechasDe(null));
    Assertions.assertThrows(IllegalArgumentException.class, () -> fachada.obtenerNecesidadesInsatisfechasDe(""));
  }
}
