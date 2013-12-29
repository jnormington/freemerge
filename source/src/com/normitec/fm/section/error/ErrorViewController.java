package com.normitec.fm.section.error;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import com.normitec.javafx.view.ModalDialog;

public class ErrorViewController implements EventHandler<Event> {
	private ModalDialog dialog;
	private ErrorView errorView;
	
	
	public ErrorViewController(ErrorView view, ModalDialog md) {
		this.errorView = view;
		this.dialog = md;
	
		this.errorView.getOKBtn().addEventHandler(ActionEvent.ACTION, this);
	}
	
	@Override
	public void handle(Event event) {
		
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				dialog.hideAlert();				
			}
		});				
	}
	
	public ErrorView getRegisteredView() {
		return this.errorView;
	}
}
