package com.normitec.fm;

import java.io.File;
import java.io.FileNotFoundException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.normitec.exception.NormitecException;
import com.normitec.fm.section.RootView;
import com.normitec.fm.section.error.ErrorView;
import com.normitec.fm.section.error.ErrorViewController;
import com.normitec.fm.section.profile.ProfileSelectorController;
import com.normitec.fm.utils.FileUtil;
import com.normitec.javafx.view.ModalDialog;

public class FPLauncher extends Application {
	private static final Logger logger = Logger.getLogger(FPLauncher.class);
	public static Stage stage;

	@Override
	public void start(Stage stage) throws Exception {
		FPLauncher.stage = stage;
		stage.setTitle(Constants.APP_NAME);
		stage.getIcons().addAll(new ImageView(Constants.APP12).getImage(),
				new ImageView(Constants.APP24).getImage(),
				new ImageView(Constants.APP32).getImage(),
				new ImageView(Constants.APP48).getImage(),
				new ImageView(Constants.APP64).getImage(),
				new ImageView(Constants.APP96).getImage(),
				new ImageView(Constants.APP128).getImage());
		stage.setWidth(860);
		stage.setHeight(640);
		stage.centerOnScreen();				
		
		// Create Scene
		Scene scene = new Scene(RootView.getInstance());
		stage.setScene(scene);
		// Load CSS file for themeing...
		scene.getStylesheets().add(FPLauncher.class.getResource(Constants.THEME_CSS).toExternalForm());
		stage.show();
		
		this.initDirectoryAndScriptAndDisplayProfiles();			
	}
	
	private void initDirectoryAndScriptAndDisplayProfiles() {
		try {			
			//Create storage directory if it doesn't exist.
			File f = new File(Constants.APP_STORAGE);			
			File ff = new File(Constants.VBSCRIPT_PATH);
			
			if (!f.exists()) {
				f.mkdirs();
				logger.log(Level.INFO, "Creating appStorage directory:  " + Constants.APP_STORAGE);			
			}
					
			if (!ff.exists()) {
				logger.log(Level.INFO, "Creating VBScript directory: " + Constants.VBSCRIPT_LDAP_IMPL_SCRIPT);
				ff.mkdirs();
			}
			
			//Write script from jar to file system
			//Write it each time to ensure that it hasn't been changed for malicious reasons
			File vb = new File(Constants.VBSCRIPT_LDAP_IMPL_SCRIPT);
			vb.setWritable(true);
			FileUtil.writeInputStreamToFile(this.getClass().getResourceAsStream(Constants.VBSCRIPT_IN_JAR),vb);
			vb.setWritable(false);
			ProfileSelectorController.getInstance().showToUserAndResetModel();
		}
		catch (NormitecException | FileNotFoundException e) {
			e.printStackTrace();
			ModalDialog md = new ModalDialog();
			ErrorViewController error = new ErrorViewController(new ErrorView("Some issue was raised while creating directories and writing the VBScript to the file system.",
					"Please try again and if the problems persists please check with your local support.\n\n" + e.getMessage()), 
					md);
			md.add(error.getRegisteredView());
			RootView.getInstance().getChildren().add(md);
			md.showAlert();
		}
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
}
