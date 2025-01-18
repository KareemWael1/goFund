package asu.eng.gofund.services;

// Abstract class for the Template Pattern
public abstract class NotificationTemplate {

    protected String body = "";
    protected String subject = "";

    public final void sendNotification(String recipient, String subject, String campaignName, Double amount) {
        boolean flag = validateMessage(amount,subject);
        if (!flag){return;}
        prepareMessage(subject, campaignName, amount);
        sendMessage(recipient);
    }

    protected abstract void prepareMessage(String subject, String campaignName, Double amount);

    protected abstract void sendMessage(String recipient);

    protected final boolean validateMessage(Double amount, String subject){
        if (amount < 0){
            return false;
        }
        if (subject != "Donation Successful" && subject != "Donation Update"){
            return false;
        }
        return true;
    }
}
