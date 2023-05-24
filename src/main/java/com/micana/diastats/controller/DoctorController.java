package com.micana.diastats.controller;

import com.micana.diastats.domain.User;
import com.micana.diastats.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class DoctorController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/patients")
    public String doctorPatients(Model model, Principal principal) {
        // Получите текущего пользователя (доктора) по его идентификатору (username)
        User doctor = userRepo.findByUsername(principal.getName());

        // Получите список всех пациентов доктора
        List<User> patients = userRepo.findByDoctor(doctor);

        model.addAttribute("patients", patients);

        return "doctorPatients";
    }

    @GetMapping("/patients/{userId}/stats")
    public String viewPatientStats(@PathVariable Long userId, Model model) {
        model.addAttribute("userId", userId);
        return "redirect:/users/{userId}/stats";
    }




}
