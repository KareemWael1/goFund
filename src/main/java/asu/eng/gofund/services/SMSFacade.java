package asu.eng.gofund.services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SMSFacade {
    private static String accountSid;
    private static String authToken;
    private static String twilioPhoneNumber;

    static {
        setSMSConfigurations();
        Twilio.init(accountSid, authToken);
    }

    public static void sendSMS(String to, String message) {
        Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(twilioPhoneNumber),
                message
        ).create();

        System.out.println("SMS sent successfully to " + to);
    }

    private static void setSMSConfigurations() {
        SMSFacade.accountSid = System.getenv("TWILIO_ACCOUNT_SID");
        SMSFacade.authToken = System.getenv("TWILIO_AUTH_TOKEN");
        SMSFacade.twilioPhoneNumber = System.getenv("TWILIO_PHONE_NUMBER");

        if (accountSid == null || authToken == null || twilioPhoneNumber == null) {
            throw new IllegalStateException("SMS configuration environment variables are missing.");
        }
    }
}
