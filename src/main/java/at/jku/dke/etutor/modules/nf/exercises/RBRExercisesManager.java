package at.jku.dke.etutor.modules.nf.exercises;

import at.jku.dke.etutor.modules.nf.RDBDConstants;

/**
 * Extends the basic RDBD exercises manager implementation for the 
 * RBR module. 
 * 
 * @author Georg Nitsche (10.01.2006)
 *
 */
public class RBRExercisesManager extends RDBDExercisesManager {

	public RBRExercisesManager() { 
		super(RDBDConstants.TYPE_RBR);
	}
}
