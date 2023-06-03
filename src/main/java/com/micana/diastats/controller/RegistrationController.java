package com.micana.diastats.controller;

import com.micana.diastats.domain.Role;
import com.micana.diastats.domain.User;
import com.micana.diastats.repos.UserRepo;
import com.micana.diastats.service.ConfirmCodeService;
import com.micana.diastats.service.MailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Controller
public class    RegistrationController {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    MailSenderService senderService;
    @Autowired
    private ConfirmCodeService confirmCodeService;

    private Map<String, User> currentUser = new HashMap<>();
    String savedConfirmationCode;


    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }
    @GetMapping("/confirm")
    public String confirm() {
        return "confirm";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.put("message", "User exists!");
            return "registration";
        }
        String confirmationCode = confirmCodeService.generateConfirmationCode();
        senderService.sendSimpleEmail(user.getEmail(),"Код подтверждения","Здравствуйте , вы отправили запрос на регистрацию в приложении DiaStats ваш код подтверждения: "+confirmationCode+" .");

        savedConfirmationCode =confirmationCode;
        currentUser.put("currentUser",user);

        model.put("user", user);
        return "redirect:/confirm";
    }
    @PostMapping("/confirm")
    public String confirmRegistration(String username, String confirmationCode, Map<String, Object> model) {
        if (savedConfirmationCode != null && savedConfirmationCode.equals(confirmationCode)) {
            // Код подтверждения верный, установите флаг активности пользователя
            User user = currentUser.get("currentUser");
            user.setActive(true);
            user.setRoles(Collections.singleton(Role.USER));
            userRepo.save(user);
            return "redirect:/login";
        } else {
            model.put("message", model.get("confirmcode"));
            return "confirm";
        }
}}
