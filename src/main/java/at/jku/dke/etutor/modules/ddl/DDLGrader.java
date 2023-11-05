package at.jku.dke.etutor.modules.ddl;

import at.jku.dke.etutor.core.evaluation.DefaultGrading;
import at.jku.dke.etutor.modules.ddl.grading.DDLGraderConfig;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class DDLGrader {
    //region Fields
    private Logger logger;
    //endregion

    public DDLGrader() {
        try {
            this.logger = (Logger) LoggerFactory.getLogger(DDLGrader.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DefaultGrading grade(DDLGraderConfig graderConfig) {
        return null;
    }
}
