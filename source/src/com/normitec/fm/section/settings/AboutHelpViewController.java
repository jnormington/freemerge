package com.normitec.fm.section.settings;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import com.normitec.fm.section.RootView;
import com.normitec.javafx.view.ModalDialog;

public class AboutHelpViewController implements EventHandler<Event> {
	private static final Logger logger = Logger.getLogger(AboutHelpViewController.class);
	private RootView viewHandler;
	private AboutHelpView view;
	private ModalDialog dialog;
	
	public AboutHelpViewController(AboutHelpView view, RootView viewHandler) {
		logger.log(Level.INFO, "Initializing AboutHelpViewController...");
		this.dialog = new ModalDialog();
		
		this.view = view;
		this.viewHandler = viewHandler;
				
		dialog.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
	}
	
	@Override
	public void handle(Event event) {
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				viewHandler.getChildren().remove(dialog);
				dialog.hideAlert();				
			}
		});				
	}
	
	public AboutHelpView getRegisteredView() {
		if (!this.viewHandler.getChildren().contains(this.dialog))
			viewHandler.getChildren().add(this.dialog);
		
		this.dialog.add(view);
		this.dialog.showAlert();
		
		return this.view;
	}
}
