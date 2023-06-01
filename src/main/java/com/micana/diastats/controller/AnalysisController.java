package com.micana.diastats.controller;

import com.micana.diastats.domain.Analysis;
import com.micana.diastats.domain.User;
import com.micana.diastats.repos.AnalysisRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisRepo analysisRepository;

    @GetMapping("/form")
    public String showAnalysisForm(Model model,@AuthenticationPrincipal User user) {
        model.addAttribute("analysis", new Analysis());
        return "form";
    }

    @PostMapping("/submit")
    public String submitAnalysis(@AuthenticationPrincipal User user,
                                 @ModelAttribute("analysis") Analysis analysis,
                                 @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("analysDate") LocalDate analysDate) {
        // Логика для обработки POST запроса
        // Сохранение анализа или выполнение другой необходимой операции
        System.out.println("Робит");
       analysis.setPatient(user);
       analysis.setDateAnalysis(analysDate);

        analysisRepository.save(analysis);
        System.out.println(analysis.getAnalysisType());

        return "redirect:/analysis/form";
    }


}
