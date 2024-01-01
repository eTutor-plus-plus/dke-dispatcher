// Generated from NF.g4 by ANTLR 4.13.1

package at.jku.dke.etutor.modules.nf.parser ;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class NFLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, Integer=16, 
		AlphaNumericChain=17, Digit=18, Letter=19, WhiteSpace=20;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "Integer", "AlphaNumericChain", 
			"Digit", "Letter", "WhiteSpace"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "':'", "'('", "')'", "'->'", "'#'", "'*'", "'.'", "'1NF'", 
			"'2NF'", "'3NF'", "'BCNF'", "'-'", "'>'", "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, "Integer", "AlphaNumericChain", "Digit", "Letter", 
			"WhiteSpace"
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


	public NFLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "NF.g4"; }

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

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 16:
			AlphaNumericChain_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void AlphaNumericChain_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			break;
		}
	}

	public static final String _serializedATN =
		"\u0004\u0000\u0014k\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"+
		"\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002"+
		"\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0001\u0000\u0001\u0000\u0001"+
		"\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001"+
		"\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t"+
		"\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\r\u0001"+
		"\r\u0001\u000e\u0001\u000e\u0001\u000f\u0004\u000fS\b\u000f\u000b\u000f"+
		"\f\u000fT\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0005\u0010\\\b\u0010\n\u0010\f\u0010_\t\u0010\u0001\u0011\u0001\u0011"+
		"\u0001\u0012\u0001\u0012\u0001\u0013\u0004\u0013f\b\u0013\u000b\u0013"+
		"\f\u0013g\u0001\u0013\u0001\u0013\u0000\u0000\u0014\u0001\u0001\u0003"+
		"\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007\u000f\b\u0011"+
		"\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e\u001d\u000f\u001f\u0010"+
		"!\u0011#\u0012%\u0013\'\u0014\u0001\u0000\u0002\u0002\u0000AZaz\u0003"+
		"\u0000\t\n\r\r  o\u0000\u0001\u0001\u0000\u0000\u0000\u0000\u0003\u0001"+
		"\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000\u0007\u0001"+
		"\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b\u0001\u0000"+
		"\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001\u0000\u0000"+
		"\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001\u0000\u0000"+
		"\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001\u0000\u0000"+
		"\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001\u0000\u0000"+
		"\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001\u0000\u0000"+
		"\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000\u0000\u0000"+
		"%\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000\u0000\u0001)\u0001"+
		"\u0000\u0000\u0000\u0003+\u0001\u0000\u0000\u0000\u0005-\u0001\u0000\u0000"+
		"\u0000\u0007/\u0001\u0000\u0000\u0000\t1\u0001\u0000\u0000\u0000\u000b"+
		"4\u0001\u0000\u0000\u0000\r6\u0001\u0000\u0000\u0000\u000f8\u0001\u0000"+
		"\u0000\u0000\u0011:\u0001\u0000\u0000\u0000\u0013>\u0001\u0000\u0000\u0000"+
		"\u0015B\u0001\u0000\u0000\u0000\u0017F\u0001\u0000\u0000\u0000\u0019K"+
		"\u0001\u0000\u0000\u0000\u001bM\u0001\u0000\u0000\u0000\u001dO\u0001\u0000"+
		"\u0000\u0000\u001fR\u0001\u0000\u0000\u0000!V\u0001\u0000\u0000\u0000"+
		"#`\u0001\u0000\u0000\u0000%b\u0001\u0000\u0000\u0000\'e\u0001\u0000\u0000"+
		"\u0000)*\u0005;\u0000\u0000*\u0002\u0001\u0000\u0000\u0000+,\u0005:\u0000"+
		"\u0000,\u0004\u0001\u0000\u0000\u0000-.\u0005(\u0000\u0000.\u0006\u0001"+
		"\u0000\u0000\u0000/0\u0005)\u0000\u00000\b\u0001\u0000\u0000\u000012\u0005"+
		"-\u0000\u000023\u0005>\u0000\u00003\n\u0001\u0000\u0000\u000045\u0005"+
		"#\u0000\u00005\f\u0001\u0000\u0000\u000067\u0005*\u0000\u00007\u000e\u0001"+
		"\u0000\u0000\u000089\u0005.\u0000\u00009\u0010\u0001\u0000\u0000\u0000"+
		":;\u00051\u0000\u0000;<\u0005N\u0000\u0000<=\u0005F\u0000\u0000=\u0012"+
		"\u0001\u0000\u0000\u0000>?\u00052\u0000\u0000?@\u0005N\u0000\u0000@A\u0005"+
		"F\u0000\u0000A\u0014\u0001\u0000\u0000\u0000BC\u00053\u0000\u0000CD\u0005"+
		"N\u0000\u0000DE\u0005F\u0000\u0000E\u0016\u0001\u0000\u0000\u0000FG\u0005"+
		"B\u0000\u0000GH\u0005C\u0000\u0000HI\u0005N\u0000\u0000IJ\u0005F\u0000"+
		"\u0000J\u0018\u0001\u0000\u0000\u0000KL\u0005-\u0000\u0000L\u001a\u0001"+
		"\u0000\u0000\u0000MN\u0005>\u0000\u0000N\u001c\u0001\u0000\u0000\u0000"+
		"OP\u0005,\u0000\u0000P\u001e\u0001\u0000\u0000\u0000QS\u0003#\u0011\u0000"+
		"RQ\u0001\u0000\u0000\u0000ST\u0001\u0000\u0000\u0000TR\u0001\u0000\u0000"+
		"\u0000TU\u0001\u0000\u0000\u0000U \u0001\u0000\u0000\u0000V]\u0003%\u0012"+
		"\u0000W\\\u0003%\u0012\u0000X\\\u0003#\u0011\u0000YZ\u0005_\u0000\u0000"+
		"Z\\\u0006\u0010\u0000\u0000[W\u0001\u0000\u0000\u0000[X\u0001\u0000\u0000"+
		"\u0000[Y\u0001\u0000\u0000\u0000\\_\u0001\u0000\u0000\u0000][\u0001\u0000"+
		"\u0000\u0000]^\u0001\u0000\u0000\u0000^\"\u0001\u0000\u0000\u0000_]\u0001"+
		"\u0000\u0000\u0000`a\u000209\u0000a$\u0001\u0000\u0000\u0000bc\u0007\u0000"+
		"\u0000\u0000c&\u0001\u0000\u0000\u0000df\u0007\u0001\u0000\u0000ed\u0001"+
		"\u0000\u0000\u0000fg\u0001\u0000\u0000\u0000ge\u0001\u0000\u0000\u0000"+
		"gh\u0001\u0000\u0000\u0000hi\u0001\u0000\u0000\u0000ij\u0006\u0013\u0001"+
		"\u0000j(\u0001\u0000\u0000\u0000\u0005\u0000T[]g\u0002\u0001\u0010\u0000"+
		"\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}