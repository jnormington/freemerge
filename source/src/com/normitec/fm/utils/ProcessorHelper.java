package com.normitec.fm.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.normitec.exception.NormitecException;
import com.normitec.fm.Constants;
import com.normitec.fm.ldap.VBLDAPQuery;
import com.normitec.fm.section.email.Email;
import com.normitec.fm.section.email.EmailViewModel;
import com.normitec.fm.section.settings.DataExtractor;
import com.normitec.fm.section.settings.SettingsModel;
import com.normitec.fm.section.settings.SettingsViewController;


public class ProcessorHelper {
	private static final Logger logger = Logger.getLogger(ProcessorHelper.class);
	private final static SettingsModel settings = SettingsViewController.getInstance().getRegisteredModel(); 

	public static void createEmailWithData(EmailViewModel emailViewModel,HashMap<String, String> aliasData) throws NormitecException {
		Email email = ProcessorHelper.initNewEmailFromSettings();
		List<String> validations = new ArrayList<String>();
		String fromLDAP;
	
		for (DataExtractor de : settings.getDataExtractors()) {
			//Pass to LDAP to get the data.
			if (de.isUseLDAP()) {
				//Get email from LDAP	
				String[] name = aliasData.get(de.getUserAlias()).split("~");

				if (name.length == 2)
					fromLDAP = new VBLDAPQuery(new File(Constants.VBSCRIPT_LDAP_IMPL_SCRIPT)).search(settings.getLdapURL(), name[0], name[1]);
				else
					fromLDAP = "";
					
				//Does the data contain an email address from LDAP?
				if (de.isEmailRequired()) {	
					if (fromLDAP.contains("@")) {
						validations.add(de.getUserAlias() + " " + Constants.EMAILVALID_SEPERATOR + " " + true);
					} else {
						validations.add(de.getUserAlias() + " " + Constants.EMAILVALID_SEPERATOR + " " + false);
					}
				}
				
				updateEmailWithData(email, de.getUserAlias(), fromLDAP);
				email.setEmailValid(validations);
			} else {
				updateEmailWithData(email, de.getUserAlias(), aliasData.get(de.getUserAlias()));
			}
		}
		
		emailViewModel.addNewEmail(email);
	}

	private static void updateEmailWithData(Email email, String alias, String data) {
		logger.log(Level.INFO, "Updating email with data from row");
		data = updateMultiColumnDataWithSpace(data);
		
		email.setTo(email.getTo().replaceAll(alias, data));
		email.setCc(email.getCc().replaceAll(alias, data));
		email.setBcc(email.getBcc().replaceAll(alias, data));
		email.setSubject(email.getSubject().replaceAll(alias, data));
		email.setBody(email.getBody().replaceAll(alias, data));
	}
	
	private static String updateMultiColumnDataWithSpace(String data) {
		
		if (data.contains("~")) {
			return data.replace("~", " ").trim();
		}
		
		return data;
	}
	
	private static Email initNewEmailFromSettings() {
		logger.log(Level.INFO, "Initializing new email");
		return new Email(settings.getEmail().getFrom(),
								settings.getEmail().getTo(),
								settings.getEmail().getCc(),
								settings.getEmail().getBcc(),
								settings.getEmail().getSubject(),
								settings.getEmail().getBody(),
								settings.getEmail().getAttachments(),
								true);
	}
		
}
