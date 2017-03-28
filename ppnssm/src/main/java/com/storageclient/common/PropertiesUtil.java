package com.storageclient.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {

	static Map<String, Properties> map = new HashMap();

	public static Properties getProperties(Class clazz, String classpathFilename) {
		Properties p = (Properties) map.get(classpathFilename);
		if (p == null) {
			synchronized (Properties.class) {
				p = new Properties();
				try {
					p.load(clazz.getResourceAsStream(classpathFilename));
					p.put(classpathFilename, p);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return p;
	}

}
