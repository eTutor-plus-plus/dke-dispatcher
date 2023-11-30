// Generated from NF.g4 by ANTLR 4.13.1

package at.jku.dke.etutor.modules.nf.parser ;

import java.util.Set;
import java.util.HashSet;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class NFParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, AlphaNumericChain=16, 
		AlphaNumericChar=17, Digit=18, Letter=19, WhiteSpace=20;
	public static final int
		RULE_relationSet = 0, RULE_relation = 1, RULE_keySet = 2, RULE_key = 3, 
		RULE_normalFormSubmission = 4, RULE_normalFormViolationSet = 5, RULE_normalFormViolation = 6, 
		RULE_normalForm = 7, RULE_attributeClosure = 8, RULE_functionalDependencySet = 9, 
		RULE_functionalDependency = 10, RULE_attributeSet = 11, RULE_attribute = 12;
	private static String[] makeRuleNames() {
		return new String[] {
			"relationSet", "relation", "keySet", "key", "normalFormSubmission", "normalFormViolationSet", 
			"normalFormViolation", "normalForm", "attributeClosure", "functionalDependencySet", 
			"functionalDependency", "attributeSet", "attribute"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'('", "')'", "'#'", "'.'", "':'", "'1'", "'2'", "'3'", 
			"'BCNF'", "')+'", "'='", "'-'", "'>'", "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, "AlphaNumericChain", "AlphaNumericChar", "Digit", 
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
	public String getGrammarFileName() { return "NF.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public NFParser(TokenStream input) {
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
			if ( listener instanceof NFListener ) ((NFListener)listener).enterRelationSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).exitRelationSet(this);
		}
	}

	public final RelationSetContext relationSet() throws RecognitionException {
		RelationSetContext _localctx = new RelationSetContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_relationSet);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26);
			relation();
			setState(31);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(27);
				match(T__0);
				setState(28);
				relation();
				}
				}
				setState(33);
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
			if ( listener instanceof NFListener ) ((NFListener)listener).enterRelation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).exitRelation(this);
		}
	}

	public final RelationContext relation() throws RecognitionException {
		RelationContext _localctx = new RelationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_relation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(34);
			match(T__1);
			setState(35);
			attributeSet();
			setState(36);
			match(T__2);
			setState(37);
			match(T__1);
			setState(38);
			keySet();
			setState(39);
			match(T__2);
			setState(40);
			match(T__1);
			setState(41);
			functionalDependencySet();
			setState(42);
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
		public Set<Key> keys;
		public KeyContext key;
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
			if ( listener instanceof NFListener ) ((NFListener)listener).enterKeySet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).exitKeySet(this);
		}
	}

	public final KeySetContext keySet() throws RecognitionException {
		KeySetContext _localctx = new KeySetContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_keySet);

		        ((KeySetContext)_localctx).keys =  new HashSet<>();
		    
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(44);
			((KeySetContext)_localctx).key = key();
			_localctx.keys.add(((KeySetContext)_localctx).key.keyObject);
			setState(52);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(46);
				match(T__0);
				setState(47);
				((KeySetContext)_localctx).key = key();
				_localctx.keys.add(((KeySetContext)_localctx).key.keyObject);
				}
				}
				setState(54);
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
		public Key keyObject;
		public AttributeSetContext attributeSet;
		public AttributeSetContext attributeSet() {
			return getRuleContext(AttributeSetContext.class,0);
		}
		public KeyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_key; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).enterKey(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).exitKey(this);
		}
	}

	public final KeyContext key() throws RecognitionException {
		KeyContext _localctx = new KeyContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_key);

		        ((KeyContext)_localctx).keyObject =  new Key();
		    
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			match(T__3);
			setState(56);
			((KeyContext)_localctx).attributeSet = attributeSet();
			_localctx.keyObject.addAllAttributes(((KeyContext)_localctx).attributeSet.attributes);
			setState(58);
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
	public static class NormalFormSubmissionContext extends ParserRuleContext {
		public NormalFormContext normalForm() {
			return getRuleContext(NormalFormContext.class,0);
		}
		public NormalFormViolationSetContext normalFormViolationSet() {
			return getRuleContext(NormalFormViolationSetContext.class,0);
		}
		public NormalFormSubmissionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_normalFormSubmission; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).enterNormalFormSubmission(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).exitNormalFormSubmission(this);
		}
	}

	public final NormalFormSubmissionContext normalFormSubmission() throws RecognitionException {
		NormalFormSubmissionContext _localctx = new NormalFormSubmissionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_normalFormSubmission);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(60);
			normalForm();
			setState(61);
			match(T__4);
			setState(63);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AlphaNumericChain) {
				{
				setState(62);
				normalFormViolationSet();
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
	public static class NormalFormViolationSetContext extends ParserRuleContext {
		public List<NormalFormViolationContext> normalFormViolation() {
			return getRuleContexts(NormalFormViolationContext.class);
		}
		public NormalFormViolationContext normalFormViolation(int i) {
			return getRuleContext(NormalFormViolationContext.class,i);
		}
		public NormalFormViolationSetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_normalFormViolationSet; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).enterNormalFormViolationSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).exitNormalFormViolationSet(this);
		}
	}

	public final NormalFormViolationSetContext normalFormViolationSet() throws RecognitionException {
		NormalFormViolationSetContext _localctx = new NormalFormViolationSetContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_normalFormViolationSet);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			normalFormViolation();
			setState(70);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(66);
				match(T__0);
				setState(67);
				normalFormViolation();
				}
				}
				setState(72);
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
	public static class NormalFormViolationContext extends ParserRuleContext {
		public FunctionalDependencyContext functionalDependency() {
			return getRuleContext(FunctionalDependencyContext.class,0);
		}
		public NormalFormContext normalForm() {
			return getRuleContext(NormalFormContext.class,0);
		}
		public NormalFormViolationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_normalFormViolation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).enterNormalFormViolation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).exitNormalFormViolation(this);
		}
	}

	public final NormalFormViolationContext normalFormViolation() throws RecognitionException {
		NormalFormViolationContext _localctx = new NormalFormViolationContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_normalFormViolation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73);
			functionalDependency();
			setState(74);
			match(T__5);
			setState(75);
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
		public NormalformLevel level;
		public NormalFormContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_normalForm; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).enterNormalForm(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).exitNormalForm(this);
		}
	}

	public final NormalFormContext normalForm() throws RecognitionException {
		NormalFormContext _localctx = new NormalFormContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_normalForm);
		try {
			setState(85);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__6:
				enterOuterAlt(_localctx, 1);
				{
				setState(77);
				match(T__6);
				((NormalFormContext)_localctx).level =  NormalformLevel.FIRST;
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 2);
				{
				setState(79);
				match(T__7);
				((NormalFormContext)_localctx).level =  NormalformLevel.SECOND;
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 3);
				{
				setState(81);
				match(T__8);
				((NormalFormContext)_localctx).level =  NormalformLevel.THIRD;
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 4);
				{
				setState(83);
				match(T__9);
				((NormalFormContext)_localctx).level =  NormalformLevel.BOYCE_CODD;
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
			if ( listener instanceof NFListener ) ((NFListener)listener).enterAttributeClosure(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).exitAttributeClosure(this);
		}
	}

	public final AttributeClosureContext attributeClosure() throws RecognitionException {
		AttributeClosureContext _localctx = new AttributeClosureContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_attributeClosure);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
			match(T__1);
			setState(88);
			attributeSet();
			setState(89);
			match(T__10);
			setState(90);
			match(T__11);
			setState(91);
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
		public Set<FunctionalDependency> functionalDependencies;
		public FunctionalDependencyContext functionalDependency;
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
			if ( listener instanceof NFListener ) ((NFListener)listener).enterFunctionalDependencySet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).exitFunctionalDependencySet(this);
		}
	}

	public final FunctionalDependencySetContext functionalDependencySet() throws RecognitionException {
		FunctionalDependencySetContext _localctx = new FunctionalDependencySetContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_functionalDependencySet);

		        ((FunctionalDependencySetContext)_localctx).functionalDependencies =  new HashSet<>();
		    
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93);
			((FunctionalDependencySetContext)_localctx).functionalDependency = functionalDependency();
			_localctx.functionalDependencies.add(((FunctionalDependencySetContext)_localctx).functionalDependency.fdObject);
			{
			setState(95);
			match(T__0);
			setState(96);
			((FunctionalDependencySetContext)_localctx).functionalDependency = functionalDependency();
			_localctx.functionalDependencies.add(((FunctionalDependencySetContext)_localctx).functionalDependency.fdObject);
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
		public FunctionalDependency fdObject;
		public AttributeSetContext attributeSet;
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
			if ( listener instanceof NFListener ) ((NFListener)listener).enterFunctionalDependency(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).exitFunctionalDependency(this);
		}
	}

	public final FunctionalDependencyContext functionalDependency() throws RecognitionException {
		FunctionalDependencyContext _localctx = new FunctionalDependencyContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_functionalDependency);

		        ((FunctionalDependencyContext)_localctx).fdObject =  new FunctionalDependency();
		    
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(99);
			((FunctionalDependencyContext)_localctx).attributeSet = attributeSet();
			_localctx.fdObject.setLHSAttributes(((FunctionalDependencyContext)_localctx).attributeSet.attributes);
			setState(101);
			match(T__12);
			setState(103);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(102);
				match(T__4);
				}
			}

			setState(105);
			match(T__13);
			setState(106);
			((FunctionalDependencyContext)_localctx).attributeSet = attributeSet();
			_localctx.fdObject.setRHSAttributes(((FunctionalDependencyContext)_localctx).attributeSet.attributes);
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
		public Set<String> attributes;
		public AttributeContext attribute;
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
			if ( listener instanceof NFListener ) ((NFListener)listener).enterAttributeSet(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).exitAttributeSet(this);
		}
	}

	public final AttributeSetContext attributeSet() throws RecognitionException {
		AttributeSetContext _localctx = new AttributeSetContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_attributeSet);

		        ((AttributeSetContext)_localctx).attributes =  new HashSet<>();
		    
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(109);
			((AttributeSetContext)_localctx).attribute = attribute();
			_localctx.attributes.add((((AttributeSetContext)_localctx).attribute!=null?_input.getText(((AttributeSetContext)_localctx).attribute.start,((AttributeSetContext)_localctx).attribute.stop):null));
			setState(117);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14) {
				{
				{
				setState(111);
				match(T__14);
				setState(112);
				((AttributeSetContext)_localctx).attribute = attribute();
				_localctx.attributes.add((((AttributeSetContext)_localctx).attribute!=null?_input.getText(((AttributeSetContext)_localctx).attribute.start,((AttributeSetContext)_localctx).attribute.stop):null));
				}
				}
				setState(119);
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
		public TerminalNode AlphaNumericChain() { return getToken(NFParser.AlphaNumericChain, 0); }
		public AttributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).enterAttribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).exitAttribute(this);
		}
	}

	public final AttributeContext attribute() throws RecognitionException {
		AttributeContext _localctx = new AttributeContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_attribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
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
		"\u0004\u0001\u0014{\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0001\u0000\u0001\u0000\u0001\u0000\u0005\u0000\u001e\b\u0000"+
		"\n\u0000\f\u0000!\t\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0005\u00023\b\u0002\n\u0002\f\u00026\t\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0003\u0004@\b\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0005\u0005"+
		"E\b\u0005\n\u0005\f\u0005H\t\u0005\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007V\b\u0007\u0001\b\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0003\nh\b\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0005\u000bt\b\u000b\n\u000b\f\u000bw\t\u000b\u0001"+
		"\f\u0001\f\u0001\f\u0000\u0000\r\u0000\u0002\u0004\u0006\b\n\f\u000e\u0010"+
		"\u0012\u0014\u0016\u0018\u0000\u0000v\u0000\u001a\u0001\u0000\u0000\u0000"+
		"\u0002\"\u0001\u0000\u0000\u0000\u0004,\u0001\u0000\u0000\u0000\u0006"+
		"7\u0001\u0000\u0000\u0000\b<\u0001\u0000\u0000\u0000\nA\u0001\u0000\u0000"+
		"\u0000\fI\u0001\u0000\u0000\u0000\u000eU\u0001\u0000\u0000\u0000\u0010"+
		"W\u0001\u0000\u0000\u0000\u0012]\u0001\u0000\u0000\u0000\u0014c\u0001"+
		"\u0000\u0000\u0000\u0016m\u0001\u0000\u0000\u0000\u0018x\u0001\u0000\u0000"+
		"\u0000\u001a\u001f\u0003\u0002\u0001\u0000\u001b\u001c\u0005\u0001\u0000"+
		"\u0000\u001c\u001e\u0003\u0002\u0001\u0000\u001d\u001b\u0001\u0000\u0000"+
		"\u0000\u001e!\u0001\u0000\u0000\u0000\u001f\u001d\u0001\u0000\u0000\u0000"+
		"\u001f \u0001\u0000\u0000\u0000 \u0001\u0001\u0000\u0000\u0000!\u001f"+
		"\u0001\u0000\u0000\u0000\"#\u0005\u0002\u0000\u0000#$\u0003\u0016\u000b"+
		"\u0000$%\u0005\u0003\u0000\u0000%&\u0005\u0002\u0000\u0000&\'\u0003\u0004"+
		"\u0002\u0000\'(\u0005\u0003\u0000\u0000()\u0005\u0002\u0000\u0000)*\u0003"+
		"\u0012\t\u0000*+\u0005\u0003\u0000\u0000+\u0003\u0001\u0000\u0000\u0000"+
		",-\u0003\u0006\u0003\u0000-4\u0006\u0002\uffff\uffff\u0000./\u0005\u0001"+
		"\u0000\u0000/0\u0003\u0006\u0003\u000001\u0006\u0002\uffff\uffff\u0000"+
		"13\u0001\u0000\u0000\u00002.\u0001\u0000\u0000\u000036\u0001\u0000\u0000"+
		"\u000042\u0001\u0000\u0000\u000045\u0001\u0000\u0000\u00005\u0005\u0001"+
		"\u0000\u0000\u000064\u0001\u0000\u0000\u000078\u0005\u0004\u0000\u0000"+
		"89\u0003\u0016\u000b\u00009:\u0006\u0003\uffff\uffff\u0000:;\u0005\u0004"+
		"\u0000\u0000;\u0007\u0001\u0000\u0000\u0000<=\u0003\u000e\u0007\u0000"+
		"=?\u0005\u0005\u0000\u0000>@\u0003\n\u0005\u0000?>\u0001\u0000\u0000\u0000"+
		"?@\u0001\u0000\u0000\u0000@\t\u0001\u0000\u0000\u0000AF\u0003\f\u0006"+
		"\u0000BC\u0005\u0001\u0000\u0000CE\u0003\f\u0006\u0000DB\u0001\u0000\u0000"+
		"\u0000EH\u0001\u0000\u0000\u0000FD\u0001\u0000\u0000\u0000FG\u0001\u0000"+
		"\u0000\u0000G\u000b\u0001\u0000\u0000\u0000HF\u0001\u0000\u0000\u0000"+
		"IJ\u0003\u0014\n\u0000JK\u0005\u0006\u0000\u0000KL\u0003\u000e\u0007\u0000"+
		"L\r\u0001\u0000\u0000\u0000MN\u0005\u0007\u0000\u0000NV\u0006\u0007\uffff"+
		"\uffff\u0000OP\u0005\b\u0000\u0000PV\u0006\u0007\uffff\uffff\u0000QR\u0005"+
		"\t\u0000\u0000RV\u0006\u0007\uffff\uffff\u0000ST\u0005\n\u0000\u0000T"+
		"V\u0006\u0007\uffff\uffff\u0000UM\u0001\u0000\u0000\u0000UO\u0001\u0000"+
		"\u0000\u0000UQ\u0001\u0000\u0000\u0000US\u0001\u0000\u0000\u0000V\u000f"+
		"\u0001\u0000\u0000\u0000WX\u0005\u0002\u0000\u0000XY\u0003\u0016\u000b"+
		"\u0000YZ\u0005\u000b\u0000\u0000Z[\u0005\f\u0000\u0000[\\\u0003\u0016"+
		"\u000b\u0000\\\u0011\u0001\u0000\u0000\u0000]^\u0003\u0014\n\u0000^_\u0006"+
		"\t\uffff\uffff\u0000_`\u0005\u0001\u0000\u0000`a\u0003\u0014\n\u0000a"+
		"b\u0006\t\uffff\uffff\u0000b\u0013\u0001\u0000\u0000\u0000cd\u0003\u0016"+
		"\u000b\u0000de\u0006\n\uffff\uffff\u0000eg\u0005\r\u0000\u0000fh\u0005"+
		"\u0005\u0000\u0000gf\u0001\u0000\u0000\u0000gh\u0001\u0000\u0000\u0000"+
		"hi\u0001\u0000\u0000\u0000ij\u0005\u000e\u0000\u0000jk\u0003\u0016\u000b"+
		"\u0000kl\u0006\n\uffff\uffff\u0000l\u0015\u0001\u0000\u0000\u0000mn\u0003"+
		"\u0018\f\u0000nu\u0006\u000b\uffff\uffff\u0000op\u0005\u000f\u0000\u0000"+
		"pq\u0003\u0018\f\u0000qr\u0006\u000b\uffff\uffff\u0000rt\u0001\u0000\u0000"+
		"\u0000so\u0001\u0000\u0000\u0000tw\u0001\u0000\u0000\u0000us\u0001\u0000"+
		"\u0000\u0000uv\u0001\u0000\u0000\u0000v\u0017\u0001\u0000\u0000\u0000"+
		"wu\u0001\u0000\u0000\u0000xy\u0005\u0010\u0000\u0000y\u0019\u0001\u0000"+
		"\u0000\u0000\u0007\u001f4?FUgu";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}