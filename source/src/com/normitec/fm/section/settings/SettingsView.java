package com.normitec.fm.section.settings;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import com.normitec.fm.section.email.EmailView;

public class SettingsView extends TabPane {
	private DataExtractorView extratorView;
	private EmailView emailView;
	private MiscOptionsView miscView;
	
	public SettingsView() {
		this.emailView = new EmailView(true);
		this.miscView = new MiscOptionsView();
		this.extratorView = new DataExtractorView();
		this.initView();
	}
	
	public DataExtractorView getExtratorView() {
		return extratorView;
	}
	
	public EmailView getEmailView() {
		return emailView;
	}
	
	public MiscOptionsView getMiscView() {
		return miscView;
	}
	
	public void setExtratorView(DataExtractorView extratorView) {
		this.extratorView = extratorView;
	}

	public void setEmailView(EmailView emailView) {
		this.emailView = emailView;
	}
	
	public void setMiscView(MiscOptionsView miscView) {
		this.miscView = miscView;
	}

	private void initView() {
		this.getTabs().add(this.createStandardTab("Email Configuration", this.emailView));		
		this.getTabs().add(this.createStandardTab("Data Extractor", this.extratorView));		
		this.getTabs().add(this.createStandardTab("Misc Options", this.miscView));		
		this.emailView.setId("theme");
		this.extratorView.setId("theme");
		this.miscView.setId("theme");
		this.getSelectionModel().select(1);
	}
	
	private Tab createStandardTab(String header, Node content) {
		Tab t = new Tab(header);
		t.setClosable(false);
		t.setContent(content);
		
		return t;
	}
}
