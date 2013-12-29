package com.normitec.fm.section.settings;

import com.normitec.fm.section.AppControlDefaults;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class MiscOptionsView extends GridPane {
	private Label ldapURLLbl;
	private TextField ldapURL;
	private Label smtpServerLbl;
	private TextField smtpServer;
	private Label smtpPortLbl;
	private TextField smtpPort;
	private Label dateFormatLbl;
	private TextField dateFormat;
	private Insets insets;
	
	public MiscOptionsView() {
		this.initView();
	}
	
	private void initView() {
		this.insets = new Insets(5,0,0,5);
		
		this.ldapURLLbl = new Label("LDAP URL");
		this.smtpServerLbl = new Label("SMTP Server");
		this.smtpPortLbl = new Label("SMTP Port");
		this.dateFormatLbl = new Label("Date Format");
		this.ldapURL = new TextField();
		this.smtpPort = new TextField();
		this.smtpServer = new TextField();
		this.dateFormat = new TextField();
		
		this.getColumnConstraints().add(0, new ColumnConstraints(100));
		this.getColumnConstraints().get(0).setHalignment(HPos.RIGHT);
		
		this.add(this.ldapURLLbl, 0, 0);
		this.add(this.ldapURL, 1, 0);
		this.add(this.smtpServerLbl, 0, 1);
		this.add(this.smtpServer, 1, 1);
		this.add(this.smtpPortLbl, 0, 2);
		this.add(this.smtpPort, 1, 2);
		this.add(this.dateFormatLbl, 0, 4);
		this.add(this.dateFormat, 1, 4);
		
		GridPane.setMargin(this.ldapURL, this.insets);
		GridPane.setMargin(this.smtpServer, this.insets);
		GridPane.setMargin(this.smtpPort, this.insets);
		GridPane.setMargin(this.dateFormat, this.insets);
		GridPane.setHgrow(this.ldapURL, Priority.SOMETIMES);
		GridPane.setHgrow(this.smtpPort, Priority.SOMETIMES);
		GridPane.setHgrow(this.smtpServer, Priority.SOMETIMES);
		GridPane.setHgrow(this.dateFormat, Priority.SOMETIMES);
		
		AppControlDefaults.setNodeWidthHeight(400, AppControlDefaults.CTRL_HEIGHT, this.ldapURL,this.smtpServer);
		AppControlDefaults.setNodeWidthHeight(100, AppControlDefaults.CTRL_HEIGHT, this.smtpPort,this.dateFormat);
	}
	
	public TextField getLdapURL() {
		return ldapURL;
	}

	public TextField getSmtpServer() {
		return smtpServer;
	}

	public TextField getSmtpPort() {
		return smtpPort;
	}

	public void setLdapURL(TextField ldapURL) {
		this.ldapURL = ldapURL;
	}
	
	public void setSmtpServer(TextField smtpServer) {
		this.smtpServer = smtpServer;
	}
	
	public void setSmtpPort(TextField smtpPort) {
		this.smtpPort = smtpPort;
	}
	
	public TextField getDateFormat() {
		return this.dateFormat;
	}
}
