package net.lamida.rest;

import java.util.Date;

import net.lamida.util.Utils;

public class Job {
	public static final String RESULT_DIRECTORY = "result";
	
	private String id;
	private Parameter param;
	private Response response;
	
	public Job(Parameter param) {
		super();
		this.param = param;
		this.id = Utils.formatDate(new Date());
	}

	public Job(Parameter param, String jobId) {
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

	public Parameter getParam() {
		return param;
	}

	public void setParam(Parameter param) {
		this.param = param;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

}
