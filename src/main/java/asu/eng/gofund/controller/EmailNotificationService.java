package asu.eng.gofund.controller;

import asu.eng.gofund.services.EmailFacade;
import asu.eng.gofund.services.NotificationTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import javax.mail.MessagingException;
import java.util.Objects;

public class EmailNotificationService extends NotificationTemplate {


    @Override
    protected void prepareMessage(String subject, String campaignName, Double amount) {
        this.subject = subject;
        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = timestamp.format(formatter);

        if (Objects.equals(subject, "Donation Successful")) {

            this.body = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "  body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }" +
                    "  .email-container { max-width: 600px; margin: auto; background: #ffffff; border: 1px solid #ddd; border-radius: 8px; padding: 20px; }" +
                    "  h1 { color: #4CAF50; }" +
                    "  p { color: #555555; font-size: 16px; line-height: 1.6; }" +
                    "  .footer { margin-top: 20px; font-size: 12px; color: #888888; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='email-container'>" +
                    "  <h1>Dear Valued Customer,</h1>" +
                    "  <p>Thank you for your generous donation to the campaign: <strong>" + campaignName + "</strong>.</p>" +
                    "  <p>Your contribution is greatly appreciated and helps us achieve our mission.</p>" +
                    "  <p><strong>Donation Details:</strong></p>" +
                    "  <ul>" +
                    "    <li>Date and Time of Donation: " + formattedTimestamp + "</li>" +
                    "    <li>Campaign Name: " + campaignName + "</li>" +
                    "    <li>Donation Amount: $" + String.format("%.2f", amount) + "</li>" +
                    "  </ul>" +
                    "  <p>We deeply appreciate your support and hope to see your continued contributions in the future.</p>" +
                    "  <div class='footer'>This is an automated email. Please do not reply.</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
        } else {
            this.body = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<style>" +
                    "  body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; }" +
                    "  .email-container { max-width: 600px; margin: auto; background: #ffffff; border: 1px solid #ddd; border-radius: 8px; padding: 20px; }" +
                    "  h1 { color: #4CAF50; }" +
                    "  p { color: #555555; font-size: 16px; line-height: 1.6; }" +
                    "  .footer { margin-top: 20px; font-size: 12px; color: #888888; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<div class='email-container'>" +
                    "  <h1>Dear Valued Customer,</h1>" +
                    "  <p>We would like to inform you that another generous donation has been made to the campaign: <strong>" + campaignName + "</strong>.</p>" +
                    "  <p>The campaign has now reached a total of: <strong>$" + String.format("%.2f", amount) + "</strong>.</p>" +
                    "  <p>We deeply appreciate all contributions that make our mission possible.</p>" +
                    "  <p><strong>Update Details:</strong></p>" +
                    "  <ul>" +
                    "    <li>Date and Time of Update: " + formattedTimestamp + "</li>" +
                    "    <li>Campaign Name: " + campaignName + "</li>" +
                    "    <li>Total Amount Reached: $" + String.format("%.2f", amount) + "</li>" +
                    "  </ul>" +
                    "  <p>Thank you for your continued support!</p>" +
                    "  <div class='footer'>This is an automated email. Please do not reply.</div>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
        }
    }


    @Override
    protected void sendMessage(String recipient) {
        try {
            System.out.println("Composing and sending the email...");
            EmailFacade.sendEmail(recipient, this.subject, this.body);
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}