package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.rest.dto.RestGrading;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class GradingManager {
    public static HashMap<String, RestGrading> gradingMap = new HashMap<>();

    public static HashMap<String, RestGrading> getGradingMap() {
        return gradingMap;
    }

    public static void setGradingMap(HashMap<String, RestGrading> gradingMap) {
        GradingManager.gradingMap = gradingMap;
    }
}
