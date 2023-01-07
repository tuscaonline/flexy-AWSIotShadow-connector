package net.socometra.ewon;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;


import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import junit.framework.TestCase;

public class ShadowTest extends TestCase {

	public TagTable getTagTable()
			throws SecurityException, NumberFormatException, IndexOutOfBoundsException, IOException {
		FileInputStream exp = new FileInputStream("$dtTL.txt");
		TagTable tags = new TagTable();
		Parsers.configValueParser(exp, tags, "A", "A");

		assertEquals(8, tags.get("DwordPoint").getTagId());
		assertEquals(11, tags.get("StringPoint").getTagId());

		FileInputStream instantExp = new FileInputStream("$dtIV$ftTA.txt");

		Parsers.instantValueParser(instantExp, tags);
		return tags;
	}

	public void testJsonSimpleDecode() throws ParseException {
		String shadow = "{\"state\": {\"reported\": {\"color\": \"blue\", \"test\":3, \"boolTest\":true}}}";

		Object obj = JSONValue.parseWithException(shadow);
		// System.out.println(obj);
		JSONObject jsobj = (JSONObject) obj;
		JSONObject shadowObj = (JSONObject) ((JSONObject) jsobj.get("state")).get("reported");

		assertTrue(shadowObj.get("test") instanceof java.lang.Long);
		assertTrue(shadowObj.get("color") instanceof java.lang.String);
		assertTrue(shadowObj.get("boolTest") instanceof java.lang.Boolean);

//		for (Iterator it = shadowObj.entrySet().iterator(); it.hasNext();) {
//			Map.Entry pairs = (Map.Entry) it.next();
//			System.out.println(pairs.getClass());
//			System.out.println(pairs.getKey());
//			System.out.println(pairs.getValue());
//		}

	}

	public void testJsonSimpleEncodeTagWithAlarm() {
		Tag tag = new Tag(32, "Essais", false, true, true, true, false, false, 0, true, true);
		tag.setValue(432);
		String expected = "{\"Essais\":{\"value\":432,\"alstatus\":0,\"AlType\":0}}";

		assertEquals(expected, JSONValue.toJSONString(tag.getHashMap()));
	}

	public void testJsonSimpleEncodeTagWithoutAlarm() {
		Tag tag = new Tag(32, "Essais", false, false, true, true, false, false, 0, true, true);
		tag.setValue(432);
		String expected = "{\"Essais\":432}";

		assertEquals(expected, JSONValue.toJSONString(tag.getHashMap()));
	}

	public void testShadowToJson()
			throws NumberFormatException, SecurityException, IndexOutOfBoundsException, IOException {
		assertEquals(
				"{\"state\":{\"reported\":{\"DwordPoint\":{\"value\":321579,\"alstatus\":2,\"AlType\":2},\"StringPoint\":\"Hallo E\"}}}",
				this.getTagTable().getShadow());

	}

	public void testUpdateDeltaParsing() throws ParseException {
		String shadow = "{\"version\": 6892,\"timestamp\": 1673067929,\"state\": {\"TRUCS\": 123},\"metadata\": {\"TRUCS\": {\"timestamp\": 1673067894}}}";
		Object obj = JSONValue.parseWithException(shadow);
		JSONObject jsobj = (JSONObject) obj;
		JSONObject shadowObj = (JSONObject) jsobj.get("state");
		assertTrue(shadowObj.get("TRUCS") instanceof java.lang.Long);

		for (Iterator it = shadowObj.entrySet().iterator(); it.hasNext();) {
			Map.Entry pairs = (Map.Entry) it.next();
			// System.out.println(pairs.getClass());
			System.out.println(pairs.getKey());
			System.out.println(pairs.getValue());

		}

	}

	public void testParseDelta() throws ParseException {
		String deltaShadow = "{\r\n"
				+ "  \"version\": 6968,\r\n"
				+ "  \"timestamp\": 1673082427,\r\n"
				+ "  \"state\": {\r\n"
				+ "    \"TRUCS\": 1334777,\r\n"
				+ "    \"BoolPointAlLvl1\": {\r\n"
				+ "      \"value\": true\r\n"
				+ "    },\r\n"
				+ "    \"DwordPoint\": {\r\n"
				+ "      \"value\": 123\r\n"
				+ "    },\r\n"
				+ "    \"FloatPoint\": {\r\n"
				+ "      \"value\": 1.5\r\n"
				+ "    },\r\n"
				+ "    \"BoolPointAlLvl0\": {\r\n"
				+ "      \"value\": true\r\n"
				+ "    }\r\n"
				+ "  },\r\n"
				+ "  \"metadata\": {\r\n"
				+ "    \"TRUCS\": {\r\n"
				+ "      \"timestamp\": 1673082427\r\n"
				+ "    },\r\n"
				+ "    \"BoolPointAlLvl1\": {\r\n"
				+ "      \"value\": {\r\n"
				+ "        \"timestamp\": 1673082427\r\n"
				+ "      }\r\n"
				+ "    },\r\n"
				+ "    \"DwordPoint\": {\r\n"
				+ "      \"value\": {\r\n"
				+ "        \"timestamp\": 1673082427\r\n"
				+ "      }\r\n"
				+ "    },\r\n"
				+ "    \"FloatPoint\": {\r\n"
				+ "      \"value\": {\r\n"
				+ "        \"timestamp\": 1673082427\r\n"
				+ "      }\r\n"
				+ "    },\r\n"
				+ "    \"BoolPointAlLvl0\": {\r\n"
				+ "      \"value\": {\r\n"
				+ "        \"timestamp\": 1673082427\r\n"
				+ "      }\r\n"
				+ "    }\r\n"
				+ "  }\r\n"
				+ "}";
		Object obj = JSONValue.parseWithException(deltaShadow);
		JSONObject jsobj = (JSONObject) obj;
		JSONObject shadowObj = (JSONObject) jsobj.get("state");

		for (Iterator it = shadowObj.entrySet().iterator(); it.hasNext();) {
			Map.Entry element = (Map.Entry) it.next();
		if (element.getValue() instanceof JSONObject) {
				JSONObject new_name = (JSONObject) element.getValue();
				if(new_name.get("value")!=null) {
					System.out.println(element.getKey() + " Val : "+ new_name.get("value"));
				}
				
			} else
			{
				System.out.println(element.getKey() + " Val : "+ element.getValue());
			}
	
			
		}
				
	}
}
