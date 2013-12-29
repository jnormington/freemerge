package com.normitec.fm.section.message;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;

import com.normitec.javafx.view.ModalDialog;


public class MessageViewController implements EventHandler<Event> {
	private ModalDialog dialog;
	private MessageView messageView;
	
	
	public MessageViewController(MessageView view, ModalDialog md) {
		this.messageView = view;
		this.dialog = md;
	
		this.messageView.getNoBtn().addEventHandler(ActionEvent.ACTION, this);
	}
	
	@Override
	public void handle(Event event) {
		if (event.getSource().equals(this.messageView.getNoBtn())) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					dialog.hideAlert();				
				}
			});				
		}
	}
	
	public MessageView getRegisteredView() {
		return this.messageView;
	}
}
