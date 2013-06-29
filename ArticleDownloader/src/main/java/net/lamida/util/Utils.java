package net.lamida.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	//static final DateFormat JOB_FOLDER_FORMAT = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss z");
	static final DateFormat JOB_FOLDER_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");
	public static final String PDF_EXTENSION = ".pdf";
	public static final String TXT_EXTENSION = ".txt";
	public static final String HTML_EXTENSION = ".html";
	
	public static String formatDate(Date date){
		return JOB_FOLDER_FORMAT.format(date);
	}
	
}
