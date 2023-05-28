package com.micana.diastats.controller;

import com.micana.diastats.domain.Blood_sugar;
import com.micana.diastats.domain.ProductConsumption;
import com.micana.diastats.domain.User;
import com.micana.diastats.repos.BloodRepo;
import com.micana.diastats.repos.BloodSugarSortingService;
import com.micana.diastats.repos.ProductConsumptionRepository;
import com.micana.diastats.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BloodRepo bloodSugarRepository;

    @Autowired
    private ProductConsumptionRepository productConsumptionRepository;

    @Autowired
    private BloodSugarSortingService bloodSugarSortingService;


    @GetMapping("/users/{userId}/stats")
    public String viewUserStats(@PathVariable Long userId, Model model, Principal principal,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));

        if (user.getDoctor() == null) {
            // Если у пациента нет врача, перенаправляем его на страницу accessDenied
            return "accessDenied";
        }
        // Проверяем, является ли текущий пользователь доктором выбранного пользователя
        if (!user.getDoctor().getUsername().equals(principal.getName())) {
            // Если текущий пользователь не является доктором, перенаправляем его на другую страницу или выводим сообщение об ошибке
            return "accessDenied";
        }


        Iterable<Blood_sugar> bloodSugarList = bloodSugarRepository.findByPatient(user);
        bloodSugarList=bloodSugarSortingService.sortBloodSugarByDateTime(bloodSugarList);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        model.addAttribute("dateTimeFormatter", formatter);
        if (startDate != null && endDate != null) {
            bloodSugarList = StreamSupport.stream(bloodSugarList.spliterator(), false)
                    .filter(bloodSugar -> isWithinDateRange(bloodSugar.getDateTime().toLocalDate(), startDate, endDate))
                    .collect(Collectors.toList());
        }

        model.addAttribute("user", user);
        model.addAttribute("bloodSugarList", bloodSugarList);

        return "userStats";
    }
    private boolean isWithinDateRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    @GetMapping("/users/{userId}/nutrition")
    public String viewUserNutrition(@PathVariable Long userId, Model model, Principal principal,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));

        // Проверяем, существует ли у пациента врач
        if (user.getDoctor() == null) {
            // Если у пациента нет врача, перенаправляем его на страницу accessDenied
            return "accessDenied";
        }

        // Проверяем, является ли текущий пользователь доктором выбранного пользователя
        if (!user.getDoctor().getUsername().equals(principal.getName())) {
            // Если текущий пользователь не является доктором, перенаправляем его на страницу accessDenied
            return "accessDenied";
        }

        List<ProductConsumption> nutritionList = productConsumptionRepository.findByUser(user);


        model.addAttribute("user", user);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        if (startDate != null && endDate != null) {
            nutritionList = StreamSupport.stream(nutritionList.spliterator(), false)
                    .filter(productConsumption -> isWithinDateRange(productConsumption.getConsumptionDateTime().toLocalDate(), startDate, endDate))
                    .collect(Collectors.toList());
        }
        model.addAttribute("dateTimeFormatter", formatter);
        model.addAttribute("nutritionList", nutritionList);

        return "userNutrition";
    }

    @GetMapping("/users/{userId}/nutrition-page")
    public String viewUserNutritionPage(@PathVariable Long userId, Model model) {
        // Получите пользователя по его идентификатору
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));



        return "userNutrition";
    }

    @GetMapping("/users/{userId}/chart")
    public String chart(@PathVariable Long userId, Model model, Principal principal) {
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));

        if (user.getDoctor() == null) {
            // Если у пациента нет врача, перенаправляем его на страницу accessDenied
            return "accessDenied";
        }

        // Проверяем, является ли текущий пользователь доктором выбранного пользователя
        if (!user.getDoctor().getUsername().equals(principal.getName())) {
            // Если текущий пользователь не является доктором, перенаправляем его на другую страницу или выводим сообщение об ошибке
            return "accessDenied";
        }

        List<String> dates = new ArrayList<>();
        List<Double> sugarValues = new ArrayList<>();

        Iterable<Blood_sugar> bloodSugarList = bloodSugarRepository.findByPatient(user);
        List<Blood_sugar> sortedSugars = bloodSugarSortingService.sortBloodSugarByDateTimeChart(bloodSugarList);

        model.addAttribute("bloodSugars",sortedSugars);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        model.addAttribute("dateTimeFormatter", formatter);

        return "patientBloodSugarChart";
    }


}
