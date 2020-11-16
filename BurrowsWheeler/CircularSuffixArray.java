import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {

    private final Integer[] sortedIndexes;
    private final int n;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException();
        n = s.length();
        sortedIndexes = new Integer[n];
        for (int i = 0; i < n; i++) {
            sortedIndexes[i] = i;
        }
        Arrays.sort(sortedIndexes, new MyComparator(s));
    }

    // length of s
    public int length() {
        return n;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length())
            throw new IllegalArgumentException();
        return sortedIndexes[i];
    }

    private static class MyComparator implements Comparator<Integer> {
        private final String s;

        MyComparator(String s) { this.s = s; }

        @Override
        public int compare(Integer i, Integer j) {
            int n = s.length();
            for (int k = 0; k < n; k++) {
                int ii = (i + k) % n, jj = (j + k) % n;
                if (s.charAt(ii) == s.charAt(jj)) continue;
                return s.charAt(ii) - s.charAt(jj);
            }
            return 0;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray c = new CircularSuffixArray("AAA");
        System.out.println(Arrays.toString(c.sortedIndexes));
        System.out.println(c.index(0));
    }

}