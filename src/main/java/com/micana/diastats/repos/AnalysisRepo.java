package com.micana.diastats.repos;

import com.micana.diastats.domain.Analysis;
import com.micana.diastats.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisRepo extends JpaRepository<Analysis, Integer> {
    Iterable<Analysis> findByPatient (User patient);
}
