// Generated from nf.g4 by ANTLR 4.13.1

    package at.jku.dke.etutor.modules.nf.parser ;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link nfParser}.
 */
public interface nfListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link nfParser#relationSet}.
	 * @param ctx the parse tree
	 */
	void enterRelationSet(nfParser.RelationSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link nfParser#relationSet}.
	 * @param ctx the parse tree
	 */
	void exitRelationSet(nfParser.RelationSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link nfParser#relation}.
	 * @param ctx the parse tree
	 */
	void enterRelation(nfParser.RelationContext ctx);
	/**
	 * Exit a parse tree produced by {@link nfParser#relation}.
	 * @param ctx the parse tree
	 */
	void exitRelation(nfParser.RelationContext ctx);
	/**
	 * Enter a parse tree produced by {@link nfParser#keySet}.
	 * @param ctx the parse tree
	 */
	void enterKeySet(nfParser.KeySetContext ctx);
	/**
	 * Exit a parse tree produced by {@link nfParser#keySet}.
	 * @param ctx the parse tree
	 */
	void exitKeySet(nfParser.KeySetContext ctx);
	/**
	 * Enter a parse tree produced by {@link nfParser#key}.
	 * @param ctx the parse tree
	 */
	void enterKey(nfParser.KeyContext ctx);
	/**
	 * Exit a parse tree produced by {@link nfParser#key}.
	 * @param ctx the parse tree
	 */
	void exitKey(nfParser.KeyContext ctx);
	/**
	 * Enter a parse tree produced by {@link nfParser#violatedNormalForm}.
	 * @param ctx the parse tree
	 */
	void enterViolatedNormalForm(nfParser.ViolatedNormalFormContext ctx);
	/**
	 * Exit a parse tree produced by {@link nfParser#violatedNormalForm}.
	 * @param ctx the parse tree
	 */
	void exitViolatedNormalForm(nfParser.ViolatedNormalFormContext ctx);
	/**
	 * Enter a parse tree produced by {@link nfParser#normalForm}.
	 * @param ctx the parse tree
	 */
	void enterNormalForm(nfParser.NormalFormContext ctx);
	/**
	 * Exit a parse tree produced by {@link nfParser#normalForm}.
	 * @param ctx the parse tree
	 */
	void exitNormalForm(nfParser.NormalFormContext ctx);
	/**
	 * Enter a parse tree produced by {@link nfParser#attributeClosure}.
	 * @param ctx the parse tree
	 */
	void enterAttributeClosure(nfParser.AttributeClosureContext ctx);
	/**
	 * Exit a parse tree produced by {@link nfParser#attributeClosure}.
	 * @param ctx the parse tree
	 */
	void exitAttributeClosure(nfParser.AttributeClosureContext ctx);
	/**
	 * Enter a parse tree produced by {@link nfParser#functionalDependencySet}.
	 * @param ctx the parse tree
	 */
	void enterFunctionalDependencySet(nfParser.FunctionalDependencySetContext ctx);
	/**
	 * Exit a parse tree produced by {@link nfParser#functionalDependencySet}.
	 * @param ctx the parse tree
	 */
	void exitFunctionalDependencySet(nfParser.FunctionalDependencySetContext ctx);
	/**
	 * Enter a parse tree produced by {@link nfParser#functionalDependency}.
	 * @param ctx the parse tree
	 */
	void enterFunctionalDependency(nfParser.FunctionalDependencyContext ctx);
	/**
	 * Exit a parse tree produced by {@link nfParser#functionalDependency}.
	 * @param ctx the parse tree
	 */
	void exitFunctionalDependency(nfParser.FunctionalDependencyContext ctx);
	/**
	 * Enter a parse tree produced by {@link nfParser#attributeSet}.
	 * @param ctx the parse tree
	 */
	void enterAttributeSet(nfParser.AttributeSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link nfParser#attributeSet}.
	 * @param ctx the parse tree
	 */
	void exitAttributeSet(nfParser.AttributeSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link nfParser#attribute}.
	 * @param ctx the parse tree
	 */
	void enterAttribute(nfParser.AttributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link nfParser#attribute}.
	 * @param ctx the parse tree
	 */
	void exitAttribute(nfParser.AttributeContext ctx);
}