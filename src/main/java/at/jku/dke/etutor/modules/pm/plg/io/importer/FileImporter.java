package at.jku.dke.etutor.modules.pm.plg.io.importer;

import at.jku.dke.etutor.modules.pm.plg.generator.ProgressAdapter;
import at.jku.dke.etutor.modules.pm.plg.model.Process;

/**
 * This abstract class implements the model import with no notification
 *  
 * @author Andrea Burattin
 */
public abstract class FileImporter implements IFileImporter {

	@Override
	public Process importModel(String filename) {
		return importModel(filename, new ProgressAdapter());
	}
}
