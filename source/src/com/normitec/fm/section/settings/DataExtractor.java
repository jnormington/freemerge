package com.normitec.fm.section.settings;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.custom.IJSONParseable;

import com.normitec.exception.NormitecException;
import com.normitec.model.AbstractModel;

public class DataExtractor extends AbstractModel implements IJSONParseable<DataExtractor> {
	private final Logger logger = Logger.getLogger(getClass());
	private String userAlias;
	private List<Integer> columnNumbers;
	private Boolean useLDAP;
	private String ldapOption;
	private Boolean emailRequired;
	
	public DataExtractor() {
	}
	
	public DataExtractor(String userAlias, String columnNumber, 
					boolean ldapQuery, boolean emailRequired, String ldapOption) {		
		this.initLists();
		this.userAlias = userAlias;
		this.splitColumnNumbersAndAdd(columnNumber);
		this.useLDAP = ldapQuery;
		this.ldapOption = ldapOption;
		this.emailRequired = emailRequired;
	}
	
	public String getUserAlias() {
		return userAlias;
	}
	
	private void initLists() {
		if (this.columnNumbers == null)
			this.columnNumbers = new ArrayList<Integer>();
	}
	
	private void splitColumnNumbersAndAdd(final String columnNumbers) {
		if (columnNumbers.contains(",")) {
			for(String s : columnNumbers.split(",")) {
				this.columnNumbers.add(Integer.parseInt(s));
			}
		} else {
			this.columnNumbers.add(Integer.parseInt(columnNumbers));
		}
	}
	
	public static boolean isColumnNumberStringValid(final String columnNumbers) {
		boolean valid = true;
		
		if (columnNumbers.contains(",") && columnNumbers.split(",").length > 0) {
			for(String s : columnNumbers.split(",")) {
				try {
					if (s.trim().length() == 0 || (!(Integer.parseInt(s) > 0)))
						valid = false;
				} catch (NumberFormatException ne) {
					ne.printStackTrace();
					valid = false;
				}
			}
		} else {
			try {
				if (!(Integer.parseInt(columnNumbers) > 0))
					valid = false;
			} catch (NumberFormatException ne) {
				ne.printStackTrace();
				valid = false;
			}
		}
		
		return valid;
	}
	
	public List<Integer> getColumnNumber() {
		return columnNumbers;
	}
	
	public Boolean isUseLDAP() {
		return useLDAP;
	}
	
	public String getLdapOption() {
		return ldapOption;
	}
	
	public Boolean isEmailRequired() {
		return emailRequired;
	}
	
	public void setUserAlias(String userAlias) {
		this.userAlias = userAlias;
	}
			
	public void setUseLDAP(Boolean ldapQuery) {
		this.useLDAP = ldapQuery;
	}
	
	public void setLdapOoption(String ldapOoption) {
		this.ldapOption = ldapOoption;
	}
	
	public void setEmailRequired(Boolean required) {
		this.emailRequired = required;
	}

	@Override
	public DataExtractor parseJSONToObject(String message) throws NormitecException {
		logger.log(Level.DEBUG, "Parse JSON object: " + message);
		JSONObject json = null;
		this.initLists();
		try {
			json = new JSONObject(message.replace(", \\", message));
			
			if (json.has("userAlias"))
				this.userAlias = json.getString("userAlias");
			
			if (json.has("columnNumber")  && json.getJSONArray("columnNumber").length() > 0) {
				json.getJSONArray("columnNumber");
				JSONArray ja = json.getJSONArray("columnNumber");
			
				for (int i=0; i < json.getJSONArray("columnNumber").length(); i++) {
					this.columnNumbers.add(ja.getInt(i));
				}
			}
			
			if (json.has("useLDAP"))
				this.useLDAP = json.getBoolean("useLDAP");
			if (json.has("emailRequired"))
				this.emailRequired = json.getBoolean("emailRequired");			
			if (json.has("ldapOption"))
				this.ldapOption = json.getString("ldapOption");	
		} catch (JSONException e) {
			logger.log(Level.DEBUG, "Parse JSON object failed: " + json.toString());
			e.printStackTrace();
			throw new NormitecException(e.getMessage());
		}
		
		return this;
	}
}
