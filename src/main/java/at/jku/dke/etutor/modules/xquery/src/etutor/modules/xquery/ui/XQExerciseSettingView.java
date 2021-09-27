package etutor.modules.xquery.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.ui.velocity.VelocityEngineUtils;

import etutor.core.ui.ExerciseSettingView;
import etutor.core.ui.Response;
import etutor.core.ws.ui.DefaultPrintView;

public class XQExerciseSettingView extends DefaultPrintView implements ExerciseSettingView {

	@Override
	public Response getResponse(Locale locale, ArrayList<String> cachedResources, Serializable exerciseBean, HashMap<String, Object> sessionAttributes) throws Exception {
		return this.generateResponse(generateXhtml(locale, exerciseBean, sessionAttributes), cachedResources);
	}

	private String generateXhtml(Locale locale, Serializable exerciseBean, HashMap<String, Object> sessionAttributes) {
		StringBuilder xhtml = new StringBuilder();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("xqExerciseBean", exerciseBean);
		model.put("errors", sessionAttributes.get("errors"));
		model.put("messageTitle", getMessageSource().getMessage("xqexercisesettingview.title", null, locale));
		model.put("messageErrors", getMessageSource().getMessage("xqexercisesettingview.errors", null, locale));
		model.put("messageQuery", getMessageSource().getMessage("xqexercisesettingview.query", null, locale));
		model.put("messageXmldocuments", getMessageSource().getMessage("xqexercisesettingview.xmldocuments", null, locale));
		model.put("messageXmldescription", getMessageSource().getMessage("xqexercisesettingview.xmldescription", null, locale));
		model.put("messageUrl", getMessageSource().getMessage("xqexercisesettingview.url", null, locale));
		model.put("messageHiddenurl", getMessageSource().getMessage("xqexercisesettingview.hiddenurl", null, locale));
		model.put("messageSortednodes", getMessageSource().getMessage("xqexercisesettingview.sortednodes", null, locale));
		model.put("messageNodesdescription", getMessageSource().getMessage("xqexercisesettingview.nodesdescription", null, locale));

		xhtml.append(VelocityEngineUtils.mergeTemplateIntoString(getVelocityEngine(),
                "resources/xq/exercisesettingview/xqExerciseSettingView.vm", model));
		return xhtml.toString();
	}
}
