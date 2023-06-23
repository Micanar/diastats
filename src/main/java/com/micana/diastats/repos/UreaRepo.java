package com.micana.diastats.repos;

import com.micana.diastats.domain.UreaAnalys;
import com.micana.diastats.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UreaRepo extends JpaRepository<UreaAnalys,Integer> {
    Iterable<UreaAnalys> findByPatient (User patient);
}
