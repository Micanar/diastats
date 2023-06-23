package com.micana.diastats.repos;

import com.micana.diastats.domain.InsulinSynthesis;
import com.micana.diastats.domain.UreaAnalys;
import com.micana.diastats.domain.UrineAnalysis;
import com.micana.diastats.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UreaAnalysRepo extends JpaRepository<UrineAnalysis,Integer> {
    Iterable<UrineAnalysis> findByPatient (User patient);
}
