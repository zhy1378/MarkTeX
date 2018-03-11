package ok.open.marktex;

/**
 * @author Hans ZHANG, OoKai Tech.
 * @version 1.0.0
 * @since 2018-03-06 22:45
 */
public class MarkTex {

	private boolean isCatalogueNeeded;
	private boolean isCoverPageNeeded;
	private CitePosition citePosition = CitePosition.FOOTER;

	public StringBuilder translate(StringBuilder mtx) {
		return null;
	}

	enum CitePosition {
		AFTER_CONTENT,
		FOOTER,
		ARTICLE_END
	}
}
