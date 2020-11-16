import java.util.Arrays;

public class CircularSuffixArray {

    private final int[] sortedIndexes;
    private final int n;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        n = s.length();
        // Arrays.sort with comparator pass 99%
        // must use 3-way quick sort to pass 100%
        sortedIndexes = threeWaySort(s);
    }

    private int[] threeWaySort(String s) {
        int n = s.length();
        int[] arr = createArray(n);
        sort(s, arr, 0, n-1, 0);
        return arr;
    }

    private void sort(String s, int[] arr, int lo, int hi, int d) {
        if (lo >= hi) return;
        char pivot = charAt(s, arr[lo] + d);
        int left = lo, curr = lo + 1, right = hi;
        // color sort
        while (curr <= right) {
            char c = charAt(s, arr[curr] + d);
            if (c > pivot) swap(curr, right--, arr);
            else if (c < pivot) swap(left++, curr++, arr);
            else curr++;
        }
        sort(s, arr, lo, left - 1, d);
        // d cannot greater than n
        if (d < s.length()) sort(s, arr, left, right, d + 1);
        sort(s, arr, right + 1, hi, d);
    }

    private char charAt(String s, int index) {
        return s.charAt(index % s.length());
    }

    private void swap(int i, int j, int[] arr) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // LSD sort pass 97%
    private int[] LSDSort(String s) {
        int[] sortedIndexes = createArray(n);
        int[] aux = new int[n];
        for (int d = 0; d < n; d++) {
            int[] count = new int[257];
            for (int i = 0; i < n; i++) {
                int index = (sortedIndexes[i] - 1 - d + n) % n;
                count[s.charAt(index) + 1]++;
            }
            for (int i = 1; i < 257; i++) {
                count[i] += count[i - 1];
            }
            for (int i = 0; i < n; i++) {
                int index = (sortedIndexes[i] - 1 - d + n) % n;
                aux[count[s.charAt(index)]++] = sortedIndexes[i];
            }
            for (int i = 0; i < n; i++) {
                sortedIndexes[i] = aux[i];
            }
        }
        return sortedIndexes;
    }

    private int[] createArray(int n) {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = i;
        }
        return arr;
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

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray c = new CircularSuffixArray("BAABA");
        System.out.println(c.index(0));
    }

}