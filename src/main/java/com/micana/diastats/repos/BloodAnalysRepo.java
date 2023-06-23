package com.micana.diastats.repos;

import com.micana.diastats.domain.Analysis;
import com.micana.diastats.domain.BloodAnalysis;
import com.micana.diastats.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloodAnalysRepo extends JpaRepository<BloodAnalysis,Integer> {
    Iterable<BloodAnalysis> findByPatient (User patient);
}
