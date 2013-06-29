package net.lamida.rest.client.impl.guardian;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import net.lamida.rest.Job;
import net.lamida.rest.Response;
import net.lamida.rest.Result;
import net.lamida.rest.client.IDocumentDownloader;
import net.lamida.util.ConsoleProgressReporter;
import net.lamida.util.ProgressReporter;
import net.lamida.util.Utils;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.google.gson.GsonBuilder;

public class GuardianDefaultDocumentDownloader implements IDocumentDownloader {
	private Logger log = Logger.getLogger(this.getClass().toString());
	public static final String DOWNLOAD_FOLDER = "html";
	public static final String RESULT_METADATA_FILE = "00_RESULT_METADATA.json";
	public static final String PARAM_METADATA_FILE = "00_PARAM_METADATA.json";
	
	private static int CONNECTION_TIMEOUT = 10000;
	private static int READ_TIMEOUT = 10000;
	
	private Job job;
	
	private ProgressReporter progressReporter;

	public GuardianDefaultDocumentDownloader(Job job,
			ProgressReporter progressReporter) {
		super();
		this.job = job;
		if(progressReporter == null){
			this.progressReporter = new ConsoleProgressReporter();
		}else{
			this.progressReporter = progressReporter;
		}
	}

	/**
	 * 
	 */
	public void download(Response response) {
		log.info("Crawling all articles for jobId: " + job.getId());
		int totalResults = response.getResults().size();
		log.info("Total articles: " + totalResults);
		File downloadFolder = new File(Job.RESULT_DIRECTORY + File.separator + job.getId() + File.separator + DOWNLOAD_FOLDER);
		downloadFolder.mkdirs();
		int i = 1;
		try{
			for (Result document: response.getResults()) {
				log.info("Downloading article: " + document.getWebTitle());
				progressReporter.updateCurrentStatus("Downloading article: " + document.getWebTitle());
				progressReporter.updateCurrentProcess(i);
				String prefix = i < 10 ? "0" + i : "" + i;
				String documentId = document.getId().replace("/", "_");
				File destinationFile = new File(downloadFolder.getAbsolutePath() + File.separator + prefix + "_" + documentId + Utils.HTML_EXTENSION);
				FileUtils.copyURLToFile(new URL(document.getWebUrl()), destinationFile, CONNECTION_TIMEOUT, READ_TIMEOUT);
				log.info("Written : " + destinationFile.getPath());
				document.setRetrievedDate(new Date()); 
				i++;
			}
			// write metadata
			log.info("saving response to loca: ");
			FileUtils.writeStringToFile(new File(downloadFolder, RESULT_METADATA_FILE), new GsonBuilder().setPrettyPrinting().create().toJson(response));
		}catch(IOException e){
			log.error(e.getMessage());
		}
	}
}
