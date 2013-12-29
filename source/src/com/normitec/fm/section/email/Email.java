package com.normitec.fm.section.email;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.custom.IJSONParseable;
import org.json.custom.IJSONRequestable;

import com.normitec.exception.NormitecException;
import com.normitec.fm.Constants;
import com.normitec.model.AbstractModel;

public class Email extends AbstractModel implements IJSONParseable<Email>, IJSONRequestable<String> {
	
	private static final Logger logger = Logger.getLogger(Email.class);
	private String from;
	private String to;
	private String cc;
	private String bcc;
	private String subject;
	private String body;
	private List<File> attachments;
	private boolean bodyHTML;
	private EmailStatus emailStatus = EmailStatus.EMAIL_NOT_SENT;
	private String emailStatusStr;
	private List<String> emailsValid;
		
	public Email() {
		this("","","","","","",null,true);
    }

	public Email(String from, String to, String cc, 
			String bcc, String subject, String body, 
			List<File> files, boolean bodyHTML) {
		this.from = from;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.subject = subject;
        this.body = body;
        this.setBodyHTML(bodyHTML);
        this.attachments = files;
    }
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	public String getFrom() {
		return from;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
	public String getTo() {
		return to;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}
	
	public String getCc() {
		return cc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	
	public String getBcc() {
		return bcc;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getSubject() {
		return subject;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public String getBody() {
		return body;
	}

	public void setAttachments(List<File> attachments) {
		this.attachments = attachments;
	}
	
	public List<File> getAttachments() {
		if (this.attachments == null)
			this.attachments = new ArrayList<File>();
		
		return attachments;
	}
	
	public void setBodyHTML(boolean bodyHTML) {
		this.bodyHTML = bodyHTML;
	}
	
	public boolean isBodyHTML() {
		return bodyHTML;
	}
	
	public void setEmailStatus(EmailStatus status, String error) {
		this.emailStatus = status;
		this.emailStatusStr = error;
	}
	
	public String getEmailStatusStr() {
		if (this.emailStatusStr == null) 
			this.emailStatusStr = "";
		
		return this.emailStatusStr;
	}
	
	public String getEmailStatusAndString() {
		return this.getEmailStatusAsStr() + (this.emailStatusStr == null || this.emailStatusStr.equals("") ? "" : ": " + this.emailStatusStr);
	}

	public EmailStatus getEmailStatus() {
		return this.emailStatus;
	}
	
	public String getEmailStatusAsStr() {
		return this.getEmailStatus().name().replace("_", " ");
	}
	
	public List<String> getEmailValid() {
		if (this.emailsValid == null)
			this.emailsValid = new ArrayList<String>();
		
		return emailsValid;
	}

	public void setEmailValid(List<String> emailValid) {
		this.emailsValid = emailValid;
	}
	
	public boolean isAllDataValidFromDataExtractor() {
		for (String ss : this.emailsValid) {
			if (!Boolean.parseBoolean(ss.split("~")[1]))
				return false;	
		}
		
		return true;
	}

	@Override
	public Email parseJSONToObject(String message) throws NormitecException {
		logger.log(Level.DEBUG, "Parse JSON object: " + message);
		JSONObject json = null;
		try {
			json = new JSONObject(message.replace(", \\", message));
			
			if (json.has("from"))
				this.from = json.getString("from");
			if (json.has("to"))
				this.to = json.getString("to");
			if (json.has("cc"))
				this.cc = json.getString("cc");
			if (json.has("bcc"))
				this.bcc = json.getString("bcc");
			if (json.has("subject"))
				this.subject = json.getString("subject");
			if (json.has("body"))
				this.body = json.getString("body");
			
			if (json.has("attachments") && json.getJSONArray("attachments").length() > 0) {
				JSONArray ja = json.getJSONArray("attachments");
				
				for (int idx = 0; idx < ja.length() ; idx++) {
					this.getAttachments().add(new File(ja.getString(idx)));
				}
			}
			
		} catch (JSONException e) {
			logger.log(Level.DEBUG, "Parse JSON object failed in email");			
			e.printStackTrace();
			throw new NormitecException(e.getMessage());
		}
		
		return this;
	}

	@Override
	public String jsonRequestMessage(JSONObject header) throws JSONException {
		header.put("from", this.from);
		header.put("to", this.to);
		header.put("cc", this.cc);
		header.put("bcc", this.bcc);
		header.put("subject", this.subject);
		header.put("body", this.body);
		header.put("attachments", this.attachments);
		
		return header.toString();
	}
	
	public boolean isAllValid() {
		System.out.println(this.getEmailValid().size());
		for (String ev : this.getEmailValid()) {
			if (!Boolean.parseBoolean(ev.split(Constants.EMAILVALID_SEPERATOR)[1].trim())) {
				return false;
			}
		}
		
		System.out.println("Returning true...");
		return true;
	}
	
	
}
