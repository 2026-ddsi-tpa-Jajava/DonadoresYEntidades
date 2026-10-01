package ar.edu.utn.dds.k3003.tools;

import ar.edu.utn.dds.k3003.Fachada;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EntidadBeneficaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ToolsTest {

  private DonadoresTools donadores;
  private EntidadesTools entidades;
  private NecesidadesTools necesidades;

  @BeforeEach
  void setUp() {
    Fachada fachada = new Fachada();
    donadores = new DonadoresTools(fachada);
    entidades = new EntidadesTools(fachada);
    necesidades = new NecesidadesTools(fachada);
  }

  @Test
  void crearListarYCambiarEstadoDeDonador() {
    DonadorDTO d = donadores.crearDonador("Ana", "Garcia", 30, "a@b.com", "1", "x");
    Assertions.assertEquals(1, donadores.listarDonadores().size());
    Assertions.assertEquals(d.id(), donadores.obtenerDonador(d.id()).id());
    Assertions.assertTrue(donadores.puedeDonar(d.id()));

    DonadorDTO baneado = donadores.cambiarEstadoDonador(d.id(), "baneado");
    Assertions.assertEquals(EstadoDonadorEnum.BANEADO, baneado.estado());
    Assertions.assertFalse(donadores.puedeDonar(d.id()));
    Assertions.assertThrows(IllegalArgumentException.class, () -> donadores.cambiarEstadoDonador(d.id(), "xxx"));
    Assertions.assertThrows(IllegalArgumentException.class, () -> donadores.cambiarEstadoDonador(d.id(), null));
  }

  @Test
  void registrarYListarQuejas() {
    DonadorDTO d = donadores.crearDonador("Ana", "Garcia", 30, null, null, null);
    donadores.registrarQueja(d.id(), "7", "mal estado");
    Assertions.assertEquals(1, donadores.listarQuejas(d.id()).size());
  }

  @Test
  void crudDeEntidades() {
    EntidadBeneficaDTO e = entidades.crearEntidad("Comedor", "Calle 1", "123", "c@c.com");
    Assertions.assertEquals(1, entidades.listarEntidades().size());
    Assertions.assertEquals("Comedor", entidades.obtenerEntidad(e.id()).razonSocial());
    EntidadBeneficaDTO actualizada = entidades.actualizarEntidad(e.id(), null, "Calle 2", null, null);
    Assertions.assertEquals("Calle 2", actualizada.domicilio());
    Assertions.assertEquals("Comedor", actualizada.razonSocial());
  }

  @Test
  void tipoDeNecesidadInvalidoSeRechaza() {
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> necesidades.registrarNecesidad("1", "p", 5, 10, "otro", "d"));
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> necesidades.registrarNecesidad("1", "p", 5, 10, null, "d"));
  }

  @Test
  void flujoDeNecesidades() {
    EntidadBeneficaDTO e = entidades.crearEntidad("Comedor", null, null, null);
    var n = necesidades.registrarNecesidad(e.id(), "prod1", 8, 10, "extraordinaria", "arroz");
    Assertions.assertEquals(n.id(), necesidades.obtenerNecesidad(n.id()).id());
    Assertions.assertEquals(1, necesidades.listarNecesidadesInsatisfechasPorProducto("prod1").size());
    necesidades.satisfacerNecesidad(n.id(), 10);
    Assertions.assertTrue(necesidades.listarNecesidadesInsatisfechasPorProducto("prod1").isEmpty());
  }
}
