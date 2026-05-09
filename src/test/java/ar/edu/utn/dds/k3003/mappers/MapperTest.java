package ar.edu.utn.dds.k3003.mappers;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.*;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.InsigniaDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.MisionDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.TipoMisionEnum;
import ar.edu.utn.dds.k3003.model.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class MapperTest {

  private DonadorDTO donadorDTO;
  private EntidadBeneficaDTO entidadBeneficaDTO;
  private NecesidadMaterialDTO necesidadMaterialDTO;
  private QuejaDTO quejaDTO;
  private InsigniaDTO insigniaDTO;
  private MisionDTO misionDTO;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    donadorDTO = new DonadorDTO(
            "donor-id",
            "John",
            "Doe",
            30,
            "johndoe@example.com",
            "12345678",
            "123 Fake Street",
            EstadoDonadorEnum.SOSPECHOSO,
            "category test");

    entidadBeneficaDTO = new EntidadBeneficaDTO(
            "charity-id",
            "Fake Charity S.A.",
            "456 Fake Avenue",
            "+5491112345678",
            "fakecharity@example.com");

    necesidadMaterialDTO = new NecesidadMaterialDTO(
            "need-id",
            entidadBeneficaDTO.id(),
            5,
            "fake need",
            100,
            "product-id",
            TipoNecesidadMaterialEnum.EXTRAORDINARIA);

    quejaDTO = new QuejaDTO(
            "complaint-id",
            "donation-id",
            donadorDTO.id(),
            LocalDate.now(),
            "complaining description");

    insigniaDTO = new InsigniaDTO(
            "badge-id",
            "fake badge",
            "fake badge description"
    );

    misionDTO = new MisionDTO(
            "mission-id",
            "fake mission",
            insigniaDTO.id(),
            CategoriaDonadorEnum.COLABORADOR,
            CategoriaDonadorEnum.OCASIONAL,
            TipoMisionEnum.COMPLETITUD);
  }

  @Test
  void testNuevoDonadorMapper_mapDonadorDTOWithVerifiedStatusAndOccasionalCategory() {
    Donador donador = new NuevoDonadorMapper().map(donadorDTO);

    Assertions.assertEquals(donadorDTO.nombre(), donador.getNombre());
    Assertions.assertEquals(donadorDTO.apellido(), donador.getApellido());
    Assertions.assertEquals(donadorDTO.edad(), donador.getEdad());
    Assertions.assertEquals(donadorDTO.email(), donador.getEmail());
    Assertions.assertEquals(donadorDTO.nroDocumento(), donador.getNroDocumento());
    Assertions.assertEquals(donadorDTO.domicilio(), donador.getDomicilio());
    Assertions.assertEquals(EstadoDonadorEnum.VERIFICADO, donador.getEstado());
    Assertions.assertEquals(CategoriaDonadorEnum.OCASIONAL.name(), donador.getCategoria());
  }

  @Test
  void testDonadorAssembler_mapDonadorDtoToDonadorBackToDonadorDto() {
    DonadorAssembler assembler = new DonadorAssembler();

    Donador donador = assembler.toDomain(donadorDTO);
    DonadorDTO nuevoDonadorDTO = assembler.toDTO(donador);

    Assertions.assertEquals(donadorDTO.nombre(), nuevoDonadorDTO.nombre());
    Assertions.assertEquals(donadorDTO.apellido(), nuevoDonadorDTO.apellido());
    Assertions.assertEquals(donadorDTO.edad(), nuevoDonadorDTO.edad());
  }

  @Test
  void testEntidadBeneficaAssembler_mapEntidadBeneficaDtoToEntidadBeneficaBackToEntidadBeneficaDto() {
    EntidadBeneficaAssembler assembler = new EntidadBeneficaAssembler();

    EntidadBenefica entidadBenefica = assembler.toDomain(entidadBeneficaDTO);
    EntidadBeneficaDTO nuevaEntidadBeneficaDTO = assembler.toDTO(entidadBenefica);

    Assertions.assertEquals(entidadBeneficaDTO.razonSocial(), nuevaEntidadBeneficaDTO.razonSocial());
    Assertions.assertEquals(entidadBeneficaDTO.domicilio(), nuevaEntidadBeneficaDTO.domicilio());
    Assertions.assertEquals(entidadBeneficaDTO.telefono(), nuevaEntidadBeneficaDTO.telefono());
    Assertions.assertEquals(entidadBeneficaDTO.correo(), nuevaEntidadBeneficaDTO.correo());
  }

  @Test
  void testNecesidadMaterialAssembler_mapNecesidadMaterialDtoToNecesidadMaterialBackToNecesidadMaterialDto() {
    NecesidadMaterialAssembler assembler = new NecesidadMaterialAssembler();

    NecesidadMaterial necesidadMaterial = assembler.toDomain(necesidadMaterialDTO);
    NecesidadMaterialDTO nuevaNecesidadMaterialDTO = assembler.toDTO(necesidadMaterial);

    Assertions.assertEquals(necesidadMaterialDTO.entidadID(), nuevaNecesidadMaterialDTO.entidadID());
    Assertions.assertEquals(necesidadMaterialDTO.nivelDeUrgencia(), nuevaNecesidadMaterialDTO.nivelDeUrgencia());
    Assertions.assertEquals(necesidadMaterialDTO.productoSolicitadoID(), nuevaNecesidadMaterialDTO.productoSolicitadoID());
  }

  @Test
  void testQuejaAssembler_mapQuejaDtoToQuejaBackToQuejaDto() {
    QuejaAssembler assembler = new QuejaAssembler();

    Queja queja = assembler.toDomain(quejaDTO);
    QuejaDTO back = assembler.toDTO(queja);

    Assertions.assertEquals(quejaDTO.donadorID(), back.donadorID());
    Assertions.assertEquals(quejaDTO.descripcion(), back.descripcion());
  }

  @Test
  void testInsigniaMapper_mapInsigniaDtoToInsignia() {
    InsigniaMapper mapper = new InsigniaMapper();

    Insignia insignia = mapper.map(insigniaDTO);

    Assertions.assertEquals(insigniaDTO.id(), insignia.getId());
  }

  @Test
  void testMisionMapper_mapMisionDtoToMision() {
    MisionMapper mapper = new MisionMapper();

    Mision mision = mapper.map(misionDTO);

    Assertions.assertEquals(misionDTO.id(), mision.getId());
  }

  @Test
  void testDonadorStatsTransformerAndDonadorStatsMapperWorks() {
    Donador donador = new DonadorAssembler().toDomain(donadorDTO);
    Mision mision = new MisionMapper().map(misionDTO);
    List<Insignia> insignias = List.of(new InsigniaMapper().map(insigniaDTO));

    DonadorStatsTransformer transformer = new DonadorStatsTransformer();

    DonadorStats stats = transformer.crearDonadorStatsCon(donador, mision, insignias);

    Assertions.assertEquals(misionDTO.id(), stats.getMisionActualID());
    Assertions.assertEquals(insignias.size(), stats.getInsigniasID().size());

    DonadorStatsDTOMapper dtoMapper = new DonadorStatsDTOMapper();

    DonadorStatsDTO statsDTO = dtoMapper.map(stats);

    Assertions.assertEquals(stats.getId(), statsDTO.id());
    Assertions.assertEquals(stats.getMisionActualID(), statsDTO.misionActualID());
  }
}
