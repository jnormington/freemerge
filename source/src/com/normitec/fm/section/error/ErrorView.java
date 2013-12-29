package com.normitec.fm.section.error;

import com.normitec.fm.section.AppControlDefaults;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class ErrorView extends VBox {
	private TextField header;
	private TextArea solution;
	private Button OKBtn;

	public ErrorView(String header, String error) {
		super(15);
		// Setup display and constraints of pane
		this.setMaxSize(500, Region.USE_PREF_SIZE);
		this.setAlignment(Pos.CENTER_RIGHT);
		this.setId("AlertDialog");
		
		this.initView(header, error);
	}
	
	private void initView(String header, String error) {		
		this.header = new TextField(header);
		this.solution = new TextArea(error);
		this.OKBtn = new Button("OK");
		this.header.setEditable(false);
		this.solution.setEditable(false);
		this.solution.setWrapText(true);
		
		
		this.getChildren().addAll(this.header, this.solution, this.OKBtn);
		AppControlDefaults.setNodeHeight(AppControlDefaults.CTRL_HEIGHT, this.header, this.OKBtn);
		AppControlDefaults.setNodeHeight(200, this.solution);
	}
	
	public Button getOKBtn() {
		return OKBtn;
	}
	
	public TextField getHeader() {
		return this.header;
	}
}

