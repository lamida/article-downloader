package net.lamida.nd.parser;


public class CnaParser extends AbstractParser {
	@Override
	public void init(String url) {
		super.init(url);
		newsContentSelector = "div.news_detail p";
		newsTitleSelector = "h1.news_title";
		newsSectionSelector = "ul.breadcrumbs-top li a";
		newsPostTime = "li.news_posttime";
	}

	@Override
	public String getNewsPostTime() {
		return super.getNewsPostTime().replace("POSTED: ", "");
	}
	
	
}
