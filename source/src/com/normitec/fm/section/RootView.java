package com.normitec.fm.section;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.normitec.javafx.view.ModalDialog;

public class RootView extends StackPane {

	private static Logger logger = Logger.getLogger(RootView.class);
	private static RootView instance;
	private BorderPane rootPaneInStack;
	private ModalDialog modalDialog;

	private RootView() {
		logger.log(Level.DEBUG, "Instantiating instance of " + getClass());
		this.rootPaneInStack = new BorderPane();
		this.getChildren().add(this.rootPaneInStack);
	}

	public static RootView getInstance() {
		if (instance == null)
			instance = new RootView();

		return instance;
	}

	public BorderPane getRootNodeInStack() {
		return this.rootPaneInStack;
	}
	
	public ModalDialog getModalDialog() {
		if (this.modalDialog == null) {
			this.modalDialog = new ModalDialog();
			this.getChildren().add(this.modalDialog);
		}
		return this.modalDialog;
	}
}
