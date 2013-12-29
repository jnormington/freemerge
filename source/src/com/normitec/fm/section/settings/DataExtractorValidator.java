package com.normitec.fm.section.settings;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.normitec.fm.Constants;

public class DataExtractorValidator {
	private static final Logger logger = Logger.getLogger(DataExtractorValidator.class);
	
	private static boolean isUserAliasExisting(List<DataExtractor> dataExtractors, String userAlias) {
		logger.log(Level.INFO, "Checking if user alias is existing");
		for (DataExtractor de : dataExtractors) {
			if (userAlias.toLowerCase().equals(de.getUserAlias().toLowerCase())) {
				logger.log(Level.DEBUG, "Is alias existing: " + true);
				return true;
			}
		}
		logger.log(Level.DEBUG, "Is alias existing: " + false);
		return false;
	}
	
	public static String isDataExtractorEntryValid(List<DataExtractor> dataExtractors, String userAlias, String columnNumbers, String ldapOption, boolean ldapRequired, boolean emailRequired, boolean isUpdate) {
		logger.log(Level.INFO, "Checking if data extractor is valid");
		StringBuilder problems = new StringBuilder();
		//User Alias is valid already and isn't modifiable
		if (!isUpdate) {
			if (userAlias == null || userAlias.contains(" ") || userAlias.contains("~") || userAlias.contains("-") || userAlias.length() < 3 || !userAlias.substring(0, 1).equals("%") || !userAlias.substring(userAlias.length()-1).equals("%")) {
				userAlias = "";
				problems.append(Constants.USERALIAS_ERR);
			}
			if (DataExtractorValidator.isUserAliasExisting(dataExtractors, userAlias))
				problems.append("User Alias: Already exists and must be unique\n");
		}
		
		try {
			if (!DataExtractor.isColumnNumberStringValid(columnNumbers)) {
				problems.append("Column Number: Must contain only positive numbers or positive numbers seperated by a , (comma).\n");
			}			
		} catch (NumberFormatException ne) {
			ne.printStackTrace();
			if (!problems.toString().contains("Column Number:"))
				problems.append("Column Number: Must be a postive number.\n");
		}
		
		if (ldapOption.equals("NONE") && (ldapRequired || emailRequired))
			problems.append("LDAP Option: Must not be 'NONE' when LDAP Required or Email Required is selected.\n");
		if (!ldapOption.equals("NONE") && ((emailRequired || !emailRequired) && !ldapRequired))
			problems.append("Use Ldap: Must be selected when the LDAP Option is not 'NONE'.\n");		
		
		if (problems.toString().contains("User Alias:"))
			problems.append("\n\nNote:\nIf you would like to edit an existing entry please select it in the table and amend it.\n");
			
		logger.log(Level.INFO, "Is data extractor valid: " + (problems.toString().length() == 0 ? true : false));
		return problems.toString(); 
	}
}
