package com.micana.diastats.controller;

import com.micana.diastats.domain.*;
import com.micana.diastats.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserProfileRepository userProfileRepository;

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

    @Autowired
    private BloodRepo bloodSugarRepository;

    @Autowired
    private ProductConsumptionRepository productConsumptionRepository;

    @Autowired
    private BloodSugarSortingService bloodSugarSortingService;


    @GetMapping("/users/{userId}/stats")
    public String viewUserStats(@PathVariable Long userId, Model model, Principal principal,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));

        if (user.getDoctor() == null) {
            // Если у пациента нет врача, перенаправляем его на страницу accessDenied
            return "accessDenied";
        }
        // Проверяем, является ли текущий пользователь доктором выбранного пользователя
        if (!user.getDoctor().getUsername().equals(principal.getName())) {
            // Если текущий пользователь не является доктором, перенаправляем его на другую страницу или выводим сообщение об ошибке
            return "accessDenied";
        }


        Iterable<Blood_sugar> bloodSugarList = bloodSugarRepository.findByPatient(user);
        bloodSugarList=bloodSugarSortingService.sortBloodSugarByDateTime(bloodSugarList);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        model.addAttribute("dateTimeFormatter", formatter);
        List<Blood_sugar> sortedBlood = bloodSugarSortingService.sortBloodSugarByDateTime(bloodSugarList);

        model.addAttribute("user", user);
        model.addAttribute("bloodSugarList", sortedBlood);


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
        if (averageSugarFirstDay>8 &&firstDayBlood.size()>1){
            model.addAttribute("attention","Cредний показатель сахаров за день у пациента довольно большой и близок к продолжительной гипергликемии,пациенту необходимо повышенное внимание");
        }else if (averageSugarFirstDay<4&&firstDayBlood.size()>1){
            model.addAttribute("attention","Средний показатель сахаров за день близок к продолжительной гипогликемии,пациенту необходимо повышенное внимание. ");
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
                model.addAttribute("attention", "У пациента замечено слишком много перепадов показаний гликемии , пациенту необходима корректировка инсулина.");
            } else if (lowSugarCount > 4) {
                model.addAttribute("attention", "У пациента замечено довольно большое количество гипогликимических показаний за последние 2 дня,необходимо принять меры .");
            }else if (highSugarCount > 4){
                model.addAttribute("attention","У пациента замечено довольно большое количество гипергликемических показаний за последние 2 дня,необходио принять меры .");
            }
        }


        //Конец


        return "userStats";
    }
    private boolean isWithinDateRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    @GetMapping("/users/{userId}/nutrition")
    public String viewUserNutrition(@PathVariable Long userId, Model model, Principal principal,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));

        // Проверяем, существует ли у пациента врач
        if (user.getDoctor() == null) {
            // Если у пациента нет врача, перенаправляем его на страницу accessDenied
            return "accessDenied";
        }

        // Проверяем, является ли текущий пользователь доктором выбранного пользователя
        if (!user.getDoctor().getUsername().equals(principal.getName())) {
            // Если текущий пользователь не является доктором, перенаправляем его на страницу accessDenied
            return "accessDenied";
        }

        List<ProductConsumption> nutritionList = productConsumptionRepository.findByUser(user);


        model.addAttribute("user", user);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        if (startDate != null && endDate != null) {
            nutritionList = StreamSupport.stream(nutritionList.spliterator(), false)
                    .filter(productConsumption -> isWithinDateRange(productConsumption.getConsumptionDateTime().toLocalDate(), startDate, endDate))
                    .collect(Collectors.toList());
        }
        model.addAttribute("dateTimeFormatter", formatter);
        model.addAttribute("nutritionList", nutritionList);

        // Фильтрация записей только для самого раннего дня
        LocalDate earliestDate = nutritionList.get(0).getConsumptionDateTime().toLocalDate();
        List<ProductConsumption> earliestDayConsumptions = nutritionList.stream()
                .filter(consumption -> consumption.getConsumptionDateTime().toLocalDate().equals(earliestDate))
                .collect(Collectors.toList());

        // Переменные для подсчета суммарного потребления простых и сложных углеводов
        double simpleCarbohydratesSum = 0;
        double complexCarbohydratesSum = 0;
        double totalBreadUnits = 0;


        for (ProductConsumption consumption : earliestDayConsumptions) {
            // Проверяем тип углеводов
            if ("Простой".equals(consumption.getCarbohydrateType())) {
                simpleCarbohydratesSum += consumption.getCarbohydrates();
            } else if ("Сложный".equals(consumption.getCarbohydrateType())) {
                complexCarbohydratesSum += consumption.getCarbohydrates();
            }if (consumption.getBreadUnits()>8){
                boolean overBreadUnits = true;
                model.addAttribute("overBreadUnits", overBreadUnits);

            }

            // Подсчитываем общее количество хлебных единиц
            totalBreadUnits += consumption.getBreadUnits();
        }

        // Проверяем, превышает ли потребление сложных углеводов 80% от общего количества углеводов
        double totalCarbohydrates = simpleCarbohydratesSum + complexCarbohydratesSum;
        double complexCarbohydratesPercentage = (complexCarbohydratesSum / totalCarbohydrates) * 100;

        // Проверяем, превышает ли общее количество хлебных единиц 20
        boolean exceedsBreadUnitsLimit = totalBreadUnits > 20;

        model.addAttribute("simpleCarbohydratesSum", simpleCarbohydratesSum);
        model.addAttribute("complexCarbohydratesSum", complexCarbohydratesSum);
        model.addAttribute("complexCarbohydratesPercentage", complexCarbohydratesPercentage);
        model.addAttribute("totalBreadUnits", totalBreadUnits);
        model.addAttribute("exceedsBreadUnitsLimit", exceedsBreadUnitsLimit);

        return "userNutrition";
    }

    @GetMapping("/users/{userId}/nutrition-page")
    public String viewUserNutritionPage(@PathVariable Long userId, Model model) {
        // Получите пользователя по его идентификатору
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));



        return "userNutrition";
    }

    @GetMapping("/users/{userId}/chart")
    public String chart(@PathVariable Long userId, Model model, Principal principal) {
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));

        if (user.getDoctor() == null) {
            // Если у пациента нет врача, перенаправляем его на страницу accessDenied
            return "accessDenied";
        }

        // Проверяем, является ли текущий пользователь доктором выбранного пользователя
        if (!user.getDoctor().getUsername().equals(principal.getName())) {
            // Если текущий пользователь не является доктором, перенаправляем его на другую страницу или выводим сообщение об ошибке
            return "accessDenied";
        }

        List<String> dates = new ArrayList<>();
        List<Double> sugarValues = new ArrayList<>();

        Iterable<Blood_sugar> bloodSugarList = bloodSugarRepository.findByPatient(user);
        List<Blood_sugar> sortedSugars = bloodSugarSortingService.sortBloodSugarByDateTimeChart(bloodSugarList);

        model.addAttribute("bloodSugars",sortedSugars);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
        model.addAttribute("dateTimeFormatter", formatter);

        return "patientBloodSugarChart";
    }


    @GetMapping("/users/{userId}/profile")
    public String viewUserNutrition(@PathVariable Long userId, Model model, Principal principal){
        User user = userRepo.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user Id: " + userId));
        UserProfile userProfile = userProfileRepository.findByUser(user);
        Iterable<GlycatedHemoglobinAnalys> glycatedHemoglobinAnalys = glycHemoRepo.findByPatient(user);
        Iterable<BloodAnalysis> bloodAnalyses = bloodAnalysRepo.findByPatient(user);
        Iterable<Creatinine> creatinines = creatinineRepo.findByPatient(user);
        Iterable<FatMetabolismIndicators> fatMetabolismIndicators = fatRepo.findByPatient(user);
        Iterable<HepaticIndicators> hepaticIndicators = hepaticRepo.findByPatient(user);
        Iterable<InsulinSynthesis> insulinSyntheses = insulinRepo.findByPatient(user);
        Iterable<UrineAnalysis> urineAnalyses = ureaAnalysRepo.findByPatient(user);
        Iterable<Analysis> analysis = analysisRepository.findByPatient(user);
        Iterable<UreaAnalys> ureaAnalys = ureaRepo.findByPatient(user);
        Iterable<UricAcidAnalys> uricAcidAnalys = uricAcidRepo.findByPatient(user);
        model.addAttribute("glycatedHemoglobinAnalys", glycatedHemoglobinAnalys);
        model.addAttribute("creatinines", creatinines);
        model.addAttribute("bloodAnalyses", bloodAnalyses);
        model.addAttribute("fatMetabolismIndicators", fatMetabolismIndicators);
        model.addAttribute("hepaticIndicators", hepaticIndicators);
        model.addAttribute("insulinSyntheses", insulinSyntheses);
        model.addAttribute("urineAnalyses", urineAnalyses);
        model.addAttribute("ureaAnalys", ureaAnalys);
        model.addAttribute("uricAcidAnalys", uricAcidAnalys);


        if (userProfile == null) {
            // Если профиль пользователя не найден, создаем новый профиль
            userProfile = new UserProfile();
            userProfile.setUser(user);
        }

        LocalDate currentDate = LocalDate.now();
        LocalDate birthdate = userProfile.getBirthdate();
        int age;
        if (birthdate != null) {
            Period period = Period.between(birthdate, currentDate);
            age = period.getYears();
            // Дальнейшая обработка возраста
        } else {
            age=0;
            // Обработка случая, когда birthdate равно null

        }

        model.addAttribute("age", age);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        model.addAttribute("dateTimeFormatter", formatter);
        model.addAttribute("analysis", analysis);
        // Передача информации о пользователе в модель
        model.addAttribute("userProfile", userProfile);
        return "patientProfile";
    }
}
