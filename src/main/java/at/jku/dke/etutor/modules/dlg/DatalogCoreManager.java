package at.jku.dke.etutor.modules.dlg;

import at.jku.dke.etutor.modules.dlg.util.PropertyFile;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

/**
 * This class serves as central manager of all required resources and information which have to be
 * collected in order to set up a datalog exercise correctly.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class DatalogCoreManager {

    private PropertyFile propertyFile;
    private DataSource dataSource;
    private Context ctx;
    private static final Logger LOGGER = initLogger();
    private static final String LINE_SEP = System.getProperty("line.separator", "\n");
    
    /**
     * The <i>singleton</i> of this class.
     */
    private static DatalogCoreManager coreManager;
    
    /**
     * Location of the folder for temp files which are created when analyzing Datalog queries.
     */
    public final static String TEMP_FOLDER = "C:/apache-tomcat-6.0.24/lib/";

    /**
     * Location of the configuration file for logging
     */
    public final static String LOG4J_PROPERTIES = "/etutor/resources/modules/datalog/properties/log4j.properties";

    /**
     * Location of the datasource within the JNDI context which is used for retrieving pooled connections
     */
    public final static String NAMING_DATASOURCE = "dlg.java.naming.datasource";
    
    /**
     * Location of the properties file from which to get basic information for setting up a datalog
     * exercise.
     */
    public final static String PROPERTIES_FILE = "/etutor/resources/modules/datalog/properties/datalog.properties";

    /**
     * Path of the file which represents the XML Schema used for validating XML configuration files
     * for initializing {@link at.jku.dke.etutor.modules.dlg.grading.DatalogScores} objects.
     */
    public final static String XSD_FILE_SCORES = "/etutor/resources/modules/datalog/xml/datalog-score.xsd";

    /**
     * Relative path of the XSL stylesheet, which is required for rendering the XML representation
     * of a Datalog result or the analysis of this result in HTML format.
     */
    public final static String XSL_RENDER_DATALOG = "/etutor/resources/modules/datalog/xml/render-datalog.xsl";

    /**
     * Property key for the executable file of the datalog processor.
     */
    public final static String KEY_EXE = "datalog.exe";

    /**
     * Property key for the time limit for processing queries, which is interpreted as milliseconds
     * given by a <code>long</code> number.
     */
    public final static String KEY_TIMEOUT = "datalog.timeout";

    /**
     * Property key for the interval, which indicates the period of checking whether the processor
     * has finished analyzing the query. This is also interpreted as milliseconds given by a
     * <code>long</code> number.
     */
    public final static String KEY_INTERVAL = "datalog.interval";

    /**
     * Property key for the database table name which holds the exercise definitions.
     */
    public final static String KEY_TABLE_EXERCISE = "table.exercise";

    /**
     * Property key for the database table name which holds the names of predicates which 
     * are part of a collection of predicates. These predicates can be defined to be required in some
     * concrete exercise.
     */
    public final static String KEY_TABLE_PREDICATES = "table.predicates";

    /**
     * Property key for the database table name which holds the filenames of datalog database files, providing
     * the facts for datalog queries.
     */
    public final static String KEY_TABLE_FACTS = "table.facts";

    /**
     * Property key for the database table name which holds the definitions of error categories.
     */
    public final static String KEY_TABLE_ERROR_CATEGORIES = "table.error.categories";

    /**
     * Property key for the database table name which holds the grading information of a single error
     * category, specifying the minus points and how often this error is
     * considered if it occurs more than once.
     */
    public final static String KEY_TABLE_ERROR_GRADING = "table.error.grading";
    
    /**
     * Property key for the database table name which contains information about which terms of which
     * predicate at which position are not encrypted, so that users can put these constants into 
     * their datalog rules without any problem.
     */
    public final static String KEY_TABLE_TERMS_UNCHECKED = "table.terms.encryption";    

    /**
     * Property key for the highest integer which should be considered when processing datalog
     * queries. This is relevant when arithmetic functions are part of querying, dealing with
     * numbers. If the processor comes across any number which exceeds the defined highest integer,
     * an exception is thrown which is wrapped in a {@link at.jku.dke.etutor.modules.dlg.QuerySyntaxException} and is
     * therefor treated as if the query contains syntax errors.
     */
    public final static String KEY_MAX_INT = "datalog.max.int";

    /**
     * Property key which indicates whether the module is run in debug modus. The implication of
     * running in debug modus is that intermediate results of the analyzation process are saved to
     * files like XML, XSL and HTML documents.
     */
    public final static String KEY_MODUS_DEBUG = "modus.debug";

    /**
     * Constructs an instance of this class, which is intended to be called only once for 
     * creating the <i>singleton</i>.
     */
    private DatalogCoreManager() {
    	init();
    }

    private void init() {
    	String msg;
    	msg = "Initializing " + this.getClass().getName() + " singleton ...";
		LOGGER.info(msg);
    	try {
			this.propertyFile = getPropertyFile();
		} catch (InvalidResourceException e) {
			msg = "Initializing properties file failed.";
			LOGGER.warn(msg, e);
		}
    	try {
			this.ctx = getContext();
		} catch (Exception e) {
			msg = "Initializing JNDI context failed.";
			LOGGER.warn(msg, e);
		}
    	try {
			this.dataSource = this.ctx != null ? getDataSource(this.ctx) : null;
		} catch (Exception e) {
			msg = "Initializing JDBC data source failed.";
			LOGGER.warn(msg, e);
		}
    }
    
    /**
     * Gets the only instance of this class.
     * 
     * @return The <code>DatalogCoreManager </code> singleton.
     */
    public synchronized static DatalogCoreManager getInstance() {
    	if (coreManager == null) {
            coreManager = new DatalogCoreManager();
        }
        return coreManager;
    }

    /**
     * Gets the property file according to this <code>DatalogCoreManager </code>.
     * 
     * @return the properties file object related to this <code>DatalogCoreManager </code>.
     * @throws InvalidResourceException if the properties file can not be found.
     */
    public PropertyFile getPropertyFile() throws InvalidResourceException {
        return this.getPropertyFile(false);
    }
    
    /**
     * Gets the property file according to this <code>DatalogCoreManager </code>.
     * 
     * @param reload A flag which indicates whether the underlying properties file which is related
     *            to this <code>DatalogCoreManager </code> is newly loaded.
     * @return the properties file object related to this <code>DatalogCoreManager </code>.
     * @throws InvalidResourceException if the properties file can not be found.
     */
    public synchronized PropertyFile getPropertyFile(boolean reload) throws InvalidResourceException {
        if (propertyFile == null || reload) {
            propertyFile = new PropertyFile(this.getClass(), PROPERTIES_FILE);
        }
        return propertyFile;
    }
    
    /**
     * <p>
     * Tries to find the specified resource. First of all the <code>getResource(resource)</code>
     * method is called from the <code>class</code> object of the <code>DatalogCoreManager</code>
     * instance. If the strategy followed there is not successful, the specified resource is
     * searched by going up the filesystem to the root iteratively. 
     * </p>
     * <p>
     * This method should provide a central way of accessing files for the whole module. 
     * Only the static fields of this class should be passed as parameters (easier maintenance
     * if these paths must be adapted).
     * </p> 
     * 
     * @param resource 
     * @return the resource as URL
     * @throws InvalidResourceException if the resource can not be found
     */
    public static URL getResource(String resource) throws InvalidResourceException {
        URL url = DatalogCoreManager.class.getResource(resource);
        URL baseURL = null;
        if (url == null) {
        	// get the base directory
        	url = DatalogCoreManager.class.getResource("/.");
        	// System.out.println("root: " + url.getPath() + "(" + url.getPath() + ")");
        	if (url != null) {
        		File dir = new File(url.getFile());
        		File file = null;
        		String parent = null;
        		baseURL = url;
        		url = null;        		
        		while (url == null && (parent = dir.getParent()) != null) {
        			// System.out.println("moving up: " + parent);
        			dir = new File(parent);
        			file = new File(parent, resource);
        			if (file.exists()) {
        				try {
							url = file.toURL();
						} catch (MalformedURLException e) {
						}
        			}
        		}
        	}
        }
        if (url == null) {
        	String msg = "Resource not found: " + resource;
        	if (baseURL != null) {
        		if (resource != null && resource.startsWith("/")) {
        			msg += "\nExpected at " + baseURL.getFile();
        			msg += " or anywhere up the directory path.";
        		} else {
        			msg += "\nExpected at " + baseURL.getFile();
        			msg += " or anywhere up or down the directory path.";        			
        		}
        	}
        	throw new InvalidResourceException(msg);
        }
        return url;
    }    

    /**
     * Gets the JNDI context for retrieving resources configured in this context.
     * 
     * @return the JNDI context
     * @throws InvalidResourceException if the properties file needed for 
     * establishing the JNDI context can not be found
     */
    private synchronized Context getContext() throws InvalidResourceException, NamingException {
    	String msg;
    	PropertyFile propertyFile;
        ClassLoader oldClassLoader;
		ClassLoader newClassLoader;
		Hashtable table;

		propertyFile = this.getPropertyFile();
        if (this.ctx == null) {
        	msg = "Setting up JNDI context ...";
        	LOGGER.info(msg);
        	//Set classloader to avoid problems with classes not found when datalog 
        	//module is initiated by the RMI server module
    		oldClassLoader = Thread.currentThread().getContextClassLoader();
    		newClassLoader = this.getClass().getClassLoader();
            Thread.currentThread().setContextClassLoader(newClassLoader);
			this.ctx = new InitialContext(propertyFile);
			table = this.ctx.getEnvironment();
			
			msg = "Main environment properties effectively set for JNDI context: " + LINE_SEP;
			msg += Context.INITIAL_CONTEXT_FACTORY + "=";
			msg += table.get(Context.INITIAL_CONTEXT_FACTORY) + LINE_SEP;
			msg += Context.PROVIDER_URL + "=";
			msg += table.get(Context.PROVIDER_URL) + LINE_SEP;
			LOGGER.info(msg);
    		Thread.currentThread().setContextClassLoader(oldClassLoader);
        }
        
        return this.ctx;
    }
    
    /**
     * Gets the datasource for retrieving connections out of a connection pool
     * which is configured in the JNDI context.
     * 
     * @return the datasource
     * @throws InvalidResourceException if the properties file can not be found
     * @throws NamingException exception thrown when creating the JNDI context or 
     * looking up the data source fails
     */
    private synchronized DataSource getDataSource() throws InvalidResourceException, NamingException {
    	Context ctx;
        if (this.dataSource == null) {
        	ctx = this.getContext();
        	this.dataSource = ctx != null ? getDataSource(ctx) : null;
        }
        return this.dataSource;
    }
    
    /**
     * Gets the datasource for retrieving connections out of a connection pool
     * which is configured in the JNDI context.
     * 
     * @return the datasource
     * @throws InvalidResourceException if the properties file can not be found
     * @throws NamingException exception thrown when looking up the data source fails
     */
    private synchronized DataSource getDataSource(Context ctx) throws InvalidResourceException, NamingException {
    	String msg;
    	String dataSourceName;
    	PropertyFile propertyFile;

		propertyFile = this.getPropertyFile();
    	dataSourceName = propertyFile.getProperty(NAMING_DATASOURCE);
    	msg = "Setting up JDBC data source mapped to " + dataSourceName + " ...";
    	LOGGER.info(msg);
		return (DataSource)ctx.lookup(dataSourceName);
    }
    
    /*
     * Creates a new connection to the database which contains exercise information.
     * 
     * @param dbDriver The database driver.
     * @param dbUrl The database url.
     * @param dbUser The database user.
     * @param dbPwd The database password.
     * @return The newly created database connection.
     * @throws ClassNotFoundException if the specified database driver can not be found.
     * @throws SQLException if an SQLException occured when trying to establish the connection.
     * @deprecated connections are retrieved using a datasource configured in JNDI context
     */
    /*
    private Connection createConnection(String dbDriver, String dbUrl, String dbUser, String dbPwd)
            throws ClassNotFoundException, SQLException {
        Class.forName(dbDriver);
        Connection con = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        con.setAutoCommit(true);
        return con;
    }
    */

    /*
     * Gets the connection to the database which contains exercise definitions.
     * 
     * @param reload A flag which indicates if the connection and all according parameters, like
     *            database url ... should be reloaded from the properties file.
     * @return Returns the connection.
     * @throws ClassNotFoundException if the database driver specified in the properties file can
     *             not be found.
     * @throws SQLException if an SQLException occured when trying to establish the connection.
     * @throws InvalidResourceException if the properties file misses some required properties or
     *             values of properties are not applicable.
     * @deprecated connections are retrieved using a datasource configured in JNDI context
     */
    /*
    public synchronized Connection getConnection(boolean reload) throws ClassNotFoundException, SQLException,
            InvalidResourceException {
    	String dbDriver;
        String dbUrl;
        String dbUser;
        String dbPwd;
    	PropertyFile propertyFile;
        if (this.connection == null || reload) {
        	propertyFile = this.getPropertyFile(false);
            dbDriver = propertyFile.loadProperty(KEY_DB_DRIVER);
            dbUrl = propertyFile.loadProperty(KEY_DB_URL);
            dbUser = propertyFile.loadProperty(KEY_DB_USER);
            dbPwd = propertyFile.loadProperty(KEY_DB_PWD);
            this.connection = createConnection(dbDriver, dbUrl, dbUser, dbPwd);
        }
        return this.connection;
    }
    */
    
    /**
     * Gets a connection from a connection pool which is configured in the JNDI context.
     * The retrieved connection must be closed explicitly after handling, in order to release 
     * it for further usage.
     * @return the pooled connection
     * @throws NamingException indicates that the JNDI context is not configured correctly
     * @throws InvalidResourceException indicates that properties needed for retrieving
     * the JNDI context are not set correctly
     * @throws SQLException is thrown when trying to retrieve a connection out of the pool
     */
    public Connection getConnection() throws NamingException, InvalidResourceException, SQLException {
    	DataSource ds;
        ds = getDataSource();
        return ds.getConnection();
    }

    /**
     * Sets up the logger used for logging within the datalog module. As configuration of
     * LOG4J logging depends on this method, logging will not work as desired until this
     * method is invoked. Thus, invocation should be included in entry point(s) of the
     * datalog module. Alternative mechanisms might be used for initialization, e.g. setting
     * the log4j.configuration system property with the configuration file or placing
     * log4j.properties into the working directory. 
     * 
     * @return the initialized <code>Logger</code>.
     * @see #LOG4J_PROPERTIES
     */
    private static Logger initLogger() {
        Logger logger;
    	String msg;
    	logger = (Logger) LoggerFactory.getLogger(DatalogCoreManager.class);
        msg = new String();
        msg += "Main logger initialized with basic configuration parameters from ";
        logger.info(msg);

        return logger;
    }
}