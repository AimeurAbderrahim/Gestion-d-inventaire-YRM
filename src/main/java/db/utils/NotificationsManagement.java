package db.utils;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class NotificationsManagement {
	private Properties props;
	private Message message;
	private String to;
	private String username;
	private String password;

	// TODO: move username and password to property file
	public NotificationsManagement(String username , String password) {
		this.username = username;
		this.password = password;
		this.to = "youcef.benhamadi13@gmail.com";
		this.props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
	}

	public void prepareMessage(String subject, String text) throws MessagingException {
		this.message = new MimeMessage(this.getSession());
		this.message.setFrom(new InternetAddress(this.username));
		this.message.setRecipient(Message.RecipientType.TO, new InternetAddress(this.to));
		this.message.setSubject(subject);
		this.message.setText(text);
	}

	private Session getSession() {
		return Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// Use instance variables instead of hardcoded values
				return new PasswordAuthentication(username, password);
			}
		});
	}

	public void sendMail() throws MessagingException {
		if (message != null) {
			Transport.send(message);
		} else {
			throw new MessagingException("Message not prepared");
		}
	}
}
