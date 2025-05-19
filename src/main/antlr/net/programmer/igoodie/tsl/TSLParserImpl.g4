// $antlr-format columnLimit 200
// $antlr-format maxEmptyLinesToKeep 1
// $antlr-format reflowComments false
// $antlr-format alignTrailingComments true

parser grammar TSLParserImpl;

options {
	tokenVocab = TSLLexer;
}

// ---------------------
// Root
// ---------------------

tslWords: word* EOF;
tslRuleset: EMPTY_LINES* tslDirective* tslRules EOF;
tslRules: () | tslRule (EMPTY_LINES tslRule)*;

tslRule: tslRuleDoc? (reactionRule | captureRule);
tslRuleDoc: (TSLDOC_COMMENT EMPTY_LINES?)+;

// ---------------------
// Directives - Platform defined metadata/preprocessors
// ---------------------

tslDirective: SYMBOL_DIRECTIVE (id = IDENTIFIER) tslDirectiveArgs (EMPTY_LINES+ | EOF);
tslDirectiveArgs: (word | SYMBOL_ASTERIKS | KEYWORD_FROM)*;

// ---------------------
// TSL Rules - Available rule types owned by the engine
// ---------------------

reactionRule: action event;

action: actionBody actionYields? actionDisplaying?;
actionBody: actionId actionArgs;
actionId: IDENTIFIER;
actionArgs: (word | actionNest)*;
actionNest: SIGN_LPARAN action SIGN_RPARAN;
actionYields: KEYWORD_YIELDS (consumer = CAPTURE_IDENTIFIER | EXPRESSION);
actionDisplaying: KEYWORD_DISPLAYING word;

event: KEYWORD_ON eventName (eventPredicate)*;
eventName: IDENTIFIER+;
eventPredicate: KEYWORD_WITH (predicateExpression | predicateOperation);
predicateExpression: EXPRESSION;
predicateOperation: (field = IDENTIFIER) predicateOperator predicateWord;
predicateOperator: IDENTIFIER+ | SYMBOL_EQUALS | SYMBOL_GT | SYMBOL_GTE | SYMBOL_LT | SYMBOL_LTE;

captureRule: captureHeader SYMBOL_EQUALS actionArgs;
captureHeader: (id = CAPTURE_IDENTIFIER) captureParams?;
captureParams: SIGN_LPARAN (IDENTIFIER (SIGN_COMMA IDENTIFIER)*)? SIGN_RPARAN;

// ---------------------
// Structures
// ---------------------

group: BEGIN_GROUP (groupString | groupExpression)* END_GROUP;
groupString: GROUP_STRING+;
groupExpression: BEGIN_GROUP_EXPRESSION word END_GROUP_EXPRESSION;

captureCall: (id = CAPTURE_IDENTIFIER) captureArgs?;
captureArgs: SIGN_LPARAN (word (SIGN_COMMA word)*)? SIGN_RPARAN;

// ---------------------
// Atoms
// ---------------------

word: EXPRESSION | PLAIN_WORD | IDENTIFIER | PLACEHOLDER | captureCall | group;
predicateWord: EXPRESSION | PLAIN_WORD | IDENTIFIER | group; // ?