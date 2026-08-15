package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.catedra.dtos.donadoresYEntidades.EstadoDonadorEnum;
import ar.edu.utn.dds.k3003.catedra.dtos.incentivos.CategoriaDonadorEnum;
import ar.edu.utn.dds.k3003.model.Donador;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

public class InMemoryRepoTest {

  Donador donador;
  InMemoryDonadoresRepo repo;

  @BeforeEach
  @SneakyThrows
  void setUp() {
    donador= new Donador(
            null,
            "John",
            "Doe",
            30,
            "johndoe@example.com",
            "12345678",
            "123 Fake Street",
            EstadoDonadorEnum.VERIFICADO,
            CategoriaDonadorEnum.OCASIONAL.name());

    repo = new InMemoryDonadoresRepo();
  }

  @Test
  void inMemoryRepo_saveThrowsErrorIfObjectIsNull() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> repo.save(null));
  }

  @Test
  void inMemoryRepo_saveGeneratesIdAndFindsById() {
    Donador donadorGuardado = repo.save(donador);
    Assertions.assertNotNull(donadorGuardado.getId());

    var donadorEncontrado = repo.findById(donadorGuardado.getId());
    Assertions.assertTrue(donadorEncontrado.isPresent());
    Assertions.assertEquals(donadorGuardado.getId(), donadorEncontrado.get().getId());
  }

  @Test
  void inMemoryRepo_findByIdThrowsExceptionWhenIdIsNull() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> repo.findById(null));
  }

  @Test
  void inMemoryRepo_updateThrowsExceptionWhenObjectIsNullOrObjectIdIsNullOrNotInRepo() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> repo.update(null));
    Assertions.assertThrows(IllegalArgumentException.class, () -> repo.update(donador));

    donador.setId(999L);
    Assertions.assertThrows(NoSuchElementException.class, () -> repo.update(donador));
  }

  @Test
  void inMemoryRepo_deleteByIdThrowsExceptionWhenObjectIdIsNullOrNotInRepo() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> repo.deleteById(null));
    Assertions.assertThrows(NoSuchElementException.class, () -> repo.deleteById(999999L));
  }

  @Test
  void inMemoryRepo_deleteByIdWorks() {
    Donador donadorGuardado = repo.save(donador);
    Assertions.assertEquals(1, repo.findAll().size());
    Assertions.assertTrue(repo.findById(donadorGuardado.getId()).isPresent());

    Donador donadorEliminado = repo.deleteById(donadorGuardado.getId());
    Assertions.assertEquals(donadorGuardado.getId(), donadorEliminado.getId());
    Assertions.assertEquals(0, repo.findAll().size());
  }
}
