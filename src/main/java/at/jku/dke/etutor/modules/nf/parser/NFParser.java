// Generated from NF.g4 by ANTLR 4.13.1

package at.jku.dke.etutor.modules.nf.parser ;

import java.util.Set;
import java.util.HashSet;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;

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
		public Set<IdentifiedRelation> relations;
		public RelationContext relation;
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

		        ((RelationSetContext)_localctx).relations =  new HashSet<>();
		    
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(26);
			((RelationSetContext)_localctx).relation = relation();
			_localctx.relations.add(((RelationSetContext)_localctx).relation.parsedRelation);
			setState(34);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(28);
				match(T__0);
				setState(29);
				((RelationSetContext)_localctx).relation = relation();
				_localctx.relations.add(((RelationSetContext)_localctx).relation.parsedRelation);
				}
				}
				setState(36);
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
		public IdentifiedRelation parsedRelation;
		public AttributeSetContext attributeSet;
		public KeySetContext keySet;
		public FunctionalDependencySetContext functionalDependencySet;
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

		        ((RelationContext)_localctx).parsedRelation =  new IdentifiedRelation();
		    
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(37);
			match(T__1);
			setState(38);
			((RelationContext)_localctx).attributeSet = attributeSet();
			_localctx.parsedRelation.setAttributes(((RelationContext)_localctx).attributeSet.attributes);
			setState(40);
			match(T__2);
			setState(41);
			match(T__1);
			setState(42);
			((RelationContext)_localctx).keySet = keySet();
			_localctx.parsedRelation.setMinimalKeys(((RelationContext)_localctx).keySet.keys);
			setState(44);
			match(T__2);
			setState(45);
			match(T__1);
			setState(46);
			((RelationContext)_localctx).functionalDependencySet = functionalDependencySet();
			_localctx.parsedRelation.setFunctionalDependencies(((RelationContext)_localctx).functionalDependencySet.functionalDependencies);
			setState(48);
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
			setState(50);
			((KeySetContext)_localctx).key = key();
			_localctx.keys.add(((KeySetContext)_localctx).key.keyObject);
			setState(58);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(52);
				match(T__0);
				setState(53);
				((KeySetContext)_localctx).key = key();
				_localctx.keys.add(((KeySetContext)_localctx).key.keyObject);
				}
				}
				setState(60);
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
			setState(61);
			match(T__3);
			setState(62);
			((KeyContext)_localctx).attributeSet = attributeSet();
			_localctx.keyObject.addAllAttributes(((KeyContext)_localctx).attributeSet.attributes);
			setState(64);
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
			setState(66);
			normalForm();
			setState(67);
			match(T__4);
			setState(69);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AlphaNumericChain) {
				{
				setState(68);
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
			setState(71);
			normalFormViolation();
			setState(76);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(72);
				match(T__0);
				setState(73);
				normalFormViolation();
				}
				}
				setState(78);
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
			setState(79);
			functionalDependency();
			setState(80);
			match(T__5);
			setState(81);
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
			setState(91);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__6:
				enterOuterAlt(_localctx, 1);
				{
				setState(83);
				match(T__6);
				((NormalFormContext)_localctx).level =  NormalformLevel.FIRST;
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 2);
				{
				setState(85);
				match(T__7);
				((NormalFormContext)_localctx).level =  NormalformLevel.SECOND;
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 3);
				{
				setState(87);
				match(T__8);
				((NormalFormContext)_localctx).level =  NormalformLevel.THIRD;
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 4);
				{
				setState(89);
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
			setState(93);
			match(T__1);
			setState(94);
			attributeSet();
			setState(95);
			match(T__10);
			setState(96);
			match(T__11);
			setState(97);
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
			setState(99);
			((FunctionalDependencySetContext)_localctx).functionalDependency = functionalDependency();
			_localctx.functionalDependencies.add(((FunctionalDependencySetContext)_localctx).functionalDependency.fdObject);
			{
			setState(101);
			match(T__0);
			setState(102);
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
			setState(105);
			((FunctionalDependencyContext)_localctx).attributeSet = attributeSet();
			_localctx.fdObject.setLHSAttributes(((FunctionalDependencyContext)_localctx).attributeSet.attributes);
			setState(107);
			match(T__12);
			setState(109);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__4) {
				{
				setState(108);
				match(T__4);
				}
			}

			setState(111);
			match(T__13);
			setState(112);
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
			setState(115);
			((AttributeSetContext)_localctx).attribute = attribute();
			_localctx.attributes.add((((AttributeSetContext)_localctx).attribute!=null?_input.getText(((AttributeSetContext)_localctx).attribute.start,((AttributeSetContext)_localctx).attribute.stop):null));
			setState(123);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__14) {
				{
				{
				setState(117);
				match(T__14);
				setState(118);
				((AttributeSetContext)_localctx).attribute = attribute();
				_localctx.attributes.add((((AttributeSetContext)_localctx).attribute!=null?_input.getText(((AttributeSetContext)_localctx).attribute.start,((AttributeSetContext)_localctx).attribute.stop):null));
				}
				}
				setState(125);
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
			setState(126);
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
		"\u0004\u0001\u0014\u0081\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0005\u0000!\b\u0000\n\u0000\f\u0000$\t\u0000\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0001"+
		"\u0002\u0005\u00029\b\u0002\n\u0002\f\u0002<\t\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0003\u0004F\b\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0005"+
		"\u0005K\b\u0005\n\u0005\f\u0005N\t\u0005\u0001\u0006\u0001\u0006\u0001"+
		"\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0003\u0007\\\b\u0007\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0003\nn\b\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\u000b\u0005\u000bz\b\u000b\n\u000b\f\u000b}\t\u000b"+
		"\u0001\f\u0001\f\u0001\f\u0000\u0000\r\u0000\u0002\u0004\u0006\b\n\f\u000e"+
		"\u0010\u0012\u0014\u0016\u0018\u0000\u0000|\u0000\u001a\u0001\u0000\u0000"+
		"\u0000\u0002%\u0001\u0000\u0000\u0000\u00042\u0001\u0000\u0000\u0000\u0006"+
		"=\u0001\u0000\u0000\u0000\bB\u0001\u0000\u0000\u0000\nG\u0001\u0000\u0000"+
		"\u0000\fO\u0001\u0000\u0000\u0000\u000e[\u0001\u0000\u0000\u0000\u0010"+
		"]\u0001\u0000\u0000\u0000\u0012c\u0001\u0000\u0000\u0000\u0014i\u0001"+
		"\u0000\u0000\u0000\u0016s\u0001\u0000\u0000\u0000\u0018~\u0001\u0000\u0000"+
		"\u0000\u001a\u001b\u0003\u0002\u0001\u0000\u001b\"\u0006\u0000\uffff\uffff"+
		"\u0000\u001c\u001d\u0005\u0001\u0000\u0000\u001d\u001e\u0003\u0002\u0001"+
		"\u0000\u001e\u001f\u0006\u0000\uffff\uffff\u0000\u001f!\u0001\u0000\u0000"+
		"\u0000 \u001c\u0001\u0000\u0000\u0000!$\u0001\u0000\u0000\u0000\" \u0001"+
		"\u0000\u0000\u0000\"#\u0001\u0000\u0000\u0000#\u0001\u0001\u0000\u0000"+
		"\u0000$\"\u0001\u0000\u0000\u0000%&\u0005\u0002\u0000\u0000&\'\u0003\u0016"+
		"\u000b\u0000\'(\u0006\u0001\uffff\uffff\u0000()\u0005\u0003\u0000\u0000"+
		")*\u0005\u0002\u0000\u0000*+\u0003\u0004\u0002\u0000+,\u0006\u0001\uffff"+
		"\uffff\u0000,-\u0005\u0003\u0000\u0000-.\u0005\u0002\u0000\u0000./\u0003"+
		"\u0012\t\u0000/0\u0006\u0001\uffff\uffff\u000001\u0005\u0003\u0000\u0000"+
		"1\u0003\u0001\u0000\u0000\u000023\u0003\u0006\u0003\u00003:\u0006\u0002"+
		"\uffff\uffff\u000045\u0005\u0001\u0000\u000056\u0003\u0006\u0003\u0000"+
		"67\u0006\u0002\uffff\uffff\u000079\u0001\u0000\u0000\u000084\u0001\u0000"+
		"\u0000\u00009<\u0001\u0000\u0000\u0000:8\u0001\u0000\u0000\u0000:;\u0001"+
		"\u0000\u0000\u0000;\u0005\u0001\u0000\u0000\u0000<:\u0001\u0000\u0000"+
		"\u0000=>\u0005\u0004\u0000\u0000>?\u0003\u0016\u000b\u0000?@\u0006\u0003"+
		"\uffff\uffff\u0000@A\u0005\u0004\u0000\u0000A\u0007\u0001\u0000\u0000"+
		"\u0000BC\u0003\u000e\u0007\u0000CE\u0005\u0005\u0000\u0000DF\u0003\n\u0005"+
		"\u0000ED\u0001\u0000\u0000\u0000EF\u0001\u0000\u0000\u0000F\t\u0001\u0000"+
		"\u0000\u0000GL\u0003\f\u0006\u0000HI\u0005\u0001\u0000\u0000IK\u0003\f"+
		"\u0006\u0000JH\u0001\u0000\u0000\u0000KN\u0001\u0000\u0000\u0000LJ\u0001"+
		"\u0000\u0000\u0000LM\u0001\u0000\u0000\u0000M\u000b\u0001\u0000\u0000"+
		"\u0000NL\u0001\u0000\u0000\u0000OP\u0003\u0014\n\u0000PQ\u0005\u0006\u0000"+
		"\u0000QR\u0003\u000e\u0007\u0000R\r\u0001\u0000\u0000\u0000ST\u0005\u0007"+
		"\u0000\u0000T\\\u0006\u0007\uffff\uffff\u0000UV\u0005\b\u0000\u0000V\\"+
		"\u0006\u0007\uffff\uffff\u0000WX\u0005\t\u0000\u0000X\\\u0006\u0007\uffff"+
		"\uffff\u0000YZ\u0005\n\u0000\u0000Z\\\u0006\u0007\uffff\uffff\u0000[S"+
		"\u0001\u0000\u0000\u0000[U\u0001\u0000\u0000\u0000[W\u0001\u0000\u0000"+
		"\u0000[Y\u0001\u0000\u0000\u0000\\\u000f\u0001\u0000\u0000\u0000]^\u0005"+
		"\u0002\u0000\u0000^_\u0003\u0016\u000b\u0000_`\u0005\u000b\u0000\u0000"+
		"`a\u0005\f\u0000\u0000ab\u0003\u0016\u000b\u0000b\u0011\u0001\u0000\u0000"+
		"\u0000cd\u0003\u0014\n\u0000de\u0006\t\uffff\uffff\u0000ef\u0005\u0001"+
		"\u0000\u0000fg\u0003\u0014\n\u0000gh\u0006\t\uffff\uffff\u0000h\u0013"+
		"\u0001\u0000\u0000\u0000ij\u0003\u0016\u000b\u0000jk\u0006\n\uffff\uffff"+
		"\u0000km\u0005\r\u0000\u0000ln\u0005\u0005\u0000\u0000ml\u0001\u0000\u0000"+
		"\u0000mn\u0001\u0000\u0000\u0000no\u0001\u0000\u0000\u0000op\u0005\u000e"+
		"\u0000\u0000pq\u0003\u0016\u000b\u0000qr\u0006\n\uffff\uffff\u0000r\u0015"+
		"\u0001\u0000\u0000\u0000st\u0003\u0018\f\u0000t{\u0006\u000b\uffff\uffff"+
		"\u0000uv\u0005\u000f\u0000\u0000vw\u0003\u0018\f\u0000wx\u0006\u000b\uffff"+
		"\uffff\u0000xz\u0001\u0000\u0000\u0000yu\u0001\u0000\u0000\u0000z}\u0001"+
		"\u0000\u0000\u0000{y\u0001\u0000\u0000\u0000{|\u0001\u0000\u0000\u0000"+
		"|\u0017\u0001\u0000\u0000\u0000}{\u0001\u0000\u0000\u0000~\u007f\u0005"+
		"\u0010\u0000\u0000\u007f\u0019\u0001\u0000\u0000\u0000\u0007\":EL[m{";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}