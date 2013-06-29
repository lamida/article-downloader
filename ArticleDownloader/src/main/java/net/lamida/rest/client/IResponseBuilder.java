package net.lamida.rest.client;

import net.lamida.rest.Response;

public interface IResponseBuilder {
	Response buildFromServer(String restResult);
	Response buildFromLocal(String restResult);
}
