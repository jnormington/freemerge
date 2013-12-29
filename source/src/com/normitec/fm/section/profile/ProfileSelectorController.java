package com.normitec.fm.section.profile;

import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.normitec.exception.NormitecException;
import com.normitec.fm.Constants;
import com.normitec.fm.FPLauncher;
import com.normitec.fm.section.RootView;
import com.normitec.fm.section.AppControlDefaults.Property;
import com.normitec.fm.section.email.EmailView;
import com.normitec.fm.section.message.MessageView;
import com.normitec.fm.section.message.MessageViewController;
import com.normitec.fm.section.navigator.NavigatorController;
import com.normitec.fm.section.settings.SettingsView;
import com.normitec.fm.section.settings.SettingsViewController;
import com.normitec.javafx.view.ModalDialog;
import com.normitec.listener.IPropertyListener;

public class ProfileSelectorController implements EventHandler<Event>, IPropertyListener {
	private static final Logger logger = Logger.getLogger(ProfileSelectorController.class);
	private ProfileSelector view;
	private ProfileSelectorModel model;
	private static ProfileSelectorController instance;
	
	public static ProfileSelectorController getInstance() {
		if(instance == null)
			instance = new ProfileSelectorController();
		
		return instance;
	}
	
	private ProfileSelectorController() {
		logger.log(Level.DEBUG, "Initialising " + getClass());
		this.view = new ProfileSelector();
		this.model = new ProfileSelectorModel();		
		this.model.addObjectChangedListener(this);
		
		//Init only control to listen for Events
		this.view.getSelectCreateBtn().addEventHandler(ActionEvent.ACTION, this);
		this.view.getProfile().addEventHandler(MouseEvent.MOUSE_CLICKED, this);
		this.view.getProfileName().addEventHandler(KeyEvent.KEY_TYPED, this);
		this.view.getDeleteBtn().addEventHandler(ActionEvent.ACTION, this);
	}
	
	public ProfileSelector getRegisteredView() {
		return this.view;
	}
	
	public ProfileSelectorModel getRegisteredModel() {
		return this.model;
	}
	
	public void showToUserAndResetModel() {
		//ReFire the updates...
		this.model.reFireUpdates();
		RootView.getInstance().getModalDialog().add(this.view).showAlert();
		this.getRegisteredModel().refreshAndReset();
		this.getRegisteredView().getProfileName().setText("");
		this.getRegisteredView().getProfile().getSelectionModel().clearSelection();
	}
		
	@Override
	public void handle(Event event) {
		logger.log(Level.DEBUG, "Recieved event from user interaction " + event.getSource());
		if (event.getSource().equals(this.view.getProfileName())) {
			this.view.getProfile().getSelectionModel().clearSelection();
			this.view.getSelectCreateBtn().setGraphic(this.view.getCreateImg());
		} else if (event.getSource().equals(this.view.getProfile()) && this.view.getProfile().getSelectionModel().getSelectedItem() != null) {
			this.view.getSelectCreateBtn().setGraphic(this.view.getSelectImg());
		}
		
		//Clear error text on next interaction
		if (this.view.getErrorText().getText().length() > 0) {
			this.view.getErrorText().setText("");
		}
		
		try {
			if (event.getSource().equals(this.view.getSelectCreateBtn())) {
				if (this.view.getProfileName().getText().length() == 0 && this.view.getProfile().getSelectionModel().getSelectedItem() == null)
					return;
				
				if (this.view.getProfileName().getText().length() > 0) {
					this.model.setProfile(this.view.getProfileName().getText(), false);
				} else if (this.view.getProfile().getSelectionModel().getSelectedItems().size() == 1) {
					this.model.setProfile(this.view.getProfile().getSelectionModel().getSelectedItem(),true);
				}
			}
			
			if (event.getSource().equals(this.view.getDeleteBtn()) && this.view.getProfile().getSelectionModel().getSelectedItem() != null) {
				this.view.setDisable(true);
				final ModalDialog modal = new ModalDialog();
				MessageViewController message = new MessageViewController(
					new MessageView("Are you sure you want to delete the selected profile? " + "\n\nProfile Name: " + this.view.getProfile().getSelectionModel().getSelectedItem()), modal);					
					//If the user presses Yes delete the row otherwise remove the overlay controlled in the controller.
					message.getRegisteredView().getYesBtn().addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							model.deleteFile(view.getProfile().getSelectionModel().getSelectedItem());
							modal.hideAlert();
							RootView.getInstance().getChildren().remove(modal);
							view.setDisable(false);
						}					
					});
					//Custom override from general no button handler.
					message.getRegisteredView().getNoBtn().addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							modal.hideAlert();
							RootView.getInstance().getChildren().remove(modal);
							view.setDisable(false);
						}					
					});
				modal.add(message.getRegisteredView());
				RootView.getInstance().getChildren().add(modal);
				modal.showAlert();				
			}
		} catch (NormitecException e) {
			e.printStackTrace();
			this.view.getErrorText().setText(e.getMessage());
		}
	}
	
	@Override
	public void propertyChanged(String property, Object propertyValue) {
		if (property.equals(Property.PROFILES_LIST.name()) || property.equals(Property.FILE_DELETED.name())) {
			this.view.getProfile().getItems().clear();
			this.view.getProfile().getItems().addAll((List<String>)propertyValue);
		} else if (property == Property.FILE_DUPLICATE.name()) {
			this.view.getErrorText().setText(propertyValue.toString());
		}
	}

	@Override
	public void propertyChanged(String property, Object propertyOldValue, Object propertyNewValue) {
	}

	@Override
	public void propertyChanged(String property, Object propertyValue, boolean valid) {
		//Hide view and display the next screen.
		if (property.equals(Property.LOAD_PROFILE.name()) && valid) {
			RootView.getInstance().getRootNodeInStack().setRight(NavigatorController.getInstance().getRegisteredView());
			RootView.getInstance().getModalDialog().hideAlert();
					
			SettingsViewController.getInstance().getRegisteredModel().setConfigFile(this.model.getSelectedProfile());
			
			
			if (this.model.isNewProfile()) {
				NavigatorController.getInstance().getRegisteredModel().requestNewView(SettingsView.class);
			} else {				
				//If the profile selected is empty display settings otherwise display email processor.
				NavigatorController.getInstance().getRegisteredModel().requestNewView(EmailView.class);
			}
			
			FPLauncher.stage.setTitle(Constants.APP_NAME + " - " + this.model.getSelectedProfile().getName());
		} else if (property.equals(Property.LOAD_PROFILE.name()) && !valid) {
			this.view.getErrorText().setText("Failed to load profile...");
		} else if (property.equals(Property.FILE_DELETED.name()) && !valid) {
			this.view.getErrorText().setText("Failed to delete profile...");
		} else if (property.equals(Property.FILE_DELETED.name()) && valid) {
			this.view.getErrorText().setText("Profile deleted...");
		}
	}

	@Override
	public void propertyChanged(String property, Object propertyOldValue, Object propertyNewValue, boolean valid) {
	}
}
