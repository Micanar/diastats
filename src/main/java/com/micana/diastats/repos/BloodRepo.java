package com.micana.diastats.repos;


import com.micana.diastats.domain.Blood_sugar;
import com.micana.diastats.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface BloodRepo extends CrudRepository<Blood_sugar, Integer> {
    Iterable<Blood_sugar> findByPatient(User patient);
}
