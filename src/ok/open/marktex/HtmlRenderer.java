package ok.open.marktex;

/**
 * @author Hans ZHANG, OoKai Tech.
 * @version 1.0.0
 * @since 2018-03-06 23:50
 */
public class HtmlRenderer {


	public StringBuilder render(MarkTex markTex) {
		StringBuilder html = new StringBuilder("<html>");

		html.append("</html>");
		return html;
	}

	enum HtmlType{
		SINGLE_PAGE // With header, style,
	}
}
