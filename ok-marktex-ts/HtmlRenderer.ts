import { Book, Part, PartType } from "./Book";

export class HtmlRenderer {

	public render(book: Book): string {
		let html = '' // Final HTML output.
		let part: Part

		for (let i = 0; i < book.parts.length; i++) {
			part = book.parts[i]

			switch (part.type) {
				case PartType.TITLE:
					
					break
				case PartType.HEADER:


					break
				case PartType.CITE:
					break
				case PartType.LIST:
					break
			}
		}

		return html
	}
}