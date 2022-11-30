package at.jku.dke.etutor.modules.pm.plg.model.gateway;

import at.jku.dke.etutor.modules.pm.plg.model.FlowObject;
import at.jku.dke.etutor.modules.pm.plg.model.Process;

/**
 * This class represents a general process gateway
 * 
 * @author Andrea Burattin
 */
public abstract class Gateway extends FlowObject {

	/**
	 * This constructor creates a new gateway and register it to the given
	 * process owner
	 * 
	 * @param owner the process owner of the new gateway
	 */
	public Gateway(Process owner) {
		super(owner);
	}
}
