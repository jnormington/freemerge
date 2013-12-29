package com.normitec.fm.section.email;

import java.io.File;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import com.normitec.fm.Constants;
import com.normitec.fm.section.AppControlDefaults;

public class EmailView extends GridPane {
	private Label fromLbl;
	private Label toLbl;
	private Label ccLbl;
	private Label bccLbl;
	private Label subjectLbl;
	private Label bodyLbl;
	private Label attachLbl;
	private Label emailStatusLbl;
	private Label emailValidsLbl;
	private Label emailUnsentLbl;
	private TextField from;
	private TextField to;
	private TextField cc;
	private TextField bcc;
	private TextField subject;
	private TextArea body;
	private TextField emailStatus;
	private ListView<File> attachments;
	private ListView<String> emailValids;
	private Button firstRecordBtn;
	private Button lastRecordBtn;
	private Button nextBtn;
	private Button prevBtn;
	private Button addAttachmentBtn;
	private Button removeAttachmentBtn;
	private Insets insets;
	private HBox emailOptions;
	private TextField emailIndex;
	private Label totalEmailsLbl;
	private CheckBox hideCompleted;
	private ImageView removeImg;
	private ImageView addImg;
	
	public EmailView() {
		this(false);
	}
	
	public EmailView(boolean isSettingDisplay) {
		this.getColumnConstraints().add(new ColumnConstraints(100));
		this.initView(isSettingDisplay);
	}
	
