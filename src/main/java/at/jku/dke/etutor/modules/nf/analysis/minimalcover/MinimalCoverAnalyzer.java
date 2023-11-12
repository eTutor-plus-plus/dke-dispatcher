package at.jku.dke.etutor.modules.nf.analysis.minimalcover;

import at.jku.dke.etutor.modules.nf.RDBDHelper;
import at.jku.dke.etutor.modules.nf.algorithms.Cover;
import at.jku.dke.etutor.modules.nf.algorithms.Member;
import at.jku.dke.etutor.modules.nf.algorithms.MinimalCover;
import at.jku.dke.etutor.modules.nf.analysis.*;
import at.jku.dke.etutor.modules.nf.exercises.RDBDExercisesManager;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.Relation;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;

public class MinimalCoverAnalyzer {

	public static MinimalCoverAnalysis analyze(Relation relation, int exerciseID){
		MinimalCoverAnalysis analysis;
		try {
			analysis = analyze(relation, (Relation)RDBDExercisesManager.fetchSpecification(exerciseID));
		} catch (Exception e) {
			RDBDHelper.getLogger().log(Level.SEVERE, "Unable to determine correct dependencies.", e);
			analysis = new MinimalCoverAnalysis();
			analysis.setSubmissionSuitsSolution(true);
		}
		return analysis;
	}
	
	public static MinimalCoverAnalysis analyze(Relation relation, Relation specification){
		HashSet correctDependencies;
		HashSet submittedDependencies;

		MinimalCoverAnalysis analysis;
		DependenciesCoverAnalysis dependenciesCoverAnalysis;
		TrivialDependenciesAnalysis trivialDependenciesAnalysis;
		ExtraneousAttributesAnalysis extraneousAttributesAnalysis;
		RedundantDependenciesAnalysis redundantDependenciesAnalysis;
		CanonicalRepresentationAnalysis canonicalRepresentationAnalysis;
		
		//INIT LOCAL VARIABLES
		analysis = new MinimalCoverAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		submittedDependencies = relation.getFunctionalDependencies();
		correctDependencies = MinimalCover.execute(specification.getFunctionalDependencies());

		//ANALYZE CANONICAL REPRESENTATION OF SUBMITTED DEPENDENCIES
		canonicalRepresentationAnalysis = analyzeCanonicalRepresentation(submittedDependencies);
		analysis.setCanonicalRepresentationAnalysis(canonicalRepresentationAnalysis);
		if (!canonicalRepresentationAnalysis.submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			return analysis;
		}
	
		//ANALYZE TRIVIAL FUNCTIONAL DEPENDENCIES
		trivialDependenciesAnalysis = analyzeTrivialDependencies(submittedDependencies);
		analysis.setTrivialDependenciesAnalysis(trivialDependenciesAnalysis);
		if (!trivialDependenciesAnalysis.submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			return analysis;
		}
		
		//ANALYZE EXTRANEOUS ATTRIBUTES
		extraneousAttributesAnalysis = analyzeExtraneousAttributes(submittedDependencies);
		analysis.setExtraneousAttributesAnalysis(extraneousAttributesAnalysis);
		if (!extraneousAttributesAnalysis.submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			return analysis;
		}
		
		//ANALYZE REDUNDAND FUNCTIONAL DEPENDENCIES
		redundantDependenciesAnalysis = analyzeRedundandDependencies(submittedDependencies);
		analysis.setRedundandDependenciesAnalysis(redundantDependenciesAnalysis);
		if (!redundantDependenciesAnalysis.submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			return analysis;
		}

		//ANALYZE DEPENDENCIES COVER
		dependenciesCoverAnalysis = analyzeDependenciesCover(submittedDependencies, correctDependencies);
		analysis.setDependenciesCoverAnalysis(dependenciesCoverAnalysis);
		if (!dependenciesCoverAnalysis.submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
		}

		return analysis;
	}
	
