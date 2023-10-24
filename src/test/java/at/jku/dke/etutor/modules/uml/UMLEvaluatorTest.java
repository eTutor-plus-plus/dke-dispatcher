package at.jku.dke.etutor.modules.uml;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.Grading;
import at.jku.dke.etutor.modules.uml.report.UMLReport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class UMLEvaluatorTest {

    @Test
    void generateHTMLResult() throws Exception {
        // Arrange
        int exerciseId = 1;
        int userId = 1;
        var loc = Locale.GERMAN;
        Map<String, String> passedParameters = new HashMap<>();
        Map<String, String> passedAttributes = new HashMap<>();
        passedAttributes.put("submit", "test");
        passedAttributes.put("submission", """
                @startuml
                Class01 "1" *-- "many" Class02 : contains
                @enduml
                """);
        passedAttributes.put("diagnoseLevel", "0");
        var evaluator = new UMLEvaluator();

        // Act
        Analysis analysis = evaluator.analyze(exerciseId, userId, passedAttributes, passedParameters, loc);
        String html = evaluator.generateHTMLResult(analysis, passedAttributes, loc);

        // Assert
        assertNotNull(html);
        assertTrue(html.contains("<img"));
    }

    @Test
    void reportWithInvalidSyntax() throws Exception {
        // Arrange
        int exerciseId = 1;
        int userId = 1;
        var loc = Locale.GERMAN;
        Map<String, String> passedParameters = new HashMap<>();
        Map<String, String> passedAttributes = new HashMap<>();
        passedAttributes.put("submit", "test");
        passedAttributes.put("submission", """
                @startuml
                Class01 "1 *-- "many" Class02 : contains
                @enduml
                """);
        passedAttributes.put("diagnoseLevel", "0");
        var evaluator = new UMLEvaluator();

        // Act
        Analysis analysis = evaluator.analyze(exerciseId, userId, passedAttributes, passedParameters, loc);
        String html = evaluator.generateHTMLResult(analysis, passedAttributes, loc);

        // Assert
        assertNotNull(html);
        assertTrue(html.contains("<img")); // Syntax error messages are rendered as image
    }

    @Test
    void reportWithNullSubmission() throws Exception {
        // Arrange
        int exerciseId = 1;
        int userId = 1;
        var loc = Locale.GERMAN;
        Map<String, String> passedParameters = new HashMap<>();
        Map<String, String> passedAttributes = new HashMap<>();
        passedAttributes.put("submit", "test");
        passedAttributes.put("submission", null);
        passedAttributes.put("diagnoseLevel", "0");
        var evaluator = new UMLEvaluator();

        // Act
        Analysis analysis = evaluator.analyze(exerciseId, userId, passedAttributes, passedParameters, loc);
        String html = evaluator.generateHTMLResult(analysis, passedAttributes, loc);

        // Assert
        assertNotNull(html);
        assertTrue(html.contains("<p"));
    }
}