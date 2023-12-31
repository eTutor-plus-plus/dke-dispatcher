package at.jku.dke.etutor.modules.nf.analysis.minimalcover;

import at.jku.dke.etutor.modules.nf.analysis.NFAnalysis;

public class MinimalCoverAnalysis extends NFAnalysis {

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

	public RedundantDependenciesAnalysis getRedundantDependenciesAnalysis(){
		return this.redundantDependenciesAnalysis;
	}
	
	public void setRedundantDependenciesAnalysis(RedundantDependenciesAnalysis analysis){
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
