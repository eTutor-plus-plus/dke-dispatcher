package at.jku.dke.etutor.modules.nf.report;

import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.modules.nf.NFConstants;
import at.jku.dke.etutor.modules.nf.NFHelper;
import at.jku.dke.etutor.modules.nf.analysis.keys.KeysAnalysis;

import java.util.Locale;
import java.util.logging.Level;

public class KeysReporter extends ErrorReporter {

	public static NFReport report(KeysAnalysis analysis, ReporterConfig config, Locale locale){
		NFReport report = new NFReport();
		StringBuilder prologue = new StringBuilder();

		//SET PROLOGUE
        if (analysis.submissionSuitsSolution()) {
            prologue.append(messageSource.getMessage("keysreporter.correctsolution", null, locale));
        } else {
            prologue.append(messageSource.getMessage("keysreporter.notcorrectsolution", null, locale));
        }
        report.setPrologue(prologue.toString());
		
		//SET ERROR REPORT IF NECESSARY
		if ((!analysis.submissionSuitsSolution()) && config.getEvalAction() != NFConstants.EvalAction.CHECK){
			report.addErrorReport(createKeysErrorReport(analysis, config, locale));
		}
		
		//CONFIGURE REPORT
		report.setShowPrologue(true);

		return report;	
	}

	public static ErrorReport createKeysErrorReport(KeysAnalysis analysis, ReporterConfig config, Locale locale){
		ErrorReport report = new ErrorReport();
		StringBuilder description = new StringBuilder();

		int missingKeysCount = analysis.getMissingKeys().size();
		int additionalKeysCount = analysis.getAdditionalKeys().size();

		NFHelper.getLogger().log(Level.INFO, "Reporting for diagnose level: " + config.getDiagnoseLevel());
		
		//SET ERROR
		report.setError(messageSource.getMessage("keysreporter.incorrectkeys", null, locale));

		//SET DESCRIPTION
		if (missingKeysCount > 0){
			if (config.getDiagnoseLevel() == 1){
				description.append(messageSource.getMessage("keysreporter.keymissing", null, locale));
			}

			if (config.getDiagnoseLevel() == 2){
				description.append(missingKeysCount);
				if (missingKeysCount == 1){
					description.append(" ").append(messageSource.getMessage("keysreporter.keyis", null, locale)).append(" ");
  			} else {
					description.append(" ").append(messageSource.getMessage("keysreporter.keysare", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("keysreporter.missing", null, locale)).append(".");
			}
			
			if (config.getDiagnoseLevel() == 3) {
				description.append(generateLevel3Div(analysis.getMissingKeys(), "keysreporter.keyisa", "keysreporter.keysarea", "keysreporter.missing", locale));

				/*currElemID = NFHelper.getNextElementID();
				description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
				description.append("<html>").append(HTML_HEADER).append("<body>");
				description.append("<p>").append(messageSource.getMessage("keysreporter.keysmissing", null, locale)).append(":</p>");

				description.append(generateTable(analysis.getMissingKeys()));

				description.append("</body></html>");
				description.append("\"></input>");

				description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(missingKeysCount);

				if (missingKeysCount == 1){
					description.append(" ").append(messageSource.getMessage("keysreporter.keyisa", null, locale)).append(" ");
				} else {
					description.append(" ").append(messageSource.getMessage("keysreporter.keysarea", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("keysreporter.missing", null, locale)).append(".");*/
			}
		}
		
		if ((missingKeysCount > 0) && (additionalKeysCount > 0)){
			description.append("<br>");
		}
		
		if (additionalKeysCount > 0){
			if (config.getDiagnoseLevel() == 1){
				description.append(messageSource.getMessage("keysreporter.keytoomuch", null, locale));
			}

			if (config.getDiagnoseLevel() == 2){
				description.append(additionalKeysCount);
				if (missingKeysCount == 1){
					description.append(" ").append(messageSource.getMessage("keysreporter.keyis", null, locale)).append(" ");
				} else {
					description.append(" ").append(messageSource.getMessage("keysreporter.keysare", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("keysreporter.toomuch", null, locale)).append(".");
			}
			
			if (config.getDiagnoseLevel() == 3) {
				description.append(generateLevel3Div(analysis.getAdditionalKeys(), "keysreporter.keyisa", "keysreporter.keysarea", "keysreporter.toomuch", locale));

				/*description.append("<div>").append(additionalKeysCount);
				if (additionalKeysCount == 1){
					description.append(" ").append(messageSource.getMessage("keysreporter.keyisa", null, locale)).append(" ");
				} else {
					description.append(" ").append(messageSource.getMessage("keysreporter.keysarea", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("keysreporter.toomuch", null, locale)).append(".");
				description.append("<p/>");*/

				/*currElemID = NFHelper.getNextElementID();*description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
				description.append("<html>").append(HTML_HEADER).append("<body>");*/
				// description.append("<p>").append(messageSource.getMessage("keysreporter.keystoomuch", null, locale)).append(":</p>");

				// description.append(generateTable(analysis.getAdditionalKeys()));

				/*description.append("</body></html>");
				description.append("\"></input>");*/

				// description.append("</div>");

				// description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">");


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
