package com.vpi.springboot.Logica;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MailService {

	@Autowired
	private JavaMailSender mailSender;

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendMail(String to, String body, String topic) throws MessagingException {
		System.out.println("Mandando mail...");

		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

		helper.setFrom("csuarezalt@gmail.com");
		helper.setTo(to);
		helper.setSubject(topic);
		helper.setText(body, true);
		// Enviamos el mensaje al mail del usuario
		mailSender.send(mimeMessage);

		System.out.println("Mail mandado con éxito.");
	}
}