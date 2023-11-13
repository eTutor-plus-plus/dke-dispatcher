package at.jku.dke.etutor.modules.nf.exercises;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

// TODO: Now that we have generics, do we even need (most of) this? (Gerald Wimmer, 2023-11-13)
public class TupleSet extends TreeSet implements Set {

	public TupleSet() {
		super(new TupleComparator());
	}
	
	public int[] get(int pos){
		return (int[])this.toArray()[pos];
	}

	public boolean add(int[] tuple){
		return super.add(tuple);
	}

	public boolean contains(Object o) {
		return super.contains(o);
	}

	public Object[] toArray(Object[] a) {
		return super.toArray(a);
	}

	public boolean add(Object o) {
		return super.add(o);
	}

	public boolean remove(Object o) {
		return super.remove(o);
	}

	public boolean containsAll(Collection c) {
		return super.containsAll(c);
	}

	public boolean addAll(Collection c) {
		return super.addAll(c);
	}

	public boolean retainAll(Collection c) {
		return super.retainAll(c);
	}

	public boolean removeAll(Collection c) {
		return super.removeAll(c);
	}
	
	public static void main(String[] args){
		TupleSet set = new TupleSet();
		
		int[] t1 = new int[]{1,2};		
		int[] t2 = new int[]{1,2};		

		System.out.println(set.add(t1));
		System.out.println(set.contains(t2));
	}
}