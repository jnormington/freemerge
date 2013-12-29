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

public class SettingOptions extends TitledPane{
	private VBox rootPane;
	private Button saveBtn;
	private ImageView saveImg;
	
	public SettingOptions() {
		this.setText("Settings");			
		this.initView();
	}
	
	public Button getSaveBtn() {
		return saveBtn;
	}
	
	private void initView() {
		this.rootPane = new VBox();
		this.setContent(this.rootPane);
		this.rootPane.setPadding(new Insets(5,0,5,0));
		this.rootPane.setAlignment(Pos.TOP_CENTER);
		this.rootPane.setSpacing(5);
		
		this.saveImg = new ImageView(Constants.SAVE_IMG);
		this.saveBtn = new Button("", this.saveImg);
		
		this.saveBtn.setId("imageOnlyButton");
		
		AppControlDefaults.setImageViewSize(AppControlDefaults.NAVBTN_IMG, AppControlDefaults.NAVBTN_IMG, this.saveImg);
		AppControlDefaults.setNodeWidthHeight(AppControlDefaults.NAVBTN_WIDTH, AppControlDefaults.NAVBTN_HEIGHT, this.saveBtn);
		this.rootPane.getChildren().addAll(this.saveBtn);
		
		//Tooltips
		this.saveBtn.setTooltip(new Tooltip("Save Changes"));
	}
}
