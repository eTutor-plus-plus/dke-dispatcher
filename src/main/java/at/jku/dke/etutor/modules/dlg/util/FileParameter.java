package at.jku.dke.etutor.modules.dlg.util;

import java.io.Serializable;

/**
 * Utility class for collecting all necessary information which can be used for
 * generating files, most of all temp files.
 * 
 * @author  Georg Nitsche
 * @version 1.0
 * @since   1.0
 */
public class FileParameter implements Serializable {

	private String tempFolder;
	private String tempPrefix;
	private String tempSuffix;
	private boolean deleteOnExit;
	private boolean defaultOnError;

	/**
	 * Creates a new instance of this class.
	 */
	public FileParameter() {
	    super();
	}
    /**
     * Creates a new instance of this class with the specified parameters.
     * 
     * @param tempFolder The (relative) folder of the temp file.
     * @param tempPrefix The suffix of the temp file, which may be null.
     * @param tempSuffix The suffix of the temp file, which may be null.
     * @param deleteOnExit Indicates whether the created file should be deleted on exit of the Java Virtual Machine. 
     * @param defaultOnError Indicates whether the default temp directory should be choosed if an exception occured on the first try.
     */
    public FileParameter(
            String tempFolder, String tempPrefix, String tempSuffix, boolean deleteOnExit,
            boolean defaultOnError) {
        super();
        this.tempFolder = tempFolder;
        this.tempPrefix = tempPrefix;
        this.tempSuffix = tempSuffix;
        this.deleteOnExit = deleteOnExit;
        this.defaultOnError = defaultOnError;
    }
    /**
     * Informs, whether a file which is created using the parameters from this <code>FileParameter</code>, will be created in 
     * the default directory for default files, if creating it elsewhere failed.
     * 
     * @return True if the option is selected.
     */
    public boolean isDefaultOnError() {
        return this.defaultOnError;
    }
    /**
     * Informs, whether a file which is created using the parameters from this <code>FileParameter</code>, will be deleted on
     * exit of the Java Virtual Machine.
     * 
     * @return True if the option is selected.
     */
    public boolean isDeleteOnExit() {
        return this.deleteOnExit;
    }

    /**
     * Returns the name of the folder in which to place a created file.
     * 
     * @return Returns the name of the folder.
     */
    public String getTempFolder() {
        return this.tempFolder;
    }

    /**
     * Returns the prefix of the file which is created as temp file, when applying the parameters from this <code>FileParameter</code>.
     * 
     * @return Returns the prefix of the file.
     */
    public String getTempPrefix() {
        return this.tempPrefix;
    }

    /**
     * Returns the suffix of the file which is created as temp file, when applying the parameters from this <code>FileParameter</code>.
     * 
     * @return Returns the suffix of the file.
     */
    public String getTempSuffix() {
        return this.tempSuffix;
    }

    /**
     * Defines if a file which is created using the parameters from this <code>FileParameter</code>, will be created in 
     * the default directory for default files, if creating it elsewhere failed.
     * 
     * @param defaultOnError true if the option is selected.
     */
    public void setDefaultOnError(boolean defaultOnError) {
        this.defaultOnError = defaultOnError;
    }

    /**
     * Defines if a file which is created using the parameters from this <code>FileParameter</code>, will be deleted on
     * exit of the Java Virtual Machine.
     * 
     * @param deleteOnExit true if the option is selected.
     */
    public void setDeleteOnExit(boolean deleteOnExit) {
        this.deleteOnExit = deleteOnExit;
    }

    /**
     * Sets the name of the folder in which to place a created file.
     * 
     * @param tempFolder The name of the folder.
     */
    public void setTempFolder(String tempFolder) {
        this.tempFolder = tempFolder;
    }

    /**
     * Sets the prefix of the file which is created as temp file, when applying the parameters from this <code>FileParameter</code>.
     * 
     * @param tempPrefix The prefix of the file.
     */
    public void setTempPrefix(String tempPrefix) {
        this.tempPrefix = tempPrefix;
    }

    /**
     * Sets the suffix of the file which is created as temp file, when applying the parameters from this <code>FileParameter</code>.
     * 
     * @param tempSuffix The suffix of the file.
     */
    public void setTempSuffix(String tempSuffix) {
        this.tempSuffix = tempSuffix;
    }

}
