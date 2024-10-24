package com.example.sinitto.helloCall.service;

import com.example.sinitto.common.exception.BadRequestException;
import com.example.sinitto.common.exception.ConflictException;
import com.example.sinitto.common.exception.NotFoundException;
import com.example.sinitto.common.exception.UnauthorizedException;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.helloCall.dto.*;
import com.example.sinitto.helloCall.entity.HelloCall;
import com.example.sinitto.helloCall.entity.HelloCallTimeLog;
import com.example.sinitto.helloCall.entity.TimeSlot;
import com.example.sinitto.helloCall.repository.HelloCallRepository;
import com.example.sinitto.helloCall.repository.HelloCallTimeLogRepository;
import com.example.sinitto.helloCall.repository.TimeSlotRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.entity.Point;
import com.example.sinitto.point.entity.PointLog;
import com.example.sinitto.point.repository.PointLogRepository;
import com.example.sinitto.point.repository.PointRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HelloCallService {

    private final HelloCallRepository helloCallRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final SeniorRepository seniorRepository;
    private final MemberRepository memberRepository;
    private final HelloCallTimeLogRepository helloCallTimeLogRepository;
    private final PointRepository pointRepository;
    private final PointLogRepository pointLogRepository;


    public HelloCallService(HelloCallRepository helloCallRepository, TimeSlotRepository timeSlotRepository,
                            SeniorRepository seniorRepository, MemberRepository memberRepository, HelloCallTimeLogRepository helloCallTimeLogRepository,
                            PointRepository pointRepository, PointLogRepository pointLogRepository) {
        this.helloCallRepository = helloCallRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.seniorRepository = seniorRepository;
        this.memberRepository = memberRepository;
        this.helloCallTimeLogRepository = helloCallTimeLogRepository;
        this.pointRepository = pointRepository;
        this.pointLogRepository = pointLogRepository;
    }

    @Transactional
    public void createHelloCallByGuard(Long memberId, HelloCallRequest helloCallRequest) {
        Senior senior = seniorRepository.findByIdAndMemberId(helloCallRequest.seniorId(), memberId)
                .orElseThrow(() -> new NotFoundException("보호자님과 연관된 시니어가 아니거나, 시니어를 찾을 수 없습니다."));

        if (helloCallRepository.existsBySeniorAndStatusIn(senior, List.of(HelloCall.Status.WAITING, HelloCall.Status.IN_PROGRESS))) {
            throw new ConflictException("이미 해당 시니어에게 할당되어 대기중 또는 진행중인 안부 전화 서비스가 존재합니다.");
        }

        HelloCall helloCall = new HelloCall(helloCallRequest.startDate(), helloCallRequest.endDate(),
                helloCallRequest.price(), helloCallRequest.serviceTime(), helloCallRequest.requirement(), senior);
        HelloCall savedHelloCall = helloCallRepository.save(helloCall);

        for (HelloCallRequest.TimeSlot timeSlotRequest : helloCallRequest.timeSlots()) {
            TimeSlot timeSlot = new TimeSlot(timeSlotRequest.dayName(), timeSlotRequest.startTime(),
                    timeSlotRequest.endTime(), savedHelloCall);
            timeSlotRepository.save(timeSlot);
        }

        Point point = pointRepository.findByMemberIdWithWriteLock(memberId)
                .orElseThrow(() -> new NotFoundException("멤버에 연관된 포인트가 없습니다."));

        if (!point.isSufficientForDeduction(helloCall.getPrice())) {
            throw new BadRequestException("포인트가 부족합니다.");
        }

        point.deduct(helloCall.getPrice());

        pointLogRepository.save(
                new PointLog(
                        PointLog.Content.SPEND_COMPLETE_HELLO_CALL.getMessage(),
                        senior.getMember(),
                        helloCall.getPrice(),
                        PointLog.Status.SPEND_COMPLETE
                ));
    }

    @Transactional
    public List<HelloCallResponse> readAllHelloCallsByGuard(Long memberId) {
        List<Senior> seniors = seniorRepository.findByMemberId(memberId);

        List<HelloCall> helloCalls = helloCallRepository.findAllBySeniorIn(seniors);

        return helloCalls.stream()
                .map(helloCall -> new HelloCallResponse(
                        helloCall.getId(),
                        helloCall.getSenior().getName(),
                        helloCall.getTimeSlots().stream().map(TimeSlot::getDayName).toList(),
                        helloCall.getStatus()))
                .toList();
    }

    @Transactional
    public Page<HelloCallResponse> readAllWaitingHelloCallsBySinitto(Pageable pageable) {

        List<HelloCall> helloCalls = helloCallRepository.findAll();

        List<HelloCallResponse> helloCallResponses = helloCalls.stream()
                .filter(helloCall -> helloCall.getStatus().equals(HelloCall.Status.WAITING))
                .map(helloCall -> new HelloCallResponse(
                        helloCall.getId(), helloCall.getSenior().getName(),
                        helloCall.getTimeSlots().stream().map(TimeSlot::getDayName).toList(), helloCall.getStatus()
                )).toList();

        int totalElements = helloCallResponses.size();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), totalElements);

        List<HelloCallResponse> pagedResponse = helloCallResponses.subList(start, end);

        return new PageImpl<>(pagedResponse, pageable, totalElements);
    }

    @Transactional(readOnly = true)
    public HelloCallDetailResponse readHelloCallDetail(Long helloCallId) {
        HelloCall helloCall = helloCallRepository.findById(helloCallId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        List<HelloCallDetailResponse.TimeSlot> timeSlots = helloCall.getTimeSlots().stream()
                .map(timeSlot -> new HelloCallDetailResponse.TimeSlot(
                        timeSlot.getDayName(), timeSlot.getStartTime(), timeSlot.getEndTime())).toList();

        return new HelloCallDetailResponse(helloCall.getStartDate(), helloCall.getEndDate(),
                timeSlots, helloCall.getRequirement(), helloCall.getSenior().getName(),
                helloCall.getSenior().getPhoneNumber(), helloCall.getPrice());
    }

    @Transactional
    public void updateHelloCallByGuard(Long memberId, Long helloCallId, HelloCallDetailUpdateRequest helloCallDetailUpdateRequest) {
        HelloCall helloCall = helloCallRepository.findById(helloCallId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버를 찾을 수 없습니다."));

        helloCall.checkGuardIsCorrect(member);

        helloCall.updateHelloCall(helloCallDetailUpdateRequest.startDate(), helloCallDetailUpdateRequest.endDate(),
                helloCallDetailUpdateRequest.price(), helloCallDetailUpdateRequest.serviceTime(), helloCallDetailUpdateRequest.requirement());

        updateTimeSlots(helloCall, helloCallDetailUpdateRequest.timeSlots());
    }

    private void updateTimeSlots(HelloCall helloCall, List<HelloCallDetailUpdateRequest.TimeSlot> updatedTimeSlots) {
        timeSlotRepository.deleteAllByHelloCall(helloCall);
        helloCall.getTimeSlots().clear();

        for (HelloCallDetailUpdateRequest.TimeSlot updatedSlot : updatedTimeSlots) {
            TimeSlot newTimeSlot = new TimeSlot(updatedSlot.dayName(), updatedSlot.startTime(), updatedSlot.endTime(), helloCall);
            timeSlotRepository.save(newTimeSlot);
            helloCall.getTimeSlots().add(newTimeSlot);
        }
    }

    @Transactional
    public void deleteHellCallByGuard(Long memberId, Long helloCallId) {
        HelloCall helloCall = helloCallRepository.findById(helloCallId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버를 찾을 수 없습니다."));

        helloCall.checkGuardIsCorrect(member);

        Point point = pointRepository.findByMemberIdWithWriteLock(memberId)
                .orElseThrow(() -> new NotFoundException("멤버에 연관된 포인트가 없습니다."));

        point.earn(helloCall.getPrice());

        pointLogRepository.save(
                new PointLog(
                        PointLog.Content.SPEND_CANCEL_HELLO_CALL.getMessage(),
                        member,
                        helloCall.getPrice(),
                        PointLog.Status.SPEND_CANCEL)
        );

        helloCall.checkStatusIsWaiting();
        helloCallRepository.delete(helloCall);
    }

    @Transactional(readOnly = true)
    public List<HelloCallTimeLogResponse> readHelloCallTimeLogByGuard(Long memberId, Long helloCallId) {
        HelloCall helloCall = helloCallRepository.findById(helloCallId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버를 찾을 수 없습니다."));

        helloCall.checkGuardIsCorrect(member);

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
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버를 찾을 수 없습니다."));

        HelloCall helloCall = helloCallRepository.findById(helloCallId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        helloCall.checkGuardIsCorrect(member);

        if (!helloCall.checkReportIsNotNull()) {
            throw new BadRequestException("아직 안부전화 서비스가 완료되지 않았습니다.");
        }

        return new HelloCallReportResponse(helloCall.getStartDate(),
                helloCall.getEndDate(), helloCall.getMemberName(), helloCall.getReport());
    }

    @Transactional
    public void makeCompleteHelloCallByGuard(Long memberId, Long helloCallId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버를 찾을 수 없습니다."));

        HelloCall helloCall = helloCallRepository.findById(helloCallId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        helloCall.checkGuardIsCorrect(member);

        helloCall.changeStatusToComplete();

        Point sinittoPoint = pointRepository.findByMember(helloCall.getMember())
                .orElseThrow(() -> new NotFoundException("포인트 적립 받을 시니또와 연관된 포인트가 없습니다"));

        sinittoPoint.earn(helloCall.getPrice());

        pointLogRepository.save(
                new PointLog(
                        PointLog.Content.COMPLETE_HELLO_CALL_AND_EARN.getMessage(),
                        sinittoPoint.getMember(),
                        helloCall.getPrice(),
                        PointLog.Status.EARN)
        );
    }

    @Transactional(readOnly = true)
    public List<HelloCallReportResponse> readAllHelloCallReportByAdmin() {
        List<HelloCall> helloCalls = helloCallRepository.findAll();

        List<HelloCallReportResponse> helloCallReportResponses = new ArrayList<>();

        for (HelloCall helloCall : helloCalls) {
            if (helloCall.checkReportIsNotNull()) {
                HelloCallReportResponse response = new HelloCallReportResponse(helloCall.getStartDate(),
                        helloCall.getEndDate(), helloCall.getMemberName(), helloCall.getReport());
                helloCallReportResponses.add(response);
            }
        }
        return helloCallReportResponses;
    }

    @Transactional
    public void acceptHelloCallBySinitto(Long memberId, Long helloCallId) {
        HelloCall helloCall = helloCallRepository.findById(helloCallId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버가 없습니다."));

        if (!member.isSinitto()) {
            throw new UnauthorizedException("시니또가 아닙니다.");
        }

        helloCall.changeStatusToInProgress();
        helloCall.setMember(member);
    }

    @Transactional
    public void writeHelloCallStartTimeBySinitto(Long memberId, Long helloCallId) {
        HelloCall helloCall = helloCallRepository.findById(helloCallId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버를 찾을 수 없습니다."));

        helloCall.checkMemberIsRightSinitto(member);

        Optional<HelloCallTimeLog> recentLog = helloCallTimeLogRepository
                .findTopByMemberAndHelloCallOrderByStartDateAndTimeDesc(member, helloCall);

        if (recentLog.isPresent() && recentLog.get().getEndDateAndTime() == null) {
            throw new BadRequestException("이미 시작된 안부전화가 있습니다. 종료를 먼저 완료해주세요.");
        }

        HelloCallTimeLog helloCallTimeLog = new HelloCallTimeLog(helloCall, member);
        helloCallTimeLog.setStartDateAndTime(LocalDateTime.now());

        helloCallTimeLogRepository.save(helloCallTimeLog);
    }

    @Transactional
    public void writeHelloCallEndTimeBySinitto(Long memberId, Long helloCallId) {
        HelloCall helloCall = helloCallRepository.findById(helloCallId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버를 찾을 수 없습니다."));

        helloCall.checkMemberIsRightSinitto(member);

        HelloCallTimeLog helloCallTimeLog = helloCallTimeLogRepository
                .findTopByMemberAndHelloCallOrderByStartDateAndTimeDesc(member, helloCall)
                .orElseThrow(() -> new NotFoundException("안부전화 로그를 찾을 수 없습니다."));

        if (helloCallTimeLog.getEndDateAndTime() != null) {
            throw new BadRequestException("이미 종료된 안부전화입니다.");
        }

        helloCallTimeLog.setEndDateAndTime(LocalDateTime.now());
    }

    @Transactional
    public void cancelHelloCallBySinitto(Long memberId, Long helloCallId) {
        HelloCall helloCall = helloCallRepository.findById(helloCallId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버를 찾을 수 없습니다."));

        helloCall.checkMemberIsRightSinitto(member);

        helloCall.changeStatusToWaiting();
        helloCall.setMember(null);
    }

    @Transactional
    public void sendReportBySinitto(Long memberId, HelloCallReportRequest helloCallReportRequest) {
        HelloCall helloCall = helloCallRepository.findById(helloCallReportRequest.helloCallId())
                .orElseThrow(() -> new NotFoundException("id에 해당하는 안부전화 정보를 찾을 수 없습니다."));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버를 찾을 수 없습니다."));

        helloCall.checkMemberIsRightSinitto(member);

        if (helloCall.checkIsNotAfterEndDate()) {
            throw new BadRequestException("서비스 종료 날짜보다 이른 날짜에 종료할 수 없습니다.");
        }

        helloCall.setReport(helloCallReportRequest.report());
        helloCall.changeStatusToPendingComplete();
    }

    @Transactional(readOnly = true)
    public List<HelloCallResponse> readOwnHelloCallBySinitto(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버를 찾을 수 없습니다."));

        List<HelloCall> helloCalls = helloCallRepository.findAllByMember(member);

        List<HelloCallResponse> helloCallResponses = new ArrayList<>();

        for (HelloCall helloCall : helloCalls) {
            HelloCallResponse response = new HelloCallResponse(helloCall.getId(), helloCall.getSenior().getName(),
                    helloCall.getTimeSlots().stream().map(TimeSlot::getDayName).toList(), helloCall.getStatus());
            helloCallResponses.add(response);
        }

        return helloCallResponses;
    }

}
