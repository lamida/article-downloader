package net.lamida.nd.pdf;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCalculator {
	public Map<String, Integer> calculateKeyword(String searchQuery, String newsContent){
		Map<String, Integer> counts = new HashMap<String, Integer>();
		String[] keywords = searchQuery.split(" ");
		for(String keyword: keywords){
			int wordCount = countWord(keyword, newsContent);
			counts.put(keyword, wordCount);
		}
		return counts;
	}
	
	private int countWord(String keyword, String document){
		String patternText = "\\b{key}\\b";
		Pattern pattern = Pattern.compile(patternText.replace("{key}", keyword.toLowerCase()));
		Matcher m = pattern.matcher(document.toLowerCase());
		int count = 0;
		while(m.find()){
			count++;
		}
		return count;
	}

}
