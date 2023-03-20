package net.socometra.utils;

import java.io.File;

import java.lang.Exception;
import com.ewon.ewonitf.IOServer;

public class Utils {

	public static void checkFileExist(IOServer ioserver, String parameter) throws Exception{
		String file =  ioserver.getConfigParam(parameter, "").trim();
		//Log.debug("Verifie le fichier:"+file);
		File fichier = new File(file);
		if(file.length()<1) {
			Log.error("The parameter " + parameter + " need to be more 1 car");
			throw new Exception();
		}
		if(!fichier.exists()) {
			Log.error("File "+ file + " not found" );
			throw new Exception();
		}
				
	}
 
	public static void checkStringParameter(IOServer ioserver, String parameter) throws Exception {
		String param = ioserver.getConfigParam(parameter, "").trim();
		//Log.debug("Parameter Control: " + parameter );
		if(param.length()<1) {
			Log.error("The parameter " + parameter + " need to be more 1 car");
			throw new Exception();
		}
	}
	public static void checkStringParameter(IOServer ioserver, String parameter, String defaultValue) throws Exception {
		String param = ioserver.getConfigParam(parameter, defaultValue).trim();
		//Log.debug("Parameter Control: " + parameter );
		if(param.length()<1) {
			Log.error("The parameter " + parameter + " need to be more 1 car");
			throw new Exception();
		}
	}
	public static void checkBooleanParameter(IOServer ioserver, String parameter, String defaultValue) throws Exception {
		String param = ioserver.getConfigParam(parameter, defaultValue).trim();
		String test = param.toUpperCase();
		Log.debug("Parameter Control: " + test );
		if(!(param.equalsIgnoreCase("FALSE") ||  param.equalsIgnoreCase("TRUE"))) {
			Log.error("The parameter " + parameter + " need to be more True or False");
			throw new Exception();
		}
 
	}
	
	public static void attend(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
