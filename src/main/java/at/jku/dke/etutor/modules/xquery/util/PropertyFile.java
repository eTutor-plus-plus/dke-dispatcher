package at.jku.dke.etutor.modules.xquery.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import at.jku.dke.etutor.modules.xquery.InvalidResourceException;
import at.jku.dke.etutor.modules.xquery.XQCoreManager;
import org.apache.log4j.Logger;


/**
 * Utility class which extends the basic {@link java.util.Properties}with tailored methods for
 * retrieving properties from a file.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class PropertyFile extends Properties {

    private String file;
    private ResourceBundle bundle;
    private static final Logger LOGGER = Logger.getLogger(PropertyFile.class);
    
    /**
     * Constructs a new PropertyFile.
     * 
     * @param owner A class which provides the <code>ClassLoader</code> to load the specified
     *            resource from (see {@link java.lang.Class#getResource(java.lang.String)}).
     * @param resource A (relative) path denoting the resource to be loaded.
     * @throws InvalidResourceException if the specified resource can not be found or accessed.
     */
    public PropertyFile(
            Class owner, String resource) throws InvalidResourceException {
        super();
        String msg;
        InputStream in = null;
        //URL resourceURL = owner.getResource(resource);
        URL resourceURL = XQCoreManager.getResource(resource);
        if (resourceURL == null) {
        	msg = "Properties file " + resource + " not found.";
            throw new InvalidResourceException(msg);
        }
        try {
        	in = resourceURL.openStream();
            this.load(in);
            this.file = resourceURL.toString();
        } catch (IOException e) {
        	msg = "Exception occured when loading property file " + resource;
            throw new InvalidResourceException(msg, e);
        } finally {
        	//closing input stream explicitly; finally block was introduced after 
        	//the following exception was thrown repeatedly at resourceURL.openStream()
        	//in corresponding class of datalog module:
        	//java.io.IOException: Too many open files
        	//(g.n., 2006-05-22)
        	if (in != null) {
        		try {
					in.close();
				} catch (IOException e) {
					msg = "Could not close URL connection to " + resource;
					LOGGER.error(msg, e);
				}
        	}
        }

    }

    /**
     * Returns the fully qualified path of the properties file which serves as resource for this
     * <code>PropertyFile</code>.
     * 
     * @return The fully qualified path of the properties file.
     */
    public String getFile() {
        return file;
    }

    /**
     * Loads a property from this <code>PropertyFile</code> object.
     * 
     * @param key The key for the property.
     * @return The property value.
     * @throws InvalidResourceException if no property specified by the key exists.
     */
    public String loadProperty(String key) throws InvalidResourceException {
    	String msg;
        String property = this.getProperty(key);
        if (property == null) {
        	msg = new String();
        	msg += "Property " + key + " not specified in property file ";
        	msg += file + ".";
            throw new InvalidResourceException(msg);
        }
        return property;
    }

    /**
     * Loads a property which is interpreted as long number.
     * 
     * @param key The key for the property.
     * @return The property value as long number.
     * @throws InvalidResourceException if no property specified by the key exists or if the value
     *             is no valid long number.
     */
    public long parseLongProperty(String key) throws InvalidResourceException {
    	String msg;
        String value = loadProperty(key);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
        	msg = new String();
        	msg += "Invalid value " + value + " specified for property " + key;
        	msg += " in property file " + file + ".";
            throw new InvalidResourceException(msg);
        }
    }

    /**
     * Loads a property which is interpreted as boolean.
     * 
     * @param key The key for the property.
     * @return The property value as boolean (see {@link Boolean#booleanValue()}).
     * @throws InvalidResourceException if no property specified by the key exists.
     */
    public boolean parseBooleanProperty(String key) throws InvalidResourceException {
        String value = loadProperty(key);
        return Boolean.valueOf(value).booleanValue();
    }

    /**
     * Loads a property which is interpreted as int number.
     * 
     * @param key The key for the property.
     * @return The property value as integer number.
     * @throws InvalidResourceException if no property specified by the key exists or if the value
     *             is no valid integer number.
     */
    public int parseIntProperty(String key) throws InvalidResourceException {
    	String msg;
        String value = loadProperty(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
        	msg = new String();
        	msg += "Invalid value " + value + " specified for property ";
        	msg += key + " in property file " + file + ".";
            throw new InvalidResourceException(msg);
        }
    }

    /**
     * Loads a property which is interpreted as a file.
     * 
     * @param key The key for the property.
     * @return An absolute file path which was inferred from the possibly relative path found in the
     *         property.
     * @throws InvalidResourceException if no property specified by the key exists or if the file
     *             denoted by the property does not exist.
     */
    public String parseFileProperty(String key) throws InvalidResourceException {
    	String msg;
        String value = loadProperty(key);
        String filepath = getFilepath(value);
        if (filepath == null) {
        	msg = new String();
        	msg = "Not existing file " + value + " specified for property ";
        	msg = key + " in property file " + file + ".";
            throw new InvalidResourceException(msg);
        }
        return filepath;
    }

    /**
     * Gets the absolute filepath according to an absolute or possibly relative filename.
     * 
     * @param filename The filename or filepath which may be absolute or relative.
     * @return The absolute filepath according to the parameter or null if the specified file does
     *         not exist.
     */
    private String getFilepath(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        return null;
    }
}