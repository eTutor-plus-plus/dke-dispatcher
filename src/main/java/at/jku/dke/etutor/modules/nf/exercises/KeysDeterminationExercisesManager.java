package at.jku.dke.etutor.modules.nf.exercises;

import etutor.modules.rdbd.RDBDConstants;

/**
 * Extends the basic RDBD exercises manager implementation for the 
 * keys determination module. 
 * 
 * @author Georg Nitsche (10.01.2006)
 *
 */
public class KeysDeterminationExercisesManager extends RDBDExercisesManager {

	public KeysDeterminationExercisesManager() { 
		super(RDBDConstants.TYPE_KEYS_DETERMINATION);
	}
}
