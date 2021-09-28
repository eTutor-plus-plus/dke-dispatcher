package at.jku.dke.etutor.modules.xquery;

import at.jku.dke.etutor.core.evaluation.Evaluator;

import java.rmi.Remote;


/**
 * Adds additional functionality to the <code>Evaluator</code> interface for RMI purpose.
 * 
 * @author  Georg Nitsche
 * @version 1.0
 * @since   1.0
 */
public interface XQEvaluator extends Remote, Evaluator {
}
