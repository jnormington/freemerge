package com.normitec.fm.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.normitec.exception.ThrowableToString;
import com.normitec.fm.section.email.Email;

public class BasicSMTPSender {
	
	private static final Logger logger = Logger.getLogger(BasicSMTPSender.class);
	
	public void sendMail(String smtpHost, String smtpPort, Email email) throws AddressException, MessagingException, IOException, IllegalArgumentException {
		this.sendMail(smtpHost, smtpPort, email.getFrom(), email.getTo(), email.getCc(), 
				email.getBcc(), email.getSubject(), email.getBody(), email.isBodyHTML(), email.getAttachments());
	}

	public void sendMail(String smtpHost, String smtpPort, String from, String to, String cc, String bcc,
			String subject, String body, boolean contentHtml, List<File> files) throws AddressException, MessagingException, IOException, IllegalArgumentException {

		if (smtpHost == null || smtpPort == null || from == null
				|| to == null)
			throw new IllegalArgumentException("SMTP Host, SMTP Port, Email From and To must be populated to send an email.");

		// Get system properties
		Properties properties = System.getProperties();
		// Setup mail server
		properties.setProperty("mail.smtp.host", smtpHost);
		properties.setProperty("mail.smtp.port", smtpPort);
		properties.setProperty("mail.smtp.timeout", "30000");
		
		Session session = Session.getDefaultInstance(properties);
		// Create MimeMessage object.
		MimeMessage message = new MimeMessage(session);
		// Set From and TO header fields
		message.setFrom(new InternetAddress(from));
		// Comma seperated email address for each RecipientType
		message.addRecipients(Message.RecipientType.TO,InternetAddress.parse(to));

		if (cc != null)
			message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
		if (bcc != null)
			message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));

		message.setSubject(subject);

		// message body part one - the message
		MimeBodyPart messageBodyPart = new MimeBodyPart();

		if (contentHtml) {
			if (!body.contains("<html>"))
				body = "<html><body>" + body.replace("\n", "<br/>") + "</body></html>";
			
			messageBodyPart.setContent(body, "text/html");
		} else
			messageBodyPart.setText(body);

		// multipart message to join the message and attachments
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		
		// attachments
		if (files != null && files.size() > 0) {
			MimeBodyPart attachment = null;
			for (File f : files) {
				try {
					attachment = new MimeBodyPart();
					attachment.attachFile(f); // Could throw exception
					multipart.addBodyPart(attachment);
				} catch (IOException e) {
					logger.log(Level.ERROR, ThrowableToString.exceptionToString(e));
					throw new IOException("Attaching file: " + f
							+ " - failed see exception below." + e.getMessage());
					
				}
			}
		}
		
		//This attaches txt file of email content.
		//multipart.addBodyPart(messageBodyPart);
		// complete message parts
		message.setContent(multipart);
		// Send message
		logger.log(Level.DEBUG, "Sending email message ");
		Transport.send(message);
		logger.log(Level.DEBUG, "Email message successfully sent...");
	}
}
