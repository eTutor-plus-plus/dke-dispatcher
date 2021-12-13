package at.jku.dke.etutor.modules.dlg;

import at.jku.dke.etutor.modules.dlg.util.PropertyFile;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
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
    public final static String PROPERTIES_FILE = "/dlg/properties/datalog.properties";

    /**
     * Path of the file which represents the XML Schema used for validating XML configuration files
     * for initializing {@link at.jku.dke.etutor.modules.dlg.grading.DatalogScores} objects.
     */
    public final static String XSD_FILE_SCORES = "/dlg/xml/datalog-score.xsd";

    /**
     * Relative path of the XSL stylesheet, which is required for rendering the XML representation
     * of a Datalog result or the analysis of this result in HTML format.
     */
    public final static String XSL_RENDER_DATALOG = "/dlg/xml/render-datalog.xsl";

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
     * Gets a connection from a connection pool .
     * @return the pooled connection
     * @throws SQLException is thrown when trying to retrieve a connection out of the pool
     */
    public Connection getConnection() throws SQLException {
    	return DatalogDataSource.getConnection();
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