package com.normitec.fm.section.email;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.mail.MessagingException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.normitec.exception.NormitecException;
import com.normitec.exception.ThrowableToString;
import com.normitec.fm.section.AppControlDefaults.Property;
import com.normitec.fm.section.settings.SettingsModel;
import com.normitec.fm.section.settings.SettingsViewController;
import com.normitec.fm.utils.BasicSMTPSender;
import com.normitec.fm.utils.ExcelReader;
import com.normitec.fm.utils.ProcessorHelper;
import com.normitec.model.AbstractModel;

public class EmailViewModel extends AbstractModel {
	private final Logger logger = Logger.getLogger(getClass());
	private File fileToProcess;
	private List<Email> emails;
	private int currentIndex = 0;
	private boolean hideCompleted = false;
	private SettingsModel settingsModel = SettingsViewController.getInstance().getRegisteredModel();
		
	public void  setCurrentIndex(final int index) {
		if (index-1 < this.getEmails().size() && index-1 >= 0)
			this.currentIndex = index-1;

		this.firePropertyChanged(Property.DISPLAY_EMAIL.name(), this.getCurrentEmail());
	}
	
	public int getCurrentIndex() {
		return this.currentIndex;
	}
	
	public Email getCurrentEmail() {
		if (this.emails == null || this.currentIndex == -1 || this.emails.size() == 0)
			return null;
		else
			return this.emails.get(currentIndex);
	}
	
	public boolean isHideCompleted() {
		return this.hideCompleted;
	}
	
	public void getNextElement() {
		if (this.hideCompleted) {
			this.currentIndex = this.getNonCompletedEmail(true);
		} else {
			++currentIndex;
		
			if (currentIndex >= this.getEmails().size())
				currentIndex = this.getEmails().size()-1;
		}
		
		this.firePropertyChanged(Property.DISPLAY_EMAIL.name(), this.getCurrentEmail());
	}
	
	public void getPreviousElement() {
		if (this.hideCompleted) {
			this.currentIndex = this.getNonCompletedEmail(false);
		} else {
			--currentIndex;
			if (this.getEmails().size() == 0 || this.currentIndex < 0)
				currentIndex = 0;
		}
		
		this.firePropertyChanged(Property.DISPLAY_EMAIL.name(), this.getCurrentEmail());
	}
		
	public void setHideCompleted(Boolean hideCompleted) {
		this.hideCompleted = hideCompleted;
		
		if (hideCompleted && this.getCurrentEmail().getEmailStatus() == EmailStatus.EMAIL_SENT) {			
			if ((this.currentIndex = this.getNonCompletedEmail(false)) == -1)
				if ((this.currentIndex = this.getNonCompletedEmail(true)) == -1)
					this.currentIndex = -1;
		}
		else if (!hideCompleted)
			this.currentIndex = 0;
		
		this.firePropertyChanged(Property.DISPLAY_EMAIL.name(), this.getCurrentEmail());
	}
	
	private int getMinIndexOfNonCompleteEmail() {
		int i = 0;
		for(Email em : this.emails) {
			if (em.getEmailStatus() != EmailStatus.EMAIL_SENT)
				return i;
			i++;
		}
		
		return -1;
	}
	
	private int getNonCompletedEmail(boolean next) {		
		
		if (next) {
			if (this.currentIndex == this.getEmails().size()-1)
				return this.emails.size()-1;
			
			for(int i = this.currentIndex+1 ; i < this.getEmails().size(); i++) {
				if (this.emails.get(i).getEmailStatus() != (EmailStatus.EMAIL_SENT))
					return i;
			}
		} else {
			if(this.currentIndex == this.getMinIndexOfNonCompleteEmail())
				return this.getMinIndexOfNonCompleteEmail();
			
			for(int i = this.currentIndex-1 ; i > 0; i--) {
				if (this.getEmails().get(i).getEmailStatus() != (EmailStatus.EMAIL_SENT))
					return i;
			}	
		}
		
		return -1;
	}
	
	public void setEmails(List<Email> emails) {
		this.emails = emails;
		this.currentIndex = 0;
		this.firePropertyChanged(Property.DISPLAY_EMAIL.name(), this.getCurrentEmail());
	}
		
	public void setFileToProcess(File file) {
		this.fileToProcess = file;
		this.firePropertyChanged(Property.FILE_SELECTED.name(), file);
	}
	
	public File getFileToProcess() {
		return this.fileToProcess;
	}

