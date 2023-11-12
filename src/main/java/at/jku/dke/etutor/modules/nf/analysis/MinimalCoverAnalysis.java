package at.jku.dke.etutor.modules.nf.analysis;

import at.jku.dke.etutor.core.evaluation.Analysis;
import at.jku.dke.etutor.core.evaluation.DefaultAnalysis;

public class MinimalCoverAnalysis extends DefaultAnalysis implements Analysis {

	DependenciesCoverAnalysis dependenciesCoverAnalysis;
	TrivialDependenciesAnalysis trivialDependenciesAnalysis;
	ExtraneousAttributesAnalysis extraneousAttributesAnalysis;
	RedundandDependenciesAnalysis redundandDependenciesAnalysis;
	CanonicalRepresentationAnalysis canonicalRepresentationAnalysis;

	public MinimalCoverAnalysis() {
		super();
		this.dependenciesCoverAnalysis = null; 
		this.trivialDependenciesAnalysis = null;
		this.extraneousAttributesAnalysis = null;
		this.redundandDependenciesAnalysis = null;
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

	public RedundandDependenciesAnalysis getRedundandDependenciesAnalysis(){
		return this.redundandDependenciesAnalysis;
	}
	
	public void setRedundandDependenciesAnalysis(RedundandDependenciesAnalysis analysis){
		this.redundandDependenciesAnalysis = analysis;
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
