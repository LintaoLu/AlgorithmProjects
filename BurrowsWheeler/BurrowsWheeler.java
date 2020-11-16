import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String input = readString();
        CircularSuffixArray csa = new CircularSuffixArray(input);
        int start = -1, n = input.length();
        char[] sorted = new char[n];
        for (int i = 0; i < n; i++) {
            int index = csa.index(i);
            if (index == 0) start = i;
            int pre = (index - 1 + n) % n;
            sorted[i] = input.charAt(pre);
        }
        BinaryStdOut.write(start);
        BinaryStdOut.write(new String(sorted));
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int start = BinaryStdIn.readInt();
        String input = readString();
        int n = input.length();
        // counting sort
        int[] count = new int[257];
        for (int i = 0; i < n; i++) {
            char c = input.charAt(i);
            count[c+1]++;
        }
        // actual index
        for (int i = 1; i < 257; i++) {
            count[i] += count[i-1];
        }

        char[] sorted = new char[n];
        int[] next = new int[n];
        for (int i = 0; i < n; i++) {
            char c = input.charAt(i);
            next[count[c]] = i;
            sorted[count[c]] = c;
            count[c]++;
        }

        int curr = start;
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(sorted[curr]);
            curr = next[curr];
        }
        BinaryStdOut.close();
    }

    private static String readString() {
        StringBuilder sb = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            // if (c == '\n') continue;
            sb.append(c);
        }
        return sb.toString();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if ("-".equals(args[0])) {
            transform();
        } else if ("+".equals(args[0])) {
            inverseTransform();
        }
    }

}