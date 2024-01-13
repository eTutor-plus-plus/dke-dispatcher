package at.jku.dke.etutor.modules.nf.report;

import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.modules.nf.NFConstants;
import at.jku.dke.etutor.modules.nf.NFHelper;
import at.jku.dke.etutor.modules.nf.analysis.normalization.DecompositionAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.normalization.DependenciesPreservationAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.normalization.LosslessAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.normalization.NormalizationAnalysis;
import at.jku.dke.etutor.modules.nf.model.IdentifiedRelation;
import org.springframework.context.MessageSource;

import java.util.Locale;
import java.util.logging.Level;

public class NormalizationReporter extends ErrorReporter {

	public static NFReport report(NormalizationAnalysis analysis, DefaultGrading grading, NormalizationReporterConfig config, Locale locale){
		NFReport report = new NFReport();
		StringBuilder prologue = new StringBuilder();
		
		//SET PROLOGUE
		if (config.getEvalAction() == NFConstants.EvalAction.SUBMIT){
			if (analysis.submissionSuitsSolution()) {
				prologue.append(messageSource.getMessage("normalizationreporter.correctsolution", null, locale));
			} else {
				prologue.append(messageSource.getMessage("normalizationreporter.notcorrectsolution", null, locale));
			}
			prologue.append(messageSource.getMessage("normalizationreporter.suggestingpoints", new Object[]{grading.getPoints()}, locale));
			if (grading != null){
				if (grading.getPoints() == 1){
					prologue.append(" ").append(messageSource.getMessage("normalizationreporter.point", null, locale)).append(" ");
				} else {
					prologue.append(" ").append(messageSource.getMessage("normalizationreporter.points", null, locale)).append(" ");
				}
				prologue.append(messageSource.getMessage("normalizationreporter.yoursolution", null, locale));
			}
		} else {
			if (analysis.submissionSuitsSolution()) {
				prologue.append(messageSource.getMessage("normalizationreporter.correct", null, locale));
			} else {
				prologue.append(messageSource.getMessage("normalizationreporter.notcorrect", null, locale));
			}
		}
		report.setPrologue(prologue.toString());
		
		//SET SUB REPORTS

		if (config.getEvalAction() != NFConstants.EvalAction.CHECK){
			//REPORT DECOMPOSITION_ANALYSIS
			if ((analysis.getDecompositionAnalysis() != null) && (!analysis.getDecompositionAnalysis().submissionSuitsSolution())){
				report.addErrorReport(createDecompositionErrorReport(analysis.getDecompositionAnalysis(), config, messageSource, locale));		
			}
	
			//REPORT LOSS_LESS_ANALYSIS
			if ((analysis.getLossLessAnalysis() != null) && (!analysis.getLossLessAnalysis().submissionSuitsSolution())){
				report.addErrorReport(createLosslessErrorReport(analysis.getLossLessAnalysis(), config, messageSource, locale));
			}
	
			//REPORT DEPENDENCIES_PRESERVATION_ANALYSIS
			if ((analysis.getDepPresAnalysis() != null) && (!analysis.getDepPresAnalysis().submissionSuitsSolution())){
				ErrorReport reportAtom = createDependenciesPreservationErrorReport(analysis.getDepPresAnalysis(), config, messageSource, locale);
				NFHelper.getLogger().log(Level.INFO, "Max Lost Dependencies: " + analysis.getMaxLostDependencies() +  " - Lost Dependencies: " + analysis.getDepPresAnalysis().lostFunctionalDependenciesCount());
	
				if (analysis.getMaxLostDependencies() < analysis.getDepPresAnalysis().lostFunctionalDependenciesCount()){
					reportAtom.setError(messageSource.getMessage("normalizationreporter.toomanylost", new Object[]{analysis.getMaxLostDependencies(), analysis.getDepPresAnalysis().lostFunctionalDependenciesCount()}, locale));
				} else {
					reportAtom.setReportAtomType(ReportAtomType.INFO);
				}
				report.addErrorReport(reportAtom);
			}
	
			//ADD RELATION SPECIFIC ERROR REPORT GROUPS
			for (IdentifiedRelation ir : config.getDecomposedRelations()){
				ErrorReportGroup reportGroup = createRelationSpecificErrorReportsGroup(ir.getID(), analysis, config, grading, messageSource, locale);
				if (!reportGroup.getErrorReports().isEmpty()){
					report.addErrorReportGroup(reportGroup);
				}
			}
		}

		//CONFIGURE REPORT
		
		return report;
	}
	
