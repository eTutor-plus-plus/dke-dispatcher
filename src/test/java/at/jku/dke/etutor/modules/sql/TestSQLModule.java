package at.jku.dke.etutor.modules.sql;

import at.jku.dke.etutor.grading.ETutorGradingApplication;
import at.jku.dke.etutor.grading.rest.dto.GradingDTO;
import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.rest.dto.SubmissionId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(classes= ETutorGradingApplication.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestSQLModule {
    private ObjectMapper mapper = new ObjectMapper();
    private HttpClient client = HttpClient.newHttpClient();
    private List<String> ids = new ArrayList<>();
    private final String REST_URL = "http://localhost:8081";

    @Autowired
    private SQLConstants sqlConstants;

    private String CONN_URL;
    private String CONN_USER;
    private String CONN_PWD;
    private final String EXERCISE_CONSTRAINTS = " WHERE id < 13914 AND id NOT IN (65, 13089, 13883, 13884, 13885, 13887, 13901, 13902, 13903, 13904, 13905" +
            ", 13906, 13907, 13908, 13909, 13910, 13911, 13912, 13913) " ;
    private final String ACTION_STRING = "diagnose";
    private final String DIAGNOSE_LEVEL = "3";

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
    @Disabled
    void whenSubmissionIsSolution_thenAllPoints() throws IOException, InterruptedException, SQLException {
        ResultSet exercises = getExercisesResultSet();
        while (exercises.next()) {
            int id = exercises.getInt("id");
            String solution = exercises.getString("solution");
            Submission submission = prepareSubmission(id, solution);
            assertFalse(submission == null);
            sendSubmission(submission);
            Thread.sleep(350);
        }
        Thread.sleep(350);
        getGradings();
        System.out.println(ids.size());
    }

    void sendSubmission(Submission submission) throws IOException, InterruptedException {
        String submissionJson = mapper.writeValueAsString(submission);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(REST_URL + "/submission"))
                .POST(HttpRequest.BodyPublishers.ofString(submissionJson))
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .setHeader(HttpHeaders.ACCEPT_LANGUAGE, "de")
                .build();
        HttpResponse<String> response = sendRequest(request);
        String id = getId(response);
        ids.add(id);
    }

    void getGradings() throws IOException, InterruptedException {
        for (String id : ids) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(REST_URL + "/grading/" + id))
                    .build();
            HttpResponse<String> response = sendRequest(request);
            GradingDTO grading = extractGrading(response);
            System.out.println(id);
            System.out.println("Result " + "\n" + grading.getResult());
            assertEquals(1, grading.getPoints());

        }
    }

    HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    String getId(HttpResponse<String> response) throws JsonProcessingException {
        EntityModel<SubmissionId> submissionModel = mapper.readValue(response.body(), new TypeReference<EntityModel<SubmissionId>>() {
        });
        SubmissionId submissionId = submissionModel.getContent();
        String id = submissionId.getSubmissionId();
        return id;
    }

    GradingDTO extractGrading(HttpResponse<String> response) throws JsonProcessingException {
        EntityModel<GradingDTO> entityModel = mapper.readValue(response.body(), new TypeReference<EntityModel<GradingDTO>>() {
        });
        return entityModel.getContent();
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
        submission.setTaskType("sql");
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

