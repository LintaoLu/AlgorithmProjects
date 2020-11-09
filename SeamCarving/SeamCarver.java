import edu.princeton.cs.algs4.Picture;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Arrays;

public class SeamCarver {

    private int[][] RGB;
    private double[][] ENERGY;
    private enum Direction { VERTICAL, HORIZONTAL }

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        RGB = getRGBArray(picture);
        ENERGY = getEnergyArray(picture);
    }

    private int[][] getRGBArray(Picture picture) {
        int m = picture.height(), n = picture.width();
        int[][] RGB = new int[m][n];
        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {
                RGB[row][col] = picture.getRGB(col, row);
            }
        }
        return RGB;
    }

    private double[][] getEnergyArray(Picture picture) {
        int m = picture.height(), n = picture.width();
        double[][] ENERGY = new double[m][n];
        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {
                ENERGY[row][col] = energy(col, row);
            }
        }
        return ENERGY;
    }

    // current picture
    public Picture picture() {
        int m = height(), n = width();
        Picture res = new Picture(n, m);
        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {
                res.setRGB(col, row, RGB[row][col]);
            }
        }
        return res;
    }

    // width of current picture
    public int width() {
        return RGB[0].length;
    }

    // height of current picture
    public int height() {
        return RGB.length;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        return reverse(y, x);
    }

    private double reverse(int x, int y) {
        if (invalidIndices(x, y)) throw new IllegalArgumentException();
        if (x == 0 || x == height() - 1 || y == 0 || y == width() - 1) {
            return 1000;
        }
        int[][] offset = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int sum = 0;
        for (int i = 0; i < 4; i += 2) {
            int x1 = x + offset[i][0], y1 = y + offset[i][1];
            int x2 = x + offset[i + 1][0], y2 = y + offset[i + 1][1];

            int[] rgb1 = decode(RGB[x1][y1]);
            int[] rgb2 = decode(RGB[x2][y2]);

            for (int j = 0; j < 3; j++) {
                sum += (rgb2[j] - rgb1[j]) * (rgb2[j] - rgb1[j]);
            }
        }
        return Math.sqrt(sum);
    }

    // decode RGB
    private int[] decode(int rgb) {
        int[] res = new int[3];
        for (int i = 0; i < res.length; i++) {
            res[i] = rgb >> ((2 - i) * 8) & 0xFF;
        }
        return res;
    }

    // check if the 2D index is valid
    private boolean invalidIndices(int x, int y) {
        return x < 0 || x >= height() || y < 0 || y >= width();
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return shortestPath(Direction.HORIZONTAL);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return shortestPath(Direction.VERTICAL);
    }

    // algorithm is actually BFS since graph is a sorted DAG
    private int[] shortestPath(Direction direction) {
        double[][] dis = new double[height()][width()];
        int[] path = new int[height() * width()];
        Queue<int[]> queue = new LinkedList<>();
        prepare(dis, path, queue, direction);
        // last cell (index) in shortest path
        int last = -1;
        while (!queue.isEmpty()) {
            int size = queue.size();
            double len = Double.POSITIVE_INFINITY;
            for (int i = 0; i < size; i++) {
                int[] from = queue.poll();
                int x = from[0], y = from[1];
                for (int[] to : adj(x, y, direction)) {
                    int xx = to[0], yy = to[1];
                    if (dis[x][y] + ENERGY[xx][yy] >= dis[xx][yy]) continue;
                    dis[xx][yy] = dis[x][y] + ENERGY[xx][yy];
                    if (dis[xx][yy] < len) {
                        last = get1DIndex(xx, yy);
                        len = dis[xx][yy];
                    }
                    path[get1DIndex(xx, yy)] = get1DIndex(x, y);
                    queue.offer(to);
                }
            }
        }
        // get shortest path
        return getShortestPath(path, last, direction);
    }

    // find the path
    private int[] getShortestPath(int[] path, int last, Direction dir) {
        int[] res = new int[0];
        if (dir == Direction.VERTICAL) res = new int[height()];
        else if (dir == Direction.HORIZONTAL) res = new int[width()];
        int index = res.length - 1;
        while (last >= 0) {
            int[] indices = get2DIndex(last);
            if (dir == Direction.VERTICAL) {
                res[index--] = indices[1];
            } else if (dir == Direction.HORIZONTAL) {
                res[index--] = indices[0];
            }
            last = path[last];
        }
        return res;
    }

    // prepare shortest path data structures
    private void prepare(double[][] dis, int[] path, Queue<int[]> queue, Direction dir) {
        for (double[] row : dis) Arrays.fill(row, Double.POSITIVE_INFINITY);
        Arrays.fill(path, -1);
        if (dir == Direction.VERTICAL) {
            for (int i = 0; i < width(); i++) {
                queue.offer(new int[] {0, i});
                dis[0][i] = ENERGY[0][i];
            }
        } else if (dir == Direction.HORIZONTAL) {
            for (int i = 0; i < height(); i++) {
                queue.offer(new int[] {i, 0});
                dis[i][0] = ENERGY[i][0];
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    // get neighbors according to direction
    private Iterable<int[]> adj(int x, int y, Direction dir) {
        List<int[]> res = new ArrayList<>();
        int[] dirs = { -1, 0, 1};
        for (int offset : dirs) {
            int xx = -1, yy = -1;
            if (dir == Direction.VERTICAL) {
                xx = x + 1;
                yy = y + offset;
            } else if (dir == Direction.HORIZONTAL) {
                xx = x + offset;
                yy = y + 1;
            }
            if (invalidIndices(xx, yy)) continue;
            res.add(new int[]{xx, yy});
        }
        return res;
    }

    private int get1DIndex(int x, int y) {
        return x * width() + y;
    }

    private int[] get2DIndex(int index) {
        return new int[] { index / width(), index % width() };
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (height() <= 1) throw new IllegalArgumentException();
        validateSeam(seam, Direction.HORIZONTAL);
        update(seam, Direction.HORIZONTAL);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (width() <= 1) throw new IllegalArgumentException();
        validateSeam(seam, Direction.VERTICAL);
        update(seam, Direction.VERTICAL);
    }

    private void validateSeam(int[] seam, Direction dir) {
        if (seam == null) throw new IllegalArgumentException();
        if (dir == Direction.VERTICAL) {
            if (seam.length != height()) throw new IllegalArgumentException();
            for (int i = 0; i < seam.length; i++) {
                // out of bound
                if (invalidIndices(i, seam[i])) {
                    throw new IllegalArgumentException();
                }
                // diff more than 1
                if (i > 0 && Math.abs(seam[i] - seam[i-1]) > 1) {
                    throw new IllegalArgumentException();
                }
            }
        } else if (dir == Direction.HORIZONTAL) {
            if (seam.length != width()) throw new IllegalArgumentException();
            for (int i = 0; i < seam.length; i++) {
                if (invalidIndices(seam[i], i)) {
                    throw new IllegalArgumentException();
                }
                if (i > 0 && Math.abs(seam[i] - seam[i-1]) > 1) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }

    private void update(int[] seam, Direction dir) {
        if (dir == Direction.VERTICAL) {
            RGB = removeVRGB(seam);
            ENERGY = removeVEnergy(seam);
        } else if (dir == Direction.HORIZONTAL) {
            RGB = removeHRGB(seam);
            ENERGY = removeHEnergy(seam);
        }
    }

    private int[][] removeVRGB(int[] seam) {
        int m = RGB.length, n = RGB[0].length;
        int[][] tRGB = new int[m][n-1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (j < seam[i]) tRGB[i][j] = RGB[i][j];
                else if (j > seam[i]) tRGB[i][j-1] = RGB[i][j];
            }
        }
        return tRGB;
    }

    private double[][] removeVEnergy(int[] seam) {
        int m = ENERGY.length, n = ENERGY[0].length;
        double[][] tENERGY = new double[m][n-1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (j < seam[i]) tENERGY[i][j] = ENERGY[i][j];
                else if (j > seam[i]) tENERGY[i][j-1] = ENERGY[i][j];
            }
        }
        // only calculate energies next to seam
        reCalculateVEnergy(seam, tENERGY);
        return tENERGY;
    }

    // must update RGB array before!
    private void reCalculateVEnergy(int[] seam, double[][] tENERGY) {
        for (int i = 0; i < seam.length; i++) {
            if (!invalidIndices(i, seam[i]-1))
                tENERGY[i][seam[i]-1] = energy(seam[i]-1, i);
            if (!invalidIndices(i, seam[i]))
                tENERGY[i][seam[i]] = energy(seam[i], i);
        }
    }

    private int[][] removeHRGB(int[] seam) {
        int m = RGB.length, n = RGB[0].length;
        int[][] tRGB = new int[m-1][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (j > seam[i]) tRGB[j-1][i] = RGB[j][i];
                else if (j < seam[i]) tRGB[j][i] = RGB[j][i];
            }
        }
        return tRGB;
    }

    private double[][] removeHEnergy(int[] seam) {
        int m = ENERGY.length, n = ENERGY[0].length;
        double[][] tENERGY = new double[m-1][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (j > seam[i]) tENERGY[j-1][i] = ENERGY[j][i];
                else if (j < seam[i]) tENERGY[j][i] = ENERGY[j][i];
            }
        }
        reCalculateHEnergy(seam, tENERGY);
        return tENERGY;
    }

    private void reCalculateHEnergy(int[] seam, double[][] tENERGY) {
        for (int i = 0; i < seam.length; i++) {
            if (!invalidIndices(seam[i]-1, i))
                tENERGY[seam[i]-1][i] = energy(i, seam[i]-1);
            if (!invalidIndices(seam[i], i))
                tENERGY[seam[i]][i] = energy(i, seam[i]);
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture("6x5.png");
        SeamCarver seamCarver = new SeamCarver(picture);
        for (double[] row : seamCarver.ENERGY) {
            System.out.println(Arrays.toString(row));
        }
    }

}