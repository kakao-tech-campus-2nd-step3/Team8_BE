package com.example.sinitto.helloCall.service;

import com.example.sinitto.auth.exception.UnauthorizedException;
import com.example.sinitto.guard.exception.SeniorNotFoundException;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.helloCall.dto.*;
import com.example.sinitto.helloCall.entity.HelloCall;
import com.example.sinitto.helloCall.entity.HelloCallTimeLog;
import com.example.sinitto.helloCall.entity.TimeSlot;
import com.example.sinitto.helloCall.exception.CompletionConditionNotFulfilledException;
import com.example.sinitto.helloCall.exception.HelloCallAlreadyExistsException;
import com.example.sinitto.helloCall.exception.HelloCallNotFoundException;
import com.example.sinitto.helloCall.repository.HelloCallRepository;
import com.example.sinitto.helloCall.repository.HelloCallTimeLogRepository;
import com.example.sinitto.helloCall.repository.TimeSlotRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.entity.Sinitto;
import com.example.sinitto.member.exception.MemberNotFoundException;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.sinitto.exception.SinittoNotFoundException;
import com.example.sinitto.sinitto.repository.SinittoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HelloCallService {

    private final HelloCallRepository helloCallRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final SeniorRepository seniorRepository;
    private final MemberRepository memberRepository;
    private final SinittoRepository sinittoRepository;
    private final HelloCallTimeLogRepository helloCallTimeLogRepository;


    public HelloCallService(HelloCallRepository helloCallRepository, TimeSlotRepository timeSlotRepository,
                            SeniorRepository seniorRepository, MemberRepository memberRepository, SinittoRepository sinittoRepository,
                            HelloCallTimeLogRepository helloCallTimeLogRepository) {
        this.helloCallRepository = helloCallRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.seniorRepository = seniorRepository;
        this.memberRepository = memberRepository;
        this.sinittoRepository = sinittoRepository;
        this.helloCallTimeLogRepository = helloCallTimeLogRepository;
    }

    @Transactional
    public void createHelloCallByGuard(Long memberId, HelloCallRequest helloCallRequest) {
        Senior senior = seniorRepository.findByIdAndMemberId(helloCallRequest.seniorId(), memberId)
                .orElseThrow(() -> new SeniorNotFoundException("시니어를 찾을 수 없습니다."));

        if (helloCallRepository.existsBySenior(senior)) {
            throw new HelloCallAlreadyExistsException("이미 해당 시니어의 안부 전화 서비스가 존재합니다.");
        }

        HelloCall helloCall = new HelloCall(helloCallRequest.startDate(), helloCallRequest.endDate(),
                helloCallRequest.price(), helloCallRequest.serviceTime(), helloCallRequest.requirement(), senior);
        HelloCall savedHelloCall = helloCallRepository.save(helloCall);

        for (HelloCallRequest.TimeSlot timeSlotRequest : helloCallRequest.timeSlots()) {
            TimeSlot timeSlot = new TimeSlot(timeSlotRequest.day(), timeSlotRequest.startTime(),
                    timeSlotRequest.endTime(), savedHelloCall);
            timeSlotRepository.save(timeSlot);
        }
    }

    @Transactional
    public List<HelloCallResponse> readAllHelloCallsByGuard(Long memberId) {
        List<Senior> seniors = seniorRepository.findByMemberId(memberId);

        List<HelloCallResponse> helloCallResponsesForGuard = new ArrayList<>();

        for (Senior senior : seniors) {
            if (helloCallRepository.existsBySenior(senior)) {
                HelloCall helloCall = helloCallRepository.findBySenior(senior).orElseThrow(
                        () -> new HelloCallNotFoundException("신청된 시니어의 안부전화 정보를 찾을 수 없습니다."));

                HelloCallResponse response = new HelloCallResponse(helloCall.getId(), helloCall.getSenior().getName(),
                        helloCall.getTimeSlots().stream().map(TimeSlot::getDay).toList(), helloCall.getStatus());

                helloCallResponsesForGuard.add(response);
            }
        }
        return helloCallResponsesForGuard;
    }

    @Transactional
    public List<HelloCallResponse> readAllWaitingHelloCallsBySinitto() {

        List<HelloCallResponse> helloCallResponses = new ArrayList<>();

        List<HelloCall> helloCalls = helloCallRepository.findAll();

        for (HelloCall helloCall : helloCalls) {
            if (helloCall.getStatus().equals(HelloCall.Status.WAITING)) {
                HelloCallResponse response = new HelloCallResponse(helloCall.getId(), helloCall.getSenior().getName(),
                        helloCall.getTimeSlots().stream().map(TimeSlot::getDay).toList(), helloCall.getStatus());

                helloCallResponses.add(response);
            }
        }
        return helloCallResponses;
    }

    @Transactional(readOnly = true)
    public HelloCallDetailResponse readHelloCallDetail(Long HelloCallId) {
        HelloCall helloCall = helloCallRepository.findById(HelloCallId)
                .orElseThrow(() -> new HelloCallNotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        return new HelloCallDetailResponse(helloCall.getStartDate(), helloCall.getEndDate(),
                helloCall.getTimeSlots(), helloCall.getRequirement(), helloCall.getSenior().getName(),
                helloCall.getSenior().getPhoneNumber(), helloCall.getPrice());
    }

    @Transactional
    public void updateHelloCallByGuard(Long memberId, Long helloCallId, HelloCallDetailUpdateRequest helloCallDetailUpdateRequest) {
        HelloCall helloCall = helloCallRepository.findById(helloCallId)
                .orElseThrow(() -> new HelloCallNotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("id에 해당하는 멤버를 찾을 수 없습니다."));

        if (helloCall.checkUnAuthorization(member)) {
            throw new UnauthorizedException("안부전화 정보를 수정할 권한이 없습니다.");
        }

        helloCall.updateHelloCall(helloCallDetailUpdateRequest.startDate(), helloCallDetailUpdateRequest.endDate(),
                helloCallDetailUpdateRequest.price(), helloCallDetailUpdateRequest.serviceTime(), helloCallDetailUpdateRequest.requirement());

        updateTimeSlots(helloCall, helloCallDetailUpdateRequest.timeSlots());
    }

    private void updateTimeSlots(HelloCall helloCall, List<HelloCallDetailUpdateRequest.TimeSlot> updatedTimeSlots) {
        List<String> updatedDays = updatedTimeSlots.stream()
                .map(HelloCallDetailUpdateRequest.TimeSlot::day)
                .toList();

        List<TimeSlot> existingTimeSlots = new ArrayList<>(helloCall.getTimeSlots());

        for (TimeSlot existingSlot : existingTimeSlots) {
            if (!updatedDays.contains(existingSlot.getDay())) {
                timeSlotRepository.delete(existingSlot);
                helloCall.getTimeSlots().remove(existingSlot);
            }
        }

        for (HelloCallDetailUpdateRequest.TimeSlot updatedSlot : updatedTimeSlots) {
            TimeSlot timeSlot = timeSlotRepository.findByHelloCallAndDay(helloCall, updatedSlot.day())
                    .orElse(new TimeSlot(updatedSlot.day(), updatedSlot.startTime(), updatedSlot.endTime(), helloCall));

            timeSlot.updateTimeSlot(updatedSlot.startTime(), updatedSlot.endTime());
            timeSlotRepository.save(timeSlot);
        }
    }

    @Transactional
    public void deleteHellCallByGuard(Long memberId, Long helloCallId) {
        HelloCall helloCall = helloCallRepository.findById(helloCallId)
                .orElseThrow(() -> new HelloCallNotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("id에 해당하는 멤버를 찾을 수 없습니다."));

        if (helloCall.checkUnAuthorization(member)) {
            throw new UnauthorizedException("안부전화 신청을 취소할 권한이 없습니다.");
        }

        helloCall.checkStatusIsWaiting();
        helloCallRepository.delete(helloCall);
    }

    @Transactional(readOnly = true)
    public List<HelloCallTimeLogResponse> readHelloCallTimeLogByGuard(Long memberId, Long helloCallId) {
        HelloCall helloCall = helloCallRepository.findById(helloCallId)
                .orElseThrow(() -> new HelloCallNotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("id에 해당하는 멤버를 찾을 수 없습니다."));

        if (helloCall.checkUnAuthorization(member)) {
            throw new UnauthorizedException("안부전화 로그를 조회할 권한이 없습니다.");
        }

        List<HelloCallTimeLog> helloCallTimeLogs = helloCallTimeLogRepository.findAllByHelloCallId(helloCallId);

        List<HelloCallTimeLogResponse> helloCallTimeLogResponses = new ArrayList<>();

        for (HelloCallTimeLog helloCallTimeLog : helloCallTimeLogs) {
            HelloCallTimeLogResponse response = new HelloCallTimeLogResponse(helloCallTimeLog.getSinittoName(),
                    helloCallTimeLog.getStartDateAndTime(), helloCallTimeLog.getEndDateAndTime());
            helloCallTimeLogResponses.add(response);
        }

        return helloCallTimeLogResponses;
    }

    @Transactional(readOnly = true)
    public HelloCallReportResponse readHelloCallReportByGuard(Long memberId, Long helloCallId) {
        if (!sinittoRepository.existsByMemberId(memberId)) {
            throw new MemberNotFoundException("id에 해당하는 멤버를 찾을 수 없습니다.");
        }

        HelloCall helloCall = helloCallRepository.findById(helloCallId)
                .orElseThrow(() -> new HelloCallNotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        if (!helloCall.checkReportIsNotNull()) {
            throw new CompletionConditionNotFulfilledException("아직 안부전화 서비스가 완료되지 않았습니다.");
        }

        return new HelloCallReportResponse(helloCall.getStartDate(),
                helloCall.getEndDate(), helloCall.getSinittoName(), helloCall.getReport());
    }

    @Transactional
    public void makeCompleteHelloCallByGuard(Long memberId, Long helloCallId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("id에 해당하는 멤버를 찾을 수 없습니다."));

        HelloCall helloCall = helloCallRepository.findById(helloCallId)
                .orElseThrow(() -> new HelloCallNotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        helloCall.checkGuardIsCorrect(member);

        helloCall.changeStatusToComplete();

        Sinitto earnedSinitto = helloCall.getSinitto();

        //earnedSinitto에게 포인트 지급 로직 필요합니다.
    }


    @Transactional(readOnly = true)
    public List<HelloCallReportResponse> readAllHelloCallReport() {
        List<HelloCall> helloCalls = helloCallRepository.findAll();

        List<HelloCallReportResponse> helloCallReportResponses = new ArrayList<>();

        for (HelloCall helloCall : helloCalls) {
            if (helloCall.checkReportIsNotNull()) {
                HelloCallReportResponse response = new HelloCallReportResponse(helloCall.getStartDate(),
                        helloCall.getEndDate(), helloCall.getSinittoName(), helloCall.getReport());
                helloCallReportResponses.add(response);
            }
        }
        return helloCallReportResponses;
    }

    @Transactional
    public void acceptHelloCallBySinitto(Long memberId, Long helloCallId) {
        HelloCall helloCall = helloCallRepository.findById(helloCallId)
                .orElseThrow(() -> new HelloCallNotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        Sinitto sinitto = sinittoRepository.findByMemberId(memberId)
                .orElseThrow(() -> new SinittoNotFoundException("id에 해당하는 시니또를 찾을 수 없습니다."));

        helloCall.changeStatusToInProgress();
        helloCall.setSinitto(sinitto);
    }

    @Transactional
    public HelloCallTimeResponse writeHelloCallStartTimeBySinitto(Long memberId, Long helloCallId) {
        HelloCall helloCall = helloCallRepository.findById(helloCallId)
                .orElseThrow(() -> new HelloCallNotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        Sinitto sinitto = sinittoRepository.findByMemberId(memberId)
                .orElseThrow(() -> new SinittoNotFoundException("id에 해당하는 시니또를 찾을 수 없습니다."));

        HelloCallTimeLog helloCallTimeLog = new HelloCallTimeLog(helloCall, sinitto);
        helloCallTimeLog.setStartDateAndTime(LocalDateTime.now());

        HelloCallTimeLog savedHelloCallTimeLog = helloCallTimeLogRepository.save(helloCallTimeLog);
        return new HelloCallTimeResponse(savedHelloCallTimeLog.getStartDateAndTime());
    }

    @Transactional
    public HelloCallTimeResponse writeHelloCallEndTimeBySinitto(Long memberId, Long helloCallId) {
        Sinitto sinitto = sinittoRepository.findByMemberId(memberId)
                .orElseThrow(() -> new SinittoNotFoundException("id에 해당하는 시니또를 찾을 수 없습니다."));

        HelloCallTimeLog helloCallTimeLog = helloCallTimeLogRepository.findBySinittoAndAndHelloCallId(sinitto, helloCallId)
                .orElseThrow(() -> new HelloCallNotFoundException("안부전화 로그를 찾을 수 없습니다."));

        helloCallTimeLog.setEndDateAndTime(LocalDateTime.now());

        return new HelloCallTimeResponse(helloCallTimeLog.getEndDateAndTime());
    }

    @Transactional
    public void cancelHelloCallBySinitto(Long memberId, Long helloCallId) {
        HelloCall helloCall = helloCallRepository.findById(helloCallId)
                .orElseThrow(() -> new HelloCallNotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        if (!sinittoRepository.existsByMemberId(memberId)) {
            throw new SinittoNotFoundException("id에 해당하는 시니또를 찾을 수 없습니다.");
        }

        helloCall.changeStatusToWaiting();
        helloCall.setSinitto(null);
    }

    @Transactional
    public void SendReportBySinitto(Long memberId, Long helloCallId, HelloCallReportRequest helloCallReportRequest) {
        HelloCall helloCall = helloCallRepository.findById(helloCallId)
                .orElseThrow(() -> new HelloCallNotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        Sinitto sinitto = sinittoRepository.findByMemberId(memberId)
                .orElseThrow(() -> new SinittoNotFoundException("id에 해당하는 시니또를 찾을 수 없습니다."));

        helloCall.checkSiniitoIsSame(sinitto);
        if (helloCall.checkIsNotAfterEndDate()) {
            throw new CompletionConditionNotFulfilledException("서비스 종료 날짜보다 이른 날짜에 종료할 수 없습니다.");
        }

        helloCall.setReport(helloCallReportRequest.report());
        helloCall.changeStatusToPendingComplete();
    }

    @Transactional(readOnly = true)
    public List<HelloCallResponse> readOwnHelloCallBySinitto(Long memberId) {
        Sinitto sinitto = sinittoRepository.findByMemberId(memberId)
                .orElseThrow(() -> new SinittoNotFoundException("id에 해당하는 시니또를 찾을 수 없습니다."));

        List<HelloCall> helloCalls = helloCallRepository.findAllBySinitto(sinitto);

        List<HelloCallResponse> helloCallResponses = new ArrayList<>();

        for (HelloCall helloCall : helloCalls) {
            HelloCallResponse response = new HelloCallResponse(helloCall.getId(), helloCall.getSenior().getName(),
                    helloCall.getTimeSlots().stream().map(TimeSlot::getDay).toList(), helloCall.getStatus());
            helloCallResponses.add(response);
        }

        return helloCallResponses;
    }


}