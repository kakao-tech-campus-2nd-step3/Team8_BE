package com.example.sinitto.point.controller;

import com.example.sinitto.member.entity.Sinitto;
import com.example.sinitto.point.dto.PointLogWithBankAccountNumber;
import com.example.sinitto.point.entity.PointLog;
import com.example.sinitto.point.service.PointAdminService;
import com.example.sinitto.sinitto.exception.SinittoNotFoundException;
import com.example.sinitto.sinitto.repository.SinittoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PointAdminController {

    private final SinittoRepository sinittoRepository;
    private final PointAdminService pointAdminService;

    public PointAdminController(SinittoRepository sinittoRepository, PointAdminService pointAdminService) {
        this.sinittoRepository = sinittoRepository;
        this.pointAdminService = pointAdminService;
    }

    @GetMapping("/admin/point")
    public String showAll(Model model) {

        List<PointLog> pointLogs = pointAdminService.readAllNotCompletedPointChargeRequest();
        List<PointLogWithBankAccountNumber> logWithBankAccountNumbers = new ArrayList<>();

        for (PointLog pointLog : pointLogs) {
            Sinitto sinitto = sinittoRepository.findByMemberId(pointLog.getMember().getId())
                    .orElseThrow(() -> new SinittoNotFoundException("시니또를 찾을 수 없습니다"));

            PointLogWithBankAccountNumber pointLogWithBankAccountNumber = new PointLogWithBankAccountNumber(
                    pointLog.getId(),
                    pointLog.getPrice(),
                    pointLog.getPostTime(),
                    pointLog.getStatus(),
                    sinitto.getAccountNumber());

            logWithBankAccountNumbers.add(pointLogWithBankAccountNumber);
        }
        model.addAttribute("logWithBankAccountNumbers", logWithBankAccountNumbers);

        return "/point/charge";
    }

    @PostMapping("/admin/point/waiting/{pointLogId}")
    public String changeToWaiting(@PathVariable Long pointLogId) {

        pointAdminService.changePointLogChargeRequestToChargeWaiting(pointLogId);
        return "redirect:/admin/point";
    }

    @PostMapping("/admin/point/complete/{pointLogId}")
    public String changeToComplete(@PathVariable Long pointLogId) {

        pointAdminService.changePointLogChargeWaitingToChargeComplete(pointLogId);
        return "redirect:/admin/point";
    }
}
