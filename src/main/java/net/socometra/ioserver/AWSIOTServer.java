package net.socometra.ioserver;

import com.ewon.ewonitf.DefaultEventHandler;
import com.ewon.ewonitf.IOManager;
import com.ewon.ewonitf.IOServer;
import com.ewon.ewonitf.IOTag;
import com.ewon.ewonitf.IOValue;

import net.socometra.ewon.EwonTagValueListener;
import net.socometra.ewon.EwonTags;
import net.socometra.mqtt.AwsMqttClient;
import net.socometra.thread.PeriodicPublish;
import net.socometra.utils.Log;
import net.socometra.utils.Utils;

public class AWSIOTServer extends IOServer implements Runnable {
	String thingName = "";
	String caFile = "";
	String thingPublicCert = "";
	String thingPrivateKey = "";
	String brokerAddress = "";
	String brokerPort = "";
	String periodicGroups = "ABCD";
	String onChangeGroups = "ABCD";
	boolean debug = false;
	private int mqqtStatusTemp = 0;
	boolean NewConfigFlag = false;
	boolean isConfigured = false;
	public AwsMqttClient mqttClient;
	boolean isConnected = false;
	int periodicFlushTime = 60;

	IOTag ConnectionStatus = null;

	static String Addr_ConnectionStatus = "CONNECTIONSTATUS";

	public AWSIOTServer() {
		super();

		setServerName("AWSIOT");
		setTagHelper(
				"{\"Gram\": [{\"helpid\": \"0\",\"itemList\": [{\"i\": \"1\",\"v\": \"CONNECTIONSTATUS\",}]} ],\"topic\": {"
						+ "\"A\": {\"startNodeId\": \"0\",\"gramAddrId\": \"0\"}},\"hlpTxt\": {\"0\": \"Enter the AwsIOT  Tag Address\"}}");

	}

	public void onRegister() throws Exception {

		setJavaReplyTimeout(1000000); // Let Java freeze the eWON while
		// debugging.

	}

	public void onPassConfig(boolean applyConfig, boolean checkConfig) throws Exception {
		if (checkConfig) {

			// Controle de la configuration fournis a l'io server
			Utils.checkStringParameter(this, "thingName");
			Utils.checkFileExist(this, "caFile");
			Utils.checkFileExist(this, "thingPublicCert");
			Utils.checkFileExist(this, "thingPrivateKey");
			Utils.checkStringParameter(this, "brokerAddress");
			Utils.checkStringParameter(this, "brokerPort", "8883");
			Utils.checkBooleanParameter(this, "debug", "False");
			Utils.checkStringParameter(this, "periodicGroups", "ABCD");
			Utils.checkStringParameter(this, "onChangeGroups", "ABCD");
			Integer.parseInt(this.getConfigParam("periodicFlushTime", "60").trim());
		}
		if (applyConfig) {

			this.thingName = this.getConfigParam("thingName", "").trim();
			this.caFile = this.getConfigParam("caFile", "").trim();
			this.thingPublicCert = this.getConfigParam("thingPublicCert", "").trim();
			this.thingPrivateKey = this.getConfigParam("thingPrivateKey", "").trim();
			this.brokerAddress = this.getConfigParam("brokerAddress", "").trim();
			this.brokerPort = this.getConfigParam("brokerPort", "8883").trim();
			this.periodicGroups = this.getConfigParam("periodicGroups", "ABCD").trim();
			this.onChangeGroups = this.getConfigParam("onChangeGroups", "ABCD").trim();
			this.periodicFlushTime = Integer.parseInt(this.getConfigParam("periodicFlushTime", "60").trim());
			if (this.getConfigParam("debug", "False").equalsIgnoreCase("TRUE"))
				this.debug = true;
			else
				this.debug = false;
			this.NewConfigFlag = true;
			this.isConfigured = true;
		}

	}

	public void onUnadviseIo(IOTag ioTag) throws Exception {
		// Tag has been removed from Tags list
		// Remove the tag from your IO server and monitor it
		if (ioTag.getIoName().equals(Addr_ConnectionStatus))
			ConnectionStatus = null;

	}

