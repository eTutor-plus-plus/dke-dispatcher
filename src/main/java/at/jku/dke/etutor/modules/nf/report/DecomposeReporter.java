package at.jku.dke.etutor.modules.nf.report;

import at.jku.dke.etutor.modules.nf.RDBDConstants;
import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.analysis.normalization.NormalizationAnalysis;
import at.jku.dke.etutor.modules.nf.model.NormalformLevel;
import at.jku.dke.etutor.modules.nf.ui.IdentifiedRelation;
import org.springframework.context.MessageSource;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;

public class DecomposeReporter {

	public static Report report(DecomposeReporterConfig config, MessageSource messageSource, Locale locale){
		ErrorReport reportAtom;
		Report report = new Report();
		StringBuilder prologue = new StringBuilder();

		//SET PROLOGUE
		if (config.getAction().equalsIgnoreCase(RDBDConstants.EVAL_ACTION_SUBMIT)) {
			if (config.getDecomposeAnalysis().submissionSuitsSolution()) {
				prologue.append(messageSource.getMessage("decomposereporter.correctsolution", null, locale));
			} else {
				prologue.append(messageSource.getMessage("decomposereporter.notcorrectsolution", null, locale));
			}

			prologue.append(messageSource.getMessage("decomposereporter.suggestingpoints", new Object[]{config.getGrading().getPoints()}, locale));

			if (config.getGrading().getPoints() == 1){
				prologue.append(" ").append(messageSource.getMessage("decomposereporter.point", null, locale)).append(" ");
			} else {
				prologue.append(" ").append(messageSource.getMessage("decomposereporter.points", null, locale)).append(" ");
			}

			prologue.append(messageSource.getMessage("decomposereporter.yoursubmission", null, locale));
		} else {
			if (config.getDecomposeAnalysis().submissionSuitsSolution()) {
				prologue.append(messageSource.getMessage("decomposereporter.correctsolution", null, locale));
			} else {
				prologue.append(messageSource.getMessage("decomposereporter.notcorrectsolution", null, locale));
			}
		}
		report.setPrologue(prologue.toString());
		
		//SET ERROR REPORT FOR LOST FUNCTIONAL DEPENDENCIES IF NECESSARY
		if ((!config.getDecomposeAnalysis().getOverallDependenciesPreservationAnalysis().submissionSuitsSolution()) && (!config.getAction().equals(RDBDConstants.EVAL_ACTION_CHECK))){
			reportAtom = NormalizationReporter.createDependenciesPreservationErrorReport(config.getDecomposeAnalysis().getOverallDependenciesPreservationAnalysis(), config, messageSource, locale); 
			reportAtom.setError(messageSource.getMessage("decomposereporter.toomanylost", new Object[]{config.getDecomposeAnalysis().getMaxLostDependencies(), config.getDecomposeAnalysis().getOverallDependenciesPreservationAnalysis().lostFunctionalDependenciesCount()}, locale));
			report.addErrorReport(reportAtom);
		}

		//ADD REPORT_ATOM_GROUP FOR EACH DECOMPOSE STEP
		if (!config.getAction().equals(RDBDConstants.EVAL_ACTION_CHECK)){
			String baseRelationID;
			Iterator<String> decomposeStepAnalysesIterator = config.getDecomposeAnalysis().iterAnalysedDecomposeStepBaseRelations();
			while (decomposeStepAnalysesIterator.hasNext()){
				baseRelationID = decomposeStepAnalysesIterator.next();
				report.addErrorReportGroup(createDecomposeStepReportAtomGroup(baseRelationID, config, messageSource, locale));	
			}
		}

		//CONFIGURE REPORT
		report.setShowPrologue(true);

		return report;	
	}

	public static ErrorReportGroup createDecomposeStepReportAtomGroup(String baseRelationID, DecomposeReporterConfig config, MessageSource messageSource, Locale locale){
		ErrorReport reportAtom;
		ErrorReportGroup relationSpecificGroup;
		ErrorReportGroup group = new ErrorReportGroup();
		NormalizationAnalysis analysis = config.getDecomposeAnalysis().getDecomposeStepAnalyses(baseRelationID);
		
		//SET HEADER
		group.setHeader(messageSource.getMessage("decomposereporter.decompositionreports", new Object[]{baseRelationID}, locale));
		
		//REPORT LOSS_LESS_ANALYSIS (IF NECESSARY)
		if (analysis.getLossLessAnalysis() != null){
			if (!analysis.getLossLessAnalysis().submissionSuitsSolution()){
				group.addErrorReport(NormalizationReporter.createLossLessErrorReport(analysis.getLossLessAnalysis(), config, messageSource, locale));
			}
		}

		//REPORT DECOMPOSITION_ANALYSIS (IF NECESSARY)
		if (analysis.getDecompositionAnalysis() != null){
			if (!analysis.getDecompositionAnalysis().submissionSuitsSolution()){
				group.addErrorReport(NormalizationReporter.createDecompositionErrorReport(analysis.getDecompositionAnalysis(), config, messageSource, locale));
			}
		}

		//REPORT DEPENDENCIES_PRESERVATION_ANALYSIS (IF NECESSARY)
		if (analysis.getDepPresAnalysis() != null){
			if (!analysis.getDepPresAnalysis().submissionSuitsSolution()){
				reportAtom = NormalizationReporter.createDependenciesPreservationErrorReport(analysis.getDepPresAnalysis(), config, messageSource, locale);
				reportAtom.setReportAtomType(ReportAtomType.INFO);
				group.addErrorReport(reportAtom);
			}
		}
		
		//ADD RELATION SPECIFIC REPORT_ATOM_GROUPS
		relationSpecificGroup = createRelationSpecificReportAtomGroup(baseRelationID + ".1", config.getDecomposeAnalysis().getDecomposeStepAnalyses(baseRelationID), config.getDecomposeAnalysis().getDecomposedRelations(), config, messageSource, locale);
		if (!relationSpecificGroup.getErrorReports().isEmpty()){
			group.addSubErrorReportGroup(relationSpecificGroup);
		}

		relationSpecificGroup = createRelationSpecificReportAtomGroup(baseRelationID + ".2", config.getDecomposeAnalysis().getDecomposeStepAnalyses(baseRelationID), config.getDecomposeAnalysis().getDecomposedRelations(), config, messageSource, locale);
		if (!relationSpecificGroup.getErrorReports().isEmpty()){
			group.addSubErrorReportGroup(relationSpecificGroup);
		}
		
		return group;
	}
	
