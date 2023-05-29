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

        // Фильтрация записей только для самого раннего дня
        LocalDate earliestDate = sortedConsuption.get(0).getConsumptionDateTime().toLocalDate();
        List<ProductConsumption> earliestDayConsumptions = sortedConsuption.stream()
                .filter(consumption -> consumption.getConsumptionDateTime().toLocalDate().equals(earliestDate))
                .collect(Collectors.toList());

        // Переменные для подсчета суммарного потребления простых и сложных углеводов
        double simpleCarbohydratesSum = 0;
        double complexCarbohydratesSum = 0;
        double totalBreadUnits = 0;


        for (ProductConsumption consumption : earliestDayConsumptions) {
            // Проверяем тип углеводов
            if ("Простой".equals(consumption.getCarbohydrateType())) {
                simpleCarbohydratesSum += consumption.getCarbohydrates();
            } else if ("Сложный".equals(consumption.getCarbohydrateType())) {
                complexCarbohydratesSum += consumption.getCarbohydrates();
            }if (consumption.getBreadUnits()>8){
                boolean overBreadUnits = true;
                model.addAttribute("overBreadUnits", overBreadUnits);

            }

            // Подсчитываем общее количество хлебных единиц
            totalBreadUnits += consumption.getBreadUnits();
        }

        // Проверяем, превышает ли потребление сложных углеводов 80% от общего количества углеводов
        double totalCarbohydrates = simpleCarbohydratesSum + complexCarbohydratesSum;
        double complexCarbohydratesPercentage = (complexCarbohydratesSum / totalCarbohydrates) * 100;

        // Проверяем, превышает ли общее количество хлебных единиц 20
        boolean exceedsBreadUnitsLimit = totalBreadUnits > 20;

        model.addAttribute("simpleCarbohydratesSum", simpleCarbohydratesSum);
        model.addAttribute("complexCarbohydratesSum", complexCarbohydratesSum);
        model.addAttribute("complexCarbohydratesPercentage", complexCarbohydratesPercentage);
        model.addAttribute("totalBreadUnits", totalBreadUnits);
        model.addAttribute("exceedsBreadUnitsLimit", exceedsBreadUnitsLimit);

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
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime dateTime,Model model) {
        PFC pfc = pfcRepository.findByName(name);

        if (pfc != null) {
            String nameConsumption = pfc.getName();
            double proteins = pfc.getProteins() * grams;
            double fats = pfc.getFats() * grams;
            double carbohydrates = pfc.getCarbohydrates() * grams;
            double breadUnits = pfc.getCarbohydrates() * grams / 12;
            breadUnits = Math.round(breadUnits * 100.0) / 100.0;

            String carbohydrateType = pfc.getCarbohydrateType();
            // Создаем новый объект ProductConsumption
            ProductConsumption consumption = new ProductConsumption(pfc, grams, dateTime, user);
            consumption.setName(nameConsumption);
            consumption.setProteins(proteins);
            consumption.setFats(fats);
            consumption.setCarbohydrates(carbohydrates);
            consumption.setBreadUnits(breadUnits);
            consumption.setCarbohydrateType(carbohydrateType);

            // Сохраняем объект ProductConsumption в репозитории
            productConsumptionRepository.save(consumption);
        }else {
            model.addAttribute("productNotFound", true);
            return "addconsumption";
        }
        return "redirect:/consumption";
    }

    }


