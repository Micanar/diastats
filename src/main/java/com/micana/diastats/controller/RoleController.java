package com.micana.diastats.controller;

import com.micana.diastats.domain.Role;
import com.micana.diastats.domain.User;
import com.micana.diastats.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/role")
@PreAuthorize("hasAuthority('ADMIN')")
public class RoleController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/edit/{userId}")
    public String editUserRoleForm(@PathVariable Long userId, Model model) {
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "editUserRole";
    }

    @PostMapping("/save")
    public String saveUserRole(@ModelAttribute("user") User user, @RequestParam(required = false) Boolean updatePassword, @RequestParam(required = false) Boolean updateActive) {
        // Получите существующего пользователя из базы данных
        User existingUser = userRepo.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + user.getId()));

        // Обновите только необходимые поля
        existingUser.setRoles(user.getRoles());
        if (existingUser.getRoles().contains(Role.ADMIN) || existingUser.getRoles().contains(Role.DOCTOR)) {
            existingUser.setDoctor(null);
        }

        userRepo.save(existingUser);

        return "redirect:/role";
    }

    @GetMapping
    public String userList(Model model, @RequestParam(value = "search", required = false) String search) {
        List<User> users;
        if (search != null && !search.isEmpty()) {
            users = userRepo.findByUsernameContainingIgnoreCase(search);
        } else {
            users = userRepo.findAll();
        }
        model.addAttribute("users", users);
        return "userList";
    }


    @GetMapping("/assign-doctor/{userId}")
    public String assignDoctorForm(@PathVariable Long userId, Model model) {
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));
        List<User> doctors = userRepo.findDoctors(); // Получаем список всех пользователей, у которых роль "Доктор"
        model.addAttribute("user", user);
        model.addAttribute("doctors", doctors);
        return "assignDoctor";
    }

    @PostMapping("/save-doctor")
    public String saveDoctorAssignment(@RequestParam("userId") Long userId, @RequestParam("doctorName") String doctorName) {
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));
        User doctor = userRepo.findByUsername(doctorName);
        user.setDoctor(doctor);
        userRepo.save(user);
        return "redirect:/role";
    }


}
