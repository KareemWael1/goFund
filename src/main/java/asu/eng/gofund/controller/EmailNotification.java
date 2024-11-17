package asu.eng.gofund.controller;

public class EmailNotification {
    public static void sendEmail(String to, String subject, String body) {
        System.out.println("Email sent to " + to + " with subject: " + subject + " and body: " + body);
    }
}
