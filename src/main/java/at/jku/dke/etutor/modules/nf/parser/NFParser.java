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
import org.antlr.v4.runtime.tree.*;
import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class NFParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, AlphaNumericChain=17, 
		AlphaNumericChar=18, Integer=19, Digit=20, Letter=21, WhiteSpace=22;
	public static final int
		RULE_relationSet = 0, RULE_relation = 1, RULE_relationId = 2, RULE_keySet = 3, 
		RULE_key = 4, RULE_normalFormSubmission = 5, RULE_normalFormViolationSet = 6, 
		RULE_normalFormViolation = 7, RULE_normalForm = 8, RULE_attributeClosure = 9, 
		RULE_functionalDependencySet = 10, RULE_functionalDependency = 11, RULE_attributeSet = 12, 
		RULE_attribute = 13;
	private static String[] makeRuleNames() {
		return new String[] {
			"relationSet", "relation", "relationId", "keySet", "key", "normalFormSubmission", 
			"normalFormViolationSet", "normalFormViolation", "normalForm", "attributeClosure", 
			"functionalDependencySet", "functionalDependency", "attributeSet", "attribute"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "':'", "'('", "')'", "'R'", "'.'", "'#'", "'1'", "'2'", 
			"'3'", "'BCNF'", "')+'", "'='", "'-'", "'>'", "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, "AlphaNumericChain", "AlphaNumericChar", 
			"Integer", "Digit", "Letter", "WhiteSpace"
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
			setState(28);
			((RelationSetContext)_localctx).relation = relation();
			_localctx.relations.add(((RelationSetContext)_localctx).relation.parsedRelation);
			setState(36);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(30);
				match(T__0);
				setState(31);
				((RelationSetContext)_localctx).relation = relation();
				_localctx.relations.add(((RelationSetContext)_localctx).relation.parsedRelation);
				}
				}
				setState(38);
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
		public RelationIdContext relationId;
		public AttributeSetContext attributeSet;
		public KeySetContext keySet;
		public FunctionalDependencySetContext functionalDependencySet;
		public RelationIdContext relationId() {
			return getRuleContext(RelationIdContext.class,0);
		}
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
			setState(39);
			((RelationContext)_localctx).relationId = relationId();
			try {_localctx.parsedRelation.setID((((RelationContext)_localctx).relationId!=null?_input.getText(((RelationContext)_localctx).relationId.start,((RelationContext)_localctx).relationId.stop):null));} catch(Exception e) { e.printStackTrace();}
			setState(41);
			match(T__1);
			setState(42);
			match(T__2);
			setState(43);
			((RelationContext)_localctx).attributeSet = attributeSet();
			_localctx.parsedRelation.setAttributes(((RelationContext)_localctx).attributeSet.attributes);
			setState(45);
			match(T__3);
			setState(46);
			match(T__2);
			setState(47);
			((RelationContext)_localctx).keySet = keySet();
			_localctx.parsedRelation.setMinimalKeys(((RelationContext)_localctx).keySet.keys);
			setState(49);
			match(T__3);
			setState(50);
			match(T__2);
			setState(51);
			((RelationContext)_localctx).functionalDependencySet = functionalDependencySet();
			_localctx.parsedRelation.setFunctionalDependencies(((RelationContext)_localctx).functionalDependencySet.functionalDependencies);
			setState(53);
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
	public static class RelationIdContext extends ParserRuleContext {
		public List<TerminalNode> Integer() { return getTokens(NFParser.Integer); }
		public TerminalNode Integer(int i) {
			return getToken(NFParser.Integer, i);
		}
		public RelationIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_relationId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).enterRelationId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof NFListener ) ((NFListener)listener).exitRelationId(this);
		}
	}

	public final RelationIdContext relationId() throws RecognitionException {
		RelationIdContext _localctx = new RelationIdContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_relationId);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			match(T__4);
			setState(56);
			match(Integer);
			setState(59);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__5) {
				{
				setState(57);
				match(T__5);
				setState(58);
				match(Integer);
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
		enterRule(_localctx, 6, RULE_keySet);

		        ((KeySetContext)_localctx).keys =  new HashSet<>();
		    
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61);
			((KeySetContext)_localctx).key = key();
			_localctx.keys.add(((KeySetContext)_localctx).key.keyObject);
			setState(69);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(63);
				match(T__0);
				setState(64);
				((KeySetContext)_localctx).key = key();
				_localctx.keys.add(((KeySetContext)_localctx).key.keyObject);
				}
				}
				setState(71);
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
		enterRule(_localctx, 8, RULE_key);

		        ((KeyContext)_localctx).keyObject =  new Key();
		    
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			match(T__6);
			setState(73);
			((KeyContext)_localctx).attributeSet = attributeSet();
			_localctx.keyObject.addAllAttributes(((KeyContext)_localctx).attributeSet.attributes);
			setState(75);
			match(T__6);
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
		enterRule(_localctx, 10, RULE_normalFormSubmission);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77);
			normalForm();
			setState(78);
			match(T__5);
			setState(80);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==AlphaNumericChain) {
				{
				setState(79);
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
		enterRule(_localctx, 12, RULE_normalFormViolationSet);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82);
			normalFormViolation();
			setState(87);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(83);
				match(T__0);
				setState(84);
				normalFormViolation();
				}
				}
				setState(89);
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
		enterRule(_localctx, 14, RULE_normalFormViolation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			functionalDependency();
			setState(91);
			match(T__1);
			setState(92);
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
		enterRule(_localctx, 16, RULE_normalForm);
		try {
			setState(102);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__7:
				enterOuterAlt(_localctx, 1);
				{
				setState(94);
				match(T__7);
				((NormalFormContext)_localctx).level =  NormalformLevel.FIRST;
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 2);
				{
				setState(96);
				match(T__8);
				((NormalFormContext)_localctx).level =  NormalformLevel.SECOND;
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 3);
				{
				setState(98);
				match(T__9);
				((NormalFormContext)_localctx).level =  NormalformLevel.THIRD;
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 4);
				{
				setState(100);
				match(T__10);
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
		enterRule(_localctx, 18, RULE_attributeClosure);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(104);
			match(T__2);
			setState(105);
			attributeSet();
			setState(106);
			match(T__11);
			setState(107);
			match(T__12);
			setState(108);
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
		enterRule(_localctx, 20, RULE_functionalDependencySet);

		        ((FunctionalDependencySetContext)_localctx).functionalDependencies =  new HashSet<>();
		    
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(110);
			((FunctionalDependencySetContext)_localctx).functionalDependency = functionalDependency();
			_localctx.functionalDependencies.add(((FunctionalDependencySetContext)_localctx).functionalDependency.fdObject);
			{
			setState(112);
			match(T__0);
			setState(113);
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
		enterRule(_localctx, 22, RULE_functionalDependency);

		        ((FunctionalDependencyContext)_localctx).fdObject =  new FunctionalDependency();
		    
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
			((FunctionalDependencyContext)_localctx).attributeSet = attributeSet();
			_localctx.fdObject.setLhsAttributes(((FunctionalDependencyContext)_localctx).attributeSet.attributes);
			setState(118);
			match(T__13);
			setState(120);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__5) {
				{
				setState(119);
				match(T__5);
				}
			}

			setState(122);
			match(T__14);
			setState(123);
			((FunctionalDependencyContext)_localctx).attributeSet = attributeSet();
			_localctx.fdObject.setRhsAttributes(((FunctionalDependencyContext)_localctx).attributeSet.attributes);
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
		enterRule(_localctx, 24, RULE_attributeSet);

		        ((AttributeSetContext)_localctx).attributes =  new HashSet<>();
		    
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			((AttributeSetContext)_localctx).attribute = attribute();
			_localctx.attributes.add((((AttributeSetContext)_localctx).attribute!=null?_input.getText(((AttributeSetContext)_localctx).attribute.start,((AttributeSetContext)_localctx).attribute.stop):null));
			setState(134);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__15) {
				{
				{
				setState(128);
				match(T__15);
				setState(129);
				((AttributeSetContext)_localctx).attribute = attribute();
				_localctx.attributes.add((((AttributeSetContext)_localctx).attribute!=null?_input.getText(((AttributeSetContext)_localctx).attribute.start,((AttributeSetContext)_localctx).attribute.stop):null));
				}
				}
				setState(136);
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
		enterRule(_localctx, 26, RULE_attribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(137);
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
		"\u0004\u0001\u0016\u008c\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0000\u0001\u0000\u0001\u0000\u0005\u0000#\b\u0000\n\u0000\f\u0000&\t"+
		"\u0000\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001"+
		"\u0002\u0001\u0002\u0001\u0002\u0003\u0002<\b\u0002\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0005\u0003D\b"+
		"\u0003\n\u0003\f\u0003G\t\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0003\u0005Q\b"+
		"\u0005\u0001\u0006\u0001\u0006\u0001\u0006\u0005\u0006V\b\u0006\n\u0006"+
		"\f\u0006Y\t\u0006\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0003\bg\b"+
		"\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0003\u000by\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0005\f\u0085\b\f\n\f"+
		"\f\f\u0088\t\f\u0001\r\u0001\r\u0001\r\u0000\u0000\u000e\u0000\u0002\u0004"+
		"\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u0000\u0000\u0087"+
		"\u0000\u001c\u0001\u0000\u0000\u0000\u0002\'\u0001\u0000\u0000\u0000\u0004"+
		"7\u0001\u0000\u0000\u0000\u0006=\u0001\u0000\u0000\u0000\bH\u0001\u0000"+
		"\u0000\u0000\nM\u0001\u0000\u0000\u0000\fR\u0001\u0000\u0000\u0000\u000e"+
		"Z\u0001\u0000\u0000\u0000\u0010f\u0001\u0000\u0000\u0000\u0012h\u0001"+
		"\u0000\u0000\u0000\u0014n\u0001\u0000\u0000\u0000\u0016t\u0001\u0000\u0000"+
		"\u0000\u0018~\u0001\u0000\u0000\u0000\u001a\u0089\u0001\u0000\u0000\u0000"+
		"\u001c\u001d\u0003\u0002\u0001\u0000\u001d$\u0006\u0000\uffff\uffff\u0000"+
		"\u001e\u001f\u0005\u0001\u0000\u0000\u001f \u0003\u0002\u0001\u0000 !"+
		"\u0006\u0000\uffff\uffff\u0000!#\u0001\u0000\u0000\u0000\"\u001e\u0001"+
		"\u0000\u0000\u0000#&\u0001\u0000\u0000\u0000$\"\u0001\u0000\u0000\u0000"+
		"$%\u0001\u0000\u0000\u0000%\u0001\u0001\u0000\u0000\u0000&$\u0001\u0000"+
		"\u0000\u0000\'(\u0003\u0004\u0002\u0000()\u0006\u0001\uffff\uffff\u0000"+
		")*\u0005\u0002\u0000\u0000*+\u0005\u0003\u0000\u0000+,\u0003\u0018\f\u0000"+
		",-\u0006\u0001\uffff\uffff\u0000-.\u0005\u0004\u0000\u0000./\u0005\u0003"+
		"\u0000\u0000/0\u0003\u0006\u0003\u000001\u0006\u0001\uffff\uffff\u0000"+
		"12\u0005\u0004\u0000\u000023\u0005\u0003\u0000\u000034\u0003\u0014\n\u0000"+
		"45\u0006\u0001\uffff\uffff\u000056\u0005\u0004\u0000\u00006\u0003\u0001"+
		"\u0000\u0000\u000078\u0005\u0005\u0000\u00008;\u0005\u0013\u0000\u0000"+
		"9:\u0005\u0006\u0000\u0000:<\u0005\u0013\u0000\u0000;9\u0001\u0000\u0000"+
		"\u0000;<\u0001\u0000\u0000\u0000<\u0005\u0001\u0000\u0000\u0000=>\u0003"+
		"\b\u0004\u0000>E\u0006\u0003\uffff\uffff\u0000?@\u0005\u0001\u0000\u0000"+
		"@A\u0003\b\u0004\u0000AB\u0006\u0003\uffff\uffff\u0000BD\u0001\u0000\u0000"+
		"\u0000C?\u0001\u0000\u0000\u0000DG\u0001\u0000\u0000\u0000EC\u0001\u0000"+
		"\u0000\u0000EF\u0001\u0000\u0000\u0000F\u0007\u0001\u0000\u0000\u0000"+
		"GE\u0001\u0000\u0000\u0000HI\u0005\u0007\u0000\u0000IJ\u0003\u0018\f\u0000"+
		"JK\u0006\u0004\uffff\uffff\u0000KL\u0005\u0007\u0000\u0000L\t\u0001\u0000"+
		"\u0000\u0000MN\u0003\u0010\b\u0000NP\u0005\u0006\u0000\u0000OQ\u0003\f"+
		"\u0006\u0000PO\u0001\u0000\u0000\u0000PQ\u0001\u0000\u0000\u0000Q\u000b"+
		"\u0001\u0000\u0000\u0000RW\u0003\u000e\u0007\u0000ST\u0005\u0001\u0000"+
		"\u0000TV\u0003\u000e\u0007\u0000US\u0001\u0000\u0000\u0000VY\u0001\u0000"+
		"\u0000\u0000WU\u0001\u0000\u0000\u0000WX\u0001\u0000\u0000\u0000X\r\u0001"+
		"\u0000\u0000\u0000YW\u0001\u0000\u0000\u0000Z[\u0003\u0016\u000b\u0000"+
		"[\\\u0005\u0002\u0000\u0000\\]\u0003\u0010\b\u0000]\u000f\u0001\u0000"+
		"\u0000\u0000^_\u0005\b\u0000\u0000_g\u0006\b\uffff\uffff\u0000`a\u0005"+
		"\t\u0000\u0000ag\u0006\b\uffff\uffff\u0000bc\u0005\n\u0000\u0000cg\u0006"+
		"\b\uffff\uffff\u0000de\u0005\u000b\u0000\u0000eg\u0006\b\uffff\uffff\u0000"+
		"f^\u0001\u0000\u0000\u0000f`\u0001\u0000\u0000\u0000fb\u0001\u0000\u0000"+
		"\u0000fd\u0001\u0000\u0000\u0000g\u0011\u0001\u0000\u0000\u0000hi\u0005"+
		"\u0003\u0000\u0000ij\u0003\u0018\f\u0000jk\u0005\f\u0000\u0000kl\u0005"+
		"\r\u0000\u0000lm\u0003\u0018\f\u0000m\u0013\u0001\u0000\u0000\u0000no"+
		"\u0003\u0016\u000b\u0000op\u0006\n\uffff\uffff\u0000pq\u0005\u0001\u0000"+
		"\u0000qr\u0003\u0016\u000b\u0000rs\u0006\n\uffff\uffff\u0000s\u0015\u0001"+
		"\u0000\u0000\u0000tu\u0003\u0018\f\u0000uv\u0006\u000b\uffff\uffff\u0000"+
		"vx\u0005\u000e\u0000\u0000wy\u0005\u0006\u0000\u0000xw\u0001\u0000\u0000"+
		"\u0000xy\u0001\u0000\u0000\u0000yz\u0001\u0000\u0000\u0000z{\u0005\u000f"+
		"\u0000\u0000{|\u0003\u0018\f\u0000|}\u0006\u000b\uffff\uffff\u0000}\u0017"+
		"\u0001\u0000\u0000\u0000~\u007f\u0003\u001a\r\u0000\u007f\u0086\u0006"+
		"\f\uffff\uffff\u0000\u0080\u0081\u0005\u0010\u0000\u0000\u0081\u0082\u0003"+
		"\u001a\r\u0000\u0082\u0083\u0006\f\uffff\uffff\u0000\u0083\u0085\u0001"+
		"\u0000\u0000\u0000\u0084\u0080\u0001\u0000\u0000\u0000\u0085\u0088\u0001"+
		"\u0000\u0000\u0000\u0086\u0084\u0001\u0000\u0000\u0000\u0086\u0087\u0001"+
		"\u0000\u0000\u0000\u0087\u0019\u0001\u0000\u0000\u0000\u0088\u0086\u0001"+
		"\u0000\u0000\u0000\u0089\u008a\u0005\u0011\u0000\u0000\u008a\u001b\u0001"+
		"\u0000\u0000\u0000\b$;EPWfx\u0086";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}