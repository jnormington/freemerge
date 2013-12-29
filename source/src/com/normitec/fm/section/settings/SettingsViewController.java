package com.normitec.fm.section.settings;

import java.io.File;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONException;

import com.normitec.exception.NormitecException;
import com.normitec.exception.ThrowableToString;
import com.normitec.fm.Constants;
import com.normitec.fm.section.AppControlDefaults.Property;
import com.normitec.fm.section.RootView;
import com.normitec.fm.section.error.ErrorView;
import com.normitec.fm.section.error.ErrorViewController;
import com.normitec.fm.section.message.MessageView;
import com.normitec.fm.section.message.MessageViewController;
import com.normitec.fm.section.navigator.NavigatorController;
import com.normitec.fm.section.settings.SettingsModel.Key;
import com.normitec.javafx.view.AbstractController;
import com.normitec.javafx.view.status.CustomLoader;
import com.normitec.javafx.view.status.StatusFXModel;

public class SettingsViewController extends AbstractController<SettingsView, SettingsModel> implements EventHandler<Event> {
	
	private static final Logger logger = Logger.getLogger(SettingsViewController.class);
	private static SettingsViewController instance;
	private static NavigatorController navigator = NavigatorController.getInstance();
	
	private SettingsViewController() {
		super(new SettingsView(), new SettingsModel());
	}
	
	public static SettingsViewController getInstance() {
		if (instance == null)
			instance = new SettingsViewController();
		
		return instance;
	}
	
	@Override
	protected void registerViewListeners(SettingsView view) {
		logger.log(Level.DEBUG, "Registering listeners for controls");
		NavigatorController.getInstance().getRegisteredView().getSettingsOptions().getSaveBtn().addEventHandler(ActionEvent.ACTION, this);
		//dataextractor add and remove table
		view.getExtratorView().getAddBtn().addEventHandler(ActionEvent.ACTION, this);
		view.getExtratorView().getDeleteBtn().addEventHandler(ActionEvent.ACTION, this);
		view.getExtratorView().getClearSelectionBtn().addEventHandler(ActionEvent.ACTION, this);
		view.getExtratorView().getDeTable().addEventHandler(MouseEvent.MOUSE_CLICKED, this);
		view.getEmailView().getAddAttachmentBtn().addEventHandler(ActionEvent.ACTION, this);
		view.getEmailView().getRemoveAttachmentBtn().addEventHandler(ActionEvent.ACTION, this);
	}
	
