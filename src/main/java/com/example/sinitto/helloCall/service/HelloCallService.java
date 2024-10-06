package com.example.sinitto.helloCall.service;

import com.example.sinitto.auth.exception.UnauthorizedException;
import com.example.sinitto.guard.exception.SeniorNotFoundException;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.helloCall.dto.HelloCallDetailResponse;
import com.example.sinitto.helloCall.dto.HelloCallDetailUpdateRequest;
import com.example.sinitto.helloCall.dto.HelloCallRequest;
import com.example.sinitto.helloCall.dto.HelloCallResponse;
import com.example.sinitto.helloCall.entity.HelloCall;
import com.example.sinitto.helloCall.entity.TimeSlot;
import com.example.sinitto.helloCall.exception.HelloCallAlreadyExistsException;
import com.example.sinitto.helloCall.exception.HelloCallNotFoundException;
import com.example.sinitto.helloCall.repository.HelloCallRepository;
import com.example.sinitto.helloCall.repository.TimeSlotRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.exception.MemberNotFoundException;
import com.example.sinitto.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class HelloCallService {

    private final HelloCallRepository helloCallRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final SeniorRepository seniorRepository;
    private final MemberRepository memberRepository;


    public HelloCallService(HelloCallRepository helloCallRepository, TimeSlotRepository timeSlotRepository,
                            SeniorRepository seniorRepository, MemberRepository memberRepository) {
        this.helloCallRepository = helloCallRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.seniorRepository = seniorRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void createHelloCallByGuard(Long memberId, HelloCallRequest helloCallRequest) {
        Senior senior = seniorRepository.findByIdAndMemberId(helloCallRequest.seniorId(), memberId)
                .orElseThrow(() -> new SeniorNotFoundException("시니어를 찾을 수 없습니다."));

        if (helloCallRepository.existsBySenior(senior)) {
            throw new HelloCallAlreadyExistsException("이미 해당 시니어의 안부 전화 서비스가 존재합니다.");
        }

        HelloCall helloCall = new HelloCall(helloCallRequest.startDate(), helloCallRequest.endDate(),
                helloCallRequest.price(), helloCallRequest.serviceTime(), helloCallRequest.content(), senior);
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
    public List<HelloCallResponse> readAllWaitingHelloCalls() {

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
                helloCall.getTimeSlots(), helloCall.getContent(), helloCall.getSenior().getName(),
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
                helloCallDetailUpdateRequest.price(), helloCallDetailUpdateRequest.serviceTime(), helloCallDetailUpdateRequest.content());

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
}
