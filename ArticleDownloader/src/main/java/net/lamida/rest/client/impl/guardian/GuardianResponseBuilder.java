package net.lamida.rest.client.impl.guardian;

import net.lamida.rest.Job;
import net.lamida.rest.RestResponse;
import net.lamida.rest.client.IResponseBuilder;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class GuardianResponseBuilder implements IResponseBuilder{
	private Logger log = Logger.getLogger(this.getClass().toString());
	private Job job;
	
	public GuardianResponseBuilder(Job job) {
		super();
		this.job = job;
	}


	public RestResponse buildResponse(String restResult) {
		log.info("Building List of articles...");
		JsonParser parser = new JsonParser();
		JsonElement responseJson = parser.parse(restResult).getAsJsonObject().get("response");
		RestResponse response = new Gson().fromJson(responseJson, RestResponse.class);
		job.setResponse(response);
		return response;
	}
}
