package asu.eng.gofund.services;

// Abstract class for the Template Pattern
public abstract class NotificationTemplate {

    protected String body = "";
    protected String subject = "";

    public final void sendNotification(String recipient, String subject, String campaignName, Double amount) {
        prepareMessage(subject, campaignName, amount);
        sendMessage(recipient);
    }

    protected abstract void prepareMessage(String subject, String campaignName, Double amount);

    protected abstract void sendMessage(String recipient);

}
