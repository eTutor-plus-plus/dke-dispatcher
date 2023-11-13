package at.jku.dke.etutor.modules.nf.analysis.closure;

import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.algorithms.Closure;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;

import java.util.Collection;
import java.util.logging.Level;

public class AttributeClosureAnalyzer {

	public static AttributeClosureAnalysis analyze(Collection<FunctionalDependency> dependencies, Collection<String> baseAttributes, Collection<String> submittedAttributes){
		AttributeClosureAnalysis analysis = new AttributeClosureAnalysis();
		Collection<String> correctAttributes = Closure.execute(baseAttributes, dependencies);

		analysis.setSubmissionSuitsSolution(true);

		//DETERMINING MISSING ATTRIBUTES
		analysis.setMissingAttributes(correctAttributes);
		analysis.removeAllMissingAttributes(submittedAttributes);
		if (!analysis.getMissingAttributes().isEmpty()){
			analysis.setSubmissionSuitsSolution(false);
			RDBDHelper.getLogger().log(Level.INFO, "FOUND MISSING ATTRIBUTES");
		}
		
		//DETERMINING ADDITIONAL ATTRIBUTES
		analysis.setAdditionalAttributes(submittedAttributes);
		analysis.removeAllAdditionalAttributes(correctAttributes);
		if (!analysis.getAdditionalAttributes().isEmpty()){
			analysis.setSubmissionSuitsSolution(false);
			RDBDHelper.getLogger().log(Level.INFO, "FOUND ADDITIONAL ATTRIBUTES");
		}

		return analysis;
	}
}
