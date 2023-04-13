package at.jku.dke.etutor.modules.jdbc;

import at.jku.dke.etutor.core.evaluation.Evaluator;

import java.rmi.Remote;

/**
 * Adds functionality to the <code>Evaluator</code> interface for RMI purpose.
 * Each method must throw a <code>RemoteException</code> to comply with RMI,
 * which is fulfilled with each <code>Evaluator</code> method throwing a basic
 * </code>Exception</code>.
 * 
 * @author  Georg Nitsche
 * @version 1.0
 * @since   1.0
 */
public interface JDBCEvaluator extends Remote, Evaluator {
}