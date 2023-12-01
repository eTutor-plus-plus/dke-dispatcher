package at.jku.dke.etutor.modules.nf.analysis;

import java.math.BigInteger;

public class CombinationGenerator {

	private final int n;
	private final int r;
	private final int[] a;
	private final BigInteger total;
	private BigInteger numLeft;

	public CombinationGenerator(int n, int r) {
		if (r > n) {
			throw new IllegalArgumentException();
		}
		if (n < 1) {
			throw new IllegalArgumentException();
		}

		this.n = n;
		this.r = r;

		a = new int[r];
		BigInteger nFact = getFactorial(n);
		BigInteger rFact = getFactorial(r);
		BigInteger nminusrFact = getFactorial(n - r);
		total = nFact.divide(rFact.multiply(nminusrFact));
		reset();
	}

	public void reset() {
		for (int i = 0; i < a.length; i++) {
			a[i] = i;
		}
		numLeft = new BigInteger(total.toString());
	}

	public BigInteger getNumLeft() {
		return numLeft;
	}

	public boolean hasMore() {
		return numLeft.compareTo(BigInteger.ZERO) > 0;
	}

	public BigInteger getTotal() {
		return total;
	}

	private static BigInteger getFactorial(int n) {
		BigInteger fact = BigInteger.ONE;
		for (int i = n; i > 1; i--) {
			fact = fact.multiply(new BigInteger(Integer.toString(i)));
		}
		return fact;
	}

	public int[] getNext() {

		if (numLeft.equals(total)) {
			numLeft = numLeft.subtract(BigInteger.ONE);
			return a;
		}

		int i = r - 1;
		while (a[i] == n - r + i) {
			i--;
		}
		a[i] = a[i] + 1;
		for (int j = i + 1; j < r; j++) {
			a[j] = a[i] + j - i;
		}

		numLeft = numLeft.subtract(BigInteger.ONE);
		return a;
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
		Vector keyCandidates;

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