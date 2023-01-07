package net.socometra.ewon;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.json.simple.JSONValue;

public class Tag {

	public static final int ALARM_STATUS_NONE = 0;
	public static final int ALARM_STATUS_ALM = 2;
	public static final int ALARM_STATUS_ACK = 3;
	public static final int ALARM_STATUS_RTN = 4;

	public static final int ALARM_TYPE_NONE = 0;
	public static final int ALARM_TYPE_HIGH = 1;
	public static final int ALARM_TYPE_LOW = 2;
	public static final int ALARM_TYPE_LEVEL = 3;
	public static final int ALARM_TYPE_HIGH_HIGH = 4;
	public static final int ALARM_TYPE_LOW_LOW = 5;
	
	public static final byte BY_NDX = 0;
	public static final byte BY_ID = 1;

	public static final int DATATYPE_BOOLEAN = 0;
	public static final int DATATYPE_FLOAT32 = 1;
	public static final int DATATYPE_INT32 = 2;
	public static final int DATATYPE_UINT32 = 3;
	public static final int DATATYPE_STRING = 6;

	private boolean AlEnabled; // Tag as Alarm
	// property from $dtIV
	private int AlStatus; // Alarm Status
	private int AlType; // Alarm Type
	private boolean IVGroupA; // Tag is in group A
	private boolean IVGroupB; // Tag is in group B

	private boolean IVGroupC; // Tag is in group C
	private boolean IVGroupD; // Tag is in group D
	private boolean LogEnabled; // Tag is Log Enabled
	private boolean onChangeFlush; // Tag is Log Enabled
	private boolean periodicFlush; // Tag is Log Enabled
	private int quality; // Quality of tag (opc related)
	// Common property
	private int tagId; // internal id of the Ewon
	private String tagName; // name of the tag
	private int type; // type of the tag
	private Object value; // Value of the tag

	/**
	 * Create Tag from config file
	 * 
	 * @param tagId
	 * @param tagName
	 * @param LogEnabled
	 * @param AlEnabled
	 * @param AlBool
	 * @param IVGroupA
	 * @param IVGroupB
	 * @param IVGroupC
	 * @param IVGroupD
	 * @param type
	 * @param periodicFlush
	 * @param onChangeFlush
	 * 
	 */
	public Tag(int tagId, String tagName, boolean LogEnabled, boolean AlEnabled, boolean IVGroupA, boolean IVGroupB,
			boolean IVGroupC, boolean IVGroupD, int type, boolean periodicFlush, boolean onChangeFlush) {

		this.tagId = tagId;
		this.tagName = tagName;
		this.LogEnabled = LogEnabled;
		this.AlEnabled = AlEnabled;
		this.IVGroupA = IVGroupA;
		this.IVGroupB = IVGroupB;
		this.IVGroupC = IVGroupC;
		this.IVGroupD = IVGroupD;
		this.type = type;
		this.setValue(0);
		this.setAlStatus(0);
		this.setAlType(0);
		this.quality = 0;
		this.periodicFlush = periodicFlush;
		this.onChangeFlush = onChangeFlush;

	}

	/**
	 * Create tag from instant value
	 * 
	 * @param tagId
	 * @param tagName
	 * @param value
	 * @param AlStatus
	 * @param AlType
	 * @param quality
	 */
	public Tag(int tagId, String tagName, double value, int AlStatus, int AlType, int quality) {
		this.tagId = tagId;
		this.tagName = tagName;
		this.setValue(value);
		this.setAlStatus(AlStatus);
		this.setAlType(AlType);
		this.quality = quality;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Tag))
			return false;

		Tag tag = (Tag) obj;

		return (tag.getTagId() == this.getTagId() && tag.getTagName().equals(this.getTagName()));

	}

	public int getAlStatus() {
		return AlStatus;
	}

	public int getAlType() {
		return AlType;
	}

	public HashMap getHashMap() {
		LinkedHashMap m1 = new LinkedHashMap();
		if (this.AlEnabled) {
			m1.put("value", this.value);
			m1.put("alStatus", new Integer(this.getAlStatus()));
			m1.put("alType", new Integer(this.getAlType()));
			LinkedHashMap m2 = new LinkedHashMap();
			m2.put(this.getTagName(), m1);
			return m2;
		}

		m1.put(this.getTagName(), this.value);
		return m1;

	}

	public int getQuality() {
		return quality;
	}

	public String getShadow() {

		LinkedHashMap m2 = new LinkedHashMap();
		m2.put("reported", this.getHashMap());

		LinkedHashMap m1 = new LinkedHashMap();
		m1.put("state", m2);

		return JSONValue.toJSONString(m1);
	}

	public int getTagId() {
		return tagId;
	}

	public String getTagName() {
		return tagName;
	}

	public int getType() {
		return type;
	}

	public Object getValue() {
		return this.value;

	}

	public boolean getValueAsBoolean() {
		return ((Boolean) this.value).booleanValue();
	}

	public double getValueAsDouble() {
		return ((Double) this.value).doubleValue();
	}

	public long getValueAsDword() {
		return ((Long) this.value).longValue();
	}

	public int getValueAsInt() {
		return ((Integer) this.value).intValue();
	}

	public String getValueAsString() {
		return ((String) this.value);
	}

	public int hashCode() {
		return getTagId();

	}

	public boolean isAlEnabled() {
		return AlEnabled;
	}

	public boolean isIVGroupA() {
		return IVGroupA;
	}

	public boolean isIVGroupB() {
		return IVGroupB;
	}

	public boolean isIVGroupC() {
		return IVGroupC;
	}

	public boolean isIVGroupD() {
		return IVGroupD;
	}

	public boolean isLogEnabled() {
		return LogEnabled;
	}

	public boolean isOnChangeFlush() {
		return onChangeFlush;
	}

	public boolean isPeriodicFlush() {
		return periodicFlush;
	}

	public void setAlStatus(int alStatus) {
		AlStatus = alStatus;
	}

	public void setAlType(int alType) {
		AlType = alType;
	}

	public void setQuality(int quality) {
		this.quality = quality;
	}

	public void setValue(boolean value) {
		this.value = new Boolean(value);
	}

	public void setValue(double value) {
		this.value = new Double(value);
	}

	public void setValue(int value) {
		this.value = new Integer(value);
	}

	public void setValue(long value) {
		this.value = new Long(value);
	}

	public void setValue(String value) {

		this.value = value;

	}

	public String toString() {

		return "tagId:" + getTagId() + ", name:" + getTagName() + ", value:" + getValue() + ", AlStatus:"
				+ getAlStatus() + ", AlType:" + getAlType() + ", quality:" + quality;
	}
}
