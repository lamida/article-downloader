package net.lamida.nd.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AbstractParser {
	protected String url;
	protected String newsContentSelector;
	protected String newsTitleSelector;
	protected String newsSectionSelector;
	protected String newsPostTime;
	
	private Map<String,Document> documentCache;
	
	private Document getDocument(String url){
		if(documentCache.get(url) != null){
			return documentCache.get(url);
		}
		
		Document doc = null;
		try {
			doc = Jsoup.parse(new URL(url), 3000);
			if(doc != null){
				documentCache.put(url, doc);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}
	
	public void init(String url){
		documentCache = new HashMap<String, Document>();
		this.url = url;
	}
	
	public String getNewsContent() {
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
		return getSingleElementText(newsTitleSelector);
	}
	
	public String getNewsSection() {
		return getSingleElementText(newsSectionSelector);
	}
	
	public String getNewsPostTime() {
		return getSingleElementText(newsPostTime);
	}
	
	private String getSingleElementText(String selector) {
		String result = null;
		Document doc = getDocument(url);
		Elements els = doc.select(selector);
		Element el = els.get(0);
		result = el.text();
		return result;
	}

}
