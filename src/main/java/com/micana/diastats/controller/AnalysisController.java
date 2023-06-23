package com.micana.diastats.controller;

import com.micana.diastats.domain.*;
import com.micana.diastats.repos.*;
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
    @Autowired
    GlycHemoRepo glycHemoRepo;
    @Autowired
    FatRepo fatRepo;
    @Autowired
    InsulinRepo insulinRepo;
    @Autowired
    BloodAnalysRepo bloodAnalysRepo;
    @Autowired
    UreaAnalysRepo ureaAnalysRepo;
    @Autowired
    CreatinineRepo creatinineRepo;
    @Autowired
    UreaRepo ureaRepo;
    @Autowired
    UricAcidRepo uricAcidRepo;
    @Autowired
    HepaticRepo hepaticRepo;


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
        if (analysis.getAnalysisType().equals("glycatedHemoglobin")){
            GlycatedHemoglobinAnalys glycatedHemoglobinAnalys = new GlycatedHemoglobinAnalys();
            glycatedHemoglobinAnalys.setGlycatedHemoglobin(analysis.getGlycatedHemoglobin());
            glycatedHemoglobinAnalys.setPatient(user);
            glycatedHemoglobinAnalys.setDateAnalysis(analysDate);
            glycHemoRepo.save(glycatedHemoglobinAnalys);
        }
        if (analysis.getAnalysisType().equals("metabolism")){
            FatMetabolismIndicators fatMetabolismIndicators = new FatMetabolismIndicators();
            fatMetabolismIndicators.setDateAnalysis(analysDate);
            fatMetabolismIndicators.setPatient(user);
            fatMetabolismIndicators.setLpnp(analysis.getLpnp());
            fatMetabolismIndicators.setHdlCholesterol(analysis.getHdlCholesterol());
            fatMetabolismIndicators.setTriglycerides(analysis.getTriglycerides());
            fatMetabolismIndicators.setTotalCholesterol(analysis.getTotalCholesterol());
            fatMetabolismIndicators.setLdlCholesterol(analysis.getLdlCholesterol());
            fatRepo.save(fatMetabolismIndicators);

        }
        if (analysis.getAnalysisType().equals("insulin")){
            InsulinSynthesis insulinSynthesis= new InsulinSynthesis();
            insulinSynthesis.setDateAnalysis(analysDate);
            insulinSynthesis.setPatient(user);
            insulinSynthesis.setInsulin(analysis.getInsulin());
            insulinSynthesis.setcPeptide(analysis.getcPeptide());
            insulinSynthesis.setProinsulin(analysis.getProinsulin());
            insulinRepo.save(insulinSynthesis);


        }
        if (analysis.getAnalysisType().equals("blood")){
            BloodAnalysis bloodAnalysis= new BloodAnalysis();
            bloodAnalysis.setDateAnalysis(analysDate);
            bloodAnalysis.setPatient(user);
            bloodAnalysis.setHemoglobin(analysis.getHemoglobin());
            bloodAnalysis.setLeukocytes(analysis.getLeukocytes());
            bloodAnalysis.setErythrocyteSedimentationRate(analysis.getErythrocyteSedimentationRate());
            bloodAnalysRepo.save(bloodAnalysis);

        }
        if (analysis.getAnalysisType().equals("urine")){
            UrineAnalysis ureaAnalys = new UrineAnalysis();
            ureaAnalys.setPatient(user);
            ureaAnalys.setDateAnalysis(analysDate);
            ureaAnalys.setBacteria(analysis.getBacteria());
            ureaAnalys.setGlucose(analysis.getGlucose());
            ureaAnalys.setProtein(analysis.getProtein());
            ureaAnalys.setSpecificGravity(analysis.getSpecificGravity());
            ureaAnalys.setUrineErythrocytes(analysis.getUrineErythrocytes());
            ureaAnalys.setUrineLeukocytes(analysis.getUrineLeukocytes());
            ureaAnalysRepo.save(ureaAnalys);

        }
        if (analysis.getAnalysisType().equals("creatinine")){
            Creatinine creatinine = new Creatinine();
            creatinine.setDateAnalysis(analysDate);
            creatinine.setPatient(user);
            creatinine.setCreatinine(analysis.getCreatinine());
            creatinineRepo.save(creatinine);

        }
        if (analysis.getAnalysisType().equals("urea")){
            UreaAnalys ureaAnalys = new UreaAnalys();
            ureaAnalys.setDateAnalysis(analysDate);
            ureaAnalys.setPatient(user);
            ureaAnalys.setUrea(analysis.getUrea());
            ureaRepo.save(ureaAnalys);


        }
        if (analysis.getAnalysisType().equals("uricAcid")){
            UricAcidAnalys uricAcidAnalys = new UricAcidAnalys();
            uricAcidAnalys.setDateAnalysis(analysDate);
            uricAcidAnalys.setPatient(user);
            uricAcidAnalys.setUricAcid(analysis.getUricAcid());
            uricAcidRepo.save(uricAcidAnalys);

        }
        if (analysis.getAnalysisType().equals("liver")){
            HepaticIndicators hepaticIndicators = new HepaticIndicators();
            hepaticIndicators.setAlt(analysis.getAlt());
            hepaticIndicators.setAst(analysis.getAst());
            hepaticIndicators.setGgtp(analysis.getGgtp());
            hepaticIndicators.setWsf(analysis.getWsf());
            hepaticIndicators.setDateAnalysis(analysDate);
            hepaticIndicators.setPatient(user);
            hepaticRepo.save(hepaticIndicators);



        }

        System.out.println("Робит");
       analysis.setPatient(user);
       analysis.setDateAnalysis(analysDate);


        System.out.println(analysis.getAnalysisType());

        return "redirect:/profile";
    }


}
