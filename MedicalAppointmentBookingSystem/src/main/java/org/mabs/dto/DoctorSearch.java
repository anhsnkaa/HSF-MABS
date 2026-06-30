package org.mabs.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DoctorSearch {
    private Long specialtyId;
    private String keyword;
    private String sortBy;
}
