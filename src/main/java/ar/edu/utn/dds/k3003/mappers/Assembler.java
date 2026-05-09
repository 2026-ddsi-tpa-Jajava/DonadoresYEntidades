package ar.edu.utn.dds.k3003.mappers;

public interface Assembler<Domain, Dto> {
  Domain toDomain(Dto dto);

  Dto toDTO(Domain domain);
}
