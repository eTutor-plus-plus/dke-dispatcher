package at.jku.dke.etutor.modules.nf.analysis.keys;

import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.model.Key;
import at.jku.dke.etutor.modules.nf.model.Relation;

import java.util.TreeSet;
import java.util.logging.Level;

public class KeysAnalyzer {

	public static KeysAnalysis analyze(Relation relation, KeysAnalyzerConfig config){
		String message = "Start analyzing keys determination.";
		RDBDHelper.getLogger().log(Level.INFO, message);

		KeysAnalysis analysis = new KeysAnalysis();
		analysis.setSubmissionSuitsSolution(true);

		TreeSet<Key> correctKeys = config.getCorrectMinimalKeys();
		analysis.setSubmission(relation.getMinimalKeys());

		analysis.setMissingKeys(correctKeys);
		analysis.removeAllMissingKeys(relation.getMinimalKeys());

		analysis.setAdditionalKeys(relation.getMinimalKeys());
		analysis.removeAllAdditionalKeys(correctKeys);

		if ((!analysis.getMissingKeys().isEmpty()) || (!analysis.getAdditionalKeys().isEmpty())){
			analysis.setSubmissionSuitsSolution(false);
		}

		RDBDHelper.getLogger().log(Level.INFO, "Found " + analysis.getMissingKeys().size() + " missing keys.");
		RDBDHelper.getLogger().log(Level.INFO, "Found " + analysis.getAdditionalKeys().size() + " additional keys.");

		message = "";
		message = message.concat("Exit analyzing keys determination.");
		RDBDHelper.getLogger().log(Level.INFO, message);
		
		return analysis;
	}
}
