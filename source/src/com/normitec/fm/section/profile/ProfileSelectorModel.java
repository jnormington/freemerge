package com.normitec.fm.section.profile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.normitec.exception.NormitecException;
import com.normitec.fm.Constants;
import com.normitec.fm.section.AppControlDefaults.Property;
import com.normitec.fm.utils.FileUtil;
import com.normitec.model.AbstractModel;

public class ProfileSelectorModel extends AbstractModel {
	private static final Logger logger = Logger.getLogger(ProfileSelectorModel.class);
	private List<File> files;
	private boolean isNewProfile;
	private File selectedProfile;
	
	public ProfileSelectorModel() {
		this.refreshAndReset();
	}
	
	public void refreshAndReset() {
		//get list of profiles
		this.setFiles(FileUtil.getListOfFiles(Constants.APP_STORAGE));		
		//Set selected profile to null
		this.selectedProfile = null;
		this.isNewProfile = false;
		this.firePropertyChanged(Property.PROFILES_LIST.name(), this.getFilesAsProfileNames());
	}
	
	public void setFiles(List<File> files) {
		this.files = files;
		this.firePropertyChanged(Property.PROFILES_LIST.name(), this.getFilesAsProfileNames());
	}
	
	public File getSelectedProfile() {
		return this.selectedProfile;
	}
	
	public boolean isNewProfile() {
		return isNewProfile;
	}

	public void setProfile(String profile, boolean selected) throws NormitecException {
		File tempFile = null;
		int index = -1;
		
		if (profile != null && profile.trim().length() > 0 && selected) {
			index = FileUtil.getIndexByFileName(files, profile);
		} 		
		
		if (index == -1) {
			tempFile = this.createFile(profile);
			this.selectedProfile = tempFile;
		
			if (tempFile != null)
				this.firePropertyChanged(Property.LOAD_PROFILE.name(), tempFile, true);
				
		} else {
			tempFile = this.files.get(index);
			if (tempFile.isFile() && tempFile.exists() && tempFile.canRead()) {
				this.selectedProfile = tempFile;
				this.firePropertyChanged(Property.LOAD_PROFILE.name(), tempFile, (tempFile == null ? false : true));
			} else {
				this.firePropertyChanged(Property.LOAD_PROFILE.name(), tempFile, false);
			}
				
		}
		
		logger.log(Level.INFO, "Selected profile: " + tempFile);
	}
	
	/***
	 * Call instance.setProfile(String profileName) to automatically create the file if it is not existent.
	 * @param profileName
	 * @return
	 * @throws NormitecException
	 */
	//File processing actions
	private File createFile(String profileName) throws NormitecException {
		File temp = null;
		if (!isProfileNameDuplicate(profileName)) {
			try {
				temp = FileUtil.createFile(Constants.APP_STORAGE, profileName);
				this.files.add(temp);
			}
			catch (IOException e) {
				e.printStackTrace();
				this.firePropertyChanged(Property.FILE_DUPLICATE.name(), "Creating profile failed");
			}
		} else {
			this.firePropertyChanged(Property.FILE_DUPLICATE.name(), "Profile already exists with this name");
		}
		
		this.isNewProfile = true;
		
		return temp;
	}
	
	public void deleteFile(String profileName) {
		int index = -1;
		if (profileName != null) {			
			index = FileUtil.getIndexByFileName(files, profileName);
			System.out.println(index);
			try {
				FileUtil.deleteFileAtIndex(files, index);
				this.firePropertyChanged(Property.FILE_DELETED.name(), profileName, true);
			}
			catch (NormitecException e) {
				e.printStackTrace();
				//Reload file list from OS as a precaution.
				this.refreshAndReset();
				this.firePropertyChanged(Property.FILE_DELETED.name(), profileName, false);				
			}
			
			this.firePropertyChanged(Property.FILE_DELETED.name(), this.getFilesAsProfileNames());
		}
	}
	
	private List<String> getFilesAsProfileNames() {
		List<String> fileNames = new ArrayList<String>();
		
		for(File f : files) {
			fileNames.add(f.getName());
		}
		
		return fileNames;
	}
	
	private boolean isProfileNameDuplicate(String profile) {		
		return FileUtil.isFileDuplicate(Constants.APP_STORAGE, profile);
	}
	
	@Override
	protected void reFireUpdates() {
		this.firePropertyChanged(Property.PROFILES_LIST.name(), this.getFilesAsProfileNames());
	}
}
