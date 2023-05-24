package com.micana.diastats.repos;

import com.micana.diastats.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
    List<User> findByDoctor(User doctor);
    List<User> findByUsernameContainingIgnoreCase(String username);
    List<User> findByDoctorAndUsernameContainingIgnoreCase(User doctor, String username);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r = 'DOCTOR'")
    List<User> findDoctors();
}