package at.jku.dke.etutor.modules.nf.report;

import etutor.core.evaluation.DefaultGrading;
import etutor.modules.rdbd.RDBDConstants;
import etutor.modules.rdbd.RDBDHelper;
import etutor.modules.rdbd.analysis.KeysAnalysis;
import org.springframework.context.MessageSource;

import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;

public class KeysReporter {

	public static Report report(KeysAnalysis analysis, DefaultGrading grading, ReporterConfig config, MessageSource messageSource, Locale locale){
		Report report = new Report();
		StringBuffer prologue = new StringBuffer();

		//SET PROLOGUE
		if (config.getAction().equalsIgnoreCase(RDBDConstants.EVAL_ACTION_SUBMIT)) {
			if (analysis.submissionSuitsSolution()) {
				prologue.append(messageSource.getMessage("keysreporter.correctsolution", null, locale));
			} else {
				prologue.append(messageSource.getMessage("keysreporter.notcorrectsolution", null, locale));
			}

			prologue.append(messageSource.getMessage("keysreporter.suggestingpoints", new Object[]{grading.getPoints()}, locale));

			if (grading.getPoints() == 1){
				prologue.append(" " + messageSource.getMessage("keysreporter.point", null, locale) + " ");
			} else {
				prologue.append(" " + messageSource.getMessage("keysreporter.points", null, locale) + " ");
			}

			prologue.append(messageSource.getMessage("keysreporter.yoursubmission", null, locale));
		} else {
			if (analysis.submissionSuitsSolution()) {
				prologue.append(messageSource.getMessage("keysreporter.correctsolution", null, locale));
			} else {
				prologue.append(messageSource.getMessage("keysreporter.notcorrectsolution", null, locale));
			}
		}
		report.setPrologue(prologue.toString());
		
		//SET ERROR REPORT IF NECESSARY
		if ((!analysis.submissionSuitsSolution()) && (!config.getAction().equals(RDBDConstants.EVAL_ACTION_CHECK))){
			report.addErrorReport(createKeysErrorReport(analysis, config, messageSource, locale));
		}
		
		//CONFIGURE REPORT
		report.setShowPrologue(true);

		return report;	
	}

	public static ErrorReport createKeysErrorReport(KeysAnalysis analysis, ReporterConfig config, MessageSource messageSource, Locale locale){
		Iterator it;
		String currElemID;
		ErrorReport report = new ErrorReport();
		StringBuffer prologue = new StringBuffer();
		StringBuffer description = new StringBuffer();

		int missingKeysCount = analysis.getMissingKeys().size();
		int additionalKeysCount = analysis.getAdditionalKeys().size();

		RDBDHelper.getLogger().log(Level.INFO, "Reporting for diagnose level: " + config.getDiagnoseLevel());
		
		//SET ERROR
		report.setError(messageSource.getMessage("keysreporter.incorrectkeys", null, locale));

		//SET DESCRIPTION
		if (missingKeysCount > 0){
			if (config.getDiagnoseLevel() == 1){
				description.append(messageSource.getMessage("keysreporter.keymissing", null, locale));
			}

			if (config.getDiagnoseLevel() == 2){
				description.append(Integer.toString(missingKeysCount));
				if (missingKeysCount == 1){
					description.append(" " + messageSource.getMessage("keysreporter.keyis", null, locale) + " ");
  			} else {
					description.append(" " + messageSource.getMessage("keysreporter.keysare", null, locale) +" ");
				}
				description.append(messageSource.getMessage("keysreporter.missing", null, locale) + ".");
			}
			
			if (config.getDiagnoseLevel() == 3){
				currElemID = RDBDHelper.getNextElementID();
				description.append("<input type='hidden' id='" + currElemID + "' value=\"");
				description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
				description.append("<p>" + messageSource.getMessage("keysreporter.keysmissing", null, locale) + ":</p>");
				description.append("<table border='2' rules='all'>");

				it = analysis.iterMissingKeys();
				while (it.hasNext()){
					description.append("<tr><td>");
					description.append(it.next().toString());
					description.append("</td></tr>");
				}
				description.append("</table>");
				description.append("</body></html>");
				description.append("\"></input>");

				description.append("<a href=\"javascript:openWindow('" + currElemID + "')\">" + missingKeysCount);

				if (missingKeysCount == 1){
					description.append(" " + messageSource.getMessage("keysreporter.keyisa", null, locale) + " ");
				} else {
					description.append(" " + messageSource.getMessage("keysreporter.keysarea", null, locale) + " ");
				}
				description.append(messageSource.getMessage("keysreporter.missing", null, locale) + ".");
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
				description.append(Integer.toString(additionalKeysCount));
				if (missingKeysCount == 1){
					description.append(" " + messageSource.getMessage("keysreporter.keyis", null, locale) + " ");
				} else {
					description.append(" " + messageSource.getMessage("keysreporter.keysare", null, locale) + " ");
				}
				description.append(messageSource.getMessage("keysreporter.toomuch", null, locale) + ".");
			}
			
			if (config.getDiagnoseLevel() == 3){
				currElemID = RDBDHelper.getNextElementID();
				description.append("<input type='hidden' id='" + currElemID + "' value=\"");
				description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
				description.append("<p>" + messageSource.getMessage("keysreporter.keystoomuch", null, locale) + ":</p>");
				description.append("<table border='2' rules='all'>");

				it = analysis.iterAdditionalKeys();
				while (it.hasNext()){
					description.append("<tr><td>");
					description.append(it.next().toString());
					description.append("</td></tr>");
				}
				description.append("</table>");
				description.append("</body></html>");
				description.append("\"></input>");
				description.append("<a href=\"javascript:openWindow('" + currElemID + "')\">" + additionalKeysCount);

				if (additionalKeysCount == 1){
					description.append(" " + messageSource.getMessage("keysreporter.keyisa", null, locale) + " ");
				} else {
					description.append(" " + messageSource.getMessage("keysreporter.keysarea", null, locale) + " ");
				}
				description.append(messageSource.getMessage("keysreporter.toomuch", null, locale) + ".");
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
