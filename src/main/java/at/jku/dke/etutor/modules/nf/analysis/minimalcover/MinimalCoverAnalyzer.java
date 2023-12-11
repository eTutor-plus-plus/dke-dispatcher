package at.jku.dke.etutor.modules.nf.analysis.minimalcover;

import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.algorithms.Cover;
import at.jku.dke.etutor.modules.nf.algorithms.Member;
import at.jku.dke.etutor.modules.nf.algorithms.MinimalCover;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.Relation;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;

public class MinimalCoverAnalyzer {

	private MinimalCoverAnalyzer() {
		// This class is not meant to be instantiated.
	}

    public static MinimalCoverAnalysis analyze(Relation relation, Relation specification){
		//INIT LOCAL VARIABLES
		MinimalCoverAnalysis analysis = new MinimalCoverAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		Set<FunctionalDependency> submittedDependencies = relation.getFunctionalDependencies();
		Set<FunctionalDependency> correctDependencies = MinimalCover.execute(specification.getFunctionalDependencies());

		//ANALYZE CANONICAL REPRESENTATION OF SUBMITTED DEPENDENCIES
		CanonicalRepresentationAnalysis canonicalRepresentationAnalysis = analyzeCanonicalRepresentation(submittedDependencies);
		analysis.setCanonicalRepresentationAnalysis(canonicalRepresentationAnalysis);
		if (!canonicalRepresentationAnalysis.submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			return analysis;
		}
	
		//ANALYZE TRIVIAL FUNCTIONAL DEPENDENCIES
		TrivialDependenciesAnalysis trivialDependenciesAnalysis = analyzeTrivialDependencies(submittedDependencies);
		analysis.setTrivialDependenciesAnalysis(trivialDependenciesAnalysis);
		if (!trivialDependenciesAnalysis.submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			return analysis;
		}
		
		//ANALYZE EXTRANEOUS ATTRIBUTES
		ExtraneousAttributesAnalysis extraneousAttributesAnalysis = analyzeExtraneousAttributes(submittedDependencies);
		analysis.setExtraneousAttributesAnalysis(extraneousAttributesAnalysis);
		if (!extraneousAttributesAnalysis.submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			return analysis;
		}
		
		//ANALYZE REDUNDANT FUNCTIONAL DEPENDENCIES
		RedundantDependenciesAnalysis redundantDependenciesAnalysis = analyzeRedundantDependencies(submittedDependencies);
		analysis.setRedundantDependenciesAnalysis(redundantDependenciesAnalysis);
		if (!redundantDependenciesAnalysis.submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			return analysis;
		}

		//ANALYZE DEPENDENCIES COVER
		DependenciesCoverAnalysis dependenciesCoverAnalysis = analyzeDependenciesCover(submittedDependencies, correctDependencies);
		analysis.setDependenciesCoverAnalysis(dependenciesCoverAnalysis);
		if (!dependenciesCoverAnalysis.submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
		}

		return analysis;
	}
	
	public static DependenciesCoverAnalysis analyzeDependenciesCover(Collection<FunctionalDependency> submittedDependencies, Collection<FunctionalDependency> correctDependencies){
		FunctionalDependency currDependency;

		DependenciesCoverAnalysis analysis = new DependenciesCoverAnalysis();
		analysis.setSubmissionSuitsSolution(true);

		Iterator<FunctionalDependency> dependenciesIterator = submittedDependencies.iterator();
		while (dependenciesIterator.hasNext()){
			currDependency = dependenciesIterator.next();
			if (!Member.execute(currDependency, correctDependencies)){
				analysis.addAdditionalDependency(currDependency);
				analysis.setSubmissionSuitsSolution(false);
				RDBDHelper.getLogger().log(Level.INFO, "Found additional functional dependency.");
			}
		}

		dependenciesIterator = correctDependencies.iterator();
		while (dependenciesIterator.hasNext()){
			currDependency = dependenciesIterator.next();
			if (!Member.execute(currDependency, submittedDependencies)){
				analysis.addMissingDependency(currDependency);
				analysis.setSubmissionSuitsSolution(false);
				RDBDHelper.getLogger().log(Level.INFO, "Found missing functional dependency.");
			}
		}
		
		return analysis;
	}
	
