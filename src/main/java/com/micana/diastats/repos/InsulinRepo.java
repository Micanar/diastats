package com.micana.diastats.repos;

import com.micana.diastats.domain.InsulinSynthesis;
import com.micana.diastats.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsulinRepo extends JpaRepository<InsulinSynthesis,Integer> {
    Iterable<InsulinSynthesis> findByPatient (User patient);
}
