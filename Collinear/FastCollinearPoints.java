import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;

public class FastCollinearPoints {

    private Point[] points, prePoints;
    private LineSegment[] res;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        this.points = points;
        initialize();
    }

    /**
     *
     * For each point, sort auxiliary array according to the slop order of
     * his point. Then all points that in a same line should be closed to
     * each other. If we can find 3 consecutive points (be careful since maybe
     * more than 4 points in this line), which means this is line), which means
     * this is a legal line, we should get smallest point and biggest
     * point (endpoints) among them and finally put the pair to a list. Because
     * all duplicated answers' endpoints are same, we can simply remove duplications.
     *
     */
    private LineSegment[] findLine() {
        int n = points.length;
        Point[] auxiliary = new Point[n];
        System.arraycopy(points, 0, auxiliary, 0, n);
        ArrayList<Data> buffer = new ArrayList<>();
        for (Point point : points) {
            Comparator<Point> cmp = point.slopeOrder();
            Arrays.sort(auxiliary, cmp);
            // for every answer, only preserve two endpoints
            Point start = point, end = point;
            for (int j = 0; j < n; j++) {
                // 4 or more than 4 points in this line
                if (j + 2 < n && cmp.compare(auxiliary[j], auxiliary[j + 1]) == 0 &&
                        cmp.compare(auxiliary[j + 1], auxiliary[j + 2]) == 0) {
                    int index = j;
                    while (j < n && cmp.compare(auxiliary[j], auxiliary[index]) == 0) {
                        start = start.compareTo(auxiliary[j]) < 0 ? start : auxiliary[j];
                        end = end.compareTo(auxiliary[j]) > 0 ? end : auxiliary[j];
                        j++;
                    }
                    buffer.add(new Data(start, end));
                }
            }
        }

        // remove duplicated lines
        ArrayList<Data> temp = new ArrayList<>();
        Collections.sort(buffer);
        for (Data curr : buffer) {
            if (temp.isEmpty() || temp.get(temp.size() - 1).compareTo(curr) != 0) {
                temp.add(curr);
            }
        }

        // convert list to array (required)
        LineSegment[] res = new LineSegment[temp.size()];
        for (int i = 0; i < temp.size(); i++) {
            res[i] = temp.get(i).toSegment();
        }
        return res;
    }

    private static class Data implements Comparable<Data> {

        final Point p1, p2;

        Data (Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }

        @Override
        public int compareTo(Data data) {
            if (p1.compareTo(data.p1) != 0) return p1.compareTo(data.p1);
            if (p2.compareTo(data.p2) != 0) return p2.compareTo(data.p2);
            return 0;
        }

        public LineSegment toSegment() {
            return new LineSegment(p1, p2);
        }
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
        int[] test1 = {
                19000,  10000,
                18000,  10000,
                32000,  10000,
                21000,  10000,
                 1234,   5678,
                14000,  10000
        };
        int[] test2 = {
                10000, 0,
                0, 10000,
                3000, 7000,
                7000, 3000,
                20000, 21000,
                3000, 4000,
                14000, 15000,
                6000, 7000};

        Point[] points = new Point[test2.length / 2];
        for (int i = 0, j = 0; i < points.length; i++, j += 2) {
            points[i] = new Point(test2[j], test2[j + 1]);
        }
        FastCollinearPoints f = new FastCollinearPoints(points);
        points[0] = null;
        System.out.println(f.numberOfSegments());
        System.out.println(Arrays.toString(f.segments()));
    }
}

