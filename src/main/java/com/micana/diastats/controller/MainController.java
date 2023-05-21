package com.micana.diastats.controller;

import com.micana.diastats.domain.Blood_sugar;
import com.micana.diastats.domain.User;
import com.micana.diastats.repos.BloodRepo;
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
public class MainController {

    @Autowired
    private BloodRepo bloodRepo;

    @GetMapping("/")
    public String greeting(Model model) {
        return "redirect:/hello";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user, Model model) {
        Iterable<Blood_sugar> blood = bloodRepo.findByPatient(user);
        List<Blood_sugar> sortedSugars = StreamSupport.stream(blood.spliterator(), false)
                .sorted(Comparator.comparing(Blood_sugar::getData).thenComparing(Blood_sugar::getTime).reversed())
                .collect(Collectors.toList());
        model.addAttribute("sugars", sortedSugars);
        return "main";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user, @RequestParam String sugar, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime time, Model model) {
        double sugarValue;
        try {
            sugarValue = Double.parseDouble(sugar);
        } catch (NumberFormatException e) {
            model.addAttribute("errorMessage", "Invalid sugar value. Please enter a valid number.");
            return "main";
        }
        Blood_sugar blood_sugar = new Blood_sugar(sugarValue, date, time, user);
        bloodRepo.save(blood_sugar);

        Iterable<Blood_sugar> blood = bloodRepo.findByPatient(user);
        List<Blood_sugar> sortedSugars = StreamSupport.stream(blood.spliterator(), false)
                .sorted(Comparator.comparing(Blood_sugar::getData).thenComparing(Blood_sugar::getTime).reversed())
                .collect(Collectors.toList());
        model.addAttribute("sugars", sortedSugars);

        return "main";
    }

}
