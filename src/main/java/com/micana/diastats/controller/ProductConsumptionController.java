package com.micana.diastats.controller;

import com.micana.diastats.domain.PFC;
import com.micana.diastats.domain.ProductConsumption;
import com.micana.diastats.domain.User;
import com.micana.diastats.repos.PFCRepository;
import com.micana.diastats.repos.ProductConsumptionRepository;
import com.micana.diastats.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class ProductConsumptionController {

    @Autowired
    private PFCRepository pfcRepository;

    @Autowired
    private ProductConsumptionRepository productConsumptionRepository;

    @Autowired
    private UserRepo userRepository;

    @GetMapping("/consumption")
    public String showConsumptionForm(Model model, @RequestParam(required = false) LocalDate date) {
        Iterable<PFC> pfcList = pfcRepository.findAll();
        model.addAttribute("pfcList", pfcList);

        Iterable<ProductConsumption> consumptions;
        if (date != null) {
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = startOfDay.plusHours(23).plusMinutes(59).plusSeconds(59);
            consumptions = productConsumptionRepository.findByConsumptionDateTimeBetween(startOfDay, endOfDay);
        } else {
            consumptions = productConsumptionRepository.findAll();
        }
        model.addAttribute("consumptions", consumptions);

        return "consumption";
    }

    @PostMapping("/consumption")
    public String addConsumption(@AuthenticationPrincipal User user, @RequestParam String name,
                                 @RequestParam double grams, @RequestParam String dateTime) {
        PFC pfc = pfcRepository.findByName(name);
        if (pfc != null) {
            pfc.setProteins(pfc.getProteins()*grams);
            pfc.setCarbohydrates(pfc.getCarbohydrates()*grams);
            pfc.setFats(pfc.getFats()*grams);
            ProductConsumption consumption = new ProductConsumption(pfc, grams, LocalDateTime.parse(dateTime), user);
            productConsumptionRepository.save(consumption);
        }
        return "redirect:/consumption";
    }
}
