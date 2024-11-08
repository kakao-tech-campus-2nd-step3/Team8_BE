package com.example.sinitto.guardGuideline.service;

import com.example.sinitto.callback.entity.Callback;
import com.example.sinitto.callback.repository.CallbackRepository;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.guardGuideline.dto.GuardGuidelineRequest;
import com.example.sinitto.guardGuideline.dto.GuardGuidelineResponse;
import com.example.sinitto.guardGuideline.entity.GuardGuideline;
import com.example.sinitto.guardGuideline.repository.GuardGuidelineRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@MockitoSettings
public class GuardGuildelineServiceTest {
    @Mock
    GuardGuidelineRepository guardGuidelineRepository;
    @Mock
    SeniorRepository seniorRepository;
    @Mock
    CallbackRepository callbackRepository;
    @InjectMocks
    GuardGuidelineService guardGuidelineService;

    @Test
    @DisplayName("addGuardGuideline 메소드 테스트")
    void addGuardGuidelineTest() {
        //given
        Member member = new Member("testName", "01000000000", "test@mail.com", false);
        Long memberId = 1L;
        Senior senior = mock(Senior.class);
        Long seniorId = 2L;
        GuardGuidelineRequest guardGuidelineRequest = new GuardGuidelineRequest
                (seniorId, GuardGuideline.Type.TAXI, "testTitle", "testContent");
        when(seniorRepository.findById(seniorId)).thenReturn(Optional.of(senior));

        //when
        guardGuidelineService.addGuardGuideline(memberId, guardGuidelineRequest);

        //then
        verify(senior, times(1)).isNotGuard(memberId);
        verify(guardGuidelineRepository, times(1)).save(any(GuardGuideline.class));
    }

    @Test
    @DisplayName("readAllGuardGuidelinesByCategoryAndSenior 메소드 테스트")
    void readAllGuardGuidelinesByCategoryAndSeniorTest() {
        //given
        Long memberId = 1L;
        Senior senior = mock(Senior.class);
        Long seniorId = 2L;
        GuardGuideline.Type type = GuardGuideline.Type.TAXI;
        List<GuardGuideline> guardGuidelines = new ArrayList<>();
        guardGuidelines.add(new GuardGuideline(GuardGuideline.Type.TAXI, "testTitle", "testContent", senior));

        when(guardGuidelineRepository.findBySeniorIdAndType(seniorId, type)).thenReturn(guardGuidelines);
        when(seniorRepository.findById(seniorId)).thenReturn(Optional.of(senior));

        //when
        List<GuardGuidelineResponse> result = guardGuidelineService.readAllGuardGuidelinesByCategoryAndSenior(memberId, seniorId, type);

        //then
        verify(senior, times(1)).isNotGuard(memberId);
        assertEquals(guardGuidelines.getFirst().getType(), result.getFirst().type());
        assertEquals(guardGuidelines.getFirst().getTitle(), result.getFirst().title());
        assertEquals(guardGuidelines.getFirst().getContent(), result.getFirst().content());
    }

    @Test
    @DisplayName("readAllGuardGuidelinesByCategoryAndCallback 메소드 테스트")
    void readAllGuardGuidelinesByCategoryAndCallbackTest() {
        //given
        Member member = mock(Member.class);
        Long memberId = 1L;
        Senior senior = mock(Senior.class);
        Long seniorId = 2L;
        Callback callback = mock(Callback.class);
        Long callbackId = 3L;
        GuardGuideline.Type type = GuardGuideline.Type.TAXI;
        List<GuardGuideline> guardGuidelines = new ArrayList<>();
        guardGuidelines.add(new GuardGuideline(GuardGuideline.Type.TAXI, "testTitle", "testContent", senior));

        // when(callback.getAssignedMemberId()).thenReturn(memberId); <- 분명 이거도 쓰는데... 왜 없애니까 되지...?
        when(callback.getStatus()).thenReturn(Callback.Status.WAITING.name());
        when(callback.getSeniorId()).thenReturn(seniorId);
        when(callbackRepository.findById(callbackId)).thenReturn(Optional.of(callback));
        when(guardGuidelineRepository.findBySeniorIdAndType(seniorId, type)).thenReturn(guardGuidelines);

        //when
        List<GuardGuidelineResponse> result = guardGuidelineService.readAllGuardGuidelinesByCategoryAndCallback(memberId, callbackId, type);

        //then
        assertEquals(result.getFirst().type(), guardGuidelines.getFirst().getType());
        assertEquals(result.getFirst().title(), guardGuidelines.getFirst().getTitle());
        assertEquals(result.getFirst().content(), guardGuidelines.getFirst().getContent());
    }

    @Test
    @DisplayName("updateGuardGuideline 메소드 테스트")
    void updateGuardGuidelineTest() {
        //given
        Long memberId = 1L;
        Senior senior = mock(Senior.class);
        GuardGuideline guardGuideline = mock(GuardGuideline.class);
        Long guidelineId = 2L;
        GuardGuidelineRequest guardGuidelineRequest = mock(GuardGuidelineRequest.class);

        when(guardGuidelineRepository.findById(guidelineId)).thenReturn(Optional.of(guardGuideline));
        when(seniorRepository.findById(guardGuidelineRequest.seniorId())).thenReturn(Optional.of(senior));
        //when
        guardGuidelineService.updateGuardGuideline(memberId, guidelineId, guardGuidelineRequest);

        //then
        verify(senior, times(1)).isNotGuard(memberId);
        verify(guardGuideline, times(1)).updateGuardGuideline(guardGuidelineRequest.type(), guardGuidelineRequest.title(), guardGuidelineRequest.content());
    }

    @Test
    @DisplayName("deleteGuardGuideline 메소드 테스트")
    void deleteGuardGuidelineTest() {
        //given
        Senior senior = mock(Senior.class);
        Long memberId = 1L;
        GuardGuideline guardGuideline = new GuardGuideline(GuardGuideline.Type.TAXI, "testTitle", "testContent", senior);
        Long guidelineId = 2L;

        when(senior.isNotGuard(memberId)).thenReturn(false);
        when(guardGuidelineRepository.findById(guidelineId)).thenReturn(Optional.of(guardGuideline));
        //when
        guardGuidelineService.deleteGuardGuideline(memberId, guidelineId);

        //then
        verify(guardGuidelineRepository, times(1)).delete(any(GuardGuideline.class));
    }

    @Test
    @DisplayName("readAllGuardGuidelinesBySenior 메소드 테스트")
    void readAllGuardGuidelinesBySeniorTest() {
        //given
        Member member = mock(Member.class);
        Long memberId = 1L;
        Senior senior = mock(Senior.class);
        Long seniorId = 2L;
        List<GuardGuideline> guardGuidelines = new ArrayList<>();
        guardGuidelines.add(mock(GuardGuideline.class));

        when(seniorRepository.findById(seniorId)).thenReturn(Optional.of(senior));
        when(senior.isNotGuard(memberId)).thenReturn(false);
        when(guardGuidelineRepository.findBySeniorId(seniorId)).thenReturn(guardGuidelines);

        //when
        List<GuardGuidelineResponse> result = guardGuidelineService.readAllGuardGuidelinesBySenior(memberId, seniorId);

        //then
        assertEquals(result.getFirst().type(), guardGuidelines.getFirst().getType());
        assertEquals(result.getFirst().title(), guardGuidelines.getFirst().getTitle());
        assertEquals(result.getFirst().content(), guardGuidelines.getFirst().getContent());
    }
}
