package spring.boot.yj.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import spring.boot.yj.entities.VerificationToken;
import spring.boot.yj.exceptions.EmailFailureException;

@Service
public class EmailService {


	@Value ("${email.from}")
	private String fromAdress;
	@Value("${app.frontend.url}")
	private String url;
	private JavaMailSender javaMailSender;

	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	private SimpleMailMessage makeMailMessage() {
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(fromAdress);
		return simpleMailMessage;
	}
	public void sendVerificationEmail(VerificationToken verificationToken) throws EmailFailureException {
		SimpleMailMessage message = makeMailMessage();
		message.setTo(verificationToken.getUser().getEmail());
		message.setSubject("Verify your email to active your account");
		message.setText("Please follow the link below to verify your email to activate your account. \n"+
				url + "/auth/verify?token=" + verificationToken.getToken());
		try {
			javaMailSender.send(message);
		} catch (MailException ex) {
			throw new EmailFailureException();
		}


	}

}
