// let mtx = 'abcd'
// console.log(mtx[2])

import StringBuilder from '../MarkTeX-TS/node_modules/typescript-dotnet-umd/System/Text/StringBuilder'
import { Part } from './Book';

function testStrConcationAndStringBuilder() {
	var str2 = 'abc'
	str2 = str2.concat('cdef')
	console.log(str2)

	const LOOP = 1000000
	const STRINGS = ['a', 'bb', 'ccc', 'dddd', 'eeeee', 'ffffff', 'ggggggg']
	const LENGTH = STRINGS.length
	var sb = new StringBuilder()

	var start = Date.now()
	var str = ''

	for (let i = 0; i < LOOP; i++) {
		str = str.concat(STRINGS[i % LENGTH])
	}

	console.log((Date.now() - start) + 'concat, LENGTH: ' + str.length)

	var start = Date.now()
	str = ''

	for (let i = 0; i < LOOP; i++) {
		str += STRINGS[i % LENGTH]
	}

	console.log((Date.now() - start) + '+=, LENGTH: ' + str.length)

	start = Date.now()

	for (let i = 0; i < LOOP; i++) {
		sb.append(STRINGS[i % LENGTH])
	}

	console.log((Date.now() - start) + 'append, LENGTH: ' + sb.toString().length)
}

let part = new Part()
part.id = 1

