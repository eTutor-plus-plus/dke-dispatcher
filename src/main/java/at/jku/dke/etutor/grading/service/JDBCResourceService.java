package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.model.repositories.GradingDTORepository;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class JDBCResourceService {

    private final String TASK_TYPE = "jdbc";
    private final SubmissionDispatcherService dispatcherService;
    private final GradingDTORepository gradingDTORepository;

    private final Logger logger;

    public JDBCResourceService(SubmissionDispatcherService dispatcherService, GradingDTORepository gradingDTORepository, ApplicationProperties properties) throws ClassNotFoundException {
        Class.forName(properties.getGrading().getJDBCDriver());
        this.logger= (Logger) LoggerFactory.getLogger("at.jku.dke.etutor.sqlexercisemanager");
        this.dispatcherService=dispatcherService;
        this.gradingDTORepository=gradingDTORepository;


    }

}
