package com.normitec.fm.section.email;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.normitec.exception.NormitecException;
import com.normitec.fm.section.RootView;
import com.normitec.fm.section.AppControlDefaults.Property;
import com.normitec.fm.section.error.ErrorView;
import com.normitec.fm.section.error.ErrorViewController;
import com.normitec.fm.section.message.MessageView;
import com.normitec.fm.section.message.MessageViewController;
import com.normitec.fm.section.navigator.NavigatorController;
import com.normitec.javafx.view.AbstractController;
import com.normitec.javafx.view.status.CustomLoader;
import com.normitec.javafx.view.status.StatusFXModel;
import com.normitec.listener.IPropertyListener;

public class EmailViewController extends AbstractController<EmailView,EmailViewModel> implements EventHandler<Event> {
	private NavigatorController navigator = NavigatorController.getInstance();
	private static final Logger logger = Logger.getLogger(EmailViewController.class);
	
	public EmailViewController() {
		super(new EmailView(),new EmailViewModel(),false);
		this.initialConfiguration();
		//Fire null to disable to controls
		this.registeredModel.firePropertyChanged(Property.DISPLAY_EMAIL.name(), null);
	}
	
	@Override
	public void handle(Event event) {		
		logger.log(Level.DEBUG, "Recieved event from user interaction " + event.getSource());
		//User selects file from here.
		if (event.getSource().equals(this.navigator.getRegisteredView().getEmailOptions().getProcessFileBtn())) {
			FileChooser fc = new FileChooser();
			fc.setTitle("Select Excel file to process");
			fc.getExtensionFilters().add(new ExtensionFilter("Excel Files *.xls,*.xlsx", "*.xls","*.xlsx"));
			this.registeredModel.setFileToProcess(fc.showOpenDialog(this.registeredView.getScene().getWindow()));
			//New thread here for processing file not on the Application thread.
			if (this.registeredModel.getFileToProcess() != null) {
				this.registeredModel.setEmails(null);
				this.navigator.getRegisteredView().setDisable(true);
				BackgroundProcessor bp = new BackgroundProcessor(true);
				RootView.getInstance().getModalDialog().add(bp.status);
				RootView.getInstance().getModalDialog().showAlert();
				bp.thread.start();
			}
			
		} else if (event.getSource().equals(this.registeredView.getHideCompleted())) {
			this.notifyRegisteredModels(this.getActionModel().get(event.getSource()), this.registeredView.getHideCompleted().isSelected());
		} else if (event.getSource().equals(this.registeredView.getNextBtn())) {
			this.registeredModel.getNextElement();
		} else if (event.getSource().equals(this.registeredView.getPrevBtn())) {
			this.registeredModel.getPreviousElement();
		} else if (event.getEventType().equals(KeyEvent.KEY_RELEASED) && this.registeredModel.getCurrentEmail() != null) {
			this.registeredModel.getCurrentEmail().setFrom(this.registeredView.getFromField().getText());
			this.registeredModel.getCurrentEmail().setTo(this.registeredView.getToField().getText());
			this.registeredModel.getCurrentEmail().setCc(this.registeredView.getCcField().getText());
			this.registeredModel.getCurrentEmail().setBcc(this.registeredView.getBccField().getText());
			this.registeredModel.getCurrentEmail().setSubject(this.registeredView.getSubjectField().getText());
			this.registeredModel.getCurrentEmail().setBody(this.registeredView.getBodyField().getText());
		} else if (event.getSource().equals(this.registeredView.getEmailIndex()) && ((KeyEvent)event).getCode() == KeyCode.ENTER) {
			this.registeredModel.setCurrentIndex(Integer.parseInt(this.registeredView.getEmailIndex().getText()));
		} else if (event.getSource().equals(this.navigator.getRegisteredView().getEmailOptions().getSendEmailBtn())) {
			if (this.registeredModel.getCurrentEmail() != null) {
			//New thread here for processing the emails.
			MessageViewController message = new MessageViewController(new MessageView("Are you sure you want to send this email?\n\n" + 
			"Current Status: " + this.registeredModel.getCurrentEmail().getEmailStatusAsStr() + "\n\n" +
			"Are all emails from LDAP valid: " + this.registeredModel.getCurrentEmail().isAllValid()), RootView.getInstance().getModalDialog());
				//If the user presses Yes delete the row otherwise remove the overlay controlled in the controller.
				message.getRegisteredView().getYesBtn().addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						RootView.getInstance().getModalDialog().hideAlert();
						navigator.getRegisteredView().setDisable(true);
						BackgroundProcessor bp = new BackgroundProcessor(false,false);
						RootView.getInstance().getModalDialog().add(bp.status);
						RootView.getInstance().getModalDialog().showAlert();
						bp.thread.start();						
					}					
				});
				RootView.getInstance().getModalDialog().add(message.getRegisteredView());
				RootView.getInstance().getModalDialog().showAlert();
			}
		} else if (event.getSource().equals(this.navigator.getRegisteredView().getEmailOptions().getSendAllEmailsBtn())) {
			if (this.registeredModel.getCurrentEmail() != null && this.registeredModel.getEmails().size() > 0) {
				//New thread here for processing the emails.
				MessageViewController message = new MessageViewController(new MessageView("Are you sure you want to process all emails?\n\n" +
						"Unsent email count: " + this.registeredModel.getUnSentEmailCount() + " will be processed."), RootView.getInstance().getModalDialog());
					//If the user presses Yes delete the row otherwise remove the overlay controlled in the controller.
					message.getRegisteredView().getYesBtn().addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							//New thread here for processing the emails in bulk.
							navigator.getRegisteredView().setDisable(true);
							BackgroundProcessor bp = new BackgroundProcessor(false,true);
							RootView.getInstance().getModalDialog().add(bp.status);
							RootView.getInstance().getModalDialog().showAlert();
							bp.thread.start();
						}					
					});
				RootView.getInstance().getModalDialog().add(message.getRegisteredView());
				RootView.getInstance().getModalDialog().showAlert();
			}			
		}
	}

	@Override
	public void propertyChanged(final String property, final Object propertyValue) {
		super.propertyChanged(property, propertyValue);
		
		//Ensure to always run on the Application thread.
		Platform.runLater(new Runnable() {
			@Override
			public void run()
			{
				Email em = null;
				if (property.equals(Property.DISPLAY_EMAIL.name())) {
					if (propertyValue == null) {
						 em = new Email();
						registeredView.getEmailIndex().setText(" / 0");
						registeredView.getEmailIndex().setText("0");
						registeredView.setAllCompDisabled(true);
						registeredView.getUnsentEmails().setText("Unsent Emails: 0 / 0");
					} else {
						registeredView.setAllCompDisabled(false);
						//Email number displayed
						registeredView.getTotalEmails().setText(" / " + Integer.toString(registeredModel.getEmails().size()));						
						registeredView.getEmailIndex().setText(Integer.toString(registeredModel.getCurrentIndex()+1));
						registeredView.getUnsentEmails().setText("Unsent Emails: " + registeredModel.getUnSentEmailCount() + " / " + registeredModel.getEmails().size());
					}
					
					if (propertyValue != null && propertyValue.getClass() == Email.class) {
						em = ((Email)propertyValue);
					}
						
					registeredView.getFromField().setText(em.getFrom());
					registeredView.getToField().setText(em.getTo());
					registeredView.getCcField().setText(em.getCc());
					registeredView.getBccField().setText(em.getBcc());
					registeredView.getSubjectField().setText(em.getSubject());
					registeredView.getBodyField().setText(em.getBody());
					registeredView.getAttachments().getItems().clear();
					registeredView.getAttachments().getItems().addAll(em.getAttachments());
					registeredView.getEmailStatus().setText(em.getEmailStatusAndString());
					registeredView.getEmailValidations().getItems().clear();
					registeredView.getEmailValidations().getItems().setAll(em.getEmailValid());
					
					
					if (em.getEmailStatus() == EmailStatus.EMAIL_SENDING_FAILED || em.getEmailStatus() == EmailStatus.EMAIL_NOT_SENT && em.getEmailStatusStr().length() > 0) {
						registeredView.getEmailStatus().setStyle("-fx-background-color: BLACK,firebrick;-fx-text-fill: white");
					} else if (em.getEmailStatus() == EmailStatus.EMAIL_SENT) {
						registeredView.getEmailStatus().setStyle("-fx-background-color: lightgreen;-fx-text-fill: black");
					} else {
						registeredView.getEmailStatus().setStyle(null);
					}
				}
			}
		});
	}
	
	@Override
	public void propertyChanged(String property, Object propertyValue, boolean valid) {
		super.propertyChanged(property, propertyValue, valid);
		//Display error message.
		//TODO: Review if it conflicts with other running thread
		if (property.equals(Property.PROCESSING_EXCEL.name()) && !valid) {
			ErrorViewController error = new ErrorViewController(new ErrorView("Some issue was raised while processing the file.", 
					"Please ensure the following issues are resolved before attempting to process the file again...\n\n" + propertyValue), 
					RootView.getInstance().getModalDialog());
			RootView.getInstance().getModalDialog().add(error.getRegisteredView());
			RootView.getInstance().getModalDialog().showAlert();
		}
	}
	
	@Override
	protected void registerViewListeners(EmailView view) {
		this.mapActionModel(view.getHideCompleted(), "HideCompleted");
	}

	@Override
	protected void initialConfiguration() {
		logger.log(Level.DEBUG, "Initializing configuration...");	
		this.navigator.getRegisteredView().getEmailOptions().getProcessFileBtn().addEventHandler(ActionEvent.ACTION, this);
		this.registeredView.getHideCompleted().addEventHandler(ActionEvent.ACTION,this);
		this.registeredView.getFromField().addEventHandler(KeyEvent.KEY_RELEASED, this);
		this.registeredView.getToField().addEventHandler(KeyEvent.KEY_RELEASED, this);
		this.registeredView.getCcField().addEventHandler(KeyEvent.KEY_RELEASED, this);
		this.registeredView.getBccField().addEventHandler(KeyEvent.KEY_RELEASED, this);
		this.registeredView.getSubjectField().addEventHandler(KeyEvent.KEY_RELEASED, this);
		this.registeredView.getBodyField().addEventHandler(KeyEvent.KEY_RELEASED, this);
		this.registeredView.getNextBtn().addEventHandler(ActionEvent.ACTION, this);
		this.registeredView.getPrevBtn().addEventHandler(ActionEvent.ACTION, this);
		this.registeredView.getEmailIndex().addEventHandler(KeyEvent.KEY_PRESSED, this);		
		
		this.navigator.getRegisteredView().getEmailOptions().getSendEmailBtn().addEventHandler(ActionEvent.ACTION, this);
		this.navigator.getRegisteredView().getEmailOptions().getSendAllEmailsBtn().addEventHandler(ActionEvent.ACTION, this);
	}
	
	final class BackgroundProcessor implements Runnable {
		CustomLoader status;
		Thread thread;
		boolean processFile;
		boolean sendAllEmails;
		boolean error;
		
		public BackgroundProcessor(boolean processFile) {			
			this(processFile,false);	
		}
		
		public BackgroundProcessor(boolean processFile, boolean sendAllEmails) {			
			this.status = new CustomLoader();
			this.thread = new Thread(this);	
			this.processFile = processFile;
			this.sendAllEmails = sendAllEmails;
			this.thread.setDaemon(true);
		}
		
		@Override
		public void run() {
			final StatusFXModel statusModel = new StatusFXModel(this.thread);
			this.status.addStatusModel(statusModel);

			/**
			 * Let the user see that the emails are being processed
			 */
			if (this.processFile) {
				logger.log(Level.INFO, "Reading excel file from user...");
				statusModel.setProgressText("Processing File...");
				
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e1) {
				}
				
				registeredModel.addObjectChangedListener(new IPropertyListener() {
					@Override
					public void propertyChanged(String property, Object propertyValue) {						
					}
					@Override
					public void propertyChanged(String property, Object propertyOldValue, Object propertyNewValue) {						
					}
					@Override
					public void propertyChanged(String property, Object propertyValue, boolean valid) {
						if (property.equals(Property.PROCESSING_EXCEL.name())) {
							statusModel.setProgressText(propertyValue.toString());
						}
					}
					@Override
					public void propertyChanged(String property, Object propertyOldValue, Object propertyNewValue, boolean valid) {
					}
				});
				
				try {
					registeredModel.processSelectedFile();
				}
				catch (final NormitecException e) {
					error = true;
					e.printStackTrace();
					Platform.runLater(new Runnable() {
						@Override
						public void run()
						{
							ErrorViewController error = new ErrorViewController(new ErrorView("Processing the file failed...", 
									"Processing the file failed with the following reason...\n\n" + e.getMessage()), 
									RootView.getInstance().getModalDialog());
							RootView.getInstance().getModalDialog().add(error.getRegisteredView());
							RootView.getInstance().getModalDialog().showAlert();
						}
					});
				}				
			} else {				
				if(sendAllEmails) {
					logger.log(Level.INFO, "Sending all valid emails...");
					registeredModel.addObjectChangedListener(new IPropertyListener() {
						@Override
						public void propertyChanged(String property, Object propertyValue) {				
							if (property.equals(Property.SENDING_EMAIL.name())) {
								statusModel.setProgressText(propertyValue.toString());
								try {
									String[] s = propertyValue.toString().substring("Sending email ".length()).split("/");
									double curr = Integer.parseInt(s[0].trim());
									double total = Integer.parseInt(s[1].trim());
									
									if (curr / total > 0.005)
										statusModel.setProgressValue(curr / total);
									
								} catch (Exception e){ e.printStackTrace();}
							}
						}
						@Override
						public void propertyChanged(String property, Object propertyOldValue, Object propertyNewValue) {						
						}
						@Override
						public void propertyChanged(String property, Object propertyValue, boolean valid) {
						}
						@Override
						public void propertyChanged(String property, Object propertyOldValue, Object propertyNewValue, boolean valid) {
						}
					});
					registeredModel.sendAllEmails();
				} else {
					logger.log(Level.INFO, "Sending single email overriding all validations...");
					statusModel.setProgressText("Sending email...");
					registeredModel.sendEmail(registeredModel.getCurrentEmail());
				}
				
				//Fire to update UI of current email details
				registeredModel.setCurrentIndex(registeredModel.getCurrentIndex()+1);
			}
			
			if (!error) {
				Platform.runLater(new Runnable() {
					@Override
					public void run()
					{
						navigator.getRegisteredView().setDisable(false);
						RootView.getInstance().getModalDialog().hideAlert();
					}
				});
			}
		}
	}
}