	public static DependenciesCoverAnalysis analyzeDependenciesCover(Collection submittedDependencies, Collection correctDependencies){
		Iterator dependenciesIterator;
		DependenciesCoverAnalysis analysis;
		FunctionalDependency currDependency;

		analysis = new DependenciesCoverAnalysis();
		analysis.setSubmissionSuitsSolution(true);

		dependenciesIterator = submittedDependencies.iterator();
		while (dependenciesIterator.hasNext()){
			currDependency = (FunctionalDependency)dependenciesIterator.next();
			if (!Member.execute(currDependency, correctDependencies)){
				analysis.addAdditionalDependency(currDependency);
				analysis.setSubmissionSuitsSolution(false);
				RDBDHelper.getLogger().log(Level.INFO, "Found additional functional dependency.");
			}
		}

		dependenciesIterator = correctDependencies.iterator();
		while (dependenciesIterator.hasNext()){
			currDependency = (FunctionalDependency)dependenciesIterator.next();
			if (!Member.execute(currDependency, submittedDependencies)){
				analysis.addMissingDependency(currDependency);
				analysis.setSubmissionSuitsSolution(false);
				RDBDHelper.getLogger().log(Level.INFO, "Found missing functional dependency.");
			}
		}
		
		return analysis;
	}
	
	public static RedundantDependenciesAnalysis analyzeRedundandDependencies(Collection dependencies){
		Vector tempDependencies;
		Iterator dependenciesIterator;
		FunctionalDependency currDependency;
		RedundantDependenciesAnalysis analysis;
		
		tempDependencies = new Vector();
		dependenciesIterator = dependencies.iterator();
		analysis = new RedundantDependenciesAnalysis();
		analysis.setSubmissionSuitsSolution(true);

		while (dependenciesIterator.hasNext()){
			currDependency = (FunctionalDependency)dependenciesIterator.next();

			tempDependencies.clear();
			tempDependencies.addAll(dependencies);
			tempDependencies.remove(currDependency);
			tempDependencies.removeAll(analysis.getRedundantDependencies());

			if (Cover.execute(tempDependencies, dependencies)){
				RDBDHelper.getLogger().log(Level.INFO, "Found redundand functional dependency.");
				analysis.addRedundandDependency(currDependency);
				analysis.setSubmissionSuitsSolution(false);
			}
		}

		return analysis;
	}
	
	public static ExtraneousAttributesAnalysis analyzeExtraneousAttributes(Collection dependencies){
		String currAttribute;
		Vector tempDependencies;
		Iterator attributesIterator;
		Iterator dependenciesIterator;
		FunctionalDependency tempDependency;
		FunctionalDependency currDependency;
		ExtraneousAttributesAnalysis analysis;
		
		tempDependencies = new Vector();
		tempDependency = new FunctionalDependency();
		analysis = new ExtraneousAttributesAnalysis();
		dependenciesIterator = dependencies.iterator();
		analysis.setSubmissionSuitsSolution(true);

		while (dependenciesIterator.hasNext()) {
			currDependency = (FunctionalDependency)dependenciesIterator.next();
			attributesIterator = currDependency.iterLHSAttributes();

			while (attributesIterator.hasNext()) {
				currAttribute = (String)attributesIterator.next();

				tempDependency.setLHSAttributes(currDependency.getLHSAttributes());
				tempDependency.setRHSAttributes(currDependency.getRHSAttributes());
				tempDependency.removeLHSAttribute(currAttribute);

				if (analysis.getExtraneousAttributes().containsKey(currDependency)){
					tempDependency.removeLHSAttributes(((Vector)analysis.getExtraneousAttributes().get(currDependency)));
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
	
	public static TrivialDependenciesAnalysis analyzeTrivialDependencies(HashSet dependencies){
		Iterator dependenciesIterator;
		FunctionalDependency currDependency;
		TrivialDependenciesAnalysis analysis;
		
		dependenciesIterator = dependencies.iterator();
		analysis = new TrivialDependenciesAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		
		while (dependenciesIterator.hasNext()){
			currDependency = (FunctionalDependency)dependenciesIterator.next();
			if (currDependency.isTrivial()){
				analysis.addTrivialDependency(currDependency);
				analysis.setSubmissionSuitsSolution(false);
				RDBDHelper.getLogger().log(Level.INFO, "Found trivial dependency.");
			}
		}
		
		return analysis;
	}
	
	public static CanonicalRepresentationAnalysis analyzeCanonicalRepresentation(HashSet dependencies){
		Iterator dependenciesIterator;
		FunctionalDependency currDependency;
		CanonicalRepresentationAnalysis analysis;
		
		dependenciesIterator = dependencies.iterator();
		analysis = new CanonicalRepresentationAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		
		while (dependenciesIterator.hasNext()){
			currDependency = (FunctionalDependency)dependenciesIterator.next();
			if (currDependency.getRHSAttributes().size() > 1){
				analysis.addNotCanonicalDependency(currDependency);
				RDBDHelper.getLogger().log(Level.INFO, "Found not canonically represented dependency.");
				analysis.setSubmissionSuitsSolution(false);
			}
		}

		return analysis;
	}
}
