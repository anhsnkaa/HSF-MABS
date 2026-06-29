package org.mabs.service;

import org.mabs.entity.Specialty;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SpecialtyService {

    List<Specialty> getALlSpecialties();

    Specialty createSpecialty(Specialty specialty);
}
