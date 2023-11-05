// Generated from C:/ETutor/dke-dispatcher/src/main/java/at/jku/dke/etutor/modules/rt/analysis/rtSyntax.g4 by ANTLR 4.13.1
package at.jku.dke.etutor.modules.rt.analysis;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link rtSyntaxParser}.
 */
public interface rtSyntaxListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link rtSyntaxParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(rtSyntaxParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link rtSyntaxParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(rtSyntaxParser.StartContext ctx);
}