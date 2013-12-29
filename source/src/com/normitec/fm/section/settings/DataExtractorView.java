package com.normitec.fm.section.settings;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import com.normitec.fm.Constants;
import com.normitec.fm.section.AppControlDefaults;

public class DataExtractorView extends GridPane {

	private TextField userAlias;
	private TextField columnNumber;
	private CheckBox useLDAP;
	private ComboBox<String> ldapOption;
	private CheckBox emailRequired;
	private Label userAliasLbl;
	private Label columnNumberLbl;
	private Label useLDAPLbl;
	private Label ldapOptionLbl;
	private Label requiredLbl;
	private Button deleteBtn;
	private Button addBtn;
	private Button clearSelectionBtn;
	private TableView<DataExtractor> deTable;
	private Insets insets;
	private ImageView deleteImg;
	private ImageView saveImg;
	private Node clearImg;

	public DataExtractorView() {
		this.createDataExtractorTable();
		this.initView();
	}

	private void initView() {
		this.insets = new Insets(5, 0, 5, 5);
		// init controls
		// image
		this.deleteImg = new ImageView(Constants.DELETE_IMG);
		this.saveImg = new ImageView(Constants.SELECT_IMG);
		this.clearImg = new ImageView(Constants.CLEAR_IMG);
		// labels
		this.userAliasLbl = new Label("User Alias");
		this.columnNumberLbl = new Label("Column Number");
		this.useLDAPLbl = new Label("Use LDAP");
		this.ldapOptionLbl = new Label("LDAP Option");
		this.requiredLbl = new Label("Email from LDAP Required");
		// input
		this.userAlias = new TextField();
		this.columnNumber = new TextField();
		this.useLDAP = new CheckBox();
		this.ldapOption = new ComboBox<String>();
		this.emailRequired = new CheckBox();
		// button
		this.deleteBtn = new Button("",this.deleteImg);
		this.addBtn = new Button("",this.saveImg);
		this.clearSelectionBtn = new Button("",this.clearImg);
		this.deleteBtn.setId("imageOnlyButton");
		this.addBtn.setId("imageOnlyButton");
		this.clearSelectionBtn.setId("imageOnlyButton");
		//insets
		GridPane.setMargin(this.userAlias, this.insets);
		GridPane.setMargin(this.columnNumber, this.insets);		
		GridPane.setMargin(this.ldapOption, this.insets);		
		
		GridPane.setMargin(this.emailRequired, new Insets(0,0,0,5));
		GridPane.setMargin(this.useLDAP, new Insets(0,0,5,5));
		GridPane.setMargin(this.useLDAPLbl, new Insets(0,0,5,5));
		
		//GridPane contraints
		this.getColumnConstraints().add(new ColumnConstraints(110));
		this.getColumnConstraints().get(0).setHalignment(HPos.RIGHT);		
		this.getColumnConstraints().add(new ColumnConstraints(175));
		this.getColumnConstraints().get(1).setHalignment(HPos.LEFT);
		this.getColumnConstraints().add(new ColumnConstraints(200));
		this.getColumnConstraints().get(2).setHalignment(HPos.RIGHT);
		this.getColumnConstraints().add(new ColumnConstraints());
		this.getColumnConstraints().add(new ColumnConstraints());
		this.getColumnConstraints().add(new ColumnConstraints());
		this.getColumnConstraints().get(4).setHgrow(Priority.ALWAYS);
		this.getColumnConstraints().add(new ColumnConstraints());		
		this.getColumnConstraints().get(5).setHgrow(Priority.ALWAYS);
		this.getColumnConstraints().get(5).setMaxWidth(Double.MAX_VALUE);

		//Add controls
		this.add(this.userAliasLbl, 0, 0);
		this.add(this.columnNumberLbl, 0, 1);
		this.add(this.useLDAPLbl, 2, 2);
		this.add(this.ldapOptionLbl, 2, 0);
		this.add(this.requiredLbl, 2, 1);
		
		this.add(this.userAlias, 1, 0);
		this.add(this.columnNumber, 1, 1);
		this.add(this.useLDAP, 3, 2);
		this.add(this.ldapOption, 3, 0);
		this.add(this.emailRequired, 3, 1);
		
		//For add and delete button
		GridPane.setConstraints(this.addBtn, 5, 0, 1, 1, HPos.RIGHT,VPos.CENTER);
		GridPane.setConstraints(this.deleteBtn, 5, 1, 1, 1, HPos.RIGHT,VPos.CENTER);
		GridPane.setConstraints(this.clearSelectionBtn, 5, 2, 1, 1, HPos.RIGHT,VPos.CENTER);
		GridPane.setConstraints(this.deTable, 0, 3, 6, Integer.MAX_VALUE, HPos.LEFT, VPos.TOP, Priority.ALWAYS,Priority.ALWAYS);
		this.getChildren().addAll(this.deTable,this.addBtn, this.deleteBtn,this.clearSelectionBtn);
		AppControlDefaults.setNodeWidthHeight(32, 32, this.addBtn,this.deleteBtn, this.clearSelectionBtn);
		AppControlDefaults.setNodeWidthHeight(175,AppControlDefaults.CTRL_HEIGHT, this.userAlias,this.ldapOption);
		AppControlDefaults.setNodeWidthHeight(75, AppControlDefaults.CTRL_HEIGHT, this.columnNumber);
	}
		
