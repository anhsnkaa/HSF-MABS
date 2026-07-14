package org.mabs.service.impl;

import lombok.*;
import org.mabs.entity.Medicine;
import org.mabs.repository.MedicineRepository;
import org.mabs.service.MedicineService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository repo;

    @Override
    public List<Medicine> getActiveMedicines() {
        List<Medicine> all = repo.findAll();
        List<Medicine> active = new ArrayList<>();
        for (Medicine m : all) {
            if (Boolean.TRUE.equals(m.getIsActive())) {
                active.add(m);
            }
        }
        Collections.sort(active, new Comparator<Medicine>() {
            @Override
            public int compare(Medicine a, Medicine b) {
                return a.getName().compareTo(b.getName());
            }
        });
        return active;
    }
}
