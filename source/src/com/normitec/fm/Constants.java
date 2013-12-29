package com.normitec.fm;

public class Constants {

	public static final String APP_NAME = "FreEMerge";
	public static final String THEME_CSS = "/com/normitec/fm/resources/bbt.css";
	public static final String USER_HOME = System.getProperty("user.home");
	public static final String DIR_SEPERATOR = System.getProperty("file.separator");
	public static final String APP_STORAGE = USER_HOME + DIR_SEPERATOR + "FreEMerge";
	public static final String VBSCRIPT_PATH = APP_STORAGE + DIR_SEPERATOR + "script";
	public static final String VBSCRIPT_LDAP_IMPL_SCRIPT = VBSCRIPT_PATH + DIR_SEPERATOR + "EasyVBLdap.vbs";
	public static final String VBSCRIPT_IN_JAR = "/com/normitec/fm/resources/script/EasyVBLdap.vbs";
	
	public static final String BASEDIR_IMG = "/com/normitec/fm/resources/image/";
	public static final String PREFERENCES_IMG = BASEDIR_IMG + "preferences.png";
	public static final String PROCESS_IMG = BASEDIR_IMG + "process.png";
	public static final String PREV_IMG = BASEDIR_IMG + "prev.png";
	public static final String NEXT_IMG = BASEDIR_IMG + "next.png";
	public static final String ATTACH_IMG = BASEDIR_IMG + "attach.png";
	public static final String SAVE_IMG = BASEDIR_IMG + "save.png";
	public static final String SENDEMAIL_IMG = BASEDIR_IMG + "send-mail.png";
	public static final String SENDALL_EMAILS_IMG = BASEDIR_IMG + "send-all.png";
	public static final String DELETE_IMG = BASEDIR_IMG + "delete.png";
	public static final String SELECT_IMG = BASEDIR_IMG + "select.png";
	public static final String ADD_IMG = BASEDIR_IMG + "add.png";
	public static final String CLEAR_IMG = BASEDIR_IMG + "clear.png";
	public static final String HELP_IMG = BASEDIR_IMG + "help.png";
	public static final String EMAILSECTION_IMG = BASEDIR_IMG + "email.png";
	public static final String HOME_IMG = BASEDIR_IMG + "home.png";
	public static final String LOGO = BASEDIR_IMG + "logo.png";
	public static final String APP128 = BASEDIR_IMG + "app128.png";
	public static final String APP96 = BASEDIR_IMG + "app96.png";
	public static final String APP64 = BASEDIR_IMG + "app64.png";
	public static final String APP32 = BASEDIR_IMG + "app32.png";
	public static final String APP48 = BASEDIR_IMG + "app48.png";
	public static final String APP24 = BASEDIR_IMG + "app24.png";
	public static final String APP12 = BASEDIR_IMG + "app12.png";
	
	public static final String[] LDAP_OPTIONS = {"NONE","FIRST & LAST NAME"};
	public static final String EMAILVALID_SEPERATOR = "-";
	
	public static final String VERSION = "2.0.1.1";
	public static final String EMAIL = "Normitec@gmail.com";
	public static final String DEVELOPER = "Jon Normington";
	public static final String DESIGNER = "Peter Normington";
	
	
	public static final String USERALIAS_ERR = "User Alias: Must contain the following to be valid; \n\n - % (percent sign) at the beginning and at the end. " +
			"\n\nShouldn't contain the following chracters to also be valid;\n\n - Space\n - (~) tilda character\n - (-) hyphen character\n\n";	
}
