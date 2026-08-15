package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.model.EntidadBenefica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntidadesJpaRepository extends JpaRepository<EntidadBenefica, Long> {}

