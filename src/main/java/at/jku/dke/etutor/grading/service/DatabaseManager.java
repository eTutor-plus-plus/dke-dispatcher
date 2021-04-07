package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.rest.dto.RestGrading;
import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.rest.dto.SubmissionId;
import org.springframework.stereotype.Component;

import java.sql.*;

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

    public static void addGrading(RestGrading grading, SubmissionId id){
        if(connected){
            String query = "INSERT INTO grading VALUES("+
                    "'"+id.getId()+"', " +
                    grading.getMaxPoints()+", " +
                    grading.getPoints() +
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

    public static RestGrading getGrading(String submissionId){
        if(connected){
            String query = "SELECT * FROM grading WHERE submissionId = " + "'" + submissionId + "';";
            try {
                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet resultSet = stmt.executeQuery();
                RestGrading grading = null;
                if (resultSet.next()){
                    double maxPoints = resultSet.getDouble("maxPoints");
                    double points = resultSet.getDouble("points");
                    grading = new RestGrading(points, maxPoints);
                }
                return grading;
            }catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }
}
