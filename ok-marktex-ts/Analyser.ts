import { Book, Part, Piece } from './Book'
import { Option } from './MarkTeX'

export class Analyser {

	analyse = function (mtx: string, option: Option = new Option()): Book {
		//	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-Defined variables.
		const book = new Book()

		const MAX_LEVEL = 6
		const LENGTH = mtx.length
		let index = 0
		let c: string
		let text = '' // Text buffer.
		let status: Status = Status.NEWLINE

		let levelCounter = 0
		let blankCounter = 0
		let linePosition = 0

		let headerIdsBuffer = [0, 0, 0, 0, 0, 0]
		let listIdsBuffer = [0, 0, 0, 0, 0, 0]

		let part = new Part()
		let piece = new Piece()

		let isLineEmpty = false

		//	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-Defined functions.
		function matchHeader() {
			for (let i = index; i < LENGTH; i++) {
				c = mtx[i]
				switch (c) {
					case '#':
						if (levelCounter < MAX_LEVEL) {
							levelCounter++
						} else {
							text += c
						}

						break
					case '*':
						text += c
						break
					case ' ': // Blank and Tab between ID and header text will be ignored.
					case '\t':
						break
					default: // Append to text.
						text += c
						break
					case '\n':
						break

				}

			}
		}

		/**
		 * Match title, if succeed, move index to next line begin.
		 * Otherwise match it as plain text.
		 */
		function matchTitle() {
			for (let i = index; i < LENGTH; i++) {
				c = mtx[i]
				switch (c) {
					case '!':
						if (levelCounter < MAX_LEVEL) {
							levelCounter++
						}
						break
					case ' ':

					case '\t': // Ignore Blanks and Tabs before text.
						break
					case '\n':
						if (text.length === 0) {
							// This title is invalid.

						}
						break
					default:


				}
			}
		}

		function matchStyledText() {

		}

		function newPiece() {
			piece.text = text.toString()
		}

		//	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	-	Logical processing.
		for (; index < LENGTH; index++) {
			c = mtx[index]

			if (c === '\\') { // Append the char after \\ directly.
				//TODO Solve numerical escaping.
				if (index + 1 < LENGTH) {
					text += mtx[++index]
					if (status === Status.NEWLINE) {
						status = Status.TEXT
					}
				} else { // If there is no 
					text += c
				}
			}

			switch (status) {
				case Status.NEWLINE:
					linePosition = index // Record the linePosition
					switch (c) {
						case '!':
							status = Status.TITLE
							break
						case '#':
							status = Status.HEADER
							break
						case '\t':
							levelCounter++
							break
						case ' ':
							blankCounter++
							if (0 === blankCounter % 4) {
								levelCounter++
							}
							break
					}
					break
				case Status.TITLE:
					if (text) {
						switch (c) {
							case '!':
								if(levelCounter < MAX_LEVEL)
								levelCounter++
								break
							case ' ':
							case '\t':
								// Ignore Blanks and TAB between title leading chars and text.
								if (text.length > 0) {
									text += c
								}
								break
							case '\n':
								// End of header.

								break
							default:
								// Append text.
								text += c
								


						}
					} else {


					}
					break
				case Status.HEADER:
					switch (c) {
						case '#':
							break
					}
					break
				case Status.TEXT:
					switch (c) {
						case '{': // Could be styled text.
							matchStyledText()
							break
						case '\n': // Match BR or P.

						case '\t': // Match TAB.

						case ' ': // Match blanks.

					}
			}
		}

		return book
	}
}

enum Status {
	NEWLINE, TEXT, TITLE, HEADER, LIST
}