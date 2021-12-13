package at.jku.dke.etutor.modules.dlg.analysis;

import DLV.*;
import at.jku.dke.etutor.modules.dlg.InitException;
import at.jku.dke.etutor.modules.dlg.QuerySyntaxException;
import at.jku.dke.etutor.modules.dlg.TimeoutException;
import at.jku.dke.etutor.modules.dlg.exercise.TermDescription;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.util.Enumeration;

/**
 * A processor for evaluating Datalog queries. The underlying processor is an executable file. Apart
 * from this file, a number of some other parameters are set for evaluating queries.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class DatalogProcessor {

    /**
     * The logger used for logging.
     */
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(DatalogProcessor.class);
    
    private static final String LINE_SEP = System.getProperty("line.separator", "\n");
    
    /**
     * Suffix to be appended to constants within datalog facts, in order to detect
     * faked submissions. 
     * NOTE: constants can also be numbers, in which case appending letters would 
     * result in syntactical errors.
     */
    private static final String SUFFIX = "0";
    
    private Program p;

    private DlvHandler dlv;

    private String database;

    private String exe;

    private int maxInt;

    private long timeout;

    private long interval;

    /**
     * Constructs a new <code>DatalogProcessor</code>, which can be used for evaluating queries.
     * 
     * @param database The database facts or any Datalog statements, which will be appended to any
     *            query which is evaluated using this <code>DatalogProcessor</code>.
     * @param exe Full filepath of the executable file which processes queries.
     * @param timeout A time span (in milliseconds), which will cause the Datalog processor to stop
     *            evaluating a query, if no result was returned before it elapsed.
     * @param interval An interval (in milliseconds), which defines the period of time, that
     *            repeatedly has to elapse before proofing whether a result has been found already.
     * @param maxInt The highest integer which is used for evaluating queries. This value is passed
     *            to the underlying Datalog processor in order to set the highest number when
     *            dealing with numeric functions. If the processor comes across a number when
     *            evaluating a query, which exceeds this limit, an error message is returned, which
     *            is, just like any ordinary syntax exception, packed in a
     *            <code>QuerySyntaxException</code>.
     * @throws InitException if any Exception occured in conjunction with using the underlying
     *             Datalog processor for evaluating queries.
     * @throws QuerySyntaxException if the passed Datalog database is syntactically incorrect.
     * @throws TimeoutException if the database itself can not be evaluated within the specified
     *             time span.
     */
    public DatalogProcessor (
            String database, String exe, long timeout, long interval, int maxInt, TermDescription[] uncheckedTerms)
            throws InitException, QuerySyntaxException, TimeoutException {
        this.exe = exe;
        this.interval = interval;
        this.timeout = timeout;
        initDLV(exe);
        setDatabase(database, maxInt, uncheckedTerms);
    }

    public DatalogProcessor() {
    }

    /**
     * Initializes this <code>DatalogProcessor</code> with a new Datalog database, which contains
     * facts or any Datalog statements.
     * 
     * @param database The database facts or any Datalog statements, which will be appended to any
     *            query which is evaluated using this <code>DatalogProcessor</code>.
     * @param maxInt The highest integer which is used for evaluating queries. This value is passed
     *            to the underlying Datalog processor in order to set the highest number when
     *            dealing with numeric functions. If the processor comes across a number when
     *            evaluating a query, which exceeds this limit, an error message is returned, which
     *            is, just like any ordinary syntax exception, packed in a
     *            <code>QuerySyntaxException</code>.
     * @throws InitException if any Exception occured in conjunction with using the underlying
     *             Datalog processor for evaluating queries.
     * @throws QuerySyntaxException if the passed Datalog database is syntactically incorrect.
     * @throws TimeoutException if the database itself can not be evaluated within the specified
     *             time span.
     */
    public void setDatabase(String database, int maxInt, TermDescription[] uncheckedTerms)
        throws InitException,
        QuerySyntaxException,
        TimeoutException {
        String oldDatabase = this.database;
        int oldMaxInt = this.maxInt;
        int count = 0;
        this.database = database;
        this.maxInt = maxInt;
        String msg;

        try {
            Model[] result = executeQuery("", true);
            // encode all constants in the database facts, except for those specified.
            if (result != null && result.length == 1 && !result[0].isNoModel()) {
                Enumeration predicates = result[0].getPredicates();
                this.database = "";
                while (predicates.hasMoreElements()) {
                	Predicate predicate = (Predicate)predicates.nextElement();
                	String s = "";
                	for (int i = 0; i < predicate.size(); i++) {
                		Predicate.Literal fact = predicate.getLiteral(i);
                		s += fact.name();
                    	if (fact.arity() > 0) {
                    		s += "(";
                    		for (int j = 0; j < fact.arity(); j++) {
                    			s += fact.getTermAt(j);
                    			//if passed array is null, nothing will be encrypted
                    			boolean encryptTerm = uncheckedTerms != null;
                    			for (int k = 0; uncheckedTerms != null && k < uncheckedTerms.length && encryptTerm; k++) {
	                    			encryptTerm = !termMatchesDescription(fact.name(), fact.getTermAt(j), j + 1, uncheckedTerms[k]);
                    			}
                    			if (encryptTerm) {
                    				s += SUFFIX;
                    				count++;
                    			} 
                        		if (j < (fact.arity() - 1)) {
                        			s += ",";
                        		}
                    		}
                    		s += ")";
                    	}
                    	s += "." + LINE_SEP;
                	}
                	this.database += s + LINE_SEP;
                }
            }
            msg = new String();
            if (count > 0) {
            	msg += count + (count == 1 ? " term " : " terms ");
            	msg += "have been manipulated within the following facts ";
            	msg += "by appending '" + SUFFIX + "':" + LINE_SEP;
            	msg += this.database;
            } else {
            	msg += "No terms in facts base found to manipulate.";
            }
            LOGGER.debug(msg);
        } catch (InitException e) {
            this.database = oldDatabase;
            this.maxInt = oldMaxInt;
            throw e;
        } catch (QuerySyntaxException e) {
            this.database = oldDatabase;
            this.maxInt = oldMaxInt;
            throw e;
        } catch (TimeoutException e) {
            this.database = oldDatabase;
            this.maxInt = oldMaxInt;
            throw e;
        }
    }

    private boolean termMatchesDescription(String predicate, String term, int pos, TermDescription desc) {
    	return desc.getPredicate().equals(predicate) 
    			&& desc.getTerm().equals(term) 
				&& desc.getPosition().equals(Integer.toString(pos));
    }
    /**
     * Returns the Datalog database set for this <code>DatalogProcessor</code>.
     * 
     * @return The database, which is appended to any query evaluated with this
     *         <code>DatalogProcessor</code>.
     */
    public String getDatabase() {
        return this.database;
    }


    /**
     * Initializes the underlying Datalog processor.
     * 
     * @param exe The fully qualified filepath of the executable file of the Datalog processor.
     */
    private void initDLV(String exe) {
        p = new Program();
        // creates a new dlv program
        //TODO Potential error: If the exe argument denotes a file which does not exist
        //or does not represent the appropriate executable file, the datalog adapter
        //will deal with an exception being thrown when trying to start the native
        //process. The problem is 1) that the datalog adapter just writes the exception
        //to stderr rather than passing it on, and 2) that the thread never terminates.
        dlv = new DlvHandler(exe);
    }

    /**
     * Executes and evaluates a query using this <code>DatalogProcessor</code> and the database
     * that is set.
     * 
     * @param query The Datalog query.
     * @return A number of models representing the result of the query. Usually this will be one
     *         model, but it is possible that a query produces several models. As an inconsistent
     *         query produces no model at all, this case has to be considered as well. Semantically,
     *         this will be indicated by a certain method of the returned objects.
     * @throws InitException if any Exception occured in conjunction with using the underlying
     *             Datalog processor for evaluating the query.
     * @throws QuerySyntaxException if the passed query is syntactically incorrect.
     * @throws TimeoutException if the query can not be evaluated within the time span specified for
     *             this <code>DatalogProcessor</code>.
     */
    public Model[] executeQuery(String query, boolean includeFacts) throws QuerySyntaxException, TimeoutException,
            InitException {
        try {
            Model[] result = executeQuery(query, this.database, this.maxInt, this.timeout, this.interval, includeFacts);

            //TODO: System errors (e.g. File not found) printed by dlv processor is interpreted as
            // QuerySyntaxException
            if (!"".equals(dlv.getWarnings())) {
                //System.out.println(dlv.getWarnings());
            	String msg = new String();
                msg += "Query result cannot be processed due to syntax errors." + LINE_SEP;
                msg += "The datalog tool reports: " + dlv.getWarnings() + LINE_SEP;
                QuerySyntaxException exception = new QuerySyntaxException(msg);
                exception.setDescription(dlv.getWarnings());
                tmpDebugging(dlv.getWarnings(), query);
                throw exception;
            }
            return result;
        } catch (DLVException e) {
            String message = "An exception was thrown when initializing the query processor.";
            throw new InitException(message, e);
        }
    }

    //TODO Method only for debugging; Find reason for "An error occurred reading warnings" warnings
    //reported by the DLV tool;
    private static void tmpDebugging(String warnings, String query) {
        if (warnings != null) {
        	warnings = warnings.replaceAll("\n", LINE_SEP);
        	String[] lines = warnings.split(LINE_SEP);
        	for (int i = 0; i < lines.length; i++) {
	            if (lines[i].toLowerCase().matches(".*an error occurred reading warnings.*")) {
	            	String msg = new String();
	                msg += "The datalog tool reports: " + warnings + LINE_SEP;
	                msg += "Query which has been analyzed: " + query;
	                LOGGER.debug(msg);
	                return;
	            }
        	}
        }
    }
    
    /**
     * Executes and evaluates a query together with a specified Datalog database, which most
     * commonly contains given facts.
     * 
     * @param query The query to evaluate.
     * @param database The Datalog database, most commonly facts, which are appended to the query
     *            for evaluation.
     * @param maxInt The highest integer which is used for evaluating queries.
     * @return Models which are the result of the query evaluated against the database.
     * @param timeout The time limit to be used when evaluating.
     * @param interval The time interval to be used for checking if a model was found.
     * @throws DLVException if the underlying Datalog processor causes a DLVException to be thrown.
     * @throws TimeoutException if the query can not be evaluated within the specified time span.
     */
    private Model[] executeQuery(String query, String database, int maxInt, long timeout,
            long interval, boolean includeFacts) throws DLVException, TimeoutException {
        p.reset();
        p.addString(query + database);

        // sets input (contained in a Program object)
        dlv.setProgram(p);
        
        // -------------- set invocation parameters -------------
        // sets the desired predicates to be contained in the result (the
        // -filter option of the executable)
        // sets the input facts to be excluded from the result (the -nofacts
        // option of the executable)
        dlv.setIncludeFacts(includeFacts);
        dlv.setMaxint(maxInt);
        /*
         * dlv.run(Dlv.MODEL_SYNCHRONOUS);
         */
        ProcessThread thread = new ProcessThread(dlv, interval, timeout);
        thread.start();
        while (!(dlv.getStatus()==dlv.FINISHED || dlv.getStatus()==dlv.KILLED)) {
        	try {
        		Thread.sleep(2000);
        	} catch (Exception e) {
        	}
        }

        return thread.getResult();

    }

    /**
     * Returns the highest number that was set for telling the underlying Datalog processor the
     * highest number to be considered when dealing with numeric values.
     * 
     * @return The highest number set.
     */
    public int getMaxInt() {
        return this.maxInt;
    }

    /**
     * Returns the filepath of the executable file of the Datalog processor.
     * 
     * @return Returns the filepath of the executable file.
     */
    public String getExe() {
        return this.exe;
    }

    /**
     * Returns the interval (in milliseconds), which was defined as the period of time, that
     * repeatedly has to elapse before proofing whether a result has been found already.
     * 
     * @return Returns the interval.
     */
    public long getInterval() {
        return this.interval;
    }

    /**
     * Returns the time span (in milliseconds), which was defined as the time limit for evaluating
     * queries.
     * 
     * @return Returns the timeout.
     */
    public long getTimeout() {
        return this.timeout;
    }

}