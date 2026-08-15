package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class ModelsTest {

  private Donador donador;
  private EntidadBenefica entidad;
  private Queja queja;
  private NecesidadMaterial necesidad;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    donador= new Donador(
            1L,
            "John",
            "Doe",
            30,
            "johndoe@example.com",
            "12345678",
            "123 Fake Street",
            EstadoDonadorEnum.VERIFICADO,
            "Ocasional");

    entidad = new EntidadBenefica(
            2L,
            "Fake Charity S.A.",
            "456 Fake Avenue",
            "+5491112345678",
            "fakecharity@example.com");

    queja = new Queja(
            null,
            "donation-id",
            donador.getId(),
            LocalDate.now(),
            "complaint description");

    necesidad = new NecesidadExtraordinaria(
            3L,
            entidad.getId(),
            1,
            "need description",
            10,
            "product-id"
    );
  }

  @Test
  void testDonador_puedeDonarTrueIfHasVerifiedStatus() {
    Assertions.assertTrue(donador.puedeDonar());
  }

  @Test
  void testDonador_puedeDonarFalseIfHasBannedStatus() {
    donador.setEstado(EstadoDonadorEnum.BANEADO);
    Assertions.assertFalse(donador.puedeDonar());
  }

  @Test
  void testDonador_agregarQuejaTransitionsToSuspiciousAndBannedAndGetQuejasReturnsAll() {
    for (int i = 0; i < 5; i++) {
      donador.agregarQueja();
    }

    Assertions.assertEquals(EstadoDonadorEnum.SOSPECHOSO, donador.getEstado());

    int intentos = 1000;
    int positivos = 0;
    for(int i = 0; i < intentos; i++) {
        if (donador.puedeDonar())
            positivos++;
    }
    float promedio = (float) positivos / intentos;
    System.out.println("Average probability of being able to donate: " + promedio);
    Assertions.assertTrue(promedio > 0.4 && promedio < 0.6);


    for (int i = 5; i < 10; i++) {
      donador.agregarQueja();
    }

    Assertions.assertEquals(EstadoDonadorEnum.BANEADO, donador.getEstado());
  }

  @Test
  void testDonador_getHistorialEstadosReturnsAllStatus() {
    donador.setEstado(EstadoDonadorEnum.SOSPECHOSO);
    donador.setEstado(EstadoDonadorEnum.VERIFICADO);
    donador.setEstado(EstadoDonadorEnum.BANEADO);

    Assertions.assertEquals(4, donador.getHistorialEstados().size());
    Assertions.assertEquals(EstadoDonadorEnum.VERIFICADO, donador.getHistorialEstados().get(0));
    Assertions.assertEquals(EstadoDonadorEnum.SOSPECHOSO, donador.getHistorialEstados().get(1));
    Assertions.assertEquals(EstadoDonadorEnum.VERIFICADO, donador.getHistorialEstados().get(2));
    Assertions.assertEquals(EstadoDonadorEnum.BANEADO, donador.getHistorialEstados().get(3));
  }

  @Test
  void testNecesidadMaterial_setNivelDeUrgenciaThrowsExceptionWhenEmergencyLevelNotBetween1And10() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> necesidad.setNivelDeUrgencia(0));
    Assertions.assertThrows(IllegalArgumentException.class, () -> necesidad.setNivelDeUrgencia(11));

    necesidad.setNivelDeUrgencia(9);
    Assertions.assertEquals(9, necesidad.getNivelDeUrgencia());

    Assertions.assertThrows(
            IllegalArgumentException.class, () -> new NecesidadExtraordinaria(3L, 2L, 0, "need description", 5, "product-id"));
    Assertions.assertThrows(
            IllegalArgumentException.class, () -> new NecesidadExtraordinaria(3L, 2L, 11, "need description", 5, "product-id"));
  }

  @Test
  void testNecesidadMaterial_satisfacerThrowsExceptionWhenQuantityIsBelow1() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> necesidad.satisfacer(0));
    necesidad.satisfacer(10);
    Assertions.assertTrue(necesidad.estaSatisfecha());
  }

  @Test
  void testNecesidadMaterial_esDeProductoIsCaseInsensitiveAndSatisfiesNeed() {
    Assertions.assertTrue(necesidad.esDeProducto("product-id"));
    Assertions.assertTrue(necesidad.esDeProducto("PRODUCT-ID"));

    Assertions.assertFalse(necesidad.estaSatisfecha());

    necesidad.satisfacer(5);
    Assertions.assertFalse(necesidad.estaSatisfecha());

    necesidad.satisfacer(5);
    Assertions.assertTrue(necesidad.estaSatisfecha());
  }

  @Test
  void testQueja_esDeDonador() {
    Assertions.assertTrue(queja.esDeDonador(donador));
  }
}
