package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.rest.dto.RestGrading;
import at.jku.dke.etutor.grading.rest.dto.Submission;
import at.jku.dke.etutor.grading.rest.dto.SubmissionId;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class DatabaseManager {
    private static Connection con;
    private static Logger logger;

    public DatabaseManager() {
        try {
            this.logger = Logger.getLogger("at.jku.dke.etutor.grading");
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection(GradingConstants.postgreURL,
                    GradingConstants.postgreUser, GradingConstants.postgrePwd);
            con.setAutoCommit(false);

        } catch (ClassNotFoundException | SQLException e) {
            logger.log(Level.SEVERE, "Could not initialize Connection to database");
        }
    }

    public static void addSubmission(Submission submission, SubmissionId id) throws DatabaseException {
            try {
                logger.info("Adding submission to database");
                String su = submission.getPassedAttributes().get("submission");
                su = su.replaceAll("'", "");

                String query = "INSERT INTO submission VALUES(" +
                        "'" + id.getId() + "'" + ", " +
                        "'" + submission.getTaskType() + "'" + ", " +
                        submission.getExerciseId() + ", " +
                        submission.getUserId() + ", " +
                        "'" + submission.getPassedAttributes().get("action") + "'" + ", " +
                        "'" + submission.getPassedAttributes().get("diagnoseLevel") + "'" + ", " +
                        "'" + su + "'" + ", " +
                        submission.getMaxPoints() +
                        ");";

                PreparedStatement stmt = con.prepareStatement(query);
                stmt.executeUpdate();
                con.commit();
                logger.info("Finished adding submission to database");
            } catch (SQLException throwables) {
                logger.log(Level.SEVERE, "Could not add submission to database due to errors");
                try {
                    logger.info("Rolling back");
                    con.rollback();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Rollback failed");
                }
                throw new DatabaseException("Error while adding submission");
            }
    }

    public static void addGrading(RestGrading grading, SubmissionId id) throws DatabaseException {
            try {
                logger.info("Adding grading to database");
                String query = "INSERT INTO grading VALUES(" +
                        "'" + id.getId() + "', " +
                        grading.getMaxPoints() + ", " +
                        grading.getPoints() +
                        ");";

                PreparedStatement stmt = con.prepareStatement(query);
                stmt.executeUpdate();
                con.commit();
                logger.info("Finished adding grading to database");
            } catch (SQLException throwables) {
                logger.log(Level.SEVERE, "Could not add grading to database due to errors");
                try {
                    logger.info("Rolling back");
                    con.rollback();
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Rollback failed");
                }
                throw new DatabaseException("Error whil adding Grading");
            }
    }

    public static RestGrading getGrading(String submissionId) {
            try {
                String query = "SELECT * FROM grading WHERE submissionId = " + "'" + submissionId + "';";

                PreparedStatement stmt = con.prepareStatement(query);
                ResultSet resultSet = stmt.executeQuery();
                RestGrading grading = null;
                if (resultSet.next()) {
                    double maxPoints = resultSet.getDouble("maxPoints");
                    double points = resultSet.getDouble("points");
                    grading = new RestGrading(points, maxPoints);
                }
                return grading;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        return null;
    }
}
