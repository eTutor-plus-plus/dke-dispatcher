package at.jku.dke.etutor.modules.sql.grading;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;
import at.jku.dke.etutor.modules.sql.analysis.SQLAnalysis;
import at.jku.dke.etutor.modules.sql.analysis.SQLCriterionAnalysis;


public class SQLGrader {

	private Logger logger;

	public SQLGrader() {
		super();

		try {
			this.logger = Logger.getLogger("at.jku.dke.etutor.modules.sql");
		} catch (Exception ignore) {
			ignore.printStackTrace();
		}
	}



	public DefaultGrading grade(SQLAnalysis analysis, SQLGraderConfig gradingConfig) throws Exception {
		int points;
		String message;
		DefaultGrading grading;
		Iterator criterions;
		SQLEvaluationCriterion criterion;
		SQLCriterionAnalysis criterionAnalysis;
		SQLCriterionGradingConfig criterionGradingConfig;

		points = 0;
		grading = new DefaultGrading();
		criterions = gradingConfig.iterCriterionsToGrade();

		while (criterions.hasNext()) {
			criterion = (SQLEvaluationCriterion)criterions.next();
			criterionGradingConfig = gradingConfig.getCriterionGradingConfig(criterion);

			if (criterionGradingConfig != null) {

				if (analysis.isCriterionAnalyzed(criterion)) {
					criterionAnalysis = analysis.getCriterionAnalysis(criterion);

					if (criterionAnalysis.getAnalysisException() == null) {
						if (criterionAnalysis.isCriterionSatisfied()) {
							if (criterionGradingConfig.getPositiveScope().equals(GradingScope.EXAMPLE)){
								points = criterionGradingConfig.getPositivePoints();
								grading.setPoints(points);
								return grading;
							} else {
								points = points + criterionGradingConfig.getPositivePoints();					
							}
						} else {
							if (criterionGradingConfig.getNegativeScope().equals(GradingScope.EXAMPLE)){
								points = criterionGradingConfig.getNegativePoints();
								grading.setPoints(points);
								return grading;
							} else {
								points = points - criterionGradingConfig.getNegativePoints();
							}
						}
					} else {
						//noch unsicher, was passieren soll, wenn kriterium aufgrund von exception in analyzer
						//nicht analysiert wurde.
					}
				} else {
					message = "";
					message = message.concat("Could not grade criterion '"  + criterion +  "'. ");

					this.logger.log(Level.INFO, message);
					//grading.setGradingException(new GradingException(message));
					//return grading;
				}
			} else {
				message = new String();
				message = message.concat("Stopped grading due to errors. ");
				message = message.concat("No config for grading criterion' " + criterion + "' available. ");
				message = message.concat("This is an internal system error. ");
				message = message.concat("Please inform the system administrator.");

				this.logger.log(Level.SEVERE, message);
				throw new MissingGradingCriterionConfigException(criterion, message);
			}
		}
		
		Object next;
		boolean isAllRight = true;
		SQLCriterionAnalysis ca;
		Iterator i = analysis.iterCriterionAnalyses();
		while (i.hasNext()){
			next = i.next();
			if (next != null){
				ca = (SQLCriterionAnalysis)next;
				if (!ca.isCriterionSatisfied()){
					isAllRight = false;
				}
			}
		}

		grading.setMaxPoints(1);
		
		if (isAllRight){
			grading.setPoints(1);
		} else {
			grading.setPoints(0);
		}
		
		return grading;
	}
	
	/*
	public DefaultGrading grade(SQLAnalysis analysis, SQLGraderConfig gradingConfig) {
		int points;
		String message;
		DefaultGrading grading;
		Iterator criterions;
		SQLEvaluationCriterion criterion;
		SQLCriterionAnalysis criterionAnalysis;
		SQLCriterionGradingConfig criterionGradingConfig;

		points = 0;
		grading = new DefaultGrading();
		criterions = gradingConfig.iterCriterionsToGrade();

		while (criterions.hasNext()) {
			criterion = (SQLEvaluationCriterion)criterions.next();
			criterionGradingConfig = gradingConfig.getCriterionGradingConfig(criterion);

			if (criterionGradingConfig != null) {

				if (analysis.isCriterionAnalyzed(criterion)) {
					criterionAnalysis = analysis.getCriterionAnalysis(criterion);

					if (criterionAnalysis.getAnalysisException() == null) {
						if (criterionAnalysis.isCriterionSatisfied()) {
							if (criterionGradingConfig.getPositiveScope().equals(GradingScope.EXAMPLE)){
								points = criterionGradingConfig.getPositivePoints();
								grading.setTotalPoints(points);
								return grading;
							} else {
								points = points + criterionGradingConfig.getPositivePoints();					
							}
						} else {
							if (criterionGradingConfig.getNegativeScope().equals(GradingScope.EXAMPLE)){
								points = criterionGradingConfig.getNegativePoints();
								grading.setTotalPoints(points);
								return grading;
							} else {
								points = points - criterionGradingConfig.getNegativePoints();
							}
						}
					} else {
						//noch unsicher, was passieren soll, wenn kriterium aufgrund von exception in analyzer
						//nicht analysiert wurde.
					}
				} else {
					message = new String();
					message = message.concat("Could not grade criterion '"  + criterion +  "'. ");

					this.logger.log(Level.INFO, message);
					//grading.setGradingException(new GradingException(message));
					//return grading;
				}
			} else {
				message = new String();
				message = message.concat("Stopped grading due to errors. ");
				message = message.concat("No config for grading criterion' " + criterion + "' available. ");
				message = message.concat("This is an internal system error. ");
				message = message.concat("Please inform the system administrator.");

				this.logger.log(Level.SEVERE, message);
				grading.setGradingException(new GradingException(message));
				return grading;
			}
		}
		
		if (points != 0){
			grading.setTotalPoints(gradingConfig.getMaximumPoints());
		} else {
			grading.setTotalPoints(0);
		}
		return grading;
	}
	 */
}
