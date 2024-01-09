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

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link NFParser}.
 */
public interface NFListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link NFParser#relationSetSubmission}.
	 * @param ctx the parse tree
	 */
	void enterRelationSetSubmission(NFParser.RelationSetSubmissionContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#relationSetSubmission}.
	 * @param ctx the parse tree
	 */
	void exitRelationSetSubmission(NFParser.RelationSetSubmissionContext ctx);
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
	 * Enter a parse tree produced by {@link NFParser#relationId}.
	 * @param ctx the parse tree
	 */
	void enterRelationId(NFParser.RelationIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#relationId}.
	 * @param ctx the parse tree
	 */
	void exitRelationId(NFParser.RelationIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link NFParser#keySetSubmission}.
	 * @param ctx the parse tree
	 */
	void enterKeySetSubmission(NFParser.KeySetSubmissionContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#keySetSubmission}.
	 * @param ctx the parse tree
	 */
	void exitKeySetSubmission(NFParser.KeySetSubmissionContext ctx);
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
	 * Enter a parse tree produced by {@link NFParser#normalFormSubmission}.
	 * @param ctx the parse tree
	 */
	void enterNormalFormSubmission(NFParser.NormalFormSubmissionContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#normalFormSubmission}.
	 * @param ctx the parse tree
	 */
	void exitNormalFormSubmission(NFParser.NormalFormSubmissionContext ctx);
	/**
	 * Enter a parse tree produced by {@link NFParser#normalFormViolationSet}.
	 * @param ctx the parse tree
	 */
	void enterNormalFormViolationSet(NFParser.NormalFormViolationSetContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#normalFormViolationSet}.
	 * @param ctx the parse tree
	 */
	void exitNormalFormViolationSet(NFParser.NormalFormViolationSetContext ctx);
	/**
	 * Enter a parse tree produced by {@link NFParser#normalFormViolation}.
	 * @param ctx the parse tree
	 */
	void enterNormalFormViolation(NFParser.NormalFormViolationContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#normalFormViolation}.
	 * @param ctx the parse tree
	 */
	void exitNormalFormViolation(NFParser.NormalFormViolationContext ctx);
	/**
	 * Enter a parse tree produced by {@link NFParser#normalFormSpecification}.
	 * @param ctx the parse tree
	 */
	void enterNormalFormSpecification(NFParser.NormalFormSpecificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#normalFormSpecification}.
	 * @param ctx the parse tree
	 */
	void exitNormalFormSpecification(NFParser.NormalFormSpecificationContext ctx);
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
	 * Enter a parse tree produced by {@link NFParser#functionalDependencySetSubmission}.
	 * @param ctx the parse tree
	 */
	void enterFunctionalDependencySetSubmission(NFParser.FunctionalDependencySetSubmissionContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#functionalDependencySetSubmission}.
	 * @param ctx the parse tree
	 */
	void exitFunctionalDependencySetSubmission(NFParser.FunctionalDependencySetSubmissionContext ctx);
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
	 * Enter a parse tree produced by {@link NFParser#attributeSetSubmission}.
	 * @param ctx the parse tree
	 */
	void enterAttributeSetSubmission(NFParser.AttributeSetSubmissionContext ctx);
	/**
	 * Exit a parse tree produced by {@link NFParser#attributeSetSubmission}.
	 * @param ctx the parse tree
	 */
	void exitAttributeSetSubmission(NFParser.AttributeSetSubmissionContext ctx);
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