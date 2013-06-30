
package net.lamida.rest;

import java.util.Date;

import net.lamida.util.Utils;

public class Job {
	public String id;
	public RestParameter param;
	public RestResponse response;

	public Job(RestParameter param) {
		super();
		this.param = param;
		this.id = Utils.formatDate(new Date());
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
