package com.example.sinitto.common.dummy;

import com.example.sinitto.callback.entity.Callback;
import com.example.sinitto.callback.repository.CallbackRepository;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.guardGuideline.entity.GuardGuideline;
import com.example.sinitto.guardGuideline.repository.GuardGuidelineRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.entity.Sinitto;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.entity.Point;
import com.example.sinitto.point.entity.PointLog;
import com.example.sinitto.point.repository.PointLogRepository;
import com.example.sinitto.point.repository.PointRepository;
import com.example.sinitto.review.entity.Review;
import com.example.sinitto.review.repository.ReviewRepository;
import com.example.sinitto.sinitto.repository.SinittoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialData implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final SeniorRepository seniorRepository;
    private final PointRepository pointRepository;
    private final CallbackRepository callbackRepository;
    private final SinittoRepository sinittoRepository;
    private final PointLogRepository pointLogRepository;
    private final ReviewRepository reviewRepository;
    private final GuardGuidelineRepository guardGuidelineRepository;

    public InitialData(MemberRepository memberRepository, SeniorRepository seniorRepository, PointRepository pointRepository, CallbackRepository callbackRepository, SinittoRepository sinittoRepository, PointLogRepository pointLogRepository, ReviewRepository reviewRepository, GuardGuidelineRepository guardGuidelineRepository) {
        this.memberRepository = memberRepository;
        this.seniorRepository = seniorRepository;
        this.pointRepository = pointRepository;
        this.callbackRepository = callbackRepository;
        this.sinittoRepository = sinittoRepository;
        this.pointLogRepository = pointLogRepository;
        this.reviewRepository = reviewRepository;
        this.guardGuidelineRepository = guardGuidelineRepository;
    }

    @Override
    public void run(String... args) {
        initial();
    }

    private void initial() {
        //시니또
        Member sinitto1 = memberRepository.save(new Member("김철수", "010-1234-5678", "chulsoo@example.com", true));
        sinittoRepository.save(new Sinitto("신한은행", "123-23-444-422", sinitto1));
        Member sinitto2 = memberRepository.save(new Member("김유진", "010-2345-6789", "kim@example.com", true));
        sinittoRepository.save(new Sinitto("대구은행", "446-5-11-2", sinitto2));
        Member sinitto3 = memberRepository.save(new Member("이민호", "010-3456-7890", "lee@example.com", true));
        sinittoRepository.save(new Sinitto("IBK은행", "7-66-8-422", sinitto3));
        Member sinitto4 = memberRepository.save(new Member("박소연", "010-4567-8901", "park@example.com", true));
        sinittoRepository.save(new Sinitto("토스뱅크", "777-1-2-3", sinitto4));
        Member sinitto5 = memberRepository.save(new Member("최진우", "010-5678-9012", "choi@example.com", true));
        sinittoRepository.save(new Sinitto("기업은행", "96-6-99-45", sinitto5));

        //보호자
        Member guard1 = memberRepository.save(new Member("정예린", "010-6789-0123", "jeong@example.com", false));
        Member guard2 = memberRepository.save(new Member("한상훈", "010-7890-1234", "han@example.com", false));
        Member guard3 = memberRepository.save(new Member("오수빈", "010-8901-2345", "oh@example.com", false));
        Member guard4 = memberRepository.save(new Member("임지훈", "010-9012-3456", "lim@example.com", false));
        Member guard5 = memberRepository.save(new Member("송하늘", "010-0123-4567", "song@example.com", false));

        //시니어
        Senior senior1 = seniorRepository.save(new Senior("권지민", "010-1357-2468", guard1));
        Senior senior2 = seniorRepository.save(new Senior("배정호", "010-2468-1357", guard1));
        Senior senior3 = seniorRepository.save(new Senior("윤수현", "010-4680-9753", guard1));
        Senior senior4 = seniorRepository.save(new Senior("하재민", "010-5791-0864", guard2));
        Senior senior5 = seniorRepository.save(new Senior("민서영", "010-6802-1975", guard2));
        Senior senior6 = seniorRepository.save(new Senior("전진우", "010-7913-2086", guard3));
        Senior senior7 = seniorRepository.save(new Senior("나미래", "010-8024-3197", guard3));
        Senior senior8 = seniorRepository.save(new Senior("임소라", "010-9135-4208", guard4));
        Senior senior9 = seniorRepository.save(new Senior("조예빈", "010-0246-5319", guard4));
        Senior senior10 = seniorRepository.save(new Senior("우지현", "010-1357-6420", guard5));
        Senior senior11 = seniorRepository.save(new Senior("서예진", "010-3579-8642", guard5));

        //포인트와 포인트로그
        pointRepository.save(new Point(50000, sinitto1));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), sinitto1, 50000, PointLog.Status.CHARGE_COMPLETE));
        pointRepository.save(new Point(50000, sinitto2));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), sinitto2, 50000, PointLog.Status.CHARGE_COMPLETE));
        pointRepository.save(new Point(50000, sinitto3));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), sinitto3, 50000, PointLog.Status.CHARGE_COMPLETE));
        pointRepository.save(new Point(50000, sinitto4));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), sinitto4, 50000, PointLog.Status.CHARGE_COMPLETE));
        pointRepository.save(new Point(50000, sinitto5));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), sinitto5, 50000, PointLog.Status.CHARGE_COMPLETE));
        pointRepository.save(new Point(50000, guard1));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard1, 50000, PointLog.Status.CHARGE_COMPLETE));
        pointRepository.save(new Point(50000, guard2));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard2, 50000, PointLog.Status.CHARGE_COMPLETE));
        pointRepository.save(new Point(50000, guard3));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard3, 50000, PointLog.Status.CHARGE_COMPLETE));
        pointRepository.save(new Point(50000, guard4));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard4, 50000, PointLog.Status.CHARGE_COMPLETE));
        pointRepository.save(new Point(50000, guard5));
        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), guard5, 50000, PointLog.Status.CHARGE_COMPLETE));


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

        //남은건 GuardGuideline HelloCall HelloCallTimeLog TimeSlot 입니다!
        //아래에 이어서 쭉 하시면 될거같아요~
    }

}
