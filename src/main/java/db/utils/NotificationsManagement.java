package db.utils;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class NotificationsManagement {

    public static void main(String[] args) {
        // SMTP server configuration (e.g., Gmail)
        String host = "smtp.gmail.com";

        // Configure properties
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS encryption
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587"); // Port for TLS

        // Create a session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a MimeMessage
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Test Email from Java");
            message.setText("Hello, this is a test email sent using JavaMail!");

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
