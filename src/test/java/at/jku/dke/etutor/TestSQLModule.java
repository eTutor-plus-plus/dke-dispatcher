package at.jku.dke.etutor;

import at.jku.dke.etutor.grading.ETutorGradingApplication;
import at.jku.dke.etutor.grading.rest.dto.GradingDTO;
import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.rest.repositories.GradingDTORepository;
import at.jku.dke.etutor.grading.service.SubmissionDispatcherService;
import at.jku.dke.etutor.modules.sql.SQLConstants;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Test for the SQL module that fetches SQL-Solutions from the database and sends
 * them to the submission-endpoint as submission for evaluation.
 * Assertion is that all Exercises without Syntax-Errors have to be evaluated as correct.
 */

@SpringBootTest(classes= ETutorGradingApplication.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
//@Disabled
public class TestSQLModule {
    private List<String> ids = new ArrayList<>();

    @Autowired
    private SQLConstants sqlConstants;
    @Autowired
    SubmissionDispatcherService dispatcherService;
    @Autowired
    GradingDTORepository gradingRepo;

    private String CONN_URL;
    private String CONN_USER;
    private String CONN_PWD;

    private final String ACTION_STRING = "diagnose";
    private final String DIAGNOSE_LEVEL = "3";
    private final String TASK_TYPE = "sql";
    private final String EXERCISE_CONSTRAINTS = " WHERE id < 13914 AND id NOT IN (65, 13089, 13883, 13884, 13885, 13887, 13901, 13902, 13903, 13904, 13905" +
            ", 13906, 13907, 13908, 13909, 13910, 13911, 13912, 13913) " ;

    @BeforeAll
    void setup() {
        CONN_URL = sqlConstants.getConnURL();
        CONN_USER = sqlConstants.getConnUser();
        CONN_PWD = sqlConstants.getConnPwd();
    }

    @BeforeEach
    void initialize() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        //exerciseConstraints = "id =22";
    }

    /**
     * Test that fetches the solutions of the persisted exercises and sends them to the dispatcher for evaluation
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws SQLException
     */
    @Test
    void whenSubmissionIsSolution_thenAllPoints() throws IOException, InterruptedException, SQLException {
        ResultSet exercises = getExercisesResultSet();
        while (exercises.next()) {
            int id = exercises.getInt("id");
            String solution = exercises.getString("solution");
            Submission submission = prepareSubmission(id, solution);
            assertNotNull(submission);
            evaluateSubmission(submission);
            Thread.sleep(350);
        }
        Thread.sleep(350);
        getGradings();
        System.out.println(ids.size());
    }

    void evaluateSubmission(Submission submission){
        String id = UUID.randomUUID().toString();
        submission.setSubmissionId(id);
        dispatcherService.run(submission, Locale.GERMAN);
        ids.add(id);
    }

    void getGradings() {
        for (String id : ids) {
            Optional<GradingDTO> optGrading = gradingRepo.findById(id);
            assertTrue(optGrading.isPresent());
            GradingDTO grading = optGrading.get();
            System.out.println(id);
            System.out.println("Result " + "\n" + grading.getResult());
            assertEquals(1, grading.getPoints());
            assertEquals(grading.getMaxPoints(), grading.getPoints());
        }
    }

    Submission prepareSubmission(int id, String solution) {
        Submission submission = new Submission();
        HashMap<String, String> attributeMap = new HashMap<>();
        attributeMap.put("action", ACTION_STRING);
        attributeMap.put("diagnoseLevel", DIAGNOSE_LEVEL);
        attributeMap.put("submission", solution);
        submission.setPassedAttributes(attributeMap);
        submission.setPassedParameters(new HashMap<String, String>());
        submission.setMaxPoints(1);
        submission.setTaskType(TASK_TYPE);
        submission.setExerciseId(id);
        return submission;
    }

    ResultSet getExercisesResultSet() {
        PreparedStatement stmt;
        ResultSet rs;
        try (Connection con = DriverManager.getConnection(CONN_URL, CONN_USER, CONN_PWD)) {
            String query = "select id, solution from exercises " + EXERCISE_CONSTRAINTS + " ORDER BY id asc;";
            stmt = con.prepareStatement(query);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

