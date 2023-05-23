package com.micana.diastats.repos;

import com.micana.diastats.domain.ProductConsumption;
import com.micana.diastats.domain.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface    ProductConsumptionRepository extends CrudRepository<ProductConsumption, Integer> {
    List<ProductConsumption> findByUserAndConsumptionDateTime(User user, LocalDateTime dateTime, Sort sort);

    List<ProductConsumption> findByUserAndConsumptionDateTime(User user, LocalDate date, LocalTime time, Sort sort);

    List<ProductConsumption> findByUserAndConsumptionDate(User user, LocalDate date, Sort sort);

    List<ProductConsumption> findByUser(User user, Sort sort);
}
