package at.jku.dke.etutor.modules.sql.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;



public class SQLPrintReportView /*ks extends DefaultPrintView implements PrintReportView */ {
	/*ks
	public Response getResponse(Report report, Locale locale, ArrayList<String> cachedResources) throws Exception {
		return generateResponse(generateXhtml(report, locale), cachedResources);
	}
	
	private String generateXhtml(Report report, Locale locale) {
		StringBuilder xhtml = new StringBuilder();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("report", report);
		model.put("messageResult", getMessageSource().getMessage("sqlprintreportview.result", null, locale));
		model.put("messageErrorreport", getMessageSource().getMessage("sqlprintreportview.errorreport", null, locale));
		model.put("messageError", getMessageSource().getMessage("sqlprintreportview.error", null, locale));
		model.put("messageDescription", getMessageSource().getMessage("sqlprintreportview.description", null, locale));
		model.put("messageHint", getMessageSource().getMessage("sqlprintreportview.hint", null, locale));
		model.put("messageQueryresult", getMessageSource().getMessage("sqlprintreportview.queryresult", null, locale));
		xhtml.append(VelocityEngineUtils.mergeTemplateIntoString(getVelocityEngine(),
                        "resources/sql/printreportview/sqlPrintReportView.vm", model));
		return xhtml.toString();
	}

	 */
}
