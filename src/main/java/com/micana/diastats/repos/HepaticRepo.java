package com.micana.diastats.repos;

import com.micana.diastats.domain.GlycatedHemoglobinAnalys;
import com.micana.diastats.domain.HepaticIndicators;
import com.micana.diastats.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HepaticRepo extends JpaRepository<HepaticIndicators,Integer> {
    Iterable<HepaticIndicators> findByPatient (User patient);
}
