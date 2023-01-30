package at.jku.dke.etutor.modules.pm.plg.generator.process;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import at.jku.dke.etutor.modules.pm.plg.utils.Pair;
import at.jku.dke.etutor.modules.pm.plg.utils.Random;
import at.jku.dke.etutor.modules.pm.plg.utils.SetUtils;

/**
 * This class describes the parameters of the process generator. With this class
 * the user can control the process randomization.
 *
 * @author Andrea Burattin
 */
public class RandomizationConfiguration {

	// todo: define parameter configurations that define levels of complexity (Exercise, Assignment, Exam)
	// todo: set maxDepth and probabilities to ensure small number of traces and activities

	public static final RandomizationConfiguration CONFIG_ONE = new RandomizationConfiguration(
			3, // max AND branches
			2, // max XOR branches
			0.1, // loop weight
			0.2, // single activity weight
			0.0, // skip weight
			0.3, // sequence weight
			0.3, // AND weight
			0.2, // XOR weight
			3, // maximum depth
			0.0 // data object probability
	);
	public static final RandomizationConfiguration CONFIG_TWO = new RandomizationConfiguration(
			2, // max AND branches
			3, // max XOR branches
			0.2, // loop weight
			0.1, // single activity weight
			0.0, // skip weight
			0.4, // sequence weight
			0.2, // AND weight
			0.3, // XOR weight
			3, // maximum depth
			0.0 // data object probability
	);
	public static final RandomizationConfiguration CONFIG_THREE = new RandomizationConfiguration(
			3, // max AND branches
			3, // max XOR branches
			0.2, // loop weight
			0.3, // single activity weight
			0.0, // skip weight
			0.2, // sequence weight
			0.2, // AND weight
			0.3, // XOR weight
			3, // maximum depth
			0.0 // data object probability
	);

	/**
	 * This is a test configuration with basic random values
	 */
	public static final RandomizationConfiguration BASIC_VALUES = new RandomizationConfiguration(
			5, // max AND branches
			5, // max XOR branches
			0.1, // loop weight
			0.2, // single activity weight
			0.1, // skip weight
			0.7, // sequence weight
			0.3, // AND weight
			0.3, // XOR weight
			3, // maximum depth
			0.0 // data object probability
		);
	
	/**
	 * The maximum number of XOR branches (if wrong value is provided)
	 */
	public static final int MAX_XOR_BRANCHES = 4;
	/**
	 * The maximum number of AND branches (if wrong value is provided)
	 */
	public static final int MAX_AND_BRANCHES = 4;
	/**
	 * The minimum number of XOR branches
	 */
	public static final int MIN_XOR_BRANCHES = 2;
	/**
	 * The minimum number of AND branches
	 */
	public static final int MIN_AND_BRANCHES = 2;
	
	/* Class' private fields */
	private int ANDBranches;
	private int XORBranches;
	private Map<RANDOMIZATION_PATTERN, Double> weights;
	private int maxDepth;
	private double dataObjectProbability;
	
	/**
	 * This enumeration describes the set of all possible patterns
	 */
	public static enum RANDOMIZATION_PATTERN {
		/**
		 * Single activity pattern
		 */
		SINGLE_ACTIVITY,
		/**
		 * Sequence activities pattern
		 */
		SEQUENCE,
		/**
		 * AND pattern
		 */
		PARALLEL_EXECUTION,
		/**
		 * XOR pattern
		 */
		MUTUAL_EXCLUSION,
		/**
		 * XOR pattern
		 */
		LOOP,
		/**
		 * Empty: no activity pattern
		 */
		SKIP
	}
	
