package de.core23.dicewars.helper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class LanguageManager {
	private static Properties properties = null;

	public static Properties load(String language) throws IOException {
		if (properties == null) {
			properties = new Properties();
			properties.load(new InputStreamReader(LanguageManager.class.getClassLoader().getResourceAsStream("lang/" + language + ".properties"))); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return properties;
	}

	public static String getString(String key) {
		try {
			if (properties.containsKey(key))
				return properties.getProperty(key);
		} catch (Exception e) {
		}
		return '!' + key + '!';
	}
}
