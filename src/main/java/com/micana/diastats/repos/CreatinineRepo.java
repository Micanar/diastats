package com.micana.diastats.repos;

import com.micana.diastats.domain.Creatinine;
import com.micana.diastats.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatinineRepo extends JpaRepository<Creatinine,Integer> {
    Iterable<Creatinine> findByPatient (User patient);
}
