package net.lamida.nd.rest.neo;
public enum SortBy {
	ALJAZEERA_RELEVANCE("relevance"), ALJAZEERA_DATE("date"),
	CNN_RELEVANCE("relevance"), CNN_DATE("date"),
	CNA_POPULARITY("popularity"), CNA_LATEST("latest");
	final static String enumLabel = "<<sortBy>>";
	private String value;
	public String getValue(){
		return value;
	}
	SortBy(String label){
		this.value =label;
	}
        
        public String toString(){
            return value;
        }
        
        public static SortBy getEnum(String label){
            for(SortBy sb:SortBy.values()){
                if(sb.getValue().equals(label)){
                    return sb;
                }
            }
            return null;
        } 
	
};