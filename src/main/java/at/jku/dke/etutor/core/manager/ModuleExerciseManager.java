package at.jku.dke.etutor.core.manager;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

/**
 * eTutor modules should implement this interface to support the functionality
 * of retrieving and manipulating the module-specific contents of exercises.
 *
 * @author Georg Nitsche (10.01.2006)
 *
 */
public interface ModuleExerciseManager {

    /**
     * Stores module-specific information of an exercise used in the eTutor
     * system.
     *
     * @param exerciseId
     *            a unique exercise ID passed by the core which is supposed not
     *            to exist yet as key in the module-specific database
     * @param exercise
     *            a module-specific object to be stored; Retrieved by the core
     *            from {@link #fetchExerciseInfo()} it is handed back via this
     *            method, after successful manipulation by the user using the
     *            web interface
     * @param attributes
     *            a map including request and session attributes from the HTTP
     *            request which initiates a call to this method when processing
     * @param parameters
     *            a map including request parameters from the HTTP request which
     *            initiates a call to this method when processing
     * @return <code>true</code> if creation was successful,
     *         <code>false</code> otherwise
     * @throws Exception
     *             if any exception occured when trying to create the exercise
     *             persistently
     */
    public int createExercise(Serializable exercise,
                              Map attributes, Map parameters) throws Exception;

    /**
     * Updates module-specific information of an exercise used in the eTutor
     * system.
     *
     * @param exerciseId
     *            a unique exercise ID passed by the core to identify the
     *            exercise
     * @param exercise
     *            a module-specific object to be updated; Retrieved by the core
     *            from {@link #fetchExercise(int)} it is handed back via this
     *            method, after successful manipulation by the user using the
     *            web interface
     * @param attributes
     *            a map including request and session attributes from the HTTP
     *            request which initiates a call to this method when processing
     * @param parameters
     *            a map including request parameters from the HTTP request which
     *            initiates a call to this method when processing
     * @return <code>true</code> if update was successful, <code>false</code>
     *         otherwise, e.g. if no exercise exists according to the passed
     *         exercise ID
     * @throws Exception
     *             if any exception occured when trying to update the exercise
     */
    public boolean modifyExercise(int exerciseId, Serializable exercise,
                                  Map attributes, Map parameters) throws Exception;

    /**
     * Deletes module-specific information of an exercise used in the eTutor
     * system.
     *
     * @param exerciseId
     *            a unique exercise ID passed by the core to identify the
     *            exercise
     * @throws Exception
     *             if any exception occured when trying to delete the exercise
     */
    public boolean deleteExercise(int exerciseID) throws Exception;

    /**
     * Fetches module-specific information for an exercise which is supposed to
     * be existing already.
     *
     * @param exerciseID
     *            a unique exercise ID passed by the core to identify the
     *            exercise
     * @return an object which can be used in the web application for
     *         manipulation; The object is passed as session attribute to the
     *         module-specific JSP or Servlet designed for exercise
     *         specification tasks. If no exercise was found, <code>null</code>
     *         should be returned. The manipulated object possibly is handed
     *         back via {@link #modifyExercise(int, Serializable, Map, Map)}.
     * @throws Exception
     *             if any exception occured when trying to fetch the exercise
     */
    public Serializable fetchExercise(int exerciseID) throws Exception;

    /**
     * Fetches module-specific information for a new exercise to be created.
     * This may contain any information necessary to specify a new exercise;
     *
     * @return an object which can be used in the web application for
     *         manipulation; The object is passed as session attribute to the
     *         module-specific JSP or Servlet designed for exercise
     *         specification tasks. The manipulated object possibly is handed
     *         back via {@link #createExercise(int, Serializable, Map, Map)}.
     * @throws Exception
     *             if any exception occured when trying to fetch information for
     *             the new exercise
     */
    public Serializable fetchExerciseInfo() throws Exception;

    /**
     * Provides the functionality for automatically generating exercise
     * assignments according to exercise specifications.
     *
     * @param exercise
     *            a module-specific object as returned by
     *            {@link #fetchExercise(int)} or {@link #fetchExerciseInfo()};
     * @param locale
     *            indicates the requested language
     * @return the generated exercise assignment; If this method is not
     *         implemented or the requested language is not supported,
     *         <code>null</code> should be returned
     * @throws Exception
     *             if any exception occured when trying to generate an assignment
     */
    public String generateHtml(Serializable exercise, Locale locale) throws Exception;
}