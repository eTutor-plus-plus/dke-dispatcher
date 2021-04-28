package at.jku.dke.etutor.modules.sql;

import at.jku.dke.etutor.grading.rest.dto.GradingDTO;
import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.rest.dto.SubmissionId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.*;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestSQLModule {
    ObjectMapper mapper = new ObjectMapper();
    HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    void initialize() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
    }

    @Test
   //@Disabled
    void testSolutions() throws IOException, InterruptedException, SQLException {
        ResultSet exercises = getExercises();
        while(exercises.next()){
            int id = exercises.getInt("id");
            /*if(id != 13089 && id != 13171 && id != 13175 && id != 2
                    && id != 23 && id != 25 && id != 26 && id != 29 && id != 31
                    && id != 33 && id != 39 && id != 55 && id != 59 && id != 68
                    && id != 71 && id != 104 && id != 108 && id != 153
                    && id != 10009 && id != 10010 && id != 10011 && id != 10012
                    && id != 13868 && id != 13870 && id != 13883 )
            {*/
                String solution = exercises.getString("solution");
                Submission submission = prepareSubmission(id, solution);
                assertFalse(submission == null);
                GradingDTO grading = executeTest(submission);
                //assertEquals(1, grading.getPoints());
            //}
        }
    }

    GradingDTO executeTest(Submission submission) throws IOException, InterruptedException {
        String submissionJson = mapper.writeValueAsString(submission);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/submission"))
                .POST(HttpRequest.BodyPublishers.ofString(submissionJson))
                .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
        HttpResponse<String> response = sendRequest(request);
        String id = getId(response);

        TimeUnit.SECONDS.sleep(2);
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/grading/"+id))
                .build();
        response = sendRequest(request);
        return getGrading(response);
    }

    HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    String getId(HttpResponse<String> response) throws JsonProcessingException {
        EntityModel<SubmissionId> submissionModel = mapper.readValue(response.body(), new TypeReference<EntityModel<SubmissionId>>(){});
        SubmissionId submissionId = submissionModel.getContent();
        String id = submissionId.getSubmissionId();
        return id;
    }

    GradingDTO getGrading(HttpResponse<String> response) throws JsonProcessingException {
        EntityModel<GradingDTO> entityModel = mapper.readValue(response.body(), new TypeReference<EntityModel<GradingDTO>>(){});
       return entityModel.getContent();
    }

    Submission prepareSubmission(int id, String solution){
        Submission submission = new Submission();
        HashMap<String, String> attributeMap = new HashMap<>();
        attributeMap.put("action", "diagnose");
        attributeMap.put("diagnoseLevel", "3");
        attributeMap.put("submission", solution);
        submission.setPassedAttributes(attributeMap);
        submission.setPassedParameters(new HashMap<String, String>());
        submission.setUserId(1);
        submission.setMaxPoints(1);
        submission.setTaskType("sql");
        submission.setExerciseId(id);
        return submission;
    }

    ResultSet getExercises(){
        PreparedStatement stmt;
        ResultSet rs;
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5433/sql", "sql", "sql")) {
            String query = "select id, solution from exercises where id IN (13651) ORDER BY id asc;";
            stmt = con.prepareStatement(query);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    Submission fetchSpecificSQLSubmssionUtil(int exerciseID)throws NoExerciseFoundException{
        Submission submission = new Submission();
        HashMap<String, String> attributeMap = new HashMap<>();
        attributeMap.put("action", "diagnose");
        attributeMap.put("diagnoseLevel", "3");
        submission.setPassedAttributes(attributeMap);
        submission.setPassedParameters(new HashMap<String, String>());
        submission.setUserId(1);
        submission.setMaxPoints(1);
        submission.setTaskType("sql");

        PreparedStatement stmt;
        ResultSet rs;
        String solution;
        int exerciseId;

        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/sql", "sql", "sql")) {
            String query = "select * from exercises where id = "+ exerciseID +";";
            stmt = con.prepareStatement(query);
            rs = stmt.executeQuery();
            if (rs.next()) {
                solution = rs.getString("solution");
                exerciseId = rs.getInt("id");
                submission.setExerciseId(exerciseId);
                attributeMap.put("submission", solution);
            }else throw new NoExerciseFoundException("No exercise for id: " + exerciseID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return submission;
    }
}

