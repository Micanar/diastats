package com.micana.diastats.repos;

import com.micana.diastats.domain.ProductConsumption;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductConsumptionRepository extends CrudRepository<ProductConsumption, Integer> {

    List<ProductConsumption> findByConsumptionDateTimeBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