	public static ErrorReportGroup createRelationSpecificReportAtomGroup(String relationID, NormalizationAnalysis analysis, Collection<IdentifiedRelation> decomposedRelations, DecomposeReporterConfig config, MessageSource messageSource, Locale locale){
		ErrorReport reportAtom;
		NormalformReporterConfig nfReporterConfig;
		ErrorReportGroup group = new ErrorReportGroup();

		RDBDHelper.getLogger().log(Level.INFO, "Create report for relation: '" + relationID + "'. Normalization-analysis is null: " + (analysis == null));

		//SET HEADER
		group.setHeader(messageSource.getMessage("decomposereporter.relationreports", new Object[]{relationID}, locale));
		
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

		//ADD REDUNDAND_DEPENDENCIES REPORT_ATOM
		if (analysis.getRedundandDependenciesAnalysis(relationID) != null){
			if (!analysis.getRedundandDependenciesAnalysis(relationID).submissionSuitsSolution()){
				group.addErrorReport(MinimalCoverReporter.createRedundandDependenciesErrorReport(analysis.getRedundandDependenciesAnalysis(relationID), config, messageSource, locale));
			}
		}

		//ADD CANONICAL_REPRESENTATION REPORT_ATOM
		if (analysis.getCanonicalRepresentationAnalysis(relationID) != null){
			if (!analysis.getCanonicalRepresentationAnalysis(relationID).submissionSuitsSolution()){
				group.addErrorReport(MinimalCoverReporter.createCanonicalRepresentationErrorReport(analysis.getCanonicalRepresentationAnalysis(relationID), config, messageSource, locale));
			}
		}
		
		//ADD NORMALFORM_ANALYSIS REPORT_ATOM
		if (analysis.getNormalformAnalysis(relationID) != null){
			if ((!analysis.getNormalformAnalysis(relationID).submissionSuitsSolution()) && (!RDBDHelper.isInnerNode(relationID, decomposedRelations))){
				nfReporterConfig = new NormalformReporterConfig();
				nfReporterConfig.setAction(config.getAction());
				nfReporterConfig.setDiagnoseLevel(config.getDiagnoseLevel());
				nfReporterConfig.setDesiredNormalformLevel(config.getDecomposeAnalysis().getTargetLevel());

				reportAtom = NormalformReporter.createNormalformErrorReport(analysis.getNormalformAnalysis(relationID), nfReporterConfig, messageSource, locale);
			}	else {
				reportAtom = new ErrorReport();

				reportAtom.setShowError(true);
				reportAtom.setShowHint(false);
				reportAtom.setShowErrorDescription(false);

				String level = "";
				if (analysis.getNormalformAnalysis(relationID).getOverallNormalformLevel().equals(NormalformLevel.FIRST)){
					level = messageSource.getMessage("decomposereporter.first", null, locale);
				}
				if (analysis.getNormalformAnalysis(relationID).getOverallNormalformLevel().equals(NormalformLevel.SECOND)){
					level = messageSource.getMessage("decomposereporter.second", null, locale);
				}
				if (analysis.getNormalformAnalysis(relationID).getOverallNormalformLevel().equals(NormalformLevel.THIRD)){
					level = messageSource.getMessage("decomposereporter.third", null, locale);
				}
				if (analysis.getNormalformAnalysis(relationID).getOverallNormalformLevel().equals(NormalformLevel.BOYCE_CODD)){
					level = messageSource.getMessage("decomposereporter.boycecodd", null, locale);
				}

				reportAtom.setReportAtomType(ReportAtomType.INFO);
				reportAtom.setError(messageSource.getMessage("decomposereporter.normalform", new Object[]{relationID, level}, locale));
			}
			group.addErrorReport(reportAtom);
		}

		//ADD KEYS_ANALYSIS REPORT_ATOM
		if (analysis.getKeysAnalysis(relationID) != null){
			if (!analysis.getKeysAnalysis(relationID).submissionSuitsSolution()){
				group.addErrorReport(KeysReporter.createKeysErrorReport(analysis.getKeysAnalysis(relationID), config, messageSource, locale));
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
}