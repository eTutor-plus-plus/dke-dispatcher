package at.jku.dke.etutor.modules.nf.analysis.minimalcover;

import at.jku.dke.etutor.modules.nf.NFHelper;
import at.jku.dke.etutor.modules.nf.algorithms.Cover;
import at.jku.dke.etutor.modules.nf.algorithms.Member;
import at.jku.dke.etutor.modules.nf.algorithms.MinimalCover;
import at.jku.dke.etutor.modules.nf.model.FunctionalDependency;
import at.jku.dke.etutor.modules.nf.model.Relation;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
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

		//ANALYZE CANONICAL REPRESENTATION OF SUBMITTED DEPENDENCIES
		CanonicalRepresentationAnalysis canonicalRepresentationAnalysis = analyzeCanonicalRepresentation(submittedDependencies);
		analysis.setCanonicalRepresentationAnalysis(canonicalRepresentationAnalysis);
		if (!canonicalRepresentationAnalysis.submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			// return analysis; // Note: disabled for grading purposes (Gerald Wimmer, 2023-12-31).
		}
	
		//ANALYZE TRIVIAL FUNCTIONAL DEPENDENCIES
		TrivialDependenciesAnalysis trivialDependenciesAnalysis = analyzeTrivialDependencies(submittedDependencies);
		analysis.setTrivialDependenciesAnalysis(trivialDependenciesAnalysis);
		if (!trivialDependenciesAnalysis.submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			// return analysis; // Note: disabled for grading purposes (Gerald Wimmer, 2023-12-31).
		}
		
		//ANALYZE EXTRANEOUS ATTRIBUTES
		ExtraneousAttributesAnalysis extraneousAttributesAnalysis = analyzeExtraneousAttributes(submittedDependencies);
		analysis.setExtraneousAttributesAnalysis(extraneousAttributesAnalysis);
		if (!extraneousAttributesAnalysis.submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			// return analysis; // Note: disabled for grading purposes (Gerald Wimmer, 2023-12-31).
		}

		/*
		 * TODO: Check whether this won't cause any issues, given that we test for redundant FDs compared to the
		 *  correct solution afterwards (Gerald Wimmer, 2023-12-31).
		 */
		// Note: To avoid deducting points for subsequent errors, trivial dependencies are ignored here.
		//ANALYZE REDUNDANT FUNCTIONAL DEPENDENCIES
		Set<FunctionalDependency> nonTrivialDeps = new HashSet<>(submittedDependencies);
		nonTrivialDeps.removeAll(trivialDependenciesAnalysis.getTrivialDependencies());

		RedundantDependenciesAnalysis redundantDependenciesAnalysis = analyzeRedundantDependencies(nonTrivialDeps);
		analysis.setRedundantDependenciesAnalysis(redundantDependenciesAnalysis);
		if (!redundantDependenciesAnalysis.submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
			// return analysis; // Note: disabled for grading purposes (Gerald Wimmer, 2023-12-31).
		}

		//ANALYZE DEPENDENCIES COVER
		Set<FunctionalDependency> correctDependencies = MinimalCover.execute(specification.getFunctionalDependencies());

		DependenciesCoverAnalysis dependenciesCoverAnalysis = analyzeDependenciesCover(submittedDependencies, correctDependencies);
		analysis.setDependenciesCoverAnalysis(dependenciesCoverAnalysis);
		if (!dependenciesCoverAnalysis.submissionSuitsSolution()){
			analysis.setSubmissionSuitsSolution(false);
		}

		return analysis;
	}

	/**
	 * Tests which of the submitted functional dependencies are extraneous or missing compared to the correct solution.
	 * @param submittedDependencies The <code>Collection</code> of <code>FunctionalDependency</code> objects that were
	 *                                 submitted.
	 * @param correctDependencies The <code>Collection</code> containing the correct minimal cover.
	 * @return A <code>DependenciesCoverAnalysis</code> of the submitted functional dependencies
	 */
	public static DependenciesCoverAnalysis analyzeDependenciesCover(Collection<FunctionalDependency> submittedDependencies, Collection<FunctionalDependency> correctDependencies) {
		DependenciesCoverAnalysis analysis = new DependenciesCoverAnalysis();
		analysis.setSubmissionSuitsSolution(true);

		// Test whether any of the submittedDependencies is extraneous
		for (FunctionalDependency currDependency : submittedDependencies) {
			if (!Member.execute(currDependency, correctDependencies)){
				analysis.addAdditionalDependency(currDependency);
				analysis.setSubmissionSuitsSolution(false);
				NFHelper.getLogger().log(Level.INFO, "Found additional functional dependency.");
			}
		}

		// Test whether any functional dependencies are missing from the submittedDependencies
		for (FunctionalDependency currDependency : correctDependencies){
			if (!Member.execute(currDependency, submittedDependencies)){
				analysis.addMissingDependency(currDependency);
				analysis.setSubmissionSuitsSolution(false);
				NFHelper.getLogger().log(Level.INFO, "Found missing functional dependency.");
			}
		}
		
		return analysis;
	}

	/**
	 * Tests which of the passed functional dependencies are redundant (i.e., the <code>Collection</code> of
	 * dependencies would remain semantically identical without it).
	 * @param dependencies The <code>Collection</code> of <code>FunctionalDependency</code> objects to be tested
	 * @return A <code>RedundantDependenciesAnalysis</code> of the passed functional dependencies
	 */
	public static RedundantDependenciesAnalysis analyzeRedundantDependencies(Collection<FunctionalDependency> dependencies){
		RedundantDependenciesAnalysis analysis = new RedundantDependenciesAnalysis();
		analysis.setSubmissionSuitsSolution(true);

		for (FunctionalDependency currDependency : dependencies) {
			/*
			 * Create a copy of all dependencies without currDependency or any previously found redundant dependencies.
			 * (Gerald Wimmer, 2023-12-31).
			 */
			List<FunctionalDependency> tempDependencies = new LinkedList<>(dependencies);
			tempDependencies.remove(currDependency);
			tempDependencies.removeAll(analysis.getRedundantDependencies());

			/*
			 * Check whether the collection of dependencies would remain semantically identical without currDependency
			 * (i.e., whether currDependency is redundant) (Gerald Wimmer, 2023-12-31).
			 */
			if (Cover.execute(tempDependencies, dependencies)){
				NFHelper.getLogger().log(Level.INFO, "Found redundant functional dependency.");
				analysis.addRedundantDependency(currDependency);
				analysis.setSubmissionSuitsSolution(false);
			}
		}

		return analysis;
	}

	/**
	 * Tests which of the passed functional dependencies are not minimal (i.e., the left-hand side contains
	 * extraneous attributes).
	 * @param dependencies The <code>Collection</code> of <code>FunctionalDependency</code> objects to be tested
	 * @return An <code>ExtraneousAttributesAnalysis</code> of the passed functional dependencies
	 */
	public static ExtraneousAttributesAnalysis analyzeExtraneousAttributes(Collection<FunctionalDependency> dependencies) {
		ExtraneousAttributesAnalysis analysis = new ExtraneousAttributesAnalysis();
		analysis.setSubmissionSuitsSolution(true);

		for (FunctionalDependency currDependency : dependencies) {
			for (String currAttribute : currDependency.getLhsAttributes()) {
				/*
				 * Create a copy of the currDependency with all attributes except currAttribute
				 * (Gerald Wimmer, 2023-12-31).
				 */
				FunctionalDependency tempDependency = new FunctionalDependency();
				tempDependency.setLhsAttributes(currDependency.getLhsAttributes());
				tempDependency.setRhsAttributes(currDependency.getRhsAttributes());
				tempDependency.removeLhsAttribute(currAttribute);

				/*
				 * If we've already detected other extraneous attributes for currDependency, remove those from the LHS
				 * as well (Gerald Wimmer, 2023-12-31).
				 */
				if (analysis.getExtraneousAttributes().containsKey(currDependency)) {
					tempDependency.removeLhsAttributes(analysis.getExtraneousAttributes().get(currDependency));
				}

				/*
				 * Create a copy of our set of dependencies which contains the tempDependency WITHOUT currAttribute in
				 * place of the old currDependency WITH curAttribute (Gerald Wimmer, 2023-12-31).
				 */
				List<FunctionalDependency> tempDependencies = new LinkedList<>(dependencies);
				tempDependencies.remove(currDependency);
				tempDependencies.add(tempDependency);

				/*
				 * Check whether the collection of dependencies would remain semantically identical without
				 * currAttribute (i.e., whether currAttribute is extraneous in currDependency)
				 * (Gerald Wimmer, 2023-12-31).
				 */
				if (Cover.execute(dependencies, tempDependencies)) {
					analysis.setSubmissionSuitsSolution(false);
					analysis.addExtraneousAttribute(currDependency, currAttribute);
					NFHelper.getLogger().log(Level.INFO, "Found extraneous attributes.");
				}
			}
		}
		
		return analysis;
	}

	/**
	 * Tests which of the passed functional dependencies is trivial (i.e., either side is empty or the left-hand
	 * side contains the right-hand side).
	 * @param dependencies The <code>Set</code> of <code>FunctionalDependency</code> objects to be tested
	 * @return A <code>TrivialDependenciesAnalysis</code> of the passed functional dependencies
	 */
	public static TrivialDependenciesAnalysis analyzeTrivialDependencies(Set<FunctionalDependency> dependencies){
		TrivialDependenciesAnalysis analysis = new TrivialDependenciesAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		
		for (FunctionalDependency currDependency : dependencies) {
			if (currDependency.isTrivial()){
				analysis.addTrivialDependency(currDependency);
				analysis.setSubmissionSuitsSolution(false);
				NFHelper.getLogger().log(Level.INFO, "Found trivial dependency.");
			}
		}
		
		return analysis;
	}

	/**
	 * Tests which of the passed functional dependencies have more than one right-hand-side attribute.
	 * @param dependencies The <code>Set</code> of <code>FunctionalDependency</code> objects to be tested
	 * @return A <code>CanonicalRepresentationAnalysis</code> of the passed functional dependencies
	 */
	public static CanonicalRepresentationAnalysis analyzeCanonicalRepresentation(Set<FunctionalDependency> dependencies){
		CanonicalRepresentationAnalysis analysis = new CanonicalRepresentationAnalysis();
		analysis.setSubmissionSuitsSolution(true);
		
		for (FunctionalDependency currDependency : dependencies) {
			if (currDependency.getRhsAttributes().size() > 1) {
				analysis.addNotCanonicalDependency(currDependency);
				NFHelper.getLogger().log(Level.INFO, "Found not canonically represented dependency.");
				analysis.setSubmissionSuitsSolution(false);
			}
		}

		return analysis;
	}
}
