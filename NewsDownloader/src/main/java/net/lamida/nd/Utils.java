package net.lamida.nd;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Utils {
	public static String readFileToString(File file) throws IOException{
		return FileUtils.readFileToString(file);
	}
}
