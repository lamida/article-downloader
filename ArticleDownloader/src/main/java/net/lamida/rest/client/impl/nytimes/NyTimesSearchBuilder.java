package net.lamida.rest.client.impl.nytimes;

import java.io.File;
import java.io.IOException;

import net.lamida.rest.Job;
import net.lamida.rest.SearchParameter;
import net.lamida.rest.SearchResponse;
import net.lamida.rest.client.ISearchBuilder;
import net.lamida.util.Utils;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import com.google.gson.GsonBuilder;

public class NyTimesSearchBuilder implements ISearchBuilder {
	private Logger log = Logger.getLogger(this.getClass().toString());
	
	private Job job;
	
	public NyTimesSearchBuilder(Job job) {
		log.info("Initializing NyTimesResponseFetcher");
	}

	private String getSearchResponse() {
		log.info("Try get list of articles...");
		SearchParameter param = job.getParam();
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
			File jobFolder = new File(Utils.RESULT_FOLDER + File.separator + job.getId());
			jobFolder.mkdirs();
			FileUtils.writeStringToFile(new File(jobFolder, Utils.REST_REQUEST_PARAM_METADATA_FILE), new GsonBuilder().setPrettyPrinting().create().toJson(param));
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		
		log.info("Using keywords: " + param.getQuery());
		log.info("page-size: " + param.getPageSize());
		ClientRequest req = new ClientRequest(param.getEndPoint());
		req.queryParameter("query", param.getQuery())
				.queryParameter("api-key", param.getApiKey())
				.queryParameter("format", param.getFormat())
//				.queryParameter("page-size", param.getPageSize())
				.queryParameter("begin_date", param.getFromDate())
				.queryParameter("end_date", param.getToDate())
				;

		ClientResponse<String> res = null;
		try {
			res = req.get(String.class);
			return res.getEntity();
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return null;
	}

	public SearchResponse buildSearchResponse() {
		// TODO Auto-generated method stub
		return null;
	}

}
