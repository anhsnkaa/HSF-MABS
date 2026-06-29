package org.mabs.repository;

import org.mabs.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    //Spring security login
    Optional<User> findByEmail(String email);

    //Check existed email(register)
    boolean existsByEmail(String email);

    List<User> findByRole(String role);
}
