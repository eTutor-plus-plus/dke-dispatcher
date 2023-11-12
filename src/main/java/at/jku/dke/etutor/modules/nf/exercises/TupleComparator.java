package at.jku.dke.etutor.modules.nf.exercises;

import java.io.Serializable;
import java.util.Comparator;

public class TupleComparator implements Comparator, Serializable {

	public TupleComparator() {
		super();
	}

	public int compare(Object o1, Object o2) {
		int[] tuple1;
		int[] tuple2;
		int maxComparisons;

		tuple1 = (int[])o1;
		tuple2 = (int[])o2;

		if (tuple1.length > tuple2.length){
			return 1;
		}

		if (tuple1.length < tuple2.length){
			return -1;
		} 

		if (tuple1.length <= tuple2.length){
			maxComparisons = tuple1.length;
		} else {
			maxComparisons = tuple2.length;
		}
	
		for (int i=0; i<maxComparisons; i++){
			if (tuple1[i] > tuple2[i]){
				return 1;
			}
		
			if (tuple1[i] < tuple2[i]){
				return -1;
			}
		}
		
		return 0;
	}
	
	public static void main(String[] args){
		TupleComparator comp = new TupleComparator();
		int result;

		int[] i1 = new int[]{5,0};
		int[] i2 = new int[]{5,0};

    result = comp.compare(i1, i2);
    
    if (result == 0){
    	System.out.println("equal");
    } else if (result > 0){
    	System.out.println("1 is grater than 2");
    } else if (result < 0){
			System.out.println("1 is smaller than 2");
    }

	}

}
