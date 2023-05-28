package com.micana.diastats.repos;

import com.micana.diastats.domain.ProductConsumption;
import com.micana.diastats.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductConsumptionRepository extends CrudRepository<ProductConsumption, Integer> {
    List<ProductConsumption> findByConsumptionDateTime(LocalDateTime dateTime);

    List<ProductConsumption> findByUserAndConsumptionDateTimeOrderByConsumptionDateTimeDesc(User user, LocalDateTime dateTime);

    List<ProductConsumption> findByUserOrderByConsumptionDateTimeDesc(User user);

    List<ProductConsumption> findByUser(User user);
}
