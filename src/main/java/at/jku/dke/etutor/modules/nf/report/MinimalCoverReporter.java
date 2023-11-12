package at.jku.dke.etutor.modules.nf.report;

import at.jku.dke.etutor.modules.nf.RDBDConstants;
import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.analysis.*;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import etutor.core.evaluation.Grading;
import org.springframework.context.MessageSource;

import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;

public class MinimalCoverReporter {

	public static Report report(MinimalCoverAnalysis analysis, Grading grading, ReporterConfig config, MessageSource messageSource, Locale locale){
		Report report = new Report();
		StringBuffer prologue = new StringBuffer();

		//SET PROLOGUE
		if (analysis.submissionSuitsSolution()) {
			prologue.append(messageSource.getMessage("minimalcoverreporter.correctsolution", null, locale));
		} else {
			prologue.append(messageSource.getMessage("minimalcoverreporter.notcorrectsolution", null, locale));
		}

		if (config.getAction().equalsIgnoreCase("SUBMIT")) {
			prologue.append(messageSource.getMessage("minimalcoverreporter.suggestingpoints", new Object[]{grading.getPoints()}, locale));

			if (grading.getPoints() == 1){
				prologue.append(" " + messageSource.getMessage("minimalcoverreporter.point", null, locale) + " ");
			} else {
				prologue.append(" " + messageSource.getMessage("minimalcoverreporter.points", null, locale) + " ");
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
		Iterator it;
		String currElemID;
		ErrorReport report = new ErrorReport();
		StringBuffer prologue = new StringBuffer();
		StringBuffer description = new StringBuffer();
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
				description.append(" " + messageSource.getMessage("minimalcoverreporter.dependencyis", null, locale) + " ");
			} else {
				description.append(" " + messageSource.getMessage("minimalcoverreporter.dependenciesare", null, locale) + " ");
			}
			description.append(" " + messageSource.getMessage("minimalcoverreporter.notcanonicalrepresentation", null, locale) + ".");
		}
		
		if (config.getDiagnoseLevel() == 3){
			currElemID = RDBDHelper.getNextElementID();
			description.append("<input type='hidden' id='" + currElemID + "' value=\"");
			description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
			description.append("<p>" + messageSource.getMessage("minimalcoverreporter.dependenciesnotcanonical", null, locale) + ":</p>");
			description.append("<table border='2' rules='all'>");

			it = analysis.iterNotCananonicalDependencies();
			while (it.hasNext()){
				description.append("<tr><td>");
				description.append(printDependency((FunctionalDependency)it.next()));
				description.append("</td></tr>");
			}
			description.append("</table>");
			description.append("</body></html>");
			description.append("\"></input>");

			description.append("<a href=\"javascript:openWindow('" + currElemID + "')\">" + count);

			if (count == 1){
				description.append(" " + messageSource.getMessage("minimalcoverreporter.dependencyisa", null, locale) + " ");
			} else {
				description.append(" " + messageSource.getMessage("minimalcoverreporter.dependenciesarea", null, locale) + " ");
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
		Iterator it;
		String currElemID;
		ErrorReport report = new ErrorReport();
		StringBuffer prologue = new StringBuffer();
		StringBuffer description = new StringBuffer();
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
				description.append(" " + messageSource.getMessage("minimalcoverreporter.dependencyis", null, locale) + " ");
			} else {
				description.append(" " + messageSource.getMessage("minimalcoverreporter.dependenciesare", null, locale) + " ");
			}

			description.append(messageSource.getMessage("minimalcoverreporter.trivial", null, locale) + ".");
		}
		
		
		if (config.getDiagnoseLevel() == 3){
			currElemID = RDBDHelper.getNextElementID();
			description.append("<input type='hidden' id='" + currElemID + "' value=\"");
			description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
			description.append("<p>" + messageSource.getMessage("minimalcoverreporter.trivialdependencies", null, locale) + ":</p>");
			description.append("<table border='2' rules='all'>");

			it = analysis.iterTrivialDependencies();
			while (it.hasNext()){
				description.append("<tr><td>");
				description.append(printDependency((FunctionalDependency)it.next()));
				description.append("</td></tr>");
			}
			description.append("</table>");
			description.append("</body></html>");
			description.append("\"></input>");

			description.append("<a href=\"javascript:openWindow('" + currElemID + "')\">" + count);

			if (count == 1){
				description.append(" " + messageSource.getMessage("minimalcoverreporter.dependencyisa", null, locale) + " ");
			} else {
				description.append(" " + messageSource.getMessage("minimalcoverreporter.dependenciesarea", null, locale) + " ");
			}
			description.append(messageSource.getMessage("minimalcoverreporter.trivial", null, locale) + ".");
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
		String currElemID;
		Iterator badDependenciesIterator;
		FunctionalDependency currBadDependency;

		ErrorReport report = new ErrorReport();
		StringBuffer description = new StringBuffer();;
		
		//COUNT BAD DEPENDECIES		
		badDependenciesIterator = analysis.getExtraneousAttributes().keySet().iterator();
		while (badDependenciesIterator.hasNext()){
			count = count + analysis.getExtraneousAttributes((FunctionalDependency)badDependenciesIterator.next()).size();
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
				description.append(" " + messageSource.getMessage("minimalcoverreporter.extraneousattributefound", null, locale));
			} else {
				description.append(" " + messageSource.getMessage("minimalcoverreporter.extraneousattributesfound", null, locale));
			}
		}
		
		if (config.getDiagnoseLevel() == 3){
			currElemID = RDBDHelper.getNextElementID();
			description.append("<input type='hidden' id='" + currElemID + "' value=\"");
			description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
			description.append("<p>Extraneous attributes:</p>");
			description.append("<table border='2' rules='all'>");

			description.append("<thead><tr><th>" + messageSource.getMessage("minimalcoverreporter.functionaldependency", null, locale) + "</th><th>" + messageSource.getMessage("minimalcoverreporter.extraneousattributes", null, locale) + "</th></tr></thead><tbody>");

			boolean first = true;
			Iterator extraneousAttributesIterator;
			badDependenciesIterator = analysis.getExtraneousAttributes().keySet().iterator();
			while (badDependenciesIterator.hasNext()){
				currBadDependency = (FunctionalDependency)badDependenciesIterator.next();

				description.append("<tr><td>");
				description.append(printDependency(currBadDependency));
				description.append("</td><td>");
				
				extraneousAttributesIterator = analysis.getExtraneousAttributes(currBadDependency).iterator();
				while (extraneousAttributesIterator.hasNext()){
					if (first){
						first = false;
					} else {
						description.append(" ");
						description.append(extraneousAttributesIterator.next().toString());
					}
				}
				description.append("</td></tr>");
			}
			description.append("</tbody></table>");
			description.append("</body></html>");
			description.append("\"></input>");

			description.append("<a href=\"javascript:openWindow('" + currElemID + "')\">" + count);

			if (count == 1){
				description.append(" " + messageSource.getMessage("minimalcoverreporter.extraneousattributefounda", null, locale));
			} else {
				description.append(" " + messageSource.getMessage("minimalcoverreporter.extraneousattributesfounda", null, locale));
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
	
	public static ErrorReport createRedundandDependenciesErrorReport(RedundandDependenciesAnalysis analysis, ReporterConfig config, MessageSource messageSource, Locale locale){
		Iterator it;
		String currElemID;
		ErrorReport report = new ErrorReport();
		StringBuffer description = new StringBuffer();
		int count = analysis.getRedundandDependencies().size();;
		
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
				description.append(" " + messageSource.getMessage("minimalcoverreporter.dependencyis", null, locale) + " ");
			} else {
				description.append(" " + messageSource.getMessage("minimalcoverreporter.dependenciesare", null, locale) + " ");
			}
			description.append(messageSource.getMessage("minimalcoverreporter.redundand", null, locale) + ".");
		}
		
		if (config.getDiagnoseLevel() == 3){
			currElemID = RDBDHelper.getNextElementID();
			description.append("<input type='hidden' id='" + currElemID + "' value=\"");
			description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
			description.append("<p>" + messageSource.getMessage("minimalcoverreporter.redundanddependencies", null, locale) + ":</p>");
			description.append("<table border='2' rules='all'>");

			it = analysis.iterRedundandDependencies();
			while (it.hasNext()){
				description.append("<tr><td>");
				description.append(printDependency((FunctionalDependency)it.next()));
				description.append("</td></tr>");
			}
			description.append("</table>");
			description.append("</body></html>");
			description.append("\"></input>");

			description.append("<a href=\"javascript:openWindow('" + currElemID + "')\">" + count);

			if (count == 1){
				description.append(" " + messageSource.getMessage("minimalcoverreporter.dependencyisa", null, locale) + " ");
			} else {
				description.append(" " + messageSource.getMessage("minimalcoverreporter.dependenciesarea", null, locale) + " ");
			}
			description.append(messageSource.getMessage("minimalcoverreporter.redundand", null, locale) + ".");
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
		Iterator it;
		String currElemID;
		ErrorReport report = new ErrorReport();
		StringBuffer description = new StringBuffer();
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
					description.append(" " + messageSource.getMessage("minimalcoverreporter.dependencyis", null, locale) + " ");
				} else {
					description.append(" " + messageSource.getMessage("minimalcoverreporter.dependenciesare", null, locale) + " ");
				}
				description.append(messageSource.getMessage("minimalcoverreporter.missing", null, locale) + ".");
			}

			if ((missingDependenciesCount > 0) && (additionalDependenciesCount > 0)){
				description.append("<br>");
			}

			if (additionalDependenciesCount > 0){
				description.append(additionalDependenciesCount);
				if (additionalDependenciesCount == 1){
					description.append(" " + messageSource.getMessage("minimalcoverreporter.dependencyis", null, locale) + " ");
				} else {
					description.append(" " + messageSource.getMessage("minimalcoverreporter.dependenciesare", null, locale) + " ");
				}
				description.append(messageSource.getMessage("minimalcoverreporter.cannotbederived", null, locale));
			}

		}
		
