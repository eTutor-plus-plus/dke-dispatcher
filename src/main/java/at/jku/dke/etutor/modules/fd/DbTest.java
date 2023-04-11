package at.jku.dke.etutor.modules.fd;


import at.jku.dke.etutor.modules.fd.entities.Dependency;
import at.jku.dke.etutor.modules.fd.repositories.DependencyRepository;
import at.jku.dke.etutor.modules.fd.services.DependencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DbTest {
    public static void main(String[] args) throws SQLException {
        DependencyService dependencyService;

//        Connection connection = FdDataSource.getConnection();
//        Statement sta = connection.createStatement();

//
//        List<Dependency> dependencies = dependencyService.getAll();
//        for (Dependency item: dependencies ) {
//            System.out.println(item.getLeftSide());
//        }
    }

}
