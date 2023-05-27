package com.micana.diastats.service;

import com.micana.diastats.domain.Blood_sugar;
import com.micana.diastats.repos.BloodSugarSortingService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BloodSugarSortingServiceImpl implements BloodSugarSortingService {

    @Override
    public List<Blood_sugar> sortBloodSugarByDateTime(Iterable<Blood_sugar> bloodSugars) {
        return StreamSupport.stream(bloodSugars.spliterator(), false)
                .sorted(Comparator.comparing(Blood_sugar::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<Blood_sugar> sortBloodSugarByDateTimeChart(Iterable<Blood_sugar> bloodSugars) {
        return StreamSupport.stream(bloodSugars.spliterator(), false)
                .sorted(Comparator.comparing(Blood_sugar::getDateTime))
                .collect(Collectors.toList());
    }

}
