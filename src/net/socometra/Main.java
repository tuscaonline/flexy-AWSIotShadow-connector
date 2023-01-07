package net.socometra;

import com.ewon.ewonitf.DefaultEventHandler;

import net.socometra.ioserver.AWSIOTServer;

import net.socometra.utils.Log;

public class Main {
	public static void main(String[] args)  throws Exception {
	

		AWSIOTServer ioServer = new AWSIOTServer();
		
		// Register IO Server
		ioServer.registerServer();
		
		Thread ioServerThread = new Thread(ioServer);
		// Start IO Server
		ioServerThread.start();
		Log.info("MQTT To AWS Iot v0.1 Started");

		// a mettre en derniere ligne.
		DefaultEventHandler.runEventManager();

		
	}

}
