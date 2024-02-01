package at.jku.dke.etutor.modules.nf.analysis;

import java.math.BigInteger;

public class CombinationGenerator {

	private final int numberOfElements;
	private final int startingIndex;
	private final int[] allIndices;
	private final BigInteger totalNumberOfCombinations;
	private BigInteger numLeft;

	public CombinationGenerator(int numberOfElements, int startingIndex) {
		if (startingIndex > numberOfElements) {
			throw new IllegalArgumentException();
		}
		if (numberOfElements < 1) {
			throw new IllegalArgumentException();
		}

		this.numberOfElements = numberOfElements;
		this.startingIndex = startingIndex;

		allIndices = new int[startingIndex];
		BigInteger nFact = getFactorial(numberOfElements);
		BigInteger rFact = getFactorial(startingIndex);
		BigInteger nminusrFact = getFactorial(numberOfElements - startingIndex);
		totalNumberOfCombinations = nFact.divide(rFact.multiply(nminusrFact));
		reset();
	}

	public void reset() {
		for (int i = 0; i < allIndices.length; i++) {
			allIndices[i] = i;
		}
		numLeft = new BigInteger(totalNumberOfCombinations.toString());
	}

	public BigInteger getNumLeft() {
		return numLeft;
	}

	public boolean hasMore() {
		return numLeft.compareTo(BigInteger.ZERO) > 0;
	}

	public BigInteger getTotalNumberOfCombinations() {
		return totalNumberOfCombinations;
	}

	private static BigInteger getFactorial(int n) {
		BigInteger fact = BigInteger.ONE;
		for (int i = n; i > 1; i--) {
			fact = fact.multiply(new BigInteger(Integer.toString(i)));
		}
		return fact;
	}

	public int[] getNext() {

		if (numLeft.equals(totalNumberOfCombinations)) {
			numLeft = numLeft.subtract(BigInteger.ONE);
			return allIndices;
		}

		int i = startingIndex - 1;
		while (allIndices[i] == numberOfElements - startingIndex + i) {
			i--;
		}
		allIndices[i] = allIndices[i] + 1;
		for (int j = i + 1; j < startingIndex; j++) {
			allIndices[j] = allIndices[i] + j - i;
		}

		numLeft = numLeft.subtract(BigInteger.ONE);
		return allIndices;
	}
	
	public static void main(String[] args) {
		String[] elements = {"a", "b", "c", "d", "e", "f", "g"};
		int[] indices;
		CombinationGenerator x = new CombinationGenerator (elements.length, 3);
		StringBuilder combination;
		while (x.hasMore ()) {
			combination = new StringBuilder();
			indices = x.getNext ();
            for (int index : indices) {
                combination.append(elements[index]).append(" ");
            }
			System.out.println (combination);
		}
		
		/*
		CombinationGenerator generator;
		int[] indices;
		int[] keyCandidate;
		int[] keyAttributePositions;
		List keyCandidates;

		keyAttributePositions = new int[] { 0, 1, 2, 3 };

		for (int i = 1; i <= keyAttributePositions.length; i++) {
			generator = new CombinationGenerator(keyAttributePositions.length, i);

			while (generator.hasMore()) {
				indices = generator.getNext();
				for (int j = 0; j < indices.length; j++) {
					System.out.print(keyAttributePositions[indices[j]]);
				}
				System.out.print("\n");
			}
		}*/
	}
}