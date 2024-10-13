package com.example.sinitto.member.dummy;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MemberInitialData implements CommandLineRunner {

    private final MemberRepository memberRepository;

    public MemberInitialData(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        initialMember();
    }

    private void initialMember() {
        //시니또
        memberRepository.save(new Member("김철수", "010-1234-5678", "chulsoo@example.com", true));
        memberRepository.save(new Member("김유진", "010-2345-6789", "kim@example.com", true));
        memberRepository.save(new Member("이민호", "010-3456-7890", "lee@example.com", true));
        memberRepository.save(new Member("박소연", "010-4567-8901", "park@example.com", true));
        memberRepository.save(new Member("최진우", "010-5678-9012", "choi@example.com", true));

        //보호자
        memberRepository.save(new Member("정예린", "010-6789-0123", "jeong@example.com", false));
        memberRepository.save(new Member("한상훈", "010-7890-1234", "han@example.com", false));
        memberRepository.save(new Member("오수빈", "010-8901-2345", "oh@example.com", false));
        memberRepository.save(new Member("임지훈", "010-9012-3456", "lim@example.com", false));
        memberRepository.save(new Member("송하늘", "010-0123-4567", "song@example.com", false));

        //시니어
        memberRepository.save(new Member("권지민", "010-1357-2468", "kwon@example.com", false));
        memberRepository.save(new Member("배정호", "010-2468-1357", "bae@example.com", false));
        memberRepository.save(new Member("서예진", "010-3579-8642", "seo@example.com", false));
        memberRepository.save(new Member("윤수현", "010-4680-9753", "yoon@example.com", false));
        memberRepository.save(new Member("하재민", "010-5791-0864", "ha@example.com", false));
        memberRepository.save(new Member("민서영", "010-6802-1975", "min@example.com", false));
        memberRepository.save(new Member("전진우", "010-7913-2086", "jeon@example.com", false));
        memberRepository.save(new Member("나미래", "010-8024-3197", "na@example.com", false));
        memberRepository.save(new Member("임소라", "010-9135-4208", "limso@example.com", false));
        memberRepository.save(new Member("조예빈", "010-0246-5319", "jo@example.com", false));
        memberRepository.save(new Member("우지현", "010-1357-6420", "woo@example.com", false));
    }
}
