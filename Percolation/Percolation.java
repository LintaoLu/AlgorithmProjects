import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int n;
    private int openSites;
    private final WeightedQuickUnionUF uf1, uf2;
    private final boolean[] grid;
    private final int[][] dirs;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        this.n = n;
        dirs = new int[][] {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        uf1 = new WeightedQuickUnionUF(n * n + 2);
        uf2 = new WeightedQuickUnionUF(n * n + 1);
        grid = new boolean[n * n];
        for (int i = 0; i < n; i++) {
            uf1.union(n * n, i);
            uf2.union(n * n, i);
            uf1.union(n * n + 1, n * (n - 1) + i);
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int index = get1DIndex(row, col);
        if (grid[index]) return;
        grid[index] = true;
        openSites++;
        for (int[] dir : dirs) {
            int x = row + dir[0], y = col + dir[1];
            // do not connect if neighbour is invalid or not open
            if (badIndices(x, y) || !isOpen(x, y)) continue;
            int neighbour = get1DIndex(x, y);
            uf1.union(index, neighbour);
            uf2.union(index, neighbour);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        int index = get1DIndex(row, col);
        return grid[index];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int index = get1DIndex(row, col);
        if (!grid[index]) return false;
        return uf2.find(n * n) == uf2.find(index);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        // what if input grid only has 1 line? we should check numberOfOpenSites
        return uf1.find(n * n) == uf1.find(n * n + 1) && numberOfOpenSites() > 0;
    }

    // convert 1-based 2D indices to 0-based 1D index
    private int get1DIndex(int row, int col) {
        if (badIndices(row, col)) {
            throw new IllegalArgumentException();
        }
        return (row - 1) * n + col - 1;
    }

    // verify indices: 1-based
    private boolean badIndices(int row, int col) {
        return row < 1 || row > n || col < 1 || col > n;
    }

    // test client (see below)
    public static void main(String[] args) {
        Percolation p = new Percolation(1);
        p.open(1, 1);
        System.out.println(p.percolates());
    }
}
