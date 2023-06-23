package com.micana.diastats.repos;

import com.micana.diastats.domain.GlycatedHemoglobinAnalys;
import com.micana.diastats.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlycHemoRepo extends JpaRepository<GlycatedHemoglobinAnalys,Integer> {
    Iterable<GlycatedHemoglobinAnalys> findByPatient (User patient);
}
