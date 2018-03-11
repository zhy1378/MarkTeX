package ok.open.marktex;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Hans ZHANG, OoKai Tech.
 * @version 1.0.0
 * @since 2018-03-06 21:36
 */
public class Analyser {


	public static final int MAX_HEADER_LEVEL = 6;
	public static final String[] HEADERS = new String[]{"h1", "h2", "h3", "h4", "h5", "h6"};

	/**
	 * Analyse the mark down String, convert it to HTML.
	 *
	 * @param mdStr MarcDawn string.
	 * @return
	 * @TODO 考虑 BOM
	 * @TODO 考虑复杂编码
	 */
	public String convert(String mdStr) {
		StringBuilder html = new StringBuilder();
		StringBuilder buffer = new StringBuilder();
		StringBuilder indexBuffer = new StringBuilder();

		int[] headers = new int[MAX_HEADER_LEVEL];
		int headerLevel = 0;
		int length = mdStr.length();
		int nLineBreakers = 0;
		int nUnorderedHeader = 0;
		boolean lineBreaker = false;
		boolean isLineBreakerR = false;
		boolean newLine = true;
		boolean escape = false;
		boolean sourceCode = false;

		char c, c1; // c = current char, c1 = buffer.

		MarkType type = MarkType.PLAIN_TEXT;

		Map<String, String> index = new HashMap<>();
		StringBuilder headerIndex = new StringBuilder();

		for (int i = 0; i < length; i++) {
			c = mdStr.charAt(i); // Current char

			switch (c) {
				case '\\': // Enable the escape char.
					if (i < length - 1) {
						i++;
						c = mdStr.charAt(i);
						switch (c) {
							case '#':
							case '\\':
							case '<':
							case '>':
								buffer.append(c);
							case '\r':
							case '\n':
								continue;
						}
					} else {
						break; // End of the data.
					}

				case '#':
					// # works only if this is new line.
					if (newLine) {
						type = MarkType.HEADER;
						headerLevel++;

						LOOP:
						while (i++ < length) {
							c = mdStr.charAt(i); // Get next char.

							switch (c) {
								case '#':
									headerLevel++; // Count the #.
									continue;
								case '!': // Unordered header
									if (i + 1 < length && mdStr.charAt(i + 1) == ' ') {
										type = MarkType.UNORDERED_HEADER;
									}
									break LOOP;
								case ' ': // End of the command.
									if (headerLevel > MAX_HEADER_LEVEL) {
										headerLevel = MAX_HEADER_LEVEL;
									}
									break LOOP;
								case '0': // Ordered header with specified number
								case '1':
								case '2':
								case '3':
								case '4':
								case '5':
								case '6':
								case '7':
								case '8':
								case '9':
									break;
								default:
									break;

							}

						}

						if (type == MarkType.UNORDERED_HEADER || headerLevel > 0) {
							while (i < length) {
								c = mdStr.charAt(i);
								if (c != '\r' && c != '\n')
									buffer.append(c);
								i++;
							}
						}

						if (headerLevel > 0) { // This is a header.
						}
					} else {
						buffer.append(c);
					}
					break;

				case '>': // block quotes.
					if (newLine) {

					} else {
						buffer.append(c);
					}
					break;

				case '\t': // Could be list.
					break;

				case '{': // Source code begin.

					break;

				case '}': // Source code end.
					if (sourceCode) {
						sourceCode = false;
					} else {
						buffer.append(c);
					}
					break;
				case '*':
				case '+':
				case '-':
				case '=':
					// Unordered list.
					if (newLine) {

					}
					break;

				case '\r':
				case '\n':
					// Line breaker, count empty lines.
					do {
						switch (c) {
							case '\r':
								nLineBreakers++;
								isLineBreakerR = true;
								continue;
							case '\n':
								if (isLineBreakerR) {
									// If a NewLine follows CarriageReturn, then there's only one LB.
								} else {
									nLineBreakers++;
									isLineBreakerR = false;
								}
								continue;
							default:
								break;
						}
					} while (i + 1 < length);
					// Trigger convert.

			}

			if (escape) { // Escape char enabled.
				switch (c) {
					case '\\':
					case '#':
				}
			} else { // Escape char disabled.

			}

			if (newLine) {
				// 处理行首命令
				switch (c) {
					case '#': // Header lines
//						header++;
						continue;
					case '>': // Quote lines
						break;
//							case '<': // 与上一行合并
//								break;
//							case '\\': // 转义字符
//								break;
//
//							default: // 默认处理


				}
			} else {
				// 非行首命令
			}

			if (lineBreaker) {
				switch (type) {
					case PLAIN_TEXT:
						html.append("<p>").append(buffer).append("</p>");
						break;

					case ORDERED_HEADER:
					case UNORDERED_HEADER:
						html.append('<').append(HEADERS[headerLevel - 1]).append('>');

						if (type == MarkType.ORDERED_HEADER) {
							html.append("<a name='UH>").append('*').append("</a>");
							headerIndex.delete(0, headerIndex.length());
							headerIndex.append(headers[0]);
							for (int j = 1; j < headerLevel; j++) {
								headerIndex.append('.').append(headers[j]);
							}

							index.put(headerIndex.toString(), buffer.toString());
						} else {
							html.append("<a name='UH>").append('*').append("</a>");
							nUnorderedHeader++;
							index.put("UH" + nUnorderedHeader, buffer.toString());
						}
						html.append(buffer);
						html.append('<').append('/').append(HEADERS[headerLevel - 1]).append('>');
						break;
				}
			}
		}


		return "";
	}

