package at.jku.dke.etutor.grading.service;


import at.jku.dke.etutor.grading.config.ApplicationProperties;
import org.springframework.stereotype.Service;

@Service
public class DroolsResourceService {
    private final ApplicationProperties applicationProperties;
    private final String TASK_TYPE = "drools";
}
