package net.lamida.nd.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import net.lamida.nd.Utils;


public class CnaParser extends AbstractParser {
	@Override
	public void init(String url) {
		super.init(url);
		Properties prop = Utils.loadConfigurationProperty();
		imageRootPath = prop.getProperty("cnaImageRootPath");
		newsContentSelector = prop.getProperty("cnaParserNewsContentSelector");
		newsImageSelector = prop.getProperty("cnaParserNewsImageSelector");
		newsImageCaptionSelector = prop.getProperty("cnaParserNewsImageCaptionSelector");
		newsTitleSelector = prop.getProperty("cnaParserNewsTitleSelector");
		newsSectionSelector = prop.getProperty("cnaParserNewsSectionSelector");
		newsDateFormat = prop.getProperty("cnaParserNewsDateFormat");
		newsPostTime = prop.getProperty("cnaParserNewsPostTime");
	}

	@Override
	public String getNewsPostTime() {
		return super.getNewsPostTime() != null ? super.getNewsPostTime().replace("POSTED: ", "") : null;
	}
}
