package net.socometra.ewon;

import junit.framework.TestCase;

public class TagTest extends TestCase {

	public void testHashCode() {
		Tag tag = new Tag(213, "test", 20.0, 0, 1, 0);
		assertEquals(213, tag.hashCode());

		
	}

	public void testTag() {
		Tag tag = new Tag(213, "test", 20.0, 0, 1, 0);
		assertEquals(213, tag.getTagId());
	}

	public void testToString() {
		Tag tag = new Tag(213, "test", 20.0, 0, 1, 0);
		assertEquals("tagId:213, name:test, value:20.0, AlStatus:0, AlType:1, quality:0", tag.toString()); 
	}

	public void testEqualsObject() {
		Tag tag = new Tag(213, "test", 20.0, 0, 1, 0);
		Tag tag2 = new Tag(213, "test", 24.0, 1, 1, 0);
		assertEquals(tag, tag2);
	}

	public void testGetTagId() {
		Tag tag = new Tag(213, "test", 20.0, 0, 1, 0);
		assertEquals(213, tag.getTagId());
		
	}

	public void testGetTagName() {
		Tag tag = new Tag(213, "test", 20.0, 0, 1, 0);
		assertEquals("test", tag.getTagName());
		
	}

	public void testGetValue() {
		Tag tag = new Tag(213, "test", 20.0, 0, 1, 0);
		assertEquals(20.0,((Double)tag.getValue()).doubleValue(), 0);
		
	}

	public void testSetValue() {
		Tag tag = new Tag(213, "test", 20.0, 0, 1, 0);
		tag.setValue(210.0);
		assertEquals(210.0,((Double)tag.getValue()).doubleValue(), 0);

	}

	public void testGetAlStatus() {
		Tag tag = new Tag(213, "test", 20.0, 0, 1, 0);
		
		assertEquals(0, tag.getAlStatus());
		
	}

	public void testSetAlStatus() {
		Tag tag = new Tag(213, "test", 20.0, 0, 1, 0);
		tag.setAlStatus(2);
		assertEquals(2, tag.getAlStatus());
	}

	public void testGetAlType() {
		Tag tag = new Tag(213, "test", 20.0, 0, 1, 0);
	
		assertEquals(1, tag.getAlType());

	}

	public void testSetAlType() {
		
		Tag tag = new Tag(213, "test", 20.0, 0, 1, 0);
		tag.setAlType(61);
		assertEquals(61, tag.getAlType());

	}

	public void testGetQuality() {
		Tag tag = new Tag(213, "test", 20.0, 0, 1, 653);

		assertEquals(653, tag.getQuality());
	}

	public void testSetQuality() {
		Tag tag = new Tag(213, "test", 20.0, 0, 1, 0);
		tag.setQuality(62);
		assertEquals(62, tag.getQuality());
	}
	
	public void testCreateTagWithConfig() {
		Tag tag = new Tag(25,"TagAvecConf", true, true, true, true, false, false,1, true, true);
		assertEquals("TagAvecConf", tag.getTagName());
	}
	
			

}
