package at.jku.dke.etutor.modules.nf.report;

import at.jku.dke.etutor.modules.nf.NFConstants;
import at.jku.dke.etutor.modules.nf.analysis.rbr.RBRAnalysis;
import org.springframework.context.MessageSource;

import java.util.Locale;

public class RBRReporter extends ErrorReporter {
	
	public static NFReport report(RBRAnalysis analysis, ReporterConfig config, MessageSource messageSource, Locale locale){
		NFReport report = new NFReport();
		StringBuilder prologue = new StringBuilder();

		//SET PROLOGUE
        if (analysis.submissionSuitsSolution()) {
            prologue.append(messageSource.getMessage("rbrreporter.correctsolution", null, locale));
        } else {
            prologue.append(messageSource.getMessage("rbrreporter.notcorrectsolution", null, locale));
        }
        report.setPrologue(prologue.toString());
		
		//SET ERROR REPORT IF NECESSARY
		if (config.getEvalAction() != NFConstants.EvalAction.CHECK){
			if (!analysis.submissionSuitsSolution()){
				report.addErrorReport(createRBRErrorReport(analysis, config, messageSource, locale));
			}
		}		
		
		//CONFIGURE REPORT
		report.setShowPrologue(true);

		return report;	
	}	

	public static ErrorReport createRBRErrorReport(RBRAnalysis analysis, ReporterConfig config, MessageSource messageSource, Locale locale){
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
			
			if (config.getDiagnoseLevel() == 3) {
				description.append(generateLevel3Div(analysis.getMissingFunctionalDependencies(), "rbrreporter.dependencyisa", "rbrreporter.dependenciesarea", "rbrreporter.missing", locale));

				/*currElemID = NFHelper.getNextElementID();
				description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
				description.append("<html>").append(HTML_HEADER).append("<body>");
				description.append("<p>").append(messageSource.getMessage("rbrreporter.dependenciesmissing", null, locale)).append(":</p>");

				description.append(generateTable(analysis.getMissingFunctionalDependencies()));

				description.append("</body></html>");
				description.append("\"></input>");

				description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(missingDependenciesCount);

				if (missingDependenciesCount == 1){
					description.append(" ").append(messageSource.getMessage("rbrreporter.dependencyisa", null, locale)).append(" ");
				} else {
					description.append(" ").append(messageSource.getMessage("rbrreporter.dependenciesarea", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("rbrreporter.missing", null, locale)).append(".");*/
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
			
			if (config.getDiagnoseLevel() == 3) {
				description.append(generateLevel3Div(analysis.getAdditionalFunctionalDependencies(), "rbrreporter.dependencyisa", "rbrreporter.dependenciesarea", "rbrreporter.toomuch", locale));

				/*currElemID = NFHelper.getNextElementID();
				description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
				description.append("<html>").append(HTML_HEADER).append("<body>");
				description.append("<p>").append(messageSource.getMessage("rbrreporter.dependenciestoomuch", null, locale)).append(":</p>");

				description.append(generateTable(analysis.getAdditionalFunctionalDependencies()));

				description.append("</body></html>");
				description.append("\"></input>");
				description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(additionalDependenciesCount);

				if (additionalDependenciesCount == 1){
					description.append(" ").append(messageSource.getMessage("rbrreporter.dependencyisa", null, locale)).append(" ");
				} else {
					description.append(" ").append(messageSource.getMessage("rbrreporter.dependenciesarea", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("rbrreporter.toomuch", null, locale)).append(".");*/
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
