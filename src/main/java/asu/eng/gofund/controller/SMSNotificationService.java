package asu.eng.gofund.controller;

import asu.eng.gofund.services.NotificationTemplate;
import asu.eng.gofund.services.SMSFacade;

public class SMSNotificationService extends NotificationTemplate {

    @Override
    protected void prepareMessage(String subject, String campaignName, Double amount) {
        this.body = subject + " \n Thank you for your generous donation to the campaign: " + campaignName + ". \n Your contribution is greatly appreciated and helps us achieve our mission. \n Donation Details: \n Date and Time of Donation: " + amount + " \n Campaign Name: " + campaignName + " \n Donation Amount: $" + String.format("%.2f", amount) + " \n We deeply appreciate your support and hope to see your continued contributions in the future.";
    }

    @Override
    protected void sendMessage(String recipient) {
        SMSFacade.sendSMS(recipient, body);
    }
}
