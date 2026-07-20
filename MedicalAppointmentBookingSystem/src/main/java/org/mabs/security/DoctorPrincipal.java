package org.mabs.security;

import lombok.RequiredArgsConstructor;
import org.mabs.entity.Doctor;
import org.mabs.entity.User;
import org.mabs.repository.DoctorRepository;
import org.mabs.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DoctorPrincipal {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    public Long resolveDoctorId(Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            throw new IllegalStateException("Tài khoản không phải bác sĩ: " + email);
        }
        Doctor doctor = doctorRepository.findByUserId(user.getId()).orElse(null);
        if (doctor == null) {
            throw new IllegalStateException("Tài khoản không phải bác sĩ: " + email);
        }
        return doctor.getId();
    }
}
