package net.lamida.nd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

public class Utils {
	private static final File configFile = new File("config/config.properties");
	public static String readFileToString(File file) throws IOException{
		return FileUtils.readFileToString(file);
	}
	
	public static Properties loadConfigurationProperty(){
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(configFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props;
	}
	
	public static String loadConfiguration(String key){
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(configFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props.getProperty(key);
	}
	
	public static void saveConfiguration(String key, String value){
		Properties props = new Properties();
		props.setProperty(key, value);
		try {
			props.store(new FileOutputStream(configFile), "config");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static int countWords(String s){

	    int counter = 0;

	    boolean word = false;
	    int endOfLine = s.length() - 1;

	    for (int i = 0; i < s.length(); i++) {
	        // if the char is letter, word = true.
	        if (Character.isLetter(s.charAt(i)) == true && i != endOfLine) {
	            word = true;
	            // if char isnt letter and there have been letters before (word
	            // == true), counter goes up.
	        } else if (Character.isLetter(s.charAt(i)) == false && word == true) {
	            counter++;
	            word = false;
	            // last word of String, if it doesnt end with nonLetter it
	            // wouldnt count without this.
	        } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
	            counter++;
	        }
	    }
	    return counter;
	}
}
