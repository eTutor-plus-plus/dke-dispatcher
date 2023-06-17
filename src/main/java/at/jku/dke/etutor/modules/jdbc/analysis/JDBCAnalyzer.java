package at.jku.dke.etutor.modules.jdbc.analysis;

import at.jku.dke.etutor.modules.jdbc.*;
import at.jku.dke.etutor.modules.pm.plg.utils.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.util.logging.Level;

public class JDBCAnalyzer {

    private Logger logger;

    public JDBCAnalyzer(){
        super();

        try {
            this.logger = (Logger) LoggerFactory.getLogger(JDBCAnalyzer.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public synchronized JDBCAnalysis analyze(int userID, int exerciseID, String mode, JDBCFile file) throws Exception {
        DBAnalysis dbAnalysis;
        DBAnalyzer dbAnalyzer;
        JDBCAnalysis analysis;
        JDBCRuntimeAnalysis runtimeAnalysis = null;
        CompilationAnalysis compilationAnalysis;
        String beginMode = null;
        String endMode = null;
        String usr = null;
        String pwd = null;
        boolean userConnErr = false;

        //Connection endConnection = null;
        //Connection userConnection = null;
        //Connection beginConnection = null;

        analysis = new JDBCAnalysis();
        analysis.setSubmissionSuitsSolution(true);

        //COMPILE FILE
        this.logger.info("COMPILING FILE");
        compilationAnalysis = this.analyzeCompilation(userID, file);
        analysis.setCompilationAnalysis(compilationAnalysis);
        if (!compilationAnalysis.submissionSuitsSolution()) {
            analysis.setSubmissionSuitsSolution(false);
            return analysis;
        }

        if (mode.equals(JDBCConstants.MODE_PRACTISE)) {
            beginMode = JDBCHelper.EXERCISE_MODE_BEGIN;
            endMode = JDBCHelper.EXERCISE_MODE_END;
        } else if (mode.equals(JDBCConstants.MODE_SUBMIT)) {
            beginMode = JDBCHelper.EXERCISE_MODE_SHADOW_BEGIN;
            endMode = JDBCHelper.EXERCISE_MODE_SHADOW_END;
        }

        try (Connection beginConnection = JDBCHelper.getConnectionFor(exerciseID, beginMode);
             Connection endConnection = JDBCHelper.getConnectionFor(exerciseID, endMode);
             Connection userConnection = JDBCHelper.getConnection(usr, pwd)) {
            //CLEAR DB
            this.logger.info("CLEARING DATABASE");
            usr = DBUserAdmin.getAdmin().getUser();
            pwd = DBUserAdmin.getAdmin().getPassword(usr);

			/*
			//JDBC module occasionally gets stuck with connection pool, 
			//Java Thread Dump (Ctrl + Break) says that DBConnectionPool is locked on this line, 
			//perhaps problem with statements; if not closed by user they never get closed (2005-11-10)
			userConnection = DBConnectionPool.getPool().getConnection();
			 */
            DBHelper.clearDB(userConnection);

            //COPY DB
            this.logger.info("COPYING DATABASE");

            DBHelper.copyDB(beginConnection, userConnection);

            //EXECUTE FILE
            this.logger.info("EXECUTING JDBC PROGRAMM");
            runtimeAnalysis = this.analyzeExecution(userID, file, userConnection);
            analysis.setRuntimeAnalysis(runtimeAnalysis);
            if (!runtimeAnalysis.submissionSuitsSolution()) {
                analysis.setSubmissionSuitsSolution(false);
                return analysis;
            }
            //COMPARE DB
            this.logger.info("COMPARING DATABASES");
            dbAnalyzer = new DBAnalyzer();

            dbAnalysis = dbAnalyzer.analyze(endConnection, userConnection);
            analysis.setDBAnalysis(dbAnalysis);
            if (!dbAnalysis.submissionSuitsSolution()) {
                analysis.setSubmissionSuitsSolution(false);
                return analysis;
            }
        } catch (Exception e) {
            if (runtimeAnalysis != null) {
                runtimeAnalysis.setSubmissionSuitsSolution(false);
                runtimeAnalysis.setRuntimeException(e.getMessage());
                analysis.setRuntimeAnalysis(runtimeAnalysis);
            }
            analysis.setSubmissionSuitsSolution(false);
            return analysis;
        } finally {
            DBUserAdmin.getAdmin().releaseUser(usr);
        }
        return analysis;
    }

    private JDBCRuntimeAnalysis analyzeExecution(int userID, JDBCFile file, Connection userConnection) throws InterruptedException {
        String path;
        long execWait;
        JDBCRuntimeAnalysis analysis;

        analysis = new JDBCRuntimeAnalysis();
        analysis.setSubmissionSuitsSolution(true);

        //EXECUTING THE JDBC PROGRAM
        path = JDBCHelper.getProperties().getProperty("compilation_directory") +  "/A" + userID + "/" + file.getFilename().replaceAll(".java", ".class");
        execWait = Integer.valueOf(JDBCHelper.getProperties().getProperty("execution_wait")).longValue();


        JDBCRunner thread = null;
        try{
            thread = new JDBCRunner(userID, new File(path), userConnection, this, analysis);
            //this.running = true;
            thread.start();
            wait(execWait);
        } catch (InterruptedException ie){
            ie.printStackTrace();
            thread.interrupt();
            throw ie;
        } finally {
            if (thread.isAlive()){
                try {
                    thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (thread.isAlive()){
                thread.interrupt();
                analysis.setRuntimeException("Time out. The execution of your JDBC programm was stopped after " + execWait/1000 + " seconds.");
                analysis.setSubmissionSuitsSolution(false);
            }
        }

        JDBCHelper.getLogger().log(Level.INFO, "RUNTIME MESSAGES: " + analysis.getMessages());

        return analysis;
    }

    private CompilationAnalysis analyzeCompilation(int userID, JDBCFile file) throws Exception {

        int length;
        char[] buffer;
        File tempFile;
        Process compilationProcess;
        CompilationAnalysis compilationAnalysis;
        StringBuffer commandLineMessage;
        StringBuffer compilationCommand;
        BufferedReader errorStreamReader;

        compilationAnalysis = new CompilationAnalysis();
        compilationAnalysis.setSubmissionSuitsSolution(true);

        //CREATE FILE IN FILE SYSTEM
        tempFile = new File(JDBCHelper.getProperties().getProperty("compilation_directory") + "/A" + userID + "/" + file.getFilename());

        try {
            if (!tempFile.getParentFile().exists()) {
                tempFile.getParentFile().mkdirs();
            }
            tempFile.createNewFile();
        } catch (IOException ex) {
            JDBCHelper.getLogger().log(Level.SEVERE, "Unable to CREATE file: " + tempFile.getPath() + " for JDBC compilation!", ex);
            throw new Exception("Unable to CREATE file: " + tempFile.getPath() + " for JDBC compilation!", ex);
        }

        String fileContent = file.getContent();
        String packageDecl;
        FileOutputStream fout = null;
        ByteArrayOutputStream out = null;

        //SET PACKAGE STATEMENT
        try{
			/*InputStreamReader inStream = new InputStreamReader(file.getContent().getInputStream());
			out = new ByteArrayOutputStream();
			out.write(inStream.read());
			fileContent = out.toString();

			BufferedReader br = new BufferedReader(new InputStreamReader(file.getContent().getInputStream()));
			String thisLine;
			while ((thisLine = br.readLine()) != null) {
				fileContent = fileContent.concat(thisLine);
			}*/

            packageDecl = "package A" + userID + ";\n";

            if (fileContent.matches("(?idmsux).*package.*;.*")){
                fileContent = fileContent.replaceAll("package.*;", packageDecl);
            } else {
                fileContent = packageDecl + fileContent;
            }
        } catch (Exception e){
            throw e;
        } finally {
            try{
                if (out != null){
                    out.close();
                }
            } catch (Exception ignore){
                JDBCHelper.getLogger().log(Level.WARNING, "", ignore);
            }
        }

        //COPY FILE CONTENT
        try{
            fout = new FileOutputStream(tempFile);
            fout.write(fileContent.getBytes());
        } catch (Exception e){
            throw e;
        } finally {
            try{
                if (fout != null){
                    fout.close();
                }
            } catch (Exception ignore){
                JDBCHelper.getLogger().log(Level.WARNING, "", ignore);
            }
        }

        //PREPARE COMPILATION COMMAND
        compilationCommand = new StringBuffer();
        compilationCommand.append("javac ");
        compilationCommand.append(" -d " + JDBCHelper.getProperties().getProperty("compilation_directory"));
        compilationCommand.append(" -classpath " + JDBCHelper.getProperties().getProperty("classpath"));
        compilationCommand.append(" " + tempFile.getCanonicalPath());

        JDBCHelper.getLogger().log(Level.INFO, "Compilation command: " + compilationCommand.toString());

        //EXECUTE COMPILATION COMMAND
        errorStreamReader = null;
        compilationProcess = null;
        commandLineMessage = new StringBuffer();

        try {
            compilationProcess = Runtime.getRuntime().exec(compilationCommand.toString());
            errorStreamReader =	new BufferedReader(new InputStreamReader(compilationProcess.getErrorStream()),4096);

            length = 0;
            buffer = new char[2048];
            while ((length = errorStreamReader.read(buffer)) != -1) {
                commandLineMessage.append(buffer,0,length);
            }

            if (commandLineMessage.toString().trim().length() != 0) {
                JDBCHelper.getLogger().log(Level.WARNING, "Exception while compiling file on command line!\n" + commandLineMessage.toString());
                compilationAnalysis.setErrorMessage(commandLineMessage.toString());
                compilationAnalysis.setSubmissionSuitsSolution(false);
            }
        } catch (IOException ex) {
            JDBCHelper.getLogger().log(Level.SEVERE, "Exception while executing compilation process!", ex);
            throw new Exception("Exception while executing compilation process!", ex);
        } finally {
            try {
                if (errorStreamReader != null){
                    errorStreamReader.close();
                }
            } catch (Exception ignore) {
                JDBCHelper.getLogger().log(Level.SEVERE, "Unable to close command line input stream!", ignore);
                throw new Exception("Unable to close command line input stream!", ignore);
            }
        }

        return compilationAnalysis;
    }
}
