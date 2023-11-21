// Generated from nf.g4 by ANTLR 4.13.1

    package at.jku.dke.etutor.modules.nf.parser ;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class nfParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, AlphaNumericChain=16, 
		AlphaNumericChar=17, Integer=18, Letter=19, WhiteSpace=20;
	public static final int
		RULE_relationSet = 0, RULE_relation = 1, RULE_keySet = 2, RULE_key = 3, 
		RULE_violatedNormalForm = 4, RULE_normalForm = 5, RULE_attributeClosure = 6, 
		RULE_functionalDependencySet = 7, RULE_functionalDependency = 8, RULE_attributeSet = 9, 
		RULE_attribute = 10;
	private static String[] makeRuleNames() {
		return new String[] {
			"relationSet", "relation", "keySet", "key", "violatedNormalForm", "normalForm", 
			"attributeClosure", "functionalDependencySet", "functionalDependency", 
			"attributeSet", "attribute"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'('", "')'", "'#'", "':'", "'1'", "'2'", "'3'", "'BCNF'", 
			"')+'", "'='", "'-'", "'.'", "'>'", "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, "AlphaNumericChain", "AlphaNumericChar", "Integer", 
			"Letter", "WhiteSpace"
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
	public String getGrammarFileName() { return "nf.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public nfParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RelationSetContext extends ParserRuleContext {
		public List<RelationContext> relation() {
			return getRuleContexts(RelationContext.class);
		}
		public RelationContext relation(int i) {
			return getRuleContext(RelationContext.class,i);
		}
		public RelationSetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relationSet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).enterRelationSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).exitRelationSet(this);
		}
	}

	public final RelationSetContext relationSet() throws RecognitionException {
		RelationSetContext _localctx = new RelationSetContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_relationSet);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(22);
			relation();
			setState(27);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(23);
				match(T__0);
				setState(24);
				relation();
				}
				}
				setState(29);
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
	public static class RelationContext extends ParserRuleContext {
		public AttributeSetContext attributeSet() {
			return getRuleContext(AttributeSetContext.class,0);
		}
		public KeySetContext keySet() {
			return getRuleContext(KeySetContext.class,0);
		}
		public FunctionalDependencySetContext functionalDependencySet() {
			return getRuleContext(FunctionalDependencySetContext.class,0);
		}
		public RelationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).enterRelation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).exitRelation(this);
		}
	}

	public final RelationContext relation() throws RecognitionException {
		RelationContext _localctx = new RelationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_relation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(30);
			match(T__1);
			setState(31);
			attributeSet();
			setState(32);
			match(T__2);
			setState(33);
			match(T__1);
			setState(34);
			keySet();
			setState(35);
			match(T__2);
			setState(36);
			match(T__1);
			setState(37);
			functionalDependencySet();
			setState(38);
			match(T__2);
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
	public static class KeySetContext extends ParserRuleContext {
		public List<KeyContext> key() {
			return getRuleContexts(KeyContext.class);
		}
		public KeyContext key(int i) {
			return getRuleContext(KeyContext.class,i);
		}
		public KeySetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keySet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).enterKeySet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).exitKeySet(this);
		}
	}

	public final KeySetContext keySet() throws RecognitionException {
		KeySetContext _localctx = new KeySetContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_keySet);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(40);
			key();
			setState(45);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(41);
				match(T__0);
				setState(42);
				key();
				}
				}
				setState(47);
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
	public static class KeyContext extends ParserRuleContext {
		public AttributeSetContext attributeSet() {
			return getRuleContext(AttributeSetContext.class,0);
		}
		public KeyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_key; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).enterKey(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).exitKey(this);
		}
	}

	public final KeyContext key() throws RecognitionException {
		KeyContext _localctx = new KeyContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_key);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(48);
			match(T__3);
			setState(49);
			attributeSet();
			setState(50);
			match(T__3);
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
	public static class ViolatedNormalFormContext extends ParserRuleContext {
		public FunctionalDependencyContext functionalDependency() {
			return getRuleContext(FunctionalDependencyContext.class,0);
		}
		public NormalFormContext normalForm() {
			return getRuleContext(NormalFormContext.class,0);
		}
		public ViolatedNormalFormContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_violatedNormalForm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).enterViolatedNormalForm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).exitViolatedNormalForm(this);
		}
	}

	public final ViolatedNormalFormContext violatedNormalForm() throws RecognitionException {
		ViolatedNormalFormContext _localctx = new ViolatedNormalFormContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_violatedNormalForm);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52);
			functionalDependency();
			setState(53);
			match(T__4);
			setState(54);
			normalForm();
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
	public static class NormalFormContext extends ParserRuleContext {
		public NormalFormContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_normalForm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).enterNormalForm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).exitNormalForm(this);
		}
	}

	public final NormalFormContext normalForm() throws RecognitionException {
		NormalFormContext _localctx = new NormalFormContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_normalForm);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(56);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 960L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
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
	public static class AttributeClosureContext extends ParserRuleContext {
		public List<AttributeSetContext> attributeSet() {
			return getRuleContexts(AttributeSetContext.class);
		}
		public AttributeSetContext attributeSet(int i) {
			return getRuleContext(AttributeSetContext.class,i);
		}
		public AttributeClosureContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeClosure; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).enterAttributeClosure(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).exitAttributeClosure(this);
		}
	}

	public final AttributeClosureContext attributeClosure() throws RecognitionException {
		AttributeClosureContext _localctx = new AttributeClosureContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_attributeClosure);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(58);
			match(T__1);
			setState(59);
			attributeSet();
			setState(60);
			match(T__9);
			setState(61);
			match(T__10);
			setState(62);
			attributeSet();
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
	public static class FunctionalDependencySetContext extends ParserRuleContext {
		public List<FunctionalDependencyContext> functionalDependency() {
			return getRuleContexts(FunctionalDependencyContext.class);
		}
		public FunctionalDependencyContext functionalDependency(int i) {
			return getRuleContext(FunctionalDependencyContext.class,i);
		}
		public FunctionalDependencySetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionalDependencySet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).enterFunctionalDependencySet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).exitFunctionalDependencySet(this);
		}
	}

	public final FunctionalDependencySetContext functionalDependencySet() throws RecognitionException {
		FunctionalDependencySetContext _localctx = new FunctionalDependencySetContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_functionalDependencySet);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			functionalDependency();
			{
			setState(65);
			match(T__0);
			setState(66);
			functionalDependency();
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
	public static class FunctionalDependencyContext extends ParserRuleContext {
		public List<AttributeSetContext> attributeSet() {
			return getRuleContexts(AttributeSetContext.class);
		}
		public AttributeSetContext attributeSet(int i) {
			return getRuleContext(AttributeSetContext.class,i);
		}
		public FunctionalDependencyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionalDependency; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).enterFunctionalDependency(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).exitFunctionalDependency(this);
		}
	}

	public final FunctionalDependencyContext functionalDependency() throws RecognitionException {
		FunctionalDependencyContext _localctx = new FunctionalDependencyContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_functionalDependency);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			attributeSet();
			setState(69);
			match(T__11);
			setState(71);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(70);
				match(T__12);
				}
			}

			setState(73);
			match(T__13);
			setState(74);
			attributeSet();
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
	public static class AttributeSetContext extends ParserRuleContext {
		public List<AttributeContext> attribute() {
			return getRuleContexts(AttributeContext.class);
		}
		public AttributeContext attribute(int i) {
			return getRuleContext(AttributeContext.class,i);
		}
		public AttributeSetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attributeSet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).enterAttributeSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).exitAttributeSet(this);
		}
	}

	public final AttributeSetContext attributeSet() throws RecognitionException {
		AttributeSetContext _localctx = new AttributeSetContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_attributeSet);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(76);
			attribute();
			setState(81);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14) {
				{
				{
				setState(77);
				match(T__14);
				setState(78);
				attribute();
				}
				}
				setState(83);
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
	public static class AttributeContext extends ParserRuleContext {
		public TerminalNode AlphaNumericChain() { return getToken(nfParser.AlphaNumericChain, 0); }
		public AttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).enterAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof nfListener ) ((nfListener)listener).exitAttribute(this);
		}
	}

	public final AttributeContext attribute() throws RecognitionException {
		AttributeContext _localctx = new AttributeContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_attribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			match(AlphaNumericChain);
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
		"\u0004\u0001\u0014W\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0005\u0000\u001a\b\u0000\n\u0000\f\u0000\u001d\t\u0000\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0005\u0002,\b\u0002\n\u0002\f\u0002/\t\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\b\u0001\b\u0001\b\u0003\bH\b\b\u0001\b\u0001\b\u0001\b\u0001\t"+
		"\u0001\t\u0001\t\u0005\tP\b\t\n\t\f\tS\t\t\u0001\n\u0001\n\u0001\n\u0000"+
		"\u0000\u000b\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0000"+
		"\u0001\u0001\u0000\u0006\tO\u0000\u0016\u0001\u0000\u0000\u0000\u0002"+
		"\u001e\u0001\u0000\u0000\u0000\u0004(\u0001\u0000\u0000\u0000\u00060\u0001"+
		"\u0000\u0000\u0000\b4\u0001\u0000\u0000\u0000\n8\u0001\u0000\u0000\u0000"+
		"\f:\u0001\u0000\u0000\u0000\u000e@\u0001\u0000\u0000\u0000\u0010D\u0001"+
		"\u0000\u0000\u0000\u0012L\u0001\u0000\u0000\u0000\u0014T\u0001\u0000\u0000"+
		"\u0000\u0016\u001b\u0003\u0002\u0001\u0000\u0017\u0018\u0005\u0001\u0000"+
		"\u0000\u0018\u001a\u0003\u0002\u0001\u0000\u0019\u0017\u0001\u0000\u0000"+
		"\u0000\u001a\u001d\u0001\u0000\u0000\u0000\u001b\u0019\u0001\u0000\u0000"+
		"\u0000\u001b\u001c\u0001\u0000\u0000\u0000\u001c\u0001\u0001\u0000\u0000"+
		"\u0000\u001d\u001b\u0001\u0000\u0000\u0000\u001e\u001f\u0005\u0002\u0000"+
		"\u0000\u001f \u0003\u0012\t\u0000 !\u0005\u0003\u0000\u0000!\"\u0005\u0002"+
		"\u0000\u0000\"#\u0003\u0004\u0002\u0000#$\u0005\u0003\u0000\u0000$%\u0005"+
		"\u0002\u0000\u0000%&\u0003\u000e\u0007\u0000&\'\u0005\u0003\u0000\u0000"+
		"\'\u0003\u0001\u0000\u0000\u0000(-\u0003\u0006\u0003\u0000)*\u0005\u0001"+
		"\u0000\u0000*,\u0003\u0006\u0003\u0000+)\u0001\u0000\u0000\u0000,/\u0001"+
		"\u0000\u0000\u0000-+\u0001\u0000\u0000\u0000-.\u0001\u0000\u0000\u0000"+
		".\u0005\u0001\u0000\u0000\u0000/-\u0001\u0000\u0000\u000001\u0005\u0004"+
		"\u0000\u000012\u0003\u0012\t\u000023\u0005\u0004\u0000\u00003\u0007\u0001"+
		"\u0000\u0000\u000045\u0003\u0010\b\u000056\u0005\u0005\u0000\u000067\u0003"+
		"\n\u0005\u00007\t\u0001\u0000\u0000\u000089\u0007\u0000\u0000\u00009\u000b"+
		"\u0001\u0000\u0000\u0000:;\u0005\u0002\u0000\u0000;<\u0003\u0012\t\u0000"+
		"<=\u0005\n\u0000\u0000=>\u0005\u000b\u0000\u0000>?\u0003\u0012\t\u0000"+
		"?\r\u0001\u0000\u0000\u0000@A\u0003\u0010\b\u0000AB\u0005\u0001\u0000"+
		"\u0000BC\u0003\u0010\b\u0000C\u000f\u0001\u0000\u0000\u0000DE\u0003\u0012"+
		"\t\u0000EG\u0005\f\u0000\u0000FH\u0005\r\u0000\u0000GF\u0001\u0000\u0000"+
		"\u0000GH\u0001\u0000\u0000\u0000HI\u0001\u0000\u0000\u0000IJ\u0005\u000e"+
		"\u0000\u0000JK\u0003\u0012\t\u0000K\u0011\u0001\u0000\u0000\u0000LQ\u0003"+
		"\u0014\n\u0000MN\u0005\u000f\u0000\u0000NP\u0003\u0014\n\u0000OM\u0001"+
		"\u0000\u0000\u0000PS\u0001\u0000\u0000\u0000QO\u0001\u0000\u0000\u0000"+
		"QR\u0001\u0000\u0000\u0000R\u0013\u0001\u0000\u0000\u0000SQ\u0001\u0000"+
		"\u0000\u0000TU\u0005\u0010\u0000\u0000U\u0015\u0001\u0000\u0000\u0000"+
		"\u0004\u001b-GQ";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}