package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.xquery.exercise.XQExerciseManagerImpl;
import org.springframework.stereotype.Service;

@Service
public class XQueryResourceService {
    private ApplicationProperties properties;
    private XQExerciseManagerImpl xqExerciseManager;

    public XQueryResourceService(ApplicationProperties properties, XQExerciseManagerImpl xqExerciseManager){
        this.properties = properties;
        this.xqExerciseManager = xqExerciseManager;
    }
}
