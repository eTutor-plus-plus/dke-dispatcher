package at.jku.dke.etutor.modules.nf.exercises;

public class RandomCyphersGenerator {

	private int minCypher;
	private int maxCypher;
	private int lastCypher;

	private boolean directDuplicates;
	
	public RandomCyphersGenerator(){
		super();
		
		this.minCypher = 0;
		this.maxCypher = 9;
		this.lastCypher = 0;
		this.directDuplicates = true;
	}
	
	public RandomCyphersGenerator(int minCypher, int maxCypher, boolean directDuplicates){
		this();
		this.minCypher = minCypher;
		this.maxCypher = maxCypher;
		this.directDuplicates = directDuplicates;
	}

	public int nextCypher(){
		int nextCypher;
		String currentTimeMillis;

		currentTimeMillis = Long.toString(System.currentTimeMillis());
		nextCypher = Integer.parseInt(currentTimeMillis.substring(currentTimeMillis.length()-1));

		if (!this.directDuplicates){
			while ((nextCypher == this.lastCypher) || (nextCypher > this.maxCypher) || (nextCypher < this.minCypher)){
				currentTimeMillis = Long.toString(System.currentTimeMillis());
				nextCypher = Integer.parseInt(currentTimeMillis.substring(currentTimeMillis.length()-1));
			}
			this.lastCypher = nextCypher;
		}
		
		return nextCypher;
	}
}
