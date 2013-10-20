package net.lamida.nd.parser;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.lamida.nd.Utils;

public class CnnParser extends AbstractParser{
	private Log log = LogFactory.getLog(this.getClass().toString());
	
	@Override
	public void init(String url) {
		super.init(url);
		Properties prop = Utils.loadConfigurationProperty();
		if(url.contains("blog")){
			imageRootPath = prop.getProperty("cnnBlogImageRootPath");
			newsContentSelector = prop.getProperty("cnnBlogParserNewsContentSelector");
			newsImageSelector = prop.getProperty("cnnBlogParserNewsImageSelector");
			newsImageCaptionSelector = prop.getProperty("cnnBlogParserNewsImageCaptionSelector");
			newsTitleSelector = prop.getProperty("cnnBlogParserNewsTitleSelector");
			newsSectionSelector = prop.getProperty("cnnBlogParserNewsSectionSelector");
			newsPostTime = prop.getProperty("cnnBlogParserNewsPostTime");
		}else{
			imageRootPath = prop.getProperty("cnnImageRootPath");
			newsContentSelector = prop.getProperty("cnnParserNewsContentSelector");
			newsImageSelector = prop.getProperty("cnnParserNewsImageSelector");
			newsImageCaptionSelector = prop.getProperty("cnnParserNewsImageCaptionSelector");
			newsTitleSelector = prop.getProperty("cnnParserNewsTitleSelector");
			newsSectionSelector = prop.getProperty("cnnParserNewsSectionSelector");
			newsPostTime = prop.getProperty("cnnParserNewsPostTime");
		}
		if(imageRootPath == null){
			imageRootPath = "";
		}
	}
	
	@Override
	public String getNewsImageCaption() {
		if(url.contains("blog")){
			log.info("getNewsImageCaption");
			String result = null;
			try {
				Document doc = getDocument(url);
				Elements els = doc.select(newsImageCaptionSelector);
				Element el = null;
				if (els != null && !els.isEmpty()) {
					el = els.get(0);
					result = el.attr("title");
				}
			} catch (Exception e) {
				log.error("some error happen when parsing this news: " + e.getMessage());
				result = "some error happen when parsing this news.";
			}
			return result;
		}else{
			return getSingleElementText(newsImageCaptionSelector);
		}	
	}
}