	/**
	 * This constructor builds a parameters configuration all parameters are
	 * required.
	 * 
	 * @param ANDBranches the maximum number of AND branches (must be > 1)
	 * @param XORBranches the maximum number of XOR branches (must be > 1)
	 * @param loopWeight the loop weight (must be in [0, 1])
	 * @param singleActivityWeight the weight of single activity (must
	 * be in <tt>[0,1]</tt>)
	 * @param skipWeight the weight of a skip (must be in <tt>[0,1]</tt>)
	 * @param sequenceWeight he weight of sequence activity (must be
	 * in <tt>[0,1]</tt>)
	 * @param ANDWeight the weight of AND split-join (must be in <tt>[0,1]</tt>)
	 * @param XORWeight the weight of XOR split-join (must be in <tt>[0,1]</tt>)
	 * @param emptyPercent the weight of an empty pattern (must be in
	 * <tt>[0,1]</tt>)
	 * @param maxDepth the maximum network deep
	 * @param dataObjectProbability probability to generate data objects
	 * associated to sequences and events
	 */
	public RandomizationConfiguration(int ANDBranches, int XORBranches,
			double loopWeight, double singleActivityWeight, double skipWeight,
			double sequenceWeight, double ANDWeight, double XORWeight,
			int maxDepth, double dataObjectProbability) {
		this.weights = new HashMap<RandomizationConfiguration.RANDOMIZATION_PATTERN, Double>();
		
		setAndBranches(ANDBranches);
		setXorBranches(XORBranches);
		setLoopWeight(loopWeight);
		setSingleActivityWeight(singleActivityWeight);
		setSkipWeight(skipWeight);
		setSequenceWeight(sequenceWeight);
		setANDWeight(ANDWeight);
		setXORWeight(XORWeight);
		setDepth(maxDepth);
		setDataObjectProbability(dataObjectProbability);
	}
	
	/**
	 * Set the AND branches parameter
	 * 
	 * @param andBranches the MAXIMUM number of AND branches
	 * @return the object after the modification
	 */
	public RandomizationConfiguration setAndBranches(int andBranches) {
		ANDBranches = (andBranches > 1)? andBranches : MAX_AND_BRANCHES;
		return this;
	}
	
	/**
	 * Get the AND branches parameter
	 * 
	 * @return the maximum number of AND branches
	 */
	public int getAndBranches() {
		return ANDBranches;
	}
	
	/**
	 * Set the XOR branches parameter
	 * 
	 * @param xorBranches the MAXIMUM number of XOR branches
	 * @return the object after the modification
	 */
	public RandomizationConfiguration setXorBranches(int xorBranches) {
		XORBranches = (xorBranches > 1)? xorBranches : MAX_XOR_BRANCHES;
		return this;
	}
	
	/**
	 * Get the XOR branches parameter
	 * 
	 * @return the maximum number of XOR branches
	 */
	public int getXorBranches() {
		return XORBranches;
	}
	
	/**
	 * Set the loop weight parameter
	 * 
	 * @param loopWeight
	 * @return the object after the modification
	 */
	public RandomizationConfiguration setLoopWeight(double loopWeight) {
		weights.put(RANDOMIZATION_PATTERN.LOOP,
				(loopWeight >= 0.0 && loopWeight <= 1.0)?
					loopWeight :
					BASIC_VALUES.weights.get(RANDOMIZATION_PATTERN.LOOP));
		return this;
	}
	
	/**
	 * Get the current value of the loop weight parameter
	 * 
	 * @return the value of the parameter
	 */
	public double getLoopWeight() {
		return weights.get(RANDOMIZATION_PATTERN.LOOP);
	}
	
	/**
	 * Set the single activity weight parameter
	 * 
	 * @param singleActivityWeight
	 * @return the object after the modification
	 */
	public RandomizationConfiguration setSingleActivityWeight(double singleActivityWeight) {
		weights.put(RANDOMIZATION_PATTERN.SINGLE_ACTIVITY,
				(singleActivityWeight >= 0.0 && singleActivityWeight <= 1.0)?
						singleActivityWeight :
						BASIC_VALUES.weights.get(RANDOMIZATION_PATTERN.SINGLE_ACTIVITY));
		return this;
	}
	
	/**
	 * Get the current value of the single activity weight parameter
	 * 
	 * @return the value of the parameter
	 */
	public double getSingleActivityWeight() {
		return weights.get(RANDOMIZATION_PATTERN.SINGLE_ACTIVITY);
	}
	
