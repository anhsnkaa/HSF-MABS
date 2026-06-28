package org.mabs.repository;

import org.mabs.entity.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;



public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
}
