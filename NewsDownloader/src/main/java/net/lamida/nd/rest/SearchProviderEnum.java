package net.lamida.nd.rest;

public enum SearchProviderEnum {
	ALJAZEERA("Aljazeera","alj"), CNN("CNN","cnn"), 
	CNA("Channel News Asia","cna");
	
	private String name;
	private String code;
	
	private SearchProviderEnum(String name, String code){
		this.name = name;
		this.code = code;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getCode(){
		return this.code;
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
