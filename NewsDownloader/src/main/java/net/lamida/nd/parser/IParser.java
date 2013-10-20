package net.lamida.nd.parser;

import java.util.Date;

public interface IParser {
	void init(String url);
	String getNewsContent();
	String getNewsImage();
	String getNewsImageCaption();
	String getNewsTitle();
	String getNewsSection();
	String getNewsPostTime();
	Date getNewsPostDateTime();
}
