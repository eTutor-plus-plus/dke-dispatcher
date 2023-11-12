package at.jku.dke.etutor.modules.nf.exercises;

import etutor.modules.rdbd.RDBDConstants;

/**
 * Extends the basic RDBD exercises manager implementation for the 
 * attribute closure module. 
 * 
 * @author Georg Nitsche (10.01.2006)
 *
 */
public class AttributeClosureExercisesManager extends RDBDExercisesManager {

	public AttributeClosureExercisesManager() { 
		super(RDBDConstants.TYPE_ATTRIBUTE_CLOSURE);
	}
}