	public void onAdviseIo(IOTag ioTag) throws Exception {
		// A Tag has been added and validated (See onGetIoInfo() )

		if (ioTag.getIoName().equals(Addr_ConnectionStatus))
			ConnectionStatus = ioTag;

		// Store the tag internally in your IO server Tags List to monitor it
		// from the run() function
		ioTag.setQuality(IOTag.QUALITY_GOOD);
		postQuality(ioTag);

	}

	public IOTag onGetIoInfo(String topicName, String ioName) throws Exception {
		if ("".equals(ioName))
			throw new Exception("AwsIOT Tag Address cannot be empty");

		if (!ioName.equals(Addr_ConnectionStatus))
			throw new Exception("AwsIOT Tag Address unknown");

		IOTag ioTag = new IOTag(topicName, ioName, IOValue.DATATYPE_UINT32, true);

		return ioTag;
	}

	public void onPutIo(IOTag ioTag, IOValue ioData) throws Exception {
		// User writes a new value to the Tag (Web page, Modbus,...)
		Log.warning("Write operations to Tags is not supported");
	}

	public IOValue onGetIo(IOTag ioTag) throws Exception {
		// Firmware requests the value of the Tag. Typically at the Tag init or
		// when doing Historical logging based on Time interval
		return ioTag.getIoData();
	}

	public void run() {

		while (true) {
			try {
				if (!this.isConfigured) {
					Utils.attend(1000);

					continue;
				} else {
					Log.info("AWSIOTServer Thread Run and Configured");
				}
				if (this.debug)
					Log.warning("Debug is on");

				EwonTags ewonTags = new EwonTags(this.periodicGroups, this.onChangeGroups);
				this.mqttClient = new AwsMqttClient(this.thingName, this.brokerAddress, this.brokerPort, this.caFile,
						this.thingPublicCert, this.thingPrivateKey, this.debug, ewonTags);

				int nbTag = IOManager.getNbTags();
				PeriodicPublish periodicPublish = new PeriodicPublish(mqttClient, this.periodicFlushTime);
				DefaultEventHandler.setDefaultTagValueListener(new EwonTagValueListener(this.mqttClient, ewonTags));
				while (true) {
					try {
						if (nbTag != IOManager.getNbTags()) {
							Log.info("the Tag list has changed");
							break;
						}
						this.checkConnection(mqttClient);
						if (this.isConnected) {

							periodicPublish.start();

						}

					} catch (Exception e) {
						Log.info("Break in main loop : " + e.getMessage());
						break;
					}

					if (this.NewConfigFlag) {
						Log.info("New Configuration restart IoServer");
						this.NewConfigFlag = false;

						break;
					}

					Utils.attend(5000);

				}

				periodicPublish.stopTheThread();
				this.mqttClient.close();
				ewonTags = null;

			} catch (Exception e) {
				Utils.attend(10000);
				e.printStackTrace();
			}
		}

	}

	// FUNCTION to update IOServer Status Tags
	private void updateTag(IOTag Tag, long value) {

		try {
			if (Tag != null && Tag.getValueUInt32() != value) {
				Tag.setValueUInt32(value);
				this.postIoChange(Tag);
			}
		} catch (Exception ex) {
			Log.error("Cannot update Tag, " + ex.getMessage());
		}
	}

	private void checkConnection(AwsMqttClient mqttClient) throws Exception {

		int status = mqttClient.getStatus();
		if (this.mqqtStatusTemp != status) {
			switch (status) {
			case 3:

				this.isConnected = false;
				Utils.attend(1000);
				Log.info("The MQTT client is trying to connect. : " + status);
				break;
			case 4:

				this.isConnected = false;
				this.mqttClient.close();
				Utils.attend(1000);
				throw new Exception("The MQTT client is disconnected : " + status);
			case 5:
				if (!this.isConnected)
					Log.info("The MQTT client is connected.");
				this.isConnected = true;
				break;
			default:

				this.isConnected = false;
				this.mqttClient.close();
				Utils.attend(1000);

				throw new Exception("The MQTT client is unknow: " + status);

			}
			this.updateTag(ConnectionStatus, status);
		}

	}

}
