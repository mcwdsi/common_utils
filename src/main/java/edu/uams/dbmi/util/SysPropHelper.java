package edu.uams.dbmi.util;

import java.io.File;
import java.util.Properties;

public class SysPropHelper {
	
	static Properties p;
	
	static {
		p = System.getProperties();
	}
	

	public static File getUserDirectory() {
		/*
		Object o = p.getProperty("user.dir");
		if (o instanceof String) {
			String s = (String)o;
		}
		*/
		if (p == null) System.out.println("System properties null or not init.");
		if (p.getProperty("user.dir") == null) System.out.println("user.dir property is null.");
		return new File((String) p.getProperty("user.dir"));
	}
	
}
