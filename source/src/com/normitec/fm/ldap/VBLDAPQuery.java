package com.normitec.fm.ldap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.normitec.exception.NormitecException;
import com.normitec.fm.Constants;
import com.normitec.fm.utils.StreamUtil;

public class VBLDAPQuery implements ILdapQuerable {
	private static final Logger logger = Logger.getLogger(VBLDAPQuery.class);
	private File vbFile;
	
	public VBLDAPQuery(File vbFile) {
		this.vbFile = vbFile;
	}
			
	public String search(String ldapURL, String firstName, String lastName) throws NormitecException {
		
		Process p = null;
		String line = "";
		
		if (this.vbFile == null)
			throw new NormitecException("VBScript file doesn't exist...");
		
		try {
			logger.log(Level.DEBUG, "Executing command for VBScript...");
			logger.log(Level.DEBUG, "Executing: " + "cscript.exe //Nologo \"" + Constants.VBSCRIPT_LDAP_IMPL_SCRIPT + "\" \"" + ldapURL + "\" " + firstName + " " + lastName);
		      p = Runtime.getRuntime().exec("cscript.exe //Nologo \"" + Constants.VBSCRIPT_LDAP_IMPL_SCRIPT + "\" \"" + ldapURL + "\" " + firstName + " " + lastName);
		      p.waitFor();
		      
		    logger.log(Level.DEBUG,"Execution has finished...");
		      InputStream is = p.getInputStream();
		      InputStreamReader isr = new InputStreamReader(is);
		      BufferedReader br = new BufferedReader(isr);

		      logger.log(Level.DEBUG, "**** VBScript output start ****");
		      System.out.println(is);
		      
		      while ((line = br.readLine()) != null) {
		    	  logger.log(Level.DEBUG, line);
		    	  if (line.contains("mail:") && line.contains("@")) {
		    		  line = line.substring("mail:".length(), line.length());
		    		  break;
		    	  }		      
		      }
		      
		      if (line == null) {
		    	  logger.log(Level.INFO, "Potentially an error may have occurred please run the VBScript manually.");
		      }
		      
		      logger.log(Level.DEBUG, "**** VBScript output ends ****");
		    }
		    catch (Exception err) {
		      throw new NormitecException(err);
		    }
		
		return line == null ? "" : line ;
	}

	public String search(String searchBase,	String compoundSearchFilter) throws NormitecException {
		throw new NormitecException("Method signature search(String, String) not implemented.");
	}
	
	public void writeVBScriptImplToFile() throws NormitecException {
		FileWriter writer = null;
		try {
			
			InputStream is = this.getClass().getResourceAsStream(Constants.VBSCRIPT_IN_JAR);
			writer = new FileWriter(vbFile);
			String toWriteOut = StreamUtil.convertStreamToString(is);
			writer.write(toWriteOut);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new NormitecException(e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}	
	}

	/**
	 * Doesn't do anything using VBScript so auto closes connection.
	 */
	@Override
	public void closeConnection() {
	}
}
