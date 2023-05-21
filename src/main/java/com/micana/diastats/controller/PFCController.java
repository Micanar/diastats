package com.micana.diastats.controller;

import com.micana.diastats.domain.PFC;
import com.micana.diastats.repos.PFCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pfc")
public class PFCController {

    @Autowired
    private PFCRepository pfcRepository;

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("pfc", new PFC());
        return "pfc";
    }

    @PostMapping("/add")
    public String addPFC(PFC pfc) {
        pfcRepository.save(pfc);
        return "redirect:/pfc/list";
    }

    @GetMapping("/list")
    public String showPFCList(Model model) {
        Iterable<PFC> pfcList = pfcRepository.findAll();
        model.addAttribute("pfcList", pfcList);
        return "pfc";
    }
}
