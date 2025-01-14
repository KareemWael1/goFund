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

    public static void sendEmail(String to, String subject, String body) throws MessagingException {
        Session session = getSession();
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(body);
        Transport.send(message);
    }

    private static Session getSession() {
        if (session != null) {
            return session;
        }
        setEmailConfigurations();
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
