package at.jku.dke.etutor;

import at.jku.dke.etutor.grading.ETutorGradingApplication;
import at.jku.dke.etutor.grading.service.DatabaseException;
import at.jku.dke.etutor.grading.service.SQLResourceService;
import at.jku.dke.etutor.grading.service.StatementValidationException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.IfProfileValue;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test-class for SQLResourceService.class
 */
@SpringBootTest(classes = ETutorGradingApplication.class)
@EnabledIfSystemProperty(named = "run_test", matches="true")
class SQLResourceServiceTest {
    @Autowired
    private SQLResourceService service;
    private final String schema = "Test_Schema";


    public SQLResourceServiceTest(){
    }

    @Test
    public void when_createSchemas_then_no_exception() {
        assertDoesNotThrow(()->service.createSchemas(schema));
        deleteSchema(schema);
    }

    @Test
    public void when_createSchemas_duplicate_then_exception(){
        createSchema(schema);
        assertThrows(DatabaseException.class, ()-> service.createSchemas(schema));
        deleteSchema(schema);
    }

    @Test
    public void when_createSchema_existent_but_capitalized_then_exception(){
        createSchema(schema);
        assertThrows(DatabaseException.class, ()->service.createSchemas(schema.toUpperCase()));
        deleteSchema(schema);
    }

    @Test
    public void when_deleteSchema_nonexistent_then_no_exception(){
        String schema = "aslddkcu";
        assertDoesNotThrow(()->service.deleteSchemas(schema));
    }

    @Test
    public void when_deleteSchema_then_no_exception(){
        createSchema(schema);
        assertDoesNotThrow(()->service.deleteSchemas(schema));
    }

    @Test
    public void when_deleteConnection_nonexistent_then_no_exception(){
        assertDoesNotThrow(()->service.deleteConnection(schema));
    }

    @Test
    public void when_deleteConnection_then_no_exception(){
        createSchema(schema);
        assertDoesNotThrow(()->service.deleteConnection(schema));
        deleteSchema(schema);
    }

    @Test
    public void when_createTables_false_statement_then_exception(){
        createSchema(schema);
        String query = "CREATE ROLE sudo WITH SUPERUSER";
        assertThrows(StatementValidationException.class, ()->service.createTables(schema, query));
        deleteSchema(schema);
    }

    @Test
    public void when_createTables_with_comments_then_exception(){
        createSchema(schema);
        String query = "CREATE ROLE sudo WITH SUPERUSER -- createtable";
        assertThrows(StatementValidationException.class, ()-> service.createTables(schema,query));
        deleteSchema(schema);
    }

    @Test
    public void when_createTables_with_comments2_then_exception(){
        createSchema(schema);
        String query = "CREATE ROLE sudo WITH SUPERUSER /*createtable*/";
        assertThrows(StatementValidationException.class, ()-> service.createTables(schema,query));
        deleteSchema(schema);
    }

    @Test
    public void when_createTables_schema_non_existent_then_exception(){
        String query = "CREATE TABLE main(id integer primary key)";
        assertThrows(DatabaseException.class, ()->service.createTables("non_existent", query));
    }

    @Test
    public void when_createTables_then_no_exception(){
        createSchema(schema);
        String query = "CREATE TABLE main(id integer primary key)";
        assertDoesNotThrow(()-> service.createTables(schema,query));
        deleteSchema(schema);
    }

    @Test
    public void when_deleteTables_non_existent_then_no_exception(){
        createSchema(schema);
        assertDoesNotThrow(()-> service.deleteTables(schema, "not_existent"));
        deleteSchema(schema);
    }


    @Test
    public void when_deleteTables_then_no_exception(){
        createSchema(schema);
        String query = "CREATE TABLE main(id integer primary key)";
        assertDoesNotThrow(()-> service.createTables(schema,query));
        assertDoesNotThrow(()->service.deleteTables(schema, "main"));
        deleteSchema(schema);
    }

    @Test
    public void when_insertDataSubmission_false_statement_then_exception(){
       createSchema(schema);
       String query = "CREAT ROLE sudo WITH SUPERUSER";
       assertThrows(StatementValidationException.class, ()->service.insertDataSubmission(schema, query));
       deleteSchema(schema);
    }

