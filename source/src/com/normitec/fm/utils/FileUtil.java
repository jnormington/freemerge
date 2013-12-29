package com.normitec.fm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.normitec.exception.NormitecException;
import com.normitec.exception.ThrowableToString;
import com.normitec.fm.Constants;


public class FileUtil {
	
	private static final Logger logger = Logger.getLogger(FileUtil.class);
	
	public static File createFile(String directory, String fileName) throws IOException {
		File f = new File(directory + Constants.DIR_SEPERATOR + fileName);
		f.createNewFile();
		
		return f;
	}
	
	public static List<File> getListOfFiles(String directory) {
		File[] files = new File(directory).listFiles();
		List<File> filesList = new ArrayList<File>();
		
		for (int f = 0; f < files.length ; f++) {
			if (files[f].isFile())
				filesList.add(files[f]);
		}
		
		return filesList;
	}
		
	public static boolean isFileDuplicate(String directory, String fileName) {
		//Check if the config already exists
		List<File> files = FileUtil.getListOfFiles(directory);
		
		for (File f : files) {
			if (f.getName().toLowerCase().equals(fileName.toLowerCase())) {
				return true;
			}
		}
		
		return false;
	}
	
	public static int getIndexByFileName(List<File> files, String fileName) {
		for (int i=0; i < files.size(); i++) {
			if (files.get(i).getName().equals(fileName)) {
				logger.log(Level.DEBUG, "Found file at index: " + i);
				return i;
			}
		}
		return -1;
	}
	
	public static void deleteFileAtIndex(List<File> files, int index) throws NormitecException {
		if (index == -1) {
			throw new NormitecException("File to delete is not found...");
		} else {
			if (files.get(index).delete())
				files.remove(index);
			else
				throw new NormitecException("File to delete is not found...");
		}
	}
	
	public static String readFileAsString(File file) throws NormitecException {
		byte[] fileByte = null;
		FileInputStream fis = null;

		try {
			// Read file
			logger.log(Level.INFO, "Reading file :" + file.getName());
			fis = new FileInputStream(file);
			fileByte = new byte[(int) file.length()];
			fis.read(fileByte);
			logger.log(Level.DEBUG, "File read...");
		} catch (IOException e) {
			ThrowableToString.exceptionToString(e);
			throw new NormitecException(e);
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return new String(fileByte);
	}
	
	public static void writeStringToFile(File file, String contents) throws NormitecException {
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(contents);
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new NormitecException("Failed to write settings to file... " + e.getMessage(), e);			
		}
	}
	
	public static void writeInputStreamToFile(InputStream input, File output) throws NormitecException, FileNotFoundException {
		FileWriter writer = null;
		try {
			writer = new FileWriter(output);
			String toWriteOut = StreamUtil.convertStreamToString(input);
			writer.write(toWriteOut);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new NormitecException(e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}	
	}
}
