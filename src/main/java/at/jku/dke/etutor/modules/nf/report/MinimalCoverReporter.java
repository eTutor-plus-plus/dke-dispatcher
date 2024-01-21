package at.jku.dke.etutor.modules.nf.report;

import at.jku.dke.etutor.modules.nf.NFConstants;
import at.jku.dke.etutor.modules.nf.NFHelper;
import at.jku.dke.etutor.modules.nf.analysis.minimalcover.CanonicalRepresentationAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.minimalcover.DependenciesCoverAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.minimalcover.ExtraneousAttributesAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.minimalcover.MinimalCoverAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.minimalcover.RedundantDependenciesAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.minimalcover.TrivialDependenciesAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import org.springframework.context.MessageSource;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;
import java.util.logging.Level;

public class MinimalCoverReporter extends ErrorReporter {

	private MinimalCoverReporter() {
		// This class is not meant to be instantiated
	}

	public static NFReport report(MinimalCoverAnalysis analysis, ReporterConfig config, Locale locale){
		NFReport report = new NFReport();
		StringBuilder prologue = new StringBuilder();

		//SET PROLOGUE
		if (analysis.submissionSuitsSolution()) {
			prologue.append(messageSource.getMessage("minimalcoverreporter.correctsolution", null, locale));
		} else {
			prologue.append(messageSource.getMessage("minimalcoverreporter.notcorrectsolution", null, locale));
		}
		report.setPrologue(prologue.toString());

		if (config.getEvalAction() != NFConstants.EvalAction.CHECK) {
			//ADDING ERROR REPORT FOR CANONICAL REPRESENTATION ANALYSIS, IF NECESSARY
			if (analysis.getCanonicalRepresentationAnalysis() != null) {
				if (!analysis.getCanonicalRepresentationAnalysis().submissionSuitsSolution()) {
					report.addErrorReport(createCanonicalRepresentationErrorReport(analysis.getCanonicalRepresentationAnalysis(), config, messageSource, locale));
					NFHelper.getLogger().log(Level.INFO, "Added canonical representation error report");
				}
			}
	
			//ADDING ERROR REPORT FOR TRIVIAL FUNCTIONAL DEPENDENCIES ANALYSIS, IF NECESSARY
			if (analysis.getTrivialDependenciesAnalysis() != null) {
				if (!analysis.getTrivialDependenciesAnalysis().submissionSuitsSolution()) {
					report.addErrorReport(createTrivialDependenciesErrorReport(analysis.getTrivialDependenciesAnalysis(), config, messageSource, locale));
					NFHelper.getLogger().log(Level.INFO, "Added trivial dependencies error report");
				}
			}
			
			//ADDING ERROR REPORT FOR EXTRANEOUS ATTRIBUTE ANALYSIS, IF NECESSARY
			if (analysis.getExtraneousAttributesAnalysis() != null) {
				if (!analysis.getExtraneousAttributesAnalysis().submissionSuitsSolution()) {
					NFHelper.getLogger().log(Level.INFO, "Added extraneous attributes error report");
					report.addErrorReport(createExtraneousAttributesErrorReport(analysis.getExtraneousAttributesAnalysis(), config, messageSource, locale));
				}
			}
			
			//ADDING ERROR REPORT FOR REDUNDANT FUNCTIONAL DEPENDENCIES ANALYSIS, IF NECESSARY
			if (analysis.getRedundantDependenciesAnalysis() != null) {
				if (!analysis.getRedundantDependenciesAnalysis().submissionSuitsSolution()) {
					report.addErrorReport(createRedundantDependenciesErrorReport(analysis.getRedundantDependenciesAnalysis(), config, messageSource, locale));
					NFHelper.getLogger().log(Level.INFO, "Added redundant dependencies error report");
				}
			}
			
			//ADDING ERROR REPORT FOR FUNCTIONAL DEPENDENCIES COVER ANALYSIS, IF NECESSARY
			if (analysis.getDependenciesCoverAnalysis() != null) {
				if (!analysis.getDependenciesCoverAnalysis().submissionSuitsSolution()) {
					report.addErrorReport(createDependenciesCoverErrorReport(analysis.getDependenciesCoverAnalysis(), config, messageSource, locale));
					NFHelper.getLogger().log(Level.INFO, "Added dependencies cover error report");
				}
			}
		}
		
		//CONFIGURE REPORT
		report.setShowPrologue(true);
		
		return report;
	}
	
