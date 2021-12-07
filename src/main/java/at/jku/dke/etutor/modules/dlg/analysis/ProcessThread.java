package at.jku.dke.etutor.modules.dlg.analysis;

import DLV.DLVException;
import DLV.DlvHandler;
import DLV.Model;
import at.jku.dke.etutor.modules.dlg.TimeoutException;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Class used for the <i>timeout </i> feature of the evaluation of Datalog queries. A separate
 * <code>Thread</code> can be started for evaluating a query, repeatedly checking if a result was
 * produced. If a specified time span is reached, the evaluation is stopped and an <code>Exception</code> is
 * thrown.
 * 
 * @author Georg Nitsche
 * @version 1.0
 * @since 1.0
 */
public class ProcessThread extends Thread {

	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ProcessThread.class);
    private DlvHandler dlv;
    private DLVException dlvException;
    private TimeoutException timeoutException;
    private long interval;
    private long timeOut;
    private long startTime;
    private long currTime;
    
    /**
     * Creates a new <code>Thread</code>.
     * 
     * @param dlv The Datalog program which is initialized with database, query and some more
     *            parameters. This program will be run and should return models after execution.
     * @param interval An interval (in milliseconds), which defines the period of time, that
     *            repeatedly has to elapse before checking whether a result has been found already.
     * @param timeOut The time limit to be used when evaluating.
     */
    public ProcessThread(DlvHandler dlv, long interval, long timeOut) {
    	this.dlv = dlv;
        this.interval = interval;
        this.timeOut = timeOut;
    }

    /**
     * Starts the <code>Thread</code>. The Datalog program is started and each period of time,
     * which has been specified when constructing this <code>ProcessThread</code>, it is checked
     * if models have been found already.
     * 
     * @see Runnable#run()
     */
    public void run() {
    	this.dlvException = null;
    	this.timeoutException = null;
        this.startTime = System.currentTimeMillis();
        this.currTime = startTime;
        
        LOGGER.debug(this.toString() + " started: Executing datalog program.");
        
        try {
            dlv.run(DlvHandler.ASYNCHRONOUS);
        } catch (DLVException e) {
            e.printStackTrace();
            this.dlvException = e;
		}
        
        try {
        	if (this.dlvException == null) {
				while (checkRunCondition()) {
				    try {
				        sleep(interval);
				    } catch (InterruptedException e) {
				    }
				}
        	}
        } catch (TimeoutException e) {
        	this.timeoutException = e;
		} finally {
			//make sure that kill method gets called (whether implementation
			//actually guarantees release of resources or not)
			try {
  			dlv.kill();
			} catch (Throwable e) {
        LOGGER.error("Killing DLV", e);
			}
		}
		
        LOGGER.debug(this.toString() + " stopped: Datalog program was executed.");
    }

    /**
     * Checks if a running <code>ProcessThread</code> instance should continue running.
     * The following conditions are evaluated:
     * <ul>
     * <li>
     * The datalog handle must not have thrown an exception when starting datalog program
     * execution. Execution is started as soon as the <code>ProcessThread</code> is started.
     * </li>
     * <li>
     * The datalog handle must indicate that the execution of the datalog program is still
     * running.
     * </li>
     * <li>
     * If the execution of the datalog program is still running, the time elapsed since
     * starting the execution must not exceed the timeout defined for the 
     * <code>ProcessThread</code>.
     * </li>
     * </ul>
     * @return 	<code>true</code> if no exception has been thrown so far and the
     * 			datalog handle indicates that the execution of the datalog program is still
     * 			active, otherwise <code>false</code>.
     * @throws	TimeoutException if the timeout has been exceeded.
     */
    private boolean checkRunCondition() throws TimeoutException {
    	String msg;
    	this.currTime = System.currentTimeMillis();
    	long timeSpan = currTime - startTime;

    	if (dlvException != null) {
    		msg = new String();
    		msg += this.toString() + " to be stopped: Datalog handle reports an exception. ";
    		msg += "Time elapsed: " + timeSpan + "; Timeout: " + timeOut;
    		LOGGER.debug(msg);
    		return false;
    	}
    	if (dlv.getStatus() != DlvHandler.RUNNING) {
    		msg = new String();
    		msg += this.toString() + " to be stopped: No datalog program executed (any more). ";
    		msg += "Time elapsed: " + timeSpan + "; Timeout: " + timeOut;
    		LOGGER.debug(msg);
    		return false;
    	}
    	if (dlv.getStatus() == DlvHandler.RUNNING && timeSpan > timeOut) {
    		msg = new String();
    		msg += this.toString() + " to be stopped: Timeout has been reached. ";
    		msg += "Time elapsed: " + timeSpan + "; Timeout: " + timeOut;
    		LOGGER.debug(msg);
   			
   			msg = new String();
   			msg += "Datalog program execution stopped, time limit has been reached ";
   			msg += "(" + timeOut + " ms).";
   			try {
     			dlv.kill();
   			} catch (Throwable e) {
   				throw new TimeoutException(e.toString(), timeOut);
   			}
        throw new TimeoutException(msg, timeOut);
    	}
    	return true;
    }

    /**
     * Requests the models computed by the Datalog program. The models represent the result of the
     * query which was set with the Datalog program. Invocations will be blocked while the 
     * <code>ProcessThread</code> is alive. Output parameters and exceptions possibly thrown in
     * this method indicate the outcome of running the <code>ProcessThread</code>.
     * 
     * @return 	An array of models representing the result computed by executing the datalog program.
     * @throws 	DLVException if the datalog handle set in the <code>ProcessThread</code> throws an
     * 			exception when starting the execution of the datalog program. This exception is
     * 			caught as soon as the <code>ProcessThread</code> is started, and rethrown here.
     * @throws 	TimeoutException if the query can not be evaluated within the time span specified for
     * 			this <code>ProcessThread</code>.
     */
    public Model[] getResult() throws DLVException, TimeoutException {
    	while (this.isAlive()) {
    	}
    	LOGGER.debug("in getResult");
        if (dlvException != null) {
            throw dlvException;
        }
        if (timeoutException != null) {
        	throw timeoutException;
    	}
        ArrayList modelList = new ArrayList();
        while (dlv.hasMoreModels()) {
          	LOGGER.debug("add model");
            modelList.add(dlv.nextModel());
        }
        return (Model[])modelList.toArray(new Model[]{});
    }
}