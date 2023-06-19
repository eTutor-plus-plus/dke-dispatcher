package at.jku.dke.etutor.modules.jdbc.analysis;

import at.jku.dke.etutor.modules.jdbc.JDBCHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCRunner extends Thread {

    private File file;
    private Connection con = null;
    private JDBCAnalyzer analyzer;
    private JDBCRuntimeAnalysis runtimeAnalysis;
    private JDBCExecutor programm;
    private int userID;

    private Logger logger;

    public JDBCRunner(int userID, File file, Connection con, JDBCAnalyzer analyzer, JDBCRuntimeAnalysis runtimeAnalysis) {
        super();

        this.userID = userID;
        this.con = con;
        this.file = file;
        this.analyzer = analyzer;
        this.runtimeAnalysis = runtimeAnalysis;
        this.runtimeAnalysis.setSubmissionSuitsSolution(true);

        try {
            this.logger = Logger.getLogger("etutor.modules.jdbc");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {

        PrintStream stream = null;
        ByteArrayOutputStream out = null;

        try {
            out = new ByteArrayOutputStream();
            stream = new PrintStream(out);

            int begin;
            String fileName;
            SecurityManager oldManager = System.getSecurityManager();

            begin = this.file.getParent().lastIndexOf("\\") + 1;
            fileName = this.file.getParent().substring(begin, this.file.getParent().length()) + "." + this.file.getName().replaceAll(".class", "");

            ClassLoader c =null;
            try {
                JDBCHelper.getLogger().log(Level.INFO, "Trying to instantiate File: " + fileName);
                c = this.getContextClassLoader();
                this.setContextClassLoader(new MyClassLoader(file, fileName, c));
                //WebappClassLoader loader = (WebappClassLoader) this.getContextClassLoader();
                //Class theClass = loader.findClass(fileName);
                Class theClass = this.getContextClassLoader().loadClass(fileName);
                logger.log(Level.INFO, "Class file: " + theClass);
                programm = (JDBCExecutor) theClass.newInstance();
                logger.log(Level.INFO, "Starting Programm execution.");

                System.setSecurityManager(new JDBCSecurityManager(JDBCHelper.getProperties().getProperty("compilation_directory") /*+  "/A" + userID*/, JDBCHelper.getProperties().getProperty("lib_directory")));
                programm.execute(con, stream);
                System.setSecurityManager(oldManager);
                this.runtimeAnalysis.setMessages(out.toString());
                logger.log(Level.INFO, "Finished Programm execution.");
                this.setContextClassLoader(c);

                //Class.forName(fileName,true,new MyClassLoader()).newInstance();
            } catch (Exception e) {
                this.runtimeAnalysis.setSubmissionSuitsSolution(false);
                this.runtimeAnalysis.setRuntimeException(prepareMessage(e));
                this.runtimeAnalysis.setMessages(out.toString());
            } finally {
                System.setSecurityManager(oldManager);
            }

        } catch (Exception e) {
            this.runtimeAnalysis.setSubmissionSuitsSolution(false);
            this.runtimeAnalysis.setRuntimeException(prepareMessage(e));
            logger.log(Level.WARNING, "JDBC RUNTIME EXCEPTION", e);
        }	finally {
            if (stream != null){
                try {
                    stream.close();
                } catch (Exception ignore){
                    JDBCHelper.getLogger().log(Level.WARNING, "", ignore);
                }
            }
            if (out != null){
                try{
                    out.close();
                } catch (Exception ignore){
                    JDBCHelper.getLogger().log(Level.WARNING, "", ignore);
                }
            }
        }

        this.logger.log(Level.INFO, "TRYING TO NOTIFY ANALYZER");

        synchronized (this.analyzer) {
            //this.analyzer.signalExecutionEnd();
            this.analyzer.notify();
            this.logger.log(Level.INFO, "NOTIFIED ANALYZER");
        }
    }

    public String prepareMessage(Exception e){
        StringBuffer message = new StringBuffer();
        StackTraceElement[] stackTrace = e.getStackTrace();

        //APPENDING EXCEPTION CLASS NAME
        message.append(e.getClass().getName() + "\n");

        //APPENDING EXCEPTION MESSAGE
        if (e.getLocalizedMessage() != null){
            message.append(e.getLocalizedMessage());
        }

        //APPENDING STACK TRACE ELEMENTS
        for (int i=0; i<stackTrace.length; i++){
            message.append("at " + stackTrace[i].toString() + "\n");
        }

        return message.toString();
    }
}

class MyClassLoader extends ClassLoader {
    byte[] classFile = null;
    File file;
    String name;
    ClassLoader parent;

    public MyClassLoader(File file, String name, ClassLoader parent) {
        this.file = file;
        this.name = name;
        this.parent = parent;
    }

    public Class findClass(String name) throws ClassNotFoundException {
        System.out.println("CLASS TO LOAD: " + name);
        if (!name.equalsIgnoreCase(this.name)) return parent.loadClass(name);
        try {
            classFile = new byte[(int) file.length()];
            FileInputStream in = new FileInputStream(file);
            int read = in.read(classFile);
            System.out.println(classFile);
            return defineClass(this.name, classFile, 0, read);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClassNotFoundException(name);
        }
    }
}