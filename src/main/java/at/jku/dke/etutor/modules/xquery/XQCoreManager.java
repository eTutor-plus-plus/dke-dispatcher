package at.jku.dke.etutor.modules.xquery.src.etutor.modules.xquery;

import at.jku.dke.etutor.modules.xquery.src.etutor.modules.xquery.util.PropertyFile;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * This class serves as central manager of all required resources and information which have to be
 * collected in order to set up an XQuery exercise correctly.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class XQCoreManager {

    private PropertyFile propertyFile;
    private DataSource dataSource;
    private Context ctx;
    private static final Logger LOGGER = initLogger();
    private static final String LINE_SEP = System.getProperty("line.separator", "\n");
    
    /**
     * The <i>singleton </i> of this class.
     */
    private static XQCoreManager coreManager;

    /**
     * Location of the folder for temp files which are created when analyzing XQuery queries.
     */
    public final static String TEMP_FOLDER = "/etutor/resources/modules/xquery/temp";

    /**
     * Location of the configuration file for logging
     */
    public final static String LOG4J_PROPERTIES = "/etutor/resources/modules/xquery/properties/log4j.properties";

    /**
     * Location of the datasource within the JNDI context which is used for retrieving pooled connections
     */
    public final static String NAMING_DATASOURCE = "xq.java.naming.datasource";
    
    /**
     * Location of the properties file from which to get basic information for setting up a XQuery
     * exercise.
     */
    public final static String PROPERTIES_FILE = "/etutor/resources/modules/xquery/properties/xquery.properties";

    /**
     * Relative path of the XSL stylesheet, which is required for rendering the XML representation
     * of an XQuery result or the analysis of this result in HTML format.
     */
    public final static String XSL_RENDER_XQ = "/etutor/resources/modules/xquery/xml/render-xquery.xsl";

    /**
     * Relative path of the XSL stylesheet, which is required for modifying the XSL stylesheet,
     * which depicts the differences between a "correct" query result and a "submitted" query
     * result. The XSL stylesheet is customized so that error information can be included and
     * transformed automatically and directly into HTML format.
     */
    public final static String XSL_MODIFY = "/etutor/resources/modules/xquery/xml/modify-xmlDiff.xsl";

    /**
     * This denotes a predefined relative path to an XML API used by the Schema generating tool
     * <i>DDBE </i>.
     * <p>
     * This library is required to be loaded with regard to the reflection concept, using a separate
     * class loader in order to prevent version conflicts with other libraries used within this
     * module.
     * 
     * @see #JAR_DDBE
     */
    public final static String JAR_XERCES = "/etutor/resources/modules/xquery/reflect/xerces.jar";

    /**
     * This denotes a predefined relative path to the Schema generating tool <i>DDBE </i>, which is
     * used for generating an XML Schema or a DTD from an XML document.
     * <p>
     * This library is required to be loaded with regard to the reflection concept, using a separate
     * class loader in order to prevent version conflicts with other libraries used within this
     * module.
     * 
     * @see #JAR_XERCES
     */
    public final static String JAR_DDBE = "/etutor/resources/modules/xquery/reflect/DDbE.jar";

    /**
     * Path of the file which represents the XML Schema used for validating XML configuration files
     * for initializing {@link etutor.modules.xquery.grading.XQGradingConfig} objects.
     */
    public final static String XSD_FILE_SCORES = "/etutor/resources/modules/xquery/xml/xquery-score.xsd";

    /**
     * Property key for the database table name which holds the exercise definitions.
     */
    public final static String KEY_TABLE_EXERCISE = "table.exercise";

    /**
     * Property key for the database table name which holds single entries of urls to XML files,
     * which can be used for an XQuery query. These entries consist of pairs of URLs and alias names
     * for these URLs.
     */
    public final static String KEY_TABLE_URLS = "table.urls";

    /**
     * Property key for the database table name which holds single XPath nodes, which are required
     * to be in a certain order according to some exercise.
     */
    public final static String KEY_TABLE_SORTINGS = "table.sortings";

    /**
     * Property key for the database table name which holds the definitions of error categories.
     */
    public final static String KEY_TABLE_ERROR_CATEGORIES = "table.error.categories";

    /**
     * Property key for the database table name which holds the grading information of a single
     * error category, specifying the minus points and how often this error is considered if it
     * occurs more than once.
     */
    public final static String KEY_TABLE_ERROR_GRADING = "table.error.grading";

    /**
     * Property key which indicates whether the module is run in debug modus. The implication of
     * running in debug modus is that intermediate results of the analyzation process are saved to
     * files like XML, XSL and HTML documents.
     */
    public final static String MODUS_DEBUG = "modus.debug";

    /**
     * Constructs an instance of this class, which is intended to be called only once for creating
     * the <i>singleton </i>.
     */
    private XQCoreManager() {
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
     * @return The <code>XQCoreManager </code> singleton.
     */
    public synchronized static XQCoreManager getInstance() {
        if (coreManager == null) {
            coreManager = new XQCoreManager();
        }
        return coreManager;
    }

    /**
     * Gets the property file according to this <code>XQCoreManager </code>.
     * 
     * @return the properties file object related to this <code>XQCoreManager </code>.
     * @throws InvalidResourceException if the properties file can not be found.
     */
    public PropertyFile getPropertyFile() throws InvalidResourceException {
        return this.getPropertyFile(false);
    }

    /**
     * Gets the property file according to this <code>XQCoreManager </code>.
     * 
     * @param reload A flag which indicates whether the underlying properties file which is related
     *            to this <code>XQCoreManager </code> is newly loaded.
     * @return the properties file object related to this <code>XQCoreManager </code>.
     * @throws InvalidResourceException if the properties file can not be found.
     */
    public PropertyFile getPropertyFile(boolean reload) throws InvalidResourceException {
        if (propertyFile == null || reload) {
            propertyFile = new PropertyFile(this.getClass(), PROPERTIES_FILE);
        }
        return propertyFile;
    }

    /**
     * <p>
     * Tries to find the specified resource. First of all the <code>getResource(resource)</code>
     * method is called from the <code>class</code> object of the <code>XQCoreManager</code>
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
        URL url = XQCoreManager.class.getResource(resource);
        URL baseURL = null;
        if (url == null) {
        	// get the base directory
        	url = XQCoreManager.class.getResource("/.");
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
    	msg = "Setting up XQ data source mapped to " + dataSourceName + " ...";
    	LOGGER.info(msg);
		return (DataSource)ctx.lookup(dataSourceName);
    }
    
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
     */
    /*
    private Connection createConnection(String dbDriver, String dbUrl, String dbUser, String dbPwd)
            throws ClassNotFoundException, SQLException {
        Class.forName(dbDriver);
        Connection con = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
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
     */
    /*
    public Connection getConnection(boolean reload) throws ClassNotFoundException, SQLException,
            InvalidResourceException {
        //TODO: newConnection
        if (connection == null || reload) {
            String dbDriver = getDbDriver(true);
            String dbUrl = getDbUrl(true);
            String dbUser = getDbUser(true);
            String dbPwd = getDbPwd(true);
            connection = createConnection(dbDriver, dbUrl, dbUser, dbPwd);
        }
        return connection;
    }
    */

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
    	URL logConfig;
    	logger = Logger.getLogger(XQCoreManager.class);
		try {
			logConfig = getResource(LOG4J_PROPERTIES);
            PropertyConfigurator.configure(logConfig);
            msg = new String();
        	msg += "Main logger initialized with basic configuration parameters from ";
        	msg += logConfig.toString();
            logger.info(msg);
		} catch (InvalidResourceException e) {
            BasicConfigurator.configure(new NullAppender());
            msg = "No log4j configuration file found at " + LOG4J_PROPERTIES;            
            logger.warn(msg);			
		}
		
		return logger;
    }
}