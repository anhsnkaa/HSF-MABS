package org.mabs.specification;

import jakarta.persistence.criteria.Join;
import org.mabs.dto.DoctorSearch;
import org.mabs.entity.Doctor;
import org.mabs.entity.User;
import org.springframework.data.jpa.domain.Specification;

public final class DoctorSpecification {

    private DoctorSpecification() {
    }

    public static Specification<Doctor> from(DoctorSearch request) {
        return Specification.allOf(
                hasSpecialtyId(request.getSpecialtyId()),
                hasKeyword(request.getKeyword())
        );
    }

    private static Specification<Doctor> hasSpecialtyId(Long specialtyId) {
        return (root, query, cb) -> {
            if (specialtyId == null) return cb.conjunction();
            return cb.equal(root.get("specialty").get("id"), specialtyId);
        };
    }

    private static Specification<Doctor> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.isBlank()) return cb.conjunction();
            Join<Doctor, User> userJoin = root.join("user");
            return cb.like(cb.lower(userJoin.get("fullName")), "%" + keyword.toLowerCase() + "%");
        };
    }
}
