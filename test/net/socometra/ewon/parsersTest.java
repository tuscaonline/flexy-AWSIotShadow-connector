package net.socometra.ewon;

import java.io.FileInputStream;
import java.io.IOException;

import junit.framework.TestCase;

public class parsersTest extends TestCase {

	public void testInstantValueParser() throws SecurityException, NumberFormatException, IndexOutOfBoundsException, IOException {
		FileInputStream exp = new FileInputStream("$dtTL.txt");
		TagTable tags = new TagTable();
		Parsers.configValueParser(exp, tags, "A", "B");
		
		assertEquals(8, tags.get("DwordPoint").getTagId());
		assertEquals(11, tags.get("StringPoint").getTagId());
		
		FileInputStream instantExp = new FileInputStream("$dtIV$ftTA.txt");
	
		Parsers.instantValueParser(instantExp, tags);
		
		
		assertEquals(8, tags.get("DwordPoint").getTagId());
		assertEquals(321579, tags.get("DwordPoint").getValueAsDword(), 0);
		assertEquals("Hallo E", tags.get("StringPoint").getValueAsString());
		
	}
	
	public void testConfigParser() throws SecurityException, NumberFormatException, IndexOutOfBoundsException, IOException {
		FileInputStream exp = new FileInputStream("$dtTL.txt");
		TagTable tags = new TagTable();
		Parsers.configValueParser(exp, tags, "ABCD", "ABCD");
		
 	
		assertEquals(11,tags.get("StringPoint").getTagId());	
		assertEquals("StringPoint",tags.get("StringPoint").getTagName());	
		assertEquals(false,tags.get("StringPoint").isLogEnabled());	
		assertEquals(false,tags.get("StringPoint").isAlEnabled());	
		assertEquals(true,tags.get("StringPoint").isIVGroupA());
		assertEquals(false,tags.get("StringPoint").isIVGroupB());
		assertEquals(false,tags.get("StringPoint").isIVGroupC());
		assertEquals(false,tags.get("StringPoint").isIVGroupD());
		assertEquals(6,tags.get("StringPoint").getType());	
 
		
		assertEquals(8,tags.get("DwordPoint").getTagId());	
		assertEquals("DwordPoint",tags.get("DwordPoint").getTagName());	
		assertEquals(false,tags.get("DwordPoint").isLogEnabled());	
		assertEquals(true,tags.get("DwordPoint").isAlEnabled());	
		assertEquals(true,tags.get("DwordPoint").isIVGroupA());
		assertEquals(false,tags.get("DwordPoint").isIVGroupB());
		assertEquals(false,tags.get("DwordPoint").isIVGroupC());
		assertEquals(false,tags.get("DwordPoint").isIVGroupD());
		assertEquals(3,tags.get("DwordPoint").getType());	
		
	}
	public void testConfigParserGA() throws SecurityException, NumberFormatException, IndexOutOfBoundsException, IOException {
		FileInputStream exp = new FileInputStream("$dtTL.txt");
		TagTable table = new TagTable();
		Parsers.configValueParser(exp,table, "A", "A");
		
 	
		assertEquals(11,table.get("StringPoint").getTagId());	
		assertEquals("StringPoint",table.get("StringPoint").getTagName());	
		assertEquals(false,table.get("StringPoint").isLogEnabled());	
		assertEquals(false,table.get("StringPoint").isAlEnabled());	
		assertEquals(true,table.get("StringPoint").isIVGroupA());
		assertEquals(false,table.get("StringPoint").isIVGroupB());
		assertEquals(false,table.get("StringPoint").isIVGroupC());
		assertEquals(false,table.get("StringPoint").isIVGroupD());
		assertEquals(6,table.get("StringPoint").getType());	
 
		
		assertEquals(8,table.get("DwordPoint").getTagId());	
		assertEquals("DwordPoint",table.get("DwordPoint").getTagName());	
		assertEquals(false,table.get("DwordPoint").isLogEnabled());	
		assertEquals(true,table.get("DwordPoint").isAlEnabled());	
		assertEquals(true,table.get("DwordPoint").isIVGroupA());
		assertEquals(false,table.get("DwordPoint").isIVGroupB());
		assertEquals(false,table.get("DwordPoint").isIVGroupC());
		assertEquals(false,table.get("DwordPoint").isIVGroupD());
		assertEquals(3,table.get("DwordPoint").getType());	
		
	}
	public void testConfigParserGB() throws SecurityException, NumberFormatException, IndexOutOfBoundsException, IOException {
		FileInputStream exp = new FileInputStream("$dtTL.txt");
		TagTable table = new TagTable();
		Parsers.configValueParser(exp,table, "B", "B");
		
 	
		assertNull(table.get("StringPoint"));	
		assertNotNull(table.get("IntegerPoint"));

	}	
	public void testConfigParserGc() throws SecurityException, NumberFormatException, IndexOutOfBoundsException, IOException {
		FileInputStream exp = new FileInputStream("$dtTL.txt");
		TagTable table = new TagTable();
	  Parsers.configValueParser(exp, table, "C", "C");
		
		assertNull(table.get("StringPoint"));	
		assertNull(table.get("IntegerPoint"));
		assertNotNull(table.get("FloatPoint"));
	}
	public void testConfigParserGD() throws SecurityException, NumberFormatException, IndexOutOfBoundsException, IOException {
		FileInputStream exp = new FileInputStream("$dtTL.txt");
		TagTable table = new TagTable();
 Parsers.configValueParser(exp,table, "D", "D");
		
		assertNull(table.get("StringPoint"));	
		assertNull(table.get("IntegerPoint"));
		assertNull(table.get("FloatPoint"));
	}
	
	public void testConfigParserGAB() throws SecurityException, NumberFormatException, IndexOutOfBoundsException, IOException {
		FileInputStream exp = new FileInputStream("$dtTL.txt");
		TagTable table = new TagTable();
		 Parsers.configValueParser(exp, table, "AB", "AB");
		
		assertNotNull(table.get("StringPoint"));	
		assertNotNull(table.get("IntegerPoint"));
		assertNull(table.get("FloatPoint"));
	}
	
	
}
