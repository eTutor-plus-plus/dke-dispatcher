package at.jku.dke.etutor;

import at.jku.dke.etutor.grading.ETutorGradingApplication;
import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.grading.rest.model.entities.Grading;
import at.jku.dke.etutor.grading.rest.model.entities.Submission;
import at.jku.dke.etutor.grading.rest.model.repositories.GradingDTORepository;
import at.jku.dke.etutor.grading.service.SubmissionDispatcherService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes= ETutorGradingApplication.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
@Disabled
public class TestDLGModule {

    private List<String> ids = new ArrayList<>();

    @Autowired
    SubmissionDispatcherService dispatcherService;
    @Autowired
    GradingDTORepository gradingRepo;
    @Autowired
    ApplicationProperties properties;

    private String CONN_URL;
    private String CONN_USER;
    private String CONN_PWD;

    private final String[] ACTION_STRINGS = {"diagnose", "submit"};
    private final String DIAGNOSE_LEVEL = "3";
    private final String TASK_TYPE = "dlg";
    private final String EXERCISE_CONSTRAINTS = "" ;
    private final String EXERCISE_TABLE = "exercise";
    private final String COLUMNS = "id, query";

    @BeforeAll
    void setup() {
        CONN_URL = properties.getDatalog().getConnUrl();
        CONN_USER = properties.getGrading().getConnSuperUser();
        CONN_PWD = properties.getGrading().getConnSuperPwd();
    }

    @BeforeEach
    void initialize() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
    }

    /**
     * Test that fetches the solutions of the persisted exercises and sends them to the dispatcher for evaluation
     *
     * @throws IOException
     * @throws InterruptedException
     * @throws SQLException
     */
    @Test
    void whenSubmissionIsSolution_thenAllPoints() throws  InterruptedException, SQLException {
        ResultSet exercises = getExercisesResultSet();
        while (exercises.next()) {
            int id = exercises.getInt("id");
            String solution = exercises.getString("query");
            Submission submission = prepareSubmission(id, solution, ACTION_STRINGS[0]);
            assertNotNull(submission);
            evaluateSubmission(submission);
            submission = prepareSubmission(id, solution, ACTION_STRINGS[1]);
            assertNotNull(submission);
            evaluateSubmission(submission);
        }
        Thread.sleep(10000);
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
            Optional<Grading> optGrading = gradingRepo.findById(id);
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
            String query = "SELECT " + COLUMNS + " FROM " + EXERCISE_TABLE + EXERCISE_CONSTRAINTS + " ORDER BY id asc;";
            stmt = con.prepareStatement(query);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
