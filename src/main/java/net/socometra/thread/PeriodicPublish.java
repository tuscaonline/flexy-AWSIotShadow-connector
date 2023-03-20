package net.socometra.thread;


import net.socometra.mqtt.AwsMqttClient;
import net.socometra.utils.Log;
import net.socometra.utils.Utils;

public class PeriodicPublish extends Thread {

	private volatile boolean running = true;
	AwsMqttClient mqttclient;
	int time = 10;

	public PeriodicPublish(AwsMqttClient mqttclient, int time) {
		this.mqttclient = mqttclient;
		this.time = time;
	}

	public void run() {

		Log.info("Launch periodic thread");
		while (running) {
			
			try {

				this.mqttclient.publishShadow();
			} catch (Exception e) {
				Log.error("Exception in periodic Thread : " + e.getMessage());
				break;
			}
			Utils.attend(time * 1000);
		}
		Log.info("periodic Thread exiting");

	}

	public void stopTheThread() {
	
		this.running = false;
	}

	public void start() {
		if (!this.isAlive()) {
			super.start();
		}
	}

}
