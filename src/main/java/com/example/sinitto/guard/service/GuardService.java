package com.example.sinitto.guard.service;

import com.example.sinitto.guard.dto.GuardRequest;
import com.example.sinitto.guard.dto.GuardResponse;
import com.example.sinitto.guard.dto.SeniorRequest;
import com.example.sinitto.guard.dto.SeniorResponse;
import com.example.sinitto.guard.exception.SeniorNotFoundException;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import com.example.sinitto.member.exception.MemberNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GuardService {
    private final MemberRepository memberRepository;
    private final SeniorRepository seniorRepository;

    public GuardService(MemberRepository memberRepository, SeniorRepository seniorRepository){
        this.memberRepository = memberRepository;
        this.seniorRepository = seniorRepository;
    }

    @Transactional
    public GuardResponse readGuard(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException("이메일에 해당하는 멤버를 찾을 수 없습니다.")
        );

        return new GuardResponse(member.getName(), member.getEmail(), member.getPhoneNumber());
    }

    @Transactional
    public void updateGuard(Long memberId, GuardRequest guardRequest){
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException("이메일에 해당하는 멤버를 찾을 수 없습니다.")
        );

        member.updateMember(guardRequest.name(), guardRequest.email(), guardRequest.phoneNumber());
    }

    @Transactional
    public void deleteGuard(Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException("이메일에 해당하는 멤버를 찾을 수 없습니다.")
        );

        memberRepository.delete(member);
    }

    @Transactional
    public void createSenior(Long memberId, SeniorRequest seniorRequest){
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException("이메일에 해당하는 멤버를 찾을 수 없습니다.")
        );

        Senior senior = new Senior(seniorRequest.seniorName(), seniorRequest.seniorPhoneNumber(), member);

        seniorRepository.save(senior);
    }

    @Transactional
    public List<SeniorResponse> readSeniors(Long memberId){
        List<Senior> senior = seniorRepository.findByMemberId(memberId);

        return senior.stream().map(Senior::mapToResponse).toList();
    }

    @Transactional
    public SeniorResponse readOneSenior(Long memberId, Long seniorId){
        Senior senior = seniorRepository.findByIdAndMemberId(seniorId, memberId).orElseThrow(
                () -> new SeniorNotFoundException("이메일에 해당하는 시니어를 찾을 수 없습니다.")
        );

        return new SeniorResponse(senior.getId(), senior.getName(), senior.getPhoneNumber());
    }

    @Transactional
    public void updateSenior(Long memberId, Long seniorId, SeniorRequest seniorRequest){
        Senior senior = seniorRepository.findByIdAndMemberId(seniorId, memberId).orElseThrow(
                () -> new SeniorNotFoundException("이메일에 해당하는 시니어를 찾을 수 없습니다.")
        );

        senior.updateSenior(seniorRequest.seniorName(), seniorRequest.seniorPhoneNumber());
    }

    @Transactional
    public void deleteSenior(Long memberId, Long seniorId){
        Senior senior = seniorRepository.findByIdAndMemberId(seniorId, memberId).orElseThrow(
                () -> new SeniorNotFoundException("이메일에 해당하는 시니어를 찾을 수 없습니다.")
        );

        seniorRepository.delete(senior);
    }

    @Transactional
    public List<GuardResponse> readAllGuards(){
        List<Member> members = memberRepository.findByIsSinitto(false);

        return members.stream()
                .map(m -> new GuardResponse(m.getName(), m.getEmail(), m.getPhoneNumber()))
                .toList();
    }
}
