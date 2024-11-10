package com.example.sinitto.point.controller;

import com.example.sinitto.point.dto.PointLogWithBankInfo;
import com.example.sinitto.point.dto.PointLogWithDepositMessage;
import com.example.sinitto.point.service.PointAdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class PointAdminController {

    private final PointAdminService pointAdminService;

    public PointAdminController(PointAdminService pointAdminService) {
        this.pointAdminService = pointAdminService;
    }

    @GetMapping("/admin/point/charge")
    public String showAllChargeRequest(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        List<PointLogWithDepositMessage> logWithDepositMessages = pointAdminService.getPointLogWithDepositMessage();

        model.addAttribute("logWithDepositMessages", logWithDepositMessages);

        return "point/charge";
    }

    @PostMapping("/admin/point/charge/waiting/{pointLogId}")
    public String changeToWaiting(@PathVariable Long pointLogId, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        pointAdminService.changeChargeLogToWaiting(pointLogId);
        return "redirect:/admin/point/charge";
    }

    @PostMapping("/admin/point/charge/complete/{pointLogId}")
    public String changeToCompleteAndEarn(@PathVariable Long pointLogId, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        pointAdminService.earnPointAndChangeToChargeComplete(pointLogId);
        return "redirect:/admin/point/charge";
    }

    @PostMapping("/admin/point/charge/fail/{pointLogId}")
    public String changeToFail(@PathVariable Long pointLogId, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        pointAdminService.changeChargeLogToFail(pointLogId);
        return "redirect:/admin/point/charge";
    }

    @GetMapping("/admin/point/withdraw")
    public String showAllWithdrawRequest(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }

        List<PointLogWithBankInfo> logWithBankInfos = pointAdminService.getPointLogWithBankInfo();

        model.addAttribute("logWithBankInfos", logWithBankInfos);

        return "point/withdraw";
    }

    @PostMapping("/admin/point/withdraw/waiting/{pointLogId}")
    public String changeWithdrawLogToWaiting(@PathVariable Long pointLogId, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        pointAdminService.changeWithdrawLogToWaiting(pointLogId);
        return "redirect:/admin/point/withdraw";
    }

    @PostMapping("/admin/point/withdraw/complete/{pointLogId}")
    public String changeWithdrawLogToCompleteAndEarn(@PathVariable Long pointLogId, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        pointAdminService.changeWithdrawLogToComplete(pointLogId);
        return "redirect:/admin/point/withdraw";
    }

    @PostMapping("/admin/point/withdraw/fail/{pointLogId}")
    public String changeWithdrawLogToFail(@PathVariable Long pointLogId, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/admin/login";
        }
        pointAdminService.changeWithdrawLogToFail(pointLogId);
        return "redirect:/admin/point/withdraw";
    }

    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "ADMIN".equals(role);
    }

}
