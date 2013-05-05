package edu.brown.cs32.takhan.tag;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Email {

	public static void sendEmail(String recipient, String message){
		String from = "trakr.notifications@gmail.com";
		Properties prop = System.getProperties();
		prop.put("mail.smtp.auth","true");
		prop.put("mail.smtp.starttls.enable","true");
		prop.put("mail.smtp.host","smtp.gmail.com");
		prop.put("mail.smtp.port","587");
		Session session = Session.getInstance(prop,new Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication(){
				return new PasswordAuthentication("trakr.notifications@gmail.com","australia321");
			}
		});
		try{
			MimeMessage toSend = new MimeMessage(session);
			toSend.setFrom(new InternetAddress(from));
			toSend.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
			toSend.setSubject("Notification");
			toSend.setText(message);
			Transport.send(toSend);
			System.out.println("Email sent");
		}
		catch(MessagingException e){
			System.err.println("Error sending email");
		}
		
	}

}
