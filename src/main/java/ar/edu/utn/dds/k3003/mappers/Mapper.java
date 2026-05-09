package ar.edu.utn.dds.k3003.mappers;

public interface Mapper<From, To> {
  To map(From obj);
}
