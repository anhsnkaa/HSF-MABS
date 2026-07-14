package org.mabs.service;

import org.mabs.entity.Medicine;

import java.util.List;

public interface MedicineService {
    List<Medicine> getActiveMedicines();
}