		if (config.getDiagnoseLevel() == 3){
			if (missingDependenciesCount > 0){
				currElemID = RDBDHelper.getNextElementID();
				description.append("<input type='hidden' id='" + currElemID + "' value=\"");
				description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
				description.append("<p>" + messageSource.getMessage("minimalcoverreporter.missingdependencies", null, locale) + ":</p>");
				description.append("<table border='2' rules='all'>");

				it = analysis.iterMissingDependencies();
				while (it.hasNext()){
					description.append("<tr><td>");
					description.append(printDependency((FunctionalDependency)it.next()));
					description.append("</td></tr>");
				}
				description.append("</table>");
				description.append("</body></html>");
				description.append("\"></input>");

				description.append("<a href=\"javascript:openWindow('" + currElemID + "')\">" + missingDependenciesCount);

				if (missingDependenciesCount == 1){
					description.append(" " + messageSource.getMessage("minimalcoverreporter.dependencyisa", null, locale) + " ");
				} else {
					description.append(" " + messageSource.getMessage("minimalcoverreporter.dependenciesarea", null, locale) + " ");
				}
				description.append(messageSource.getMessage("minimalcoverreporter.missing", null, locale) + ".");
			}
			
			if ((missingDependenciesCount > 0) && (additionalDependenciesCount > 0)){
				description.append("<br>");
			}
			
			if (additionalDependenciesCount > 0){
				currElemID = RDBDHelper.getNextElementID();
				description.append("<input type='hidden' id='" + currElemID + "' value=\"");
				description.append("<html><head><link rel='stylesheet' href='/etutor/css/etutor.css'></link></head><body>");
				description.append("<p>" + messageSource.getMessage("minimalcoverreporter.dependenciesnotderived", null, locale) + ":</p>");
				description.append("<table border='2' rules='all'>");

				it = analysis.iterAdditionalDependencies();
				while (it.hasNext()){
					description.append("<tr><td>");
					description.append(printDependency((FunctionalDependency)it.next()));
					description.append("</td></tr>");
				}
				description.append("</table>");
				description.append("</body></html>");
				description.append("\"></input>");

				description.append("<a href=\"javascript:openWindow('" + currElemID + "')\">" + additionalDependenciesCount);

				if (additionalDependenciesCount == 1){
					description.append(" " + messageSource.getMessage("minimalcoverreporter.dependencyisa", null, locale) + " ");
				} else {
					description.append(" " + messageSource.getMessage("minimalcoverreporter.dependenciesarea", null, locale) + " ");
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
		String s;
		boolean first;
		Iterator attributesIterator;
		
		first = true;
		s = new String();

		attributesIterator = dependency.iterLHSAttributes();
		while (attributesIterator.hasNext()){
			if (first){
				first = false;
			} else {
				s = s.concat(" ");
			}
			s = s.concat(attributesIterator.next().toString());
		}

		s = s.concat(" &rarr; ");

		first = true;
		attributesIterator = dependency.iterRHSAttributes();
		while (attributesIterator.hasNext()){
			if (first){
				first = false;
			} else {
				s = s.concat(" ");
			}
			s = s.concat(attributesIterator.next().toString());
		}
		
		return s;
	}
}
