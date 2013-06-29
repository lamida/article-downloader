package net.lamida.rest.client.impl.guardian;

import net.lamida.rest.Job;
import net.lamida.rest.Response;
import net.lamida.rest.client.IResponseBuilder;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class GuardianResponseBuilder implements IResponseBuilder{
	private Logger log = Logger.getLogger(this.getClass().toString());
	private Job job;
	public static String jSON_RESPONSE_PATH = "response";
	
	public GuardianResponseBuilder(Job job) {
		super();
		this.job = job;
	}


	public Response buildFromServer(String restResult) {
		log.info("Building List of articles...");
		JsonParser parser = new JsonParser();
		JsonElement responseJson = parser.parse(restResult).getAsJsonObject().get("response");
		Response response = new Gson().fromJson(responseJson, Response.class);
		job.setResponse(response);
		return response;
	}

	public Response buildFromLocal(String restResultLocal) {
		log.info("Building List of articles...");
		Response response = new Gson().fromJson(restResultLocal, Response.class);
		job.setResponse(response);
		return response;
	}

}
