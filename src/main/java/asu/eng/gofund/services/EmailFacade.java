package asu.eng.gofund.services;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailFacade {

    private static Session session = null;
    private static String username;
    private static String password;
    private static String smtpHost;
    private static int smtpPort;

    private String recipient;
    private String subject;
    private String body;
    private MimeMessage message;

    // Static block to configure email settings
    static {
        setEmailConfigurations();
    }

    public EmailFacade(String recipient) {
        this.recipient = recipient;
        this.session = getSession();
        this.message = new MimeMessage(session);
    }

    // Method to set the email subject
    public void setSubject(String subject) {
        this.subject = subject;
    }

    // Method to set the email body
    public void setBody(String body) {
        this.body = body;
    }

    // Method to compose the email message
    public void composeMessage() throws MessagingException {
        message.setFrom(new InternetAddress(username));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);
        message.setText(body);
    }

    // Method to send the composed email
    public void send() throws MessagingException {
        Transport.send(message);
        System.out.println("Email sent successfully to " + recipient);
    }

    private static Session getSession() {
        if (session != null) {
            return session;
        }
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", String.valueOf(smtpPort));
        session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        return session;
    }

    private static void setEmailConfigurations() {
        EmailFacade.username = System.getenv("EMAIL_USERNAME");
        EmailFacade.password = System.getenv("EMAIL_PASSWORD");
        EmailFacade.smtpHost = System.getenv("EMAIL_SMTP_HOST");
        EmailFacade.smtpPort = Integer.parseInt(System.getenv("EMAIL_SMTP_PORT"));
    }
}