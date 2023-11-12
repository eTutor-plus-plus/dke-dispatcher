package at.jku.dke.etutor.modules.nf.exercises;

import at.jku.dke.etutor.modules.nf.RDBDConstants;

/**
 * Extends the basic RDBD exercises manager implementation for the 
 * minimal cover module. 
 * 
 * @author Georg Nitsche (10.01.2006)
 *
 */
public class MinimalCoverExercisesManager extends RDBDExercisesManager {

	public MinimalCoverExercisesManager() { 
		super(RDBDConstants.TYPE_MINIMAL_COVER);
	}
}
