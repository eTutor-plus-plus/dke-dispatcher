package at.jku.dke.etutor.modules.jdbc.analysis;

import java.io.PrintStream;
import java.sql.Connection;

/**
 * Interface to be implemented by students for a given <code>JDBC</code> task assignment.
 * An implementation can be submitted by the student to the <code>eTutor</code> system
 * in order to be executed and evaluated.
 */
public interface JDBCExecutor{

    /**
     * This method will be executed by the <code>eTutor</code> system for each
     * <code>JDBCExecutor</code> implementation, which is submitted by a student.
     * When implementing this method, any database operations should be performed
     * in order to fulfill requirements of a given <code>JDBC</code> task assignment.
     * These operations must be performed, using the given <code>JDBC</code> connection
     * instance. <code>NOTE</code>: Do <b>not</b> close the connection in your
     * implementation. Otherwise, the <code>eTutor</code> system will not be able to
     * evaluate the program properly.
     *
     * @param 	con
     * 			The <code>JDBC</code> connection instance, which must be used for
     * 			performing required database operations.
     * @param 	out
     * 			A stream, which can be used for writing arbitrary messages.
     * @throws 	Exception
     * 			Any exception, which occurs when executing operations inside of this method.
     */
    public void execute(Connection con, PrintStream out) throws Exception;
}
