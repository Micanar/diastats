package com.micana.diastats.controller;

import com.micana.diastats.domain.PFC;
import com.micana.diastats.repos.PFCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PFCInfoController {

    @Autowired
    private PFCRepository pfcRepository;

    @GetMapping("/pfcinfo")
    public String showPFCInfoForm(Model model) {
        Iterable<PFC> pfcList = pfcRepository.findAll();
        model.addAttribute("pfcList", pfcList);
        return "pfcinfo";
    }

    @PostMapping("/calculate")
    public String calculatePFC(@RequestParam String name, @RequestParam double grams, Model model) {
        PFC pfc = pfcRepository.findByName(name);
        if (pfc != null) {
            double proteins = pfc.getProteins() * grams;
            double fats = pfc.getFats() * grams;
            double carbohydrates = pfc.getCarbohydrates() * grams;
            double breadUnits = pfc.getBreadUnits() * grams;

            model.addAttribute("name", name);
            model.addAttribute("grams", grams);
            model.addAttribute("proteins", proteins);
            model.addAttribute("fats", fats);
            model.addAttribute("breadUnits", breadUnits);
            model.addAttribute("carbohydrates", carbohydrates);
            model.addAttribute("error", null);
        } else {
            model.addAttribute("error", "Product not found");
        }

        Iterable<PFC> pfcList = pfcRepository.findAll();
        model.addAttribute("pfcList", pfcList);
        return "pfcinfo";
    }
}