    @Test
    public void when_insertDataSubmission_with_comments_then_exception(){
        createSchema(schema);
        String query = "CREATE ROLE sudo WITH SUPERUSER -- insert into";
        assertThrows(StatementValidationException.class, ()->service.insertDataSubmission(schema, query));
        deleteSchema(schema);
    }


    @Test
    public void when_insertDataSubmission_with_comments2_then_exception(){
        createSchema(schema);
        String query = "CREATE ROLE sudo WITH SUPERUSER /*insert into*/";
        assertThrows(StatementValidationException.class, ()->service.insertDataSubmission(schema, query));
        deleteSchema(schema);
    }

    @Test
    public void when_insertDataSubmission_no_table_then_exception(){
        createSchema(schema);
        String query = "INSERT INTO main VALUES(1)";
        assertThrows(DatabaseException.class, ()->service.insertDataSubmission(schema, query));
        deleteSchema(schema);
    }

    @Test
    public void when_insertDataSubmission_then_no_exception(){
        createSchema(schema);
        String table = "CREATE TABLE main (id integer primary key)";
        assertDoesNotThrow(()->service.createTables(schema, table));
        String insert = "INSERT INTO main VALUES(1)";
        assertDoesNotThrow(()-> service.insertDataSubmission(schema, insert));
        deleteSchema(schema);
    }


    @Test
    public void when_insertDataDiagnose_then_no_exception(){
        createSchema(schema);
        String table = "CREATE TABLE main (id integer primary key)";
        assertDoesNotThrow(()->service.createTables(schema, table));
        String insert = "INSERT INTO main VALUES(1)";
        assertDoesNotThrow(()-> service.insertDataDiagnose(schema, insert));
        deleteSchema(schema);
    }

    @Test
    public void when_createExercise_then_no_exception(){
        createSchema(schema);
        assertDoesNotThrow(()->service.createExercise(schema, "SELECT * FROM MAIN"));
        assertDoesNotThrow(()->service.deleteConnection(schema));
        deleteSchema(schema);
    }

    @Test
    public void test_exercise_lifecycle(){
       createSchema(schema);
       int id = -1;
        try {
            assertDoesNotThrow(()->service.createTables(schema, "CREATE TABLE main(id integer primary key)"));
            assertDoesNotThrow(()->service.insertDataDiagnose(schema, "INSERT INTO main VALUES(1)"));
            assertDoesNotThrow(()->service.insertDataSubmission(schema, "INSERT INTO MAIN values(2)"));

            id = service.createExercise(schema, "SELECT * FROM MAIN");
            assertNotEquals(-1, id);
            int finalId = id;

            assertDoesNotThrow(()->service.updateExerciseSolution(finalId, "SELECT id FROM MAIN"));
            assertEquals("SELECT id FROM MAIN", service.getSolution(id));

            assertTrue(service.getHTMLTable("main").contains("id"));
            assertTrue(service.getHTMLTable("main").contains("1"));
            assertTrue(service.getHTMLTableByExerciseID(id, "main").contains("id"));
            assertTrue(service.getHTMLTableByExerciseID(id, "main").contains("1"));
            assertTrue(service.getHTMLTableByTaskGroup(schema, "main").contains("id"));
            assertTrue(service.getHTMLTableByTaskGroup(schema, "main").contains("1"));
            assertFalse(service.getHTMLTable("main").contains("2"));
            assertFalse(service.getHTMLTableByExerciseID(id, "main").contains("2"));
            assertFalse(service.getHTMLTableByTaskGroup(schema, "main").contains("2"));

            assertDoesNotThrow(()->service.deleteExercise(finalId));
            assertEquals("", service.getSolution(id));
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        assertDoesNotThrow(()->service.deleteConnection(schema));
        deleteSchema(schema);
    }






    private void createSchema(String s){
        try {
            service.createSchemas(s);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    private void deleteSchema(String s){
        try {
            service.deleteSchemas(s);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

}
