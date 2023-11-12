package at.jku.dke.etutor.modules.nf.ui;

import etutor.core.ui.ExerciseSettingView;
import etutor.core.ui.Response;
import etutor.core.ws.ui.DefaultPrintView;
import etutor.modules.rdbd.RDBDConstants;
import etutor.modules.rdbd.RDBDSpecification;
import etutor.modules.rdbd.algorithms.Closure;
import etutor.modules.rdbd.algorithms.ReductionByResolution;
import org.apache.velocity.app.FieldMethodizer;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RDBDExerciseSettingView extends DefaultPrintView implements ExerciseSettingView {
	protected int rdbdType;
	
	public RDBDExerciseSettingView (int rdbdType) {
		this.rdbdType = rdbdType;
	}

	@Override
	public Response getResponse(Locale locale, ArrayList<String> cachedResources, Serializable exerciseBean, HashMap<String, Object> sessionAttributes) throws Exception {
		return this.generateResponse(generateXhtml(locale, exerciseBean), cachedResources);
	}

	private String generateXhtml(Locale locale, Serializable exerciseBean) {
		StringBuilder xhtml = new StringBuilder();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("rdbdExerciseBean", exerciseBean);
		RDBDSpecification specification = ((SpecificationEditor) exerciseBean).getSpecTmp();
		model.put("specification", specification);
		model.put("rdbdconstants", new FieldMethodizer( "etutor.modules.rdbd.RDBDConstants" ));
		model.put("normalformlevel", new FieldMethodizer( "etutor.modules.rdbd.model.NormalformLevel" ));
		model.put("locale", locale);
		model.put("HTMLPrinter", HTMLPrinter.class);
		model.put("messageOk", getMessageSource().getMessage("showpopup.ok", null, locale));
		
		String velocityTemplate = "";
		if (rdbdType == RDBDConstants.TYPE_KEYS_DETERMINATION) {
			model.put("messageTitle", getMessageSource().getMessage("rdbdkeysexercisesettingview.title", null, locale));
			model.put("messageInfo", getMessageSource().getMessage("rdbdkeysexercisesettingview.info", null, locale));
			model.put("messageSpecification", getMessageSource().getMessage("rdbdkeysexercisesettingview.specification", null, locale));
			model.put("messageSpecificationdefinition", getMessageSource().getMessage("rdbdkeysexercisesettingview.specificationdefinition", null, locale));
			model.put("messageSyntax", getMessageSource().getMessage("rdbdkeysexercisesettingview.syntax", null, locale));
			model.put("messagePreview", getMessageSource().getMessage("rdbdkeysexercisesettingview.preview", null, locale));
			model.put("messageReset", getMessageSource().getMessage("rdbdkeysexercisesettingview.reset", null, locale));
			velocityTemplate = "resources/rdbd/exercisesettingview/rdbdKeysExerciseSettingView.vm";
		} else if (rdbdType == RDBDConstants.TYPE_NORMALIZATION) {
			model.put("messageTitle", getMessageSource().getMessage("rdbdnormaliseexercisesettingview.title", null, locale));
			model.put("messageSpecification", getMessageSource().getMessage("rdbdnormaliseexercisesettingview.specification", null, locale));
			model.put("messageModifyspecification", getMessageSource().getMessage("rdbdnormaliseexercisesettingview.modifyspecification", null, locale));
			model.put("messageSyntax", getMessageSource().getMessage("rdbdnormaliseexercisesettingview.syntax", null, locale));
			model.put("messageNormalform", getMessageSource().getMessage("rdbdnormaliseexercisesettingview.normalform", null, locale));
			model.put("messageMaxlostdependencies", getMessageSource().getMessage("rdbdnormaliseexercisesettingview.maxlostdependencies", null, locale));
			model.put("messageFirst", getMessageSource().getMessage("rdbdnormaliseexercisesettingview.first", null, locale));
			model.put("messageSecond", getMessageSource().getMessage("rdbdnormaliseexercisesettingview.second", null, locale));
			model.put("messageThird", getMessageSource().getMessage("rdbdnormaliseexercisesettingview.third", null, locale));
			model.put("messageBoycecodd", getMessageSource().getMessage("rdbdnormaliseexercisesettingview.boycecodd", null, locale));
			model.put("messagePreview", getMessageSource().getMessage("rdbdnormaliseexercisesettingview.preview", null, locale));
			model.put("messageReset", getMessageSource().getMessage("rdbdnormaliseexercisesettingview.reset", null, locale));
			velocityTemplate = "resources/rdbd/exercisesettingview/rdbdNormaliseExerciseSettingView.vm";
		} else if (rdbdType == RDBDConstants.TYPE_MINIMAL_COVER) {
			model.put("messageTitle", getMessageSource().getMessage("rdbdcoverexercisesettingview.title", null, locale));
			model.put("messageInfo", getMessageSource().getMessage("rdbdcoverexercisesettingview.info", null, locale));
			model.put("messageSpecification", getMessageSource().getMessage("rdbdcoverexercisesettingview.specification", null, locale));
			model.put("messageSpecificationdefinition", getMessageSource().getMessage("rdbdcoverexercisesettingview.specificationdefinition", null, locale));
			model.put("messageSyntax", getMessageSource().getMessage("rdbdcoverexercisesettingview.syntax", null, locale));
			model.put("messagePreview", getMessageSource().getMessage("rdbdcoverexercisesettingview.preview", null, locale));
			model.put("messageReset", getMessageSource().getMessage("rdbdcoverexercisesettingview.reset", null, locale));
			model.put("messageDependenciesnormalforms", getMessageSource().getMessage("rdbdcoverexercisesettingview.dependenciesnormalforms", null, locale));
			velocityTemplate = "resources/rdbd/exercisesettingview/rdbdCoverExerciseSettingView.vm";
		} else if (rdbdType == RDBDConstants.TYPE_ATTRIBUTE_CLOSURE) {
			model.put("Closure", Closure.class);
			model.put("messageTitle", getMessageSource().getMessage("rdbdclosureexercisesettingview.title", null, locale));
			model.put("messageInfo", getMessageSource().getMessage("rdbdclosureexercisesettingview.info", null, locale));
			model.put("messageSpecification", getMessageSource().getMessage("rdbdclosureexercisesettingview.specification", null, locale));
			model.put("messageModifyspecification", getMessageSource().getMessage("rdbdclosureexercisesettingview.modifyspecification", null, locale));
			model.put("messageSyntax", getMessageSource().getMessage("rdbdclosureexercisesettingview.syntax", null, locale));
			model.put("messagePreview", getMessageSource().getMessage("rdbdclosureexercisesettingview.preview", null, locale));
			model.put("messageReset", getMessageSource().getMessage("rdbdclosureexercisesettingview.reset", null, locale));
			model.put("messageAttributecombination", getMessageSource().getMessage("rdbdclosureexercisesettingview.attributecombination", null, locale));
			model.put("messageAttributeclosure", getMessageSource().getMessage("rdbdclosureexercisesettingview.attributeclosure", null, locale));
			velocityTemplate = "resources/rdbd/exercisesettingview/rdbdClosureExerciseSettingView.vm";
		} else if (rdbdType == RDBDConstants.TYPE_RBR) {
			model.put("ReductionByResolution", ReductionByResolution.class);
			model.put("messageTitle", getMessageSource().getMessage("rdbdrbrexercisesettingview.title", null, locale));
			model.put("messageSpecification", getMessageSource().getMessage("rdbdrbrexercisesettingview.specification", null, locale));
			model.put("messageSubscheme", getMessageSource().getMessage("rdbdrbrexercisesettingview.subscheme", null, locale));
			model.put("messageDeriveddependencies", getMessageSource().getMessage("rdbdrbrexercisesettingview.deriveddependencies", null, locale));
			model.put("messageModifyspecification", getMessageSource().getMessage("rdbdrbrexercisesettingview.modifyspecification", null, locale));
			model.put("messageSyntax", getMessageSource().getMessage("rdbdrbrexercisesettingview.syntax", null, locale));
			model.put("messagePreview", getMessageSource().getMessage("rdbdrbrexercisesettingview.preview", null, locale));
			model.put("messageReset", getMessageSource().getMessage("rdbdrbrexercisesettingview.reset", null, locale));
			velocityTemplate = "resources/rdbd/exercisesettingview/rdbdRbrExerciseSettingView.vm";
		} else if (rdbdType == RDBDConstants.TYPE_DECOMPOSE) {
			model.put("messageTitle", getMessageSource().getMessage("rdbddecomposeexercisesettingview.title", null, locale));
			model.put("messageInfo", getMessageSource().getMessage("rdbddecomposeexercisesettingview.info", null, locale));
			model.put("messageSpecification", getMessageSource().getMessage("rdbddecomposeexercisesettingview.specification", null, locale));
			model.put("messageModifyspecification", getMessageSource().getMessage("rdbddecomposeexercisesettingview.modifyspecification", null, locale));
			model.put("messageSyntax", getMessageSource().getMessage("rdbddecomposeexercisesettingview.syntax", null, locale));
			model.put("messagePreview", getMessageSource().getMessage("rdbddecomposeexercisesettingview.preview", null, locale));
			model.put("messageReset", getMessageSource().getMessage("rdbddecomposeexercisesettingview.reset", null, locale));
			model.put("messageNormalform", getMessageSource().getMessage("rdbddecomposeexercisesettingview.normalform", null, locale));
			model.put("messageLostdependencies", getMessageSource().getMessage("rdbddecomposeexercisesettingview.lostdependencies", null, locale));
			model.put("messageFirst", getMessageSource().getMessage("rdbddecomposeexercisesettingview.first", null, locale));
			model.put("messageSecond", getMessageSource().getMessage("rdbddecomposeexercisesettingview.second", null, locale));
			model.put("messageThird", getMessageSource().getMessage("rdbddecomposeexercisesettingview.third", null, locale));
			model.put("messageBoycecodd", getMessageSource().getMessage("rdbddecomposeexercisesettingview.boycecodd", null, locale));
			velocityTemplate = "resources/rdbd/exercisesettingview/rdbdDecomposeExerciseSettingView.vm";
		} else if (rdbdType == RDBDConstants.TYPE_NORMALFORM_DETERMINATION) {
			model.put("messageTitle", getMessageSource().getMessage("rdbdnormalformexercisesettingview.title", null, locale));
			model.put("messageInfo", getMessageSource().getMessage("rdbdnormalformexercisesettingview.info", null, locale));
			model.put("messageSpecification", getMessageSource().getMessage("rdbdnormalformexercisesettingview.specification", null, locale));
			model.put("messageSpecificationdefinition", getMessageSource().getMessage("rdbdnormalformexercisesettingview.specificationdefinition", null, locale));
			model.put("messageSyntax", getMessageSource().getMessage("rdbdnormalformexercisesettingview.syntax", null, locale));
			model.put("messagePreview", getMessageSource().getMessage("rdbdnormalformexercisesettingview.preview", null, locale));
			model.put("messageReset", getMessageSource().getMessage("rdbdnormalformexercisesettingview.reset", null, locale));
			model.put("messageDependenciesnormalforms", getMessageSource().getMessage("rdbdnormalformexercisesettingview.dependenciesnormalforms", null, locale));
			velocityTemplate = "resources/rdbd/exercisesettingview/rdbdNormalformExerciseSettingView.vm";
		}
		xhtml.append(VelocityEngineUtils.mergeTemplateIntoString(getVelocityEngine(),
                velocityTemplate, model));
		return xhtml.toString();
	}
}
