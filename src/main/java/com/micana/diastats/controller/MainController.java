package com.micana.diastats.controller;

import com.micana.diastats.domain.Blood_sugar;
import com.micana.diastats.domain.User;
import com.micana.diastats.repos.BloodRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private BloodRepo bloodRepo;

    @GetMapping("/")
    public String greeting(Map<String,Object> model) {
        return "redirect:/hello";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }



    @GetMapping("/main")
    public String main(Map <String,Object> model){
        Iterable<Blood_sugar> blood = bloodRepo.findAll();
        model.put("sugars",blood);
        return "main";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user, @RequestParam String sugar, @RequestParam String date, @RequestParam String time, Map<String, Object> model) {
        Blood_sugar blood_sugar = new Blood_sugar(sugar, date, time,user);
        bloodRepo.save(blood_sugar);

        Iterable<Blood_sugar> blood = bloodRepo.findAll();
        model.put("sugars", blood);

        return "main";
    }

}
