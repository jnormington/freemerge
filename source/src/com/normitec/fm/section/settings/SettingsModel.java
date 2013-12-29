package com.normitec.fm.section.settings;

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
import com.normitec.exception.ThrowableToString;
import com.normitec.fm.section.AppControlDefaults.Property;
import com.normitec.fm.section.email.Email;
import com.normitec.fm.utils.FileUtil;
import com.normitec.model.AbstractModel;


public class SettingsModel extends AbstractModel implements IJSONParseable<SettingsModel>, IJSONRequestable<JSONObject> {

	private static final Logger logger = Logger.getLogger(SettingsModel.class);
	private File configFile;	
	private String smtpHost;
	private String smtpPort;
	private String ldapURL;
	private String ldapBaseTree;
	private String dateFormat;
	private List<DataExtractor> dataExtractors;
	private DataExtractor selectedRow;
	private Email emailConfig;
	
	public SettingsModel() {
		this.initDefaultValues();
		this.initLists();
		//Default date to standard format
		this.setDateFormat("dd/MM/YYYY");
	}
	
	public enum Key {
		SmtpHost,
		SmtpPort,
		LdapURL,
		LdapBaseTree,
		LdapLoginRequired,
		DataExtractor,
		DateFormat,
		LdapRequired,
		UseVBImpl
	};

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
		this.firePropertyChanged(Key.DateFormat.name(), this.dateFormat);
	}
	
	public Email getEmail() {
		if (this.emailConfig == null)
			this.emailConfig = new Email();
		
		return emailConfig;
	}
	
	public void setEmail(Email email) {
		this.emailConfig = email;
		this.firePropertyChanged("Email_Config", email);
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
		this.firePropertyChanged(Key.SmtpHost.name(), smtpHost);
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
		this.firePropertyChanged(Key.SmtpPort.name(), smtpPort);
	}

	public void setLdapURL(String ldapURL) {
		this.ldapURL = ldapURL;		
		this.firePropertyChanged(Key.LdapURL.name(), ldapURL);
	}

	public void setLdapBaseTree(String ldapBaseTree) {
		this.ldapBaseTree = ldapBaseTree;
		this.firePropertyChanged(Key.LdapBaseTree.name(), ldapBaseTree);
	}
	
	public String getLdapURL() {
		return this.ldapURL;
	}
	
	public String getLdapBaseTree() {
		return this.ldapBaseTree;
	}
	
	public String getSmtpHost() {
		return this.smtpHost;
	}
	
	public String getSmtpPort() {
		return this.smtpPort;
	}

	public List<DataExtractor> getDataExtractors() {
		if (this.dataExtractors == null)
			this.dataExtractors = new ArrayList<DataExtractor>();
		return dataExtractors;
	}

	public void setDataExtractors(List<DataExtractor> dataExtractors) {
		this.dataExtractors = dataExtractors;
		this.firePropertyChanged(Key.DataExtractor.name(), this.dataExtractors);
	}
	
	public void addToDataExtrators(DataExtractor dataExtractor) {
		this.dataExtractors.add(dataExtractor);
		this.firePropertyChanged(Key.DataExtractor.name(), this.dataExtractors);	
	}
	
	public void removeDataExtrator(DataExtractor dataExtractor) {
		if (this.dataExtractors != null)
			this.dataExtractors.remove(dataExtractor);
		
		this.firePropertyChanged(Key.DataExtractor.name(), this.dataExtractors);
	}
	
	private void initLists() {
		if (this.dataExtractors == null)
			this.dataExtractors = new ArrayList<DataExtractor>();
	}
	
	public File getConfigFile() {
		return configFile;
	}

	public void setConfigFile(File configFile) {	
		if (configFile.isFile() && configFile.exists() && configFile.canRead()) {
			this.configFile = configFile;
			try {
				if (FileUtil.readFileAsString(configFile).length() != 0)
					this.parseJSONToObject(FileUtil.readFileAsString(configFile));
			}
			catch (NormitecException e) {
				ThrowableToString.exceptionToString(e);
				e.printStackTrace();
			}	
			this.reFireUpdates();
		}		
	}
	
	public DataExtractor getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(DataExtractor selectedRow) {
		this.selectedRow = selectedRow;
		this.firePropertyChanged("DataExtractorRowSelected", selectedRow);
	}
	
	@Override
	public JSONObject jsonRequestMessage(JSONObject json) throws JSONException {
		json.put(Key.SmtpHost.name(), this.smtpHost);
		json.put(Key.SmtpPort.name(), this.smtpPort);	
		json.put(Key.LdapURL.name(), this.ldapURL);
		json.put(Key.LdapBaseTree.name(), this.ldapBaseTree);
		json.put(Key.DateFormat.name(), this.dateFormat);
		json.put(Key.DataExtractor.name(), this.dataExtractors);
		json.put("EmailConfig", this.emailConfig.jsonRequestMessage(json));
		
		return json;
	}

	@Override
	public SettingsModel parseJSONToObject(String message) throws NormitecException {
		logger.log(Level.DEBUG, "Parse JSON object: " + message);
		JSONObject json = null;
		try {
			
			json = new JSONObject(message.replace(", \\", message));
			this.initLists();
			
			if (json.has(Key.SmtpHost.name()))
				this.smtpHost = json.getString(Key.SmtpHost.name());
			if (json.has(Key.SmtpPort.name()))
				this.smtpPort = json.getString(Key.SmtpPort.name());
			if (json.has("EmailConfig"))
				this.emailConfig = new Email().parseJSONToObject(json.getString("EmailConfig"));
			if (json.has(Key.LdapURL.name()))
				this.ldapURL = json.getString(Key.LdapURL.name());
			if (json.has(Key.LdapBaseTree.name()))
				this.ldapBaseTree = json.getString(Key.LdapBaseTree.name());
			
			if (json.has(Key.DataExtractor.name()) && json.getJSONArray(Key.DataExtractor.name()).length() > 0) {
				JSONArray ja = json.getJSONArray(Key.DataExtractor.name());				
				
				for (int idx = 0; idx < ja.length() ; idx++) {
					System.out.println(ja.get(idx).toString());
					this.dataExtractors.add(new DataExtractor().parseJSONToObject(ja.get(idx).toString()));
				}
			}
		} catch (JSONException | NormitecException e) {
			logger.log(Level.DEBUG, "Parse JSON object failed in settings model");
			this.firePropertyChanged(Property.LOAD_PROFILE.name(), e.getMessage(), false);
			e.printStackTrace();
			throw new NormitecException(e.getMessage());
		}
		return this;
	}
	
	public void writeSettingsToFile() throws NormitecException, JSONException {
		FileUtil.writeStringToFile(this.configFile,this.jsonRequestMessage(new JSONObject()).toString(4));
	}
	
	@Override
	public void reFireUpdates() {
		this.firePropertyChanged(Key.SmtpHost.name(), this.getSmtpHost());
		this.firePropertyChanged(Key.SmtpPort.name(), this.getSmtpPort());
		this.firePropertyChanged(Key.LdapURL.name(), this.getLdapURL());
		this.firePropertyChanged(Key.LdapBaseTree.name(), this.getLdapBaseTree());
		this.firePropertyChanged(Key.DateFormat.name(), this.getDateFormat());
		this.firePropertyChanged(Key.DataExtractor.name(), this.getDataExtractors());
		
		this.firePropertyChanged("EmailConfig", this.getEmail());
		this.firePropertyChanged("EmailAttachments", null);
	}
	
	@Override
	public void initDefaultValues() {
		this.ldapBaseTree = "";
		this.ldapURL = "";
		this.smtpHost = "";
		this.smtpPort = "";
		this.emailConfig = null;
		this.dataExtractors = null;
		this.dateFormat = "dd/MM/YYYY";
		this.configFile = null;
		this.selectedRow = null;
	}
}
