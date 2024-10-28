package com.example.sinitto.point.entity;

import com.example.sinitto.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@MockitoSettings
class PointTest {

    @Test
    @DisplayName("포인트 적립")
    void earn() {
        //given
        Member member = mock(Member.class);
        Point point = new Point(100, member);

        //when
        point.earn(3000);

        //then
        assertEquals(3100, point.getPrice());
    }

    @Test
    @DisplayName("포인트 차감")
    void deduct() {
        //given
        Member member = mock(Member.class);
        Point point = new Point(100, member);

        //when
        point.deduct(80);

        //then
        assertEquals(20, point.getPrice());
    }

    @Test
    @DisplayName("차감이 되는지 확인 - FALSE")
    void isSufficientForDeduction() {
        //given
        Member member = mock(Member.class);
        Point point = new Point(100, member);

        //when then
        assertFalse(point.isSufficientForDeduction(3000));
    }

    @Test
    @DisplayName("차감이 되는지 확인 - TRUE")
    void isSufficientForDeduction2() {
        //given
        Member member = mock(Member.class);
        Point point = new Point(5000, member);

        //when then
        assertTrue(point.isSufficientForDeduction(3000));
    }
}
