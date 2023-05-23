    package com.micana.diastats.controller;

    import com.micana.diastats.domain.PFC;
    import com.micana.diastats.domain.User;
    import com.micana.diastats.repos.PFCRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.core.annotation.AuthenticationPrincipal;
    import org.springframework.stereotype.Controller;
    import org.springframework.ui.Model;
    import org.springframework.web.bind.annotation.*;

    import java.math.BigDecimal;
    import java.math.RoundingMode;
    import java.text.DecimalFormat;

    @Controller
    @RequestMapping("/pfc")
    public class PFCController {

        @Autowired
        private PFCRepository pfcRepository;

        @GetMapping("/add")
        public String showAddFormAndPFCList(Model model) {
            Iterable<PFC> pfcList = pfcRepository.findAll();
            model.addAttribute("pfcList", pfcList);
            model.addAttribute("pfc", new PFC());
            return "pfc";
        }

        @PostMapping("/add")
        public String addPFC(@AuthenticationPrincipal User user, @RequestParam String name, @RequestParam double proteins, @RequestParam double fats, @RequestParam double carbohydrates) {
            double proteinsInGrams = proteins / 100;
            double fatsInGrams = fats / 100;
            double carbohydratesInGrams = carbohydrates / 100;
            double breadUnitsIngram = carbohydratesInGrams / 12;
            breadUnitsIngram = Math.round(breadUnitsIngram * 100.0) / 100.0;

            // Округление до двух знаков после запятой
            BigDecimal proteinsBigDecimal = BigDecimal.valueOf(proteinsInGrams).setScale(2, RoundingMode.HALF_UP);
            BigDecimal fatsBigDecimal = BigDecimal.valueOf(fatsInGrams).setScale(2, RoundingMode.HALF_UP);
            BigDecimal carbohydratesBigDecimal = BigDecimal.valueOf(carbohydratesInGrams).setScale(2, RoundingMode.HALF_UP);

            PFC pfc = new PFC(name, proteinsBigDecimal.doubleValue(), fatsBigDecimal.doubleValue(), carbohydratesBigDecimal.doubleValue(),breadUnitsIngram);
            pfcRepository.save(pfc);
            return "redirect:/pfc/add";
        }


    }

