package com.example.sinitto.callback.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TwilioHelperTest {

    @Test
    @DisplayName("+821023462346 -> 01023462346 으로 변환")
    void trimPhoneNumber() {
        //given
        String testPhoneNumber = "+821023462346";

        //when
        String result = TwilioHelper.trimPhoneNumber(testPhoneNumber);

        //then
        assertEquals("01023462346", result);
    }

    @Test
    @DisplayName("텍스트 메시지를 TwiML(Twilio 음성응답을 위한 것)로 전환")
    void convertMessageToTwiML() {
        //given
        String message = "안녕하세요";

        //when
        String result = TwilioHelper.convertMessageToTwiML(message);

        //then
        assertTrue(result.contains("안녕하세요"));
        assertTrue(result.contains("xml"));
    }
}
