package com.normitec.fm.section.navigator;

import javafx.scene.Node;

import com.normitec.fm.section.RootView;
import com.normitec.fm.section.AppControlDefaults.Property;
import com.normitec.fm.section.email.EmailView;
import com.normitec.fm.section.email.EmailViewController;
import com.normitec.fm.section.profile.ProfileSelector;
import com.normitec.fm.section.profile.ProfileSelectorController;
import com.normitec.fm.section.settings.AboutHelpView;
import com.normitec.fm.section.settings.AboutHelpViewController;
import com.normitec.fm.section.settings.SettingsView;
import com.normitec.fm.section.settings.SettingsViewController;
import com.normitec.model.AbstractModel;

public class NavigatorModel extends AbstractModel {

	protected EmailViewController emailView;
	protected SettingsViewController settingsView;
	protected AboutHelpViewController aboutHelpView;
	private Node lastNode;

	public void requestNewView(Class s) {
		this.initViewIfRequired(s);
		this.firePropertyChanged(Property.ADD_REMOVE_VIEW.name(), this.lastNode, this.getNodeByClass(s));
		this.lastNode = this.getNodeByClass(s);
	}

	private void initViewIfRequired(Class s) {
		if (s == EmailView.class && this.emailView == null) {
			this.emailView = new EmailViewController();
			this.emailView.getRegisteredView().setId("theme");
		}
		else if (s == SettingsView.class && this.settingsView == null) {
			this.settingsView = SettingsViewController.getInstance();
			this.settingsView.getRegisteredView().setId("theme");
		} 
		else if (s == AboutHelpView.class && this.aboutHelpView == null) {
			this.aboutHelpView = new AboutHelpViewController(new AboutHelpView(),RootView.getInstance());
		}
	}

	private Node getNodeByClass(Class s) {
		if (s == EmailView.class) {
			return this.emailView.getRegisteredView();
		}
		else if (s == SettingsView.class) {
			return this.settingsView.getRegisteredView();
		}
		else if (s == AboutHelpView.class) {
			return this.aboutHelpView.getRegisteredView();
		}
		else if (s == ProfileSelector.class) {
			this.firePropertyChanged(Property.ADD_REMOVE_VIEW.name(), ProfileSelectorController.getInstance().getRegisteredView());
		}

		return null;
	}
}
