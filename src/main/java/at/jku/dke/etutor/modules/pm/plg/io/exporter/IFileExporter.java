package at.jku.dke.etutor.modules.pm.plg.io.exporter;

import at.jku.dke.etutor.modules.pm.plg.generator.IProgressVisualizer;
import at.jku.dke.etutor.modules.pm.plg.model.Process;

/**
 * This interface represents the basic structure of a model exporter.
 * 
 * @author Andrea Burattin
 */
public interface IFileExporter {

	/**
	 * General interface of a method that exports a model.
	 * 
	 * @param model the model to export
	 * @param filename the target of the model to export
	 * @param progress the progress to notify the user
	 */
	public void exportModel(Process model, String filename, IProgressVisualizer progress);
	
	/**
	 * General interface of a method that exports a model without any progress.
	 * 
	 * @param model the model to export
	 * @param filename the target of the model to export
	 */
	public void exportModel(Process model, String filename);
}
