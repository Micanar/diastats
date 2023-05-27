package com.micana.diastats.controller;

import com.micana.diastats.domain.Blood_sugar;
import com.micana.diastats.domain.User;
import com.micana.diastats.repos.BloodRepo;
import com.micana.diastats.repos.BloodSugarSortingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class MainController {

    @Autowired
    private BloodRepo bloodRepo;

    @Autowired
    BloodSugarSortingService bloodSugarSortingService;

    @GetMapping("/")
    public String greeting(Model model) {
        return "redirect:/hello";
    }

    @GetMapping("/hello")
    public String hello(Model model, Principal principal) {
        Authentication authentication = (Authentication) principal;
        boolean isDoctor = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("DOCTOR"));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ADMIN"));

        // Передаем флаг в модель
        model.addAttribute("isDoctor", isDoctor);
        model.addAttribute("isAdmin", isAdmin);

        return "hello";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user, Model model,
                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Iterable<Blood_sugar> blood = bloodRepo.findByPatient(user);

        // Фильтрация данных о кровном сахаре по указанному периоду, если даты указаны
        if (startDate != null && endDate != null) {
            blood = StreamSupport.stream(blood.spliterator(), false)
                    .filter(bloodSugar -> isWithinDateRange(bloodSugar.getDateTime().toLocalDate(), startDate, endDate))
                    .collect(Collectors.toList());
        }

        List<Blood_sugar> sortedBlood = bloodSugarSortingService.sortBloodSugarByDateTime(blood);

        model.addAttribute("sugars", sortedBlood);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        model.addAttribute("dateTimeFormatter", formatter);

        return "main";
    }

    private boolean isWithinDateRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }


    @GetMapping("/blood-sugar/chart")
    public String showBloodSugarChart(Model model, @AuthenticationPrincipal User user,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Iterable<Blood_sugar> bloodSugars = bloodRepo.findByPatient(user);

        // Фильтрация данных о кровном сахаре по указанному периоду, если даты указаны
        if (startDate != null && endDate != null) {
            bloodSugars = StreamSupport.stream(bloodSugars.spliterator(), false)
                    .filter(bloodSugar -> isWithinDateRange(bloodSugar.getDateTime(), startDate, endDate))
                    .collect(Collectors.toList());
        }

        List<Blood_sugar> sortedBloodSugars = bloodSugarSortingService.sortBloodSugarByDateTimeChart(bloodSugars);

        model.addAttribute("bloodSugars", sortedBloodSugars);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        model.addAttribute("dateTimeFormatter", formatter);

        return "blood-sugar-chart";
    }

    private boolean isWithinDateRange(LocalDateTime dateTime, LocalDate startDate, LocalDate endDate) {
        return !dateTime.toLocalDate().isBefore(startDate) && !dateTime.toLocalDate().isAfter(endDate);
    }


    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user, @RequestParam String sugar, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime dateTime, Model model) {
        double sugarValue;
        try {
            sugarValue = Double.parseDouble(sugar);
        } catch (NumberFormatException e) {
            model.addAttribute("errorMessage", "Invalid sugar value. Please enter a valid number.");
            return "main";
        }
        Blood_sugar blood_sugar = new Blood_sugar(sugarValue, dateTime, user);
        bloodRepo.save(blood_sugar);
        return "redirect:/main";
    }
}
