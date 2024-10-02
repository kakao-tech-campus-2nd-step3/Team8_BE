package com.example.sinitto.callback.util;

import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Say;

public class TwilioHelper {

    private TwilioHelper() {

    }

    public static String trimPhoneNumber(String fromNumber) {

        return "0" + fromNumber.substring(3);
    }

    public static String convertMessageToTwiML(String message) {

        return new VoiceResponse.Builder()
                .say(new Say.Builder(message)
                        .voice(Say.Voice.GOOGLE_KO_KR_STANDARD_A)
                        .build())
                .build()
                .toXml();
    }

}
