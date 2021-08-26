function square(x) {
    return x * x;
}

_.union([square(10)], [1, 2, 3], [$TSL_VERSION])
//function* generator(i) {
//  yield i;
//  yield i + 10;
//}
//
//const gen = generator(10);
//[
//    gen.next().value,
//    gen.next().value,
//    gen.next().value
//];

//import foo from "bar"

//_.union(["foo", "bar", "baz"], [JSON.stringify({a:1})])

//function square(x) {
//    return x * x;
//}
//
//export default square;
