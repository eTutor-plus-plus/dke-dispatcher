package at.jku.dke.etutor.modules.nf.analysis.minimalcover;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;
import at.jku.dke.etutor.modules.nf.analysis.*;

public class MinimalCoverAnalysis extends DefaultAnalysis implements Analysis {

	DependenciesCoverAnalysis dependenciesCoverAnalysis;
	TrivialDependenciesAnalysis trivialDependenciesAnalysis;
	ExtraneousAttributesAnalysis extraneousAttributesAnalysis;
	RedundantDependenciesAnalysis redundantDependenciesAnalysis;
	CanonicalRepresentationAnalysis canonicalRepresentationAnalysis;

	public MinimalCoverAnalysis() {
		super();
		this.dependenciesCoverAnalysis = null; 
		this.trivialDependenciesAnalysis = null;
		this.extraneousAttributesAnalysis = null;
		this.redundantDependenciesAnalysis = null;
		this.canonicalRepresentationAnalysis = null;
	}

	public ExtraneousAttributesAnalysis getExtraneousAttributesAnalysis(){
		return this.extraneousAttributesAnalysis;
	}

	public void setExtraneousAttributesAnalysis(ExtraneousAttributesAnalysis analysis){
		this.extraneousAttributesAnalysis = analysis; 
	}

	public DependenciesCoverAnalysis getDependenciesCoverAnalysis(){
		return this.dependenciesCoverAnalysis;
	}
	
	public void setDependenciesCoverAnalysis(DependenciesCoverAnalysis analysis){
		this.dependenciesCoverAnalysis = analysis;
	}

	public RedundantDependenciesAnalysis getRedundandDependenciesAnalysis(){
		return this.redundantDependenciesAnalysis;
	}
	
	public void setRedundandDependenciesAnalysis(RedundantDependenciesAnalysis analysis){
		this.redundantDependenciesAnalysis = analysis;
	}

	public CanonicalRepresentationAnalysis getCanonicalRepresentationAnalysis() {
		return this.canonicalRepresentationAnalysis;
	}

	public void setCanonicalRepresentationAnalysis(CanonicalRepresentationAnalysis analysis) {
		this.canonicalRepresentationAnalysis = analysis;
	}
	
	public TrivialDependenciesAnalysis getTrivialDependenciesAnalysis() {
		return this.trivialDependenciesAnalysis;
	}

	public void setTrivialDependenciesAnalysis(TrivialDependenciesAnalysis analysis) {
		this.trivialDependenciesAnalysis = analysis;
	}
}
