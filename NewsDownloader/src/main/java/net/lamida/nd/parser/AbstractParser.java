package net.lamida.nd.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.lamida.nd.rest.SearchProviderEnum;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AbstractParser implements IParser{
	private Log log = LogFactory.getLog(this.getClass().toString());
	protected String url;
	protected String newsContentSelector;
	protected String newsTitleSelector;
	protected String newsSectionSelector;
	protected String newsPostTime;
	
	public static IParser getParser(SearchProviderEnum searchProviderEnum){
		IParser parser = null;
		switch (searchProviderEnum) {
		case ALJAZEERA:
			parser = new AljazeeraParser();
			break;
		case CNA:
			parser = new CnaParser();
			break;
		case CNN:
			parser = new CnnParser();
			break;
		default:
			break;
		}
		return parser;
	}
	
	private Map<String,Document> documentCache;
	
	private Document getDocument(String url){
		log.info("getDocument: " + url);
		if(documentCache.get(url) != null){
			return documentCache.get(url);
		}
		
		Document doc = null;
		try {
			doc = Jsoup.parse(new URL(url), 10000);
			if(doc != null){
				documentCache.put(url, doc);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	public void init(String url){
		log.info("init: " + url);
		documentCache = new HashMap<String, Document>();
		this.url = url;
	}
	
	public String getNewsContent() {
		log.info("getNewsContent");
		StringBuffer sb = new StringBuffer();
		Document doc = getDocument(url);
		Elements paragraphs = doc.select(newsContentSelector);
		for(Element el : paragraphs){
			String p = el.text();
			sb.append(p);
			sb.append("\n\n");
		}
		return sb.toString();
	}
	
	public String getNewsTitle() {
		log.info("getNewsTitle");
		return getSingleElementText(newsTitleSelector);
	}
	
	public String getNewsSection() {
		log.info("getNewsSection");
		return getSingleElementText(newsSectionSelector);
	}
	
	public String getNewsPostTime() {
		log.info("getNewsPostTime");
		return getSingleElementText(newsPostTime);
	}
	
	private String getSingleElementText(String selector) {
		String result = null;
		Document doc = getDocument(url);
		try {
			Elements els = doc.select(selector);
			Element el = null;
			if (els != null && !els.isEmpty()) {
				el = els.get(0);
				result = el.text();
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return result;
	}

}
