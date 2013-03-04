package net.lamida.rest.client.impl.guardian;

import java.io.File;
import java.io.IOException;

import net.lamida.rest.Job;
import net.lamida.rest.RestParameter;
import net.lamida.rest.client.IRestResponseFetcher;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import com.google.gson.GsonBuilder;

public class GuardianResponseFetcher implements IRestResponseFetcher {
	private Logger log = Logger.getLogger(this.getClass().toString());
	
	private Job job;
	
	public GuardianResponseFetcher(Job job) {
		log.info("Initializing GuardianResponseFetcher");
		this.job = job;
	}
	

	public String getResponse() {
		log.info("Try get list of articles...");
		RestParameter param = job.getParam();
		if (param == null) {
			throw new IllegalStateException(
					"Provide parameters before calling getResult");
		}
		if (!param.getFormat().equals("json")) {
			throw new IllegalArgumentException("Only Support Json Format");
		}
		
		// save param metadata
		try {
			log.info("writing param metadata");
			File jobFolder = new File(Job.RESULT_DIRECTORY + File.separator + job.getId());
			jobFolder.mkdirs();
			FileUtils.writeStringToFile(new File(jobFolder, GuardianDefaultDocumentDownloader.PARAM_METADATA_FILE), new GsonBuilder().setPrettyPrinting().create().toJson(param));
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		
		log.info("Using keywords: " + param.getQuery());
		log.info("page-size: " + param.getPageSize());
		ClientRequest req = new ClientRequest(param.getEndPoint());
		req.queryParameter("q", param.getQuery())
				.queryParameter("api-key", param.getApiKey())
				.queryParameter("format", param.getFormat())
				.queryParameter("page-size", param.getPageSize());

		ClientResponse<String> res = null;
		try {
			res = req.get(String.class);
			return res.getEntity();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}


}
