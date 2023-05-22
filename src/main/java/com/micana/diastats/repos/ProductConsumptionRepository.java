package com.micana.diastats.repos;

import com.micana.diastats.domain.Blood_sugar;
import com.micana.diastats.domain.ProductConsumption;
import com.micana.diastats.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductConsumptionRepository extends JpaRepository<ProductConsumption, Integer> {
    Iterable<ProductConsumption> findByUserOrderByConsumptionDateAscConsumptionTimeAsc(User user);
}
