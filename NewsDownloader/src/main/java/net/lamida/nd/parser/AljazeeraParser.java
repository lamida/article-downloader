package net.lamida.nd.parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
		newsDateFormat = prop.getProperty("aljazeeraParserNewsDateFormat");
		newsPostTime = prop.getProperty("aljazeeraParserNewsPostTime");
	}
	
	@Override
	public String getNewsPostTime() {
		String np = super.getNewsPostTime();
		if(np.indexOf(",") != -1 && np.indexOf("-") != -1){
			return np != null ? np.substring(np.indexOf(",") + 1, np.indexOf("-")).trim() : null;
		}else{
			return np;
		}
	}
	
	@Override
	public Date getNewsPostDateTime(){
		Date date = null;
		try {
			date = new SimpleDateFormat(newsDateFormat).parse(getNewsPostTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}
}
