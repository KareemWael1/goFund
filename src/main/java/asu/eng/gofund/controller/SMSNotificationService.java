package asu.eng.gofund.controller;

import asu.eng.gofund.services.NotificationTemplate;
import asu.eng.gofund.services.SMSFacade;

// Concrete class for SMS Notification
public class SMSNotificationService extends NotificationTemplate {

    @Override
    protected void prepareMessage(String subject) {
        System.out.println("Preparing SMS message: ");
    }

    @Override
    protected void sendMessage(String recipient) {
        // Debug Mode To avoid consuming Twilio free trial
        if (System.getenv("DEBUG") != null && System.getenv("DEBUG").equals("true")) {
            System.out.println("SMS sent to phone number: " + recipient);
            return;
        }
        SMSFacade.sendSMS(recipient, "Hello, this is a test message from GoFund.");
    }
}
