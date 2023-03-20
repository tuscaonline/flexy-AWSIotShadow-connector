package net.socometra.ewon;

import java.io.IOException;

import com.ewon.ewonitf.EWException;
import com.ewon.ewonitf.Exporter;
import com.ewon.ewonitf.TagControl;

public class EwonTags extends TagTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6847886625257989409L;

	private String periodicGroup = "";
	private String onChangeGroups = "";

	/**
	 * Tags list of the ewon
	 * 
	 * @param periodicGroup  Type "AB" to get group A and B
	 * @param onChangeGroups
	 * @throws Exception
	 */
	public EwonTags(String periodicGroup, String onChangeGroups) throws Exception {
		this.periodicGroup = periodicGroup;
		this.onChangeGroups = onChangeGroups;
		this.getConfigValue();
		this.updateInstantValue();
	}

	public void updateInstantValue() throws Exception {

		String szGroup = "";
		if (!this.periodicGroup.equals(""))
			szGroup = "$fl" + this.periodicGroup;

		Exporter exp = new Exporter("$dtIV$ftT" + szGroup);

		Parsers.instantValueParser(exp, this);

	}

	public void getConfigValue() throws IOException {

		Exporter exp = new Exporter("$dtTL");
		Parsers.configValueParser(exp, this, this.periodicGroup, this.onChangeGroups);
	}

	public void setTagValue(String name, Object value) throws EWException {
		// Tag tag = this.get(name);
		TagControl tagCtrl = new TagControl(name);
		// Log.info("Variable " + name + " Class : " + value.getClass());

		if (value instanceof Long) {
			long valint = ((Long) value).longValue();
			tagCtrl.setTagValueAsLong(valint);
		}
		if (value instanceof Boolean) {
			boolean val = ((Boolean) value).booleanValue();
			if (val)
				tagCtrl.setTagValueAsInt(1);
			else
				tagCtrl.setTagValueAsInt(0);
		}
		if (value instanceof Double) {
			double val = ((Double) value).doubleValue();
			tagCtrl.setTagValueAsDouble(val);
		}
		if (value instanceof String) {
			String val = ((String) value);
			tagCtrl.setTagValueAsString(val);
		}

	}

}
