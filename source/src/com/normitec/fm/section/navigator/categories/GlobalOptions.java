package com.normitec.fm.section.navigator.categories;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import com.normitec.fm.Constants;
import com.normitec.fm.section.AppControlDefaults;

public class GlobalOptions extends TitledPane{
	private VBox rootPane;
	private Button preferencesBtn;
	private Button emailViewBtn;
	private Button helpBtn;
	private Button homeScreenBtn;
	private ImageView preferencesImg;
	private ImageView emailViewImg;
	private ImageView helpImg;
	private ImageView homeScreenImg;
	
	public GlobalOptions() {
		this.setText("Global");	
		
		this.initView();
	}
	
	public Button getPreferencesBtn() {
		return preferencesBtn;
	}
	
	public Button getEmailViewBtn() {
		return emailViewBtn;
	}
	
	public Button getHelpBtn() {
		return this.helpBtn;
	}	
	
	public Button getHomeScreenBtn() {
		return this.homeScreenBtn;
	}
	
	private void initView() {
		this.rootPane = new VBox();
		this.setContent(this.rootPane);
		this.rootPane.setPadding(new Insets(5,0,5,0));
		this.rootPane.setAlignment(Pos.TOP_CENTER);
		this.rootPane.setSpacing(5);
		
		this.preferencesImg = new ImageView(Constants.PREFERENCES_IMG);
		this.emailViewImg = new ImageView(Constants.EMAILSECTION_IMG);
		this.helpImg = new ImageView(Constants.HELP_IMG);
		this.homeScreenImg = new ImageView(Constants.HOME_IMG);
		this.emailViewBtn = new Button("", this.emailViewImg);
		this.preferencesBtn = new Button("", this.preferencesImg);
		this.homeScreenBtn = new Button("", this.homeScreenImg);
		this.helpBtn = new Button("", this.helpImg);
		
		this.emailViewBtn.setId("imageOnlyButton");
		this.preferencesBtn.setId("imageOnlyButton");
		this.helpBtn.setId("imageOnlyButton");
		this.homeScreenBtn.setId("imageOnlyButton");
		
		AppControlDefaults.setImageViewSize(AppControlDefaults.NAVBTN_IMG, AppControlDefaults.NAVBTN_IMG, this.preferencesImg, this.emailViewImg, this.helpImg, this.homeScreenImg);
		AppControlDefaults.setNodeWidthHeight(AppControlDefaults.NAVBTN_WIDTH, AppControlDefaults.NAVBTN_HEIGHT, this.preferencesBtn, this.emailViewBtn, this.helpBtn, this.homeScreenBtn);
		this.rootPane.getChildren().addAll(this.emailViewBtn, this.preferencesBtn, this.homeScreenBtn, this.helpBtn);
		
		//Tooltips
		this.emailViewBtn.setTooltip(new Tooltip("Process Emails"));
		this.preferencesBtn.setTooltip(new Tooltip("Manage Settings"));
		this.homeScreenBtn.setTooltip(new Tooltip("Select new Profile"));
	}
}