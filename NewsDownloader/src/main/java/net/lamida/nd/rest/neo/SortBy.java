package net.lamida.nd.rest.neo;

import net.lamida.nd.rest.SearchProviderEnum;

public enum SortBy {
	ALJAZEERA_RELEVANCE(SearchProviderEnum.ALJAZEERA.getName(),"r", "relevance"), ALJAZEERA_DATE(SearchProviderEnum.ALJAZEERA.getName(),"d", "date"), 
	CNN_RELEVANCE(SearchProviderEnum.CNN.getName(),"relevance", "relevance"), CNN_DATE(SearchProviderEnum.CNN.getName(),"date", "date"), 
	CNA_POPULARITY(SearchProviderEnum.CNA.getName(),"popularity", "popularity"), CNA_LATEST(SearchProviderEnum.CNA.getName(),"latest", "latest");
	
	final static String enumLabel = "<<sortBy>>";
	private String providerId;
	private String value;
	private String description;

	public String getValue() {
		return value;
	}
	
	public String getDescription() {
		return description;
	}

	SortBy(String id, String value, String description) {
		this.providerId = id;
		this.value = value;
		this.description = description;
	}

	public String toString() {
		return description;
	}

	public static SortBy getEnumByValue(String provider, String value) {
		for (SortBy sb : SortBy.values()) {
			if (sb.providerId.startsWith(provider) && sb.getValue().equals(value)) {
				return sb;
			}
		}
		return null;
	}

	public static SortBy getEnumByDescription(String prefixId, String desc) {
		for (SortBy sb : SortBy.values()) {
			if (sb.providerId.startsWith(prefixId) && sb.getDescription().equals(desc)) {
				return sb;
			}
		}
		return null;
	}
};