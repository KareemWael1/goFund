package asu.eng.gofund.controller;

import asu.eng.gofund.services.NotificationTemplate;

// Concrete class for SMS Notification
public class SMSNotificationService extends NotificationTemplate {

    @Override
    protected void prepareMessage(String subject) {
        System.out.println("Preparing SMS message: ");
    }

    @Override
    protected void sendMessage(String recipient) {
        System.out.println("Sending SMS to phone number: ");
    }
}
