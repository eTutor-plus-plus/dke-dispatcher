package at.jku.dke.etutor.modules.sql.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;



public class SQLExerciseSettingView /*ks extends DefaultPrintView implements ExerciseSettingView */ {
	/*ks
	@Override
	public Response getResponse(Locale locale, ArrayList<String> cachedResources, Serializable exerciseBean, HashMap<String, Object> sessionAttributes) throws Exception {
		return this.generateResponse(generateXhtml(locale, exerciseBean), cachedResources);
	}

	private String generateXhtml(Locale locale, Serializable exerciseBean) {
		StringBuilder xhtml = new StringBuilder();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("sqlExerciseBean", exerciseBean);
		model.put("messageTitle", getMessageSource().getMessage("sqlexercisesettingview.title", null, locale));
		model.put("messageQuery", getMessageSource().getMessage("sqlexercisesettingview.query", null, locale));
		model.put("messageSubmission", getMessageSource().getMessage("sqlexercisesettingview.submission", null, locale));
		model.put("messageSelectdbschema", getMessageSource().getMessage("sqlexercisesettingview.selectdbschema", null, locale));
		model.put("messageTrial", getMessageSource().getMessage("sqlexercisesettingview.trial", null, locale));
		xhtml.append(VelocityEngineUtils.mergeTemplateIntoString(getVelocityEngine(),
                "resources/sql/exercisesettingview/sqlExerciseSettingView.vm", model));
		return xhtml.toString();
	}
	*/
}
