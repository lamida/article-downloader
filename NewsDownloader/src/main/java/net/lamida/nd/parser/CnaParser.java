package net.lamida.nd.parser;

import java.util.Properties;

import net.lamida.nd.Utils;


public class CnaParser extends AbstractParser {
	@Override
	public void init(String url) {
		super.init(url);
		Properties prop = Utils.loadConfigurationProperty();
		newsContentSelector = prop.getProperty("cnaParserNewsContentSelector");
		newsTitleSelector = prop.getProperty("cnaParserNewsTitleSelector");
		newsSectionSelector = prop.getProperty("cnaParserNewsSectionSelector");
		newsPostTime = prop.getProperty("cnaParserNewsPostTime");
	}

	@Override
	public String getNewsPostTime() {
		return super.getNewsPostTime() != null ? super.getNewsPostTime().replace("POSTED: ", "") : null;
	}
	
	
}
