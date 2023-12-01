package at.jku.dke.etutor.modules.nf.report;

import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.modules.nf.RDBDConstants;
import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.analysis.*;
import at.jku.dke.etutor.modules.nf.analysis.minimalcover.DependenciesCoverAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.minimalcover.MinimalCoverAnalysis;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import org.springframework.context.MessageSource;

import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;

public class MinimalCoverReporter {

	public static Report report(MinimalCoverAnalysis analysis, Grading grading, ReporterConfig config, MessageSource messageSource, Locale locale){
		Report report = new Report();
		StringBuilder prologue = new StringBuilder();

		//SET PROLOGUE
		if (analysis.submissionSuitsSolution()) {
			prologue.append(messageSource.getMessage("minimalcoverreporter.correctsolution", null, locale));
		} else {
			prologue.append(messageSource.getMessage("minimalcoverreporter.notcorrectsolution", null, locale));
		}

		if (config.getAction().equalsIgnoreCase("SUBMIT")) {
			prologue.append(messageSource.getMessage("minimalcoverreporter.suggestingpoints", new Object[]{grading.getPoints()}, locale));

			if (grading.getPoints() == 1){
				prologue.append(" ").append(messageSource.getMessage("minimalcoverreporter.point", null, locale)).append(" ");
			} else {
				prologue.append(" ").append(messageSource.getMessage("minimalcoverreporter.points", null, locale)).append(" ");
			}

			prologue.append(messageSource.getMessage("minimalcoverreporter.yoursolution", null, locale));
		}
		report.setPrologue(prologue.toString());

		if (!config.getAction().equals(RDBDConstants.EVAL_ACTION_CHECK)){
			//ADDING ERROR REPORT FOR CANONICAL REPRESENTATION ANALYSIS, IF NECESSARY
			if (analysis.getCanonicalRepresentationAnalysis() != null){
				if (!analysis.getCanonicalRepresentationAnalysis().submissionSuitsSolution()){
					report.addErrorReport(createCanonicalRepresentationErrorReport(analysis.getCanonicalRepresentationAnalysis(), config, messageSource, locale));
					RDBDHelper.getLogger().log(Level.INFO, "Added canonical representation error report");
				}
			}
	
			//ADDING ERROR REPORT FOR TRIVIAL FUNCTIONAL DEPENDENCIES ANALYSIS, IF NECESSARY
			if (analysis.getTrivialDependenciesAnalysis() != null){
				if (!analysis.getTrivialDependenciesAnalysis().submissionSuitsSolution()){
					report.addErrorReport(createTrivialDependenciesErrorReport(analysis.getTrivialDependenciesAnalysis(), config, messageSource, locale));
					RDBDHelper.getLogger().log(Level.INFO, "Added trivial dependencies error report");
				}
			}
			
			//ADDING ERROR REPORT FOR EXTRANEOUS ATTRIBUTE ANALYSIS, IF NECESSARY
			if (analysis.getExtraneousAttributesAnalysis() != null){
				if (!analysis.getExtraneousAttributesAnalysis().submissionSuitsSolution()){
					RDBDHelper.getLogger().log(Level.INFO, "Added extraneous attributes error report");
					report.addErrorReport(createExtraneousAttributesErrorReport(analysis.getExtraneousAttributesAnalysis(), config, messageSource, locale));
				}
			}
			
			//ADDING ERROR REPORT FOR REDUNDAND FUNCTIONAL DEPENDENCIES ANALYSIS, IF NECESSARY
			if (analysis.getRedundandDependenciesAnalysis() != null){
				if (!analysis.getRedundandDependenciesAnalysis().submissionSuitsSolution()){
					report.addErrorReport(createRedundandDependenciesErrorReport(analysis.getRedundandDependenciesAnalysis(), config, messageSource, locale));
					RDBDHelper.getLogger().log(Level.INFO, "Added redundand dependencies error report");
				}
			}
			
			//ADDING ERROR REPORT FOR FUNCTIONAL DEPENDENCIES COVER ANALYSIS, IF NECESSARY
			if (analysis.getDependenciesCoverAnalysis() != null){
				if (!analysis.getDependenciesCoverAnalysis().submissionSuitsSolution()){
					report.addErrorReport(createDependenciesCoverErrorReport(analysis.getDependenciesCoverAnalysis(), config, messageSource, locale));
					RDBDHelper.getLogger().log(Level.INFO, "Added dependencies cover error report");
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
		
		if (config.getDiagnoseLevel() == 3){
			String currElemID = RDBDHelper.getNextElementID();
			description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
			description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
			description.append("<p>").append(messageSource.getMessage("minimalcoverreporter.dependenciesnotcanonical", null, locale)).append(":</p>");
			description.append("<table border='2' rules='all'>");

			for (FunctionalDependency fd : analysis.getNotCanonicalDependencies()){
				description.append("<tr><td>");
				description.append(printDependency(fd));
				description.append("</td></tr>");
			}
			description.append("</table>");
			description.append("</body></html>");
			description.append("\"></input>");

			description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(count);

			if (count == 1){
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependencyisa", null, locale)).append(" ");
			} else {
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependenciesarea", null, locale)).append(" ");
			}
			description.append(messageSource.getMessage("minimalcoverreporter.notcanonicalrepresentation", null, locale));
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
		
		
		if (config.getDiagnoseLevel() == 3){
			String currElemID = RDBDHelper.getNextElementID();
			description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
			description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
			description.append("<p>").append(messageSource.getMessage("minimalcoverreporter.trivialdependencies", null, locale)).append(":</p>");
			description.append("<table border='2' rules='all'>");

			for (FunctionalDependency fd : analysis.getTrivialDependencies()){
				description.append("<tr><td>");
				description.append(printDependency(fd));
				description.append("</td></tr>");
			}
			description.append("</table>");
			description.append("</body></html>");
			description.append("\"></input>");

			description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(count);

			if (count == 1){
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependencyisa", null, locale)).append(" ");
			} else {
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependenciesarea", null, locale)).append(" ");
			}
			description.append(messageSource.getMessage("minimalcoverreporter.trivial", null, locale)).append(".");
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
		int count = 0;

		ErrorReport report = new ErrorReport();
		StringBuilder description = new StringBuilder();

        //COUNT BAD DEPENDECIES
        for (FunctionalDependency functionalDependency : analysis.getExtraneousAttributes().keySet()) {
            count = count + analysis.getExtraneousAttributes(functionalDependency).size();
        }

		//SET ERROR
		report.setError(messageSource.getMessage("minimalcoverreporter.extraneousattribute", null, locale) + ".");
		
		//SET HINT

		//SET DESCRIPTION
		if (config.getDiagnoseLevel() == 1){
			description.append(messageSource.getMessage("minimalcoverreporter.minoneextraneousattribute", null, locale));
		}
		
		if (config.getDiagnoseLevel() == 2){
			description.append(count);
	
			if (count == 1){
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.extraneousattributefound", null, locale));
			} else {
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.extraneousattributesfound", null, locale));
			}
		}
		
		if (config.getDiagnoseLevel() == 3){
			String currElemID = RDBDHelper.getNextElementID();
			description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
			description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
			description.append("<p>Extraneous attributes:</p>");
			description.append("<table border='2' rules='all'>");

			description.append("<thead><tr><th>").append(messageSource.getMessage("minimalcoverreporter.functionaldependency", null, locale)).append("</th><th>").append(messageSource.getMessage("minimalcoverreporter.extraneousattributes", null, locale)).append("</th></tr></thead><tbody>");

			boolean first = true;
			Iterator<String> extraneousAttributesIterator;
            for (FunctionalDependency currBadDependency : analysis.getExtraneousAttributes().keySet()) {
                description.append("<tr><td>");
                description.append(printDependency(currBadDependency));
                description.append("</td><td>");

                extraneousAttributesIterator = analysis.getExtraneousAttributes(currBadDependency).iterator();
                while (extraneousAttributesIterator.hasNext()) {
                    if (first) {
                        first = false;
                    } else {
                        description.append(" ");
                        description.append(extraneousAttributesIterator.next());
                    }
                }
                description.append("</td></tr>");
            }
			description.append("</tbody></table>");
			description.append("</body></html>");
			description.append("\"></input>");

			description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(count);

			if (count == 1){
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.extraneousattributefounda", null, locale));
			} else {
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.extraneousattributesfounda", null, locale));
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
	
	public static ErrorReport createRedundandDependenciesErrorReport(RedundantDependenciesAnalysis analysis, ReporterConfig config, MessageSource messageSource, Locale locale){
		ErrorReport report = new ErrorReport();
		StringBuilder description = new StringBuilder();
		int count = analysis.getRedundantDependencies().size();

        //SET ERROR
		report.setError(messageSource.getMessage("minimalcoverreporter.redundanddependency", null, locale));

		//SET HINT

		//SET DESCRIPTION
		if (config.getDiagnoseLevel() == 1){
			description.append(messageSource.getMessage("minimalcoverreporter.minoneredundant", null, locale));
		}
		
		if (config.getDiagnoseLevel() == 2){
			description.append(count);
			if (count == 1){
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependencyis", null, locale)).append(" ");
			} else {
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependenciesare", null, locale)).append(" ");
			}
			description.append(messageSource.getMessage("minimalcoverreporter.redundand", null, locale)).append(".");
		}
		
		if (config.getDiagnoseLevel() == 3){
			String currElemID = RDBDHelper.getNextElementID();
			description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
			description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
			description.append("<p>").append(messageSource.getMessage("minimalcoverreporter.redundanddependencies", null, locale)).append(":</p>");
			description.append("<table border='2' rules='all'>");

			for (FunctionalDependency fd : analysis.getRedundantDependencies()){
				description.append("<tr><td>");
				description.append(printDependency(fd));
				description.append("</td></tr>");
			}
			description.append("</table>");
			description.append("</body></html>");
			description.append("\"></input>");

			description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(count);

			if (count == 1){
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependencyisa", null, locale)).append(" ");
			} else {
				description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependenciesarea", null, locale)).append(" ");
			}
			description.append(messageSource.getMessage("minimalcoverreporter.redundand", null, locale)).append(".");
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
		Iterator<FunctionalDependency> it;
		String currElemID;
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
			if (missingDependenciesCount > 0){
				currElemID = RDBDHelper.getNextElementID();
				description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
				description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
				description.append("<p>").append(messageSource.getMessage("minimalcoverreporter.missingdependencies", null, locale)).append(":</p>");
				description.append("<table border='2' rules='all'>");

				for (FunctionalDependency fd : analysis.getMissingDependencies()){
					description.append("<tr><td>");
					description.append(printDependency(fd));
					description.append("</td></tr>");
				}
				description.append("</table>");
				description.append("</body></html>");
				description.append("\"></input>");

				description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(missingDependenciesCount);

				if (missingDependenciesCount == 1){
					description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependencyisa", null, locale)).append(" ");
				} else {
					description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependenciesarea", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("minimalcoverreporter.missing", null, locale)).append(".");
			}
			
			if ((missingDependenciesCount > 0) && (additionalDependenciesCount > 0)){
				description.append("<br>");
			}
			
			if (additionalDependenciesCount > 0){
				currElemID = RDBDHelper.getNextElementID();
				description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
				description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
				description.append("<p>").append(messageSource.getMessage("minimalcoverreporter.dependenciesnotderived", null, locale)).append(":</p>");
				description.append("<table border='2' rules='all'>");

				for (FunctionalDependency fd : analysis.getAdditionalDependencies()){
					description.append("<tr><td>");
					description.append(printDependency(fd));
					description.append("</td></tr>");
				}
				description.append("</table>");
				description.append("</body></html>");
				description.append("\"></input>");

				description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(additionalDependenciesCount);

				if (additionalDependenciesCount == 1){
					description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependencyisa", null, locale)).append(" ");
				} else {
					description.append(" ").append(messageSource.getMessage("minimalcoverreporter.dependenciesarea", null, locale)).append(" ");
				}
				description.append(messageSource.getMessage("minimalcoverreporter.determinedcannotbederived", null, locale));
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
		boolean first = true;
		String s = "";

		for (String a : dependency.getLHSAttributes()){
			if (first){
				first = false;
			} else {
				s = s.concat(" ");
			}
			s = s.concat(a);
		}

		s = s.concat(" &rarr; ");

		first = true;
		for (String a : dependency.getRHSAttributes()){
			if (first){
				first = false;
			} else {
				s = s.concat(" ");
			}
			s = s.concat(a);
		}
		
		return s;
	}
}
