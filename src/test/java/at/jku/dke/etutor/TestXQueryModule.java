package at.jku.dke.etutor;

import at.jku.dke.etutor.grading.ETutorGradingApplication;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.model.entities.Grading;
import at.jku.dke.etutor.grading.rest.model.entities.Submission;
import at.jku.dke.etutor.grading.rest.model.repositories.GradingDTORepository;
import at.jku.dke.etutor.grading.service.SubmissionDispatcherService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class that fetches the persisted xquery-solutions, wraps
 * them as submissions and lets them be evaluated by the dispatcher
 */
@SpringBootTest(classes= ETutorGradingApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Disabled
public class TestXQueryModule {
    private List<String> ids = new ArrayList<>();

    @Autowired
    private ApplicationProperties properties;
    @Autowired
    private SubmissionDispatcherService dispatcherService;
    @Autowired
    private GradingDTORepository gradingRepo;

    private String CONN_URL;
    private String CONN_USER;
    private String CONN_PWD;
    private final String EXERCISE_CONSTRAINTS = " where id < 14326";
    private final String[] ACTION_STRINGS = {"diagnose", "submit"};
    private final String DIAGNOSE_LEVEL = "3";
    private final String TASK_TYPE = "http://www.dke.uni-linz.ac.at/etutorpp/TaskAssignmentType#XQTask";

    /**
     * Sets the connection parameters
     * @throws ClassNotFoundException
     */
    @BeforeAll
    void setup() throws ClassNotFoundException {
        CONN_URL = properties.getXquery().getConnUrl();
        CONN_USER = properties.getXquery().getConnUser();
        CONN_PWD = properties.getXquery().getConnPwd();
        Class.forName("org.postgresql.Driver");
    }

    /**
     * Test that fetches the solutions of the persisted exercises and evaluates them
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws SQLException
     */
    @Test
    void whenSubmissionIsSolution_thenAllPoints() throws InterruptedException, SQLException {
        ResultSet exercises = getExercisesResultSet();
        while (exercises.next()) {
            int id = exercises.getInt("id");
            String solution = exercises.getString("query");
            Submission submission = prepareSubmission(id, solution, ACTION_STRINGS[0]);
            assertFalse(submission == null);
            evaluateSubmission(submission);
            Thread.sleep(2000);
            submission = prepareSubmission(id, solution, ACTION_STRINGS[1]);
            assertFalse(submission == null);
            evaluateSubmission(submission);
            Thread.sleep(2000);
        }
        Thread.sleep(10000);
        getGradings();
        System.out.println(ids.size());
    }

    /**
     * Utility method that triggers the evaluation of a submission
     * @param submission the submission
     */
    void evaluateSubmission(Submission submission){
        String id = UUID.randomUUID().toString();
        submission.setSubmissionId(id);
        dispatcherService.run(submission, Locale.GERMAN);
        ids.add(id);
    }

    /**
     * Utility method that iterates over the submission-ids and fetches the corresponding grading from the database
     */
    void getGradings()  {
        for (String id : ids) {
            Optional<Grading> optGrading = gradingRepo.findById(id);
            System.out.println(id);
            assertTrue(optGrading.isPresent());
            Grading grading = optGrading.get();
            System.out.println(id);
            System.out.println("Result " + "\n" + grading.getResult());
            assertEquals(1, grading.getPoints());
            assertEquals(grading.getMaxPoints(), grading.getPoints());

        }
    }



    Submission prepareSubmission(int id, String solution, String action) {
        Submission submission = new Submission();
        HashMap<String, String> attributeMap = new HashMap<>();
        attributeMap.put("action", action);
        attributeMap.put("diagnoseLevel", DIAGNOSE_LEVEL);
        attributeMap.put("submission", solution);
        submission.setPassedAttributes(attributeMap);
        submission.setPassedParameters(new HashMap<>());
        submission.setMaxPoints(1);
        submission.setTaskType(TASK_TYPE);
        submission.setExerciseId(id);
        return submission;
    }

    /**
     * Fetches all xquery-exercise-solutions
     * @return the ResultSet of the xq-exercise-table
     */
    ResultSet getExercisesResultSet() {
        PreparedStatement stmt;
        try (Connection con = DriverManager.getConnection(CONN_URL, CONN_USER, CONN_PWD)) {
            String query = "select id, query from exercise " + EXERCISE_CONSTRAINTS + " ORDER BY id asc;";
            stmt = con.prepareStatement(query);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
