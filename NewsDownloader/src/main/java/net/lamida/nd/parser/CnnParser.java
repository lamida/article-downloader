package net.lamida.nd.parser;

import java.util.Properties;

import net.lamida.nd.Utils;

public class CnnParser extends AbstractParser{
	@Override
	public void init(String url) {
		super.init(url);
		Properties prop = Utils.loadConfigurationProperty();
		imageRootPath = prop.getProperty("cnnImageRootPath");
		newsContentSelector = prop.getProperty("cnnParserNewsContentSelector");
		newsImageSelector = prop.getProperty("cnnParserNewsImageSelector");
		newsImageCaptionSelector = prop.getProperty("cnnParserNewsImageCaptionSelector");
		newsTitleSelector = prop.getProperty("cnnParserNewsTitleSelector");
		newsSectionSelector = prop.getProperty("cnnParserNewsSectionSelector");
		newsPostTime = prop.getProperty("cnnParserNewsPostTime");
	}
}
