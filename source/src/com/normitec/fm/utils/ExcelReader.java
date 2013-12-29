package com.normitec.fm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.normitec.fm.section.settings.SettingsModel;

public class ExcelReader {
	private static final Logger logger = Logger.getLogger(ExcelReader.class);
	private Workbook workBook;
	private Sheet workSheet;
	private SettingsModel settings;
	
	/***
	 * Create HSSFWorkbook or XSSFWorkbook instance with the supplied excel file instance.
	 * @param file
	 * @throws IOException
	 */
	
	public ExcelReader(File file, int workSheetNumber, SettingsModel settings) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		this.settings = settings;
		
		if (file.getName().substring(file.getName().lastIndexOf(".")).equals(".xls")) {
			this.workBook = new HSSFWorkbook(fileInputStream);			
		} else if (file.getName().substring(file.getName().lastIndexOf(".")).equals(".xlsx")) {
			this.workBook = new XSSFWorkbook(fileInputStream);
		}
		
		this.workSheet = this.workBook.getSheetAt(workSheetNumber);
	}
	
	public int getUsedRowCount() {
		return this.workSheet.getPhysicalNumberOfRows();
	}
	
	public int getUsedColumnCount(int row) {
		return this.workSheet.getRow(row).getPhysicalNumberOfCells();
	}
	
	public void setSettingsModel(SettingsModel settings) {
		this.settings = settings;
	}
	
	public String getCellDataAtRowColumn(int row, int column) {
	
		String cellData = null;

		cellData = this.convertDataTypeToString(this.workSheet.getRow(row).getCell(column));
		
		if (cellData == null)
			logger.log(Level.INFO, "Is returning blank data for row: " + row + " col: " + column + " = " + true);
		else
			logger.log(Level.INFO, "Is returning blank data for row: " + row + " col: " + column + " = " + false);
		
		return cellData;
	}
	
	private String convertDataTypeToString(Cell cell) {
		//If cell is null then it is likely to be empty.
		if (cell != null) {
			switch(cell.getCellType()) {
		        case Cell.CELL_TYPE_BOOLEAN:
		        	return String.valueOf(cell.getBooleanCellValue());
		        case Cell.CELL_TYPE_NUMERIC:
		        	if (DateUtil.isCellDateFormatted(cell))
		        		return new SimpleDateFormat(this.settings.getDateFormat()).format(cell.getDateCellValue());
		        	else
		        		return String.valueOf(cell.getNumericCellValue());
		        case Cell.CELL_TYPE_STRING:
		        	return cell.getStringCellValue();
		        case Cell.CELL_TYPE_BLANK: default:
		        	return "";
			}
		} else 
			return "";
	}
}
