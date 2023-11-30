package at.jku.dke.etutor.modules.nf.report;

import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.modules.nf.RDBDConstants;
import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.analysis.rbr.RBRAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import org.springframework.context.MessageSource;

import java.util.Iterator;
import java.util.Locale;

public class RBRReporter {
	
	public static Report report(RBRAnalysis analysis, DefaultGrading grading, ReporterConfig config, MessageSource messageSource, Locale locale){
		Report report = new Report();
		StringBuilder prologue = new StringBuilder();

		//SET PROLOGUE
		if (config.getAction().equalsIgnoreCase(RDBDConstants.EVAL_ACTION_SUBMIT)) {
			if (analysis.submissionSuitsSolution()) {
				prologue.append(messageSource.getMessage("rbrreporter.correctsolution", null, locale));
			} else {
				prologue.append(messageSource.getMessage("rbrreporter.notcorrectsolution", null, locale));
			}

			prologue.append(messageSource.getMessage("rbrreporter.suggestingpoints", new Object[]{grading.getPoints()}, locale));

			if (grading.getPoints() == 1){
				prologue.append(" ").append(messageSource.getMessage("rbrreporter.point", null, locale)).append(" ");
			} else {
				prologue.append(" ").append(messageSource.getMessage("rbrreporter.points", null, locale)).append(" ");
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
		Iterator<FunctionalDependency> it;
		String currElemID;
		ErrorReport report = new ErrorReport();
		StringBuilder description = new StringBuilder();
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
				description.append(missingDependenciesCount);
				if (missingDependenciesCount == 1){
					description.append(" ").append(messageSource.getMessage("rbrreporter.dependencyis", null, locale)).append(" ");
				} else {
					description.append(" ").append(messageSource.getMessage("rbrreporter.dependenciesare", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("rbrreporter.missing", null, locale)).append(".");
			}
			
			if (config.getDiagnoseLevel() == 3){
				currElemID = RDBDHelper.getNextElementID();
				description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
				description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
				description.append("<p>").append(messageSource.getMessage("rbrreporter.dependenciesmissing", null, locale)).append(":</p>");
				description.append("<table border='2' rules='all'>");

				for (FunctionalDependency fd : analysis.getMissingFunctionalDependencies()){
					description.append("<tr><td>");
					description.append(fd.toString());
					description.append("</td></tr>");
				}
				description.append("</table>");
				description.append("</body></html>");
				description.append("\"></input>");

				description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(missingDependenciesCount);

				if (missingDependenciesCount == 1){
					description.append(" ").append(messageSource.getMessage("rbrreporter.dependencyisa", null, locale)).append(" ");
				} else {
					description.append(" ").append(messageSource.getMessage("rbrreporter.dependenciesarea", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("rbrreporter.missing", null, locale)).append(".");
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
				description.append(additionalDependenciesCount);
				if (additionalDependenciesCount == 1){
					description.append(" ").append(messageSource.getMessage("rbrreporter.dependencyis", null, locale)).append(" ");
				} else {
					description.append(" ").append(messageSource.getMessage("rbrreporter.dependenciesare", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("rbrreporter.toomuch", null, locale)).append(".");
			}
			
			if (config.getDiagnoseLevel() == 3){
				currElemID = RDBDHelper.getNextElementID();
				description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
				description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
				description.append("<p>").append(messageSource.getMessage("rbrreporter.dependenciestoomuch", null, locale)).append(":</p>");
				description.append("<table border='2' rules='all'>");

				for (FunctionalDependency fd : analysis.getAdditionalFunctionalDependencies()){
					description.append("<tr><td>");
					description.append(fd.toString());
					description.append("</td></tr>");
				}
				description.append("</table>");
				description.append("</body></html>");
				description.append("\"></input>");
				description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(additionalDependenciesCount);

				if (additionalDependenciesCount == 1){
					description.append(" ").append(messageSource.getMessage("rbrreporter.dependencyisa", null, locale)).append(" ");
				} else {
					description.append(" ").append(messageSource.getMessage("rbrreporter.dependenciesarea", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("rbrreporter.toomuch", null, locale)).append(".");
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
