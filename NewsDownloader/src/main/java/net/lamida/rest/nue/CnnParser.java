package net.lamida.rest.nue;

public class CnnParser extends AbstractParser{
	@Override
	public void init(String url) {
		super.init(url);
		newsContentSelector = "div.cnn_storyarea p.cnn_storypgraphtxt";
		newsTitleSelector = "div.cnn_storyarea h1";
		newsSectionSelector = "a.nav-on";
		newsPostTime = "div.cnn_strytmstmp";
	}
}