	private void initView(boolean isSettingDisplay) {
		this.removeImg = new ImageView(Constants.DELETE_IMG);
		this.addImg = new ImageView(Constants.ADD_IMG);
		//Make it look the same size as delete btn
		AppControlDefaults.setImageViewSize(30, 30, this.addImg);
		
		this.fromLbl = new Label("From");
		this.toLbl = new Label("To");
		this.ccLbl = new Label("CC");
		this.bccLbl = new Label("BCC");
		this.subjectLbl = new Label("Subject");
		this.bodyLbl = new Label("Body");
		this.attachLbl = new Label("Attachments");
		this.emailStatusLbl = new Label("Status");
		this.emailValidsLbl = new Label("Validations");
		this.emailUnsentLbl = new Label("Unsent emails: 0/0");
		this.from = new TextField();
		this.to = new TextField();
		this.cc = new TextField();
		this.bcc = new TextField();
		this.subject = new TextField();
		this.emailStatus = new TextField();
		this.emailStatus.setEditable(false);
		this.body = new TextArea();
		this.attachments = new ListView<File>();
		this.emailValids = new ListView<String>();
		
		this.emailOptions = new HBox(5);
		this.hideCompleted = new CheckBox("Hide Completed");
		this.hideCompleted.setPrefWidth(150);
		this.emailIndex = new TextField("0");
		this.emailIndex.setAlignment(Pos.CENTER);
		this.totalEmailsLbl = new Label(" / 0");
		HBox.setMargin(this.totalEmailsLbl, new Insets(0,25,0,0));
		HBox.setMargin(this.emailUnsentLbl, new Insets(0,50,0,0));
		this.prevBtn = new Button("<");
		this.nextBtn = new Button(">");
		AppControlDefaults.setNodeHeight(30, this.prevBtn,this.nextBtn);
		this.emailOptions.setAlignment(Pos.CENTER_RIGHT);
		this.emailOptions.getChildren().addAll(this.emailUnsentLbl, this.hideCompleted, this.emailIndex, this.totalEmailsLbl, this.prevBtn, this.nextBtn);
		this.insets = new Insets(5,5,5,5);
		
		this.removeAttachmentBtn = new Button("",this.removeImg);
		this.removeAttachmentBtn.setId("imageOnlyButton");
		this.addAttachmentBtn = new Button("",this.addImg);
		this.addAttachmentBtn.setId("imageOnlyButton");
		AppControlDefaults.setNodeWidthHeight(32,32,this.removeAttachmentBtn,this.addAttachmentBtn);
		
		this.getColumnConstraints().add(new ColumnConstraints());
		ColumnConstraints bc = new ColumnConstraints();
		bc.setHgrow(Priority.ALWAYS);
		cc.setMaxWidth(Double.MAX_VALUE);
		this.getColumnConstraints().add(bc);		
		ColumnConstraints cc = new ColumnConstraints();
		cc.setHgrow(Priority.NEVER);
		cc.setPrefWidth(80);
		cc.setMaxWidth(80);
		cc.setHalignment(HPos.RIGHT);
		this.getColumnConstraints().add(cc);
		ColumnConstraints nc = new ColumnConstraints();
		nc.setHgrow(Priority.ALWAYS);
		nc.setMaxWidth(Double.MAX_VALUE);
		this.getColumnConstraints().add(nc);
		this.getColumnConstraints().add(new ColumnConstraints(40));
		
		//Constraints
		GridPane.setConstraints(this.emailOptions, 1, 0, 5, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER, this.insets);
		GridPane.setConstraints(this.fromLbl, 0, 2, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
		GridPane.setConstraints(this.toLbl, 0, 3, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
		GridPane.setConstraints(this.ccLbl, 0, 4, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
		GridPane.setConstraints(this.bccLbl, 0, 5, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
		GridPane.setConstraints(this.subjectLbl, 0, 6, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
		GridPane.setConstraints(this.attachLbl, 0, 7, 1, 2, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
		GridPane.setConstraints(this.bodyLbl, 0, 9, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);	
		
		GridPane.setConstraints(this.from, 1, 2, 5, 1, HPos.LEFT, VPos.TOP, Priority.ALWAYS, Priority.NEVER, this.insets);
		GridPane.setConstraints(this.to, 1, 3, 5, 1, HPos.LEFT, VPos.TOP, Priority.ALWAYS, Priority.NEVER, this.insets);
		GridPane.setConstraints(this.cc, 1, 4, 5, 1, HPos.LEFT, VPos.TOP, Priority.ALWAYS, Priority.NEVER, this.insets);
		GridPane.setConstraints(this.bcc, 1, 5, 5, 1, HPos.LEFT, VPos.TOP, Priority.ALWAYS, Priority.NEVER, this.insets);
		GridPane.setConstraints(this.subject, 1, 6, 5, 1, HPos.LEFT, VPos.TOP, Priority.ALWAYS, Priority.NEVER, this.insets);
		GridPane.setConstraints(this.body, 1, 9, 5, 1, HPos.LEFT, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS, this.insets);
				
		AppControlDefaults.setNodeWidthHeight(70, AppControlDefaults.CTRL_HEIGHT, this.emailIndex);
		AppControlDefaults.setNodeHeight(AppControlDefaults.CTRL_HEIGHT, this.from, this.to, this.cc, this.bcc, this.subject, this.emailStatus);
		AppControlDefaults.setNodeHeight(100,this.attachments, this.emailValids);
		
		this.body.setWrapText(true);
		this.getChildren().addAll(this.fromLbl, this.toLbl,this.ccLbl,this.bccLbl, this.subjectLbl, this.attachLbl, this.bodyLbl);
		this.getChildren().addAll(this.from, this.to,this.cc,this.bcc, this.subject, this.attachments, this.body);
		
		if (!isSettingDisplay) {
			GridPane.setConstraints(this.emailStatusLbl, 0, 1, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
			GridPane.setConstraints(this.emailStatus, 1, 1, 5, 1, HPos.LEFT, VPos.TOP, Priority.NEVER, Priority.NEVER, this.insets);
			GridPane.setConstraints(this.attachments, 1, 7, 2, 1, HPos.LEFT, VPos.TOP, Priority.ALWAYS, Priority.NEVER, this.insets);
			GridPane.setConstraints(this.emailValidsLbl, 3, 7, 1, 2, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER);
			GridPane.setConstraints(this.emailValids, 4, 7, 2, 2, HPos.LEFT, VPos.TOP, Priority.ALWAYS, Priority.NEVER, this.insets);
			this.getChildren().addAll(this.emailOptions,this.emailStatusLbl,this.emailStatus, this.emailValidsLbl, this.emailValids);
		} else {
			GridPane.setConstraints(this.attachments, 1, 7, 4, 2, HPos.LEFT, VPos.TOP, Priority.ALWAYS, Priority.NEVER, this.insets);
			GridPane.setConstraints(this.addAttachmentBtn, 5, 7, 1, 1, HPos.RIGHT, VPos.CENTER, Priority.NEVER, Priority.NEVER, new Insets(5,5,0,0));
			GridPane.setConstraints(this.removeAttachmentBtn, 5, 8, 1, 1, HPos.RIGHT, VPos.TOP, Priority.NEVER, Priority.NEVER, new Insets(5,5,0,0));
			this.getChildren().addAll(this.addAttachmentBtn, this.removeAttachmentBtn);
		}
	}
	
	public void setAllCompDisabled(boolean disable) {
		this.from.setDisable(disable);
		this.to.setDisable(disable);
		this.cc.setDisable(disable);
		this.bcc.setDisable(disable);
		this.subject.setDisable(disable);
		this.body.setDisable(disable);
		this.attachments.setDisable(disable);
		this.emailValids.setDisable(disable);
		this.emailStatus.setDisable(disable);
	}
	
	public TextField getFromField() {
		return this.from;
	}
	
	public TextField getToField() {
		return this.to;
	}
	
	public TextField getCcField() {
		return this.cc;
	}
	
	public TextField getBccField() {
		return this.bcc;
	}
	
	public TextField getSubjectField() {
		return this.subject;
	}
	
	public TextArea getBodyField() {
		return this.body;
	}
	
	public ListView<File> getAttachments() {
		return this.attachments;
	}
	
	public ListView<String> getEmailValidations() {
		return this.emailValids;
	}
	
	public Button getFirstRecordBtn() {
		return this.firstRecordBtn;
	}
	
	public Button getLastRecordBtn() {
		return this.lastRecordBtn;
	}
	
	public Button getNextBtn() {
		return this.nextBtn;
	}	

	public Button getPrevBtn() {
		return this.prevBtn;
	}
	
	public TextField getEmailStatus() {
		return this.emailStatus;
	}
	
	public Button getAddAttachmentBtn() {
		return this.addAttachmentBtn;
	}

	public Button getRemoveAttachmentBtn() {
		return this.removeAttachmentBtn;
	}
	
	public CheckBox getHideCompleted() {
		return this.hideCompleted;
	}
	
	public TextField getEmailIndex() {
		return emailIndex;
	}
	
	public Label getTotalEmails() {
		return this.totalEmailsLbl;
	}
	
	public Label getUnsentEmails() {
		return this.emailUnsentLbl;
	}
}
