package com.example.sinitto.common.dummy;

import com.example.sinitto.auth.service.TokenService;
import com.example.sinitto.callback.entity.Callback;
import com.example.sinitto.callback.repository.CallbackRepository;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.guardGuideline.entity.GuardGuideline;
import com.example.sinitto.guardGuideline.repository.GuardGuidelineRepository;
import com.example.sinitto.helloCall.entity.HelloCall;
import com.example.sinitto.helloCall.entity.HelloCallTimeLog;
import com.example.sinitto.helloCall.entity.TimeSlot;
import com.example.sinitto.helloCall.repository.HelloCallRepository;
import com.example.sinitto.helloCall.repository.HelloCallTimeLogRepository;
import com.example.sinitto.helloCall.repository.TimeSlotRepository;
import com.example.sinitto.helloCall.service.HelloCallService;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.entity.Point;
import com.example.sinitto.point.entity.PointLog;
import com.example.sinitto.point.repository.PointLogRepository;
import com.example.sinitto.point.repository.PointRepository;
import com.example.sinitto.review.entity.Review;
import com.example.sinitto.review.repository.ReviewRepository;
import com.example.sinitto.sinitto.entity.SinittoBankInfo;
import com.example.sinitto.sinitto.repository.SinittoBankInfoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class InitialData implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final SeniorRepository seniorRepository;
    private final PointRepository pointRepository;
    private final CallbackRepository callbackRepository;
    private final SinittoBankInfoRepository sinittoBankInfoRepository;
    private final PointLogRepository pointLogRepository;
    private final ReviewRepository reviewRepository;
    private final GuardGuidelineRepository guardGuidelineRepository;
    private final HelloCallRepository helloCallRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final HelloCallTimeLogRepository helloCallTimeLogRepository;
    private final TokenService tokenService;
    private final HelloCallService helloCallService;


    public InitialData(MemberRepository memberRepository, SeniorRepository seniorRepository,
                       PointRepository pointRepository, CallbackRepository callbackRepository,
                       SinittoBankInfoRepository sinittoBankInfoRepository, PointLogRepository pointLogRepository,
                       ReviewRepository reviewRepository, GuardGuidelineRepository guardGuidelineRepository,
                       HelloCallRepository helloCallRepository, TimeSlotRepository timeSlotRepository,
                       HelloCallTimeLogRepository helloCallTimeLogRepository, TokenService tokenService, HelloCallService helloCallService) {
        this.memberRepository = memberRepository;
        this.seniorRepository = seniorRepository;
        this.pointRepository = pointRepository;
        this.callbackRepository = callbackRepository;
        this.sinittoBankInfoRepository = sinittoBankInfoRepository;
        this.pointLogRepository = pointLogRepository;
        this.reviewRepository = reviewRepository;
        this.guardGuidelineRepository = guardGuidelineRepository;
        this.helloCallRepository = helloCallRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.helloCallTimeLogRepository = helloCallTimeLogRepository;
        this.tokenService = tokenService;
        this.helloCallService = helloCallService;
    }

    @Override
    public void run(String... args) {
        initial();
        saveRefreshTokenToRedis();
    }

    private void saveRefreshTokenToRedis() {
        System.out.println("---------------[더미데이터] 멤버별 RefreshToken---------------");
        System.out.printf("시니또1 김철수(MemberId : 1)의 refreshToken : %s%n", tokenService.generateRefreshToken("1chulsoo@example.com"));
        System.out.printf("시니또2 김유진(MemberId : 2)의 refreshToken : %s%n", tokenService.generateRefreshToken("2kim@example.com"));
        System.out.printf("시니또3 이민호(MemberId : 3)의 refreshToken : %s%n", tokenService.generateRefreshToken("3lee@example.com"));
        System.out.printf("시니또4 박소연(MemberId : 4)의 refreshToken : %s%n", tokenService.generateRefreshToken("4park@example.com"));
        System.out.printf("시니또5 최진우(MemberId : 5)의 refreshToken : %s%n", tokenService.generateRefreshToken("5choi@example.com"));
        System.out.printf("보호자1 정예린(MemberId : 6)의 refreshToken : %s%n", tokenService.generateRefreshToken("6jeong@example.com"));
        System.out.printf("보호자2 한상훈(MemberId : 7)의 refreshToken : %s%n", tokenService.generateRefreshToken("7han@example.com"));
        System.out.printf("보호자3 오수빈(MemberId : 8)의 refreshToken : %s%n", tokenService.generateRefreshToken("8oh@example.com"));
        System.out.printf("보호자4 임지훈(MemberId : 9)의 refreshToken : %s%n", tokenService.generateRefreshToken("9lim@example.com"));
        System.out.printf("보호자5 송하늘(MemberId : 10)의 refreshToken : %s%n", tokenService.generateRefreshToken("10song@example.com"));
        System.out.println("----------------------------------------------------------");
    }

    private void initial() {
        //시니또
        Member memberSinitto1 = memberRepository.save(new Member("김철수", "01012345678", "1chulsoo@example.com", true));
        sinittoBankInfoRepository.save(new SinittoBankInfo("신한은행", "123-23-444-422", memberSinitto1));
        Member memberSinitto2 = memberRepository.save(new Member("김유진", "01023456789", "2kim@example.com", true));
        sinittoBankInfoRepository.save(new SinittoBankInfo("대구은행", "446-5-11-2", memberSinitto2));
        Member memberSinitto3 = memberRepository.save(new Member("이민호", "01034567890", "3lee@example.com", true));
        sinittoBankInfoRepository.save(new SinittoBankInfo("IBK은행", "7-66-8-422", memberSinitto3));
        Member memberSinitto4 = memberRepository.save(new Member("박소연", "01045678901", "4park@example.com", true));
        sinittoBankInfoRepository.save(new SinittoBankInfo("토스뱅크", "777-1-2-3", memberSinitto4));
        Member memberSinitto5 = memberRepository.save(new Member("최진우", "01056789012", "5choi@example.com", true));
        sinittoBankInfoRepository.save(new SinittoBankInfo("기업은행", "96-6-99-45", memberSinitto5));

        //보호자
        Member guard1 = memberRepository.save(new Member("정예린", "01067890123", "6jeong@example.com", false));
        Member guard2 = memberRepository.save(new Member("한상훈", "01078901234", "7han@example.com", false));
        Member guard3 = memberRepository.save(new Member("오수빈", "01089012345", "8oh@example.com", false));
        Member guard4 = memberRepository.save(new Member("임지훈", "01090123456", "9lim@example.com", false));
        Member guard5 = memberRepository.save(new Member("송하늘", "01001234567", "10song@example.com", false));

        //시니어
        Senior senior1 = seniorRepository.save(new Senior("권지민", "01013572468", guard1));
        Senior senior2 = seniorRepository.save(new Senior("배정호", "01024681357", guard1));
        Senior senior3 = seniorRepository.save(new Senior("윤수현", "01046809753", guard1));
        Senior senior4 = seniorRepository.save(new Senior("하재민", "01057910864", guard2));
        Senior senior5 = seniorRepository.save(new Senior("민서영", "01068021975", guard2));
        Senior senior6 = seniorRepository.save(new Senior("전진우", "01079132086", guard3));
        Senior senior7 = seniorRepository.save(new Senior("나미래", "01080243197", guard3));
        Senior senior8 = seniorRepository.save(new Senior("임소라", "01091354208", guard4));
        Senior senior9 = seniorRepository.save(new Senior("조예빈", "01002465319", guard4));
        Senior senior10 = seniorRepository.save(new Senior("우지현", "01013576420", guard5));
        Senior senior11 = seniorRepository.save(new Senior("서예진", "01035798642", guard5));
        Senior senior12 = seniorRepository.save(new Senior("이도훈", "01047389557", guard1));
        Senior senior13 = seniorRepository.save(new Senior("정성훈", "01095502346", guard1));
        Senior senior14 = seniorRepository.save(new Senior("이지호", "01099329746", guard1));
        Senior senior15 = seniorRepository.save(new Senior("김강민", "01030957259", guard1));
        Senior senior16 = seniorRepository.save(new Senior("김지훈", "01062479134", guard2));
        Senior senior17 = seniorRepository.save(new Senior("박서연", "01083649205", guard2));
        Senior senior18 = seniorRepository.save(new Senior("홍승현", "01074102368", guard3));
        Senior senior19 = seniorRepository.save(new Senior("이수빈", "01051908364", guard3));
        Senior senior20 = seniorRepository.save(new Senior("최민지", "01096347281", guard4));
        Senior senior21 = seniorRepository.save(new Senior("백승민", "01018539647", guard4));
        Senior senior22 = seniorRepository.save(new Senior("윤지수", "01073214950", guard5));
        Senior senior23 = seniorRepository.save(new Senior("한유정", "01058231946", guard5));
        Senior senior24 = seniorRepository.save(new Senior("전현우", "01031905728", guard1));
        Senior senior25 = seniorRepository.save(new Senior("송예은", "01071489352", guard2));
        Senior senior26 = seniorRepository.save(new Senior("고유민", "01040257193", guard2));
        Senior senior27 = seniorRepository.save(new Senior("황지호", "01062079351", guard3));
        Senior senior28 = seniorRepository.save(new Senior("김도현", "01081724630", guard3));
        Senior senior29 = seniorRepository.save(new Senior("서민재", "01079320584", guard4));
        Senior senior30 = seniorRepository.save(new Senior("오예림", "01091268437", guard5));

        //포인트와 포인트로그
        pointRepository.save(new Point(50000, memberSinitto1));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto1, 50000, PointLog.Status.CHARGE_FAIL));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto1, 50000, PointLog.Status.CHARGE_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto1, 50000, PointLog.Status.CHARGE_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto1, 50000, PointLog.Status.CHARGE_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_CALLBACK_AND_EARN.getMessage(), memberSinitto1, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_HELLO_CALL_AND_EARN.getMessage(), memberSinitto1, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_HELLO_CALL.getMessage(), memberSinitto1, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_CALLBACK.getMessage(), memberSinitto1, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_CANCEL_HELLO_CALL.getMessage(), memberSinitto1, 50000, PointLog.Status.SPEND_CANCEL));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto1, 50000, PointLog.Status.WITHDRAW_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto1, 50000, PointLog.Status.WITHDRAW_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto1, 50000, PointLog.Status.WITHDRAW_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto1, 50000, PointLog.Status.WITHDRAW_FAIL_AND_RESTORE_POINT));

        pointRepository.save(new Point(50000, memberSinitto2));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto2, 50000, PointLog.Status.CHARGE_FAIL));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto2, 50000, PointLog.Status.CHARGE_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto2, 50000, PointLog.Status.CHARGE_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto2, 50000, PointLog.Status.CHARGE_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_CALLBACK_AND_EARN.getMessage(), memberSinitto2, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_HELLO_CALL_AND_EARN.getMessage(), memberSinitto2, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_HELLO_CALL.getMessage(), memberSinitto2, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_CALLBACK.getMessage(), memberSinitto2, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_CANCEL_HELLO_CALL.getMessage(), memberSinitto2, 50000, PointLog.Status.SPEND_CANCEL));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto2, 50000, PointLog.Status.WITHDRAW_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto2, 50000, PointLog.Status.WITHDRAW_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto2, 50000, PointLog.Status.WITHDRAW_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto2, 50000, PointLog.Status.WITHDRAW_FAIL_AND_RESTORE_POINT));

        pointRepository.save(new Point(50000, memberSinitto3));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto3, 50000, PointLog.Status.CHARGE_FAIL));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto3, 50000, PointLog.Status.CHARGE_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto3, 50000, PointLog.Status.CHARGE_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto3, 50000, PointLog.Status.CHARGE_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_CALLBACK_AND_EARN.getMessage(), memberSinitto3, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_HELLO_CALL_AND_EARN.getMessage(), memberSinitto3, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_HELLO_CALL.getMessage(), memberSinitto3, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_CALLBACK.getMessage(), memberSinitto3, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_CANCEL_HELLO_CALL.getMessage(), memberSinitto3, 50000, PointLog.Status.SPEND_CANCEL));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto3, 50000, PointLog.Status.WITHDRAW_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto3, 50000, PointLog.Status.WITHDRAW_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto3, 50000, PointLog.Status.WITHDRAW_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto3, 50000, PointLog.Status.WITHDRAW_FAIL_AND_RESTORE_POINT));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto3, 50000, PointLog.Status.CHARGE_COMPLETE));

        pointRepository.save(new Point(50000, memberSinitto4));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto4, 50000, PointLog.Status.CHARGE_FAIL));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto4, 50000, PointLog.Status.CHARGE_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto4, 50000, PointLog.Status.CHARGE_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto4, 50000, PointLog.Status.CHARGE_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_CALLBACK_AND_EARN.getMessage(), memberSinitto4, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_HELLO_CALL_AND_EARN.getMessage(), memberSinitto4, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_HELLO_CALL.getMessage(), memberSinitto4, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_CALLBACK.getMessage(), memberSinitto4, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_CANCEL_HELLO_CALL.getMessage(), memberSinitto4, 50000, PointLog.Status.SPEND_CANCEL));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto4, 50000, PointLog.Status.WITHDRAW_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto4, 50000, PointLog.Status.WITHDRAW_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto4, 50000, PointLog.Status.WITHDRAW_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto4, 50000, PointLog.Status.WITHDRAW_FAIL_AND_RESTORE_POINT));

        pointRepository.save(new Point(50000, memberSinitto5));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto5, 50000, PointLog.Status.CHARGE_FAIL));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto5, 50000, PointLog.Status.CHARGE_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto5, 50000, PointLog.Status.CHARGE_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), memberSinitto5, 50000, PointLog.Status.CHARGE_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_CALLBACK_AND_EARN.getMessage(), memberSinitto5, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_HELLO_CALL_AND_EARN.getMessage(), memberSinitto5, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_HELLO_CALL.getMessage(), memberSinitto5, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_CALLBACK.getMessage(), memberSinitto5, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_CANCEL_HELLO_CALL.getMessage(), memberSinitto5, 50000, PointLog.Status.SPEND_CANCEL));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto5, 50000, PointLog.Status.WITHDRAW_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto5, 50000, PointLog.Status.WITHDRAW_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto5, 50000, PointLog.Status.WITHDRAW_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), memberSinitto5, 50000, PointLog.Status.WITHDRAW_FAIL_AND_RESTORE_POINT));

        pointRepository.save(new Point(50000, guard1));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard1, 50000, PointLog.Status.CHARGE_FAIL));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard1, 50000, PointLog.Status.CHARGE_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard1, 50000, PointLog.Status.CHARGE_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard1, 50000, PointLog.Status.CHARGE_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_CALLBACK_AND_EARN.getMessage(), guard1, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_HELLO_CALL_AND_EARN.getMessage(), guard1, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_HELLO_CALL.getMessage(), guard1, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_CALLBACK.getMessage(), guard1, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_CANCEL_HELLO_CALL.getMessage(), guard1, 50000, PointLog.Status.SPEND_CANCEL));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard1, 50000, PointLog.Status.WITHDRAW_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard1, 50000, PointLog.Status.WITHDRAW_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard1, 50000, PointLog.Status.WITHDRAW_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard1, 50000, PointLog.Status.WITHDRAW_FAIL_AND_RESTORE_POINT));

        pointRepository.save(new Point(50000, guard2));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard2, 50000, PointLog.Status.CHARGE_FAIL));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard2, 50000, PointLog.Status.CHARGE_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard2, 50000, PointLog.Status.CHARGE_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard2, 50000, PointLog.Status.CHARGE_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_CALLBACK_AND_EARN.getMessage(), guard2, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_HELLO_CALL_AND_EARN.getMessage(), guard2, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_HELLO_CALL.getMessage(), guard2, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_CALLBACK.getMessage(), guard2, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_CANCEL_HELLO_CALL.getMessage(), guard2, 50000, PointLog.Status.SPEND_CANCEL));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard2, 50000, PointLog.Status.WITHDRAW_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard2, 50000, PointLog.Status.WITHDRAW_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard2, 50000, PointLog.Status.WITHDRAW_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard2, 50000, PointLog.Status.WITHDRAW_FAIL_AND_RESTORE_POINT));

        pointRepository.save(new Point(50000, guard3));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard3, 50000, PointLog.Status.CHARGE_FAIL));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard3, 50000, PointLog.Status.CHARGE_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard3, 50000, PointLog.Status.CHARGE_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard3, 50000, PointLog.Status.CHARGE_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_CALLBACK_AND_EARN.getMessage(), guard3, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_HELLO_CALL_AND_EARN.getMessage(), guard3, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_HELLO_CALL.getMessage(), guard3, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_CALLBACK.getMessage(), guard3, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_CANCEL_HELLO_CALL.getMessage(), guard3, 50000, PointLog.Status.SPEND_CANCEL));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard3, 50000, PointLog.Status.WITHDRAW_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard3, 50000, PointLog.Status.WITHDRAW_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard3, 50000, PointLog.Status.WITHDRAW_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard3, 50000, PointLog.Status.WITHDRAW_FAIL_AND_RESTORE_POINT));

        pointRepository.save(new Point(50000, guard4));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard4, 50000, PointLog.Status.CHARGE_FAIL));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard4, 50000, PointLog.Status.CHARGE_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard4, 50000, PointLog.Status.CHARGE_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard4, 50000, PointLog.Status.CHARGE_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_CALLBACK_AND_EARN.getMessage(), guard4, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_HELLO_CALL_AND_EARN.getMessage(), guard4, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_HELLO_CALL.getMessage(), guard4, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_CALLBACK.getMessage(), guard4, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_CANCEL_HELLO_CALL.getMessage(), guard4, 50000, PointLog.Status.SPEND_CANCEL));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard4, 50000, PointLog.Status.WITHDRAW_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard4, 50000, PointLog.Status.WITHDRAW_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard4, 50000, PointLog.Status.WITHDRAW_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard4, 50000, PointLog.Status.WITHDRAW_FAIL_AND_RESTORE_POINT));

        pointRepository.save(new Point(50000, guard5));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard5, 50000, PointLog.Status.CHARGE_FAIL));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard5, 50000, PointLog.Status.CHARGE_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard5, 50000, PointLog.Status.CHARGE_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard5, 50000, PointLog.Status.CHARGE_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_CALLBACK_AND_EARN.getMessage(), guard5, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_HELLO_CALL_AND_EARN.getMessage(), guard5, 50000, PointLog.Status.EARN));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_HELLO_CALL.getMessage(), guard5, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_COMPLETE_CALLBACK.getMessage(), guard5, 50000, PointLog.Status.SPEND_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.SPEND_CANCEL_HELLO_CALL.getMessage(), guard5, 50000, PointLog.Status.SPEND_CANCEL));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard5, 50000, PointLog.Status.WITHDRAW_REQUEST));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard5, 50000, PointLog.Status.WITHDRAW_WAITING));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard5, 50000, PointLog.Status.WITHDRAW_COMPLETE));
        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), guard5, 50000, PointLog.Status.WITHDRAW_FAIL_AND_RESTORE_POINT));

        //콜백
        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior1));
        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior1));
        callbackRepository.save(new Callback(Callback.Status.WAITING, senior1));

        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior2));
        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior2));
        callbackRepository.save(new Callback(Callback.Status.WAITING, senior2));

        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior3));
        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior3));
        callbackRepository.save(new Callback(Callback.Status.WAITING, senior3));

        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior4));
        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior4));
        callbackRepository.save(new Callback(Callback.Status.WAITING, senior4));

        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior5));
        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior5));
        callbackRepository.save(new Callback(Callback.Status.WAITING, senior5));

        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior6));
        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior6));
        callbackRepository.save(new Callback(Callback.Status.WAITING, senior6));

        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior7));
        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior7));
        callbackRepository.save(new Callback(Callback.Status.WAITING, senior7));

        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior8));
        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior8));
        callbackRepository.save(new Callback(Callback.Status.WAITING, senior8));

        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior9));
        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior9));
        callbackRepository.save(new Callback(Callback.Status.WAITING, senior9));

        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior10));
        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior10));
        callbackRepository.save(new Callback(Callback.Status.WAITING, senior10));

        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior11));
        callbackRepository.save(new Callback(Callback.Status.COMPLETE, senior11));
        callbackRepository.save(new Callback(Callback.Status.WAITING, senior11));

        //리뷰
        reviewRepository.save(new Review(5, 4, 5, "테스트용 리뷰 1", guard1));
        reviewRepository.save(new Review(5, 2, 2, "테스트용 리뷰 2", guard2));
        reviewRepository.save(new Review(2, 3, 1, "테스트용 리뷰 3", guard3));
        reviewRepository.save(new Review(4, 1, 4, "테스트용 리뷰 4", guard4));
        reviewRepository.save(new Review(2, 5, 3, "테스트용 리뷰 5", guard5));

        //가이드라인
        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.TAXI, "노인복지센터까지 택시 이용 가이드라인", "출발지: 충청북도 청주시 흥덕구 복대1동 555-1, 목적지: 충청북도 청주시 상당구 대성로 172번길 21 (청주시노인종합복지관). 어머니께서 집에서 출발하십니다. 확인 후 택시 호출해주세요. 결제는 어머니께서 직접 현금으로 하십니다.", senior1));
        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.TAXI, "청주터미널로 가는 택시 이용 가이드라인", "출발지: 충청북도 청주시 흥덕구 복대1동 555-1, 목적지: 충청북도 청주시 흥덕구 가로수로 120 (청주터미널). 어머니는 터미널로 가실 때 주로 오전 시간에 출발하십니다. 확인 후 택시 호출해주세요.", senior1));
        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.TAXI, "청주대학교 병원까지 택시 이용 가이드라인", "출발지: 충청북도 청주시 흥덕구 복대1동 555-1, 목적지: 충청북도 청주시 서원구 청남로 135 (청주대학교 병원). 병원 예약 시간에 맞춰 오전에 출발합니다. 결제는 어머니께서 직접 하실 예정입니다.", senior1));
        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.DELIVERY, "피자 주문", "가게명: 청주피자 청주지점, 메뉴명: 슈퍼콤보 피자 (라지), 가격: 23000원, 보통 슈퍼콤보 피자를 시키십니다. 혹시 크러스트 추가 원하시면 치즈크러스트로 추가해주세요.", senior1));

        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.TAXI, "동네 병원까지 택시 이용 가이드라인", "출발지: 충청북도 청주시 흥덕구 비하동 123-4, 목적지: 충청북도 청주시 흥덕구 비하로 55 (비하병원). 병원에 갈 때는 보통 오전 10시에 출발하십니다. 결제는 현금으로 하실 예정입니다.", senior2));
        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.TAXI, "청주역까지 택시 이용 가이드라인", "출발지: 충청북도 청주시 흥덕구 비하동 123-4, 목적지: 충청북도 청주시 흥덕구 팔봉로 54 (청주역). 오전 출발 예정이며, 어머니께서 직접 결제하실 예정입니다.", senior2));
        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.DELIVERY, "치킨 주문", "가게명: 여수정통치킨 여수점, 메뉴명: 순살후라이드, 가격: 18000원, 주로 순살후라이드를 드시지만 양념이 필요하면 간장양념치킨으로 변경 부탁드립니다.", senior2));
        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.TAXI, "동네 마트까지 택시 이용 가이드라인", "출발지: 충청북도 청주시 흥덕구 비하동 123-4, 목적지: 충청북도 청주시 흥덕구 마트로 32 (비하동 마트). 장을 볼 때 자주 이용하시며, 결제는 항상 현금으로 하십니다.", senior2));

        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.TAXI, "충북대병원까지 택시 이용 가이드라인", "출발지: 충청북도 청주시 상당구 용암동 345-6, 목적지: 충청북도 청주시 상당구 중흥로 22 (충북대병원). 병원 예약 시간에 맞춰 오전에 출발합니다. 결제는 현금으로 하실 예정입니다.", senior3));
        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.TAXI, "청주공항까지 택시 이용 가이드라인", "출발지: 충청북도 청주시 상당구 용암동 345-6, 목적지: 충청북도 청주시 청원구 내수읍 오창대로 607 (청주국제공항). 항공편에 맞춰 오전에 출발할 예정입니다.", senior3));
        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.DELIVERY, "족발 주문", "가게명: 청주족발전문점, 메뉴명: 앞다리 족발 (중), 가격: 29000원, 보통 앞다리 족발을 드시며, 추가 요청사항은 무조건 무김치를 함께 보내달라고 하십니다.", senior3));
        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.DELIVERY, "한식당 배달 주문", "가게명: 청주미소한식당, 메뉴명: 된장찌개, 가격: 8000원, 된장찌개를 자주 드시며, 나물 반찬을 추가로 요청하시면 됩니다.", senior3));

        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.TAXI, "청주 성안길시장까지 택시 이용 가이드라인", "출발지: 충청북도 청주시 흥덕구 복대동 78-3, 목적지: 충청북도 청주시 상당구 성안길 112 (성안길시장). 시장에 가실 때 보통 점심 직후에 출발하십니다. 결제는 어머니께서 현금으로 하실 예정입니다.", senior4));
        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.TAXI, "청주문화재단까지 택시 이용 가이드라인", "출발지: 충청북도 청주시 흥덕구 복대동 78-3, 목적지: 충청북도 청주시 흥덕구 공단로 108 (청주문화재단). 문화 활동을 위해 자주 가십니다. 결제는 현금으로 하실 예정입니다.", senior4));
        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.DELIVERY, "불고기 백반 주문", "가게명: 청주진가네한식당, 메뉴명: 불고기 백반, 가격: 12000원, 불고기 백반을 즐겨 드시고 반찬 추가는 김치와 나물로 요청하시면 됩니다.", senior4));
        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.DELIVERY, "김밥 주문", "가게명: 청주광장김밥, 메뉴명: 참치김밥 2줄, 가격: 8000원, 참치김밥을 2줄 주문하십니다. 혹시 추가 요청사항이 있으면 단무지를 넉넉히 부탁드리세요.", senior4));

        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.TAXI, "서원구교회까지 택시 이용 가이드라인", "출발지: 충청북도 청주시 흥덕구 복대동 12-34, 목적지: 충청북도 청주시 서원구 성당길 12 (서원구교회). 주일 예배 참석을 위해 교회로 가십니다. 예배 시간 전에 미리 도착할 수 있도록 조정해주세요.", senior5));
        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.TAXI, "청주 시립도서관까지 택시 이용 가이드라인", "출발지: 충청북도 청주시 흥덕구 복대동 12-34, 목적지: 충청북도 청주시 서원구 수곡로 108 (청주 시립도서관). 주로 설정한 출발지에서 출발하시지만 따로 요청하시면 변경부탁드릴게요.", senior5));
        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.DELIVERY, "떡볶이 주문", "가게명: 청주신당떡볶이, 메뉴명: 매운 떡볶이 (중), 가격: 9000원, 매운 떡볶이를 자주 드시지만, 간혹 덜 맵게 요청하시는 경우가 있으니 주문 시 주의해주세요.", senior5));
        guardGuidelineRepository.save(new GuardGuideline(GuardGuideline.Type.DELIVERY, "비빔밥 주문", "가게명: 청주미소한식당, 메뉴명: 돌솥비빔밥, 가격: 11000원, 돌솥비빔밥을 즐겨 드시며 나물은 조금 더 넣어달라고 요청하시면 좋습니다.", senior5));

        //안부전화
        HelloCall helloCall1 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 8, 5), LocalDate.of(2024, 10, 1), 13000, 10, "어머님께서 매일 아침 등산을 하시는 걸 좋아하세요. 요즘 날씨가 추워졌는데, 건강하게 등산을 잘 다니시는지 여쭤봐 주세요. 등산 이야기를 하면 기분이 좋아지실 거예요.", senior1));
        HelloCall helloCall2 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 8, 2), LocalDate.of(2024, 10, 1), 14000, 10, "어머님께서 손주들 이야기를 굉장히 좋아하십니다. 최근에 손주들이 학교에서 무엇을 하고 있는지 말씀드리면 아주 즐거워하세요. 손주들과 관련된 이야기로 대화를 시작해 주세요.", senior2));
        HelloCall helloCall3 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 10, 5), LocalDate.of(2024, 11, 7), 13000, 15, "아버님은 오래된 한국 영화를 좋아하세요. 요즘 어떤 영화가 재미있었는지 물어보시면, 아마 영화 이야기를 하며 기분 좋게 통화하실 겁니다.", senior3));
        HelloCall helloCall4 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 10, 5), LocalDate.of(2024, 11, 8), 7500, 15, "어머님께서는 정원을 돌보는 것을 좋아하세요. 요즘 날씨 때문에 정원을 돌보시기 어려운지, 새로 심은 꽃들은 잘 자라고 있는지 여쭤봐 주세요.", senior4));
        HelloCall helloCall5 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 10, 5), LocalDate.of(2024, 11, 9), 6000, 5, "아버님께서는 요즘 건강에 많이 신경을 쓰고 계세요. 최근 혈압이나 운동을 잘하고 계신지, 건강 관련해서 대화를 나눠주시면 좋을 것 같아요.", senior5));
        HelloCall helloCall6 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 8, 3), LocalDate.of(2024, 10, 1), 13500, 5, "어머님께서는 옛 친구들 이야기를 좋아하세요. 요즘 친구들과 자주 연락하시거나 만나시는지 물어봐 주시고, 친구들 근황에 대해 여쭤보시면 즐거워하실 겁니다.", senior6));
        HelloCall helloCall7 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 8, 4), LocalDate.of(2024, 10, 1), 15000, 10, "아버님께서는 요즘 책 읽는 것을 좋아하십니다. 최근에 어떤 책을 읽고 계신지 여쭤보시고, 책과 관련된 대화를 이어나가시면 좋을 것 같아요.", senior7));
        HelloCall helloCall8 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 8, 6), LocalDate.of(2024, 10, 1), 7500, 10, "어머님은 전통 음식 만드는 것을 좋아하세요. 최근에 어떤 음식을 만들어 드셨는지, 특히 요즘 김장 준비는 잘하고 계신지 여쭤봐 주세요.", senior8));
        HelloCall helloCall9 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 6, 7), LocalDate.of(2024, 9, 8), 8500, 15, "아버님께서는 요즘 운동을 자주 하려고 하시는데, 날씨가 추워지면서 잘하고 계신지 물어봐 주세요. 운동과 관련된 대화를 나누면 좋을 것 같습니다.", senior12));
        HelloCall helloCall10 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 6, 5), LocalDate.of(2024, 9, 8), 7550, 15, "어머님께서는 최근에 동네 산책을 즐겨 하십니다. 요즘 날씨 때문에 산책이 어려우신지, 산책하시며 만나는 사람들과 근황을 나누고 계신지 물어봐 주시면 좋을 것 같아요.", senior13));
        HelloCall helloCall11 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 6, 5), LocalDate.of(2024, 9, 6), 16550, 5, "아버님께서는 요즘 음악 듣는 시간을 즐기세요. 특히 트로트를 좋아하시는데, 최근에 어떤 노래를 들으셨는지 여쭤봐 주세요. 음악 이야기로 대화를 시작하면 기분 좋으실 거예요.", senior14));
        HelloCall helloCall12 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 9, 2), LocalDate.of(2024, 10, 1), 12500, 10, "어머님께서는 뜨개질을 즐기세요. 요즘 어떤 작품을 만들고 계신지 여쭤보고, 새로운 작품 계획이 있으신지 물어봐 주세요.", senior16));
        HelloCall helloCall13 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 9, 3), LocalDate.of(2024, 10, 2), 14000, 15, "아버님께서는 고전 문학을 좋아하세요. 최근에 읽으신 책이나 추천하실 만한 책이 있으신지 대화를 시작해 주세요.", senior17));
        HelloCall helloCall14 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 9, 4), LocalDate.of(2024, 10, 3), 13500, 10, "어머님께서는 요리를 매우 좋아하십니다. 최근에 어떤 음식을 만드셨는지 여쭤보시면 기분 좋은 대화를 나누실 수 있을 거예요.", senior18));
        HelloCall helloCall15 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 9, 5), LocalDate.of(2024, 10, 4), 14500, 5, "아버님께서는 오래된 추억 이야기를 자주 하세요. 어린 시절의 추억에 대해 물어보시면 즐거운 대화를 이어가실 겁니다.", senior19));
        HelloCall helloCall16 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 9, 6), LocalDate.of(2024, 10, 5), 12000, 15, "어머님께서는 손으로 만드는 공예품을 좋아하세요. 최근에 어떤 작품을 완성하셨는지 이야기 나눠보세요.", senior20));
        HelloCall helloCall17 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 9, 7), LocalDate.of(2024, 10, 6), 15500, 10, "아버님께서는 최근 건강 관리에 신경을 많이 쓰고 계십니다. 운동이나 식단 관련 이야기를 나누시면 좋을 것 같습니다.", senior21));
        HelloCall helloCall18 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 9, 8), LocalDate.of(2024, 10, 7), 16000, 10, "어머님께서는 손주들 사진을 자주 보신다고 합니다. 최근에 손주들이 어떻게 지내고 있는지 물어보시면 기분 좋게 대화하실 거예요.", senior22));
        HelloCall helloCall19 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 9, 9), LocalDate.of(2024, 10, 8), 11000, 15, "아버님께서는 오래된 음악을 즐겨 들으십니다. 좋아하시는 노래나 추억의 음악에 대해 이야기 나눠보세요.", senior23));
        HelloCall helloCall20 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 9, 10), LocalDate.of(2024, 10, 9), 10500, 5, "어머님께서는 식물 기르는 것을 좋아하세요. 최근에 새로운 식물을 들이셨는지, 잘 자라고 있는지 물어봐 주세요.", senior24));
        HelloCall helloCall21 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 9, 11), LocalDate.of(2024, 10, 10), 15000, 15, "아버님께서는 스포츠 경기를 즐겨 보십니다. 최근에 어떤 경기를 보셨는지, 가장 기억에 남는 순간이 무엇인지 물어봐 주세요.", senior25));
        HelloCall helloCall22 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 9, 12), LocalDate.of(2024, 10, 11), 13000, 10, "어머님께서는 주말에 친구들과 산책을 즐기십니다. 요즘 산책이 편하신지, 친구들과 자주 만나시는지 여쭤봐 주세요.", senior26));
        HelloCall helloCall23 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 9, 13), LocalDate.of(2024, 10, 12), 14500, 15, "아버님께서는 바둑 두는 것을 좋아하세요. 최근에 바둑을 두셨는지, 바둑에 대한 이야기를 나누면 좋을 것 같습니다.", senior27));
        HelloCall helloCall24 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 9, 14), LocalDate.of(2024, 10, 13), 12500, 5, "어머님께서는 정원 가꾸는 일을 즐기십니다. 최근에 심은 꽃이나 식물들이 잘 자라고 있는지 물어봐 주세요.", senior28));
        HelloCall helloCall25 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 9, 15), LocalDate.of(2024, 10, 14), 14000, 10, "아버님께서는 요즘 정치에 관심이 많으십니다. 최근 뉴스나 정치 이슈에 대해 대화를 나눠보세요.", senior29));
        HelloCall helloCall26 = helloCallRepository.save(new HelloCall(LocalDate.of(2024, 9, 16), LocalDate.of(2024, 10, 15), 11000, 5, "어머님께서는 손주들 사진을 자주 보신다고 합니다. 최근에 손주들이 어떻게 지내고 있는지 물어보시면 즐거운 대화를 나눌 수 있을 겁니다.", senior30));

        //안부전화별 타임슬롯
        timeSlotRepository.save(new TimeSlot("월", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall1));
        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(16, 30), LocalTime.of(18, 30), helloCall1));
        timeSlotRepository.save(new TimeSlot("금", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall1));

        timeSlotRepository.save(new TimeSlot("월", LocalTime.of(10, 0), LocalTime.of(12, 0), helloCall2));
        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(10, 30), LocalTime.of(12, 30), helloCall2));

        timeSlotRepository.save(new TimeSlot("월", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall3));
        timeSlotRepository.save(new TimeSlot("화", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall3));
        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall3));
        timeSlotRepository.save(new TimeSlot("목", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall3));
        timeSlotRepository.save(new TimeSlot("금", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall3));

        timeSlotRepository.save(new TimeSlot("화", LocalTime.of(15, 30), LocalTime.of(17, 30), helloCall4));
        timeSlotRepository.save(new TimeSlot("목", LocalTime.of(16, 30), LocalTime.of(18, 30), helloCall4));
        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall4));
        timeSlotRepository.save(new TimeSlot("일", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall4));

        timeSlotRepository.save(new TimeSlot("화", LocalTime.of(17, 0), LocalTime.of(19, 0), helloCall5));
        timeSlotRepository.save(new TimeSlot("목", LocalTime.of(17, 0), LocalTime.of(19, 0), helloCall5));
        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(19, 0), LocalTime.of(21, 0), helloCall5));

        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall6));
        timeSlotRepository.save(new TimeSlot("일", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall6));

        timeSlotRepository.save(new TimeSlot("월", LocalTime.of(15, 0), LocalTime.of(17, 0), helloCall7));

        timeSlotRepository.save(new TimeSlot("월", LocalTime.of(20, 0), LocalTime.of(22, 0), helloCall8));
        timeSlotRepository.save(new TimeSlot("화", LocalTime.of(20, 0), LocalTime.of(22, 0), helloCall8));
        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(20, 0), LocalTime.of(22, 0), helloCall8));

        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall9));
        timeSlotRepository.save(new TimeSlot("금", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall9));
        timeSlotRepository.save(new TimeSlot("일", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall9));

        timeSlotRepository.save(new TimeSlot("월", LocalTime.of(11, 0), LocalTime.of(13, 0), helloCall10));
        timeSlotRepository.save(new TimeSlot("화", LocalTime.of(11, 0), LocalTime.of(13, 0), helloCall10));

        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall11));
        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall11));
        timeSlotRepository.save(new TimeSlot("일", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall11));

        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall12));
        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall12));
        timeSlotRepository.save(new TimeSlot("일", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall12));

        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall13));
        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall13));
        timeSlotRepository.save(new TimeSlot("일", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall13));

        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall14));
        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall14));
        timeSlotRepository.save(new TimeSlot("일", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall14));

        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall15));
        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall15));
        timeSlotRepository.save(new TimeSlot("일", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall15));

        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall16));
        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall16));
        timeSlotRepository.save(new TimeSlot("일", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall16));

        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall17));
        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall17));
        timeSlotRepository.save(new TimeSlot("일", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall17));

        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall18));
        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall18));
        timeSlotRepository.save(new TimeSlot("일", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall18));

        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall19));
        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall19));
        timeSlotRepository.save(new TimeSlot("일", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall19));

        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall20));
        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall20));
        timeSlotRepository.save(new TimeSlot("일", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall20));

        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall21));
        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall21));
        timeSlotRepository.save(new TimeSlot("일", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall21));

        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall22));
        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall22));
        timeSlotRepository.save(new TimeSlot("일", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall22));

        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall23));
        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall23));
        timeSlotRepository.save(new TimeSlot("일", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall23));

        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall24));
        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall24));
        timeSlotRepository.save(new TimeSlot("일", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall24));

        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall25));
        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall25));
        timeSlotRepository.save(new TimeSlot("일", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall25));

        timeSlotRepository.save(new TimeSlot("수", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall26));
        timeSlotRepository.save(new TimeSlot("토", LocalTime.of(18, 30), LocalTime.of(20, 30), helloCall26));
        timeSlotRepository.save(new TimeSlot("일", LocalTime.of(18, 0), LocalTime.of(20, 0), helloCall26));

        //시니또 안부전화 할당
        helloCallService.acceptHelloCallBySinitto(memberSinitto1.getId(), helloCall1.getId());
        helloCallService.acceptHelloCallBySinitto(memberSinitto1.getId(), helloCall2.getId());
        helloCallService.acceptHelloCallBySinitto(memberSinitto2.getId(), helloCall3.getId());
        helloCallService.acceptHelloCallBySinitto(memberSinitto2.getId(), helloCall4.getId());
        helloCallService.acceptHelloCallBySinitto(memberSinitto3.getId(), helloCall5.getId());
        helloCallService.acceptHelloCallBySinitto(memberSinitto4.getId(), helloCall6.getId());
        helloCallService.acceptHelloCallBySinitto(memberSinitto4.getId(), helloCall7.getId());
        helloCallService.acceptHelloCallBySinitto(memberSinitto5.getId(), helloCall8.getId());
        helloCallService.acceptHelloCallBySinitto(memberSinitto5.getId(), helloCall9.getId());
        helloCallService.acceptHelloCallBySinitto(memberSinitto2.getId(), helloCall10.getId());
        helloCallService.acceptHelloCallBySinitto(memberSinitto3.getId(), helloCall11.getId());

        //안부전화 실시 타임로그
        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall1, memberSinitto3, LocalDateTime.of(2024, 8, 5, 18, 30), LocalDateTime.of(2024, 8, 5, 18, 40)));
        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall1, memberSinitto3, LocalDateTime.of(2024, 8, 7, 18, 30), LocalDateTime.of(2024, 8, 7, 18, 40)));
        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall1, memberSinitto1, LocalDateTime.of(2024, 8, 9, 18, 30), LocalDateTime.of(2024, 8, 9, 18, 40)));
        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall1, memberSinitto1, LocalDateTime.of(2024, 8, 11, 18, 30), LocalDateTime.of(2024, 8, 11, 18, 40)));
        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall1, memberSinitto1, LocalDateTime.of(2024, 8, 13, 18, 30), LocalDateTime.of(2024, 8, 13, 18, 40)));

        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall2, memberSinitto1, LocalDateTime.of(2024, 8, 2, 10, 30), LocalDateTime.of(2024, 8, 2, 10, 40)));
        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall2, memberSinitto1, LocalDateTime.of(2024, 8, 5, 11, 30), LocalDateTime.of(2024, 8, 5, 11, 40)));

        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall3, memberSinitto2, LocalDateTime.of(2024, 10, 8, 18, 30), LocalDateTime.of(2024, 10, 8, 18, 40)));
        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall3, memberSinitto2, LocalDateTime.of(2024, 10, 10, 18, 30), LocalDateTime.of(2024, 10, 10, 18, 40)));

        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall4, memberSinitto2, LocalDateTime.of(2024, 10, 13, 18, 30), LocalDateTime.of(2024, 10, 13, 18, 40)));
        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall4, memberSinitto2, LocalDateTime.of(2024, 10, 13, 18, 30), LocalDateTime.of(2024, 10, 13, 18, 40)));

        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall5, memberSinitto3, LocalDateTime.of(2024, 10, 13, 18, 30), LocalDateTime.of(2024, 10, 13, 18, 40)));
        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall5, memberSinitto3, LocalDateTime.of(2024, 10, 13, 18, 30), LocalDateTime.of(2024, 10, 13, 18, 40)));

        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall6, memberSinitto3, LocalDateTime.of(2024, 8, 13, 18, 30), LocalDateTime.of(2024, 8, 13, 18, 40)));
        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall6, memberSinitto4, LocalDateTime.of(2024, 8, 13, 18, 30), LocalDateTime.of(2024, 8, 13, 18, 40)));

        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall7, memberSinitto4, LocalDateTime.of(2024, 8, 13, 18, 30), LocalDateTime.of(2024, 8, 13, 18, 40)));
        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall7, memberSinitto4, LocalDateTime.of(2024, 8, 13, 18, 30), LocalDateTime.of(2024, 8, 13, 18, 40)));

        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall8, memberSinitto5, LocalDateTime.of(2024, 8, 13, 18, 30), LocalDateTime.of(2024, 8, 13, 18, 40)));
        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall8, memberSinitto5, LocalDateTime.of(2024, 8, 13, 18, 30), LocalDateTime.of(2024, 8, 13, 18, 40)));

        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall9, memberSinitto5, LocalDateTime.of(2024, 6, 13, 18, 30), LocalDateTime.of(2024, 6, 13, 18, 40)));
        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall9, memberSinitto5, LocalDateTime.of(2024, 6, 13, 18, 30), LocalDateTime.of(2024, 6, 13, 18, 40)));

        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall10, memberSinitto2, LocalDateTime.of(2024, 6, 13, 18, 30), LocalDateTime.of(2024, 6, 13, 18, 40)));
        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall10, memberSinitto2, LocalDateTime.of(2024, 6, 13, 18, 30), LocalDateTime.of(2024, 6, 13, 18, 40)));

        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall11, memberSinitto3, LocalDateTime.of(2024, 6, 13, 18, 30), LocalDateTime.of(2024, 6, 13, 18, 40)));
        helloCallTimeLogRepository.save(new HelloCallTimeLog(helloCall11, memberSinitto3, LocalDateTime.of(2024, 6, 13, 18, 30), LocalDateTime.of(2024, 6, 13, 18, 40)));

    }

}
