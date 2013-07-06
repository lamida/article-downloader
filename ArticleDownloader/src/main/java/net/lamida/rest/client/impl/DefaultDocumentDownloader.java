package net.lamida.rest.client.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import net.lamida.rest.Job;
import net.lamida.rest.SearchResponse;
import net.lamida.rest.SearchResult;
import net.lamida.rest.client.IDocumentDownloader;
import net.lamida.util.ConsoleProgressReporter;
import net.lamida.util.ProgressReporter;
import net.lamida.util.Utils;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.google.gson.GsonBuilder;

public class DefaultDocumentDownloader implements IDocumentDownloader {
	private Logger log = Logger.getLogger(this.getClass().toString());
	
	private static int CONNECTION_TIMEOUT = 10000;
	private static int READ_TIMEOUT = 10000;
	
	private Job job;
	
	private ProgressReporter progressReporter;

	public DefaultDocumentDownloader(Job job,
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
	public void download(SearchResponse response) {
		log.info("Crawling all articles for jobId: " + job.getId());
		int totalResults = response.getResults().size();
		log.info("Total articles: " + totalResults);
		File downloadFolder = new File(Utils.buildDownloadFolder(job.getId()));
		downloadFolder.mkdirs();
		int i = 1;
		try{
			for (SearchResult document: response.getResults()) {
				log.info("Downloading article: " + document.getWebTitle());
				updateProgress(i, document);
				String prefix = i < 10 ? "0" + i : "" + i;
				String documentId = document.getId().replace("/", "_");
				File destinationFile = new File(buildArticlesFilePath(downloadFolder, prefix, documentId));
				FileUtils.copyURLToFile(new URL(document.getWebUrl()), destinationFile, CONNECTION_TIMEOUT, READ_TIMEOUT);
				log.info("Written : " + destinationFile.getPath());
				document.setRetrievedDate(new Date()); 
				i++;
			}
			saveResultMetadata(response);
		}catch(IOException e){
			log.error(e.getMessage());
		}
	}

	private String buildArticlesFilePath(File downloadFolder, String prefix, String documentId) {
		return downloadFolder.getAbsolutePath() + File.separator + prefix + "_" + documentId + Utils.HTML_EXTENSION;
	}

	private void saveResultMetadata(SearchResponse response)
			throws IOException {
		// write metadata
		log.info("saving response to loca: ");
		FileUtils.writeStringToFile(new File(Utils.buildRestResultMetadataFilePath(job.getId())), new GsonBuilder().setPrettyPrinting().create().toJson(response));
	}
	
	private void updateProgress(int i, SearchResult document) {
		progressReporter.updateCurrentStatus("Downloading article: " + document.getWebTitle());
		progressReporter.updateCurrentProcess(i);
	}
}
