package at.jku.dke.etutor.modules.drools.grading;


import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.grading.rest.ETutorDroolsController;
import at.jku.dke.etutor.modules.drools.analysis.DroolsAnalysis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DroolsGrading extends DefaultGrading {

    private final Logger logger;
    private final DroolsAnalysis analysis;

    public DroolsGrading(DroolsAnalysis analysis, int maxPoints) throws IOException {
        super();
        this.logger = LoggerFactory.getLogger(ETutorDroolsController.class);
        this.analysis = analysis;
        this.setMaxPoints(maxPoints);
        grade();
    }

    public void grade() throws IOException {
        logger.debug("Enter: grade()");
        if (analysis.isHasSyntaxError()) this.setPoints(0);
        else {
            double points = this.getMaxPoints() - (analysis.getWrongFactList().size() + Math.abs(analysis.getAdditionalFacts())) * analysis.getTaskErrorWeighting() / 100.0;
            if (points < 0) points = 0;
            this.setPoints(points);
            logger.debug("set points to: {}", points);
        }
    }

}
