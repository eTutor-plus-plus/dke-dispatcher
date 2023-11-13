package at.jku.dke.etutor.modules.nf.ui;

import at.jku.dke.etutor.modules.nf.*;
import etutor.core.ui.Response;
import etutor.core.ui.ShowEditorView;
import etutor.core.ws.ui.DefaultPrintView;
import etutor.modules.sql.SQLConstants;
import org.apache.velocity.app.FieldMethodizer;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.io.Serializable;
import java.util.*;

public class RDBDShowEditorView extends DefaultPrintView implements
		ShowEditorView {
	protected final int rdbdType;

	public RDBDShowEditorView(int rdbdType) {
		this.rdbdType = rdbdType;
	}

	@Override
	public Response getResponse(HashMap<String, Object> passedAttributes,
			Locale locale, ArrayList<String> cachedResources) throws Exception {
		return this.generateResponse(generateXhtml(passedAttributes, locale),
				cachedResources);
	}

	private String generateXhtml(HashMap<String, Object> passedAttributes,
			Locale locale) {
		StringBuilder xhtml = new StringBuilder();

		Set actions = passedAttributes.get(SQLConstants.ATTR_ACTIONS) != null ? (Set) passedAttributes
				.get(SQLConstants.ATTR_ACTIONS)
				: null;

		Map<String, Object> model = new HashMap<String, Object>();
		int exerciseId = Integer.parseInt(passedAttributes.get(
				RDBDConstants.ATT_EXERCISE_ID).toString());
		Serializable submission = (Serializable) passedAttributes
				.get(RDBDConstants.calcSubmissionIDFor(exerciseId));

		model.put("actions", actions);
		model.put("rdbdconstants", new FieldMethodizer(
				"etutor.modules.rdbd.RDBDConstants"));
		model.put("sqlconstants", new FieldMethodizer(
				"etutor.modules.sql.SQLConstants"));
		model.put("locale", locale);

		model.put("messageButtondiagnose", getMessageSource().getMessage(
				"editorview.buttondiagnose", null, locale));
		model.put("messageButtonsubmit", getMessageSource().getMessage(
				"editorview.buttonsubmit", null, locale));
		String velocityTemplate = "";

		if (rdbdType == RDBDConstants.TYPE_KEYS_DETERMINATION) {
			IdentifiedRelation specification = (IdentifiedRelation) passedAttributes
					.get(RDBDConstants.calcSpecificationIDFor(exerciseId));
			model.put("specification", specification);
			IdentifiedRelation relation = (IdentifiedRelation) ((Vector) submission)
					.get(0);
			model.put("relation", relation);
			model.put("numberAttempts", passedAttributes.get("numberAttempts"));
			model.put("sessionAttributes", passedAttributes);
			model.put("HTMLPrinter", HTMLPrinter.class);
			model.put("messagePrompt", getMessageSource().getMessage(
					"rdbdkeysshoweditorview.prompt", null, locale));
			velocityTemplate = "resources/rdbd/showeditorview/rdbdKeysShowEditorView.vm";
		} else if (rdbdType == RDBDConstants.TYPE_NORMALIZATION) {
			model.put("submission", submission);
			NormalizationSpecification specification = (NormalizationSpecification) passedAttributes
					.get(RDBDConstants.calcSpecificationIDFor(exerciseId));
			model.put("specification", specification);
			model.put("numberAttempts", passedAttributes.get("numberAttempts"));
			model.put("sessionAttributes", passedAttributes);
			model.put("HTMLPrinter", HTMLPrinter.class);
			model.put("messageRelation", getMessageSource().getMessage(
					"rdbdnormaliseshoweditorview.relation", null, locale));
			model
					.put(
							"messageRemoverelation",
							getMessageSource()
									.getMessage(
											"rdbdnormaliseshoweditorview.removerelation",
											null, locale));
			model.put("messageAddrelation", getMessageSource().getMessage(
					"rdbdnormaliseshoweditorview.addrelation", null, locale));
			velocityTemplate = "resources/rdbd/showeditorview/rdbdNormaliseShowEditorView.vm";
		} else if (rdbdType == RDBDConstants.TYPE_MINIMAL_COVER) {
			IdentifiedRelation specification = (IdentifiedRelation) passedAttributes
					.get(RDBDConstants.calcSpecificationIDFor(exerciseId));
			model.put("specification", specification);
			IdentifiedRelation relation = (IdentifiedRelation) ((Vector) submission)
					.get(0);
			model.put("relation", relation);
			model.put("numberAttempts", passedAttributes.get("numberAttempts"));
			model.put("sessionAttributes", passedAttributes);
			model.put("HTMLPrinter", HTMLPrinter.class);
			model.put("messagePrompt", getMessageSource().getMessage(
					"rdbdcovershoweditorview.prompt", null, locale));
			velocityTemplate = "resources/rdbd/showeditorview/rdbdCoverShowEditorView.vm";
		} else if (rdbdType == RDBDConstants.TYPE_ATTRIBUTE_CLOSURE) {
			AttributeClosureSpecification specification = (AttributeClosureSpecification) passedAttributes
					.get(RDBDConstants.calcSpecificationIDFor(exerciseId));
			model.put("specification", specification);
			IdentifiedRelation relation = (IdentifiedRelation) ((Vector) submission)
					.get(0);
			model.put("relation", relation);
			model.put("numberAttempts", passedAttributes.get("numberAttempts"));
			model.put("sessionAttributes", passedAttributes);
			model.put("HTMLPrinter", HTMLPrinter.class);
			model.put("messagePrompt", getMessageSource().getMessage(
					"rdbdclosureshoweditorview.prompt", null, locale));
			velocityTemplate = "resources/rdbd/showeditorview/rdbdClosureShowEditorView.vm";
		} else if (rdbdType == RDBDConstants.TYPE_RBR) {
			RBRSpecification specification = (RBRSpecification) passedAttributes
					.get(RDBDConstants.calcSpecificationIDFor(exerciseId));
			model.put("specification", specification);
			IdentifiedRelation relation = (IdentifiedRelation) ((Vector) submission)
					.get(0);
			model.put("relation", relation);
			model.put("numberAttempts", passedAttributes.get("numberAttempts"));
			model.put("sessionAttributes", passedAttributes);
			model.put("HTMLPrinter", HTMLPrinter.class);
			model.put("messagePrompt", getMessageSource().getMessage(
					"rdbdrbrshoweditorview.prompt", null, locale));
			velocityTemplate = "resources/rdbd/showeditorview/rdbdRbrShowEditorView.vm";
		} else if (rdbdType == RDBDConstants.TYPE_DECOMPOSE) {
			model.put("submission", submission);
			model.put("diagnoseRelationID", passedAttributes
					.get(RDBDConstants.PARAM_DIAGNOSE_RELATION));
			DecomposeSpecification specification = (DecomposeSpecification) passedAttributes
					.get(RDBDConstants.calcSpecificationIDFor(exerciseId));
			model.put("specification", specification);
			model.put("hiddenRelations", passedAttributes
					.get("hiddenRelations"));
			model.put("HTMLPrinter", HTMLPrinter.class);
			model.put("RDBDHelper", RDBDHelper.class);
			model.put("numberAttempts", passedAttributes.get("numberAttempts"));
			model.put("sessionAttributes", passedAttributes);

			model.put("messageRelations", getMessageSource().getMessage(
					"rdbddecomposeshoweditorview.relations", null, locale));
			velocityTemplate = "resources/rdbd/showeditorview/rdbdDecomposeShowEditorView.vm";
		} else if (rdbdType == RDBDConstants.TYPE_NORMALFORM_DETERMINATION) {
			model.put("submission", submission);
			model.put("sqlconstants", new FieldMethodizer(
					"etutor.modules.sql.SQLConstants"));
			model.put("normalformlevel", new FieldMethodizer(
					"etutor.modules.rdbd.model.NormalformLevel"));
			model.put("messagePrompt", getMessageSource().getMessage(
					"rdbdnormalformshoweditorview.prompt", null, locale));
			model.put("messageNone", getMessageSource().getMessage(
					"rdbdnormalformshoweditorview.none", null, locale));
			model.put("messageFirst", getMessageSource().getMessage(
					"rdbdnormalformshoweditorview.first", null, locale));
			model.put("messageSecond", getMessageSource().getMessage(
					"rdbdnormalformshoweditorview.second", null, locale));
			model.put("messageThird", getMessageSource().getMessage(
					"rdbdnormalformshoweditorview.third", null, locale));
			model.put("messageBoycecodd", getMessageSource().getMessage(
					"rdbdnormalformshoweditorview.boycecodd", null, locale));
			model
					.put("messageViolatedlevel", getMessageSource().getMessage(
							"rdbdnormalformshoweditorview.violatedlevel", null,
							locale));
			velocityTemplate = "resources/rdbd/showeditorview/rdbdNormalformShowEditorView.vm";
		}
		xhtml.append(VelocityEngineUtils.mergeTemplateIntoString(
				getVelocityEngine(), velocityTemplate, model));
		return xhtml.toString();
	}
}
