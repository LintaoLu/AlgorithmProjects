import java.util.ArrayList;
import java.util.Arrays;

/**
 * Please note: For simplicity, we will not supply any input to BruteCollinearPoints
 * that has 5 or more collinear points.
 * */

public class BruteCollinearPoints {

    private final LineSegment[] res;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        Point[] input = points.clone();
        checkInput(input);
        res = findLine(input);
    }

    private LineSegment[] findLine(Point[] points) {
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
        return res.length;
    }

    // the line segments
    public LineSegment[] segments()  {
        return res.clone();
    }

    // check if input is valid
    private void checkInput(Point[] points) {
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
        System.out.println(f.numberOfSegments());
        System.out.println(Arrays.toString(f.segments()));
    }
}
