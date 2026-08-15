package ar.edu.utn.dds.k3003;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.NecesidadMaterialDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.TipoNecesidadMaterialEnum;
import ar.edu.utn.dds.k3003.clients.DonacionesApiClient;
import ar.edu.utn.dds.k3003.clients.IncentivosApiClient;
import ar.edu.utn.dds.k3003.clients.LogisticaApiClient;
import ar.edu.utn.dds.k3003.exceptions.NecesidadNoEncontradaException;
import ar.edu.utn.dds.k3003.model.NecesidadMaterial;
import ar.edu.utn.dds.k3003.repositories.InMemoryDonadoresRepo;
import ar.edu.utn.dds.k3003.repositories.InMemoryEntidadesRepo;
import ar.edu.utn.dds.k3003.repositories.InMemoryNecesidadesRepo;
import ar.edu.utn.dds.k3003.repositories.InMemoryQuejasRepo;
import ar.edu.utn.dds.k3003.repositories.NecesidadesRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FachadaUnitTest {

  @Mock private DonacionesApiClient donacionesApiClient;
  @Mock private IncentivosApiClient incentivosApiClient;
  @Mock private LogisticaApiClient logisticaApiClient;

  private Fachada fachada;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    fachada = new Fachada(
        new InMemoryDonadoresRepo(),
        new InMemoryEntidadesRepo(),
        new InMemoryQuejasRepo(),
        new InMemoryNecesidadesRepo(),
        incentivosApiClient,
        donacionesApiClient,
        logisticaApiClient);
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
    when(donacionesApiClient.esProductoValido(anyString())).thenReturn(true);
    when(logisticaApiClient.cuantoStockHayDe(anyString())).thenReturn(0);

    NecesidadMaterialDTO necesidadMaterialDTO = new NecesidadMaterialDTO(
            null,
            "1",
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
    NecesidadMaterial necesidadMaterial = repo.findById(Long.valueOf(necesidadSatisfechaDTO.id()))
            .orElseThrow(() -> new NecesidadNoEncontradaException("No existe una necesidad con ese ID"));

    Assertions.assertEquals(necesidadGuardadaDTO.id(), necesidadSatisfechaDTO.id());
    Assertions.assertTrue(necesidadMaterial.estaSatisfecha());
  }

  @Disabled
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
