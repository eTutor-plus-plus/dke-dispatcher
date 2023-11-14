package at.jku.dke.etutor.modules.nf.ui;

import at.jku.dke.etutor.core.evaluation.Report;
import etutor.core.ui.PrintReportView;
import etutor.core.ui.Response;
import etutor.core.ws.ui.DefaultPrintView;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RDBDPrintReportView extends DefaultPrintView implements PrintReportView {
	
	public Response getResponse(Report report, Locale locale, ArrayList<String> cachedResources) throws Exception {
		return generateResponse(generateXhtml(report, locale), cachedResources);
	}
	
	private String generateXhtml(Report report, Locale locale) {
		StringBuilder xhtml = new StringBuilder();
		Map<String, Object> model = new HashMap<>();
		model.put("HTMLPrinter", HTMLPrinter.class);
		model.put("report", report);
		model.put("locale", locale);
		xhtml.append(VelocityEngineUtils.mergeTemplateIntoString(getVelocityEngine(),
                "resources/rdbd/printreportview/rdbdPrintReportView.vm", model));
		return xhtml.toString();
	}
}

//context.put("String", String.class);
//#set( $foo = $String.format('%.1f', $dataFede) )