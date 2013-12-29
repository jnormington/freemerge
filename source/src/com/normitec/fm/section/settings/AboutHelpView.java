package com.normitec.fm.section.settings;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import com.normitec.fm.Constants;
import com.normitec.fm.section.AppControlDefaults;

public class AboutHelpView extends VBox {
	private ImageView logo;
	private Text developedBy;
	private Text designBy;
	private Text testedBy;
	private Text supportEmail;
	private VBox inner;
	private Text version;

	public AboutHelpView() {
		super(50);
		// Setup display and constraints of pane
		this.setMaxSize(USE_PREF_SIZE,USE_PREF_SIZE);
		this.setAlignment(Pos.CENTER_RIGHT);
		this.setId("AboutHelpView");
		this.initView();
	}
	
	private void initView() {
		this.inner = new VBox();
		this.inner.setSpacing(10);
		this.inner.setAlignment(Pos.CENTER_LEFT);
		
		this.logo = new ImageView(Constants.LOGO);
		AppControlDefaults.setImageViewSize(500, 150, this.logo);
		this.logo.setId("logo");

		this.version = new Text("Version: " + Constants.VERSION);
		this.developedBy = new Text("Developed By: Jon Normington"); 
		this.designBy = new Text("Workflow Design By: Peter Normington");
		this.testedBy = new Text("Tested By: Peter Normington & Jon Normington");
		this.supportEmail = new Text("Support Email: normitec@gmail.com");
		Font textSize = new Font(20);
		
		this.developedBy.setFill(Color.WHITESMOKE);
		this.designBy.setFill(Color.WHITESMOKE);
		this.testedBy.setFill(Color.WHITESMOKE);
		this.supportEmail.setFill(Color.WHITESMOKE);
		this.version.setFill(Color.WHITESMOKE);
		this.supportEmail.setFont(textSize);
		this.designBy.setFont(textSize);
		this.testedBy.setFont(textSize);
		this.version.setFont(textSize);
		this.developedBy.setFont(textSize);
		this.developedBy.setId("logo");
		this.supportEmail.setId("logo");
		this.version.setId("logo");
		this.testedBy.setId("logo");
		this.designBy.setId("logo");
		
		this.inner.getChildren().addAll(this.version, this.developedBy,this.designBy, this.testedBy, this.supportEmail);
		this.inner.setMaxSize(200, 100);
		
		this.getChildren().addAll(this.logo, this.inner);
	}
}