	private void createDataExtractorTable() {
		// TableView
		this.deTable = new TableView<DataExtractor>();
		// setup columns
		TableColumn<DataExtractor, String> userAliasCol = new TableColumn<DataExtractor, String>();
		TableColumn<DataExtractor, String> columnNumberCol = new TableColumn<DataExtractor, String>();
		TableColumn<DataExtractor, String> ldapOptionCol = new TableColumn<DataExtractor, String>();
		TableColumn<DataExtractor, Boolean> useLDAPCol = new TableColumn<DataExtractor, Boolean>();
		TableColumn<DataExtractor, Boolean> emailRequiredCol = new TableColumn<DataExtractor, Boolean>();
		// Column constraints
		userAliasCol.setText("User Alias");
		columnNumberCol.setText("Column Number");
		ldapOptionCol.setText("LDAP Option");
		useLDAPCol.setText("Use LDAP");
		emailRequiredCol.setText("Email Required");

		userAliasCol.setCellValueFactory(new PropertyValueFactory<DataExtractor, String>("userAlias"));
		columnNumberCol.setCellValueFactory(new PropertyValueFactory<DataExtractor, String>("columnNumber"));
		ldapOptionCol.setCellValueFactory(new PropertyValueFactory<DataExtractor, String>("ldapOption"));
		useLDAPCol.setCellValueFactory(new PropertyValueFactory<DataExtractor, Boolean>("useLDAP"));
		emailRequiredCol.setCellValueFactory(new PropertyValueFactory<DataExtractor, Boolean>("emailRequired"));

		userAliasCol.setPrefWidth(150);
		columnNumberCol.setPrefWidth(150);
		ldapOptionCol.setPrefWidth(150);
		useLDAPCol.setPrefWidth(150);
		emailRequiredCol.setPrefWidth(150);
		
		this.deTable.getColumns().addAll(userAliasCol, columnNumberCol, ldapOptionCol, useLDAPCol, emailRequiredCol);
	}
	
	public TextField getUserAlias() {
		return userAlias;
	}
	
	public TextField getColumnNumber() {
		return columnNumber;
	}
	
	public CheckBox getUseLDAP() {
		return useLDAP;
	}
	
	public ComboBox<String> getLdapOption() {
		return ldapOption;
	}
	
	public CheckBox getEmailRequired() {
		return emailRequired;
	}

	public Button getDeleteBtn() {
		return deleteBtn;
	}

	public Button getAddBtn() {
		return addBtn;
	}
	
	public TableView<DataExtractor> getDeTable() {
		return deTable;
	}
	
	public Button getClearSelectionBtn() {
		return clearSelectionBtn;
	}
}
