package asu.eng.gofund.services;

// Abstract class for the Template Pattern
public abstract class NotificationTemplate {

    // Template method (final to prevent overriding)
    public final void sendNotification(String recipient, String message) {
        prepareMessage(message);
        sendMessage(recipient);
        logNotification(recipient);
    }

    protected abstract void prepareMessage(String message);

    protected abstract void sendMessage(String recipient);

    protected void logNotification(String recipient) {
        System.out.println("Notification sent to " + recipient + " and logged successfully.");
    }
}
