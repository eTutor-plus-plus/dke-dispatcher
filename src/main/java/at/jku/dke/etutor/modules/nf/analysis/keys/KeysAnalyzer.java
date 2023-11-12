package at.jku.dke.etutor.modules.nf.analysis.keys;

import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.model.Relation;

import java.util.TreeSet;
import java.util.logging.Level;

public class KeysAnalyzer {

	public static KeysAnalysis analyze(Relation relation, KeysAnalyzerConfig config){
		String message;
		TreeSet correctKeys;
		KeysAnalysis analysis;

		message = new String();
		message = message.concat("Start analyzing keys determination.");
		RDBDHelper.getLogger().log(Level.INFO, message);

		analysis = new KeysAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		
		correctKeys = config.getCorrectMinimalKeys();
		analysis.setSubmission(relation.getMinimalKeys());

		analysis.setMissingKeys(correctKeys);
		analysis.removeAllMissingKeys(relation.getMinimalKeys());

		analysis.setAdditionalKeys(relation.getMinimalKeys());
		analysis.removeAllAdditionalKeys(correctKeys);

		if ((analysis.getMissingKeys().size() != 0) || (analysis.getAdditionalKeys().size() != 0)){
			analysis.setSubmissionSuitsSolution(false);
		}

		RDBDHelper.getLogger().log(Level.INFO, "Found " + analysis.getMissingKeys().size() + " missing keys.");
		RDBDHelper.getLogger().log(Level.INFO, "Found " + analysis.getAdditionalKeys().size() + " additional keys.");

		message = new String();
		message = message.concat("Exit analyzing keys determination.");
		RDBDHelper.getLogger().log(Level.INFO, message);
		
		return analysis;
	}
}
