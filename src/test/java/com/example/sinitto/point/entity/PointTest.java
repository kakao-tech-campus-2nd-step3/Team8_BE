package com.example.sinitto.point.entity;

import com.example.sinitto.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
