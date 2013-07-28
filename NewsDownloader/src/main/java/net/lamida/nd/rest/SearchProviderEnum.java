package net.lamida.nd.rest;

public enum SearchProviderEnum {
	ALJAZEERA("Aljazeera"), CNN("CNN"), CNA("Channel News Asia");
	
	private String name;
	
	private SearchProviderEnum(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}

	@Override
	public String toString() {
		return name;
	}
	
	public static SearchProviderEnum getSearchProviderEnumByName(String name){
		for(SearchProviderEnum en : SearchProviderEnum.values()){
			if(name.equals(en.getName())){
				return en;
			}
		}
		return null;
	}
}
