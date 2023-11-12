package at.jku.dke.etutor.modules.nf.exercises;

import etutor.modules.rdbd.RDBDConstants;

/**
 * Extends the basic RDBD exercises manager implementation for the 
 * normalization module. 
 * 
 * @author Georg Nitsche (10.01.2006)
 *
 */
public class NormalizationExercisesManager extends RDBDExercisesManager {

	public NormalizationExercisesManager() { 
		super(RDBDConstants.TYPE_NORMALIZATION);
	}
}
