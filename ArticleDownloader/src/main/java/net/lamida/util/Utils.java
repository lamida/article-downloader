package net.lamida.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Utils {
	static final DateFormat JOB_FOLDER_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");
	public static final String PDF_EXTENSION = ".pdf";
	public static final String TXT_EXTENSION = ".txt";
	public static final String HTML_EXTENSION = ".html";
	
	public static String formatDate(Date date){
		return JOB_FOLDER_FORMAT.format(date);
	}

	public static final String RESULT_FOLDER = "result";
	public static final String ARTICLES_DOWNLOAD_FOLDER = "html";
	public static final String ARTICLES_PARSED_FOLDER = "txt";
	public static final String REST_REQUEST_PARAM_METADATA_FILE = "00_PARAM_META.json";
	public static final String REST_RESPONSE_RESULT_METADATA_FILE = "00_RESULT_META.json";
	
	
	public static String buildResultFolder(String jobId){
		StringBuffer sb = new StringBuffer();
		sb.append(Utils.RESULT_FOLDER);
		sb.append(File.separator);
		sb.append(jobId);
		return sb.toString();
	}
	
	public static String buildDownloadFolder(String jobId){
		StringBuffer sb = new StringBuffer();
		sb.append(buildResultFolder(jobId));
		sb.append(File.separator);
		sb.append(Utils.ARTICLES_DOWNLOAD_FOLDER);
		return sb.toString();
	}

	public static String buildParsedFolder(String jobId){
		StringBuffer sb = new StringBuffer();
		sb.append(buildResultFolder(jobId));
		sb.append(File.separator);
		sb.append(Utils.ARTICLES_PARSED_FOLDER);
		return sb.toString();
	}
	public static String buildPdfFolder(String jobId){
		StringBuffer sb = new StringBuffer();
		sb.append(buildResultFolder(jobId));
		sb.append(File.separator);
		sb.append(Utils.PDF_FOLDER);
		return sb.toString();
	}
	
	
	public static String buildRestRequestParamMetadataFilePath(String jobId){
		StringBuffer sb = new StringBuffer();
		sb.append(buildResultFolder(jobId));
		sb.append(File.separator);
		sb.append(Utils.REST_REQUEST_PARAM_METADATA_FILE);
		return sb.toString();
		
	}

	public static String buildRestResultMetadataFilePath(String jobId){
		StringBuffer sb = new StringBuffer();
		sb.append(buildResultFolder(jobId));
		sb.append(File.separator);
		sb.append(Utils.REST_RESPONSE_RESULT_METADATA_FILE);
		return sb.toString();
		
	}

	public static final String PDF_FOLDER = "pdf";
	public static final String MERGED_PDF_FILE = "00_articles_merged.pdf";
	
}
