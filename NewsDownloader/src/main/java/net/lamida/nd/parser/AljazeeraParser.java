package net.lamida.nd.parser;

import java.util.Properties;

import net.lamida.nd.Utils;

public class AljazeeraParser extends AbstractParser{
	@Override
	public void init(String url) {
		super.init(url);
		Properties prop = Utils.loadConfigurationProperty();
		imageRootPath = prop.getProperty("aljazeeraImageRootPath");
		newsContentSelector = prop.getProperty("aljazeeraParserNewsContentSelector");
		newsImageSelector = prop.getProperty("aljazeeraParserNewsImageSelector");
		newsImageCaptionSelector = prop.getProperty("aljazeeraParserNewsImageCaptionSelector");
		newsTitleSelector = prop.getProperty("aljazeeraParserNewsTitleSelector");
		newsSectionSelector = prop.getProperty("aljazeeraParserNewsSectionSelector");
		newsPostTime = prop.getProperty("aljazeeraParserNewsPostTime");
	}
	
	@Override
	public String getNewsPostTime() {
		String np = super.getNewsPostTime();
		return np != null ? np.substring(np.indexOf(",") + 1, np.indexOf("-")).trim() : null;
	}
}
