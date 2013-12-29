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

public class EmailOptions extends TitledPane{
	private VBox rootPane;
	private Button sendEmailBtn;
	private Button sendAllEmailsBtn;
	private Button processFileBtn;
	private ImageView sendEmailImg;
	private ImageView sendAllEmailsImg;
	private ImageView processFileImg;
	
	public EmailOptions() {
		this.setText("Email");			
		this.initView();
	}
	
	public Button getSendEmailBtn() {
		return sendEmailBtn;
	}
	
	public Button getSendAllEmailsBtn() {
		return sendAllEmailsBtn;
	}
	
	public Button getProcessFileBtn() {
		return processFileBtn;
	}
	
	private void initView() {
		this.rootPane = new VBox();
		this.setContent(this.rootPane);
		this.rootPane.setPadding(new Insets(5,0,5,0));
		this.rootPane.setAlignment(Pos.TOP_CENTER);
		this.rootPane.setSpacing(5);
		
		this.sendEmailImg = new ImageView(Constants.SENDEMAIL_IMG);
		this.sendAllEmailsImg = new ImageView(Constants.SENDALL_EMAILS_IMG);
		this.processFileImg = new ImageView(Constants.PROCESS_IMG);
		
		this.sendEmailBtn = new Button("", this.sendEmailImg);
		this.sendAllEmailsBtn = new Button("", this.sendAllEmailsImg);
		this.processFileBtn = new Button("", this.processFileImg);
		
		this.sendEmailBtn.setId("imageOnlyButton");
		this.sendAllEmailsBtn.setId("imageOnlyButton");
		this.processFileBtn.setId("imageOnlyButton");
		
		AppControlDefaults.setImageViewSize(AppControlDefaults.NAVBTN_IMG, AppControlDefaults.NAVBTN_IMG, this.sendEmailImg, this.sendAllEmailsImg, this.processFileImg);
		AppControlDefaults.setNodeWidthHeight(AppControlDefaults.NAVBTN_WIDTH, AppControlDefaults.NAVBTN_HEIGHT, this.sendEmailBtn, this.sendAllEmailsBtn, this.processFileBtn);
		this.rootPane.getChildren().addAll(this.processFileBtn, this.sendEmailBtn, this.sendAllEmailsBtn);
		
		//Tooltips
		this.processFileBtn.setTooltip(new Tooltip("Process Excel file"));
		this.sendEmailBtn.setTooltip(new Tooltip("Send Current email"));
		this.sendAllEmailsBtn.setTooltip(new Tooltip("Send All emails"));
	}
}
