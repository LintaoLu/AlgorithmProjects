import edu.princeton.cs.algs4.MinPQ;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Solver {

    private boolean solvable;
    private final Iterable<Board> path;
    private int steps;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        path = solve(initial);
    }

    private List<Board> solve(Board initial) {
        Board twin = initial.twin();
        MinPQ<GameTreeNode> mainPQ = new MinPQ<>();
        MinPQ<GameTreeNode> twinPQ = new MinPQ<>();
        mainPQ.insert(new GameTreeNode(initial, null, 0));
        twinPQ.insert(new GameTreeNode(twin, null, 0));
        List<Board> res = new ArrayList<>();

        while (!mainPQ.isEmpty() && !twinPQ.isEmpty()) {
            GameTreeNode mParent = mainPQ.isEmpty() ? null : mainPQ.delMin();
            if (mParent == null) break;
            GameTreeNode tParent = twinPQ.isEmpty() ? null : twinPQ.delMin();

            if (mParent.board.isGoal()) {
                solvable = true;
                findPath(mParent, res);
                break;
            }
            if (tParent != null && tParent.board.isGoal()) {
                solvable = false;
                break;
            }

            findNeighbors(mParent, mainPQ);
            findNeighbors(tParent, twinPQ);
        }
        return res;
    }

    private void findNeighbors(GameTreeNode parent, MinPQ<GameTreeNode> pq) {
        if (parent == null) return;
        for (Board next : parent.board.neighbors()) {
            Board grandparent = parent.pre == null ? null : parent.pre.board;
            if (next.equals(grandparent)) continue;
            GameTreeNode child = new GameTreeNode(next, parent, parent.len + 1);
            pq.insert(child);
        }
    }

    // find path form goal board to initial board
    private void findPath(GameTreeNode node, List<Board> res) {
        do {
            res.add(node.board);
            steps++;
            node = node.pre;
        } while (node != null);
        Collections.reverse(res);
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!solvable) return -1;
        return steps - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) return null;
        return path;
    }

    private static class GameTreeNode implements Comparable<GameTreeNode> {
        final GameTreeNode pre;
        final Board board;
        final int len;
        final int manhattan;

        GameTreeNode(Board curr, GameTreeNode pre, int len) {
            this.pre = pre;
            this.board = curr;
            this.len = len;
            manhattan = curr.manhattan();
        }

        @Override
        public int compareTo(GameTreeNode other) {
            // if manhattan distance are equal, should consider length of the path
            return manhattan + len - other.manhattan - other.len;
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        int[][] arr1 = {
                {1, 2, 3, 4, 5, 7, 14},
                {8, 9, 10, 11, 12, 13, 6},
                {15, 16, 17, 18, 19, 20, 21},
                {22, 23, 24, 25, 26, 27, 28},
                {29, 30, 31, 32,  0, 33, 34},
                {36, 37, 38, 39, 40, 41, 35},
                {43, 44, 45, 46, 47, 48, 42}
        };
        int[][] arr2 = {
                {5, 1, 8},
                {2, 7, 3},
                {4, 0, 6}
        };
        int[][] arr3 = {
                {7,  8,  5},
                {4,  0,  2},
                {3,  6,  1}
        };
        Solver solver = new Solver(new Board(arr1));
        System.out.println(solver.moves());
        System.out.println(solver.isSolvable());
        Iterable<Board> solution = solver.solution();
        for (Board b : solution) System.out.println(b);
    }

}