	/**
	 * Set the skip weight parameter
	 * 
	 * @param skipWeight
	 * @return the object after the modification
	 */
	public RandomizationConfiguration setSkipWeight(double skipWeight) {
		weights.put(RANDOMIZATION_PATTERN.SKIP,
				(skipWeight >= 0.0 && skipWeight <= 1.0)?
						skipWeight :
							BASIC_VALUES.weights.get(RANDOMIZATION_PATTERN.SKIP));
		return this;
	}
	
	/**
	 * Get the current value of the skip weight parameter
	 * 
	 * @return the value of the parameter
	 */
	public double getSkipWeight() {
		return weights.get(RANDOMIZATION_PATTERN.SKIP);
	}
	
	/**
	 * Set the sequence weight parameter
	 * 
	 * @param sequenceWeight
	 * @return the object after the modification
	 */
	public RandomizationConfiguration setSequenceWeight(double sequenceWeight) {
		weights.put(RANDOMIZATION_PATTERN.SEQUENCE,
				(sequenceWeight >= 0.0 && sequenceWeight <= 1.0)?
						sequenceWeight :
							BASIC_VALUES.weights.get(RANDOMIZATION_PATTERN.SEQUENCE));
		return this;
	}
	
	/**
	 * Get the current value of the sequence weight parameter
	 * 
	 * @return the value of the parameter
	 */
	public double getSequenceWeight() {
		return weights.get(RANDOMIZATION_PATTERN.SEQUENCE);
	}
	
	/**
	 * Set the AND weight parameter
	 * 
	 * @param ANDWeight
	 * @return the object after the modification
	 */
	public RandomizationConfiguration setANDWeight(double ANDWeight) {
		weights.put(RANDOMIZATION_PATTERN.PARALLEL_EXECUTION,
				(ANDWeight >= 0.0 && ANDWeight <= 1.0)?
						ANDWeight :
							BASIC_VALUES.weights.get(RANDOMIZATION_PATTERN.PARALLEL_EXECUTION));
		return this;
	}
	
	/**
	 * Get the current value of the AND weight parameter
	 * 
	 * @return the value of the parameter
	 */
	public double getANDWeight() {
		return weights.get(RANDOMIZATION_PATTERN.PARALLEL_EXECUTION);
	}
	
	/**
	 * Set the XOR weight parameter
	 * 
	 * @param XORWeight
	 * @return the object after the modification
	 */
	public RandomizationConfiguration setXORWeight(double XORWeight) {
		weights.put(RANDOMIZATION_PATTERN.MUTUAL_EXCLUSION,
				(XORWeight >= 0.0 && XORWeight <= 1.0)?
						XORWeight :
							BASIC_VALUES.weights.get(RANDOMIZATION_PATTERN.MUTUAL_EXCLUSION));
		return this;
	}
	
	/**
	 * Get the current value of the XOR weight parameter
	 * 
	 * @return the value of the parameter
	 */
	public double getXORWeight() {
		return weights.get(RANDOMIZATION_PATTERN.MUTUAL_EXCLUSION);
	}
	
	/**
	 * Set the maximum depth parameter
	 * 
	 * @param depth
	 * @return the object after the modification
	 */
	public RandomizationConfiguration setDepth(int depth) {
		this.maxDepth = (depth > 0)? depth : BASIC_VALUES.maxDepth;
		return this;
	}
	
	/**
	 * Set the probability to generate data objects
	 * 
	 * @param dataObjectProbability
	 * @return the object after the modification
	 */
	public RandomizationConfiguration setDataObjectProbability(double dataObjectProbability) {
		this.dataObjectProbability = (dataObjectProbability <= 1 || dataObjectProbability >= 0)?
				dataObjectProbability : BASIC_VALUES.dataObjectProbability;
		return this;
	}
	
	/**
	 * Get the current value of the data object probability
	 * 
	 * @return the value of the parameter
	 */
	public double getDataObjectProbability() {
		return dataObjectProbability;
	}
	
	/**
	 * Get the current value of the maximum depth parameter
	 * 
	 * @return the value of the parameter
	 */
	public int getMaximumDepth() {
		return maxDepth;
	}
	
	/**
	 * This method return the number of AND branches to generate, according to
	 * the given weight
	 * 
	 * @return the number of AND branches to generate
	 */
	public int getRandomANDBranches() {
		return Random.nextInt(MIN_AND_BRANCHES, getAndBranches() - 1);
	}
	
