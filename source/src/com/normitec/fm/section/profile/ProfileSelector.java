package com.normitec.fm.section.profile;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import com.normitec.fm.Constants;
import com.normitec.fm.section.AppControlDefaults;

public class ProfileSelector extends VBox {
	private ListView<String> profile;
	private TextField profileName;
	private Button selectCreateBtn;
	private Button deleteBtn;
	private GridPane footer;
	private Text errorText;
	private Insets insets;
	private ImageView deleteImg;
	private ImageView selectImg;
	private ImageView createImg;

	
	public ProfileSelector() {
		super(10);
		// Setup display and constraints of pane
		this.setMaxSize(400, Region.USE_PREF_SIZE);
		this.setAlignment(Pos.CENTER_RIGHT);
		this.setId("AlertDialog");
		
		this.initView();
	}
	
	public ListView<String> getProfile() {
		return this.profile;
	}
	
	public TextField getProfileName() {
		return this.profileName;
	}

	
	public Button getSelectCreateBtn() {
		return this.selectCreateBtn;
	}
	
	public Text getErrorText() {
		return this.errorText;
	}
	
	public Button getDeleteBtn() {
		return this.deleteBtn;
	}
	
	public ImageView getSelectImg() {
		return this.selectImg;
	}
	
	public ImageView getCreateImg() {
		return this.createImg;
	}
	
	private void initView() {
		this.deleteImg = new ImageView(Constants.DELETE_IMG);
		this.selectImg = new ImageView(Constants.SELECT_IMG);
		this.createImg = new ImageView(Constants.ADD_IMG);
		AppControlDefaults.setImageViewSize(30, 30, this.createImg);
		this.insets = new Insets(0,0,0,5);
		this.profile = new ListView<String>();
		this.profileName = new TextField();
		this.profileName.setPromptText("Create new profile here");
		this.selectCreateBtn = new Button("", this.selectImg);
		this.deleteBtn = new Button("",this.deleteImg);
		this.deleteBtn.setId("imageOnlyButton");
		this.selectCreateBtn.setId("imageOnlyButton");
		this.errorText = new Text();
		this.errorText.setFill(Color.RED);
		
		//Footer
		this.footer = new GridPane();
		this.footer.getColumnConstraints().add(new ColumnConstraints(250));
		this.footer.getColumnConstraints().add(new ColumnConstraints(120));
		this.footer.getColumnConstraints().add(new ColumnConstraints(37));
		this.footer.setAlignment(Pos.CENTER_RIGHT);
		this.footer.add(this.errorText, 0, 0);
		this.footer.add(this.selectCreateBtn, 1, 0);
		this.footer.add(this.deleteBtn, 2, 0);
		GridPane.setMargin(this.deleteBtn, this.insets);
		//GridPane.setHalignment(this.errorText, HPos.LEFT);
		GridPane.setHalignment(this.selectCreateBtn, HPos.RIGHT);
		GridPane.setHgrow(this.errorText, Priority.NEVER);
		
		//Add to this
		AppControlDefaults.setNodeHeight(150, this.profile);
		AppControlDefaults.setNodeHeight(AppControlDefaults.CTRL_HEIGHT,this.profileName);
		AppControlDefaults.setNodeWidthHeight(32, 32, this.selectCreateBtn, this.deleteBtn);
		this.getChildren().addAll(this.profile,this.profileName, this.footer);
	}
}
