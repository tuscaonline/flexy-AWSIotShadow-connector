package net.socometra.ewon;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Parsers {
	public static void instantValueParser(InputStream stream, TagTable tagtable)
			throws NumberFormatException, IndexOutOfBoundsException, IOException {
		// Vector tags = new Vector();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String ligne;
		int lineNumber = 0;
		while ((ligne = reader.readLine()) != null) {
			if (lineNumber > 0) {
				StringTokenizer rowTokenizer = getStringTokenizerStrict(ligne, ";", true);
				int columnNumber = 0;

				String tagName = "";
				String value = "";
				int AlStatus = 0;
				int AlType = 0;
				int quality = 0;

				while (rowTokenizer.hasMoreTokens()) {
					String token = rowTokenizer.nextToken();
					switch (columnNumber) {
//					case 0:
//						tagId = Integer.parseInt(token);
//						break;
					case 1:
						tagName = token.substring(1, token.length() - 1);
						break;
					case 2:
						value = token;
						break;
					case 3:
						AlStatus = Integer.parseInt(token);
						break;
					case 4:
						AlType = Integer.parseInt(token);
						break;
					case 5:
						quality = Integer.parseInt(token);
						break;

					default:
						break;
					}
					columnNumber++;
				}
				
				Tag tag = tagtable.get(tagName);

				// pose un probleme avec le type string

				tag.setAlStatus(AlStatus);
				tag.setAlType(AlType);
				tag.setQuality(quality);
				switch (tag.getType()) {
				case Tag.DATATYPE_BOOLEAN:
					tag.setValue(value.equalsIgnoreCase("1"));
					break;
				case Tag.DATATYPE_FLOAT32:
					tag.setValue(Float.parseFloat(value));

					break;
				case Tag.DATATYPE_INT32:
					tag.setValue(Integer.parseInt(value));
					break;
				case Tag.DATATYPE_UINT32:
					tag.setValue(Long.parseLong(value));
					break;
				case Tag.DATATYPE_STRING:
					tag.setValue(value.substring(1, value.length() - 1));
					break;

				default:
					break;
				}

			}

			lineNumber++;
		}
		reader.close();

	}

	public static void configValueParser(InputStream stream, TagTable tags, String periodicGroups, String onChangeGroups)
			throws NumberFormatException, IndexOutOfBoundsException, IOException {
		// TagTable tags = new TagTable();
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String ligne;
		int lineNumber = 0;
		while ((ligne = reader.readLine()) != null) {
			if (lineNumber > 0) {

				StringTokenizer rowTokenizer = getStringTokenizerStrict(ligne, ";", true);
				int columnNumber = 0;

				int tagId = -1; // internal id of the Ewon
				String tagName = ""; // name of the tag
				boolean LogEnabled = false;
				boolean AlEnabled = false;
				boolean IVGroupA = false;
				boolean IVGroupB = false;
				boolean IVGroupC = false;
				boolean IVGroupD = false;
				int type = -1;
				boolean periodic = false;
				boolean onChange = false;
				while (rowTokenizer.hasMoreTokens()) {
					String token = rowTokenizer.nextToken();
					switch (columnNumber) {
					case 0:
						tagId = Integer.parseInt(token);
						break;
					case 1:
						tagName = token.substring(1, token.length() - 1);
						break;
					case 8:
						LogEnabled = token.equalsIgnoreCase("1");
						break;
					case 9:
						AlEnabled = token.equalsIgnoreCase("1");
						break;
					case 25:
						IVGroupA = token.equalsIgnoreCase("1");
						if (periodicGroups.toUpperCase().indexOf("A") != -1 && IVGroupA)
							periodic = true;
						if (onChangeGroups.toUpperCase().indexOf("A") != -1 && IVGroupA)
							onChange = true;
						break;
					case 26:
						IVGroupB = token.equalsIgnoreCase("1");
						if (periodicGroups.toUpperCase().indexOf("B") != -1 && IVGroupB)
							periodic = true;
						if (onChangeGroups.toUpperCase().indexOf("B") != -1 && IVGroupB)
							onChange = true;
						break;
					case 27:
						IVGroupC = token.equalsIgnoreCase("1");
						if (periodicGroups.toUpperCase().indexOf("C") != -1 && IVGroupC)
							periodic = true;
						if (onChangeGroups.toUpperCase().indexOf("C") != -1 && IVGroupC)
							onChange = true;
						break;
					case 28:
						IVGroupD = token.equalsIgnoreCase("1");
						if (periodicGroups.toUpperCase().indexOf("D") != -1 && IVGroupD)
							periodic = true;
						if (onChangeGroups.toUpperCase().indexOf("D") != -1 && IVGroupD)
							onChange = true;
						break;
					case 55:
						type = Integer.parseInt(token);
						break;
					default:
						break;
					}
					columnNumber++;
				}
				if (periodic)
					tags.put(new Tag(tagId, tagName, LogEnabled, AlEnabled, IVGroupA, IVGroupB, IVGroupC, IVGroupD,
							type, periodic, onChange));

			}

			lineNumber++;
		}
		reader.close();
		// return tags;
	}

	/**
	 * Strict implementation of StringTokenizer
	 * 
	 * @param str
	 * @param delim
	 * @param strict true = include NULL Token
	 * @return
	 */
	static StringTokenizer getStringTokenizerStrict(String str, String delim, boolean strict) {
		StringTokenizer st = new StringTokenizer(str, delim, strict);
		StringBuffer sb = new StringBuffer();

		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			if (s.equals(delim)) {
				sb.append(" ").append(delim);
			} else {
				sb.append(s).append(delim);
				if (st.hasMoreTokens())
					st.nextToken();
			}
		}
		return (new StringTokenizer(sb.toString(), delim));
	}

}
