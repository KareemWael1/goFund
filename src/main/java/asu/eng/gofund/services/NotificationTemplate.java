package asu.eng.gofund.services;

// Abstract class for the Template Pattern
public abstract class NotificationTemplate {

    // Template method (final to prevent overriding)
    public final void sendNotification(String subject, String body) {
        prepareMessage(subject,body);
        sendMessage();
    }

    protected abstract void prepareMessage(String subject, String body);

    protected abstract void sendMessage();

    protected void logNotification(String recipient) {
        System.out.println("Notification sent and logged successfully.");
    }
}
