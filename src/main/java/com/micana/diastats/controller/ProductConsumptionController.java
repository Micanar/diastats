package com.micana.diastats.controller;

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
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
public class ProductConsumptionController {

    private final ProductConsumptionRepository productConsumptionRepository;
    private final PFCRepository pfcRepository;

    public ProductConsumptionController(ProductConsumptionRepository productConsumptionRepository, PFCRepository pfcRepository) {
        this.productConsumptionRepository = productConsumptionRepository;
        this.pfcRepository = pfcRepository;
    }

    @GetMapping("/consumption")
    public String showConsumptionForm(Model model, @AuthenticationPrincipal User user) {
        Iterable<PFC> pfcList = pfcRepository.findAll();
        model.addAttribute("pfcList", pfcList);

        Iterable<ProductConsumption> consumptions = productConsumptionRepository.findByUserOrderByConsumptionDateAscConsumptionTimeAsc(user);
        model.addAttribute("consumptions", consumptions);

        return "consumption";
    }

    @PostMapping("/consumption")
    public String addConsumption(@AuthenticationPrincipal User user, @RequestParam String name,
                                 @RequestParam double grams, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate consumptionDate,
                                 @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime consumptionTime) {
        PFC pfc = pfcRepository.findByName(name);
        if (pfc != null) {
            pfc.setProteins(pfc.getProteins() * grams);
            pfc.setCarbohydrates(pfc.getCarbohydrates() * grams);
            pfc.setFats(pfc.getFats() * grams);
            ProductConsumption consumption = new ProductConsumption(pfc, grams, consumptionDate, consumptionTime, user);
            productConsumptionRepository.save(consumption);
        }
        return "redirect:/consumption";
    }
}