	public static String convert(File file) {
		StringBuilder html = new StringBuilder();
		StringBuilder md = new StringBuilder();
		StringBuilder lineBuffer = new StringBuilder();
		int[] headers = new int[25];
		FileInputStream stream;

		try (Scanner scanner = new Scanner(file)) {

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				int length = line.length();
				int header = -1;

				for (int i = 0; i < length; i++) {
					char c = line.charAt(i); // Current char
					if (i == 0) {
						// First char
						switch (c) {
							case '#': // Header lines
								header++;
								continue;
							case '>': // Quote lines
								break;
//							case '<': // 与上一行合并
//								break;
//							case '\\': // 转义字符
//								break;
//
//							default: // 默认处理


						}
					}

					if (header >= 0) {
						switch (c) {
							case '#': // Header lines
								header++;
								continue;
							case ' ': // End of header level count
								break;
							case '!': // Unordered header
								break;

							case '(':
								break;
							case '[':
								break;
							case '{':
								break;
						}
					}
				}


				md.append(line);
			}

			scanner.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return md.toString();
	}

//	public StringBuilder analyse(Scanner scanner) {
//
//	}

	public String analyse(String md) {
		StringBuilder html = new StringBuilder();

		int length = md.length();
		int headerLevel = 0;
		char c;

		boolean newLine = true;

		MarkType type = MarkType.PLAIN_TEXT;

		for (int i = 0; i < length; i++) {
			c = md.charAt(i);

			switch (c) {
				case '#':
					if (newLine) {
						type = MarkType.HEADER;
						newLine = false;
						break;
					} else if (type == MarkType.HEADER) {
						headerLevel++;
						break;
					}
			}
		}

		return html.toString();
	}

	public boolean isBlank(char c) {
		return c == ' ' || c == '\r' || c == '\n';
	}

	public boolean isLineBreaker(char c) {
		return c == '\r' || c == '\n';
	}

	public int toNextNonEmptyLine(String mdStr, char begin) {
		return 0;
	}

	public boolean isEmptyLine(String line) {
		boolean empty = true;
		char c;

		LOOP:
		for (int i = 0; i < line.length(); i++) {
			c = line.charAt(i);
			switch (c) {
				case '\r':
				case '\n':
				case ' ':
				case '\t':
					continue;
				default:
					empty = false;
					break LOOP;
			}
		}

		return empty;
	}
}
