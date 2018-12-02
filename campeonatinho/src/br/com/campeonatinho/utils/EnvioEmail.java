package br.com.campeonatinho.utils;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EnvioEmail {

	/*
	 * Baseado no exemplo: https://www.devmedia.com.br/enviando-email-com-javamail-utilizando-gmail/18034
	 */
	public static void enviarEmailConfirmacao(String emailUsuario) {

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator( ) {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("campeonatinho.br@gmail.com", "");
			}
		});
		
		try {
			 
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("campeonatinho.br@gmail.com"));

            Address[] toUser = InternetAddress
                       .parse(emailUsuario);  

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject("Ativação de cadastro - Campeonatinho");
            message.setText("Bem-vindo. Este é o seu link para ativação de seu cadastro: " + montaUrlAtivacao(emailUsuario));
            Transport.send(message);
            
       } catch (MessagingException e) {
            throw new RuntimeException(e);
      }
	}
	
	private static String montaUrlAtivacao(String emailUsuario) {
		
		return "http://campeonatinho.ddns.net:8443/campeonatinho/index.jsf?emailAtivacao=" + emailUsuario + "&idAtivacao=" + Encrypt.geraChaveAtivacao(emailUsuario);
	}
}
