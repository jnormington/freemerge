package com.normitec.fm.section;

import javafx.scene.control.Control;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;

public class AppControlDefaults {
	public static final double CTRL_HEIGHT = 30;
	public static final double NAVBTN_WIDTH = 65;
	public static final double NAVBTN_HEIGHT = 60;
	public static final double NAVBTN_IMG = 45;
	
	public enum Property {
		DISPLAY_EMAIL,
		FILE_SELECTED,
		ADD_REMOVE_VIEW,
		FILE_DELETED,
		LOAD_PROFILE,
		PROFILE_VALID,
		PROFILES_LIST,
		HIDE_COMPLETED, 
		FILE_CREATED,
		FILE_DUPLICATE,
		PROCESSING_EXCEL,
		ERROR,
		ATTACHMENT,
		SENDING_EMAIL
	}
	
	public static void setNodeHeight(double value, Control... controls) {
		for (Control c : controls) {
			c.setPrefHeight(value);
			c.setMinHeight(Region.USE_PREF_SIZE);
			c.setMaxHeight(Region.USE_PREF_SIZE);
		}
	}
	
	/**
	 * To ensure the same look and feel across the whole application
	 * this will set the height and width for the navigation buttons.
	 * @param controls
	 */
	public static void setNodeWidth(double value, Control... controls) {	
		for (Control c : controls) {			
			c.setPrefWidth(value);
			//c.setMinWidth(Region.USE_PREF_SIZE);
			c.setMaxWidth(Region.USE_PREF_SIZE);
		}
	}
			
	/**
	 * This method makes a call to setNodeWidth and setNodeHeight with the values passed.
	 * @param width
	 * @param height
	 * @param controls
	 */
	public static void setNodeWidthHeight(double width, double height, Control... controls) {
		setNodeHeight(height, controls);
		setNodeWidth(width, controls);
	}
	
	public static void setImageViewSize(double width, double height, ImageView... images) {
		for (ImageView imgView : images) {
			imgView.setFitWidth(width);
			imgView.setFitHeight(height);
			imgView.setPreserveRatio(true);
		}
	}
	
}
