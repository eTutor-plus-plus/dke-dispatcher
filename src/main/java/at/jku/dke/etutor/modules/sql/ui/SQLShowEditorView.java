package at.jku.dke.etutor.modules.sql.ui;

public class SQLShowEditorView /*ks extends  DefaultPrintView implements ShowEditorView */ {
	/*ks
	@Override
	public Response getResponse(HashMap<String, Object> passedAttributes, Locale locale, ArrayList<String> cachedResources) throws Exception {
		return this.generateResponse(generateXhtml(passedAttributes, locale), cachedResources);
	}

	private String generateXhtml(HashMap<String, Object> passedAttributes, Locale locale) {
		StringBuilder xhtml = new StringBuilder();
		
		Object query = passedAttributes.get("SQL_QUERY_" + passedAttributes.get(SQLConstants.ATTR_TASK_ID));
		Set actions = passedAttributes.get(SQLConstants.ATTR_ACTIONS) != null ? (Set)passedAttributes.get(SQLConstants.ATTR_ACTIONS) : null;
		
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("sqlconstants", new FieldMethodizer( "at.jku.dke.etutor.modules.sql.SQLConstants" ));
		model.put("query", query);
		model.put("actions", actions);
		model.put("messagePrompt", getMessageSource().getMessage("sqlshoweditorview.prompt", null, locale));
		model.put("messageButtondiagnose", getMessageSource().getMessage("editorview.buttondiagnose", null, locale));
		model.put("messageButtonsubmit", getMessageSource().getMessage("editorview.buttonsubmit", null, locale));
		xhtml.append(VelocityEngineUtils.mergeTemplateIntoString(getVelocityEngine(),
                "resources/sql/showeditorview/sqlShowEditorView.vm", model));

		return xhtml.toString();
	}
	*/
}
