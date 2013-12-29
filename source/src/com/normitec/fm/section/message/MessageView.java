package com.normitec.fm.section.message;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.normitec.fm.section.AppControlDefaults;


public class MessageView extends VBox {
	private TextArea message;
	private Button yesBtn;
	private Button noBtn;

	public MessageView(String message) {
		super(15);
		// Setup display and constraints of pane
		this.setMaxSize(400, 250);
		this.setAlignment(Pos.CENTER_RIGHT);
		this.setId("AlertDialog");
		
		this.initView(message);
	}
	
	private void initView(String message) {
		this.message = new TextArea(message);
		this.message.setEditable(false);
		this.message.setWrapText(true);
		AppControlDefaults.setNodeHeight(200, this.message);
		
		HBox option = new HBox(10);
		this.yesBtn = new Button("Yes");
		this.noBtn = new Button("No");
		option.getChildren().addAll(this.yesBtn, this.noBtn);
		option.setAlignment(Pos.CENTER_RIGHT);
		
		this.getChildren().addAll(this.message, option);
		AppControlDefaults.setNodeHeight(AppControlDefaults.CTRL_HEIGHT, this.yesBtn, this.noBtn);
	}
	
	public Button getYesBtn() {
		return this.yesBtn;
	}
	
	public Button getNoBtn() {
		return this.noBtn;
	}
}
