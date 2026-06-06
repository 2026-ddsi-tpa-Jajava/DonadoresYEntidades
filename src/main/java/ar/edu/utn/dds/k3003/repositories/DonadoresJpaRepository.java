package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.Donador;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio JPA para la entidad Donador. Proporciona operaciones CRUD contra la base de datos
 * usando Hibernate.
 */
public interface DonadoresJpaRepository extends JpaRepository<Donador, String> {

}