	public List<Email> getEmails() {
		if (this.emails == null)
			this.emails = new ArrayList<Email>();
		
		return emails;
	}
	
	public void addNewEmail(Email email) {
		if (this.emails == null)
			this.emails = new ArrayList<Email>();
		
		this.emails.add(email);
		this.firePropertyChanged(Property.DISPLAY_EMAIL.name(), this.getCurrentEmail());
	}
	
	public int getUnSentEmailCount() {
		int cnt = 0;
		for (Email em : this.emails) {
			if (em.getEmailStatus() != EmailStatus.EMAIL_SENT)
				++cnt;
		}
		
		return cnt;
	}
	
	public void processSelectedFile() throws NormitecException {
		ExcelReader reader = null;
		HashMap<String,String> aliasData;
		
		try {
			reader = new ExcelReader(this.fileToProcess, 0, settingsModel);
		}
		catch (IOException e) {
			e.printStackTrace();
			this.firePropertyChanged(Property.PROCESSING_EXCEL.name(), 
					"Failed to due to some problem...\n\n" + e.getMessage(), false);
		}
		if (this.fileToProcess == null)
			this.firePropertyChanged(Property.PROCESSING_EXCEL.name(), "No file selected...", false);
		else {
			this.firePropertyChanged(Property.PROCESSING_EXCEL.name(), "Processing selected file...", true);
			for (int r=0 ; r < reader.getUsedRowCount() ; r++) {
				this.firePropertyChanged(Property.PROCESSING_EXCEL.name(), "Processing row " + r + " / " + reader.getUsedRowCount(), true);
				aliasData = new HashMap<String,String>();
				
				for (int c=0 ; c < settingsModel.getDataExtractors().size() ; c++) {
					StringBuilder temp = new StringBuilder();
				
					for (Integer col : settingsModel.getDataExtractors().get(c).getColumnNumber()) {					
						if (settingsModel.getDataExtractors().get(c).getColumnNumber().size() == 1)
							temp.append(reader.getCellDataAtRowColumn(r, col-1));
						else
							temp.append(reader.getCellDataAtRowColumn(r, col-1)).append("~");
					}
					
					aliasData.put(settingsModel.getDataExtractors().get(c).getUserAlias(),temp.toString());
				}					
				
				try {
					ProcessorHelper.createEmailWithData(this, aliasData);
				}
				catch (NormitecException e) {
					e.printStackTrace();
					throw new NormitecException("Processing row " + r + " failed with the following error.\n\n" + e.getMessage(),e);
				}
			}
		}
	}
	
	public void sendAllEmails() {
		for(int em = 0 ; em < this.emails.size(); em++) {
			if (this.emails.get(em).getEmailStatus() != EmailStatus.EMAIL_SENT && this.emails.get(em).isAllValid()) {
				this.firePropertyChanged(Property.SENDING_EMAIL.name(), "Sending email " + em + " / " + this.getUnSentEmailCount());
				this.sendEmail(this.emails.get(em));
			} else {
				this.emails.get(em).setEmailStatus(EmailStatus.EMAIL_NOT_SENT, "Email not sent due to email validation restriction setting on one of the data extractors.");
			}
		}		
	}
	
	public void sendEmail(Email em) {
		SettingsModel sm = SettingsViewController.getInstance().getRegisteredModel();
		try {
			new BasicSMTPSender().sendMail(sm.getSmtpHost(), sm.getSmtpPort(), em);
			em.setEmailStatus(EmailStatus.EMAIL_SENT, "");
		} catch (MessagingException | IOException | NullPointerException | IllegalArgumentException e) {
			em.setEmailStatus(EmailStatus.EMAIL_SENDING_FAILED, e.getMessage());
			e.printStackTrace();
			logger.log(Level.ERROR, ThrowableToString.exceptionToString(e));
		} finally {
			try {
				Thread.sleep(500);
			}
			catch (InterruptedException e) {
				//Not bothered if it throws exception
			}
		}
	}
	
	@Override
	public void initDefaultValues() {
		this.setCurrentIndex(-1);
		this.emails = null;
		this.hideCompleted = false;
		this.fileToProcess = null;
	}
	
	@Override
	public void reFireUpdates() {
		this.firePropertyChanged(Property.DISPLAY_EMAIL.name(), this.getCurrentEmail());
		this.firePropertyChanged(Property.HIDE_COMPLETED.name(), this.hideCompleted);
	}
}
