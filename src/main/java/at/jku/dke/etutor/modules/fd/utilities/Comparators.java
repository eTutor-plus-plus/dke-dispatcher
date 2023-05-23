package at.jku.dke.etutor.modules.fd.utilities;

import java.util.Arrays;
import java.util.Comparator;

public class Comparators {

    public static class ArrayComparator implements Comparator<String []> {
        @Override public int compare(String[] e1, String[] e2) {
            return Arrays.compare(e1, e2);
        }
    }
}
