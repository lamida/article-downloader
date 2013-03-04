package net.lamida.rest.client;

import net.lamida.rest.RestResponse;

public interface IResponseBuilder {
	RestResponse buildFromServer(String restResult);
	RestResponse buildFromLocal(String restResult);
}
