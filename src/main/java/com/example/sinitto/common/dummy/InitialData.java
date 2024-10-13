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
        Member guard1 = memberRepository.save(new Member("정예린", "01067890123", "jeong@example.com", false));
        Member guard2 = memberRepository.save(new Member("한상훈", "01078901234", "han@example.com", false));
        Member guard3 = memberRepository.save(new Member("오수빈", "01089012345", "oh@example.com", false));
        Member guard4 = memberRepository.save(new Member("임지훈", "01090123456", "lim@example.com", false));
        Member guard5 = memberRepository.save(new Member("송하늘", "01001234567", "song@example.com", false));

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

        //남은건 HelloCall HelloCallTimeLog TimeSlot 입니다!
        //아래에 이어서 쭉 하시면 될거같아요~
    }

}
