package org.mabs.service;

import org.mabs.entity.User;
import org.mabs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Check user status (e.g. if the user status is active or blocked)
        if (user.getStatus() != null && !user.getStatus().equalsIgnoreCase("active")) {
            throw new UsernameNotFoundException("User account is not active");
        }

        // Format role as ROLE_ROLE_NAME (Spring Security standard)
        String roleName = user.getRole();
        if (roleName == null) {
            roleName = "PATIENT";
        }
        String authority = roleName.toUpperCase().startsWith("ROLE_")
                ? roleName.toUpperCase()
                : "ROLE_" + roleName.toUpperCase();

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                Collections.singletonList(new SimpleGrantedAuthority(authority))
        );
    }
}
