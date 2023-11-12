package at.jku.dke.etutor.modules.nf.exercises;

import etutor.modules.rdbd.RDBDConstants;

/**
 * Extends the basic RDBD exercises manager implementation for the 
 * normalform determination module. 
 * 
 * @author Georg Nitsche (10.01.2006)
 *
 */
public class NormalformDeterminationExercisesManager extends RDBDExercisesManager {

	public NormalformDeterminationExercisesManager() { 
		super(RDBDConstants.TYPE_NORMALFORM_DETERMINATION);
	}
}
