package etutor.modules.xquery;

import java.rmi.Remote;

import etutor.core.evaluation.Evaluator;

/**
 * Adds additional functionality to the <code>Evaluator</code> interface for RMI purpose.
 * 
 * @author  Georg Nitsche
 * @version 1.0
 * @since   1.0
 */
public interface XQEvaluator extends Remote, Evaluator {
}
