package org.mabs.service;

import org.mabs.entity.Specialty;

import java.util.List;

public interface SpecialtyService {

    List<Specialty> getALlSpecialties();

    Specialty createSpecialty(Specialty specialty);

    Specialty updateSpecialty(Specialty specialty);

    void deleteSpecialty(Long id);

    Specialty findById(Long id);
}