	public static ErrorReportGroup createRelationSpecificErrorReportsGroup(String relationID, NormalizationAnalysis analysis, NormalizationReporterConfig config, DefaultGrading grading, MessageSource messageSource, Locale locale){
		ErrorReportGroup group = new ErrorReportGroup();
		NormalformReporterConfig nfReporterConfig;

		//SET HEADER
		group.setHeader(messageSource.getMessage("normalizationreporter.relationreports", new Object[]{relationID}, locale));
		
		//ADD TRIVIAL_DEPENDENCIES REPORT_ATOM
		if (analysis.getTrivialDependenciesAnalysis(relationID) != null){
			if (!analysis.getTrivialDependenciesAnalysis(relationID).submissionSuitsSolution()){
				group.addErrorReport(MinimalCoverReporter.createTrivialDependenciesErrorReport(analysis.getTrivialDependenciesAnalysis(relationID), config, messageSource, locale));
			}
		}
		
		//ADD EXTRANEOUS_ATTRIBUTES REPORT_ATOM
		if (analysis.getExtraneousAttributesAnalysis(relationID) != null){
			if (!analysis.getExtraneousAttributesAnalysis(relationID).submissionSuitsSolution()){
				group.addErrorReport(MinimalCoverReporter.createExtraneousAttributesErrorReport(analysis.getExtraneousAttributesAnalysis(relationID), config, messageSource, locale));
			}
		}

		//ADD REDUNDANT_DEPENDENCIES REPORT_ATOM
		if (analysis.getRedundantDependenciesAnalysis(relationID) != null){
			if (!analysis.getRedundantDependenciesAnalysis(relationID).submissionSuitsSolution()){
				group.addErrorReport(MinimalCoverReporter.createRedundantDependenciesErrorReport(analysis.getRedundantDependenciesAnalysis(relationID), config, messageSource, locale));
			}
		}

		//ADD CANONICAL_REPRESENTATION REPORT_ATOM
		if (analysis.getCanonicalRepresentationAnalysis(relationID) != null){
			if (!analysis.getCanonicalRepresentationAnalysis(relationID).submissionSuitsSolution()){
				group.addErrorReport(MinimalCoverReporter.createCanonicalRepresentationErrorReport(analysis.getCanonicalRepresentationAnalysis(relationID), config, messageSource, locale));
			}
		}

		//ADD KEYS_ANALYSIS REPORT_ATOM		
		if (analysis.getKeysAnalysis(relationID) != null){
			if (!analysis.getKeysAnalysis(relationID).submissionSuitsSolution()){
				group.addErrorReport(KeysReporter.createKeysErrorReport(analysis.getKeysAnalysis(relationID), config, locale));
			}
		}

		//ADD NORMALFORM_ANALYSIS REPORT_ATOM
		if (analysis.getNormalformAnalysis(relationID) != null){
			if (!analysis.getNormalformAnalysis(relationID).submissionSuitsSolution()){
				nfReporterConfig = new NormalformReporterConfig();
				nfReporterConfig.setEvalAction(config.getEvalAction());
				nfReporterConfig.setDiagnoseLevel(config.getDiagnoseLevel());
				nfReporterConfig.setDesiredNormalformLevel(config.getDesiredNormalformLevel());

				group.addErrorReport(NormalformReporter.createNormalformErrorReport(analysis.getNormalformAnalysis(relationID), nfReporterConfig, locale));
			}
		}

		//ADD RBR_ANALYSIS REPORT_ATOM
		if (analysis.getRBRAnalysis(relationID) != null){
			if (!analysis.getRBRAnalysis(relationID).submissionSuitsSolution()){
				group.addErrorReport(RBRReporter.createRBRErrorReport(analysis.getRBRAnalysis(relationID), config, messageSource, locale));
			}
		}

		return group;
	}

