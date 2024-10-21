package com.example.sinitto.point.service;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.dto.PointLogResponse;
import com.example.sinitto.point.entity.PointLog;
import com.example.sinitto.point.repository.PointLogRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MockitoSettings
class PointServiceTest {

    @Mock
    MemberRepository memberRepository;
    @Mock
    PointLogRepository pointLogRepository;
    @InjectMocks
    PointService pointService;

    @Test
    void getPointLogs() {
        //given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Member member = mock(Member.class);
        PointLog pointLog = mock(PointLog.class);

        when(pointLog.getContent()).thenReturn("content");
        when(pointLog.getPrice()).thenReturn(10000);
        when(pointLog.getStatus()).thenReturn(PointLog.Status.EARN);

        Page<PointLog> pointLogPage = new PageImpl<>(List.of(pointLog));

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(pointLogRepository.findAllByMember(member, pageable)).thenReturn(pointLogPage);

        //when
        Page<PointLogResponse> result = pointService.getPointLogs(memberId, pageable);

        //then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(PointLog.Status.EARN, result.getContent().getFirst().status());
    }
}
