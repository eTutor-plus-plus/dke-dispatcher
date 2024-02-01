package at.jku.dke.etutor.modules.nf.exercises;

import java.util.TreeSet;

// TODO: Now that we have generics, do we even need this? (Gerald Wimmer, 2023-11-13)
public class TupleSet extends TreeSet<int[]> {

	public TupleSet() {
		super(new TupleComparator());
	}
	
	public int[] get(int pos){
		return (int[])this.toArray()[pos];
	}
	
	public static void main(String[] args){
		TupleSet set = new TupleSet();
		
		int[] t1 = new int[]{1,2};		
		int[] t2 = new int[]{1,2};		

		System.out.println(set.add(t1));
		System.out.println(set.contains(t2));
	}
}