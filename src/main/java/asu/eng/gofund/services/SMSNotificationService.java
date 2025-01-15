package asu.eng.gofund.services;

// Concrete class for SMS Notification
public class SMSNotificationService extends NotificationTemplate {

    @Override
    protected void prepareMessage(String subject,String body) {
        System.out.println("Preparing SMS message: ");
    }

    @Override
    protected void sendMessage() {
        System.out.println("Sending SMS to phone number: ");
    }
}
