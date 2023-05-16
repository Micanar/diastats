package com.micana.diastats;

import com.micana.diastats.domain.Blood_sugar;
import com.micana.diastats.repos.BloodRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class GreetingController {

    @Autowired
    private BloodRepo bloodRepo;

    @GetMapping("/greeting")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Map<String,Object> model) {
        model.put("name", name);
        return "greeting";
    }

    @GetMapping("/")
    public String test(Map <String,Object> model){
        Iterable<Blood_sugar> blood = bloodRepo.findAll();
        model.put("sugars",blood);
        return "test";
    }

    @PostMapping("/add")
    public String add(@RequestParam String sugar, @RequestParam String date, @RequestParam String time, Map<String, Object> model) {
        Blood_sugar blood_sugar = new Blood_sugar(sugar, date, time);
        bloodRepo.save(blood_sugar);

        Iterable<Blood_sugar> blood = bloodRepo.findAll();
        model.put("sugars", blood);

        return "test";
    }

}
