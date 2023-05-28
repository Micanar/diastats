package com.micana.diastats.repos;

import com.micana.diastats.domain.Blood_sugar;
import com.micana.diastats.domain.ProductConsumption;

import java.util.List;

public interface ProductConsumptionSortingService {
    List<ProductConsumption> sortProductConsumptionByDateTime(Iterable<ProductConsumption> productConsumptions);
    List<ProductConsumption> sortProductConsumptionByDateTimeChart(Iterable<ProductConsumption> productConsumptions);

}
