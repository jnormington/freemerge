package com.normitec.fm.section.navigator;

import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.normitec.fm.section.AppControlDefaults.Property;
import com.normitec.fm.section.RootView;
import com.normitec.fm.section.email.EmailView;
import com.normitec.fm.section.profile.ProfileSelector;
import com.normitec.fm.section.profile.ProfileSelectorController;
import com.normitec.fm.section.settings.AboutHelpView;
import com.normitec.fm.section.settings.SettingsView;
import com.normitec.fm.section.settings.SettingsViewController;
import com.normitec.javafx.view.AbstractController;

public class NavigatorController extends AbstractController<Navigator, NavigatorModel> implements EventHandler<Event> {
	private static final Logger logger = Logger.getLogger(NavigatorController.class);
	private static NavigatorController instance;
	private HashMap<Button, Class> btnViewMap = new HashMap<Button, Class>();
	private HashMap<Class, Node> viewNavMap = new HashMap<Class, Node>();

	public NavigatorController() {
		super(new Navigator(), new NavigatorModel(),false);
		this.initialConfiguration();
	}
	
	public static NavigatorController getInstance() {
		if(instance == null)
			instance = new NavigatorController();
		
		return instance;
	}
	
	@Override
	protected void initialConfiguration() {
		logger.log(Level.DEBUG, "Initialising configuration for view model mapping.");
		//this.registeredModel.addObjectChangedListener(this);
		// Adding mapping for the events
		this.btnViewMap.put(this.registeredView.getGlobalOptions().getEmailViewBtn(), EmailView.class);
		this.btnViewMap.put(this.registeredView.getGlobalOptions().getPreferencesBtn(), SettingsView.class);
		this.btnViewMap.put(this.registeredView.getGlobalOptions().getHomeScreenBtn(), ProfileSelector.class);
		this.btnViewMap.put(this.registeredView.getGlobalOptions().getHelpBtn(), AboutHelpView.class);
		this.viewNavMap.put(EmailView.class, this.registeredView.getEmailOptions());
		this.viewNavMap.put(SettingsView.class, this.registeredView.getSettingsOptions());		
	}

	@Override
	public void propertyChanged(String property, Object propertyValue) {
		logger.log(Level.DEBUG, "Event to update view recieved...");
		if (property.equals(Property.ADD_REMOVE_VIEW.name()) && propertyValue instanceof ProfileSelector) {
			SettingsViewController.getInstance().getRegisteredModel().initDefaultValues();
			SettingsViewController.getInstance().getRegisteredModel().reFireUpdates();
			ProfileSelectorController.getInstance().showToUserAndResetModel();
			this.registeredModel.emailView.getRegisteredModel().initDefaultValues();
			this.registeredModel.emailView.getRegisteredModel().reFireUpdates();
			
		}
	}

	@Override
	public void propertyChanged(String property, Object propertyOldValue, Object propertyNewValue) {
	}

	@Override
	public void propertyChanged(String property, Object propertyOldValue, boolean valid) {
	}

	@Override
	public void propertyChanged(String property, Object propertyOldValue, Object propertyNewValue, boolean valid) {
		logger.log(Level.DEBUG, "Event to remove and update view recieved...");
		// Remove the old view
		if (propertyOldValue != null) {
			RootView.getInstance().getRootNodeInStack().setCenter(null);
			if (this.viewNavMap.containsKey(propertyOldValue.getClass()))
				this.registeredView.getRootPane().getChildren().remove(this.viewNavMap.get(propertyOldValue.getClass()));
		}

		// Add the new view
		if (propertyNewValue != null) {
			RootView.getInstance().getRootNodeInStack().setCenter((Node)propertyNewValue);
			if (this.viewNavMap.containsKey(propertyNewValue.getClass()))
				this.registeredView.getRootPane().getChildren().add(0, this.viewNavMap.get(propertyNewValue.getClass()));
		}
	}

	@Override
	public void handle(Event event) {
		this.registeredModel.requestNewView(this.btnViewMap.get(event.getSource()));
	}

	@Override
	protected void registerViewListeners(Navigator view) {
		// Global Options
		view.getGlobalOptions().getPreferencesBtn().addEventHandler(ActionEvent.ACTION, this);
		view.getGlobalOptions().getEmailViewBtn().addEventHandler(ActionEvent.ACTION, this);
		view.getGlobalOptions().getHelpBtn().addEventHandler(ActionEvent.ACTION, this);
		view.getGlobalOptions().getHomeScreenBtn().addEventHandler(ActionEvent.ACTION, this);
		
	}
}
