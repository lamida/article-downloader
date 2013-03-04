package net.lamida.rest;

import java.util.Date;

import net.lamida.util.Utils;

public class Job {
	public static final String RESULT_DIRECTORY = "result";
	
	private String id;
	private RestParameter param;
	private RestResponse response;
	
	public Job(RestParameter param) {
		super();
		this.param = param;
		this.id = Utils.formatDate(new Date());
	}

	public Job(RestParameter param, String jobId) {
		super();
		this.param = param;
		this.id = jobId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RestParameter getParam() {
		return param;
	}

	public void setParam(RestParameter param) {
		this.param = param;
	}

	public RestResponse getResponse() {
		return response;
	}

	public void setResponse(RestResponse response) {
		this.response = response;
	}

}
