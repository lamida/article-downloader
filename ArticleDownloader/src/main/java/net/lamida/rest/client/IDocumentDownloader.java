package net.lamida.rest.client;

import net.lamida.rest.SearchResponse;

public interface IDocumentDownloader {
	void download(SearchResponse response);
}
