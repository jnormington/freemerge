package com.normitec.fm.section.navigator;

import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import com.normitec.fm.section.navigator.categories.EmailOptions;
import com.normitec.fm.section.navigator.categories.GlobalOptions;
import com.normitec.fm.section.navigator.categories.SettingOptions;

public class Navigator extends ScrollPane {

	private VBox rootPane;
	private GlobalOptions globalOptions;
	private EmailOptions emailOptions;
	private SettingOptions settingOptions;
	
	public Navigator() {
		this.setPrefWidth(110);
		this.setVmax(1);
		this.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		this.setFitToWidth(true);
		this.setId("theme");

		this.initView();
	}

	public VBox getRootPane() {
		return this.rootPane;
	}

	public GlobalOptions getGlobalOptions() {
		return globalOptions;
	}
	
	public EmailOptions getEmailOptions() {
		return this.emailOptions;
	}
	
	public SettingOptions getSettingsOptions() {
		return this.settingOptions;
	}

	/***
	 * Initialises all the nodes required for the display
	 */
	private void initView() {
		this.rootPane = new VBox();
		this.setContent(this.rootPane);
		this.rootPane.setAlignment(Pos.TOP_CENTER);

		this.globalOptions = new GlobalOptions();
		this.rootPane.getChildren().addAll(this.globalOptions);
		this.emailOptions = new EmailOptions();
		this.settingOptions = new SettingOptions();
	}
}
