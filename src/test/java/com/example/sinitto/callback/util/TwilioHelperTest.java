package com.example.sinitto.callback.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TwilioHelperTest {

    @Test
    void trimPhoneNumber() {
        //given
        String testPhoneNumber = "+821023462346";

        //when
        String result = TwilioHelper.trimPhoneNumber(testPhoneNumber);

        //then
        assertEquals("01023462346", result);
    }

    @Test
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
