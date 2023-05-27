package com.micana.diastats.repos;

import com.micana.diastats.domain.Blood_sugar;

import java.util.List;

public interface BloodSugarSortingService {
    List<Blood_sugar> sortBloodSugarByDateTime(Iterable<Blood_sugar> bloodSugars);
    List<Blood_sugar> sortBloodSugarByDateTimeChart(Iterable<Blood_sugar> bloodSugars);

}
