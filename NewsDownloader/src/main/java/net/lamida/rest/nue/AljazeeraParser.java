package net.lamida.rest.nue;

public class AljazeeraParser extends AbstractParser{
	@Override
	public void init(String url) {
		super.init(url);
		newsContentSelector = "td.DetailedSummary";
		newsTitleSelector = "h1#DetailedTitle";
		newsSectionSelector = "span#ctl00_cphBody_ChannelTitle a";
		newsPostTime = "span#ctl00_cphBody_lblDate";
	}
}