	/**
	 * This method return the number of XOR branches to generate, according to
	 * the given weight
	 * 
	 * @return the number of XOR branches to generate
	 */
	public int getRandomXORBranches() {
		return Random.nextInt(MIN_XOR_BRANCHES, getXorBranches() - 1);
	}
	
	/**
	 * This method is used for the definition of the presence of a loop
	 * 
	 * @return true if a loop must be inserted, false otherwise
	 */
	public boolean getLoopPresence() {
		return Random.randomFromWeight(getLoopWeight());
	}
	
	/**
	 * This method returns a pattern, randomly selected between:
	 * <ul>
	 * 	<li>Single activity</li>
	 * 	<li>Sequence pattern</li>
	 * 	<li>AND pattern</li>
	 * 	<li>XOR pattern</li>
	 * 	<li>Skip (according to the parameter)</li>
	 * 	<li>Loop (according to the parameter)</li>
	 * </ul>
	 * 
	 * <p> The selection is done according to the given probabilities
	 * 
	 * @param canLoop specifies whether the pattern can be a loop
	 * @param canSkip specifies whether the pattern can be a skip
	 * @return the random pattern
	 */
	public RANDOMIZATION_PATTERN getRandomPattern(boolean canLoop, boolean canSkip) {
		Set<RANDOMIZATION_PATTERN> options = new HashSet<RANDOMIZATION_PATTERN>();
		options.add(RANDOMIZATION_PATTERN.SINGLE_ACTIVITY);
		options.add(RANDOMIZATION_PATTERN.SEQUENCE);
		options.add(RANDOMIZATION_PATTERN.PARALLEL_EXECUTION);
		options.add(RANDOMIZATION_PATTERN.MUTUAL_EXCLUSION);
		if (canSkip) {
			options.add(RANDOMIZATION_PATTERN.SKIP);
		}
		if (canLoop) {
			options.add(RANDOMIZATION_PATTERN.LOOP);
		}
		
		return getRandomPattern(options.toArray(new RANDOMIZATION_PATTERN[options.size()]));
	}
	
	/**
	 * This method returns a random pattern, selected between the provided ones
	 * 
	 * <p>
	 * The selection is done according to the given probabilities
	 * 
	 * @param patterns the patterns to choose from
	 * @return the random pattern
	 */
	public RANDOMIZATION_PATTERN getRandomPattern(RANDOMIZATION_PATTERN ... patterns) {
		Set<Pair<RANDOMIZATION_PATTERN, Double>> options = new HashSet<Pair<RANDOMIZATION_PATTERN, Double>>();
		for(RANDOMIZATION_PATTERN p : patterns) {
			options.add(new Pair<RANDOMIZATION_PATTERN, Double>(p, weights.get(p)));
		}
		return SetUtils.getRandomWeighted(options);
	}
	
	/**
	 * This method tells the requester whether it is necessary to generate a
	 * data object, according to the value set for the
	 * {@link #dataObjectProbability} variable.
	 * 
	 * @return <tt>true</tt> if a data object needs to be created,
	 * <tt>false</tt> otherwise
	 */
	public boolean generateDataObject() {
		return Random.randomFromWeight(dataObjectProbability);
	}
	
	@Override
	public String toString() {
		String toRet = "";
		toRet += "And Branches = " + getAndBranches() + "\n";
		toRet += "Xor Branches = " + getXorBranches() + "\n";
		toRet += "Loop Weight = " + getLoopWeight() + "\n";
		toRet += "Single Activity Weight = " + getSingleActivityWeight() + "\n";
		toRet += "Skip Weight = " + getSkipWeight() + "\n";
		toRet += "Sequence Weight = " + getSequenceWeight() + "\n";
		toRet += "AND Weight = " + getANDWeight() + "\n";
		toRet += "XOR Weight = " + getXORWeight() + "\n";
		toRet += "Maximum Depth = " + getMaximumDepth() + "\n";
		toRet += "Data Object Probability = " + getDataObjectProbability() + "\n";
		return toRet;
	}
}
