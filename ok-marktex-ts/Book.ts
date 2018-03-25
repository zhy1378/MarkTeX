export class Book {
	title: string
	author: string

	parts: Array<Part> = []
}

/**
 * Part contains:
 * 1. Title
 * 2. Header
 * 3. List items
 * 4. Block quotes 引用块
 * 5. Footer cites 脚注
 * 
 * Each part contains multiple pieces.
 * 
 * 
 */
export class Part {
	type: PartType
	level = -1 // level < 0: no level is required.
	id: Array<number> // id is undefined: Part is unordered.
	pieces: Array<Piece> = []
}

/**
 * Piece contains text directly.
 */
export class Piece {
	style: TextStyle
	text: string
	/**
	 * Use BR for single break, use P for multi breaks.
	 */
	numOfBreaks = true
}

export class TextStyle {
	bold = false
	italic = false
	strong = false
	emphasized = false
	underline = false
	midline = false

	size = 0
}

export enum PartType {
	TITLE, HEADER, LIST, PRE, QUOTE, CITE
}