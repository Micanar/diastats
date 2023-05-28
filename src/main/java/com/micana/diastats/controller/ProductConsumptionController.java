package com.micana.diastats.controller;

import com.micana.diastats.domain.PFC;
import com.micana.diastats.domain.ProductConsumption;
import com.micana.diastats.domain.User;
import com.micana.diastats.repos.PFCRepository;
import com.micana.diastats.repos.ProductConsumptionRepository;
import com.micana.diastats.repos.ProductConsumptionSortingService;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private ProductConsumptionSortingService productConsumptionSortingService;

    @Autowired
    private UserRepo userRepository;

    @GetMapping("/consumption")
    public String showConsumptionForm(Model model,
                                      @AuthenticationPrincipal User user,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        Iterable<ProductConsumption> consumptions;



            consumptions = productConsumptionRepository.findByUserOrderByConsumptionDateTimeDesc(user);
        if (startDate != null && endDate != null) {
            consumptions = StreamSupport.stream(consumptions.spliterator(), false)
                    .filter(consumption -> isWithinDateRange(consumption.getConsumptionDateTime().toLocalDate(), startDate, endDate))
                    .collect(Collectors.toList());
        }

            List<ProductConsumption> sortedConsuption =productConsumptionSortingService.sortProductConsumptionByDateTime(consumptions);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        model.addAttribute("dateTimeFormatter", formatter);

        model.addAttribute("consumptions", sortedConsuption);

        return "consumption";
    }
    private boolean isWithinDateRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    @GetMapping("/consumption/chart")
    public String showConsumptionChart(Model model, @AuthenticationPrincipal User user) {
        List<ProductConsumption> consumptions = productConsumptionRepository.findByUserOrderByConsumptionDateTimeDesc(user);
        model.addAttribute("consumptions", consumptions);
        return "consumption-chart";
    }

    @PostMapping("/consumption")
    public String addConsumption(@AuthenticationPrincipal User user, @RequestParam String name,
                                 @RequestParam double grams,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime dateTime) {
        PFC pfc = pfcRepository.findByName(name);
        if (pfc != null) {
            String nameConsumption = pfc.getName();
            double proteins = pfc.getProteins() * grams;
            double fats = pfc.getFats() * grams;
            double carbohydrates = pfc.getCarbohydrates() * grams;
            double breadUnits = pfc.getCarbohydrates() * grams / 12;
            breadUnits = Math.round(breadUnits * 100.0) / 100.0;

            // Создаем новый объект ProductConsumption
            ProductConsumption consumption = new ProductConsumption(pfc, grams, dateTime, user);
            consumption.setName(nameConsumption);
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
