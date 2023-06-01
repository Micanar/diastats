package com.micana.diastats.controller;

import com.micana.diastats.domain.Analysis;
import com.micana.diastats.domain.User;
import com.micana.diastats.domain.UserProfile;
import com.micana.diastats.repos.AnalysisRepo;
import com.micana.diastats.repos.UserProfileRepository;
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
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Controller
public class UserPageController {
    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private AnalysisRepo analysisRepository;



    @GetMapping("/profile")
    public String showUserProfile(Model model, @AuthenticationPrincipal User user) {
        // Получение профиля пользователя из репозитория
        UserProfile userProfile = userProfileRepository.findByUser(user);
        Iterable<Analysis> analysis = analysisRepository.findByPatient(user);

        if (userProfile == null) {
            // Если профиль пользователя не найден, создаем новый профиль
            userProfile = new UserProfile();
            userProfile.setUser(user);
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate birthdate = userProfile.getBirthdate();
        int age;
        if (birthdate != null) {
            Period period = Period.between(birthdate, currentDate);
            age = period.getYears();
            // Дальнейшая обработка возраста
        } else {
            age=0;
            // Обработка случая, когда birthdate равно null

        }

        model.addAttribute("age", age);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        model.addAttribute("dateTimeFormatter", formatter);
        model.addAttribute("analysis", analysis);
        // Передача информации о пользователе в модель
        model.addAttribute("userProfile", userProfile);

        return "profile";
    }



    @PostMapping("/profile")
    public String updateUserProfile(@RequestParam("height") double height,
                                    @RequestParam("weight") double weight,
                                    @RequestParam("gender") String gender,
                                    @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("birthDate") LocalDate birthDate,
                                    @AuthenticationPrincipal User user) {
        // Получение профиля пользователя, если он уже существует
        UserProfile userProfile = userProfileRepository.findByUser(user);

        if (userProfile == null) {
            // Если профиль пользователя не существует, создаем новый
            userProfile = new UserProfile();
            userProfile.setUser(user);
        }

        double heightInMeters = height / 100;
        double bmi = weight / (heightInMeters * heightInMeters);
        bmi = Math.round(bmi*100)/100;

        userProfile.setHeight(height);
        userProfile.setWeight(weight);
        userProfile.setGender(gender);
        userProfile.setBmi(bmi);
        userProfile.setBirthdate(birthDate);

        // Сохранение или обновление профиля пользователя в базе данных
        userProfileRepository.save(userProfile);

        return "redirect:/profile";
    }




}
