package com.userregistration.main.utli;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class JMSUtli {
	public static void sendEmail(String toEmail, String subject, String body) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		String fromEmail = System.getenv("email");
		mailSender.setUsername(fromEmail);
		String password = System.getenv("password");
		mailSender.setPassword(password);
		System.out.println("From Mail : " + fromEmail);
		System.out.println("To Mail : " + toEmail);
		System.out.println("Subject of Mail : " + subject);
		System.out.println("Body of Mail : " + body);
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};
		Session session = Session.getInstance(props, auth);
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
			message.setSubject(subject);
			message.setText(body);
			Transport.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("exception occured when sending the mail");
		}
	}
}
