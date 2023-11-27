// Generated from NF.g4 by ANTLR 4.13.1

package at.jku.dke.etutor.modules.nf.parser ;

import java.util.Set;
import java.util.HashSet;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link NFParser}.
 */
public interface NFListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link NFParser#relationSet}.
	 * @param ctx the parse tree
	 */
	void enterRelationSet(NFParser.RelationSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#relationSet}.
	 * @param ctx the parse tree
	 */
	void exitRelationSet(NFParser.RelationSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link NFParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterRelation(NFParser.RelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitRelation(NFParser.RelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link NFParser#keySet}.
	 * @param ctx the parse tree
	 */
	void enterKeySet(NFParser.KeySetContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#keySet}.
	 * @param ctx the parse tree
	 */
	void exitKeySet(NFParser.KeySetContext ctx);
	/**
	 * Enter a parse tree produced by {@link NFParser#key}.
	 * @param ctx the parse tree
	 */
	void enterKey(NFParser.KeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#key}.
	 * @param ctx the parse tree
	 */
	void exitKey(NFParser.KeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link NFParser#violatedNormalForm}.
	 * @param ctx the parse tree
	 */
	void enterViolatedNormalForm(NFParser.ViolatedNormalFormContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#violatedNormalForm}.
	 * @param ctx the parse tree
	 */
	void exitViolatedNormalForm(NFParser.ViolatedNormalFormContext ctx);
	/**
	 * Enter a parse tree produced by {@link NFParser#normalForm}.
	 * @param ctx the parse tree
	 */
	void enterNormalForm(NFParser.NormalFormContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#normalForm}.
	 * @param ctx the parse tree
	 */
	void exitNormalForm(NFParser.NormalFormContext ctx);
	/**
	 * Enter a parse tree produced by {@link NFParser#attributeClosure}.
	 * @param ctx the parse tree
	 */
	void enterAttributeClosure(NFParser.AttributeClosureContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#attributeClosure}.
	 * @param ctx the parse tree
	 */
	void exitAttributeClosure(NFParser.AttributeClosureContext ctx);
	/**
	 * Enter a parse tree produced by {@link NFParser#functionalDependencySet}.
	 * @param ctx the parse tree
	 */
	void enterFunctionalDependencySet(NFParser.FunctionalDependencySetContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#functionalDependencySet}.
	 * @param ctx the parse tree
	 */
	void exitFunctionalDependencySet(NFParser.FunctionalDependencySetContext ctx);
	/**
	 * Enter a parse tree produced by {@link NFParser#functionalDependency}.
	 * @param ctx the parse tree
	 */
	void enterFunctionalDependency(NFParser.FunctionalDependencyContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#functionalDependency}.
	 * @param ctx the parse tree
	 */
	void exitFunctionalDependency(NFParser.FunctionalDependencyContext ctx);
	/**
	 * Enter a parse tree produced by {@link NFParser#attributeSet}.
	 * @param ctx the parse tree
	 */
	void enterAttributeSet(NFParser.AttributeSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#attributeSet}.
	 * @param ctx the parse tree
	 */
	void exitAttributeSet(NFParser.AttributeSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link NFParser#attribute}.
	 * @param ctx the parse tree
	 */
	void enterAttribute(NFParser.AttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#attribute}.
	 * @param ctx the parse tree
	 */
	void exitAttribute(NFParser.AttributeContext ctx);
}