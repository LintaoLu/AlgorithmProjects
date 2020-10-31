import java.util.ArrayList;
import java.util.Arrays;

/**
 * Please note: For simplicity, we will not supply any input to BruteCollinearPoints
 * that has 5 or more collinear points.
 * */

public class BruteCollinearPoints {

    private Point[] points, prePoints;
    private LineSegment[] res;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        this.points = points;
        initialize();
    }

    private LineSegment[] findLine() {
        ArrayList<LineSegment> buffer = new ArrayList<>();
        int n = points.length;
        for (int i = 0; i < n - 3; i++) {
            for (int j = i + 1; j < n - 2; j++) {
                for (int k = j + 1; k < n - 1; k++) {
                    for (int l = k + 1; l < n; l++) {
                        if (points[i].slopeTo(points[j]) == points[j].slopeTo(points[k]) &&
                                points[j].slopeTo(points[k]) == points[k].slopeTo(points[l])) {
                            buffer.add(new LineSegment(points[i], points[l]));
                        }
                    }
                }
            }
        }
        return buffer.toArray(new LineSegment[0]);
    }

    // the number of line segments
    public int numberOfSegments() {
        if (isModified()) initialize();
        return res.length;
    }

    // the line segments
    public LineSegment[] segments()  {
        if (isModified()) initialize();
        return res;
    }

    // check if input array is modified
    private boolean isModified() {
        if (points == null || points.length != prePoints.length) return true;
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null || points[i].compareTo(prePoints[i]) != 0) {
                return true;
            }
        }
        return false;
    }

    private void initialize() {
        checkInput();
        prePoints = new Point[points.length];
        System.arraycopy(points, 0, prePoints, 0, points.length);
        res = findLine();
    }

    // check if input is valid
    private void checkInput() {
        // check null
        if (points == null) throw new IllegalArgumentException();
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }
        int n = points.length;
        Arrays.sort(points);
        // check null and duplications
        for (int i = 0; i < n; i++) {
            if (i + 1 < n && points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
    }

    public static void main(String[] args) {
        int[] arr = {19000, 10000, 18000, 10000};
        Point[] points = new Point[arr.length / 2];
        for (int i = 0, j = 0; i < points.length; i++, j += 2) {
            points[i] = new Point(arr[j], arr[j + 1]);
        }
        BruteCollinearPoints f = new BruteCollinearPoints(points);
        System.out.println(Arrays.toString(f.points));
        System.out.println(f.numberOfSegments());
        System.out.println(Arrays.toString(f.segments()));
    }
}