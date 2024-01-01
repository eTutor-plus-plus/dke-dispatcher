package at.jku.dke.etutor.modules.nf.analysis.keys;

import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.Relation;

import java.util.TreeSet;
import java.util.logging.Level;

public class KeysAnalyzer {

	/**
	 * Tests whether the supplied relation has the correct minimal keys and only minimal keys.
	 * @param relation The relation to be tested
	 * @param config The configuration for this analyzer
	 * @return A <code>KeysAnalysis</code> about the supplied relation
	 */
	public static KeysAnalysis analyze(Relation relation, KeysAnalyzerConfig config){
		// Log progress
		RDBDHelper.getLogger().log(Level.INFO, "Start analyzing keys determination.");

		KeysAnalysis analysis = new KeysAnalysis();

		TreeSet<Key> correctKeys = config.getCorrectMinimalKeys();
		analysis.setSubmission(relation.getMinimalKeys());

		analysis.setMissingKeys(correctKeys);
		analysis.removeAllMissingKeys(relation.getMinimalKeys());

		analysis.setAdditionalKeys(relation.getMinimalKeys());
		analysis.removeAllAdditionalKeys(correctKeys);

		analysis.setSubmissionSuitsSolution(analysis.getMissingKeys().isEmpty() && analysis.getAdditionalKeys().isEmpty());

		RDBDHelper.getLogger().log(Level.INFO, "Found " + analysis.getMissingKeys().size() + " missing keys.");
		RDBDHelper.getLogger().log(Level.INFO, "Found " + analysis.getAdditionalKeys().size() + " additional keys.");

		// Log Progress
		RDBDHelper.getLogger().log(Level.INFO, "Exit analyzing keys determination.");
		
		return analysis;
	}
}
