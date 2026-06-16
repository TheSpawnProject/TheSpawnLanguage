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

tslRule: EMPTY_LINES* tslRuleDoc? (reactionRule | captureRule);
tslRuleDoc: (TSLDOC_COMMENT EMPTY_LINES?)+;

// ---------------------
// Directives - Platform defined metadata/preprocessors
// ---------------------

tslDirective: SYMBOL_DIRECTIVE (id = IDENTIFIER) tslDirectiveArgs (EMPTY_LINES+ | EOF);
tslDirectiveArgs: (word | KEYWORD_FROM)*;

// ---------------------
// TSL Rules - Available rule types owned by the engine
// ---------------------

reactionRule: action event;

action: actionBody ((actionYielding? actionDisplaying?) | (actionDisplaying? actionYielding?));
actionBody: actionId actionArgs;
actionId: IDENTIFIER;
actionArgs: (word | wordNest)*;
actionYielding: KEYWORD_YIELDING (consumer = CAPTURE_IDENTIFIER | EXPRESSION);
actionDisplaying: KEYWORD_DISPLAYING word;

event: KEYWORD_ON eventName (eventPredicate)*;
//event: KEYWORD_ON eventName (KEYWORD_FROM eventFrom)? (eventPredicate)*; // <-- Namespaces aren't supported in TSL1.5, they'll be introduced in TSL2.0
eventName: IDENTIFIER+;
eventFrom: IDENTIFIER;
eventPredicate: KEYWORD_WITH (predicateExpression | predicateOperation);
predicateExpression: EXPRESSION;
predicateOperation: (field = IDENTIFIER) predicateOperator predicateWord;
predicateOperator: IDENTIFIER+ | SYMBOL_EQUALS | SYMBOL_GT | SYMBOL_GTE | SYMBOL_LT | SYMBOL_LTE;

captureRule: captureHeader SYMBOL_EQUALS captureContent;
captureHeader: (id = CAPTURE_IDENTIFIER) captureParams?;
captureContent: (word | wordNest)+;
captureParams: SIGN_LPARAN (IDENTIFIER (SIGN_COMMA IDENTIFIER)*)? SIGN_RPARAN;

// ---------------------
// Structures
// ---------------------

group: BEGIN_GROUP (groupString | groupExpression)* END_GROUP;
groupString: GROUP_STRING+;
groupExpression: BEGIN_GROUP_EXPRESSION word END_GROUP_EXPRESSION;

captureCall: (id = CAPTURE_IDENTIFIER) captureArgs?;
captureArgs: SIGN_LPARAN (captureArg (SIGN_COMMA captureArg)*)? SIGN_RPARAN;
captureArg: (word | wordNest);

// ---------------------
// Atoms
// ---------------------

// TODO: Rename to tslToken (?)
word: EXPRESSION | PLAIN_WORD | IDENTIFIER | PLACEHOLDER | captureCall | group;
predicateWord: EXPRESSION | PLAIN_WORD | IDENTIFIER | group; // ?
wordNest: SIGN_LPARAN wordNestContent SIGN_RPARAN;
wordNestContent: action;