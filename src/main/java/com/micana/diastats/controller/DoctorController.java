package com.micana.diastats.controller;

import com.micana.diastats.domain.User;
import com.micana.diastats.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;

@Controller
public class DoctorController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/patients")
    public String doctorPatients(@RequestParam(required = false) String username, Model model, Principal principal) {


        User doctor = userRepo.findByUsername(principal.getName());

        // Получите список всех пациентов доктора
        List<User> patients;
        if (username != null && !username.isEmpty()) {
            // Если указано имя пользователя, применяем фильтрацию
            patients = userRepo.findByDoctorAndUsernameContainingIgnoreCase(doctor, username);
        } else {
            // Иначе получаем всех пациентов без фильтрации
            patients = userRepo.findByDoctor(doctor);
        }

        model.addAttribute("patients", patients);

        return "doctorPatients";
    }



    @GetMapping("/patients/{userId}/stats")
    public String viewPatientStats(@PathVariable Long userId, Model model) {
        model.addAttribute("userId", userId);
        return "redirect:/users/{userId}/stats";
    }




}
