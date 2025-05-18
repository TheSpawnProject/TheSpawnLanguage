// $antlr-format columnLimit 200
// $antlr-format maxEmptyLinesToKeep 1
// $antlr-format reflowComments false
// $antlr-format alignTrailingComments true

lexer grammar TSLLexer;

options {
	caseInsensitive = true;
}

tokens {
	EXPRESSION
}

// ---------------------
// Comments
// ---------------------

SINGLELINE_COMMENT: '#' ~[!*] ~[\r\n\u2028\u2029]* -> channel(HIDDEN);
MULTILINE_COMMENT: '#*' .*? '*#' -> channel(HIDDEN);
TSLDOC_COMMENT: '#**' .*? '*#';

EMPTY_LINES: RN HS* (RN HS*)+;
NEW_LINE: RN -> skip;
HORIZONTAL_SPACES: HS+ -> skip;

// ---------------------
// Root Keywords & Symbols
// ---------------------

KEYWORD_ON: 'ON';
KEYWORD_FROM: 'FROM';
KEYWORD_WITH: 'WITH';
KEYWORD_DISPLAYING: 'DISPLAYING';
KEYWORD_YIELDS: 'YIELDS';

SYMBOL_EQUALS: '=';
SYMBOL_GT: '>';
SYMBOL_GTE: '>=';
SYMBOL_LT: '<';
SYMBOL_LTE: '<=';
SYMBOL_DIRECTIVE: '#!';
SYMBOL_ASTERIKS: '*';

// ---------------------
// Root Context
// ---------------------

fragment DIGIT: [0-9];
fragment ALPHA: [A-Z];
fragment ALPHANUMERIC: DIGIT | ALPHA;
fragment IDENTIFIER_CHAR: ALPHANUMERIC | [_:];
fragment HS: [ \t];
fragment RN: '\r'? '\n';

BEGIN_EXPRESSION: '${' -> pushMode(JS_SCOPE), more;
BEGIN_GROUP: '%' -> pushMode(GROUP_SCOPE);
END_GROUP_EXPRESSION: '|' -> popMode;

// {{ x }}
PLACEHOLDER: '{{' IDENTIFIER '}}';

// $test_1 | $foo
CAPTURE_IDENTIFIER: '$' IDENTIFIER_CHAR+;

// TEST_1 | FOO
IDENTIFIER: ALPHA IDENTIFIER_CHAR+;

// 99 | TEST_1 | FOO | ~
PLAIN_WORD: IDENTIFIER_CHAR+ | '~';

SIGN_AT: '@';
SIGN_DOLLAR: '$';
SIGN_LPARAN: '(';
SIGN_RPARAN: ')';
SIGN_COMMA: ',';
PUNCTUATION: [.,!?;:-];

// ---------------------
// Group Context
// ---------------------

mode GROUP_SCOPE;
ESCAPED_BACKSLASH: '\\\\' -> type(GROUP_STRING);
ESCAPED_END_GROUP: '\\%' -> type(GROUP_STRING);
ESCAPED_GROUP_EXPR: '\\|' -> type(GROUP_STRING);

BEGIN_GROUP_EXPRESSION: '|' -> pushMode(DEFAULT_MODE);
END_GROUP: '%' -> popMode;

GROUP_STRING: .;

// ---------------------
// Javascript Context
// ---------------------

fragment SINGLE_QUOTE_STRING: '\'' ('\\\'' | ~'\'')* '\'';
fragment DOUBLE_QUOTE_STRING: '"' ('\\"' | ~'"')* '"';
fragment ESC_TEMPLATE_STRING: '\\`';
fragment ESC_TEMPLATE_EXPR: '\\${';

// ${...}
mode JS_SCOPE;
JS_CONTENT_STRING: (SINGLE_QUOTE_STRING | DOUBLE_QUOTE_STRING) -> more;
END_EXPRESSION: '}' -> popMode, type(EXPRESSION);
BEGIN_STRING_TEMPLATE: '`' -> pushMode(JS_TEMPLATE_STRING), more;
BEGIN_OBJECT: '{' -> pushMode(JS_OBJECT), more;
JS_CONTENT: . -> more;

// `...`
mode JS_TEMPLATE_STRING;
JS_CONTENT_STRING2: (ESC_TEMPLATE_STRING | ESC_TEMPLATE_EXPR) -> more;
END_TEMPLATE_STRING: '`' -> popMode, more;
BEGIN_TEMPLATE_EXPR: '${' -> pushMode(JS_TEMPLATE_EXPR), more;
JS_CONTENT2: . -> more;

// `.. ${...} ..`
mode JS_TEMPLATE_EXPR;
JS_CONTENT_STRING3: (SINGLE_QUOTE_STRING | DOUBLE_QUOTE_STRING) -> more;
END_TEMPLATE_EXPR: '}' -> popMode, more;
BEGIN_STRING_TEMPLATE3: '`' -> pushMode(JS_TEMPLATE_STRING), more;
BEGIN_OBJECT3: '{' -> pushMode(JS_OBJECT), more;
JS_CONTENT3: . -> more;

// { "...": "..." }
mode JS_OBJECT;
JS_CONTENT_STRING4: (SINGLE_QUOTE_STRING | DOUBLE_QUOTE_STRING) -> more;
END_OBJECT: '}' -> popMode, more;
BEGIN_STRING_TEMPLATE4: '`' -> pushMode(JS_TEMPLATE_STRING), more;
BEGIN_OBJECT4: '{' -> pushMode(JS_OBJECT), more;
JS_CONTENT4: . -> more;

// TODO:
// .. /.../ ..
// mode JS_REGEX;
