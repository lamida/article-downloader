package net.lamida.nd.parser;

public interface IParser {
	void init(String url);
	String getNewsContent();
	String getNewsTitle();
	String getNewsSection();
	String getNewsPostTime();
}
