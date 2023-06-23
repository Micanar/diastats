package com.micana.diastats.repos;

import com.micana.diastats.domain.UricAcidAnalys;
import com.micana.diastats.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UricAcidRepo extends JpaRepository<UricAcidAnalys,Integer> {
    Iterable<UricAcidAnalys> findByPatient (User patient);
}