	public static ErrorReport createDependenciesPreservationErrorReport(DependenciesPreservationAnalysis analysis, ReporterConfig config, MessageSource messageSource, Locale locale){
		ErrorReport report = new ErrorReport();
		StringBuilder description = new StringBuilder();
		int numberOfLostDependencies = analysis.getLostFunctionalDependencies().size();		

		//SET ERROR
		report.setError(messageSource.getMessage("normalizationreporter.notdependenciespreserving", null, locale));

		//SET HINT
		
		//SET DESCRIPTION
		if (config.getDiagnoseLevel() == 0){
			description.append(messageSource.getMessage("normalizationreporter.minonerelationlost", null, locale));				
		}
		
		if (config.getDiagnoseLevel() == 1){
			description.append(numberOfLostDependencies);
			if (numberOfLostDependencies == 1){
				description.append(" ").append(messageSource.getMessage("normalizationreporter.dependencybaserelation", null, locale)).append(" ");
			} else {
				description.append(" ").append(messageSource.getMessage("normalizationreporter.dependenciesbaserelation", null, locale)).append(" ");
			}
			description.append(" ").append(messageSource.getMessage("normalizationreporter.lost", null, locale)).append(".");
		} 
		
		if ((config.getDiagnoseLevel() == 2) || (config.getDiagnoseLevel() == 3)) {
			description.append(generateLevel3Div(analysis.getLostFunctionalDependencies(), "normalizationreporter.dependencybaserelationa", "normalizationreporter.dependenciesbaserelationa", "normalizationreporter.lost", locale));

			/*currElemID = NFHelper.getNextElementID();
			description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
			description.append("<html>").append(HTML_HEADER).append("<body>");
			description.append("<p>").append(messageSource.getMessage("normalizationreporter.dependencieslost", null, locale)).append(":</p>");

			description.append(generateTable(analysis.getLostFunctionalDependencies()));

			description.append("</body></html>");
			description.append("\"></input>");

			description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(numberOfLostDependencies);

			if (numberOfLostDependencies == 1){
				description.append(" ").append(messageSource.getMessage("normalizationreporter.dependencybaserelationa", null, locale)).append(" ");
			} else {
				description.append(" ").append(messageSource.getMessage("normalizationreporter.dependenciesbaserelationa", null, locale)).append(" ");
			}
			description.append(messageSource.getMessage("normalizationreporter.lost", null, locale)).append(".");*/
		}
		report.setDescription(description.toString());

		//CONFIGURE REPORT
		report.setShowHint(false);
		report.setShowError(true);
		report.setShowErrorDescription(true);

		return report;
	}

	public static ErrorReport createLosslessErrorReport(LosslessAnalysis analysis, ReporterConfig config, MessageSource messageSource, Locale locale){
		ErrorReport report = new ErrorReport();

		//SET ERROR
		report.setError(messageSource.getMessage("normalizationreporter.notlossless", null, locale));
		
		//SET HINT
		
		//SET DESCRIPTION
		report.setDescription(messageSource.getMessage("normalizationreporter.joinnotbaserelation", null, locale));

		//CONFIGURE REPORT
		report.setShowHint(false);
		report.setShowError(true);
		report.setShowErrorDescription(true);

		return report;
	}
	
	public static ErrorReport createDecompositionErrorReport(DecompositionAnalysis analysis, ReporterConfig config, MessageSource messageSource, Locale locale){
		ErrorReport report = new ErrorReport();
		StringBuilder description = new StringBuilder();
		int numberOfMissingAttributes = analysis.getMissingAttributes().size();

        //SET ERROR
		report.setError(messageSource.getMessage("normalizationreporter.invaliddecomposition", null, locale));

		//SET HINT

		//SET DESCRIPTION
		if (config.getDiagnoseLevel() == 0){
			description.append(messageSource.getMessage("normalizationreporter.notcomprisedbaserelation", null, locale));				
		}
		
		if (config.getDiagnoseLevel() == 1){
			description.append(numberOfMissingAttributes);
			if (numberOfMissingAttributes == 1){
				description.append(" ").append(messageSource.getMessage("normalizationreporter.attributeis", null, locale)).append(" ");
			} else {
				description.append(" ").append(messageSource.getMessage("normalizationreporter.attributesare", null, locale)).append(" ");
			}
			description.append(messageSource.getMessage("normalizationreporter.notcompriseddecomposed", null, locale));
		} 
		
		if ((config.getDiagnoseLevel() == 2) || (config.getDiagnoseLevel() == 3)) {
			description.append(generateLevel3Div(analysis.getMissingAttributes(), "normalizationreporter.attributeisa", "normalizationreporter.attributesarea", "normalizationreporter.missing", locale));

			/*currElemID = NFHelper.getNextElementID();
			description.append("<input type='hidden' id='").append(currElemID).append("' value=\"");
			description.append("<html>").append(HTML_HEADER).append("<body>");
			description.append("<p>").append(messageSource.getMessage("normalizationreporter.attributenotcomprised", null, locale)).append(":</p>");

			description.append(generateTable(analysis.getMissingAttributes()));

			description.append("</body></html>");
			description.append("\"></input>");

			description.append("<a href=\"javascript:openWindow('").append(currElemID).append("')\">").append(numberOfMissingAttributes);

			if (numberOfMissingAttributes == 1){
				description.append(" ").append(messageSource.getMessage("normalizationreporter.attributeisa", null, locale)).append(" ");
			} else {
				description.append(" ").append(messageSource.getMessage("normalizationreporter.attributesarea", null, locale)).append(" ");
			}
			description.append(messageSource.getMessage("normalizationreporter.missing", null, locale)).append(".");*/
		}
		report.setDescription(description.toString());

		//CONFIGURE REPORT
		report.setShowHint(false);
		report.setShowError(true);
		report.setShowErrorDescription(true);
		
		return report;
	}
}
