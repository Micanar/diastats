package com.micana.diastats.controller;

import com.micana.diastats.domain.ProductConsumption;
import com.micana.diastats.domain.User;
import com.micana.diastats.repos.ProductConsumptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class AddConsumptionController {

    @Autowired
    private ProductConsumptionRepository productConsumptionRepository;


    @GetMapping("/addconsumption")
    public String showAddConsumptionForm() {
        return "addconsumption";
    }

    @PostMapping("/addconsumption")
    public String addConsumption(@AuthenticationPrincipal User user,
                                 @RequestParam String name,
                                 @RequestParam double grams,
                                 @RequestParam double carbohydrates,
                                 @RequestParam double fats,
                                 @RequestParam double proteins,
                                 @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime dateTime){
        ProductConsumption productConsumption = new ProductConsumption(user,name,grams,proteins/100*grams,fats/100*grams,carbohydrates/100*grams,dateTime);
        productConsumption.setBreadUnits( Math.round(carbohydrates/100/12*100.0)/100.0);
        productConsumptionRepository.save(productConsumption);
        return "redirect:/consumption";
    }



}
