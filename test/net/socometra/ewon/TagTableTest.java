package net.socometra.ewon;

import junit.framework.TestCase;

public class TagTableTest extends TestCase {
	public void testAddElement() {
		TagTable tagTable = new TagTable();

		Tag tag = new Tag(213, "test", 20.0, 0, 1, 0);
		Tag tag2 = new Tag(2, "test1", 20.0, 0, 1, 0);

		tagTable.put(tag);
		tagTable.put(tag2);

		Tag tag3 = tagTable.get("test1");

		assertEquals(tag2, tag3);
	}
	
	public void testAddElementTwice() {
		TagTable tagTable = new TagTable();

		Tag tag = new Tag(213, "test", 20.0, 0, 1, 0);
		tagTable.put(tag);
		tagTable.put(tag);

		Tag tag3 = tagTable.get("test");

		assertEquals(tag, tag3);
	}
	
	
}
