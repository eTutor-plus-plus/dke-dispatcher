package at.jku.dke.etutor.grading.service;

import at.jku.dke.etutor.grading.config.ApplicationProperties;
import at.jku.dke.etutor.modules.rt.RTDataSource;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class RTResourceService {
    private ApplicationProperties properties;

    public RTResourceService(ApplicationProperties properties) {
        this.properties = properties;
    }

    public Long insertTask(String solution) throws SQLException {
        Long generatedID = null;
        RTDataSource dataSource = new RTDataSource(this.properties);
        Connection con = dataSource.getConnection();
        try (con) {
            String insertQuery = "INSERT INTO rt.public.tasks (solution) VALUES (?) RETURNING id";
            try (PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, solution);
                if (preparedStatement.execute()) {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            generatedID = resultSet.getLong("id");
                        }
                    }
                }
            }
            con.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        con.close();
        return generatedID;
    }

}
