package com.micana.diastats.repos;

import com.micana.diastats.domain.Creatinine;
import com.micana.diastats.domain.FatMetabolismIndicators;
import com.micana.diastats.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FatRepo extends JpaRepository<FatMetabolismIndicators,Integer> {
    Iterable<FatMetabolismIndicators> findByPatient (User patient);
}
