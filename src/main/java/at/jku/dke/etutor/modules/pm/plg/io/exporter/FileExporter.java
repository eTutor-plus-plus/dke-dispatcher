package at.jku.dke.etutor.modules.pm.plg.io.exporter;

import at.jku.dke.etutor.modules.pm.plg.generator.ProgressAdapter;
import at.jku.dke.etutor.modules.pm.plg.model.Process;

/**
 * This abstract class implements the model export with no notification
 *  
 * @author Andrea Burattin
 */
public abstract class FileExporter implements IFileExporter {

	@Override
	public void exportModel(Process model, String filename) {
		exportModel(model, filename, new ProgressAdapter());
	}
}
