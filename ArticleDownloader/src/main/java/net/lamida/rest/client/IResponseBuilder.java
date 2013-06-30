package net.lamida.rest.client;

import net.lamida.rest.RestResponse;

public interface IResponseBuilder {
	RestResponse buildResponse(String restResult);
}
