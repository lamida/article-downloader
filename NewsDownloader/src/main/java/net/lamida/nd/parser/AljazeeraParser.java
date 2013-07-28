package net.lamida.nd.parser;

import java.util.Properties;

import net.lamida.nd.Utils;

public class AljazeeraParser extends AbstractParser{
	@Override
	public void init(String url) {
		super.init(url);
		Properties prop = Utils.loadConfigurationProperty();
		newsContentSelector = prop.getProperty("aljazeeraParserNewsContentSelector");
		newsTitleSelector = prop.getProperty("aljazeeraParserNewsTitleSelector");
		newsSectionSelector = prop.getProperty("aljazeeraParserNewsSectionSelector");
		newsPostTime = prop.getProperty("aljazeeraParserNewsPostTime");
	}
}
