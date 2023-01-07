package net.socometra.mqtt;

import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.ewon.ewonitf.EWException;
import com.ewon.ewonitf.MqttClient;
import com.ewon.ewonitf.MqttMessage;

import net.socometra.ewon.EwonTags;
import net.socometra.ewon.Tag;
import net.socometra.utils.Log;

public class AwsMqttClient extends MqttClient {
	String thingName;
	EwonTags ewonTags;

	public AwsMqttClient(String thingName, String Host, String port, String cafile, String certfile, String keyFile,
			boolean debug, EwonTags ewonTags) throws Exception {
		super(thingName, Host);
		this.thingName = thingName;
		this.ewonTags = ewonTags;
		try {
			this.setOption("port", port);
			if (debug)
				this.setOption("log", "1");
			this.setOption("keepalive", "60");
			this.setOption("cafile", cafile);
			this.setOption("certfile", certfile);
			this.setOption("keyfile", keyFile);
			this.setOption("PROTOCOLVERSION", "3.1.1");
			this.setOption("TLSVERSION", "tlsv1.2");
		} catch (Exception e) {
			Log.error("MQTT Config ERROR: " + e.getMessage());
			throw e;

		}
		try {
			this.connect();
			this.subscribe("$aws/things/" + this.thingName + "/shadow/update/delta", 0);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			Log.error("MQTT Connect failed :" + e1.getMessage());
		}

	}

	public void callMqttEvent(int event) {

		switch (event) {
		case 0:
			try {

				MqttMessage msg;
				msg = super.readMessage();
				if (msg != null) {
					String str = new String(msg.getPayload(), "UTF-8");
//					Log.info(str);
					Object obj = JSONValue.parseWithException(str);
					JSONObject jsobj = (JSONObject) obj;
					JSONObject shadowObj = (JSONObject) jsobj.get("state");

					for (Iterator it = shadowObj.entrySet().iterator(); it.hasNext();) {
						String tagName = "";
						Object tagValue = null;

						Map.Entry element = (Map.Entry) it.next();
						tagName = (String) element.getKey();
						if (element.getValue() instanceof JSONObject) {

							JSONObject descendant = (JSONObject) element.getValue();
							if (descendant.get("value") != null) {

								tagValue = descendant.get("value");
							}

						} else {

							tagValue = element.getValue();
						}

						try {
							ewonTags.setTagValue(tagName, tagValue);
						} catch (Exception e) {
							Log.error("Error update Tag Value : " + tagName + " Err : " + e.toString());
						}
					}
				}

			} catch (Exception e) {
				Log.error("Error callMqttEvent: " + e.toString());
			}
			break;

		default:
			Log.error("CallMqttEvent is called with : " + event);
			break;
		}

	}

	public void publish(String payload) throws Exception {
		MqttMessage msg = new MqttMessage("$aws/things/" + this.thingName + "/shadow/update", payload);
		try {
			super.publish(msg, 0, false);
		} catch (Exception e) {
			Log.error("Publish failed : " + e.getMessage());
			throw e;

		}
	}

	public void publishShadow() throws Exception {
		this.ewonTags.updateInstantValue();
		MqttMessage msg = new MqttMessage("$aws/things/" + this.thingName + "/shadow/update", ewonTags.getShadow());

		super.publish(msg, 0, false);
	}

	public void publishTag(Tag tag) throws EWException {
		if (tag.isOnChangeFlush()) {

			MqttMessage msg = new MqttMessage("$aws/things/" + this.thingName + "/shadow/update", tag.getShadow());

			super.publish(msg, 0, false);

		}

	}

}