	public static RedundantDependenciesAnalysis analyzeRedundantDependencies(Collection<FunctionalDependency> dependencies){
		FunctionalDependency currDependency;

		Vector<FunctionalDependency> tempDependencies = new Vector<>();
		Iterator<FunctionalDependency> dependenciesIterator = dependencies.iterator();
		RedundantDependenciesAnalysis analysis = new RedundantDependenciesAnalysis();
		analysis.setSubmissionSuitsSolution(true);

		while (dependenciesIterator.hasNext()){
			currDependency = dependenciesIterator.next();

			tempDependencies.clear();
			tempDependencies.addAll(dependencies);
			tempDependencies.remove(currDependency);
			tempDependencies.removeAll(analysis.getRedundantDependencies());

			if (Cover.execute(tempDependencies, dependencies)){
				RDBDHelper.getLogger().log(Level.INFO, "Found redundant functional dependency.");
				analysis.addRedundantDependency(currDependency);
				analysis.setSubmissionSuitsSolution(false);
			}
		}

		return analysis;
	}
	
	public static ExtraneousAttributesAnalysis analyzeExtraneousAttributes(Collection<FunctionalDependency> dependencies){
		Vector<FunctionalDependency> tempDependencies = new Vector<>();
		FunctionalDependency tempDependency = new FunctionalDependency();
		ExtraneousAttributesAnalysis analysis = new ExtraneousAttributesAnalysis();
		Iterator<FunctionalDependency> dependenciesIterator = dependencies.iterator();
		analysis.setSubmissionSuitsSolution(true);

		while (dependenciesIterator.hasNext()) {
			FunctionalDependency currDependency = dependenciesIterator.next();

			for (String currAttribute : currDependency.getLhsAttributes()) {
				tempDependency.setLhsAttributes(currDependency.getLhsAttributes());
				tempDependency.setRhsAttributes(currDependency.getRhsAttributes());
				tempDependency.removeLhsAttribute(currAttribute);

				if (analysis.getExtraneousAttributes().containsKey(currDependency)){
					tempDependency.removeLhsAttributes(analysis.getExtraneousAttributes().get(currDependency));
				}

				tempDependencies.clear();
				tempDependencies.addAll(dependencies);
				tempDependencies.remove(currDependency);
				tempDependencies.add(tempDependency);
				
				if (Cover.execute(dependencies, tempDependencies)){
					analysis.setSubmissionSuitsSolution(false);
					analysis.addExtraneousAttribute(currDependency, currAttribute);
					RDBDHelper.getLogger().log(Level.INFO, "Found extraneous attributes.");
				}
			}
		}
		
		return analysis;
	}
	
	public static TrivialDependenciesAnalysis analyzeTrivialDependencies(Set<FunctionalDependency> dependencies){
		Iterator<FunctionalDependency> dependenciesIterator = dependencies.iterator();
		TrivialDependenciesAnalysis analysis = new TrivialDependenciesAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		
		while (dependenciesIterator.hasNext()){
			FunctionalDependency currDependency = dependenciesIterator.next();
			if (currDependency.isTrivial()){
				analysis.addTrivialDependency(currDependency);
				analysis.setSubmissionSuitsSolution(false);
				RDBDHelper.getLogger().log(Level.INFO, "Found trivial dependency.");
			}
		}
		
		return analysis;
	}
	
	public static CanonicalRepresentationAnalysis analyzeCanonicalRepresentation(Set<FunctionalDependency> dependencies){
		CanonicalRepresentationAnalysis analysis = new CanonicalRepresentationAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		
		for (FunctionalDependency currDependency : dependencies){
			if (currDependency.getRhsAttributes().size() > 1){
				analysis.addNotCanonicalDependency(currDependency);
				RDBDHelper.getLogger().log(Level.INFO, "Found not canonically represented dependency.");
				analysis.setSubmissionSuitsSolution(false);
			}
		}

		return analysis;
	}
}
