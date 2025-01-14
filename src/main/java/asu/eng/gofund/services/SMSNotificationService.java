package asu.eng.gofund.services;

// Concrete class for SMS Notification
public class SMSNotificationService extends NotificationTemplate {

    @Override
    protected void prepareMessage(String message) {
        System.out.println("Preparing SMS message: " + message);
    }

    @Override
    protected void sendMessage(String recipient) {
        System.out.println("Sending SMS to phone number: " + recipient);
    }
}
