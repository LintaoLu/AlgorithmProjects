import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final int n, trials;
    private final double coefficient;
    private final double[] res;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        this.n = n;
        coefficient = 1.96;
        this.trials = trials;
        res = new double[trials];
        percolation();
    }

    // test percolation "trials" times
    private void percolation() {
        for (int i = 0; i < trials; i++) {
            // 0-based indices
            int[] indices = StdRandom.permutation(n * n);
            Percolation p = new Percolation(n);
            for (int j = 0; j < n * n; j++) {
                int index = indices[j];
                p.open(index / n + 1, index % n + 1);
                if (p.percolates()) {
                    // store ith test result
                    res[i] = (double) p.numberOfOpenSites() / (n * n);
                    break;
                }
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(res);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(res);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - coefficient * stddev() / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + coefficient * stddev() / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        StdOut.println("mean                    = " + stats.mean());
        StdOut.println("stddev                  = " + stats.stddev());
        StdOut.println("95% confidence interval = [" + stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }
}