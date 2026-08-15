package ar.edu.utn.dds.k3003.mappers;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.DonadorDTO;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import ar.edu.utn.dds.k3003.model.Donador;

public class DonadorAssembler implements Assembler<Donador, DonadorDTO> {
  @Override
  public Donador toDomain(DonadorDTO dto) {
    return new Donador(
        IdUtils.parse(dto.id()),
        dto.nombre(),
        dto.apellido(),
        dto.edad(),
        dto.email(),
        dto.nroDocumento(),
        dto.domicilio(),
        dto.estado(),
        dto.categoria());
  }

  @Override
  public DonadorDTO toDTO(Donador domain) {
    return new DonadorDTO(
        IdUtils.stringify(domain.getId()),
        domain.getNombre(),
        domain.getApellido(),
        domain.getEdad(),
        domain.getEmail(),
        domain.getNroDocumento(),
        domain.getDomicilio(),
        domain.getEstado(),
        domain.getCategoria());
  }
}
