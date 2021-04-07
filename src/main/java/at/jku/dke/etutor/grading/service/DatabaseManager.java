package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.rest.dto.RestGrading;
import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.rest.dto.SubmissionId;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class DatabaseManager {
    private static Connection con;
    private static boolean connected;

    public DatabaseManager()  {
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(GradingConstants.postgreURL,
                    GradingConstants.postgreUser, GradingConstants.postgrePwd);
            con.setAutoCommit(false);
            connected = true;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addSubmission(Submission submission, SubmissionId id){
        if (connected){
            String query = "INSERT INTO submission VALUES("+
                    "'"+id.getId()+"'"+", "+
                    "'"+submission.getTaskType()+"'"+", " +
                    submission.getExerciseId()+", " +
                    submission.getUserId()+", " +
                    "'"+submission.getPassedAttributes().get("action")+"'"+", " +
                    "'"+submission.getPassedAttributes().get("diagnoseLevel")+"'"+", " +
                    "'"+submission.getPassedAttributes().get("submission")+"'"+", " +
                    submission.getMaxPoints() +
                    ");";
            System.out.println(query);
            try {
                PreparedStatement stmt = con.prepareStatement(query);
                stmt.executeUpdate();
                con.commit();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                try {
                    con.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public RestGrading getGrading(String submissionId){

        return null;
    }

}
