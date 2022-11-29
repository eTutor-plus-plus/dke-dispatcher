package at.jku.dke.etutor.modules.pm.plg.model.data;

import at.jku.dke.etutor.modules.pm.plg.exceptions.InvalidScript;
import at.jku.dke.etutor.modules.pm.plg.generator.scriptexecuter.ScriptExecutor;
import at.jku.dke.etutor.modules.pm.plg.model.Process;
import at.jku.dke.etutor.modules.pm.plg.model.data.IDataObjectOwner.DATA_OBJECT_DIRECTION;

/**
 * This class describes a data object that can be associated to any flow object,
 * with a value automatically generated.
 * 
 * @author Andrea Burattin
 */
public abstract class GeneratedDataObject extends DataObject implements INoiseSensitiveDataObject {

	protected ScriptExecutor executor;
	protected Object originalValue = null;
	
	/**
	 * Class constructor that build a new data object associated to the current
	 * process.
	 * 
	 * @param processOwner the process owner of this data object
	 * @param objectOwner the process owner of this data object
	 * @param generateInstance manually decide whether the instance value
	 * should be generated or not
	 * @param direction the direction of the data object
	 */
	protected GeneratedDataObject(Process processOwner, IDataObjectOwner objectOwner, DATA_OBJECT_DIRECTION direction) {
		super(processOwner, objectOwner, direction);
	}
	
	/**
	 * Class constructor that build a new data object associated to the current
	 * process.
	 * 
	 * @param processOwner the process owner of this data object
	 * @param generateInstance manually decide whether the instance value
	 * should be generated or not
	 */
	protected GeneratedDataObject(Process processOwner) {
		this(processOwner, null, null);
	}

	/**
	 * This method will call a new generation of the value for the current data
	 * object instance. Different invocations of this method may generate
	 * different values.
	 * 
	 * @param caseId the case identifier of the current process instance
	 * @throws InvalidScript 
	 */
	public void generateInstance(String caseId) throws InvalidScript {
		executor.execute(caseId);
		originalValue = executor.getValue();
		setValue(originalValue);
	}
	
	/**
	 * This method returns the script executor associated to the current data
	 * object
	 * 
	 * @return the script executor
	 */
	public ScriptExecutor getScriptExecutor() {
		return executor;
	}
	
	@Override
	public Object getOriginalValue() {
		return originalValue;
	}
}
