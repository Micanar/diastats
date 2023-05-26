package com.micana.diastats.repos;

import com.micana.diastats.domain.Blood_sugar;
import com.micana.diastats.domain.ProductConsumption;
import com.micana.diastats.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ProductConsumptionRepository extends CrudRepository<ProductConsumption, Integer> {
    List<ProductConsumption> findByConsumptionDate(LocalDate date);


    List<ProductConsumption> findByUserAndConsumptionDateOrderByConsumptionDateDesc(User user, LocalDate date);

    List<ProductConsumption> findByUserOrderByConsumptionDateDesc(User user);

    List<ProductConsumption> findByUser(User user);
}
