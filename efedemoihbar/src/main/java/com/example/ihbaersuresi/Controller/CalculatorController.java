package com.example.ihbaersuresi.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
public class CalculatorController {

    private boolean isStartDateBeforeEndDate(LocalDate startDate, LocalDate endDate) {
        return startDate != null && endDate != null && startDate.isBefore(endDate);
    }
    @GetMapping("/")
    public String showCalculatorForm() {
        return "index";
    }

    @PostMapping("/calculate")
    public String calculate(@RequestParam("startDate") String startDate,
                            @RequestParam("endDate") String endDate,
                            @RequestParam("jobSearchPermission") String jobSearchPermission,
                            Model model) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        if (!isStartDateBeforeEndDate(start, end)) {
            model.addAttribute("warningMessage", "Başlangıç tarihi bitiş tarihinden geri veya aynı olmalıdır.");
            return "index";
        }
        long daysBetween = calculateNoticePeriod(start, end);
        long reducedNoticePeriod = calculateReducedNoticePeriod(daysBetween, jobSearchPermission);

        LocalDate newEndDate = end.plusDays(reducedNoticePeriod);

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("jobSearchPermission", jobSearchPermission);
        model.addAttribute("daysBetween", daysBetween);
        model.addAttribute("reducedNoticePeriod", reducedNoticePeriod);
        model.addAttribute("newEndDate", newEndDate);

        return "result";
    }


    private long calculateNoticePeriod(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    private long calculateReducedNoticePeriod(long daysBetween, String jobSearchPermission) {
        if ("no".equals(jobSearchPermission)) {
            if (daysBetween <= 180) { // 0-6 ay
                return 14; // 2 hafta
            } else if (daysBetween <= 540) { // 6-18 ay
                return 28; // 4 hafta
            } else if (daysBetween <= 1080) { // 18-36 ay
                return 42; // 6 hafta
            } else { // 36 ay ve üzeri
                return 56; // 8 hafta
            }
        } else if ("yes".equals(jobSearchPermission)) {
                long noticePeriod = 0;
                if (daysBetween <= 180) {
                    noticePeriod = 14;
                    return noticePeriod - 4;// 2 hafta
                } else if (daysBetween <= 540) {
                    noticePeriod = 28; // 4 hafta
                    return noticePeriod - 8;
                } else if (daysBetween <= 1080) {
                    noticePeriod = 42; // 6 hafta
                    return noticePeriod - 12;
                } else {
                    noticePeriod = 56;
                    return noticePeriod - 16;// 8 hafta
                }
        }


        return daysBetween; // Hata durumunda başlangıçta hesaplanan süreyi döndürür
    }
}
