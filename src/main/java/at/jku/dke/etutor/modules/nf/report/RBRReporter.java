package at.jku.dke.etutor.modules.nf.report;

import etutor.core.evaluation.DefaultGrading;
import etutor.modules.rdbd.RDBDConstants;
import etutor.modules.rdbd.RDBDHelper;
import etutor.modules.rdbd.analysis.RBRAnalysis;
import org.springframework.context.MessageSource;

import java.util.Iterator;
import java.util.Locale;

public class RBRReporter {
	
	public static Report report(RBRAnalysis analysis, DefaultGrading grading, ReporterConfig config, MessageSource messageSource, Locale locale){
		Report report = new Report();
		StringBuffer prologue = new StringBuffer();

		//SET PROLOGUE
		if (config.getAction().equalsIgnoreCase(RDBDConstants.EVAL_ACTION_SUBMIT)) {
			if (analysis.submissionSuitsSolution()) {
				prologue.append(messageSource.getMessage("rbrreporter.correctsolution", null, locale));
			} else {
				prologue.append(messageSource.getMessage("rbrreporter.notcorrectsolution", null, locale));
			}

			prologue.append(messageSource.getMessage("rbrreporter.suggestingpoints", new Object[]{grading.getPoints()}, locale));

			if (grading.getPoints() == 1){
				prologue.append(" " + messageSource.getMessage("rbrreporter.point", null, locale) + " ");
			} else {
				prologue.append(" " + messageSource.getMessage("rbrreporter.points", null, locale) + " ");
			}

			prologue.append(messageSource.getMessage("rbrreporter.yoursubmission", null, locale));
		} else {
			if (analysis.submissionSuitsSolution()) {
				prologue.append(messageSource.getMessage("rbrreporter.correctsolution", null, locale));
			} else {
				prologue.append(messageSource.getMessage("rbrreporter.notcorrectsolution", null, locale));
			}
		}
		report.setPrologue(prologue.toString());
		
		//SET ERROR REPORT IF NECESSARY
		if (!config.getAction().equals(RDBDConstants.EVAL_ACTION_CHECK)){
			if (!analysis.submissionSuitsSolution()){
				report.addErrorReport(createRBRErrorReport(analysis, config, messageSource, locale));
			}
		}		
		
		//CONFIGURE REPORT
		report.setShowPrologue(true);

		return report;	
	}	

	public static ErrorReport createRBRErrorReport(RBRAnalysis analysis, ReporterConfig config, MessageSource messageSource, Locale locale){
		Iterator it;
		String currElemID;
		ErrorReport report = new ErrorReport();
		StringBuffer description = new StringBuffer();
		int missingDependenciesCount = analysis.getMissingFunctionalDependencies().size();
		int additionalDependenciesCount = analysis.getAdditionalFunctionalDependencies().size();
		
		//SET ERROR
		report.setError(messageSource.getMessage("rbrreporter.incorrectdependencies", null, locale));

		//SET HINT
		
		//SET DESCRIPTION
		if (missingDependenciesCount > 0){
			if (config.getDiagnoseLevel() == 1){
				description.append(messageSource.getMessage("rbrreporter.dependencymissing", null, locale));
			}

			if (config.getDiagnoseLevel() == 2){
				description.append(Integer.toString(missingDependenciesCount));
				if (missingDependenciesCount == 1){
					description.append(" " + messageSource.getMessage("rbrreporter.dependencyis", null, locale) + " ");
				} else {
					description.append(" " + messageSource.getMessage("rbrreporter.dependenciesare", null, locale) + " ");
				}
				description.append(messageSource.getMessage("rbrreporter.missing", null, locale) + ".");
			}
			
			if (config.getDiagnoseLevel() == 3){
				currElemID = RDBDHelper.getNextElementID();
				description.append("<input type='hidden' id='" + currElemID + "' value=\"");
				description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
				description.append("<p>" + messageSource.getMessage("rbrreporter.dependenciesmissing", null, locale) + ":</p>");
				description.append("<table border='2' rules='all'>");

				it = analysis.iterMissingFunctionalDependencies();
				while (it.hasNext()){
					description.append("<tr><td>");
					description.append(it.next().toString());
					description.append("</td></tr>");
				}
				description.append("</table>");
				description.append("</body></html>");
				description.append("\"></input>");

				description.append("<a href=\"javascript:openWindow('" + currElemID + "')\">" + missingDependenciesCount);

				if (missingDependenciesCount == 1){
					description.append(" " + messageSource.getMessage("rbrreporter.dependencyisa", null, locale) + " ");
				} else {
					description.append(" " + messageSource.getMessage("rbrreporter.dependenciesarea", null, locale) + " ");
				}
				description.append(messageSource.getMessage("rbrreporter.missing", null, locale) + ".");
			}
		}

		if ((missingDependenciesCount > 0) && (additionalDependenciesCount > 0)){
			description.append("<br>");
		}
		
		if (additionalDependenciesCount > 0){
			if (config.getDiagnoseLevel() == 1){
				description.append(messageSource.getMessage("rbrreporter.dependencytoomuch", null, locale));
			}

			if (config.getDiagnoseLevel() == 2){
				description.append(Integer.toString(additionalDependenciesCount));
				if (additionalDependenciesCount == 1){
					description.append(" " + messageSource.getMessage("rbrreporter.dependencyis", null, locale) + " ");
				} else {
					description.append(" " + messageSource.getMessage("rbrreporter.dependenciesare", null, locale) + " ");
				}
				description.append(messageSource.getMessage("rbrreporter.toomuch", null, locale) + ".");
			}
			
			if (config.getDiagnoseLevel() == 3){
				currElemID = RDBDHelper.getNextElementID();
				description.append("<input type='hidden' id='" + currElemID + "' value=\"");
				description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
				description.append("<p>" + messageSource.getMessage("rbrreporter.dependenciestoomuch", null, locale) + ":</p>");
				description.append("<table border='2' rules='all'>");

				it = analysis.iterAdditionalFunctionalDependencies();
				while (it.hasNext()){
					description.append("<tr><td>");
					description.append(it.next().toString());
					description.append("</td></tr>");
				}
				description.append("</table>");
				description.append("</body></html>");
				description.append("\"></input>");
				description.append("<a href=\"javascript:openWindow('" + currElemID + "')\">" + additionalDependenciesCount);

				if (additionalDependenciesCount == 1){
					description.append(" " + messageSource.getMessage("rbrreporter.dependencyisa", null, locale) + " ");
				} else {
					description.append(" " + messageSource.getMessage("rbrreporter.dependenciesarea", null, locale) + " ");
				}
				description.append(messageSource.getMessage("rbrreporter.toomuch", null, locale) + ".");
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
