package asu.eng.gofund.services;

// Abstract class for the Template Pattern
public abstract class NotificationTemplate {

    // Template method (final to prevent overriding)
    public final void sendNotification(String recipient, String subject) {
        prepareMessage(subject);
        sendMessage(recipient);
    }

    protected abstract void prepareMessage(String subject);

    protected abstract void sendMessage(String recipient);

}
