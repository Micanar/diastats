package com.micana.diastats.controller;

import com.micana.diastats.domain.Blood_sugar;
import com.micana.diastats.domain.User;
import com.micana.diastats.repos.BloodRepo;
import com.micana.diastats.repos.BloodSugarSortingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class MainController {

    @Autowired
    private BloodRepo bloodRepo;

    @Autowired
    BloodSugarSortingService bloodSugarSortingService;

    @GetMapping("/")
    public String greeting(Model model) {
        return "redirect:/hello";
    }

    @GetMapping("/hello")
    public String hello(Model model, Principal principal) {
        Authentication authentication = (Authentication) principal;
        boolean isDoctor = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("DOCTOR"));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ADMIN"));

        // Передаем флаг в модель
        model.addAttribute("isDoctor", isDoctor);
        model.addAttribute("isAdmin", isAdmin);

        return "hello";
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user, Model model,
                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Iterable<Blood_sugar> blood = bloodRepo.findByPatient(user);

        // Фильтрация данных о кровном сахаре по указанному периоду, если даты указаны
        if (startDate != null && endDate != null) {
            blood = StreamSupport.stream(blood.spliterator(), false)
                    .filter(bloodSugar -> isWithinDateRange(bloodSugar.getDateTime().toLocalDate(), startDate, endDate))
                    .collect(Collectors.toList());
        }

        List<Blood_sugar> sortedBlood = bloodSugarSortingService.sortBloodSugarByDateTime(blood);

        model.addAttribute("sugars", sortedBlood);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        model.addAttribute("dateTimeFormatter", formatter);

        // Находим первую дату в списке sortedBlood
        LocalDate firstDate = sortedBlood.get(0).getDateTime().toLocalDate();

// Отфильтровываем значения только для первого дня
        List<Blood_sugar> firstDayBlood = sortedBlood.stream()
                .filter(bloodSugar -> bloodSugar.getDateTime().toLocalDate().isEqual(firstDate))
                .collect(Collectors.toList());

// Рассчитываем среднее значение для отфильтрованного списка
        double averageSugarFirstDay = firstDayBlood.stream()
                .mapToDouble(Blood_sugar::getSugar)
                .average()
                .orElse(0.0);

        // Округляем значение до 1 знака после запятой
        String formattedAverage = String.format("%.1f", averageSugarFirstDay);

// Добавляем округленное значение в модель
        model.addAttribute("formattedAverageSugarFirstDay", formattedAverage);
        if (averageSugarFirstDay>8&&firstDayBlood.size()>1){
            model.addAttribute("attention","Ваш средний показатель сахаров за день довольно большой ,пожалуйста проконтролируйте количество съеденных углеводов и введенный инсулин.");
        }else if (averageSugarFirstDay<4&&firstDayBlood.size()>1){
            model.addAttribute("attention","Ваш средний показатель за день близок к продолжительной гипогликемии, пожалуйста проконтролируйте количество съеденных углеводов и введенный инсулин. ");
        }

// Добавляем среднее значение первого дня в модель
        //Начало
        // Получение текущей даты и времени
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime twoDaysAgo = currentDateTime.minusDays(2);

        // Фильтрация записей за последние два дня с учетом значений
        List<Blood_sugar> lastTwoDaysBlood = sortedBlood.stream()
                .filter(bloodSugar -> {
                    LocalDateTime bloodDateTime = bloodSugar.getDateTime();
                    double bloodSugarValue = bloodSugar.getSugar();
                    return bloodDateTime.isAfter(twoDaysAgo) &&
                            bloodDateTime.isBefore(currentDateTime) &&
                            (bloodSugarValue > 8.3 || bloodSugarValue < 4.0);
                })
                .collect(Collectors.toList());



        // Выполнение расчетов и добавление сообщения в модель
        if (lastTwoDaysBlood.size() > 4) {
            long highSugarCount = lastTwoDaysBlood.stream()
                    .filter(bloodSugar -> bloodSugar.getSugar() > 8.3)
                    .count();

            long lowSugarCount = lastTwoDaysBlood.stream()
                    .filter(bloodSugar -> bloodSugar.getSugar() < 4.0)
                    .count();

            if (lowSugarCount>4 || highSugarCount>4) {
                model.addAttribute("attention1", "У вас замечено слишком много перепадов показаний гликемии , пожалуйста проконсультируйтесь с врачом.");
            } else if (lowSugarCount > 4) {
                model.addAttribute("attention1", "У вас замечено довольно большое количество гипогликимических показаний за последние 2 дня,пожалуйста , проконсультируйтесь с врачом.");
            }else if (highSugarCount > 4){
                model.addAttribute("attention1","У вас замечео довольно большое количество гипергликемических показаний за последние 2 дня, пожалуйста проконсультируйтесь с врачом.");
            }
        }


        //Конец

        return "main";
    }

    private boolean isWithinDateRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }


    @GetMapping("/blood-sugar/chart")
    public String showBloodSugarChart(Model model, @AuthenticationPrincipal User user,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        Iterable<Blood_sugar> bloodSugars = bloodRepo.findByPatient(user);

        // Фильтрация данных о кровном сахаре по указанному периоду, если даты указаны
        if (startDate != null && endDate != null) {
            bloodSugars = StreamSupport.stream(bloodSugars.spliterator(), false)
                    .filter(bloodSugar -> isWithinDateRange(bloodSugar.getDateTime(), startDate, endDate))
                    .collect(Collectors.toList());
        }

        List<Blood_sugar> sortedBloodSugars = bloodSugarSortingService.sortBloodSugarByDateTimeChart(bloodSugars);

        model.addAttribute("bloodSugars", sortedBloodSugars);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        model.addAttribute("dateTimeFormatter", formatter);



        return "blood-sugar-chart";
    }

    private boolean isWithinDateRange(LocalDateTime dateTime, LocalDate startDate, LocalDate endDate) {
        return !dateTime.toLocalDate().isBefore(startDate) && !dateTime.toLocalDate().isAfter(endDate);
    }


    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user, @RequestParam String sugar, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime dateTime, Model model) {
        double sugarValue;
        try {
            sugarValue = Double.parseDouble(sugar);
        } catch (NumberFormatException e) {
            model.addAttribute("errorMessage", "Invalid sugar value. Please enter a valid number.");
            return "main";
        }
        Blood_sugar blood_sugar = new Blood_sugar(sugarValue, dateTime, user);
        bloodRepo.save(blood_sugar);
        return "redirect:/main";
    }
}
