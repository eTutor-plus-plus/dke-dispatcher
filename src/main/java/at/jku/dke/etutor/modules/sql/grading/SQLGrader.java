package at.jku.dke.etutor.modules.sql.grading;

import java.util.Iterator;

import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.modules.sql.SQLEvaluationCriterion;
import at.jku.dke.etutor.modules.sql.analysis.SQLAnalysis;
import at.jku.dke.etutor.modules.sql.analysis.SQLCriterionAnalysis;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;


public class SQLGrader {

	private Logger logger;

	public SQLGrader() {
		super();

		try {
			this.logger = (Logger) LoggerFactory.getLogger(SQLGrader.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	public DefaultGrading grade(SQLAnalysis analysis, SQLGraderConfig gradingConfig) throws Exception {
		int points;
		String message;
		DefaultGrading grading;
		Iterator<SQLEvaluationCriterion> criterions;
		SQLEvaluationCriterion criterion;
		SQLCriterionAnalysis criterionAnalysis;
		SQLCriterionGradingConfig criterionGradingConfig;

		points = 0;
		grading = new DefaultGrading();
		criterions = gradingConfig.iterCriterionsToGrade();

		while (criterions.hasNext()) {
			criterion = criterions.next();
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

					this.logger.info(message);
					//grading.setGradingException(new GradingException(message));
					return grading;
				}
			} else {
				message = "";
				message = message.concat("Stopped grading due to errors. ");
				message = message.concat("No config for grading criterion' " + criterion + "' available. ");
				message = message.concat("This is an internal system error. ");
				message = message.concat("Please inform the system administrator.");

				this.logger.error(message);
				throw new MissingGradingCriterionConfigException(criterion, message);
			}
		}
		
		SQLCriterionAnalysis next;
		boolean isAllRight = true;
		Iterator<SQLCriterionAnalysis> i = analysis.iterCriterionAnalyses();
		while (i.hasNext()){
			next = i.next();
			if (next != null && !next.isCriterionSatisfied()){
				isAllRight = false;
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
}
