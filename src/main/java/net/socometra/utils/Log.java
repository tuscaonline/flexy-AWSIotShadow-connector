package net.socometra.utils;

import com.ewon.ewonitf.EventManager;

public class Log {

	static String facilityName = "AWIOT";

	public static void error(String message) {
		EventManager.logEvent("[" + facilityName + " ERR] " + message,0);
		EventManager.logRTEvent("[" + facilityName + " ERR] " + message);
	}

	public static void warning(String message) {
		EventManager.logEvent("[" + facilityName + " WARN] " + message, -1);
		EventManager.logRTEvent("[" + facilityName + " WARN] " + message);
	}

	public static void info(String message) {
		EventManager.logEvent("[" + facilityName + " INFO] " + message, 99);
		EventManager.logRTEvent("[" + facilityName + " INFO] " + message);
	}

	public static void debug(String message) {
		EventManager.logRTEvent("[" + facilityName + " DEBUG] " + message);
	}
}