	@Override
	public void handle(Event event) {	
		logger.log(Level.DEBUG, "Recieved event from user interaction " + event.getSource());
		if (event.getSource().equals(navigator.getRegisteredView().getSettingsOptions().getSaveBtn())) {
			//misc options - dirty cheap way
			this.registeredModel.setLdapURL(this.registeredView.getMiscView().getLdapURL().getText());
			this.registeredModel.setSmtpHost(this.registeredView.getMiscView().getSmtpServer().getText());
			this.registeredModel.setSmtpPort(this.registeredView.getMiscView().getSmtpPort().getText());
			this.registeredModel.setDateFormat(this.registeredView.getMiscView().getDateFormat().getText());
			//get all email config - dirty cheap way.			
			this.registeredModel.getEmail().setFrom(this.registeredView.getEmailView().getFromField().getText());
			this.registeredModel.getEmail().setTo(this.registeredView.getEmailView().getToField().getText());
			this.registeredModel.getEmail().setCc(this.registeredView.getEmailView().getCcField().getText());
			this.registeredModel.getEmail().setBcc(this.registeredView.getEmailView().getBccField().getText());
			this.registeredModel.getEmail().setSubject(this.registeredView.getEmailView().getSubjectField().getText());
			this.registeredModel.getEmail().setBody(this.registeredView.getEmailView().getBodyField().getText());
			//Now save
			PersistenceProcessor bgp = new PersistenceProcessor();
			RootView.getInstance().getModalDialog().add(bgp.status);
			RootView.getInstance().getModalDialog().showAlert();
			bgp.thread.start();
			
		} else if (event.getSource().equals(this.registeredView.getExtratorView().getDeleteBtn())) {
			if (this.getRegisteredView().getExtratorView().getDeTable().getSelectionModel().getSelectedItem() != null) {
				DataExtractor selected = this.getRegisteredView().getExtratorView().getDeTable().getSelectionModel().getSelectedItem();
				MessageViewController message = new MessageViewController(
					new MessageView("Are you sure you want to delete the selected row? " + 
							"\n\nUser Alias: " + selected.getUserAlias() +
							"\nColumn No: " + selected.getColumnNumber() +
							"\nUse LDAP: " + selected.isUseLDAP() +
							"\nLDAP Option: " + selected.getLdapOption() + 
							"\nEmail Required: " + selected.isEmailRequired()), RootView.getInstance().getModalDialog());
				//If the user presses Yes delete the row otherwise remove the overlay controlled in the controller.
				message.getRegisteredView().getYesBtn().addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						registeredModel.removeDataExtrator(getRegisteredView().getExtratorView().getDeTable().getSelectionModel().getSelectedItem());
						RootView.getInstance().getModalDialog().hideAlert();
					}					
				});
				RootView.getInstance().getModalDialog().add(message.getRegisteredView());
				RootView.getInstance().getModalDialog().showAlert();
			}			
		} else if (event.getSource().equals(this.registeredView.getExtratorView().getAddBtn())) {
				String problems = DataExtractorValidator.isDataExtractorEntryValid(this.registeredModel.getDataExtractors(), 
					this.getRegisteredView().getExtratorView().getUserAlias().getText(), 
					this.getRegisteredView().getExtratorView().getColumnNumber().getText(),
					this.getRegisteredView().getExtratorView().getLdapOption().getSelectionModel().getSelectedItem(),
					this.getRegisteredView().getExtratorView().getUseLDAP().isSelected(),
					this.getRegisteredView().getExtratorView().getEmailRequired().isSelected(), this.registeredModel.getSelectedRow() == null ? false : true);
					
				if(problems.length() > 0) {
					ErrorViewController error = new ErrorViewController(new ErrorView("Some problems exists with the new or updated data extractor entry.", 
							"Please ensure the following issues are resolved before attempting to create a new or update an existing data extractor...\n\n" + problems), 
							RootView.getInstance().getModalDialog());
					RootView.getInstance().getModalDialog().add(error.getRegisteredView());
					RootView.getInstance().getModalDialog().showAlert();
				} else {					
					this.registeredModel.addToDataExtrators(new DataExtractor(this.getRegisteredView().getExtratorView().getUserAlias().getText(),
					this.getRegisteredView().getExtratorView().getColumnNumber().getText(),
					this.getRegisteredView().getExtratorView().getUseLDAP().isSelected(),
					this.getRegisteredView().getExtratorView().getEmailRequired().isSelected(),						
					this.getRegisteredView().getExtratorView().getLdapOption().getSelectionModel().getSelectedItem()));
					
					//Dirty workaround until complete revision of API for updating table on changes
					if (this.registeredModel.getSelectedRow() != null) {
						this.registeredModel.removeDataExtrator(this.registeredModel.getSelectedRow());
						this.registeredModel.setSelectedRow(null);
					}
				}
		} else if (event.getSource().equals(this.registeredView.getExtratorView().getDeTable())) {
			if (this.registeredView.getExtratorView().getDeTable().getSelectionModel().getSelectedItem() != null) {
				this.registeredModel.setSelectedRow(this.registeredView.getExtratorView().getDeTable().getSelectionModel().getSelectedItem());
			}
		} else if (event.getSource().equals(this.registeredView.getExtratorView().getClearSelectionBtn())) {
			this.registeredModel.setSelectedRow(null);
			this.registeredView.getExtratorView().getDeTable().getSelectionModel().clearSelection();
		} else if (event.getSource().equals(this.registeredView.getEmailView().getAddAttachmentBtn())) {
			FileChooser fc = new FileChooser();
			fc.setTitle("Select file to as attachment");
			File selected = fc.showOpenDialog(this.registeredView.getScene().getWindow());
			if (selected != null) {
				this.registeredModel.getEmail().getAttachments().add((selected));
				this.registeredModel.firePropertyChanged("EmailAttachments", null);
			}			
		} else if (event.getSource().equals(this.registeredView.getEmailView().getRemoveAttachmentBtn())) {
			this.registeredModel.getEmail().getAttachments().remove(this.registeredView.getEmailView().getAttachments().getSelectionModel().getSelectedItem());
			this.registeredModel.firePropertyChanged("EmailAttachments", null);
		}
			
	}

	@Override
	protected void initialConfiguration() {
		logger.log(Level.DEBUG, "Initialising configuration for view model mapping.");
		//miscOptions View
		this.mapActionModel(this.registeredView.getMiscView().getSmtpServer(), Key.SmtpHost.name());
		this.mapActionModel(this.registeredView.getMiscView().getSmtpPort(), Key.SmtpPort.name());
		this.mapActionModel(this.registeredView.getMiscView().getLdapURL(), Key.LdapURL.name());
		this.mapActionModel(this.registeredView.getMiscView().getDateFormat(), Key.DateFormat.name());
		//data extractor table
		this.mapActionModel(this.registeredView.getExtratorView().getDeTable(), Key.DataExtractor.name());
		
		this.registeredView.getExtratorView().getLdapOption().getItems().addAll(Constants.LDAP_OPTIONS);
		this.registeredView.getExtratorView().getLdapOption().getSelectionModel().select("NONE");
	}
	
	@Override
	public void propertyChanged(String property, final Object propertyValue) {
		super.propertyChanged(property,propertyValue);
		
		if (propertyValue != null) {
			if (this.getModelAction().get(property) instanceof TextField)
				((TextField) this.getModelAction().get(property)).setText(propertyValue.toString());
			else if (this.getModelAction().get(property) instanceof Label)
				((Label) this.getModelAction().get(property)).setText(propertyValue.toString());
			else if (this.getModelAction().get(property) instanceof CheckBox)
				((CheckBox) this.getModelAction().get(property)).setSelected((Boolean) (propertyValue));
			else if (this.getModelAction().get(property) instanceof TextArea)
				((TextArea) this.getModelAction().get(property)).setText(propertyValue.toString());
			else if (this.getModelAction().get(property) instanceof ListView) {
				logger.log(Level.DEBUG, "Updating list....");
				((ListView) this.getModelAction().get(property)).getItems().clear();
				((ListView) this.getModelAction().get(property)).getItems().addAll((List)propertyValue);
			} else if (this.getModelAction().get(property) instanceof TableView) {
				logger.log(Level.DEBUG, "Updating table....");
				((TableView) this.getModelAction().get(property)).getItems().clear();
				((TableView) this.getModelAction().get(property)).getItems().setAll((List)propertyValue);
			}
		}
		
		//Reset all the options when DataExtractor is fired - this means there is an update from user interaction.
		if (property.equals(Key.DataExtractor.name()) || property.equals("DataExtractorRowSelected") && propertyValue == null) {
			this.registeredView.getExtratorView().getLdapOption().getSelectionModel().select("NONE");
			this.registeredView.getExtratorView().getUserAlias().setDisable(false);
			this.registeredView.getExtratorView().getUserAlias().setText("%%");
			this.registeredView.getExtratorView().getColumnNumber().setText("");
			this.registeredView.getExtratorView().getUseLDAP().setSelected(false);
			this.registeredView.getExtratorView().getEmailRequired().setSelected(false);
		} else if (property.equals("DataExtractorRowSelected") && propertyValue != null) {
			//Reset all the options when DataExtractor is fired - this means there is an update from user interaction.
			DataExtractor de = ((DataExtractor)propertyValue);
			this.registeredView.getExtratorView().getLdapOption().getSelectionModel().select(de.getLdapOption());
			this.registeredView.getExtratorView().getUserAlias().setDisable(true);
			this.registeredView.getExtratorView().getUserAlias().setText(de.getUserAlias());
			this.registeredView.getExtratorView().getColumnNumber().setText(de.getColumnNumber().toString().replace("[", "").replace("]", "").replace(" ",""));
			this.registeredView.getExtratorView().getUseLDAP().setSelected(de.isUseLDAP());
			this.registeredView.getExtratorView().getEmailRequired().setSelected(de.isEmailRequired());
		} else if (property.equals("EmailConfig")) {
			this.registeredView.getEmailView().getFromField().setText(this.registeredModel.getEmail().getFrom());
			this.registeredView.getEmailView().getToField().setText(this.registeredModel.getEmail().getTo());
			this.registeredView.getEmailView().getCcField().setText(this.registeredModel.getEmail().getCc());
			this.registeredView.getEmailView().getBccField().setText(this.registeredModel.getEmail().getBcc());
			this.registeredView.getEmailView().getSubjectField().setText(this.registeredModel.getEmail().getSubject());
			this.registeredView.getEmailView().getBodyField().setText(this.registeredModel.getEmail().getBody());
		} else if (property.equals("EmailAttachments")) {
			this.registeredView.getEmailView().getAttachments().getItems().clear();
			this.registeredView.getEmailView().getAttachments().getItems().addAll(this.registeredModel.getEmail().getAttachments());
		}
	}
	
	@Override
	public void propertyChanged(String property, Object propertyValue, boolean valid) {
		super.propertyChanged(property,propertyValue);
		if (property.equals(Property.LOAD_PROFILE.name()) && !valid) {
			//Display error when loading a profile is invalid possible manual manipulation?
			ErrorViewController error = new ErrorViewController(new ErrorView("Failed to load all the profile...", 
					propertyValue.toString()), RootView.getInstance().getModalDialog());
			RootView.getInstance().getModalDialog().add(error.getRegisteredView());
			RootView.getInstance().getModalDialog().showAlert();
		}
	}
	
	final class PersistenceProcessor implements Runnable {
		CustomLoader status;
		Thread thread;
		
		public PersistenceProcessor() {			
			this.status = new CustomLoader();
			this.thread = new Thread(this);			
		}
		
		@Override
		public void run() {
			logger.log(Level.DEBUG, "Saving file on new thread.");
			final StatusFXModel statusModel = new StatusFXModel(this.thread);
			this.status.addStatusModel(statusModel);

			/**
			 * Let the user see that the profile is saving and saved...
			 */
			try {
				statusModel.setProgressText("Saving File");
				Thread.sleep(1000);
				registeredModel.writeSettingsToFile();
				statusModel.setProgressText("Saved File Sucessfully...");
				Thread.sleep(1000);
				RootView.getInstance().getModalDialog().hideAlert();
				logger.log(Level.INFO, "File saved " + registeredModel.getConfigFile());
			}
			catch (NormitecException | JSONException | InterruptedException | NullPointerException e) {
				logger.log(Level.INFO, "File saving failed " + registeredModel.getConfigFile());
				logger.log(Level.INFO, ThrowableToString.exceptionToString(e));
				e.printStackTrace();
				RootView.getInstance().getModalDialog().hideAlert();
				ErrorViewController error = new ErrorViewController(new ErrorView("Failed to save profile", e.getMessage()), RootView.getInstance().getModalDialog());
				RootView.getInstance().getModalDialog().add(error.getRegisteredView());
				RootView.getInstance().getModalDialog().showAlert();
			}

		}
	}
}
