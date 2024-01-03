package at.jku.dke.etutor.modules.nf.report;

import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.modules.nf.RDBDConstants;
import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.analysis.closure.AttributeClosureAnalysis;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.logging.Level;

public class AttributeClosureReporter extends ErrorReporter {

	public static NFReport report(AttributeClosureAnalysis analysis, DefaultGrading grading, ReporterConfig config, MessageSource messageSource, Locale locale){
		NFReport report = new NFReport();
		StringBuilder prologue = new StringBuilder();

		//SET PROLOGUE
		if (config.getAction().equalsIgnoreCase(RDBDConstants.EVAL_ACTION_SUBMIT)) {
			if (analysis.submissionSuitsSolution()) {
				prologue.append(messageSource.getMessage("attributeclosurereporter.correctsolution", null, locale));
			} else {
				prologue.append(messageSource.getMessage("attributeclosurereporter.notcorrectsolution", null, locale));
			}

			prologue.append(messageSource.getMessage("attributeclosurereporter.suggestingpoints", new Object[]{grading.getPoints()}, locale));

			if (grading.getPoints() == 1){
				prologue.append(" ").append(messageSource.getMessage("attributeclosurereporter.point", null, locale)).append("</b> ");
			} else {
				prologue.append(" ").append(messageSource.getMessage("attributeclosurereporter.points", null, locale)).append("</b> ");
			}

			prologue.append(messageSource.getMessage("attributeclosurereporter.yoursubmission", null, locale));
		} else {
			if (analysis.submissionSuitsSolution()) {
				prologue.append(messageSource.getMessage("attributeclosurereporter.correctsolution", null, locale));
			} else {
				prologue.append(messageSource.getMessage("attributeclosurereporter.notcorrectsolution", null, locale));
			}
		}
		report.setPrologue(prologue.toString());
		
		//SET ERROR REPORT IF NECESSARY
		if ((!analysis.submissionSuitsSolution()) && (!config.getAction().equals(RDBDConstants.EVAL_ACTION_CHECK))){
			report.addErrorReport(createAttributeClosureErrorReport(analysis, grading, config, messageSource, locale));
		}
		
		//CONFIGURE REPORT
		report.setShowPrologue(true);

		return report;	
	}

	public static ErrorReport createAttributeClosureErrorReport(AttributeClosureAnalysis analysis, DefaultGrading grading, ReporterConfig config, MessageSource messageSource, Locale locale){
		String currElemID;
		ErrorReport report = new ErrorReport();
		StringBuilder description = new StringBuilder();

		int missingAttributesCount = analysis.getMissingAttributes().size();
		int additionalAttributesCount = analysis.getAdditionalAttributes().size();

		RDBDHelper.getLogger().log(Level.INFO, "Reporting for diagnose level: " + config.getDiagnoseLevel());
		
		//SET ERROR
		report.setError(messageSource.getMessage("attributeclosurereporter.incorrectclosure", null, locale));

		//SET DESCRIPTION
		if (missingAttributesCount > 0){
			if (config.getDiagnoseLevel() == 1){
				description.append(messageSource.getMessage("attributeclosurereporter.attributemissing", null, locale));
			}

			if (config.getDiagnoseLevel() == 2){
				description.append(missingAttributesCount);
				if (missingAttributesCount == 1){
					description.append(" ").append(messageSource.getMessage("attributeclosurereporter.attributeis", null, locale)).append(" ");
				} else {
					description.append(" ").append(messageSource.getMessage("attributeclosurereporter.attributeare", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("attributeclosurereporter.missing", null, locale));
			}
			
			if (config.getDiagnoseLevel() == 3){
				currElemID = RDBDHelper.getNextElementID();
				description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
				description.append("<html>").append(HTML_HEADER).append("<body>");
				description.append("<p>").append(messageSource.getMessage("attributeclosurereporter.attributesmissing", null, locale)).append("</b>:</p>");

				description.append(generateTable(analysis.getMissingAttributes()));

				description.append("</body></html>");
				description.append("\"></input>");

				description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(missingAttributesCount);

				if (missingAttributesCount == 1){
					description.append(" ").append(messageSource.getMessage("attributeclosurereporter.attributeisa", null, locale)).append(" ");
				} else {
					description.append(" ").append(messageSource.getMessage("attributeclosurereporter.attributesarea", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("attributeclosurereporter.missing", null, locale)).append("</b>.");
			}
		}
		
		if ((missingAttributesCount > 0) && (additionalAttributesCount > 0)){
			description.append("<br>");
		}
		
		if (additionalAttributesCount > 0){
			if (config.getDiagnoseLevel() == 1){
				description.append(messageSource.getMessage("attributeclosurereporter.attributetoomuch", null, locale));
			}

			if (config.getDiagnoseLevel() == 2){
				description.append(additionalAttributesCount);
				if (missingAttributesCount == 1){
					description.append(" ").append(messageSource.getMessage("attributeclosurereporter.attributeis", null, locale)).append(" ");
				} else {
					description.append(" ").append(messageSource.getMessage("attributeclosurereporter.attributesare", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("attributeclosurereporter.toomuch", null, locale));
			}
			
			if (config.getDiagnoseLevel() == 3){
				currElemID = RDBDHelper.getNextElementID();
				description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
				description.append("<html>").append(HTML_HEADER).append("<body>");
				description.append("<p>").append(messageSource.getMessage("attributeclosurereporter.attributestoomuch", null, locale)).append("</b>:</p>");

				description.append(generateTable(analysis.getAdditionalAttributes()));

				description.append("</body></html>");
				description.append("\"></input>");
				description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(additionalAttributesCount);

				if (additionalAttributesCount == 1){
					description.append(" ").append(messageSource.getMessage("attributeclosurereporter.attributeisa", null, locale)).append(" ");
				} else {
					description.append(" ").append(messageSource.getMessage("attributeclosurereporter.attributesarea", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("attributeclosurereporter.toomuch", null, locale)).append(".");
			}
		}
		report.setDescription(description.toString());
		
		//CONFIGURE REPORT
		report.setShowHint(false);
		report.setShowError(false);
		report.setShowErrorDescription(false);
		
		if (config.getDiagnoseLevel() >= 0){
			report.setShowError(true);
		}
		if (config.getDiagnoseLevel() >= 1) {
			report.setShowErrorDescription(true);
		}
		if (config.getDiagnoseLevel() >= 2){
			//report.setShowHint(true); SOLLTE ES EIGENTLICH SEIN. ES WIRD ABER NOCH KEIN HINT SPEZIFIZIERT MOMENTAN
			report.setShowHint(false);
		}
				
		return report;
	}
}
