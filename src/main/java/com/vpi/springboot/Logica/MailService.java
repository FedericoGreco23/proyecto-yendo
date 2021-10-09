package com.vpi.springboot.Logica;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	@Autowired
	private JavaMailSender mailSender; 
	
	public void sendMail(String to, String body, String topic) {
		System.out.println("Enviando mail");
		SimpleMailMessage simpleMail = new SimpleMailMessage();
		simpleMail.setFrom("csuarezalt@gmail.com");
		simpleMail.setTo(to);
		simpleMail.setSubject(topic);
		simpleMail.setText(body);
		//Enviamos el mensaje al mail del usuario
		mailSender.send(simpleMail);
		System.out.println("Mail enviado correctamente");
	}
}
