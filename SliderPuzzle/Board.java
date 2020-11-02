import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Note: You may also assume that 2 ≤ n < 128.
 * */

public class Board {

    private final int[][] tiles;
    private int hamming, manhattan;
    private List<Board> neighbors;
    private String strBoard;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null || tiles.length == 0
                || tiles.length != tiles[0].length) {
            throw new IllegalArgumentException();
        }
        // copy constructor
        this.tiles = copyTiles(tiles);
        computeDistances(this.tiles);
    }

    // calculate hamming and manhattan distance
    private void computeDistances(int[][] tiles) {
        int n = tiles.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int curr = tiles[i][j];
                // don't need to calculate distances for 0
                if (curr == 0) continue;
                int target = i == n - 1 && j == n - 1 ? 0 : i * n + j + 1;
                if (curr == target) continue;
                hamming++;
                int row = (curr - 1) / n, col = (curr - 1) % n;
                manhattan += Math.abs(row - i) + Math.abs(col - j);
            }
        }
    }

    // find all children
    private void findNeighbours(int[][] tiles) {
        int n = tiles.length;
        int row = -1, col = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    row = i;
                    col = j;
                }
            }
        }

        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : dirs) {
            int x = row + dir[0];
            int y = col + dir[1];
            if (!validIndices(x, y, n)) continue;
            int[][] neighbor = copyTiles(tiles);
            swap(x, y, row, col, neighbor);
            neighbors.add(new Board(neighbor));
        }
    }

    // swap two element form two dimensional array
    private void swap(int x1, int y1, int x2, int y2, int[][] arr) {
        int temp = arr[x1][y1];
        arr[x1][y1] = arr[x2][y2];
        arr[x2][y2] = temp;
    }

    // check if 2-d index is out of bound
    private boolean validIndices(int x, int y, int n) {
        return 0 <= x && x < n && 0 <= y && y < n;
    }

    // string representation of this board
    public String toString() {
        // cache string board, only run loops one time
        if (strBoard != null) return strBoard;
        StringBuilder sb = new StringBuilder();
        for (int[] row : tiles) {
            for (int num : row) {
                sb.append(getStringNum(num)).append(" ");
            }
            sb.append("\n");
        }
        return strBoard = sb.toString();
    }

    // convert a number to 3 chars string
    private String getStringNum(int num) {
        // You may also assume that 2 ≤ n < 128.
        StringBuilder str = new StringBuilder(String.valueOf(num));
        int diff = 3 - str.length();
        for (int i = 0; i < diff; i++) {
            str.insert(0, "0");
        }
        return str.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
   }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (!(y instanceof Board)) return false;

        Board that = (Board) y;
        if (that.dimension() != dimension() ||
                that.manhattan != manhattan ||
                that.hamming != hamming) {
            return false;
        }

        int n = dimension();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        // cache neighbours, only be called 1 time
        if (neighbors != null) return neighbors;
        neighbors = new ArrayList<>();
        findNeighbours(tiles);
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[][] arr = copyTiles(tiles);
        // n is at least 2
        if (arr[0][0] != 0 && arr[0][1] != 0) swap(0, 0, 0, 1, arr);
        else if (arr[0][0] != 0 && arr[1][0] != 0) swap(0, 0, 1, 0, arr);
        else swap(0, 1, 1, 0, arr);
        return new Board(arr);
    }

    // deep copy a 2-d integer array
    private int[][] copyTiles(int[][] arr) {
        int m = arr.length, n = arr[0].length;
        int[][] copy = new int[m][n];
        for (int i = 0; i < m; i++) {
            copy[i] = Arrays.copyOf(arr[i], n);
        }
        return copy;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] arr1 = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board board1 = new Board(arr1);
        Board board2 = board1.twin();
        System.out.println(board1);
        System.out.println(board1.hamming + " " + board1.manhattan);
        System.out.println(board2);
        for (Board neighbor : board1.neighbors()) {
            System.out.println(neighbor);
        }
    }

}