// Generated from net/programmer/igoodie/tsl/TSLParserImpl.g4 by ANTLR 4.13.1
package net.programmer.igoodie.tsl.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class TSLParserImpl extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		EXPRESSION=1, TSLDOC_COMMENT=2, SINGLELINE_COMMENT=3, MULTILINE_COMMENT=4, 
		EMPTY_LINES=5, NEW_LINE=6, HORIZONTAL_SPACES=7, KEYWORD_ON=8, KEYWORD_FROM=9, 
		KEYWORD_WITH=10, KEYWORD_DISPLAYING=11, KEYWORD_YIELDING=12, SYMBOL_EQUALS=13, 
		SYMBOL_GT=14, SYMBOL_GTE=15, SYMBOL_LT=16, SYMBOL_LTE=17, SYMBOL_DIRECTIVE=18, 
		BEGIN_GROUP=19, END_GROUP_EXPRESSION=20, PLACEHOLDER=21, CAPTURE_IDENTIFIER=22, 
		IDENTIFIER=23, PLAIN_WORD=24, SIGN_AT=25, SIGN_DOLLAR=26, SIGN_LPARAN=27, 
		SIGN_RPARAN=28, SIGN_COMMA=29, PUNCTUATION=30, BEGIN_GROUP_EXPRESSION=31, 
		END_GROUP=32, GROUP_STRING=33, BEGIN_EXPRESSION=34, ESCAPED_BACKSLASH=35, 
		ESCAPED_END_GROUP=36, ESCAPED_GROUP_EXPR=37, END_EXPRESSION=38, BEGIN_STRING_TEMPLATE=39, 
		BEGIN_REGEX=40, BEGIN_OBJECT=41, END_REGEX_ESC=42;
	public static final int
		RULE_tslWords = 0, RULE_tslRuleset = 1, RULE_tslRules = 2, RULE_tslRule = 3, 
		RULE_tslRuleDoc = 4, RULE_tslDirective = 5, RULE_tslDirectiveArgs = 6, 
		RULE_reactionRule = 7, RULE_action = 8, RULE_actionBody = 9, RULE_actionId = 10, 
		RULE_actionArgs = 11, RULE_actionYielding = 12, RULE_actionDisplaying = 13, 
		RULE_event = 14, RULE_eventName = 15, RULE_eventFrom = 16, RULE_eventPredicate = 17, 
		RULE_predicateExpression = 18, RULE_predicateOperation = 19, RULE_predicateOperator = 20, 
		RULE_captureRule = 21, RULE_captureHeader = 22, RULE_captureContent = 23, 
		RULE_captureParams = 24, RULE_group = 25, RULE_groupString = 26, RULE_groupExpression = 27, 
		RULE_captureCall = 28, RULE_captureArgs = 29, RULE_captureArg = 30, RULE_word = 31, 
		RULE_predicateWord = 32, RULE_wordNest = 33, RULE_wordNestContent = 34;
	private static String[] makeRuleNames() {
		return new String[] {
			"tslWords", "tslRuleset", "tslRules", "tslRule", "tslRuleDoc", "tslDirective", 
			"tslDirectiveArgs", "reactionRule", "action", "actionBody", "actionId", 
			"actionArgs", "actionYielding", "actionDisplaying", "event", "eventName", 
			"eventFrom", "eventPredicate", "predicateExpression", "predicateOperation", 
			"predicateOperator", "captureRule", "captureHeader", "captureContent", 
			"captureParams", "group", "groupString", "groupExpression", "captureCall", 
			"captureArgs", "captureArg", "word", "predicateWord", "wordNest", "wordNestContent"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, "'ON'", "'FROM'", "'WITH'", 
			"'DISPLAYING'", "'YIELDING'", "'='", "'>'", "'>='", "'<'", "'<='", "'#!'", 
			null, null, null, null, null, null, "'@'", "'$'", "'('", "')'", "','", 
			null, null, null, null, null, "'\\\\'", "'\\%'", "'\\|'", null, null, 
			null, null, "'\\/'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "EXPRESSION", "TSLDOC_COMMENT", "SINGLELINE_COMMENT", "MULTILINE_COMMENT", 
			"EMPTY_LINES", "NEW_LINE", "HORIZONTAL_SPACES", "KEYWORD_ON", "KEYWORD_FROM", 
			"KEYWORD_WITH", "KEYWORD_DISPLAYING", "KEYWORD_YIELDING", "SYMBOL_EQUALS", 
			"SYMBOL_GT", "SYMBOL_GTE", "SYMBOL_LT", "SYMBOL_LTE", "SYMBOL_DIRECTIVE", 
			"BEGIN_GROUP", "END_GROUP_EXPRESSION", "PLACEHOLDER", "CAPTURE_IDENTIFIER", 
			"IDENTIFIER", "PLAIN_WORD", "SIGN_AT", "SIGN_DOLLAR", "SIGN_LPARAN", 
			"SIGN_RPARAN", "SIGN_COMMA", "PUNCTUATION", "BEGIN_GROUP_EXPRESSION", 
			"END_GROUP", "GROUP_STRING", "BEGIN_EXPRESSION", "ESCAPED_BACKSLASH", 
			"ESCAPED_END_GROUP", "ESCAPED_GROUP_EXPR", "END_EXPRESSION", "BEGIN_STRING_TEMPLATE", 
			"BEGIN_REGEX", "BEGIN_OBJECT", "END_REGEX_ESC"
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

	@Override
	public String getGrammarFileName() { return "TSLParserImpl.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TSLParserImpl(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TslWordsContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(TSLParserImpl.EOF, 0); }
		public List<WordContext> word() {
			return getRuleContexts(WordContext.class);
		}
		public WordContext word(int i) {
			return getRuleContext(WordContext.class,i);
		}
		public TslWordsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tslWords; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterTslWords(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitTslWords(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitTslWords(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TslWordsContext tslWords() throws RecognitionException {
		TslWordsContext _localctx = new TslWordsContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_tslWords);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 31981570L) != 0)) {
				{
				{
				setState(70);
				word();
				}
				}
				setState(75);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(76);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TslRulesetContext extends ParserRuleContext {
		public TslRulesContext tslRules() {
			return getRuleContext(TslRulesContext.class,0);
		}
		public TerminalNode EOF() { return getToken(TSLParserImpl.EOF, 0); }
		public List<TerminalNode> EMPTY_LINES() { return getTokens(TSLParserImpl.EMPTY_LINES); }
		public TerminalNode EMPTY_LINES(int i) {
			return getToken(TSLParserImpl.EMPTY_LINES, i);
		}
		public List<TslDirectiveContext> tslDirective() {
			return getRuleContexts(TslDirectiveContext.class);
		}
		public TslDirectiveContext tslDirective(int i) {
			return getRuleContext(TslDirectiveContext.class,i);
		}
		public TslRulesetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tslRuleset; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterTslRuleset(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitTslRuleset(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitTslRuleset(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TslRulesetContext tslRuleset() throws RecognitionException {
		TslRulesetContext _localctx = new TslRulesetContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_tslRuleset);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(81);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(78);
					match(EMPTY_LINES);
					}
					} 
				}
				setState(83);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
			}
			setState(87);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==SYMBOL_DIRECTIVE) {
				{
				{
				setState(84);
				tslDirective();
				}
				}
				setState(89);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(90);
			tslRules();
			setState(91);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TslRulesContext extends ParserRuleContext {
		public List<TslRuleContext> tslRule() {
			return getRuleContexts(TslRuleContext.class);
		}
		public TslRuleContext tslRule(int i) {
			return getRuleContext(TslRuleContext.class,i);
		}
		public List<TerminalNode> EMPTY_LINES() { return getTokens(TSLParserImpl.EMPTY_LINES); }
		public TerminalNode EMPTY_LINES(int i) {
			return getToken(TSLParserImpl.EMPTY_LINES, i);
		}
		public TslRulesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tslRules; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterTslRules(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitTslRules(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitTslRules(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TslRulesContext tslRules() throws RecognitionException {
		TslRulesContext _localctx = new TslRulesContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_tslRules);
		int _la;
		try {
			setState(102);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EOF:
				enterOuterAlt(_localctx, 1);
				{
				{
				}
				}
				break;
			case TSLDOC_COMMENT:
			case EMPTY_LINES:
			case CAPTURE_IDENTIFIER:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(94);
				tslRule();
				setState(99);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==EMPTY_LINES) {
					{
					{
					setState(95);
					match(EMPTY_LINES);
					setState(96);
					tslRule();
					}
					}
					setState(101);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TslRuleContext extends ParserRuleContext {
		public ReactionRuleContext reactionRule() {
			return getRuleContext(ReactionRuleContext.class,0);
		}
		public CaptureRuleContext captureRule() {
			return getRuleContext(CaptureRuleContext.class,0);
		}
		public List<TerminalNode> EMPTY_LINES() { return getTokens(TSLParserImpl.EMPTY_LINES); }
		public TerminalNode EMPTY_LINES(int i) {
			return getToken(TSLParserImpl.EMPTY_LINES, i);
		}
		public TslRuleDocContext tslRuleDoc() {
			return getRuleContext(TslRuleDocContext.class,0);
		}
		public TslRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tslRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterTslRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitTslRule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitTslRule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TslRuleContext tslRule() throws RecognitionException {
		TslRuleContext _localctx = new TslRuleContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_tslRule);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==EMPTY_LINES) {
				{
				{
				setState(104);
				match(EMPTY_LINES);
				}
				}
				setState(109);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(111);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==TSLDOC_COMMENT) {
				{
				setState(110);
				tslRuleDoc();
				}
			}

			setState(115);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				{
				setState(113);
				reactionRule();
				}
				break;
			case CAPTURE_IDENTIFIER:
				{
				setState(114);
				captureRule();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TslRuleDocContext extends ParserRuleContext {
		public List<TerminalNode> TSLDOC_COMMENT() { return getTokens(TSLParserImpl.TSLDOC_COMMENT); }
		public TerminalNode TSLDOC_COMMENT(int i) {
			return getToken(TSLParserImpl.TSLDOC_COMMENT, i);
		}
		public List<TerminalNode> EMPTY_LINES() { return getTokens(TSLParserImpl.EMPTY_LINES); }
		public TerminalNode EMPTY_LINES(int i) {
			return getToken(TSLParserImpl.EMPTY_LINES, i);
		}
		public TslRuleDocContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tslRuleDoc; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterTslRuleDoc(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitTslRuleDoc(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitTslRuleDoc(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TslRuleDocContext tslRuleDoc() throws RecognitionException {
		TslRuleDocContext _localctx = new TslRuleDocContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_tslRuleDoc);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(117);
				match(TSLDOC_COMMENT);
				setState(119);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==EMPTY_LINES) {
					{
					setState(118);
					match(EMPTY_LINES);
					}
				}

				}
				}
				setState(123); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==TSLDOC_COMMENT );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TslDirectiveContext extends ParserRuleContext {
		public Token id;
		public TerminalNode SYMBOL_DIRECTIVE() { return getToken(TSLParserImpl.SYMBOL_DIRECTIVE, 0); }
		public TslDirectiveArgsContext tslDirectiveArgs() {
			return getRuleContext(TslDirectiveArgsContext.class,0);
		}
		public TerminalNode EOF() { return getToken(TSLParserImpl.EOF, 0); }
		public TerminalNode IDENTIFIER() { return getToken(TSLParserImpl.IDENTIFIER, 0); }
		public List<TerminalNode> EMPTY_LINES() { return getTokens(TSLParserImpl.EMPTY_LINES); }
		public TerminalNode EMPTY_LINES(int i) {
			return getToken(TSLParserImpl.EMPTY_LINES, i);
		}
		public TslDirectiveContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tslDirective; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterTslDirective(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitTslDirective(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitTslDirective(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TslDirectiveContext tslDirective() throws RecognitionException {
		TslDirectiveContext _localctx = new TslDirectiveContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_tslDirective);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(125);
			match(SYMBOL_DIRECTIVE);
			{
			setState(126);
			((TslDirectiveContext)_localctx).id = match(IDENTIFIER);
			}
			setState(127);
			tslDirectiveArgs();
			setState(134);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EMPTY_LINES:
				{
				setState(129); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(128);
						match(EMPTY_LINES);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(131); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case EOF:
				{
				setState(133);
				match(EOF);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TslDirectiveArgsContext extends ParserRuleContext {
		public List<WordContext> word() {
			return getRuleContexts(WordContext.class);
		}
		public WordContext word(int i) {
			return getRuleContext(WordContext.class,i);
		}
		public List<TerminalNode> KEYWORD_FROM() { return getTokens(TSLParserImpl.KEYWORD_FROM); }
		public TerminalNode KEYWORD_FROM(int i) {
			return getToken(TSLParserImpl.KEYWORD_FROM, i);
		}
		public TslDirectiveArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tslDirectiveArgs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterTslDirectiveArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitTslDirectiveArgs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitTslDirectiveArgs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TslDirectiveArgsContext tslDirectiveArgs() throws RecognitionException {
		TslDirectiveArgsContext _localctx = new TslDirectiveArgsContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_tslDirectiveArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(140);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 31982082L) != 0)) {
				{
				setState(138);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case EXPRESSION:
				case BEGIN_GROUP:
				case PLACEHOLDER:
				case CAPTURE_IDENTIFIER:
				case IDENTIFIER:
				case PLAIN_WORD:
					{
					setState(136);
					word();
					}
					break;
				case KEYWORD_FROM:
					{
					setState(137);
					match(KEYWORD_FROM);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(142);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReactionRuleContext extends ParserRuleContext {
		public ActionContext action() {
			return getRuleContext(ActionContext.class,0);
		}
		public EventContext event() {
			return getRuleContext(EventContext.class,0);
		}
		public ReactionRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reactionRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterReactionRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitReactionRule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitReactionRule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReactionRuleContext reactionRule() throws RecognitionException {
		ReactionRuleContext _localctx = new ReactionRuleContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_reactionRule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
			action();
			setState(144);
			event();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ActionContext extends ParserRuleContext {
		public ActionBodyContext actionBody() {
			return getRuleContext(ActionBodyContext.class,0);
		}
		public ActionYieldingContext actionYielding() {
			return getRuleContext(ActionYieldingContext.class,0);
		}
		public ActionDisplayingContext actionDisplaying() {
			return getRuleContext(ActionDisplayingContext.class,0);
		}
		public ActionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_action; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterAction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitAction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitAction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActionContext action() throws RecognitionException {
		ActionContext _localctx = new ActionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_action);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(146);
			actionBody();
			setState(159);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				{
				{
				setState(148);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==KEYWORD_YIELDING) {
					{
					setState(147);
					actionYielding();
					}
				}

				setState(151);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==KEYWORD_DISPLAYING) {
					{
					setState(150);
					actionDisplaying();
					}
				}

				}
				}
				break;
			case 2:
				{
				{
				setState(154);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==KEYWORD_DISPLAYING) {
					{
					setState(153);
					actionDisplaying();
					}
				}

				setState(157);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==KEYWORD_YIELDING) {
					{
					setState(156);
					actionYielding();
					}
				}

				}
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ActionBodyContext extends ParserRuleContext {
		public ActionIdContext actionId() {
			return getRuleContext(ActionIdContext.class,0);
		}
		public ActionArgsContext actionArgs() {
			return getRuleContext(ActionArgsContext.class,0);
		}
		public ActionBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actionBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterActionBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitActionBody(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitActionBody(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActionBodyContext actionBody() throws RecognitionException {
		ActionBodyContext _localctx = new ActionBodyContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_actionBody);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(161);
			actionId();
			setState(162);
			actionArgs();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ActionIdContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(TSLParserImpl.IDENTIFIER, 0); }
		public ActionIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actionId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterActionId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitActionId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitActionId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActionIdContext actionId() throws RecognitionException {
		ActionIdContext _localctx = new ActionIdContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_actionId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ActionArgsContext extends ParserRuleContext {
		public List<WordContext> word() {
			return getRuleContexts(WordContext.class);
		}
		public WordContext word(int i) {
			return getRuleContext(WordContext.class,i);
		}
		public List<WordNestContext> wordNest() {
			return getRuleContexts(WordNestContext.class);
		}
		public WordNestContext wordNest(int i) {
			return getRuleContext(WordNestContext.class,i);
		}
		public ActionArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actionArgs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterActionArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitActionArgs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitActionArgs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActionArgsContext actionArgs() throws RecognitionException {
		ActionArgsContext _localctx = new ActionArgsContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_actionArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(170);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 166199298L) != 0)) {
				{
				setState(168);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case EXPRESSION:
				case BEGIN_GROUP:
				case PLACEHOLDER:
				case CAPTURE_IDENTIFIER:
				case IDENTIFIER:
				case PLAIN_WORD:
					{
					setState(166);
					word();
					}
					break;
				case SIGN_LPARAN:
					{
					setState(167);
					wordNest();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(172);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ActionYieldingContext extends ParserRuleContext {
		public Token consumer;
		public TerminalNode KEYWORD_YIELDING() { return getToken(TSLParserImpl.KEYWORD_YIELDING, 0); }
		public TerminalNode EXPRESSION() { return getToken(TSLParserImpl.EXPRESSION, 0); }
		public TerminalNode CAPTURE_IDENTIFIER() { return getToken(TSLParserImpl.CAPTURE_IDENTIFIER, 0); }
		public ActionYieldingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actionYielding; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterActionYielding(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitActionYielding(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitActionYielding(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActionYieldingContext actionYielding() throws RecognitionException {
		ActionYieldingContext _localctx = new ActionYieldingContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_actionYielding);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(173);
			match(KEYWORD_YIELDING);
			setState(176);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case CAPTURE_IDENTIFIER:
				{
				setState(174);
				((ActionYieldingContext)_localctx).consumer = match(CAPTURE_IDENTIFIER);
				}
				break;
			case EXPRESSION:
				{
				setState(175);
				match(EXPRESSION);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ActionDisplayingContext extends ParserRuleContext {
		public TerminalNode KEYWORD_DISPLAYING() { return getToken(TSLParserImpl.KEYWORD_DISPLAYING, 0); }
		public WordContext word() {
			return getRuleContext(WordContext.class,0);
		}
		public ActionDisplayingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actionDisplaying; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterActionDisplaying(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitActionDisplaying(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitActionDisplaying(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActionDisplayingContext actionDisplaying() throws RecognitionException {
		ActionDisplayingContext _localctx = new ActionDisplayingContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_actionDisplaying);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			match(KEYWORD_DISPLAYING);
			setState(179);
			word();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EventContext extends ParserRuleContext {
		public TerminalNode KEYWORD_ON() { return getToken(TSLParserImpl.KEYWORD_ON, 0); }
		public EventNameContext eventName() {
			return getRuleContext(EventNameContext.class,0);
		}
		public List<EventPredicateContext> eventPredicate() {
			return getRuleContexts(EventPredicateContext.class);
		}
		public EventPredicateContext eventPredicate(int i) {
			return getRuleContext(EventPredicateContext.class,i);
		}
		public EventContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_event; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterEvent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitEvent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitEvent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EventContext event() throws RecognitionException {
		EventContext _localctx = new EventContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_event);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(181);
			match(KEYWORD_ON);
			setState(182);
			eventName();
			setState(186);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==KEYWORD_WITH) {
				{
				{
				setState(183);
				eventPredicate();
				}
				}
				setState(188);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EventNameContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(TSLParserImpl.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(TSLParserImpl.IDENTIFIER, i);
		}
		public EventNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eventName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterEventName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitEventName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitEventName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EventNameContext eventName() throws RecognitionException {
		EventNameContext _localctx = new EventNameContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_eventName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(190); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(189);
				match(IDENTIFIER);
				}
				}
				setState(192); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==IDENTIFIER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EventFromContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(TSLParserImpl.IDENTIFIER, 0); }
		public EventFromContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eventFrom; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterEventFrom(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitEventFrom(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitEventFrom(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EventFromContext eventFrom() throws RecognitionException {
		EventFromContext _localctx = new EventFromContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_eventFrom);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(194);
			match(IDENTIFIER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class EventPredicateContext extends ParserRuleContext {
		public TerminalNode KEYWORD_WITH() { return getToken(TSLParserImpl.KEYWORD_WITH, 0); }
		public PredicateExpressionContext predicateExpression() {
			return getRuleContext(PredicateExpressionContext.class,0);
		}
		public PredicateOperationContext predicateOperation() {
			return getRuleContext(PredicateOperationContext.class,0);
		}
		public EventPredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eventPredicate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterEventPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitEventPredicate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitEventPredicate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EventPredicateContext eventPredicate() throws RecognitionException {
		EventPredicateContext _localctx = new EventPredicateContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_eventPredicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(196);
			match(KEYWORD_WITH);
			setState(199);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EXPRESSION:
				{
				setState(197);
				predicateExpression();
				}
				break;
			case IDENTIFIER:
				{
				setState(198);
				predicateOperation();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PredicateExpressionContext extends ParserRuleContext {
		public TerminalNode EXPRESSION() { return getToken(TSLParserImpl.EXPRESSION, 0); }
		public PredicateExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicateExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterPredicateExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitPredicateExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitPredicateExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateExpressionContext predicateExpression() throws RecognitionException {
		PredicateExpressionContext _localctx = new PredicateExpressionContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_predicateExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(201);
			match(EXPRESSION);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PredicateOperationContext extends ParserRuleContext {
		public Token field;
		public PredicateOperatorContext predicateOperator() {
			return getRuleContext(PredicateOperatorContext.class,0);
		}
		public PredicateWordContext predicateWord() {
			return getRuleContext(PredicateWordContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(TSLParserImpl.IDENTIFIER, 0); }
		public PredicateOperationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicateOperation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterPredicateOperation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitPredicateOperation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitPredicateOperation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateOperationContext predicateOperation() throws RecognitionException {
		PredicateOperationContext _localctx = new PredicateOperationContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_predicateOperation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(203);
			((PredicateOperationContext)_localctx).field = match(IDENTIFIER);
			}
			setState(204);
			predicateOperator();
			setState(205);
			predicateWord();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PredicateOperatorContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER() { return getTokens(TSLParserImpl.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(TSLParserImpl.IDENTIFIER, i);
		}
		public TerminalNode SYMBOL_EQUALS() { return getToken(TSLParserImpl.SYMBOL_EQUALS, 0); }
		public TerminalNode SYMBOL_GT() { return getToken(TSLParserImpl.SYMBOL_GT, 0); }
		public TerminalNode SYMBOL_GTE() { return getToken(TSLParserImpl.SYMBOL_GTE, 0); }
		public TerminalNode SYMBOL_LT() { return getToken(TSLParserImpl.SYMBOL_LT, 0); }
		public TerminalNode SYMBOL_LTE() { return getToken(TSLParserImpl.SYMBOL_LTE, 0); }
		public PredicateOperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicateOperator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterPredicateOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitPredicateOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitPredicateOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateOperatorContext predicateOperator() throws RecognitionException {
		PredicateOperatorContext _localctx = new PredicateOperatorContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_predicateOperator);
		try {
			int _alt;
			setState(217);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(208); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(207);
						match(IDENTIFIER);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(210); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,25,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case SYMBOL_EQUALS:
				enterOuterAlt(_localctx, 2);
				{
				setState(212);
				match(SYMBOL_EQUALS);
				}
				break;
			case SYMBOL_GT:
				enterOuterAlt(_localctx, 3);
				{
				setState(213);
				match(SYMBOL_GT);
				}
				break;
			case SYMBOL_GTE:
				enterOuterAlt(_localctx, 4);
				{
				setState(214);
				match(SYMBOL_GTE);
				}
				break;
			case SYMBOL_LT:
				enterOuterAlt(_localctx, 5);
				{
				setState(215);
				match(SYMBOL_LT);
				}
				break;
			case SYMBOL_LTE:
				enterOuterAlt(_localctx, 6);
				{
				setState(216);
				match(SYMBOL_LTE);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CaptureRuleContext extends ParserRuleContext {
		public CaptureHeaderContext captureHeader() {
			return getRuleContext(CaptureHeaderContext.class,0);
		}
		public TerminalNode SYMBOL_EQUALS() { return getToken(TSLParserImpl.SYMBOL_EQUALS, 0); }
		public CaptureContentContext captureContent() {
			return getRuleContext(CaptureContentContext.class,0);
		}
		public CaptureRuleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_captureRule; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterCaptureRule(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitCaptureRule(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitCaptureRule(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaptureRuleContext captureRule() throws RecognitionException {
		CaptureRuleContext _localctx = new CaptureRuleContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_captureRule);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(219);
			captureHeader();
			setState(220);
			match(SYMBOL_EQUALS);
			setState(221);
			captureContent();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CaptureHeaderContext extends ParserRuleContext {
		public Token id;
		public TerminalNode CAPTURE_IDENTIFIER() { return getToken(TSLParserImpl.CAPTURE_IDENTIFIER, 0); }
		public CaptureParamsContext captureParams() {
			return getRuleContext(CaptureParamsContext.class,0);
		}
		public CaptureHeaderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_captureHeader; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterCaptureHeader(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitCaptureHeader(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitCaptureHeader(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaptureHeaderContext captureHeader() throws RecognitionException {
		CaptureHeaderContext _localctx = new CaptureHeaderContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_captureHeader);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(223);
			((CaptureHeaderContext)_localctx).id = match(CAPTURE_IDENTIFIER);
			}
			setState(225);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==SIGN_LPARAN) {
				{
				setState(224);
				captureParams();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CaptureContentContext extends ParserRuleContext {
		public List<WordContext> word() {
			return getRuleContexts(WordContext.class);
		}
		public WordContext word(int i) {
			return getRuleContext(WordContext.class,i);
		}
		public List<WordNestContext> wordNest() {
			return getRuleContexts(WordNestContext.class);
		}
		public WordNestContext wordNest(int i) {
			return getRuleContext(WordNestContext.class,i);
		}
		public CaptureContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_captureContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterCaptureContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitCaptureContent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitCaptureContent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaptureContentContext captureContent() throws RecognitionException {
		CaptureContentContext _localctx = new CaptureContentContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_captureContent);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(229); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(229);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case EXPRESSION:
				case BEGIN_GROUP:
				case PLACEHOLDER:
				case CAPTURE_IDENTIFIER:
				case IDENTIFIER:
				case PLAIN_WORD:
					{
					setState(227);
					word();
					}
					break;
				case SIGN_LPARAN:
					{
					setState(228);
					wordNest();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(231); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & 166199298L) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CaptureParamsContext extends ParserRuleContext {
		public TerminalNode SIGN_LPARAN() { return getToken(TSLParserImpl.SIGN_LPARAN, 0); }
		public TerminalNode SIGN_RPARAN() { return getToken(TSLParserImpl.SIGN_RPARAN, 0); }
		public List<TerminalNode> IDENTIFIER() { return getTokens(TSLParserImpl.IDENTIFIER); }
		public TerminalNode IDENTIFIER(int i) {
			return getToken(TSLParserImpl.IDENTIFIER, i);
		}
		public List<TerminalNode> SIGN_COMMA() { return getTokens(TSLParserImpl.SIGN_COMMA); }
		public TerminalNode SIGN_COMMA(int i) {
			return getToken(TSLParserImpl.SIGN_COMMA, i);
		}
		public CaptureParamsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_captureParams; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterCaptureParams(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitCaptureParams(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitCaptureParams(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaptureParamsContext captureParams() throws RecognitionException {
		CaptureParamsContext _localctx = new CaptureParamsContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_captureParams);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(233);
			match(SIGN_LPARAN);
			setState(242);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IDENTIFIER) {
				{
				setState(234);
				match(IDENTIFIER);
				setState(239);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SIGN_COMMA) {
					{
					{
					setState(235);
					match(SIGN_COMMA);
					setState(236);
					match(IDENTIFIER);
					}
					}
					setState(241);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(244);
			match(SIGN_RPARAN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GroupContext extends ParserRuleContext {
		public TerminalNode BEGIN_GROUP() { return getToken(TSLParserImpl.BEGIN_GROUP, 0); }
		public TerminalNode END_GROUP() { return getToken(TSLParserImpl.END_GROUP, 0); }
		public List<GroupStringContext> groupString() {
			return getRuleContexts(GroupStringContext.class);
		}
		public GroupStringContext groupString(int i) {
			return getRuleContext(GroupStringContext.class,i);
		}
		public List<GroupExpressionContext> groupExpression() {
			return getRuleContexts(GroupExpressionContext.class);
		}
		public GroupExpressionContext groupExpression(int i) {
			return getRuleContext(GroupExpressionContext.class,i);
		}
		public GroupContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_group; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterGroup(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitGroup(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitGroup(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupContext group() throws RecognitionException {
		GroupContext _localctx = new GroupContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_group);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(246);
			match(BEGIN_GROUP);
			setState(251);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==BEGIN_GROUP_EXPRESSION || _la==GROUP_STRING) {
				{
				setState(249);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case GROUP_STRING:
					{
					setState(247);
					groupString();
					}
					break;
				case BEGIN_GROUP_EXPRESSION:
					{
					setState(248);
					groupExpression();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(253);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(254);
			match(END_GROUP);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GroupStringContext extends ParserRuleContext {
		public List<TerminalNode> GROUP_STRING() { return getTokens(TSLParserImpl.GROUP_STRING); }
		public TerminalNode GROUP_STRING(int i) {
			return getToken(TSLParserImpl.GROUP_STRING, i);
		}
		public GroupStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupString; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterGroupString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitGroupString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitGroupString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupStringContext groupString() throws RecognitionException {
		GroupStringContext _localctx = new GroupStringContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_groupString);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(257); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(256);
					match(GROUP_STRING);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(259); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,34,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class GroupExpressionContext extends ParserRuleContext {
		public TerminalNode BEGIN_GROUP_EXPRESSION() { return getToken(TSLParserImpl.BEGIN_GROUP_EXPRESSION, 0); }
		public WordContext word() {
			return getRuleContext(WordContext.class,0);
		}
		public TerminalNode END_GROUP_EXPRESSION() { return getToken(TSLParserImpl.END_GROUP_EXPRESSION, 0); }
		public GroupExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_groupExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterGroupExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitGroupExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitGroupExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final GroupExpressionContext groupExpression() throws RecognitionException {
		GroupExpressionContext _localctx = new GroupExpressionContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_groupExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(261);
			match(BEGIN_GROUP_EXPRESSION);
			setState(262);
			word();
			setState(263);
			match(END_GROUP_EXPRESSION);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CaptureCallContext extends ParserRuleContext {
		public Token id;
		public TerminalNode CAPTURE_IDENTIFIER() { return getToken(TSLParserImpl.CAPTURE_IDENTIFIER, 0); }
		public CaptureArgsContext captureArgs() {
			return getRuleContext(CaptureArgsContext.class,0);
		}
		public CaptureCallContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_captureCall; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterCaptureCall(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitCaptureCall(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitCaptureCall(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaptureCallContext captureCall() throws RecognitionException {
		CaptureCallContext _localctx = new CaptureCallContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_captureCall);
		try {
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(265);
			((CaptureCallContext)_localctx).id = match(CAPTURE_IDENTIFIER);
			}
			setState(267);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				{
				setState(266);
				captureArgs();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CaptureArgsContext extends ParserRuleContext {
		public TerminalNode SIGN_LPARAN() { return getToken(TSLParserImpl.SIGN_LPARAN, 0); }
		public TerminalNode SIGN_RPARAN() { return getToken(TSLParserImpl.SIGN_RPARAN, 0); }
		public List<CaptureArgContext> captureArg() {
			return getRuleContexts(CaptureArgContext.class);
		}
		public CaptureArgContext captureArg(int i) {
			return getRuleContext(CaptureArgContext.class,i);
		}
		public List<TerminalNode> SIGN_COMMA() { return getTokens(TSLParserImpl.SIGN_COMMA); }
		public TerminalNode SIGN_COMMA(int i) {
			return getToken(TSLParserImpl.SIGN_COMMA, i);
		}
		public CaptureArgsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_captureArgs; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterCaptureArgs(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitCaptureArgs(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitCaptureArgs(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaptureArgsContext captureArgs() throws RecognitionException {
		CaptureArgsContext _localctx = new CaptureArgsContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_captureArgs);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(269);
			match(SIGN_LPARAN);
			setState(278);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & 166199298L) != 0)) {
				{
				setState(270);
				captureArg();
				setState(275);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==SIGN_COMMA) {
					{
					{
					setState(271);
					match(SIGN_COMMA);
					setState(272);
					captureArg();
					}
					}
					setState(277);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(280);
			match(SIGN_RPARAN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CaptureArgContext extends ParserRuleContext {
		public WordContext word() {
			return getRuleContext(WordContext.class,0);
		}
		public WordNestContext wordNest() {
			return getRuleContext(WordNestContext.class,0);
		}
		public CaptureArgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_captureArg; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterCaptureArg(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitCaptureArg(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitCaptureArg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CaptureArgContext captureArg() throws RecognitionException {
		CaptureArgContext _localctx = new CaptureArgContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_captureArg);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(284);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EXPRESSION:
			case BEGIN_GROUP:
			case PLACEHOLDER:
			case CAPTURE_IDENTIFIER:
			case IDENTIFIER:
			case PLAIN_WORD:
				{
				setState(282);
				word();
				}
				break;
			case SIGN_LPARAN:
				{
				setState(283);
				wordNest();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WordContext extends ParserRuleContext {
		public TerminalNode EXPRESSION() { return getToken(TSLParserImpl.EXPRESSION, 0); }
		public TerminalNode PLAIN_WORD() { return getToken(TSLParserImpl.PLAIN_WORD, 0); }
		public TerminalNode IDENTIFIER() { return getToken(TSLParserImpl.IDENTIFIER, 0); }
		public TerminalNode PLACEHOLDER() { return getToken(TSLParserImpl.PLACEHOLDER, 0); }
		public CaptureCallContext captureCall() {
			return getRuleContext(CaptureCallContext.class,0);
		}
		public GroupContext group() {
			return getRuleContext(GroupContext.class,0);
		}
		public WordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_word; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterWord(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitWord(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitWord(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WordContext word() throws RecognitionException {
		WordContext _localctx = new WordContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_word);
		try {
			setState(292);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EXPRESSION:
				enterOuterAlt(_localctx, 1);
				{
				setState(286);
				match(EXPRESSION);
				}
				break;
			case PLAIN_WORD:
				enterOuterAlt(_localctx, 2);
				{
				setState(287);
				match(PLAIN_WORD);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 3);
				{
				setState(288);
				match(IDENTIFIER);
				}
				break;
			case PLACEHOLDER:
				enterOuterAlt(_localctx, 4);
				{
				setState(289);
				match(PLACEHOLDER);
				}
				break;
			case CAPTURE_IDENTIFIER:
				enterOuterAlt(_localctx, 5);
				{
				setState(290);
				captureCall();
				}
				break;
			case BEGIN_GROUP:
				enterOuterAlt(_localctx, 6);
				{
				setState(291);
				group();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PredicateWordContext extends ParserRuleContext {
		public TerminalNode EXPRESSION() { return getToken(TSLParserImpl.EXPRESSION, 0); }
		public TerminalNode PLAIN_WORD() { return getToken(TSLParserImpl.PLAIN_WORD, 0); }
		public TerminalNode IDENTIFIER() { return getToken(TSLParserImpl.IDENTIFIER, 0); }
		public GroupContext group() {
			return getRuleContext(GroupContext.class,0);
		}
		public PredicateWordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicateWord; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterPredicateWord(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitPredicateWord(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitPredicateWord(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateWordContext predicateWord() throws RecognitionException {
		PredicateWordContext _localctx = new PredicateWordContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_predicateWord);
		try {
			setState(298);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case EXPRESSION:
				enterOuterAlt(_localctx, 1);
				{
				setState(294);
				match(EXPRESSION);
				}
				break;
			case PLAIN_WORD:
				enterOuterAlt(_localctx, 2);
				{
				setState(295);
				match(PLAIN_WORD);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 3);
				{
				setState(296);
				match(IDENTIFIER);
				}
				break;
			case BEGIN_GROUP:
				enterOuterAlt(_localctx, 4);
				{
				setState(297);
				group();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WordNestContext extends ParserRuleContext {
		public TerminalNode SIGN_LPARAN() { return getToken(TSLParserImpl.SIGN_LPARAN, 0); }
		public WordNestContentContext wordNestContent() {
			return getRuleContext(WordNestContentContext.class,0);
		}
		public TerminalNode SIGN_RPARAN() { return getToken(TSLParserImpl.SIGN_RPARAN, 0); }
		public WordNestContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wordNest; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterWordNest(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitWordNest(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitWordNest(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WordNestContext wordNest() throws RecognitionException {
		WordNestContext _localctx = new WordNestContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_wordNest);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(300);
			match(SIGN_LPARAN);
			setState(301);
			wordNestContent();
			setState(302);
			match(SIGN_RPARAN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class WordNestContentContext extends ParserRuleContext {
		public ActionContext action() {
			return getRuleContext(ActionContext.class,0);
		}
		public WordNestContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_wordNestContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).enterWordNestContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TSLParserImplListener ) ((TSLParserImplListener)listener).exitWordNestContent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TSLParserImplVisitor ) return ((TSLParserImplVisitor<? extends T>)visitor).visitWordNestContent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final WordNestContentContext wordNestContent() throws RecognitionException {
		WordNestContentContext _localctx = new WordNestContentContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_wordNestContent);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(304);
			action();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\u0004\u0001*\u0133\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0001"+
		"\u0000\u0005\u0000H\b\u0000\n\u0000\f\u0000K\t\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0001\u0005\u0001P\b\u0001\n\u0001\f\u0001S\t\u0001\u0001"+
		"\u0001\u0005\u0001V\b\u0001\n\u0001\f\u0001Y\t\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0005"+
		"\u0002b\b\u0002\n\u0002\f\u0002e\t\u0002\u0003\u0002g\b\u0002\u0001\u0003"+
		"\u0005\u0003j\b\u0003\n\u0003\f\u0003m\t\u0003\u0001\u0003\u0003\u0003"+
		"p\b\u0003\u0001\u0003\u0001\u0003\u0003\u0003t\b\u0003\u0001\u0004\u0001"+
		"\u0004\u0003\u0004x\b\u0004\u0004\u0004z\b\u0004\u000b\u0004\f\u0004{"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005\u0004\u0005\u0082\b\u0005"+
		"\u000b\u0005\f\u0005\u0083\u0001\u0005\u0003\u0005\u0087\b\u0005\u0001"+
		"\u0006\u0001\u0006\u0005\u0006\u008b\b\u0006\n\u0006\f\u0006\u008e\t\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0003\b\u0095\b\b"+
		"\u0001\b\u0003\b\u0098\b\b\u0001\b\u0003\b\u009b\b\b\u0001\b\u0003\b\u009e"+
		"\b\b\u0003\b\u00a0\b\b\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\u000b"+
		"\u0001\u000b\u0005\u000b\u00a9\b\u000b\n\u000b\f\u000b\u00ac\t\u000b\u0001"+
		"\f\u0001\f\u0001\f\u0003\f\u00b1\b\f\u0001\r\u0001\r\u0001\r\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0005\u000e\u00b9\b\u000e\n\u000e\f\u000e\u00bc"+
		"\t\u000e\u0001\u000f\u0004\u000f\u00bf\b\u000f\u000b\u000f\f\u000f\u00c0"+
		"\u0001\u0010\u0001\u0010\u0001\u0011\u0001\u0011\u0001\u0011\u0003\u0011"+
		"\u00c8\b\u0011\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0014\u0004\u0014\u00d1\b\u0014\u000b\u0014\f\u0014"+
		"\u00d2\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0003"+
		"\u0014\u00da\b\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015\u0001"+
		"\u0016\u0001\u0016\u0003\u0016\u00e2\b\u0016\u0001\u0017\u0001\u0017\u0004"+
		"\u0017\u00e6\b\u0017\u000b\u0017\f\u0017\u00e7\u0001\u0018\u0001\u0018"+
		"\u0001\u0018\u0001\u0018\u0005\u0018\u00ee\b\u0018\n\u0018\f\u0018\u00f1"+
		"\t\u0018\u0003\u0018\u00f3\b\u0018\u0001\u0018\u0001\u0018\u0001\u0019"+
		"\u0001\u0019\u0001\u0019\u0005\u0019\u00fa\b\u0019\n\u0019\f\u0019\u00fd"+
		"\t\u0019\u0001\u0019\u0001\u0019\u0001\u001a\u0004\u001a\u0102\b\u001a"+
		"\u000b\u001a\f\u001a\u0103\u0001\u001b\u0001\u001b\u0001\u001b\u0001\u001b"+
		"\u0001\u001c\u0001\u001c\u0003\u001c\u010c\b\u001c\u0001\u001d\u0001\u001d"+
		"\u0001\u001d\u0001\u001d\u0005\u001d\u0112\b\u001d\n\u001d\f\u001d\u0115"+
		"\t\u001d\u0003\u001d\u0117\b\u001d\u0001\u001d\u0001\u001d\u0001\u001e"+
		"\u0001\u001e\u0003\u001e\u011d\b\u001e\u0001\u001f\u0001\u001f\u0001\u001f"+
		"\u0001\u001f\u0001\u001f\u0001\u001f\u0003\u001f\u0125\b\u001f\u0001 "+
		"\u0001 \u0001 \u0001 \u0003 \u012b\b \u0001!\u0001!\u0001!\u0001!\u0001"+
		"\"\u0001\"\u0001\"\u0000\u0000#\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010"+
		"\u0012\u0014\u0016\u0018\u001a\u001c\u001e \"$&(*,.02468:<>@BD\u0000\u0000"+
		"\u0142\u0000I\u0001\u0000\u0000\u0000\u0002Q\u0001\u0000\u0000\u0000\u0004"+
		"f\u0001\u0000\u0000\u0000\u0006k\u0001\u0000\u0000\u0000\by\u0001\u0000"+
		"\u0000\u0000\n}\u0001\u0000\u0000\u0000\f\u008c\u0001\u0000\u0000\u0000"+
		"\u000e\u008f\u0001\u0000\u0000\u0000\u0010\u0092\u0001\u0000\u0000\u0000"+
		"\u0012\u00a1\u0001\u0000\u0000\u0000\u0014\u00a4\u0001\u0000\u0000\u0000"+
		"\u0016\u00aa\u0001\u0000\u0000\u0000\u0018\u00ad\u0001\u0000\u0000\u0000"+
		"\u001a\u00b2\u0001\u0000\u0000\u0000\u001c\u00b5\u0001\u0000\u0000\u0000"+
		"\u001e\u00be\u0001\u0000\u0000\u0000 \u00c2\u0001\u0000\u0000\u0000\""+
		"\u00c4\u0001\u0000\u0000\u0000$\u00c9\u0001\u0000\u0000\u0000&\u00cb\u0001"+
		"\u0000\u0000\u0000(\u00d9\u0001\u0000\u0000\u0000*\u00db\u0001\u0000\u0000"+
		"\u0000,\u00df\u0001\u0000\u0000\u0000.\u00e5\u0001\u0000\u0000\u00000"+
		"\u00e9\u0001\u0000\u0000\u00002\u00f6\u0001\u0000\u0000\u00004\u0101\u0001"+
		"\u0000\u0000\u00006\u0105\u0001\u0000\u0000\u00008\u0109\u0001\u0000\u0000"+
		"\u0000:\u010d\u0001\u0000\u0000\u0000<\u011c\u0001\u0000\u0000\u0000>"+
		"\u0124\u0001\u0000\u0000\u0000@\u012a\u0001\u0000\u0000\u0000B\u012c\u0001"+
		"\u0000\u0000\u0000D\u0130\u0001\u0000\u0000\u0000FH\u0003>\u001f\u0000"+
		"GF\u0001\u0000\u0000\u0000HK\u0001\u0000\u0000\u0000IG\u0001\u0000\u0000"+
		"\u0000IJ\u0001\u0000\u0000\u0000JL\u0001\u0000\u0000\u0000KI\u0001\u0000"+
		"\u0000\u0000LM\u0005\u0000\u0000\u0001M\u0001\u0001\u0000\u0000\u0000"+
		"NP\u0005\u0005\u0000\u0000ON\u0001\u0000\u0000\u0000PS\u0001\u0000\u0000"+
		"\u0000QO\u0001\u0000\u0000\u0000QR\u0001\u0000\u0000\u0000RW\u0001\u0000"+
		"\u0000\u0000SQ\u0001\u0000\u0000\u0000TV\u0003\n\u0005\u0000UT\u0001\u0000"+
		"\u0000\u0000VY\u0001\u0000\u0000\u0000WU\u0001\u0000\u0000\u0000WX\u0001"+
		"\u0000\u0000\u0000XZ\u0001\u0000\u0000\u0000YW\u0001\u0000\u0000\u0000"+
		"Z[\u0003\u0004\u0002\u0000[\\\u0005\u0000\u0000\u0001\\\u0003\u0001\u0000"+
		"\u0000\u0000]g\u0001\u0000\u0000\u0000^c\u0003\u0006\u0003\u0000_`\u0005"+
		"\u0005\u0000\u0000`b\u0003\u0006\u0003\u0000a_\u0001\u0000\u0000\u0000"+
		"be\u0001\u0000\u0000\u0000ca\u0001\u0000\u0000\u0000cd\u0001\u0000\u0000"+
		"\u0000dg\u0001\u0000\u0000\u0000ec\u0001\u0000\u0000\u0000f]\u0001\u0000"+
		"\u0000\u0000f^\u0001\u0000\u0000\u0000g\u0005\u0001\u0000\u0000\u0000"+
		"hj\u0005\u0005\u0000\u0000ih\u0001\u0000\u0000\u0000jm\u0001\u0000\u0000"+
		"\u0000ki\u0001\u0000\u0000\u0000kl\u0001\u0000\u0000\u0000lo\u0001\u0000"+
		"\u0000\u0000mk\u0001\u0000\u0000\u0000np\u0003\b\u0004\u0000on\u0001\u0000"+
		"\u0000\u0000op\u0001\u0000\u0000\u0000ps\u0001\u0000\u0000\u0000qt\u0003"+
		"\u000e\u0007\u0000rt\u0003*\u0015\u0000sq\u0001\u0000\u0000\u0000sr\u0001"+
		"\u0000\u0000\u0000t\u0007\u0001\u0000\u0000\u0000uw\u0005\u0002\u0000"+
		"\u0000vx\u0005\u0005\u0000\u0000wv\u0001\u0000\u0000\u0000wx\u0001\u0000"+
		"\u0000\u0000xz\u0001\u0000\u0000\u0000yu\u0001\u0000\u0000\u0000z{\u0001"+
		"\u0000\u0000\u0000{y\u0001\u0000\u0000\u0000{|\u0001\u0000\u0000\u0000"+
		"|\t\u0001\u0000\u0000\u0000}~\u0005\u0012\u0000\u0000~\u007f\u0005\u0017"+
		"\u0000\u0000\u007f\u0086\u0003\f\u0006\u0000\u0080\u0082\u0005\u0005\u0000"+
		"\u0000\u0081\u0080\u0001\u0000\u0000\u0000\u0082\u0083\u0001\u0000\u0000"+
		"\u0000\u0083\u0081\u0001\u0000\u0000\u0000\u0083\u0084\u0001\u0000\u0000"+
		"\u0000\u0084\u0087\u0001\u0000\u0000\u0000\u0085\u0087\u0005\u0000\u0000"+
		"\u0001\u0086\u0081\u0001\u0000\u0000\u0000\u0086\u0085\u0001\u0000\u0000"+
		"\u0000\u0087\u000b\u0001\u0000\u0000\u0000\u0088\u008b\u0003>\u001f\u0000"+
		"\u0089\u008b\u0005\t\u0000\u0000\u008a\u0088\u0001\u0000\u0000\u0000\u008a"+
		"\u0089\u0001\u0000\u0000\u0000\u008b\u008e\u0001\u0000\u0000\u0000\u008c"+
		"\u008a\u0001\u0000\u0000\u0000\u008c\u008d\u0001\u0000\u0000\u0000\u008d"+
		"\r\u0001\u0000\u0000\u0000\u008e\u008c\u0001\u0000\u0000\u0000\u008f\u0090"+
		"\u0003\u0010\b\u0000\u0090\u0091\u0003\u001c\u000e\u0000\u0091\u000f\u0001"+
		"\u0000\u0000\u0000\u0092\u009f\u0003\u0012\t\u0000\u0093\u0095\u0003\u0018"+
		"\f\u0000\u0094\u0093\u0001\u0000\u0000\u0000\u0094\u0095\u0001\u0000\u0000"+
		"\u0000\u0095\u0097\u0001\u0000\u0000\u0000\u0096\u0098\u0003\u001a\r\u0000"+
		"\u0097\u0096\u0001\u0000\u0000\u0000\u0097\u0098\u0001\u0000\u0000\u0000"+
		"\u0098\u00a0\u0001\u0000\u0000\u0000\u0099\u009b\u0003\u001a\r\u0000\u009a"+
		"\u0099\u0001\u0000\u0000\u0000\u009a\u009b\u0001\u0000\u0000\u0000\u009b"+
		"\u009d\u0001\u0000\u0000\u0000\u009c\u009e\u0003\u0018\f\u0000\u009d\u009c"+
		"\u0001\u0000\u0000\u0000\u009d\u009e\u0001\u0000\u0000\u0000\u009e\u00a0"+
		"\u0001\u0000\u0000\u0000\u009f\u0094\u0001\u0000\u0000\u0000\u009f\u009a"+
		"\u0001\u0000\u0000\u0000\u00a0\u0011\u0001\u0000\u0000\u0000\u00a1\u00a2"+
		"\u0003\u0014\n\u0000\u00a2\u00a3\u0003\u0016\u000b\u0000\u00a3\u0013\u0001"+
		"\u0000\u0000\u0000\u00a4\u00a5\u0005\u0017\u0000\u0000\u00a5\u0015\u0001"+
		"\u0000\u0000\u0000\u00a6\u00a9\u0003>\u001f\u0000\u00a7\u00a9\u0003B!"+
		"\u0000\u00a8\u00a6\u0001\u0000\u0000\u0000\u00a8\u00a7\u0001\u0000\u0000"+
		"\u0000\u00a9\u00ac\u0001\u0000\u0000\u0000\u00aa\u00a8\u0001\u0000\u0000"+
		"\u0000\u00aa\u00ab\u0001\u0000\u0000\u0000\u00ab\u0017\u0001\u0000\u0000"+
		"\u0000\u00ac\u00aa\u0001\u0000\u0000\u0000\u00ad\u00b0\u0005\f\u0000\u0000"+
		"\u00ae\u00b1\u0005\u0016\u0000\u0000\u00af\u00b1\u0005\u0001\u0000\u0000"+
		"\u00b0\u00ae\u0001\u0000\u0000\u0000\u00b0\u00af\u0001\u0000\u0000\u0000"+
		"\u00b1\u0019\u0001\u0000\u0000\u0000\u00b2\u00b3\u0005\u000b\u0000\u0000"+
		"\u00b3\u00b4\u0003>\u001f\u0000\u00b4\u001b\u0001\u0000\u0000\u0000\u00b5"+
		"\u00b6\u0005\b\u0000\u0000\u00b6\u00ba\u0003\u001e\u000f\u0000\u00b7\u00b9"+
		"\u0003\"\u0011\u0000\u00b8\u00b7\u0001\u0000\u0000\u0000\u00b9\u00bc\u0001"+
		"\u0000\u0000\u0000\u00ba\u00b8\u0001\u0000\u0000\u0000\u00ba\u00bb\u0001"+
		"\u0000\u0000\u0000\u00bb\u001d\u0001\u0000\u0000\u0000\u00bc\u00ba\u0001"+
		"\u0000\u0000\u0000\u00bd\u00bf\u0005\u0017\u0000\u0000\u00be\u00bd\u0001"+
		"\u0000\u0000\u0000\u00bf\u00c0\u0001\u0000\u0000\u0000\u00c0\u00be\u0001"+
		"\u0000\u0000\u0000\u00c0\u00c1\u0001\u0000\u0000\u0000\u00c1\u001f\u0001"+
		"\u0000\u0000\u0000\u00c2\u00c3\u0005\u0017\u0000\u0000\u00c3!\u0001\u0000"+
		"\u0000\u0000\u00c4\u00c7\u0005\n\u0000\u0000\u00c5\u00c8\u0003$\u0012"+
		"\u0000\u00c6\u00c8\u0003&\u0013\u0000\u00c7\u00c5\u0001\u0000\u0000\u0000"+
		"\u00c7\u00c6\u0001\u0000\u0000\u0000\u00c8#\u0001\u0000\u0000\u0000\u00c9"+
		"\u00ca\u0005\u0001\u0000\u0000\u00ca%\u0001\u0000\u0000\u0000\u00cb\u00cc"+
		"\u0005\u0017\u0000\u0000\u00cc\u00cd\u0003(\u0014\u0000\u00cd\u00ce\u0003"+
		"@ \u0000\u00ce\'\u0001\u0000\u0000\u0000\u00cf\u00d1\u0005\u0017\u0000"+
		"\u0000\u00d0\u00cf\u0001\u0000\u0000\u0000\u00d1\u00d2\u0001\u0000\u0000"+
		"\u0000\u00d2\u00d0\u0001\u0000\u0000\u0000\u00d2\u00d3\u0001\u0000\u0000"+
		"\u0000\u00d3\u00da\u0001\u0000\u0000\u0000\u00d4\u00da\u0005\r\u0000\u0000"+
		"\u00d5\u00da\u0005\u000e\u0000\u0000\u00d6\u00da\u0005\u000f\u0000\u0000"+
		"\u00d7\u00da\u0005\u0010\u0000\u0000\u00d8\u00da\u0005\u0011\u0000\u0000"+
		"\u00d9\u00d0\u0001\u0000\u0000\u0000\u00d9\u00d4\u0001\u0000\u0000\u0000"+
		"\u00d9\u00d5\u0001\u0000\u0000\u0000\u00d9\u00d6\u0001\u0000\u0000\u0000"+
		"\u00d9\u00d7\u0001\u0000\u0000\u0000\u00d9\u00d8\u0001\u0000\u0000\u0000"+
		"\u00da)\u0001\u0000\u0000\u0000\u00db\u00dc\u0003,\u0016\u0000\u00dc\u00dd"+
		"\u0005\r\u0000\u0000\u00dd\u00de\u0003.\u0017\u0000\u00de+\u0001\u0000"+
		"\u0000\u0000\u00df\u00e1\u0005\u0016\u0000\u0000\u00e0\u00e2\u00030\u0018"+
		"\u0000\u00e1\u00e0\u0001\u0000\u0000\u0000\u00e1\u00e2\u0001\u0000\u0000"+
		"\u0000\u00e2-\u0001\u0000\u0000\u0000\u00e3\u00e6\u0003>\u001f\u0000\u00e4"+
		"\u00e6\u0003B!\u0000\u00e5\u00e3\u0001\u0000\u0000\u0000\u00e5\u00e4\u0001"+
		"\u0000\u0000\u0000\u00e6\u00e7\u0001\u0000\u0000\u0000\u00e7\u00e5\u0001"+
		"\u0000\u0000\u0000\u00e7\u00e8\u0001\u0000\u0000\u0000\u00e8/\u0001\u0000"+
		"\u0000\u0000\u00e9\u00f2\u0005\u001b\u0000\u0000\u00ea\u00ef\u0005\u0017"+
		"\u0000\u0000\u00eb\u00ec\u0005\u001d\u0000\u0000\u00ec\u00ee\u0005\u0017"+
		"\u0000\u0000\u00ed\u00eb\u0001\u0000\u0000\u0000\u00ee\u00f1\u0001\u0000"+
		"\u0000\u0000\u00ef\u00ed\u0001\u0000\u0000\u0000\u00ef\u00f0\u0001\u0000"+
		"\u0000\u0000\u00f0\u00f3\u0001\u0000\u0000\u0000\u00f1\u00ef\u0001\u0000"+
		"\u0000\u0000\u00f2\u00ea\u0001\u0000\u0000\u0000\u00f2\u00f3\u0001\u0000"+
		"\u0000\u0000\u00f3\u00f4\u0001\u0000\u0000\u0000\u00f4\u00f5\u0005\u001c"+
		"\u0000\u0000\u00f51\u0001\u0000\u0000\u0000\u00f6\u00fb\u0005\u0013\u0000"+
		"\u0000\u00f7\u00fa\u00034\u001a\u0000\u00f8\u00fa\u00036\u001b\u0000\u00f9"+
		"\u00f7\u0001\u0000\u0000\u0000\u00f9\u00f8\u0001\u0000\u0000\u0000\u00fa"+
		"\u00fd\u0001\u0000\u0000\u0000\u00fb\u00f9\u0001\u0000\u0000\u0000\u00fb"+
		"\u00fc\u0001\u0000\u0000\u0000\u00fc\u00fe\u0001\u0000\u0000\u0000\u00fd"+
		"\u00fb\u0001\u0000\u0000\u0000\u00fe\u00ff\u0005 \u0000\u0000\u00ff3\u0001"+
		"\u0000\u0000\u0000\u0100\u0102\u0005!\u0000\u0000\u0101\u0100\u0001\u0000"+
		"\u0000\u0000\u0102\u0103\u0001\u0000\u0000\u0000\u0103\u0101\u0001\u0000"+
		"\u0000\u0000\u0103\u0104\u0001\u0000\u0000\u0000\u01045\u0001\u0000\u0000"+
		"\u0000\u0105\u0106\u0005\u001f\u0000\u0000\u0106\u0107\u0003>\u001f\u0000"+
		"\u0107\u0108\u0005\u0014\u0000\u0000\u01087\u0001\u0000\u0000\u0000\u0109"+
		"\u010b\u0005\u0016\u0000\u0000\u010a\u010c\u0003:\u001d\u0000\u010b\u010a"+
		"\u0001\u0000\u0000\u0000\u010b\u010c\u0001\u0000\u0000\u0000\u010c9\u0001"+
		"\u0000\u0000\u0000\u010d\u0116\u0005\u001b\u0000\u0000\u010e\u0113\u0003"+
		"<\u001e\u0000\u010f\u0110\u0005\u001d\u0000\u0000\u0110\u0112\u0003<\u001e"+
		"\u0000\u0111\u010f\u0001\u0000\u0000\u0000\u0112\u0115\u0001\u0000\u0000"+
		"\u0000\u0113\u0111\u0001\u0000\u0000\u0000\u0113\u0114\u0001\u0000\u0000"+
		"\u0000\u0114\u0117\u0001\u0000\u0000\u0000\u0115\u0113\u0001\u0000\u0000"+
		"\u0000\u0116\u010e\u0001\u0000\u0000\u0000\u0116\u0117\u0001\u0000\u0000"+
		"\u0000\u0117\u0118\u0001\u0000\u0000\u0000\u0118\u0119\u0005\u001c\u0000"+
		"\u0000\u0119;\u0001\u0000\u0000\u0000\u011a\u011d\u0003>\u001f\u0000\u011b"+
		"\u011d\u0003B!\u0000\u011c\u011a\u0001\u0000\u0000\u0000\u011c\u011b\u0001"+
		"\u0000\u0000\u0000\u011d=\u0001\u0000\u0000\u0000\u011e\u0125\u0005\u0001"+
		"\u0000\u0000\u011f\u0125\u0005\u0018\u0000\u0000\u0120\u0125\u0005\u0017"+
		"\u0000\u0000\u0121\u0125\u0005\u0015\u0000\u0000\u0122\u0125\u00038\u001c"+
		"\u0000\u0123\u0125\u00032\u0019\u0000\u0124\u011e\u0001\u0000\u0000\u0000"+
		"\u0124\u011f\u0001\u0000\u0000\u0000\u0124\u0120\u0001\u0000\u0000\u0000"+
		"\u0124\u0121\u0001\u0000\u0000\u0000\u0124\u0122\u0001\u0000\u0000\u0000"+
		"\u0124\u0123\u0001\u0000\u0000\u0000\u0125?\u0001\u0000\u0000\u0000\u0126"+
		"\u012b\u0005\u0001\u0000\u0000\u0127\u012b\u0005\u0018\u0000\u0000\u0128"+
		"\u012b\u0005\u0017\u0000\u0000\u0129\u012b\u00032\u0019\u0000\u012a\u0126"+
		"\u0001\u0000\u0000\u0000\u012a\u0127\u0001\u0000\u0000\u0000\u012a\u0128"+
		"\u0001\u0000\u0000\u0000\u012a\u0129\u0001\u0000\u0000\u0000\u012bA\u0001"+
		"\u0000\u0000\u0000\u012c\u012d\u0005\u001b\u0000\u0000\u012d\u012e\u0003"+
		"D\"\u0000\u012e\u012f\u0005\u001c\u0000\u0000\u012fC\u0001\u0000\u0000"+
		"\u0000\u0130\u0131\u0003\u0010\b\u0000\u0131E\u0001\u0000\u0000\u0000"+
		")IQWcfkosw{\u0083\u0086\u008a\u008c\u0094\u0097\u009a\u009d\u009f\u00a8"+
		"\u00aa\u00b0\u00ba\u00c0\u00c7\u00d2\u00d9\u00e1\u00e5\u00e7\u00ef\u00f2"+
		"\u00f9\u00fb\u0103\u010b\u0113\u0116\u011c\u0124\u012a";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}