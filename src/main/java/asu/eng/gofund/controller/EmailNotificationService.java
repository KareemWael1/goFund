package asu.eng.gofund.controller;

import asu.eng.gofund.services.EmailFacade;
import asu.eng.gofund.services.NotificationTemplate;

import javax.mail.MessagingException;

public class EmailNotificationService extends NotificationTemplate {

    private final EmailFacade emailFacade;

    public EmailNotificationService(String recipient) {
        this.emailFacade = new EmailFacade(recipient);
    }

    @Override
    protected void prepareMessage(String subject,String body) {
        System.out.println("Preparing email components...");
        emailFacade.setSubject(subject);
        emailFacade.setBody(body);
    }

    @Override
    protected void sendMessage() {
        try {
            System.out.println("Composing and sending the email...");
            emailFacade.composeMessage(); // Compose the email
            emailFacade.send();           // Send the email
        } catch (MessagingException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}