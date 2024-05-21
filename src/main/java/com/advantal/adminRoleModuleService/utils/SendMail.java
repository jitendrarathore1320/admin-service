package com.advantal.adminRoleModuleService.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SendMail {

	public static void sendMailTemplate(String to, String subject, String msg) {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "outlook.office365.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.enable", false);
		// props.put("mail.smtp.socketFactory.class",
		// "javax.net.ssl.SSSlSocketFactory");
		props.put("mail.debug", "true");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Constant.EMAIL_ADDRESS, Constant.PASSWORD);
			}
		});
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(Constant.EMAIL_ADDRESS));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setContent(msg, "text/html");
			log.info("------------- Mail is Sending-------------");
			Transport.send(message);
			log.info("------------- Mail Sent successfully -------------");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
