package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.algorithms.Closure;

import java.util.Collection;
import java.util.logging.Level;

public class AttributeClosureAnalyzer {

	public static AttributeClosureAnalysis analyze(Collection dependencies, Collection baseAttributes, Collection submittedAttributes){
		AttributeClosureAnalysis analysis = new AttributeClosureAnalysis();
		Collection correctAttributes = Closure.execute(baseAttributes, dependencies);

		analysis.setSubmissionSuitsSolution(true);

		//DETERMINING MISSING ATTRIBUTES
		analysis.setMissingAttributes(correctAttributes);
		analysis.removeAllMissingAttributes(submittedAttributes);
		if (analysis.getMissingAttributes().size() > 0){
			analysis.setSubmissionSuitsSolution(false);
			RDBDHelper.getLogger().log(Level.INFO, "FOUND MISSING ATTRIBUTES");
		}
		
		//DETERMINING ADDITIONAL ATTRIBUTES
		analysis.setAdditionalAttributes(submittedAttributes);
		analysis.removeAllAdditionalAttributes(correctAttributes);
		if (analysis.getAdditionalAttributes().size() > 0){
			analysis.setSubmissionSuitsSolution(false);
			RDBDHelper.getLogger().log(Level.INFO, "FOUND ADDITIONAL ATTRIBUTES");
		}

		return analysis;
	}
}
