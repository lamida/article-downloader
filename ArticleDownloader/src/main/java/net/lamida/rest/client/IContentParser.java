package net.lamida.rest.client;

import java.io.File;


public interface IContentParser {
	String parse(File htmlFile);
	void saveAll();
}
