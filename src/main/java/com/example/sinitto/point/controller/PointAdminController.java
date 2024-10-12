package com.example.sinitto.point.controller;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Sinitto;
import com.example.sinitto.member.exception.MemberNotFoundException;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.dto.PointLogWithBankInfo;
import com.example.sinitto.point.dto.PointLogWithDepositMessage;
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

    private final PointAdminService pointAdminService;
    private final MemberRepository memberRepository;
    private final SinittoRepository sinittoRepository;

    public PointAdminController(PointAdminService pointAdminService, MemberRepository memberRepository, SinittoRepository sinittoRepository) {
        this.pointAdminService = pointAdminService;
        this.memberRepository = memberRepository;
        this.sinittoRepository = sinittoRepository;
    }

    @GetMapping("/admin/point/charge")
    public String showAllChargeRequest(Model model) {

        List<PointLog> pointLogs = pointAdminService.readAllNotCompletedPointChargeRequest();
        List<PointLogWithDepositMessage> logWithDepositMessages = new ArrayList<>();

        for (PointLog pointLog : pointLogs) {
            Member member = memberRepository.findById(pointLog.getMember().getId())
                    .orElseThrow(() -> new MemberNotFoundException("멤버를 찾을 수 없습니다"));

            PointLogWithDepositMessage pointLogWithDepositMessage = new PointLogWithDepositMessage(
                    pointLog.getId(),
                    pointLog.getPrice(),
                    pointLog.getPostTime(),
                    pointLog.getStatus(),
                    member.getDepositMessage()
            );

            logWithDepositMessages.add(pointLogWithDepositMessage);
        }
        model.addAttribute("logWithDepositMessages", logWithDepositMessages);

        return "/point/charge";
    }

    @PostMapping("/admin/point/charge/waiting/{pointLogId}")
    public String changeToWaiting(@PathVariable Long pointLogId) {

        pointAdminService.changeChargeLogToWaiting(pointLogId);
        return "redirect:/admin/point/charge";
    }

    @PostMapping("/admin/point/charge/complete/{pointLogId}")
    public String changeToCompleteAndEarn(@PathVariable Long pointLogId) {

        pointAdminService.earnPointAndChangeToChargeComplete(pointLogId);
        return "redirect:/admin/point/charge";
    }

    @PostMapping("/admin/point/charge/fail/{pointLogId}")
    public String changeToFail(@PathVariable Long pointLogId) {

        pointAdminService.changeChargeLogToFail(pointLogId);
        return "redirect:/admin/point/charge";
    }

    @GetMapping("/admin/point/withdraw")
    public String showAllWithdrawRequest(Model model) {

        List<PointLog> pointLogs = pointAdminService.readAllPointWithdrawRequest();
        List<PointLogWithBankInfo> logWithBankInfos = new ArrayList<>();

        for (PointLog pointLog : pointLogs) {
            Sinitto sinitto = sinittoRepository.findByMemberId(pointLog.getMember().getId())
                    .orElseThrow(() -> new SinittoNotFoundException("시니또를 찾을 수 없습니다"));

            PointLogWithBankInfo pointLogWithBankInfo = new PointLogWithBankInfo(
                    pointLog.getId(),
                    pointLog.getPrice(),
                    pointLog.getPostTime(),
                    pointLog.getStatus(),
                    sinitto.getBankName(),
                    sinitto.getAccountNumber()
            );

            logWithBankInfos.add(pointLogWithBankInfo);
        }
        model.addAttribute("logWithBankInfos", logWithBankInfos);

        return "/point/withdraw";
    }

    @PostMapping("/admin/point/withdraw/waiting/{pointLogId}")
    public String changeWithdrawLogToWaiting(@PathVariable Long pointLogId) {

        pointAdminService.changeWithdrawLogToWaiting(pointLogId);
        return "redirect:/admin/point/withdraw";
    }

    @PostMapping("/admin/point/withdraw/complete/{pointLogId}")
    public String changeWithdrawLogToCompleteAndEarn(@PathVariable Long pointLogId) {

        pointAdminService.changeWithdrawLogToComplete(pointLogId);
        return "redirect:/admin/point/withdraw";
    }

    @PostMapping("/admin/point/withdraw/fail/{pointLogId}")
    public String changeWithdrawLogToFail(@PathVariable Long pointLogId) {

        pointAdminService.changeWithdrawLogToFail(pointLogId);
        return "redirect:/admin/point/withdraw";
    }

}
