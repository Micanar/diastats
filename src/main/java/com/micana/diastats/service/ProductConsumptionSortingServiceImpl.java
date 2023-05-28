package com.micana.diastats.service;

import com.micana.diastats.domain.Blood_sugar;
import com.micana.diastats.domain.ProductConsumption;
import com.micana.diastats.repos.ProductConsumptionSortingService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductConsumptionSortingServiceImpl implements ProductConsumptionSortingService {
    @Override
    public List<ProductConsumption> sortProductConsumptionByDateTime(Iterable<ProductConsumption> productConsumptions) {
        return StreamSupport.stream(productConsumptions.spliterator(), false)
                .sorted(Comparator.comparing(ProductConsumption::getConsumptionDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductConsumption> sortProductConsumptionByDateTimeChart(Iterable<ProductConsumption> productConsumptions) {
        return StreamSupport.stream(productConsumptions.spliterator(), false)
                .sorted(Comparator.comparing(ProductConsumption::getConsumptionDateTime))
                .collect(Collectors.toList());
    }
}
