package etutor.modules.xquery.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.velocity.app.FieldMethodizer;
import org.springframework.ui.velocity.VelocityEngineUtils;

import etutor.core.ui.Response;
import etutor.core.ui.ShowEditorView;
import etutor.core.ws.ui.DefaultPrintView;
import etutor.modules.sql.SQLConstants;

public class XQShowEditorView extends DefaultPrintView implements ShowEditorView {

	@Override
	public Response getResponse(HashMap<String, Object> passedAttributes, Locale locale, ArrayList<String> cachedResources) throws Exception {
		return this.generateResponse(generateXhtml(passedAttributes, locale), cachedResources);
	}

	private String generateXhtml(HashMap<String, Object> passedAttributes, Locale locale) {
		StringBuilder xhtml = new StringBuilder();
		
		Object submission = passedAttributes.get("submission");
		Set actions = passedAttributes.get(SQLConstants.ATTR_ACTIONS) != null ? (Set)passedAttributes.get(SQLConstants.ATTR_ACTIONS) : null;
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("sqlconstants", new FieldMethodizer( "etutor.modules.sql.SQLConstants" ));
		model.put("XQConstants", new FieldMethodizer( "etutor.modules.xquery.XQConstants" ));
		model.put("submission", submission);
		model.put("actions", actions);
		model.put("messagePrompt", getMessageSource().getMessage("xqshoweditorview.prompt", null, locale));
		model.put("messageButtondiagnose", getMessageSource().getMessage("editorview.buttondiagnose", null, locale));
		model.put("messageButtonsubmit", getMessageSource().getMessage("editorview.buttonsubmit", null, locale));
		xhtml.append(VelocityEngineUtils.mergeTemplateIntoString(getVelocityEngine(),
                "resources/xq/showeditorview/xqShowEditorView.vm", model));

		return xhtml.toString();
	}
}