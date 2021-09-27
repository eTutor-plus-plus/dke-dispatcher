package etutor.modules.xquery.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.ui.velocity.VelocityEngineUtils;

import etutor.core.evaluation.Report;
import etutor.core.ui.PrintReportView;
import etutor.core.ui.Response;
import etutor.core.ws.ui.DefaultPrintView;

public class XQPrintReportView extends DefaultPrintView implements PrintReportView {
	
	public Response getResponse(Report report, Locale locale, ArrayList<String> cachedResources) throws Exception {
		return generateResponse(generateXhtml(report, locale), cachedResources);
	}
	
	private String generateXhtml(Report report, Locale locale) {
		StringBuilder xhtml = new StringBuilder();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("report", report);
		xhtml.append(VelocityEngineUtils.mergeTemplateIntoString(getVelocityEngine(),
                        "resources/xq/printreportview/xqPrintReportView.vm", model));
		return xhtml.toString();
	}
}
