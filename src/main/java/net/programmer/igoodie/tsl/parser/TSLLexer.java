// Generated from net/programmer/igoodie/tsl/TSLLexer.g4 by ANTLR 4.13.1
package net.programmer.igoodie.tsl.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class TSLLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		EXPRESSION=1, TSLDOC_COMMENT=2, MULTILINE_COMMENT=3, SINGLELINE_COMMENT=4, 
		EMPTY_LINES=5, NEW_LINE=6, HORIZONTAL_SPACES=7, KEYWORD_ON=8, KEYWORD_FROM=9, 
		KEYWORD_WITH=10, KEYWORD_DISPLAYING=11, KEYWORD_YIELDS=12, SYMBOL_EQUALS=13, 
		SYMBOL_GT=14, SYMBOL_GTE=15, SYMBOL_LT=16, SYMBOL_LTE=17, SYMBOL_DIRECTIVE=18, 
		SYMBOL_ASTERIKS=19, BEGIN_GROUP=20, END_GROUP_EXPRESSION=21, PLACEHOLDER=22, 
		CAPTURE_IDENTIFIER=23, IDENTIFIER=24, PLAIN_WORD=25, SIGN_AT=26, SIGN_DOLLAR=27, 
		SIGN_LPARAN=28, SIGN_RPARAN=29, SIGN_COMMA=30, PUNCTUATION=31, BEGIN_GROUP_EXPRESSION=32, 
		END_GROUP=33, GROUP_STRING=34, BEGIN_EXPRESSION=35, ESCAPED_BACKSLASH=36, 
		ESCAPED_END_GROUP=37, ESCAPED_GROUP_EXPR=38, END_EXPRESSION=39, BEGIN_STRING_TEMPLATE=40, 
		BEGIN_OBJECT=41;
	public static final int
		GROUP_SCOPE=1, JS_SCOPE=2, JS_TEMPLATE_STRING=3, JS_TEMPLATE_EXPR=4, JS_OBJECT=5;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE", "GROUP_SCOPE", "JS_SCOPE", "JS_TEMPLATE_STRING", "JS_TEMPLATE_EXPR", 
		"JS_OBJECT"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"TSLDOC_COMMENT", "MULTILINE_COMMENT", "SINGLELINE_COMMENT", "EMPTY_LINES", 
			"NEW_LINE", "HORIZONTAL_SPACES", "KEYWORD_ON", "KEYWORD_FROM", "KEYWORD_WITH", 
			"KEYWORD_DISPLAYING", "KEYWORD_YIELDS", "SYMBOL_EQUALS", "SYMBOL_GT", 
			"SYMBOL_GTE", "SYMBOL_LT", "SYMBOL_LTE", "SYMBOL_DIRECTIVE", "SYMBOL_ASTERIKS", 
			"DIGIT", "ALPHA", "ALPHANUMERIC", "IDENTIFIER_CHAR", "HS", "RN", "BEGIN_EXPRESSION", 
			"BEGIN_GROUP", "END_GROUP_EXPRESSION", "PLACEHOLDER", "CAPTURE_IDENTIFIER", 
			"IDENTIFIER", "PLAIN_WORD", "SIGN_AT", "SIGN_DOLLAR", "SIGN_LPARAN", 
			"SIGN_RPARAN", "SIGN_COMMA", "PUNCTUATION", "ESCAPED_BACKSLASH", "ESCAPED_END_GROUP", 
			"ESCAPED_GROUP_EXPR", "BEGIN_GROUP_EXPRESSION", "END_GROUP", "GROUP_STRING", 
			"SINGLE_QUOTE_STRING", "DOUBLE_QUOTE_STRING", "ESC_TEMPLATE_STRING", 
			"ESC_TEMPLATE_EXPR", "JS_CONTENT_STRING", "END_EXPRESSION", "BEGIN_STRING_TEMPLATE", 
			"BEGIN_OBJECT", "JS_CONTENT", "JS_CONTENT_STRING2", "END_TEMPLATE_STRING", 
			"BEGIN_TEMPLATE_EXPR", "JS_CONTENT2", "JS_CONTENT_STRING3", "END_TEMPLATE_EXPR", 
			"BEGIN_STRING_TEMPLATE3", "BEGIN_OBJECT3", "JS_CONTENT3", "JS_CONTENT_STRING4", 
			"END_OBJECT", "BEGIN_STRING_TEMPLATE4", "BEGIN_OBJECT4", "JS_CONTENT4"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, "'ON'", "'FROM'", "'WITH'", 
			"'DISPLAYING'", "'YIELDS'", "'='", "'>'", "'>='", "'<'", "'<='", "'#!'", 
			"'*'", null, null, null, null, null, null, "'@'", "'$'", "'('", "')'", 
			"','", null, null, null, null, null, "'\\\\'", "'\\%'", "'\\|'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "EXPRESSION", "TSLDOC_COMMENT", "MULTILINE_COMMENT", "SINGLELINE_COMMENT", 
			"EMPTY_LINES", "NEW_LINE", "HORIZONTAL_SPACES", "KEYWORD_ON", "KEYWORD_FROM", 
			"KEYWORD_WITH", "KEYWORD_DISPLAYING", "KEYWORD_YIELDS", "SYMBOL_EQUALS", 
			"SYMBOL_GT", "SYMBOL_GTE", "SYMBOL_LT", "SYMBOL_LTE", "SYMBOL_DIRECTIVE", 
			"SYMBOL_ASTERIKS", "BEGIN_GROUP", "END_GROUP_EXPRESSION", "PLACEHOLDER", 
			"CAPTURE_IDENTIFIER", "IDENTIFIER", "PLAIN_WORD", "SIGN_AT", "SIGN_DOLLAR", 
			"SIGN_LPARAN", "SIGN_RPARAN", "SIGN_COMMA", "PUNCTUATION", "BEGIN_GROUP_EXPRESSION", 
			"END_GROUP", "GROUP_STRING", "BEGIN_EXPRESSION", "ESCAPED_BACKSLASH", 
			"ESCAPED_END_GROUP", "ESCAPED_GROUP_EXPR", "END_EXPRESSION", "BEGIN_STRING_TEMPLATE", 
			"BEGIN_OBJECT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public TSLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "TSLLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000)\u01cb\u0006\uffff\uffff\u0006\uffff\uffff\u0006\uffff\uffff"+
		"\u0006\uffff\uffff\u0006\uffff\uffff\u0006\uffff\uffff\u0002\u0000\u0007"+
		"\u0000\u0002\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007"+
		"\u0003\u0002\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007"+
		"\u0006\u0002\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n"+
		"\u0007\n\u0002\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002"+
		"\u000e\u0007\u000e\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002"+
		"\u0011\u0007\u0011\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0002"+
		"\u0014\u0007\u0014\u0002\u0015\u0007\u0015\u0002\u0016\u0007\u0016\u0002"+
		"\u0017\u0007\u0017\u0002\u0018\u0007\u0018\u0002\u0019\u0007\u0019\u0002"+
		"\u001a\u0007\u001a\u0002\u001b\u0007\u001b\u0002\u001c\u0007\u001c\u0002"+
		"\u001d\u0007\u001d\u0002\u001e\u0007\u001e\u0002\u001f\u0007\u001f\u0002"+
		" \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002#\u0007#\u0002$\u0007$\u0002"+
		"%\u0007%\u0002&\u0007&\u0002\'\u0007\'\u0002(\u0007(\u0002)\u0007)\u0002"+
		"*\u0007*\u0002+\u0007+\u0002,\u0007,\u0002-\u0007-\u0002.\u0007.\u0002"+
		"/\u0007/\u00020\u00070\u00021\u00071\u00022\u00072\u00023\u00073\u0002"+
		"4\u00074\u00025\u00075\u00026\u00076\u00027\u00077\u00028\u00078\u0002"+
		"9\u00079\u0002:\u0007:\u0002;\u0007;\u0002<\u0007<\u0002=\u0007=\u0002"+
		">\u0007>\u0002?\u0007?\u0002@\u0007@\u0002A\u0007A\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0005\u0000\u0090\b\u0000\n\u0000"+
		"\f\u0000\u0093\t\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0005\u0001\u009c\b\u0001\n\u0001"+
		"\f\u0001\u009f\t\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0005\u0002\u00a9\b\u0002"+
		"\n\u0002\f\u0002\u00ac\t\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001"+
		"\u0003\u0004\u0003\u00b2\b\u0003\u000b\u0003\f\u0003\u00b3\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0004\u0005\u00bb\b\u0005"+
		"\u000b\u0005\f\u0005\u00bc\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001"+
		"\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0011"+
		"\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0014"+
		"\u0001\u0014\u0003\u0014\u00f7\b\u0014\u0001\u0015\u0001\u0015\u0003\u0015"+
		"\u00fb\b\u0015\u0001\u0016\u0001\u0016\u0001\u0017\u0003\u0017\u0100\b"+
		"\u0017\u0001\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001"+
		"\u0018\u0001\u0018\u0001\u0018\u0001\u0019\u0001\u0019\u0001\u0019\u0001"+
		"\u0019\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001\u001b\u0001"+
		"\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b\u0001"+
		"\u001c\u0001\u001c\u0004\u001c\u011b\b\u001c\u000b\u001c\f\u001c\u011c"+
		"\u0001\u001d\u0001\u001d\u0004\u001d\u0121\b\u001d\u000b\u001d\f\u001d"+
		"\u0122\u0001\u001e\u0004\u001e\u0126\b\u001e\u000b\u001e\f\u001e\u0127"+
		"\u0001\u001f\u0001\u001f\u0001 \u0001 \u0001!\u0001!\u0001\"\u0001\"\u0001"+
		"#\u0001#\u0001$\u0001$\u0001%\u0001%\u0001%\u0001%\u0001%\u0001&\u0001"+
		"&\u0001&\u0001&\u0001&\u0001\'\u0001\'\u0001\'\u0001\'\u0001\'\u0001("+
		"\u0001(\u0001(\u0001(\u0001)\u0001)\u0001)\u0001)\u0001*\u0001*\u0001"+
		"+\u0001+\u0001+\u0001+\u0005+\u0153\b+\n+\f+\u0156\t+\u0001+\u0001+\u0001"+
		",\u0001,\u0001,\u0001,\u0005,\u015e\b,\n,\f,\u0161\t,\u0001,\u0001,\u0001"+
		"-\u0001-\u0001-\u0001.\u0001.\u0001.\u0001.\u0001/\u0001/\u0003/\u016e"+
		"\b/\u0001/\u0001/\u00010\u00010\u00010\u00010\u00010\u00011\u00011\u0001"+
		"1\u00011\u00011\u00012\u00012\u00012\u00012\u00012\u00013\u00013\u0001"+
		"3\u00013\u00014\u00014\u00034\u0187\b4\u00014\u00014\u00015\u00015\u0001"+
		"5\u00015\u00015\u00016\u00016\u00016\u00016\u00016\u00016\u00017\u0001"+
		"7\u00017\u00017\u00018\u00018\u00038\u019c\b8\u00018\u00018\u00019\u0001"+
		"9\u00019\u00019\u00019\u0001:\u0001:\u0001:\u0001:\u0001:\u0001;\u0001"+
		";\u0001;\u0001;\u0001;\u0001<\u0001<\u0001<\u0001<\u0001=\u0001=\u0003"+
		"=\u01b5\b=\u0001=\u0001=\u0001>\u0001>\u0001>\u0001>\u0001>\u0001?\u0001"+
		"?\u0001?\u0001?\u0001?\u0001@\u0001@\u0001@\u0001@\u0001@\u0001A\u0001"+
		"A\u0001A\u0001A\u0002\u0091\u009d\u0000B\u0006\u0002\b\u0003\n\u0004\f"+
		"\u0005\u000e\u0006\u0010\u0007\u0012\b\u0014\t\u0016\n\u0018\u000b\u001a"+
		"\f\u001c\r\u001e\u000e \u000f\"\u0010$\u0011&\u0012(\u0013*\u0000,\u0000"+
		".\u00000\u00002\u00004\u00006#8\u0014:\u0015<\u0016>\u0017@\u0018B\u0019"+
		"D\u001aF\u001bH\u001cJ\u001dL\u001eN\u001fP$R%T&V X!Z\"\\\u0000^\u0000"+
		"`\u0000b\u0000d\u0000f\'h(j)l\u0000n\u0000p\u0000r\u0000t\u0000v\u0000"+
		"x\u0000z\u0000|\u0000~\u0000\u0080\u0000\u0082\u0000\u0084\u0000\u0086"+
		"\u0000\u0088\u0000\u0006\u0000\u0001\u0002\u0003\u0004\u0005\u001a\u0001"+
		"\u0000!!\u0003\u0000\n\n\r\r\u2028\u2029\u0002\u0000OOoo\u0002\u0000N"+
		"Nnn\u0002\u0000FFff\u0002\u0000RRrr\u0002\u0000MMmm\u0002\u0000WWww\u0002"+
		"\u0000IIii\u0002\u0000TTtt\u0002\u0000HHhh\u0002\u0000DDdd\u0002\u0000"+
		"SSss\u0002\u0000PPpp\u0002\u0000LLll\u0002\u0000AAaa\u0002\u0000YYyy\u0002"+
		"\u0000GGgg\u0002\u0000EEee\u0001\u000009\u0002\u0000AZaz\u0001\u0000_"+
		"_\u0002\u0000\t\t  \u0004\u0000!!,.:;??\u0001\u0000\'\'\u0001\u0000\""+
		"\"\u01ce\u0000\u0006\u0001\u0000\u0000\u0000\u0000\b\u0001\u0000\u0000"+
		"\u0000\u0000\n\u0001\u0000\u0000\u0000\u0000\f\u0001\u0000\u0000\u0000"+
		"\u0000\u000e\u0001\u0000\u0000\u0000\u0000\u0010\u0001\u0000\u0000\u0000"+
		"\u0000\u0012\u0001\u0000\u0000\u0000\u0000\u0014\u0001\u0000\u0000\u0000"+
		"\u0000\u0016\u0001\u0000\u0000\u0000\u0000\u0018\u0001\u0000\u0000\u0000"+
		"\u0000\u001a\u0001\u0000\u0000\u0000\u0000\u001c\u0001\u0000\u0000\u0000"+
		"\u0000\u001e\u0001\u0000\u0000\u0000\u0000 \u0001\u0000\u0000\u0000\u0000"+
		"\"\u0001\u0000\u0000\u0000\u0000$\u0001\u0000\u0000\u0000\u0000&\u0001"+
		"\u0000\u0000\u0000\u0000(\u0001\u0000\u0000\u0000\u00006\u0001\u0000\u0000"+
		"\u0000\u00008\u0001\u0000\u0000\u0000\u0000:\u0001\u0000\u0000\u0000\u0000"+
		"<\u0001\u0000\u0000\u0000\u0000>\u0001\u0000\u0000\u0000\u0000@\u0001"+
		"\u0000\u0000\u0000\u0000B\u0001\u0000\u0000\u0000\u0000D\u0001\u0000\u0000"+
		"\u0000\u0000F\u0001\u0000\u0000\u0000\u0000H\u0001\u0000\u0000\u0000\u0000"+
		"J\u0001\u0000\u0000\u0000\u0000L\u0001\u0000\u0000\u0000\u0000N\u0001"+
		"\u0000\u0000\u0000\u0001P\u0001\u0000\u0000\u0000\u0001R\u0001\u0000\u0000"+
		"\u0000\u0001T\u0001\u0000\u0000\u0000\u0001V\u0001\u0000\u0000\u0000\u0001"+
		"X\u0001\u0000\u0000\u0000\u0001Z\u0001\u0000\u0000\u0000\u0002d\u0001"+
		"\u0000\u0000\u0000\u0002f\u0001\u0000\u0000\u0000\u0002h\u0001\u0000\u0000"+
		"\u0000\u0002j\u0001\u0000\u0000\u0000\u0002l\u0001\u0000\u0000\u0000\u0003"+
		"n\u0001\u0000\u0000\u0000\u0003p\u0001\u0000\u0000\u0000\u0003r\u0001"+
		"\u0000\u0000\u0000\u0003t\u0001\u0000\u0000\u0000\u0004v\u0001\u0000\u0000"+
		"\u0000\u0004x\u0001\u0000\u0000\u0000\u0004z\u0001\u0000\u0000\u0000\u0004"+
		"|\u0001\u0000\u0000\u0000\u0004~\u0001\u0000\u0000\u0000\u0005\u0080\u0001"+
		"\u0000\u0000\u0000\u0005\u0082\u0001\u0000\u0000\u0000\u0005\u0084\u0001"+
		"\u0000\u0000\u0000\u0005\u0086\u0001\u0000\u0000\u0000\u0005\u0088\u0001"+
		"\u0000\u0000\u0000\u0006\u008a\u0001\u0000\u0000\u0000\b\u0097\u0001\u0000"+
		"\u0000\u0000\n\u00a5\u0001\u0000\u0000\u0000\f\u00af\u0001\u0000\u0000"+
		"\u0000\u000e\u00b5\u0001\u0000\u0000\u0000\u0010\u00ba\u0001\u0000\u0000"+
		"\u0000\u0012\u00c0\u0001\u0000\u0000\u0000\u0014\u00c3\u0001\u0000\u0000"+
		"\u0000\u0016\u00c8\u0001\u0000\u0000\u0000\u0018\u00cd\u0001\u0000\u0000"+
		"\u0000\u001a\u00d8\u0001\u0000\u0000\u0000\u001c\u00df\u0001\u0000\u0000"+
		"\u0000\u001e\u00e1\u0001\u0000\u0000\u0000 \u00e3\u0001\u0000\u0000\u0000"+
		"\"\u00e6\u0001\u0000\u0000\u0000$\u00e8\u0001\u0000\u0000\u0000&\u00eb"+
		"\u0001\u0000\u0000\u0000(\u00ee\u0001\u0000\u0000\u0000*\u00f0\u0001\u0000"+
		"\u0000\u0000,\u00f2\u0001\u0000\u0000\u0000.\u00f6\u0001\u0000\u0000\u0000"+
		"0\u00fa\u0001\u0000\u0000\u00002\u00fc\u0001\u0000\u0000\u00004\u00ff"+
		"\u0001\u0000\u0000\u00006\u0103\u0001\u0000\u0000\u00008\u0109\u0001\u0000"+
		"\u0000\u0000:\u010d\u0001\u0000\u0000\u0000<\u0111\u0001\u0000\u0000\u0000"+
		">\u0118\u0001\u0000\u0000\u0000@\u011e\u0001\u0000\u0000\u0000B\u0125"+
		"\u0001\u0000\u0000\u0000D\u0129\u0001\u0000\u0000\u0000F\u012b\u0001\u0000"+
		"\u0000\u0000H\u012d\u0001\u0000\u0000\u0000J\u012f\u0001\u0000\u0000\u0000"+
		"L\u0131\u0001\u0000\u0000\u0000N\u0133\u0001\u0000\u0000\u0000P\u0135"+
		"\u0001\u0000\u0000\u0000R\u013a\u0001\u0000\u0000\u0000T\u013f\u0001\u0000"+
		"\u0000\u0000V\u0144\u0001\u0000\u0000\u0000X\u0148\u0001\u0000\u0000\u0000"+
		"Z\u014c\u0001\u0000\u0000\u0000\\\u014e\u0001\u0000\u0000\u0000^\u0159"+
		"\u0001\u0000\u0000\u0000`\u0164\u0001\u0000\u0000\u0000b\u0167\u0001\u0000"+
		"\u0000\u0000d\u016d\u0001\u0000\u0000\u0000f\u0171\u0001\u0000\u0000\u0000"+
		"h\u0176\u0001\u0000\u0000\u0000j\u017b\u0001\u0000\u0000\u0000l\u0180"+
		"\u0001\u0000\u0000\u0000n\u0186\u0001\u0000\u0000\u0000p\u018a\u0001\u0000"+
		"\u0000\u0000r\u018f\u0001\u0000\u0000\u0000t\u0195\u0001\u0000\u0000\u0000"+
		"v\u019b\u0001\u0000\u0000\u0000x\u019f\u0001\u0000\u0000\u0000z\u01a4"+
		"\u0001\u0000\u0000\u0000|\u01a9\u0001\u0000\u0000\u0000~\u01ae\u0001\u0000"+
		"\u0000\u0000\u0080\u01b4\u0001\u0000\u0000\u0000\u0082\u01b8\u0001\u0000"+
		"\u0000\u0000\u0084\u01bd\u0001\u0000\u0000\u0000\u0086\u01c2\u0001\u0000"+
		"\u0000\u0000\u0088\u01c7\u0001\u0000\u0000\u0000\u008a\u008b\u0005#\u0000"+
		"\u0000\u008b\u008c\u0005*\u0000\u0000\u008c\u008d\u0005*\u0000\u0000\u008d"+
		"\u0091\u0001\u0000\u0000\u0000\u008e\u0090\t\u0000\u0000\u0000\u008f\u008e"+
		"\u0001\u0000\u0000\u0000\u0090\u0093\u0001\u0000\u0000\u0000\u0091\u0092"+
		"\u0001\u0000\u0000\u0000\u0091\u008f\u0001\u0000\u0000\u0000\u0092\u0094"+
		"\u0001\u0000\u0000\u0000\u0093\u0091\u0001\u0000\u0000\u0000\u0094\u0095"+
		"\u0005*\u0000\u0000\u0095\u0096\u0005#\u0000\u0000\u0096\u0007\u0001\u0000"+
		"\u0000\u0000\u0097\u0098\u0005#\u0000\u0000\u0098\u0099\u0005*\u0000\u0000"+
		"\u0099\u009d\u0001\u0000\u0000\u0000\u009a\u009c\t\u0000\u0000\u0000\u009b"+
		"\u009a\u0001\u0000\u0000\u0000\u009c\u009f\u0001\u0000\u0000\u0000\u009d"+
		"\u009e\u0001\u0000\u0000\u0000\u009d\u009b\u0001\u0000\u0000\u0000\u009e"+
		"\u00a0\u0001\u0000\u0000\u0000\u009f\u009d\u0001\u0000\u0000\u0000\u00a0"+
		"\u00a1\u0005*\u0000\u0000\u00a1\u00a2\u0005#\u0000\u0000\u00a2\u00a3\u0001"+
		"\u0000\u0000\u0000\u00a3\u00a4\u0006\u0001\u0000\u0000\u00a4\t\u0001\u0000"+
		"\u0000\u0000\u00a5\u00a6\u0005#\u0000\u0000\u00a6\u00aa\b\u0000\u0000"+
		"\u0000\u00a7\u00a9\b\u0001\u0000\u0000\u00a8\u00a7\u0001\u0000\u0000\u0000"+
		"\u00a9\u00ac\u0001\u0000\u0000\u0000\u00aa\u00a8\u0001\u0000\u0000\u0000"+
		"\u00aa\u00ab\u0001\u0000\u0000\u0000\u00ab\u00ad\u0001\u0000\u0000\u0000"+
		"\u00ac\u00aa\u0001\u0000\u0000\u0000\u00ad\u00ae\u0006\u0002\u0000\u0000"+
		"\u00ae\u000b\u0001\u0000\u0000\u0000\u00af\u00b1\u00034\u0017\u0000\u00b0"+
		"\u00b2\u00034\u0017\u0000\u00b1\u00b0\u0001\u0000\u0000\u0000\u00b2\u00b3"+
		"\u0001\u0000\u0000\u0000\u00b3\u00b1\u0001\u0000\u0000\u0000\u00b3\u00b4"+
		"\u0001\u0000\u0000\u0000\u00b4\r\u0001\u0000\u0000\u0000\u00b5\u00b6\u0003"+
		"4\u0017\u0000\u00b6\u00b7\u0001\u0000\u0000\u0000\u00b7\u00b8\u0006\u0004"+
		"\u0001\u0000\u00b8\u000f\u0001\u0000\u0000\u0000\u00b9\u00bb\u00032\u0016"+
		"\u0000\u00ba\u00b9\u0001\u0000\u0000\u0000\u00bb\u00bc\u0001\u0000\u0000"+
		"\u0000\u00bc\u00ba\u0001\u0000\u0000\u0000\u00bc\u00bd\u0001\u0000\u0000"+
		"\u0000\u00bd\u00be\u0001\u0000\u0000\u0000\u00be\u00bf\u0006\u0005\u0001"+
		"\u0000\u00bf\u0011\u0001\u0000\u0000\u0000\u00c0\u00c1\u0007\u0002\u0000"+
		"\u0000\u00c1\u00c2\u0007\u0003\u0000\u0000\u00c2\u0013\u0001\u0000\u0000"+
		"\u0000\u00c3\u00c4\u0007\u0004\u0000\u0000\u00c4\u00c5\u0007\u0005\u0000"+
		"\u0000\u00c5\u00c6\u0007\u0002\u0000\u0000\u00c6\u00c7\u0007\u0006\u0000"+
		"\u0000\u00c7\u0015\u0001\u0000\u0000\u0000\u00c8\u00c9\u0007\u0007\u0000"+
		"\u0000\u00c9\u00ca\u0007\b\u0000\u0000\u00ca\u00cb\u0007\t\u0000\u0000"+
		"\u00cb\u00cc\u0007\n\u0000\u0000\u00cc\u0017\u0001\u0000\u0000\u0000\u00cd"+
		"\u00ce\u0007\u000b\u0000\u0000\u00ce\u00cf\u0007\b\u0000\u0000\u00cf\u00d0"+
		"\u0007\f\u0000\u0000\u00d0\u00d1\u0007\r\u0000\u0000\u00d1\u00d2\u0007"+
		"\u000e\u0000\u0000\u00d2\u00d3\u0007\u000f\u0000\u0000\u00d3\u00d4\u0007"+
		"\u0010\u0000\u0000\u00d4\u00d5\u0007\b\u0000\u0000\u00d5\u00d6\u0007\u0003"+
		"\u0000\u0000\u00d6\u00d7\u0007\u0011\u0000\u0000\u00d7\u0019\u0001\u0000"+
		"\u0000\u0000\u00d8\u00d9\u0007\u0010\u0000\u0000\u00d9\u00da\u0007\b\u0000"+
		"\u0000\u00da\u00db\u0007\u0012\u0000\u0000\u00db\u00dc\u0007\u000e\u0000"+
		"\u0000\u00dc\u00dd\u0007\u000b\u0000\u0000\u00dd\u00de\u0007\f\u0000\u0000"+
		"\u00de\u001b\u0001\u0000\u0000\u0000\u00df\u00e0\u0005=\u0000\u0000\u00e0"+
		"\u001d\u0001\u0000\u0000\u0000\u00e1\u00e2\u0005>\u0000\u0000\u00e2\u001f"+
		"\u0001\u0000\u0000\u0000\u00e3\u00e4\u0005>\u0000\u0000\u00e4\u00e5\u0005"+
		"=\u0000\u0000\u00e5!\u0001\u0000\u0000\u0000\u00e6\u00e7\u0005<\u0000"+
		"\u0000\u00e7#\u0001\u0000\u0000\u0000\u00e8\u00e9\u0005<\u0000\u0000\u00e9"+
		"\u00ea\u0005=\u0000\u0000\u00ea%\u0001\u0000\u0000\u0000\u00eb\u00ec\u0005"+
		"#\u0000\u0000\u00ec\u00ed\u0005!\u0000\u0000\u00ed\'\u0001\u0000\u0000"+
		"\u0000\u00ee\u00ef\u0005*\u0000\u0000\u00ef)\u0001\u0000\u0000\u0000\u00f0"+
		"\u00f1\u0007\u0013\u0000\u0000\u00f1+\u0001\u0000\u0000\u0000\u00f2\u00f3"+
		"\u0007\u0014\u0000\u0000\u00f3-\u0001\u0000\u0000\u0000\u00f4\u00f7\u0003"+
		"*\u0012\u0000\u00f5\u00f7\u0003,\u0013\u0000\u00f6\u00f4\u0001\u0000\u0000"+
		"\u0000\u00f6\u00f5\u0001\u0000\u0000\u0000\u00f7/\u0001\u0000\u0000\u0000"+
		"\u00f8\u00fb\u0003.\u0014\u0000\u00f9\u00fb\u0007\u0015\u0000\u0000\u00fa"+
		"\u00f8\u0001\u0000\u0000\u0000\u00fa\u00f9\u0001\u0000\u0000\u0000\u00fb"+
		"1\u0001\u0000\u0000\u0000\u00fc\u00fd\u0007\u0016\u0000\u0000\u00fd3\u0001"+
		"\u0000\u0000\u0000\u00fe\u0100\u0005\r\u0000\u0000\u00ff\u00fe\u0001\u0000"+
		"\u0000\u0000\u00ff\u0100\u0001\u0000\u0000\u0000\u0100\u0101\u0001\u0000"+
		"\u0000\u0000\u0101\u0102\u0005\n\u0000\u0000\u01025\u0001\u0000\u0000"+
		"\u0000\u0103\u0104\u0005$\u0000\u0000\u0104\u0105\u0005{\u0000\u0000\u0105"+
		"\u0106\u0001\u0000\u0000\u0000\u0106\u0107\u0006\u0018\u0002\u0000\u0107"+
		"\u0108\u0006\u0018\u0003\u0000\u01087\u0001\u0000\u0000\u0000\u0109\u010a"+
		"\u0005%\u0000\u0000\u010a\u010b\u0001\u0000\u0000\u0000\u010b\u010c\u0006"+
		"\u0019\u0004\u0000\u010c9\u0001\u0000\u0000\u0000\u010d\u010e\u0005|\u0000"+
		"\u0000\u010e\u010f\u0001\u0000\u0000\u0000\u010f\u0110\u0006\u001a\u0005"+
		"\u0000\u0110;\u0001\u0000\u0000\u0000\u0111\u0112\u0005{\u0000\u0000\u0112"+
		"\u0113\u0005{\u0000\u0000\u0113\u0114\u0001\u0000\u0000\u0000\u0114\u0115"+
		"\u0003@\u001d\u0000\u0115\u0116\u0005}\u0000\u0000\u0116\u0117\u0005}"+
		"\u0000\u0000\u0117=\u0001\u0000\u0000\u0000\u0118\u011a\u0005$\u0000\u0000"+
		"\u0119\u011b\u00030\u0015\u0000\u011a\u0119\u0001\u0000\u0000\u0000\u011b"+
		"\u011c\u0001\u0000\u0000\u0000\u011c\u011a\u0001\u0000\u0000\u0000\u011c"+
		"\u011d\u0001\u0000\u0000\u0000\u011d?\u0001\u0000\u0000\u0000\u011e\u0120"+
		"\u0003,\u0013\u0000\u011f\u0121\u00030\u0015\u0000\u0120\u011f\u0001\u0000"+
		"\u0000\u0000\u0121\u0122\u0001\u0000\u0000\u0000\u0122\u0120\u0001\u0000"+
		"\u0000\u0000\u0122\u0123\u0001\u0000\u0000\u0000\u0123A\u0001\u0000\u0000"+
		"\u0000\u0124\u0126\u00030\u0015\u0000\u0125\u0124\u0001\u0000\u0000\u0000"+
		"\u0126\u0127\u0001\u0000\u0000\u0000\u0127\u0125\u0001\u0000\u0000\u0000"+
		"\u0127\u0128\u0001\u0000\u0000\u0000\u0128C\u0001\u0000\u0000\u0000\u0129"+
		"\u012a\u0005@\u0000\u0000\u012aE\u0001\u0000\u0000\u0000\u012b\u012c\u0005"+
		"$\u0000\u0000\u012cG\u0001\u0000\u0000\u0000\u012d\u012e\u0005(\u0000"+
		"\u0000\u012eI\u0001\u0000\u0000\u0000\u012f\u0130\u0005)\u0000\u0000\u0130"+
		"K\u0001\u0000\u0000\u0000\u0131\u0132\u0005,\u0000\u0000\u0132M\u0001"+
		"\u0000\u0000\u0000\u0133\u0134\u0007\u0017\u0000\u0000\u0134O\u0001\u0000"+
		"\u0000\u0000\u0135\u0136\u0005\\\u0000\u0000\u0136\u0137\u0005\\\u0000"+
		"\u0000\u0137\u0138\u0001\u0000\u0000\u0000\u0138\u0139\u0006%\u0006\u0000"+
		"\u0139Q\u0001\u0000\u0000\u0000\u013a\u013b\u0005\\\u0000\u0000\u013b"+
		"\u013c\u0005%\u0000\u0000\u013c\u013d\u0001\u0000\u0000\u0000\u013d\u013e"+
		"\u0006&\u0006\u0000\u013eS\u0001\u0000\u0000\u0000\u013f\u0140\u0005\\"+
		"\u0000\u0000\u0140\u0141\u0005|\u0000\u0000\u0141\u0142\u0001\u0000\u0000"+
		"\u0000\u0142\u0143\u0006\'\u0006\u0000\u0143U\u0001\u0000\u0000\u0000"+
		"\u0144\u0145\u0005|\u0000\u0000\u0145\u0146\u0001\u0000\u0000\u0000\u0146"+
		"\u0147\u0006(\u0007\u0000\u0147W\u0001\u0000\u0000\u0000\u0148\u0149\u0005"+
		"%\u0000\u0000\u0149\u014a\u0001\u0000\u0000\u0000\u014a\u014b\u0006)\u0005"+
		"\u0000\u014bY\u0001\u0000\u0000\u0000\u014c\u014d\t\u0000\u0000\u0000"+
		"\u014d[\u0001\u0000\u0000\u0000\u014e\u0154\u0005\'\u0000\u0000\u014f"+
		"\u0150\u0005\\\u0000\u0000\u0150\u0153\u0005\'\u0000\u0000\u0151\u0153"+
		"\b\u0018\u0000\u0000\u0152\u014f\u0001\u0000\u0000\u0000\u0152\u0151\u0001"+
		"\u0000\u0000\u0000\u0153\u0156\u0001\u0000\u0000\u0000\u0154\u0152\u0001"+
		"\u0000\u0000\u0000\u0154\u0155\u0001\u0000\u0000\u0000\u0155\u0157\u0001"+
		"\u0000\u0000\u0000\u0156\u0154\u0001\u0000\u0000\u0000\u0157\u0158\u0005"+
		"\'\u0000\u0000\u0158]\u0001\u0000\u0000\u0000\u0159\u015f\u0005\"\u0000"+
		"\u0000\u015a\u015b\u0005\\\u0000\u0000\u015b\u015e\u0005\"\u0000\u0000"+
		"\u015c\u015e\b\u0019\u0000\u0000\u015d\u015a\u0001\u0000\u0000\u0000\u015d"+
		"\u015c\u0001\u0000\u0000\u0000\u015e\u0161\u0001\u0000\u0000\u0000\u015f"+
		"\u015d\u0001\u0000\u0000\u0000\u015f\u0160\u0001\u0000\u0000\u0000\u0160"+
		"\u0162\u0001\u0000\u0000\u0000\u0161\u015f\u0001\u0000\u0000\u0000\u0162"+
		"\u0163\u0005\"\u0000\u0000\u0163_\u0001\u0000\u0000\u0000\u0164\u0165"+
		"\u0005\\\u0000\u0000\u0165\u0166\u0005`\u0000\u0000\u0166a\u0001\u0000"+
		"\u0000\u0000\u0167\u0168\u0005\\\u0000\u0000\u0168\u0169\u0005$\u0000"+
		"\u0000\u0169\u016a\u0005{\u0000\u0000\u016ac\u0001\u0000\u0000\u0000\u016b"+
		"\u016e\u0003\\+\u0000\u016c\u016e\u0003^,\u0000\u016d\u016b\u0001\u0000"+
		"\u0000\u0000\u016d\u016c\u0001\u0000\u0000\u0000\u016e\u016f\u0001\u0000"+
		"\u0000\u0000\u016f\u0170\u0006/\u0003\u0000\u0170e\u0001\u0000\u0000\u0000"+
		"\u0171\u0172\u0005}\u0000\u0000\u0172\u0173\u0001\u0000\u0000\u0000\u0173"+
		"\u0174\u00060\u0005\u0000\u0174\u0175\u00060\b\u0000\u0175g\u0001\u0000"+
		"\u0000\u0000\u0176\u0177\u0005`\u0000\u0000\u0177\u0178\u0001\u0000\u0000"+
		"\u0000\u0178\u0179\u00061\t\u0000\u0179\u017a\u00061\u0003\u0000\u017a"+
		"i\u0001\u0000\u0000\u0000\u017b\u017c\u0005{\u0000\u0000\u017c\u017d\u0001"+
		"\u0000\u0000\u0000\u017d\u017e\u00062\n\u0000\u017e\u017f\u00062\u0003"+
		"\u0000\u017fk\u0001\u0000\u0000\u0000\u0180\u0181\t\u0000\u0000\u0000"+
		"\u0181\u0182\u0001\u0000\u0000\u0000\u0182\u0183\u00063\u0003\u0000\u0183"+
		"m\u0001\u0000\u0000\u0000\u0184\u0187\u0003`-\u0000\u0185\u0187\u0003"+
		"b.\u0000\u0186\u0184\u0001\u0000\u0000\u0000\u0186\u0185\u0001\u0000\u0000"+
		"\u0000\u0187\u0188\u0001\u0000\u0000\u0000\u0188\u0189\u00064\u0003\u0000"+
		"\u0189o\u0001\u0000\u0000\u0000\u018a\u018b\u0005`\u0000\u0000\u018b\u018c"+
		"\u0001\u0000\u0000\u0000\u018c\u018d\u00065\u0005\u0000\u018d\u018e\u0006"+
		"5\u0003\u0000\u018eq\u0001\u0000\u0000\u0000\u018f\u0190\u0005$\u0000"+
		"\u0000\u0190\u0191\u0005{\u0000\u0000\u0191\u0192\u0001\u0000\u0000\u0000"+
		"\u0192\u0193\u00066\u000b\u0000\u0193\u0194\u00066\u0003\u0000\u0194s"+
		"\u0001\u0000\u0000\u0000\u0195\u0196\t\u0000\u0000\u0000\u0196\u0197\u0001"+
		"\u0000\u0000\u0000\u0197\u0198\u00067\u0003\u0000\u0198u\u0001\u0000\u0000"+
		"\u0000\u0199\u019c\u0003\\+\u0000\u019a\u019c\u0003^,\u0000\u019b\u0199"+
		"\u0001\u0000\u0000\u0000\u019b\u019a\u0001\u0000\u0000\u0000\u019c\u019d"+
		"\u0001\u0000\u0000\u0000\u019d\u019e\u00068\u0003\u0000\u019ew\u0001\u0000"+
		"\u0000\u0000\u019f\u01a0\u0005}\u0000\u0000\u01a0\u01a1\u0001\u0000\u0000"+
		"\u0000\u01a1\u01a2\u00069\u0005\u0000\u01a2\u01a3\u00069\u0003\u0000\u01a3"+
		"y\u0001\u0000\u0000\u0000\u01a4\u01a5\u0005`\u0000\u0000\u01a5\u01a6\u0001"+
		"\u0000\u0000\u0000\u01a6\u01a7\u0006:\t\u0000\u01a7\u01a8\u0006:\u0003"+
		"\u0000\u01a8{\u0001\u0000\u0000\u0000\u01a9\u01aa\u0005{\u0000\u0000\u01aa"+
		"\u01ab\u0001\u0000\u0000\u0000\u01ab\u01ac\u0006;\n\u0000\u01ac\u01ad"+
		"\u0006;\u0003\u0000\u01ad}\u0001\u0000\u0000\u0000\u01ae\u01af\t\u0000"+
		"\u0000\u0000\u01af\u01b0\u0001\u0000\u0000\u0000\u01b0\u01b1\u0006<\u0003"+
		"\u0000\u01b1\u007f\u0001\u0000\u0000\u0000\u01b2\u01b5\u0003\\+\u0000"+
		"\u01b3\u01b5\u0003^,\u0000\u01b4\u01b2\u0001\u0000\u0000\u0000\u01b4\u01b3"+
		"\u0001\u0000\u0000\u0000\u01b5\u01b6\u0001\u0000\u0000\u0000\u01b6\u01b7"+
		"\u0006=\u0003\u0000\u01b7\u0081\u0001\u0000\u0000\u0000\u01b8\u01b9\u0005"+
		"}\u0000\u0000\u01b9\u01ba\u0001\u0000\u0000\u0000\u01ba\u01bb\u0006>\u0005"+
		"\u0000\u01bb\u01bc\u0006>\u0003\u0000\u01bc\u0083\u0001\u0000\u0000\u0000"+
		"\u01bd\u01be\u0005`\u0000\u0000\u01be\u01bf\u0001\u0000\u0000\u0000\u01bf"+
		"\u01c0\u0006?\t\u0000\u01c0\u01c1\u0006?\u0003\u0000\u01c1\u0085\u0001"+
		"\u0000\u0000\u0000\u01c2\u01c3\u0005{\u0000\u0000\u01c3\u01c4\u0001\u0000"+
		"\u0000\u0000\u01c4\u01c5\u0006@\n\u0000\u01c5\u01c6\u0006@\u0003\u0000"+
		"\u01c6\u0087\u0001\u0000\u0000\u0000\u01c7\u01c8\t\u0000\u0000\u0000\u01c8"+
		"\u01c9\u0001\u0000\u0000\u0000\u01c9\u01ca\u0006A\u0003\u0000\u01ca\u0089"+
		"\u0001\u0000\u0000\u0000\u0019\u0000\u0001\u0002\u0003\u0004\u0005\u0091"+
		"\u009d\u00aa\u00b3\u00bc\u00f6\u00fa\u00ff\u011c\u0122\u0127\u0152\u0154"+
		"\u015d\u015f\u016d\u0186\u019b\u01b4\f\u0000\u0001\u0000\u0006\u0000\u0000"+
		"\u0005\u0002\u0000\u0003\u0000\u0000\u0005\u0001\u0000\u0004\u0000\u0000"+
		"\u0007\"\u0000\u0005\u0000\u0000\u0007\u0001\u0000\u0005\u0003\u0000\u0005"+
		"\u0005\u0000\u0005\u0004\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}