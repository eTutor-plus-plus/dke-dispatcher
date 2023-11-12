package at.jku.dke.etutor.modules.nf.exercises;

import at.jku.dke.etutor.modules.nf.RDBDConstants;

/**
 * Extends the basic RDBD exercises manager implementation for the 
 * decompose module. 
 * 
 * @author Georg Nitsche (10.01.2006)
 *
 */
public class DecomposeExercisesManager extends RDBDExercisesManager {

	public DecomposeExercisesManager() { 
		super(RDBDConstants.TYPE_DECOMPOSE);
	}
}
