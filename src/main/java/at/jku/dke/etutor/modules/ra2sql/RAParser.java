package at.jku.dke.etutor.modules.ra2sql;// $ANTLR 2.7.2: "parser_grammar.g" -> "RAParser.java"$


		import java.util.GregorianCalendar;

import antlr.MismatchedCharException;
import antlr.MismatchedTokenException;
import antlr.NoViableAltException;
import antlr.NoViableAltForCharException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.SemanticException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.TokenStreamIOException;
import antlr.TokenStreamRecognitionException;
import antlr.TokenStreamRetryException;
		import at.jku.dke.etutor.modules.ra2sql.model.*;


public class RAParser extends antlr.LLkParser       implements RAParserTokenTypes
 {

		public Expression parse() throws UnexpectedAtomException, MismatchedAtomException, AtomStreamIOException, InvalidAtomException {
			Expression expression = null;
	
			try {
				expression = this.process();
			} catch (RecognitionException e) {
				e.printStackTrace();
			} catch (TokenStreamException e) {
				e.printStackTrace();
			} 
			
			return expression;
		}
	
protected RAParser(TokenBuffer tokenBuf, int k) {
  super(tokenBuf,k);
  tokenNames = _tokenNames;
}

public RAParser(TokenBuffer tokenBuf) {
  this(tokenBuf,2);
}

protected RAParser(TokenStream lexer, int k) {
  super(lexer,k);
  tokenNames = _tokenNames;
}

public RAParser(TokenStream lexer) {
  this(lexer,2);
}

public RAParser(ParserSharedInputState state) {
  super(state,2);
  tokenNames = _tokenNames;
}

	private final Expression  process() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException,InvalidAtomException {
		Expression expression;
		
		
				expression = null;
			
		
		try {      // for error handling
			expression=expression();
			match(Token.EOF_TYPE);
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("process", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("process", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("process", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("process", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("process", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("process", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("process", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("process", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("process", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return expression;
	}
	
	private final Expression  expression() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException,InvalidAtomException {
		Expression expression;
		
		
				expression = null;
			
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_LEFTPARENTHESES:
			case LITERAL_SELECTION:
			case LITERAL_PROJECTION:
			case LITERAL_RENAMING:
			{
				expression=operator();
				break;
			}
			case STRING:
			{
				expression=relation();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("expression", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("expression", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("expression", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("expression", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("expression", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("expression", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("expression", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("expression", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("expression", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return expression;
	}
	
	private final Expression  operator() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException,InvalidAtomException {
		Expression expression;
		
		
				expression = null;
			
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_SELECTION:
			case LITERAL_PROJECTION:
			case LITERAL_RENAMING:
			{
				expression=unary();
				break;
			}
			case LITERAL_LEFTPARENTHESES:
			{
				expression=binary();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("operator", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("operator", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("operator", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("operator", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("operator", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("operator", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("operator", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("operator", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("operator", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return expression;
	}
	
	private final Relation relation() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		Relation relation;
		
		
				String name;
				relation = new Relation();
			
		
		try {      // for error handling
			name=name();
			
					relation.setName(name);
				
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("relation", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("relation", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("relation", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("relation", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("relation", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("relation", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("relation", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("relation", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("relation", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return relation;
	}
	
	private final String  name() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		String name;
		
		Token  tempName = null;
		
				name = null;
			
		
		try {      // for error handling
			tempName = LT(1);
			match(STRING);
			
					name = tempName.getText();
				
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("name", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("name", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("name", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("name", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("name", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("name", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("name", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("name", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("name", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return name;
	}
	
	private final UnaryOperator unary() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException,InvalidAtomException {
		UnaryOperator uop;
		
		
				uop = null;
				Expression expression;
			
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_SELECTION:
			{
				uop=selection();
				break;
			}
			case LITERAL_PROJECTION:
			{
				uop=projection();
				break;
			}
			case LITERAL_RENAMING:
			{
				uop=renaming();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			match(LITERAL_LEFTPARENTHESES);
			expression=expression();
			match(LITERAL_RIGHTPARENTHESES);
			
					uop.setExpression(expression);
				
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("unary operator", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("unary operator", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("unary operator", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("unary operator", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("unary operator", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("unary operator", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("unary operator", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("unary operator", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("unary operator", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return uop;
	}
	
	private final BinaryOperator binary() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException,InvalidAtomException {
		BinaryOperator bop;
		
		
				bop = null;
				Expression leftExp;
				Expression rightExp;
			
		
		try {      // for error handling
			match(LITERAL_LEFTPARENTHESES);
			leftExp=expression();
			{
			switch ( LA(1)) {
			case LITERAL_JOIN:
			{
				bop=join();
				break;
			}
			case LITERAL_INTERSECTION:
			{
				bop=intersection();
				break;
			}
			case LITERAL_UNION:
			{
				bop=union();
				break;
			}
			case LITERAL_DIVISION:
			{
				bop=division();
				break;
			}
			case LITERAL_MINUS:
			{
				bop=minus();
				break;
			}
			case LITERAL_CARTESIANPRODUCT:
			{
				bop=cartesianProduct();
				break;
			}
			case LITERAL_LEFTSEMI:
			{
				bop=leftSemiJoin();
				break;
			}
			case LITERAL_RIGHTSEMI:
			{
				bop=rightSemiJoin();
				break;
			}
			case LITERAL_LEFTCURLY:
			{
				bop=thetaJoin();
				break;
			}
			case LITERAL_OUTER_JOIN:
			{
				bop=outerJoin();
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			rightExp=expression();
			match(LITERAL_RIGHTPARENTHESES);
			
					bop.setLeftExpression(leftExp);
					bop.setRightExpression(rightExp);
				
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("binary operator", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("binary operator", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("binary operator", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("binary operator", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("binary operator", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("binary operator", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("binary operator", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("binary operator", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("binary operator", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return bop;
	}
	
	private final Selection selection() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException,InvalidAtomException {
		Selection sel;
		
		
				String value;
				ComparisonOperator op;
		
				sel = new Selection();
				Comparison comp = new Comparison();
			
		
		try {      // for error handling
			match(LITERAL_SELECTION);
			match(LITERAL_LEFTBRACKET);
			{
			switch ( LA(1)) {
			case STRING:
			{
				value=attribute();
				
						comp.setLeftValue(value);
						comp.setLeftValueType(ComparisonValueType.ATTRIBUTE);
					
				break;
			}
			case NUMBER:
			{
				value=number();
				
						comp.setLeftValue(value);
						comp.setLeftValueType(ComparisonValueType.NUMBER);
					
				break;
			}
			default:
				if ((LA(1)==LITERAL_APOSTROPHE) && (LA(2)==NUMBER)) {
					value=date();
					
							comp.setLeftValue(value);
							comp.setLeftValueType(ComparisonValueType.DATE);
						
				}
				else if ((LA(1)==LITERAL_APOSTROPHE) && (LA(2)==STRING)) {
					value=literal();
					
							comp.setLeftValue(value);
							comp.setLeftValueType(ComparisonValueType.LITERAL);
						
				}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			op=comparisonOperator();
			
					comp.setOperator(op);
				
			{
			switch ( LA(1)) {
			case STRING:
			{
				value=attribute();
				
						comp.setRightValue(value);
						comp.setRightValueType(ComparisonValueType.ATTRIBUTE);
					
				break;
			}
			case NUMBER:
			{
				value=number();
				
						comp.setRightValue(value);
						comp.setRightValueType(ComparisonValueType.NUMBER);
					
				break;
			}
			default:
				if ((LA(1)==LITERAL_APOSTROPHE) && (LA(2)==NUMBER)) {
					value=date();
					
							comp.setRightValue(value);
							comp.setRightValueType(ComparisonValueType.DATE);
						
				}
				else if ((LA(1)==LITERAL_APOSTROPHE) && (LA(2)==STRING)) {
					value=literal();
					
							comp.setRightValue(value);
							comp.setRightValueType(ComparisonValueType.LITERAL);
						
				}
			else {
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
			
					sel.addComparison(comp);
				
			{
			_loop15:
			do {
				if ((LA(1)==LITERAL_COMMA)) {
					match(LITERAL_COMMA);
					
							comp = new Comparison();
						
					{
					switch ( LA(1)) {
					case STRING:
					{
						value=attribute();
						
								comp.setLeftValue(value);
								comp.setLeftValueType(ComparisonValueType.ATTRIBUTE);
							
						break;
					}
					case NUMBER:
					{
						value=number();
						
								comp.setLeftValue(value);
								comp.setLeftValueType(ComparisonValueType.NUMBER);
							
						break;
					}
					default:
						if ((LA(1)==LITERAL_APOSTROPHE) && (LA(2)==NUMBER)) {
							value=date();
							
									comp.setLeftValue(value);
									comp.setLeftValueType(ComparisonValueType.DATE);
								
						}
						else if ((LA(1)==LITERAL_APOSTROPHE) && (LA(2)==STRING)) {
							value=literal();
							
									comp.setLeftValue(value);
									comp.setLeftValueType(ComparisonValueType.LITERAL);
								
						}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					op=comparisonOperator();
					
							comp.setOperator(op);
						
					{
					switch ( LA(1)) {
					case STRING:
					{
						value=attribute();
						
								comp.setRightValue(value);
								comp.setRightValueType(ComparisonValueType.ATTRIBUTE);
							
						break;
					}
					case NUMBER:
					{
						value=number();
						
								comp.setRightValue(value);
								comp.setRightValueType(ComparisonValueType.NUMBER);
							
						break;
					}
					default:
						if ((LA(1)==LITERAL_APOSTROPHE) && (LA(2)==STRING)) {
							value=literal();
							
									comp.setRightValue(value);
									comp.setRightValueType(ComparisonValueType.LITERAL);
								
						}
						else if ((LA(1)==LITERAL_APOSTROPHE) && (LA(2)==NUMBER)) {
							value=date();
							
									comp.setRightValue(value);
									comp.setRightValueType(ComparisonValueType.DATE);
								
						}
					else {
						throw new NoViableAltException(LT(1), getFilename());
					}
					}
					}
					
							sel.addComparison(comp);
						
				}
				else {
					break _loop15;
				}
				
			} while (true);
			}
			match(LITERAL_RIGHTBRACKET);
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("selection", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("selection", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("selection", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("selection", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("selection", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("selection", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("selection", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("selection", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("selection", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return sel;
	}
	
	private final Projection  projection() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		Projection pro;
		
		
				String att;
				pro = new Projection();
			
		
		try {      // for error handling
			match(LITERAL_PROJECTION);
			match(LITERAL_LEFTBRACKET);
			att=attribute();
			
					pro.addProjectionAttribute(att);
				
			{
			_loop18:
			do {
				if ((LA(1)==LITERAL_COMMA)) {
					match(LITERAL_COMMA);
					att=attribute();
					
							pro.addProjectionAttribute(att);
						
				}
				else {
					break _loop18;
				}
				
			} while (true);
			}
			match(LITERAL_RIGHTBRACKET);
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("projection", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("projection", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("projection", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("projection", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("projection", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("projection", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("projection", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("projection", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("projection", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return pro;
	}
	
	private final Renaming  renaming() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		Renaming ren;
		
		
				String att;
				String alias;
				ren = new Renaming();
			
		
		try {      // for error handling
			match(LITERAL_RENAMING);
			match(LITERAL_LEFTBRACKET);
			att=attribute();
			match(LITERAL_LEFTARROW);
			alias=attribute();
			
					ren.addAttributeAlias(att,alias);
				
			{
			_loop21:
			do {
				if ((LA(1)==LITERAL_COMMA)) {
					match(LITERAL_COMMA);
					att=attribute();
					match(LITERAL_LEFTARROW);
					alias=attribute();
					
							ren.addAttributeAlias(att,alias);
						
				}
				else {
					break _loop21;
				}
				
			} while (true);
			}
			match(LITERAL_RIGHTBRACKET);
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("renaming", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("renaming", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("renaming", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("renaming", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("renaming", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("renaming", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("renaming", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("renaming", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("renaming", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return ren;
	}
	
	private final String  attribute() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		String attribute;
		
		Token  tempAttribute = null;
		
				attribute = null;
			
		
		try {      // for error handling
			tempAttribute = LT(1);
			match(STRING);
			
					attribute = tempAttribute.getText();
				
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("attribute", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("attribute", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("attribute", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("attribute", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("attribute", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("attribute", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("attribute", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("attribute", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("attribute", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return attribute;
	}
	
	private final String  date() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException,InvalidAtomException {
		String date;
		
		
				int column = 0;
				date = new String();
				String day = new String();
				String year = new String();
				String month = new String();
				GregorianCalendar gc = null;
			
		
		try {      // for error handling
			match(LITERAL_APOSTROPHE);
			
					column = this.LT(1).getColumn();
				
			day=day();
			match(LITERAL_DOT);
			month=month();
			match(LITERAL_DOT);
			year=year();
			match(LITERAL_APOSTROPHE);
			
					date = 	day + "." + month + "." + year;
					gc = new GregorianCalendar(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));
					gc.setLenient(false);
					gc.getTime();
				
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("date", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("date", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("date", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("date", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("date", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("date", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("date", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("date", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("date", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (IllegalArgumentException ex) {
			
					InvalidAtomException iae = new InvalidAtomException();
					iae.setRule("date");
					iae.setLine(this.LT(1).getLine());
					iae.setColumn(column);
					iae.setInvalidAtom(date);
					throw iae;
				
		}
		return date;
	}
	
	private final String  literal() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		String literal;
		
		Token  leadingLiteral = null;
		Token  followingLiteral = null;
		
				literal = null;
			
		
		try {      // for error handling
			match(LITERAL_APOSTROPHE);
			leadingLiteral = LT(1);
			match(STRING);
			
					literal=leadingLiteral.getText();
				
			{
			_loop42:
			do {
				if ((LA(1)==STRING)) {
					followingLiteral = LT(1);
					match(STRING);
					
							literal=literal.concat(" " + followingLiteral.getText());
						
				}
				else {
					break _loop42;
				}
				
			} while (true);
			}
			match(LITERAL_APOSTROPHE);
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("literal", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("literal", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("literal", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("literal", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("literal", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("literal", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("literal", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("literal", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("literal", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return literal;
	}
	
	private final String  number() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		String number;
		
		Token  tempNumber = null;
		
				number = null;
			
		
		try {      // for error handling
			tempNumber = LT(1);
			match(NUMBER);
			
					number = tempNumber.getText();
				
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("number", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("number", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("number", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("number", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("number", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("number", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("number", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("number", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("number", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return number;
	}
	
	private final ComparisonOperator  comparisonOperator() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		ComparisonOperator operator;
		
		
				operator = null;
			
		
		try {      // for error handling
			{
			switch ( LA(1)) {
			case LITERAL_LESSTHAN:
			{
				match(LITERAL_LESSTHAN);
				
						operator = ComparisonOperator.LESS_THAN;
					
				{
				switch ( LA(1)) {
				case LITERAL_EQUAL:
				{
					match(LITERAL_EQUAL);
					
							operator = ComparisonOperator.LESS_OR_EQUAL;
						
					break;
				}
				case LITERAL_GREATERTHAN:
				{
					match(LITERAL_GREATERTHAN);
					
							operator = ComparisonOperator.NOT_EQUAL;
						
					break;
				}
				case LITERAL_APOSTROPHE:
				case STRING:
				case NUMBER:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case LITERAL_GREATERTHAN:
			{
				match(LITERAL_GREATERTHAN);
				
						operator = ComparisonOperator.GREATER_THAN;
					
				{
				switch ( LA(1)) {
				case LITERAL_EQUAL:
				{
					match(LITERAL_EQUAL);
					
							operator = ComparisonOperator.GREATER_OR_EQUAL;
						
					break;
				}
				case LITERAL_APOSTROPHE:
				case STRING:
				case NUMBER:
				{
					break;
				}
				default:
				{
					throw new NoViableAltException(LT(1), getFilename());
				}
				}
				}
				break;
			}
			case LITERAL_EQUAL:
			{
				match(LITERAL_EQUAL);
				
						operator = ComparisonOperator.EQUAL;
					
				break;
			}
			default:
			{
				throw new NoViableAltException(LT(1), getFilename());
			}
			}
			}
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("comparison operator", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("comparison operator", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("comparison operator", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("comparison operator", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("comparison operator", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("comparison operator", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("comparison operator", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("comparison operator", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("comparison operator", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return operator;
	}
	
	private final Join  join() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		Join join;
		
		
				join = null;
			
		
		try {      // for error handling
			match(LITERAL_JOIN);
			
					join = new Join();
				
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("join", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("join", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("join", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("join", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("join", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("join", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("join", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("join", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("join", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return join;
	}
	
	private final Intersection  intersection() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		Intersection intersection;
		
		
				intersection = null;
			
		
		try {      // for error handling
			match(LITERAL_INTERSECTION);
			
					intersection = new Intersection();
				
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("intersection", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("intersection", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("intersection", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("intersection", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("intersection", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("intersection", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("intersection", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("intersection", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("intersection", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return intersection;
	}
	
	private final Union  union() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		Union union;
		
		
				union = null;
			
		
		try {      // for error handling
			match(LITERAL_UNION);
			
					union = new Union();
				
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("union", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("union", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("union", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("union", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("union", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("union", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("union", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("union", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("union", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return union;
	}
	
	private final Division  division() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		Division division;
		
		
				division = null;
			
		
		try {      // for error handling
			match(LITERAL_DIVISION);
			
					division = new Division();
				
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("division", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("division", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("division", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("division", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("division", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("division", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("division", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("division", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("division", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return division;
	}
	
	private final Minus  minus() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		Minus minus;
		
		
				minus = null;
			
		
		try {      // for error handling
			match(LITERAL_MINUS);
			
					minus = new Minus();
				
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("minus", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("minus", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("minus", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("minus", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("minus", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("minus", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("minus", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("minus", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("minus", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return minus;
	}
	
	private final CartesianProduct  cartesianProduct() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		CartesianProduct cartesianProduct;
		
		
				cartesianProduct = null;
			
		
		try {      // for error handling
			match(LITERAL_CARTESIANPRODUCT);
			
					cartesianProduct = new CartesianProduct();
				
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("cartesian product", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("cartesian product", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("cartesian product", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("cartesian product", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("cartesian product", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("cartesian product", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("cartesian product", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("cartesian product", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("cartesian product", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return cartesianProduct;
	}
	
	private final LeftSemiJoin  leftSemiJoin() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		LeftSemiJoin leftSemiJoin;
		
		
				leftSemiJoin = null;
			
		
		try {      // for error handling
			match(LITERAL_LEFTSEMI);
			
					leftSemiJoin = new LeftSemiJoin();
				
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("left semi join", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("left semi join", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("left semi join", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("left semi join", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("left semi join", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("left semi join", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("left semi join", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("left semi join", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("left semi join", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return leftSemiJoin;
	}
	
	private final RightSemiJoin  rightSemiJoin() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		RightSemiJoin rightSemiJoin;
		
		
				rightSemiJoin = null;
			
		
		try {      // for error handling
			match(LITERAL_RIGHTSEMI);
			
					rightSemiJoin = new RightSemiJoin();
				
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("right semi join", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("right semi join", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("right semi join", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("right semi join", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("right semi join", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("right semi join", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("right semi join", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("right semi join", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("right semi join", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return rightSemiJoin;
	}
	
	private final ThetaJoin  thetaJoin() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		ThetaJoin tj;
		
		
				String value;
				Comparison comp;
				ComparisonOperator op;
		
				value = null;
				tj = new ThetaJoin();
				comp = new Comparison();
			
		
		try {      // for error handling
			match(LITERAL_LEFTCURLY);
			value=attribute();
			
					comp.setLeftValue(value);
					comp.setLeftValueType(ComparisonValueType.ATTRIBUTE);
				
			op=comparisonOperator();
			
					comp.setOperator(op);
				
			value=attribute();
			
					comp.setRightValue(value);
					comp.setRightValueType(ComparisonValueType.ATTRIBUTE);
					tj.addComparison(comp);
				
			{
			_loop35:
			do {
				if ((LA(1)==LITERAL_COMMA)) {
					match(LITERAL_COMMA);
					
							comp = new Comparison();
						
					value=attribute();
					
							comp.setLeftValue(value);
							comp.setLeftValueType(ComparisonValueType.ATTRIBUTE);
						
					op=comparisonOperator();
					
							comp.setOperator(op);
						
					value=attribute();
					
							comp.setRightValue(value);
							comp.setRightValueType(ComparisonValueType.ATTRIBUTE);
							tj.addComparison(comp);
						
				}
				else {
					break _loop35;
				}
				
			} while (true);
			}
			match(LITERAL_RIGHTCURLY);
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("theta join", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("theta join", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("theta join", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("theta join", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("theta join", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("theta join", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("theta join", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("theta join", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("theta join", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return tj;
	}
	
	private final OuterJoin  outerJoin() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		OuterJoin outerJoin;
		
		
				outerJoin = null;
			
		
		try {      // for error handling
			match(LITERAL_OUTER_JOIN);
			
					outerJoin = new OuterJoin();
				
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("outer join", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("outer join", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("outer join", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("outer join", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("outer join", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("outer join", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("outer join", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("outer join", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("outer join", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return outerJoin;
	}
	
	private final String  day() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		String day;
		
		Token  tempDay = null;
		
				day = null;
			
		
		try {      // for error handling
			tempDay = LT(1);
			match(NUMBER);
			
					day = tempDay.getText();
				
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("day", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("day", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("day", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("day", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("day", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("day", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("day", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("day", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("day", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return day;
	}
	
	private final String  month() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		String month;
		
		Token  tempMonth = null;
		
				month = null;
			
		
		try {      // for error handling
			tempMonth = LT(1);
			match(NUMBER);
			
					month = tempMonth.getText();
				
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("month", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("month", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("month", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("month", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("month", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("month", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("month", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("month", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("month", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return month;
	}
	
	private final String  year() throws RecognitionException, TokenStreamException, UnexpectedAtomException,MismatchedAtomException,AtomStreamIOException {
		String year;
		
		Token  tempYear = null;
		
				year = null;
			
		
		try {      // for error handling
			tempYear = LT(1);
			match(NUMBER);
			
					year = tempYear.getText();
				
		}
		catch (RecognitionException ex) {
			
					if (ex instanceof MismatchedCharException) {
						throw new MismatchedAtomException("year", (MismatchedCharException)ex);
					}
					if (ex instanceof MismatchedTokenException) {
						throw new MismatchedAtomException("year", (MismatchedTokenException)ex, this.tokenNames);
					}
					if (ex instanceof NoViableAltException) {
						throw new UnexpectedAtomException("year", (NoViableAltException)ex);
					}
					if (ex instanceof NoViableAltForCharException) {
						throw new UnexpectedAtomException("year", (NoViableAltForCharException)ex);
					}
					if (ex instanceof SemanticException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		catch (TokenStreamException ex) {
			
					if (ex instanceof TokenStreamIOException) {
						throw new AtomStreamIOException("year", (TokenStreamIOException)ex);
					}
					if (ex instanceof TokenStreamRecognitionException) {
						RecognitionException re = ((TokenStreamRecognitionException)ex).recog;
						if (re instanceof MismatchedCharException) {
							throw new MismatchedAtomException("year", (MismatchedCharException)re);
						}
						if (re instanceof MismatchedTokenException) {
							throw new MismatchedAtomException("year", (MismatchedTokenException)re, this.tokenNames);
						}
						if (re instanceof NoViableAltException) {
							throw new UnexpectedAtomException("year", (NoViableAltException)re);
						}
						if (re instanceof NoViableAltForCharException) {
							throw new UnexpectedAtomException("year", (NoViableAltForCharException)re);
						}
						if (re instanceof SemanticException) {
							//Impossible to happen in current grammar!
							re.printStackTrace();
						}
					}
					if (ex instanceof TokenStreamRetryException) {
						//Impossible to happen in current grammar!
						ex.printStackTrace();
					}
				
		}
		return year;
	}
	
	
	public static final String[] _tokenNames = {
		"<0>",
		"EOF",
		"<2>",
		"NULL_TREE_LOOKAHEAD",
		"\"LEFTPARENTHESES\"",
		"\"RIGHTPARENTHESES\"",
		"\"SELECTION\"",
		"\"LEFTBRACKET\"",
		"\"COMMA\"",
		"\"RIGHTBRACKET\"",
		"\"PROJECTION\"",
		"\"RENAMING\"",
		"\"LEFTARROW\"",
		"\"JOIN\"",
		"\"INTERSECTION\"",
		"\"UNION\"",
		"\"OUTER_JOIN\"",
		"\"DIVISION\"",
		"\"MINUS\"",
		"\"CARTESIANPRODUCT\"",
		"\"LEFTSEMI\"",
		"\"RIGHTSEMI\"",
		"\"LEFTCURLY\"",
		"\"RIGHTCURLY\"",
		"\"LESSTHAN\"",
		"\"EQUAL\"",
		"\"GREATERTHAN\"",
		"\"APOSTROPHE\"",
		"a string",
		"a number",
		"\"DOT\"",
		"WS"
	};
	
	
	}
