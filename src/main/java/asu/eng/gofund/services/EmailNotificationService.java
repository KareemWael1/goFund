package asu.eng.gofund.services;

public class EmailNotificationService extends NotificationTemplate {

    @Override
    protected void prepareMessage(String message) {
        System.out.println("Preparing Email content: " + message);
    }

    @Override
    protected void sendMessage(String recipient) {
        System.out.println("Sending Email to address: " + recipient);
    }
}
