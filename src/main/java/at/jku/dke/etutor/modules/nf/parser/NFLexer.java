// Generated from NF.g4 by ANTLR 4.13.1

package at.jku.dke.etutor.modules.nf.parser ;

import java.util.Set;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;
import at.jku.dke.etutor.modules.nf.NormalformDeterminationSubmission;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class NFLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, Integer=14, AlphaNumericChain=15, 
		Digit=16, Letter=17, WhiteSpace=18;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "Integer", "AlphaNumericChain", "Digit", 
			"Letter", "WhiteSpace"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "':'", "'('", "')'", "'->'", "'#'", "'*'", "'.'", "'1NF'", 
			"'2NF'", "'3NF'", "'BCNF'", "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, "Integer", "AlphaNumericChain", "Digit", "Letter", "WhiteSpace"
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
		case 14:
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
		"\u0004\u0000\u0012c\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002\u0001"+
		"\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004"+
		"\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007"+
		"\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b"+
		"\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002"+
		"\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0001"+
		"\u0000\u0001\u0000\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001"+
		"\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005\u0001"+
		"\u0005\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b"+
		"\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\f\u0001\f\u0001\r\u0004\rK\b\r\u000b\r\f\rL\u0001\u000e\u0001\u000e"+
		"\u0001\u000e\u0001\u000e\u0001\u000e\u0005\u000eT\b\u000e\n\u000e\f\u000e"+
		"W\t\u000e\u0001\u000f\u0001\u000f\u0001\u0010\u0001\u0010\u0001\u0011"+
		"\u0004\u0011^\b\u0011\u000b\u0011\f\u0011_\u0001\u0011\u0001\u0011\u0000"+
		"\u0000\u0012\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b"+
		"\u0006\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b"+
		"\u000e\u001d\u000f\u001f\u0010!\u0011#\u0012\u0001\u0000\u0002\u0002\u0000"+
		"AZaz\u0003\u0000\t\n\r\r  g\u0000\u0001\u0001\u0000\u0000\u0000\u0000"+
		"\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000"+
		"\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b"+
		"\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f\u0001"+
		"\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013\u0001"+
		"\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001"+
		"\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001"+
		"\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001"+
		"\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000\u0000"+
		"\u0000\u0001%\u0001\u0000\u0000\u0000\u0003\'\u0001\u0000\u0000\u0000"+
		"\u0005)\u0001\u0000\u0000\u0000\u0007+\u0001\u0000\u0000\u0000\t-\u0001"+
		"\u0000\u0000\u0000\u000b0\u0001\u0000\u0000\u0000\r2\u0001\u0000\u0000"+
		"\u0000\u000f4\u0001\u0000\u0000\u0000\u00116\u0001\u0000\u0000\u0000\u0013"+
		":\u0001\u0000\u0000\u0000\u0015>\u0001\u0000\u0000\u0000\u0017B\u0001"+
		"\u0000\u0000\u0000\u0019G\u0001\u0000\u0000\u0000\u001bJ\u0001\u0000\u0000"+
		"\u0000\u001dN\u0001\u0000\u0000\u0000\u001fX\u0001\u0000\u0000\u0000!"+
		"Z\u0001\u0000\u0000\u0000#]\u0001\u0000\u0000\u0000%&\u0005;\u0000\u0000"+
		"&\u0002\u0001\u0000\u0000\u0000\'(\u0005:\u0000\u0000(\u0004\u0001\u0000"+
		"\u0000\u0000)*\u0005(\u0000\u0000*\u0006\u0001\u0000\u0000\u0000+,\u0005"+
		")\u0000\u0000,\b\u0001\u0000\u0000\u0000-.\u0005-\u0000\u0000./\u0005"+
		">\u0000\u0000/\n\u0001\u0000\u0000\u000001\u0005#\u0000\u00001\f\u0001"+
		"\u0000\u0000\u000023\u0005*\u0000\u00003\u000e\u0001\u0000\u0000\u0000"+
		"45\u0005.\u0000\u00005\u0010\u0001\u0000\u0000\u000067\u00051\u0000\u0000"+
		"78\u0005N\u0000\u000089\u0005F\u0000\u00009\u0012\u0001\u0000\u0000\u0000"+
		":;\u00052\u0000\u0000;<\u0005N\u0000\u0000<=\u0005F\u0000\u0000=\u0014"+
		"\u0001\u0000\u0000\u0000>?\u00053\u0000\u0000?@\u0005N\u0000\u0000@A\u0005"+
		"F\u0000\u0000A\u0016\u0001\u0000\u0000\u0000BC\u0005B\u0000\u0000CD\u0005"+
		"C\u0000\u0000DE\u0005N\u0000\u0000EF\u0005F\u0000\u0000F\u0018\u0001\u0000"+
		"\u0000\u0000GH\u0005,\u0000\u0000H\u001a\u0001\u0000\u0000\u0000IK\u0003"+
		"\u001f\u000f\u0000JI\u0001\u0000\u0000\u0000KL\u0001\u0000\u0000\u0000"+
		"LJ\u0001\u0000\u0000\u0000LM\u0001\u0000\u0000\u0000M\u001c\u0001\u0000"+
		"\u0000\u0000NU\u0003!\u0010\u0000OT\u0003!\u0010\u0000PT\u0003\u001f\u000f"+
		"\u0000QR\u0005_\u0000\u0000RT\u0006\u000e\u0000\u0000SO\u0001\u0000\u0000"+
		"\u0000SP\u0001\u0000\u0000\u0000SQ\u0001\u0000\u0000\u0000TW\u0001\u0000"+
		"\u0000\u0000US\u0001\u0000\u0000\u0000UV\u0001\u0000\u0000\u0000V\u001e"+
		"\u0001\u0000\u0000\u0000WU\u0001\u0000\u0000\u0000XY\u000209\u0000Y \u0001"+
		"\u0000\u0000\u0000Z[\u0007\u0000\u0000\u0000[\"\u0001\u0000\u0000\u0000"+
		"\\^\u0007\u0001\u0000\u0000]\\\u0001\u0000\u0000\u0000^_\u0001\u0000\u0000"+
		"\u0000_]\u0001\u0000\u0000\u0000_`\u0001\u0000\u0000\u0000`a\u0001\u0000"+
		"\u0000\u0000ab\u0006\u0011\u0001\u0000b$\u0001\u0000\u0000\u0000\u0005"+
		"\u0000LSU_\u0002\u0001\u000e\u0000\u0006\u0000\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}