package net.socometra.ewon;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import org.json.simple.JSONValue;

public class TagTable extends Hashtable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7158249270502083506L;

	/**
	 * Add an element in collection
	 * 
	 * @param value a Tag object
	 * @return the previous value of the specified key in this hashtable, or null if
	 *         it did not have one.
	 */
	public synchronized Tag put(Tag value) {

		return (Tag) super.put(value.getTagName(), value);

	}

	/**
	 * Get a tag by is name
	 * 
	 * @param key the name tag to be retrieve
	 * @return the tag
	 */
	public synchronized Tag get(String key) {

		return (Tag) super.get(key);
	}

	public String getShadow() {
		HashMap m3 = new HashMap();

		Enumeration _tags = this.elements();
		while (_tags.hasMoreElements()) {
			Tag tag = (Tag) _tags.nextElement();
			if (tag.isPeriodicFlush()) {
				m3.putAll(tag.getHashMap());
			}

		}

		LinkedHashMap m2 = new LinkedHashMap();
		m2.put("reported", m3);

		LinkedHashMap m1 = new LinkedHashMap();
		m1.put("state", m2);

		return JSONValue.toJSONString(m1);
	}
}
