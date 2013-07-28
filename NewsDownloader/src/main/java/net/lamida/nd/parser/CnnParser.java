package net.lamida.nd.parser;

import java.util.Properties;

import net.lamida.nd.Utils;

public class CnnParser extends AbstractParser{
	@Override
	public void init(String url) {
		super.init(url);
		Properties prop = Utils.loadConfigurationProperty();
		newsContentSelector = prop.getProperty("cnnParserNewsContentSelector");
		newsTitleSelector = prop.getProperty("cnnParserNewsTitleSelector");
		newsSectionSelector = prop.getProperty("cnnParserNewsSectionSelector");
		newsPostTime = prop.getProperty("cnnParserNewsPostTime");
	}
}
