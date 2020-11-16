import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.LinkedList;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> alphabet = getAlphabet();
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            // if (c == '\n') continue;
            int index = alphabet.indexOf(c);
            alphabet.remove(index);
            alphabet.addFirst(c);
            BinaryStdOut.write(index, 8);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> alphabet = getAlphabet();
        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readChar();
            char c = alphabet.remove(index);
            alphabet.addFirst(c);
            BinaryStdOut.write(c);
        }
        BinaryStdOut.close();
    }

    private static LinkedList<Character> getAlphabet() {
        LinkedList<Character> alphabet = new LinkedList<>();
        for (char c = 0; c < 256; c++) alphabet.add(c);
        return alphabet;
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if ("-".equals(args[0])) {
            encode();
        } else if ("+".equals(args[0])) {
            decode();
        }
    }

}