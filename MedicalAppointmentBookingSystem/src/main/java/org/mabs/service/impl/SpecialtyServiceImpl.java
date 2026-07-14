package org.mabs.service.impl;

import lombok.RequiredArgsConstructor;
import org.mabs.entity.Specialty;
import org.mabs.repository.SpecialtyRepository;
import org.mabs.service.SpecialtyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyRepository repository;

    @Override
    public List<Specialty> getALlSpecialties() {
        return repository.findAll();
    }

    @Override
    public Specialty createSpecialty(Specialty specialty) {
        return repository.save(specialty);
    }

    @Override
    public Specialty updateSpecialty(Specialty specialty) {
        return repository.save(specialty);
    }

    @Override
    public Specialty findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void deleteSpecialty(Specialty specialty) {
        repository.delete(specialty);
    }
}
