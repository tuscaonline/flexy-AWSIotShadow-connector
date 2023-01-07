package net.socometra.ewon;

import com.ewon.ewonitf.EvtTagValueListener;

import net.socometra.mqtt.AwsMqttClient;
import net.socometra.utils.Log;

public class EwonTagValueListener extends EvtTagValueListener {

	AwsMqttClient mqttClient;
	EwonTags ewonTags;

	public EwonTagValueListener(AwsMqttClient mqttClient, EwonTags ewonTags) {
		this.mqttClient = mqttClient;
		this.ewonTags = ewonTags;

	}

	public void callTagChanged() {
		Tag tag = ewonTags.get(this.getTagName());
		// Log.info("Tag " + this.getTagName() + " has changed is onChangeFlush "+ tag.isOnChangeFlush() );

		if (tag != null && tag.isOnChangeFlush()) {
			if (this.getTagValueType() != tag.getType())
				try {
					this.ewonTags.getConfigValue();

				} catch (Exception e) {
					Log.error("Err in callTagChanged :" + e.getMessage());

				}
			try {
				this.ewonTags.updateInstantValue();
				this.mqttClient.publishTag(tag);
			} catch (Exception e) {

				Log.error("Err when publish tag :" + e.getMessage());
			}
		}

	}

}
