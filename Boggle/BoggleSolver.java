import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {

    private final int[][] dirs;
    private final Node root;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null) throw new IllegalArgumentException();
        dirs = new int[][] {{-1, 0}, {0, -1}, {1, 0}, {0, 1}, {-1, 1}, {1, -1}, {-1, -1}, {1, 1}};
        root = new Node();
        for (String word: dictionary) add(word);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        int m = board.rows(), n = board.cols();
        Set<String> set = new HashSet<>();
        boolean[][] visited = new boolean[m][n];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                findWords(i, j, sb, board, visited, root, set);
            }
        }
        return set;
    }

    private void findWords(int i, int j, StringBuilder sb, BoggleBoard board,
                           boolean[][] visited, Node curr, Set<String> set) {
        if (invalidIndex(i, j, board) || visited[i][j]) return;
        char c = board.getLetter(i, j);
        curr = curr.next[c - 'A'];
        if (curr == null) return;
        sb.append(c);
        boolean isQU = false;
        if (c == 'Q') {
            c = 'U';
            curr = curr.next[c - 'A'];
            if (curr == null) {
                sb.setLength(sb.length() - 1);
                return;
            }
            sb.append(c);
            isQU = true;
        }
        visited[i][j] = true;
        if (curr.isEnd && sb.length() >= 3) set.add(sb.toString());
        for (int[] dir : dirs) {
            int x = i + dir[0], y = j + dir[1];
            findWords(x, y, sb, board, visited, curr, set);
        }
        visited[i][j] = false;
        int n = sb.length();
        if (isQU) sb.setLength(n - 2);
        else sb.setLength(n - 1);
    }

    private boolean invalidIndex(int i, int j, BoggleBoard board) {
        int m = board.rows(), n = board.cols();
        return i < 0 || i >= m || j < 0 || j >= n;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null || !contains(word)) return 0;
        int n = word.length();
        if (n < 3) return 0;
        if (n == 3 || n == 4) return 1;
        if (n == 5) return 2;
        if (n == 6) return 3;
        if (n == 7) return 5;
        return 11;
    }

    private void add(String word) {
        Node temp = root;
        for (int i = 0; i < word.length(); i++) {
            int c = word.charAt(i) - 'A';
            if (temp.next[c] == null) temp.next[c] = new Node();
            temp = temp.next[c];
        }
        temp.isEnd = true;
    }

    private boolean contains(String word) {
        Node temp = root;
        for (int i = 0; i < word.length(); i++) {
            int c = word.charAt(i) - 'A';
            if (temp.next[c] == null) return false;
            temp = temp.next[c];
        }
        return temp.isEnd;
    }

    private static class Node {
        boolean isEnd = false;
        // since word only contains uppercase letters A through Z
        Node[] next = new Node[26];
    }

    public static void main(String[] args) {
        In in = new In("dictionary-algs4.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("board4x4.txt");
        int score = 0, counter = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
            counter++;
        }
        StdOut.println("Score = " + score);
        StdOut.println("Entries = " + counter);
    }

}