import spawnjs from "spawnjs";
import os from "spawnjs/os";
import * as library from "./myLibrary";

function* gen() {
  yield 1;
  yield 2;
  yield 3;
}

const g = gen();

// windows 100 LE 1
const platform = os.platform();
const square = library.square(10);
const endian = spawnjs.os.endianness();
export default `${platform} ${square} ${endian} ${g.next().value}`;
