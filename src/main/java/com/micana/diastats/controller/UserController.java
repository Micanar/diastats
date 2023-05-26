package com.micana.diastats.controller;

import com.micana.diastats.domain.Blood_sugar;
import com.micana.diastats.domain.ProductConsumption;
import com.micana.diastats.domain.User;
import com.micana.diastats.repos.BloodRepo;
import com.micana.diastats.repos.ProductConsumptionRepository;
import com.micana.diastats.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
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


    @GetMapping("/users/{userId}/stats")
    public String viewUserStats(@PathVariable Long userId, Model model, Principal principal) {
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

        model.addAttribute("user", user);
        model.addAttribute("bloodSugarList", bloodSugarList);

        return "userStats";
    }

    @GetMapping("/users/{userId}/nutrition")
    public String viewUserNutrition(@PathVariable Long userId, Model model, Principal principal) {

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
        List<Blood_sugar> sortedSugars = StreamSupport.stream(bloodSugarList.spliterator(), false)
                .sorted(Comparator.comparing(Blood_sugar::getData).thenComparing(Blood_sugar::getTime).reversed())
                .collect(Collectors.toList());
        model.addAttribute("bloodSugars",bloodSugarList);

        return "patientBloodSugarChart";
    }


}
