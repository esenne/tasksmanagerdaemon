package com.bpanalytics.tasksmanagerdaemon.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.bpanalytics.tasksmanager.persistence.model.Task;

public class EmailTask implements Runnable {
	
	private Task task;
	
	public EmailTask(Task task) {
		this.task = task;
	}

	@Override
	public void run() {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
 
		Session session = Session.getDefaultInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("taskmanager22","taskmanager22*");
				}
			});
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("taskmanager22@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(task.getUser().getEmail()));
			message.setSubject("Pending task");
			message.setText("You have a pending task: " + task.getDescription());
 
			Transport.send(message);
			
			System.out.println("Email enviado para: " + task.getUser().getEmail());
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
}