	public static ErrorReport createCanonicalRepresentationErrorReport(CanonicalRepresentationAnalysis analysis, ReporterConfig config, MessageSource messageSource, Locale locale){
		ErrorReport report = new ErrorReport();
		StringBuilder description = new StringBuilder();
		int count = analysis.getNotCanonicalDependencies().size();
		
		//SET ERROR
		report.setError(messageSource.getMessage("minimalcoverreporter.incorrectcanonicalrepresentation", null, locale));

		//SET HINT

		//SET DESCRIPTION		
		if (config.getDiagnoseLevel() == 1){
			description.append(messageSource.getMessage("minimalcoverreporter.notcanonical", null, locale));
		}
		
		if (config.getDiagnoseLevel() == 2){
			description.append(count);
			if (count == 1){
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependencyis", null, locale)).append(" ");
			} else {
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependenciesare", null, locale)).append(" ");
			}
			description.append(" ").append(messageSource.getMessage("minimalcoverreporter.notcanonicalrepresentation", null, locale)).append(".");
		}
		
		if (config.getDiagnoseLevel() == 3) {
			description.append(generateLevel3Div(analysis.getNotCanonicalDependencies(), "minimalcoverreporter.dependencyisa", "minimalcoverreporter.dependenciesarea", "minimalcoverreporter.notcanonicalrepresentation", locale));

			/*String currElemID = NFHelper.getNextElementID();
			description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
			description.append("<html>").append(HTML_HEADER).append("<body>");
			description.append("<p>").append(messageSource.getMessage("minimalcoverreporter.dependenciesnotcanonical", null, locale)).append(":</p>");

			description.append(generateTable(analysis.getNotCanonicalDependencies()));

			description.append("</body></html>");
			description.append("\"></input>");

			description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(count);

			if (count == 1){
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependencyisa", null, locale)).append(" ");
			} else {
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependenciesarea", null, locale)).append(" ");
			}
			description.append(messageSource.getMessage("minimalcoverreporter.notcanonicalrepresentation", null, locale));*/
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

	public static ErrorReport createTrivialDependenciesErrorReport(TrivialDependenciesAnalysis analysis, ReporterConfig config, MessageSource messageSource, Locale locale){
		ErrorReport report = new ErrorReport();
		StringBuilder description = new StringBuilder();
		int count = analysis.getTrivialDependencies().size();
		
		//SET ERROR
		report.setError(messageSource.getMessage("minimalcoverreporter.trivialdependency", null, locale));

		//SET HINT

		//SET DESCRIPTION
		if (config.getDiagnoseLevel() == 1){
			description.append(messageSource.getMessage("minimalcoverreporter.minonetrivial", null, locale));
		}
		
		if (config.getDiagnoseLevel() == 2){
			description.append(count);
	
			if (count == 1){
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependencyis", null, locale)).append(" ");
			} else {
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependenciesare", null, locale)).append(" ");
			}

			description.append(messageSource.getMessage("minimalcoverreporter.trivial", null, locale)).append(".");
		}
		
		
		if (config.getDiagnoseLevel() == 3) {
			description.append(generateLevel3Div(analysis.getTrivialDependencies(), "minimalcoverreporter.dependencyisa", "minimalcoverreporter.dependenciesarea", "minimalcoverreporter.trivial", locale));

			/*String currElemID = NFHelper.getNextElementID();
			description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
			description.append("<html>").append(HTML_HEADER).append("<body>");
			description.append("<p>").append(messageSource.getMessage("minimalcoverreporter.trivialdependencies", null, locale)).append(":</p>");

			description.append(generateTable(analysis.getTrivialDependencies()));

			description.append("</body></html>");
			description.append("\"></input>");

			description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(count);

			if (count == 1){
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependencyisa", null, locale)).append(" ");
			} else {
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependenciesarea", null, locale)).append(" ");
			}
			description.append(messageSource.getMessage("minimalcoverreporter.trivial", null, locale)).append(".");*/
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
	
	public static ErrorReport createExtraneousAttributesErrorReport(ExtraneousAttributesAnalysis analysis, ReporterConfig config, MessageSource messageSource, Locale locale){
		ErrorReport report = new ErrorReport();
		StringBuilder description = new StringBuilder();

        //COUNT BAD DEPENDENCIES
		int count = 0;
        for (List<String> extraneousAttributes : analysis.getExtraneousAttributes().values()) {
            count += extraneousAttributes.size();
        }

		//SET ERROR
		report.setError(messageSource.getMessage("minimalcoverreporter.extraneousattribute", null, locale) + ".");
		
		//SET HINT

		//SET DESCRIPTION
		if (config.getDiagnoseLevel() == 1) {
			description.append(messageSource.getMessage("minimalcoverreporter.minoneextraneousattribute", null, locale));
		}
		
		if (config.getDiagnoseLevel() == 2) {
			description.append(count);
	
			if (count == 1){
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.extraneousattributefound", null, locale));
			} else {
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.extraneousattributesfound", null, locale));
			}
		}
		
		if (config.getDiagnoseLevel() == 3) {
			description.append("<div>").append(count);
			if (count == 1){
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.extraneousattributefounda", null, locale)).append(" ");
			} else {
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.extraneousattributesfounda", null, locale)).append(" ");
			}
			description.append(messageSource.getMessage("minimalcoverreporter.extraneousattributes", null, locale)).append(".");
			description.append("<p/>");

			description.append(TABLE_HEADER);

			description.append("<thead><tr><th>").append(messageSource.getMessage("minimalcoverreporter.functionaldependency", null, locale)).append("</th><th>").append(messageSource.getMessage("minimalcoverreporter.extraneousattributes", null, locale)).append("</th></tr></thead><tbody>");

			for (Map.Entry<FunctionalDependency, List<String>> currEntry : analysis.getExtraneousAttributes().entrySet()) {
				description.append("<tr><td>");
				description.append(printDependency(currEntry.getKey()));
				description.append("</td><td>");

				StringJoiner attributesJoiner = new StringJoiner(" ");
				for (String extraneousAttribute : currEntry.getValue()) {
					attributesJoiner.add(extraneousAttribute);
				}
				description.append(attributesJoiner);

				description.append("</td></tr>");
			}
			description.append("</tbody></table>");

			description.append("</div>");

			/*String currElemID = NFHelper.getNextElementID();
			description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
			description.append("<html>").append(HTML_HEADER).append("<body>");

			// table generation

			description.append("</body></html>");
			description.append("\"></input>");

			description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(count);*/
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
	
	public static ErrorReport createRedundantDependenciesErrorReport(RedundantDependenciesAnalysis analysis, ReporterConfig config, MessageSource messageSource, Locale locale){
		ErrorReport report = new ErrorReport();
		StringBuilder description = new StringBuilder();
		int count = analysis.getRedundantDependencies().size();

        //SET ERROR
		report.setError(messageSource.getMessage("minimalcoverreporter.redundanddependency", null, locale));

		//SET HINT

		//SET DESCRIPTION
		if (config.getDiagnoseLevel() == 1) {
			description.append(messageSource.getMessage("minimalcoverreporter.minoneredundant", null, locale));
		}
		
		if (config.getDiagnoseLevel() == 2) {
			description.append(count);
			if (count == 1) {
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependencyis", null, locale)).append(" ");
			} else {
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependenciesare", null, locale)).append(" ");
			}
			description.append(messageSource.getMessage("minimalcoverreporter.redundand", null, locale)).append(".");
		}
		
		if (config.getDiagnoseLevel() == 3) {
			description.append(generateLevel3Div(analysis.getRedundantDependencies(), "minimalcoverreporter.dependencyisa", "minimalcoverreporter.dependenciesarea", "minimalcoverreporter.redundand", locale));

			/*String currElemID = NFHelper.getNextElementID();
			description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
			description.append("<html>").append(HTML_HEADER).append("<body>");
			description.append("<p>").append(messageSource.getMessage("minimalcoverreporter.redundanddependencies", null, locale)).append(":</p>");

			description.append(generateTable(analysis.getRedundantDependencies()));

			description.append("</body></html>");
			description.append("\"></input>");

			description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(count);

			if (count == 1){
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependencyisa", null, locale)).append(" ");
			} else {
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependenciesarea", null, locale)).append(" ");
			}
			description.append(messageSource.getMessage("minimalcoverreporter.redundand", null, locale)).append(".");*/
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
	
	public static ErrorReport createDependenciesCoverErrorReport(DependenciesCoverAnalysis analysis, ReporterConfig config, MessageSource messageSource, Locale locale){
		ErrorReport report = new ErrorReport();
		StringBuilder description = new StringBuilder();
		int missingDependenciesCount = analysis.getMissingDependencies().size();
		int additionalDependenciesCount = analysis.getAdditionalDependencies().size();
		
		//SET ERROR
		report.setError(messageSource.getMessage("minimalcoverreporter.incorrectnumberdependencies", null, locale));

		//SET HINT

		//SET DESCRIPTION
		if (config.getDiagnoseLevel() == 1){
			if (missingDependenciesCount > 0){
				description.append(messageSource.getMessage("minimalcoverreporter.minonedependencymissing", null, locale));
			}
			if ((missingDependenciesCount > 0) && (additionalDependenciesCount > 0)){
				description.append("<br>");
			}
			if (additionalDependenciesCount > 0){
				description.append(messageSource.getMessage("minimalcoverreporter.dependencynotderived", null, locale));
			}
		}
		
		if (config.getDiagnoseLevel() == 2){
			if (missingDependenciesCount > 0){
				description.append(missingDependenciesCount);
				if (missingDependenciesCount == 1){
					description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependencyis", null, locale)).append(" ");
				} else {
					description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependenciesare", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("minimalcoverreporter.missing", null, locale)).append(".");
			}

			if ((missingDependenciesCount > 0) && (additionalDependenciesCount > 0)){
				description.append("<br>");
			}

			if (additionalDependenciesCount > 0){
				description.append(additionalDependenciesCount);
				if (additionalDependenciesCount == 1){
					description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependencyis", null, locale)).append(" ");
				} else {
					description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependenciesare", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("minimalcoverreporter.cannotbederived", null, locale));
			}

		}
		
		if (config.getDiagnoseLevel() == 3){
			if (missingDependenciesCount > 0) {
				description.append(generateLevel3Div(analysis.getMissingDependencies(), "minimalcoverreporter.dependencyisa", "minimalcoverreporter.dependenciesarea", "minimalcoverreporter.missing", locale));

				/*currElemID = NFHelper.getNextElementID();
				description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
				description.append("<html>").append(HTML_HEADER).append("<body>");
				description.append("<p>").append(messageSource.getMessage("minimalcoverreporter.missingdependencies", null, locale)).append(":</p>");

				description.append(generateTable(analysis.getMissingDependencies()));

				description.append("</body></html>");
				description.append("\"></input>");

				description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(missingDependenciesCount);

				if (missingDependenciesCount == 1){
					description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependencyisa", null, locale)).append(" ");
				} else {
					description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependenciesarea", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("minimalcoverreporter.missing", null, locale)).append(".");*/
			}
			
			if ((missingDependenciesCount > 0) && (additionalDependenciesCount > 0)){
				description.append("<br>");
			}
			
			if (additionalDependenciesCount > 0) {
				description.append(generateLevel3Div(analysis.getAdditionalDependencies(), "minimalcoverreporter.dependencyisa", "minimalcoverreporter.dependenciesarea", "minimalcoverreporter.determinedcannotbederived", locale));

				/*currElemID = NFHelper.getNextElementID();
				description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
				description.append("<html>").append(HTML_HEADER).append("<body>");
				description.append("<p>").append(messageSource.getMessage("minimalcoverreporter.dependenciesnotderived", null, locale)).append(":</p>");

				description.append(generateTable(analysis.getAdditionalDependencies()));

				description.append("</body></html>");
				description.append("\"></input>");

				description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(additionalDependenciesCount);

				if (additionalDependenciesCount == 1){
					description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependencyisa", null, locale)).append(" ");
				} else {
					description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependenciesarea", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("minimalcoverreporter.determinedcannotbederived", null, locale));*/
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
	
	public static String printDependency(FunctionalDependency dependency){
		StringJoiner lhsJoiner = new StringJoiner(" ");
		for (String a : dependency.getLhsAttributes()){
			lhsJoiner.add(a);
		}

		StringJoiner rhsJoiner = new StringJoiner(" ");
		for (String a : dependency.getRhsAttributes()){
			rhsJoiner.add(a);
		}

		return lhsJoiner + " &rarr; " + rhsJoiner;
	}
}
