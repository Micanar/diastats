package com.micana.diastats.controller;

import com.micana.diastats.domain.Blood_sugar;
import com.micana.diastats.domain.PFC;
import com.micana.diastats.domain.ProductConsumption;
import com.micana.diastats.domain.User;
import com.micana.diastats.repos.PFCRepository;
import com.micana.diastats.repos.ProductConsumptionRepository;
import com.micana.diastats.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class ProductConsumptionController {

    @Autowired
    private PFCRepository pfcRepository;

    @Autowired
    private ProductConsumptionRepository productConsumptionRepository;

    @Autowired
    private UserRepo userRepository;

    @GetMapping("/consumption")
    public String showConsumptionForm(Model model,
                                      @RequestParam(required = false) LocalDate date,
                                      @RequestParam(required = false) LocalTime time,
                                      @AuthenticationPrincipal User user) {

        Iterable<ProductConsumption> consumptions;

        if (date != null && time != null) {
            consumptions = productConsumptionRepository.findByUserAndConsumptionDateOrderByConsumptionDateDesc(user, date);
        } else if (date != null) {
            consumptions = productConsumptionRepository.findByUserAndConsumptionDateOrderByConsumptionDateDesc(user, date);
        } else {
            consumptions = productConsumptionRepository.findByUserOrderByConsumptionDateDesc(user);
        }

        List<ProductConsumption> sortedConsumptions = StreamSupport.stream(consumptions.spliterator(), false)
                .sorted(Comparator.comparing(ProductConsumption::getConsumptionDate).thenComparing(ProductConsumption::getConsumptionTime).reversed())
                .collect(Collectors.toList());

        model.addAttribute("consumptions", sortedConsumptions);

        return "consumption";
    }

    @GetMapping("/consumption/chart")
    public String showConsumptionChart(Model model, @AuthenticationPrincipal User user) {
        List<ProductConsumption> consumptions = productConsumptionRepository.findByUserOrderByConsumptionDateDesc(user);
        model.addAttribute("consumptions", consumptions);
        return "consumption-chart";
    }



    @PostMapping("/consumption")
    public String addConsumption(@AuthenticationPrincipal User user, @RequestParam String name,
                                 @RequestParam double grams, @RequestParam("consumptionDate")@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                 @RequestParam("consumptionTime") @DateTimeFormat(pattern = "HH:mm") LocalTime time) {
        PFC pfc = pfcRepository.findByName(name);
        if (pfc != null) {
            double proteins = pfc.getProteins() * grams;
            double fats = pfc.getFats() * grams;
            double carbohydrates = pfc.getCarbohydrates() * grams;
            double breadUnits = pfc.getCarbohydrates()/12;
            breadUnits = Math.round(breadUnits * 100.0) / 100.0;

            // Создаем новый объект ProductConsumption
            ProductConsumption consumption = new ProductConsumption( pfc,grams, date, time, user);
            consumption.setProteins(proteins);
            consumption.setFats(fats);
            consumption.setCarbohydrates(carbohydrates);
            consumption.setBreadUnits(breadUnits);

            // Сохраняем объект ProductConsumption в репозитории
            productConsumptionRepository.save(consumption);
        }
        return "redirect:/consumption";
    }
}
