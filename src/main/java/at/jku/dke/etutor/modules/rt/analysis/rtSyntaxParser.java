// Generated from C:/ETutor/dke-dispatcher/src/main/java/at/jku/dke/etutor/modules/rt/analysis/rtSyntax.g4 by ANTLR 4.13.1
package at.jku.dke.etutor.modules.rt.analysis;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class rtSyntaxParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, STRING=4, ATT_BLOCK=5, INK=6, INK_BLOCK=7;
	public static final int
		RULE_start = 0;
	private static String[] makeRuleNames() {
		return new String[] {
			"start"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "'|'", "')'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, "STRING", "ATT_BLOCK", "INK", "INK_BLOCK"
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
	public String getGrammarFileName() { return "rtSyntax.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public rtSyntaxParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StartContext extends ParserRuleContext {
		public List<TerminalNode> STRING() { return getTokens(rtSyntaxParser.STRING); }
		public TerminalNode STRING(int i) {
			return getToken(rtSyntaxParser.STRING, i);
		}
		public TerminalNode EOF() { return getToken(rtSyntaxParser.EOF, 0); }
		public List<TerminalNode> ATT_BLOCK() { return getTokens(rtSyntaxParser.ATT_BLOCK); }
		public TerminalNode ATT_BLOCK(int i) {
			return getToken(rtSyntaxParser.ATT_BLOCK, i);
		}
		public TerminalNode INK() { return getToken(rtSyntaxParser.INK, 0); }
		public TerminalNode INK_BLOCK() { return getToken(rtSyntaxParser.INK_BLOCK, 0); }
		public StartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof rtSyntaxListener ) ((rtSyntaxListener)listener).enterStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof rtSyntaxListener ) ((rtSyntaxListener)listener).exitStart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof rtSyntaxVisitor ) return ((rtSyntaxVisitor<? extends T>)visitor).visitStart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StartContext start() throws RecognitionException {
		StartContext _localctx = new StartContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_start);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(2);
			match(STRING);
			setState(3);
			match(T__0);
			setState(4);
			match(T__1);
			setState(5);
			_la = _input.LA(1);
			if ( !(_la==STRING || _la==ATT_BLOCK) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(6);
			match(T__1);
			setState(8);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STRING || _la==ATT_BLOCK) {
				{
				setState(7);
				_la = _input.LA(1);
				if ( !(_la==STRING || _la==ATT_BLOCK) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(10);
			match(T__2);
			setState(12);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==INK || _la==INK_BLOCK) {
				{
				setState(11);
				_la = _input.LA(1);
				if ( !(_la==INK || _la==INK_BLOCK) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(14);
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

	public static final String _serializedATN =
		"\u0004\u0001\u0007\u0011\u0002\u0000\u0007\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0003\u0000\t\b\u0000"+
		"\u0001\u0000\u0001\u0000\u0003\u0000\r\b\u0000\u0001\u0000\u0001\u0000"+
		"\u0001\u0000\u0000\u0000\u0001\u0000\u0000\u0002\u0001\u0000\u0004\u0005"+
		"\u0001\u0000\u0006\u0007\u0011\u0000\u0002\u0001\u0000\u0000\u0000\u0002"+
		"\u0003\u0005\u0004\u0000\u0000\u0003\u0004\u0005\u0001\u0000\u0000\u0004"+
		"\u0005\u0005\u0002\u0000\u0000\u0005\u0006\u0007\u0000\u0000\u0000\u0006"+
		"\b\u0005\u0002\u0000\u0000\u0007\t\u0007\u0000\u0000\u0000\b\u0007\u0001"+
		"\u0000\u0000\u0000\b\t\u0001\u0000\u0000\u0000\t\n\u0001\u0000\u0000\u0000"+
		"\n\f\u0005\u0003\u0000\u0000\u000b\r\u0007\u0001\u0000\u0000\f\u000b\u0001"+
		"\u0000\u0000\u0000\f\r\u0001\u0000\u0000\u0000\r\u000e\u0001\u0000\u0000"+
		"\u0000\u000e\u000f\u0005\u0000\u0000\u0001\u000f\u0001\u0001\u0000\u0000"+
		"\u0000\u0002\b\f";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}