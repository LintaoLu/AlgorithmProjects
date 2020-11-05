import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private TreeNode root;
    private int size;

    // construct an empty set of points
    public KdTree() {
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = insert(p, root, 0);
    }

    private TreeNode insert(Point2D p, TreeNode node, int h) {
        if (node == null) {
            size++;
            return new TreeNode(p);
        }
        // the node is already been inserted
        if (node.samePoint(p)) return node;
        if (h % 2 == 0) {
            if (p.x() < node.x()) node.left = insert(p, node.left, h + 1);
            else node.right = insert(p, node.right, h + 1);
        } else {
            if (p.y() < node.y()) node.left = insert(p, node.left, h + 1);
            else node.right = insert(p, node.right, h + 1);
        }
        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return find(p, root, 0);
    }

    private boolean find(Point2D p, TreeNode node, int h) {
        if (node == null) return false;
        if (node.samePoint(p)) return true;
        if (h % 2 == 0) {
            if (p.x() < node.x()) return find(p, node.left, h + 1);
            else return find(p, node.right, h + 1);
        } else {
            if (p.y() < node.y()) return find(p, node.left, h + 1);
            else return find(p, node.right, h + 1);
        }
    }

    // draw all points to standard draw
    public void draw() { }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> list = new ArrayList<>();
        rangeSearch(new RectHV(0.0, 0.0, 1.0, 1.0), 0, root, rect, list);
        return list;
    }

    private void rangeSearch(RectHV curr, int h, TreeNode node, RectHV target, List<Point2D> list) {
        if (node == null || !curr.intersects(target)) return;
        if (target.contains(node.point2D)) list.add(node.point2D);
        double x1 = curr.xmin(), y1 = curr.ymin(), x2 = curr.xmax(), y2 = curr.ymax();
        if (h % 2 == 0) {
            rangeSearch(new RectHV(x1, y1, node.x(), y2), h + 1, node.left, target, list);
            rangeSearch(new RectHV(node.x(), y1, x2, y2), h + 1, node.right, target, list);
        } else {
            rangeSearch(new RectHV(x1, y1, x2, node.y()), h + 1, node.left, target, list);
            rangeSearch(new RectHV(x1, node.y(), x2, y2), h + 1, node.right, target, list);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        double[] distance = { Double.POSITIVE_INFINITY };
        Point2D[] res = new Point2D[1];
        findClosestPoint(p, new RectHV(0.0, 0.0, 1.0, 1.0), distance, res, root, 0);
        return res[0];
    }

    private void findClosestPoint(Point2D p, RectHV curr, double[] distance, Point2D[] res, TreeNode node, int h) {
        if (node == null || distance[0] < curr.distanceTo(p)) return;
        if (distance[0] > node.point2D.distanceTo(p)) {
            res[0] = node.point2D;
            distance[0] = node.point2D.distanceTo(p);
        }
        double x1 = curr.xmin(), y1 = curr.ymin(), x2 = curr.xmax(), y2 = curr.ymax();
        if (h % 2 == 0) {
            RectHV rec1 = new RectHV(x1, y1, node.x(), y2);
            RectHV rec2 = new RectHV(node.x(), y1, x2, y2);
            if (p.x() < node.x()) {
                findClosestPoint(p, rec1, distance, res, node.left, h + 1);
                findClosestPoint(p, rec2, distance, res, node.right, h + 1);
            } else {
                findClosestPoint(p, rec2, distance, res, node.right, h + 1);
                findClosestPoint(p, rec1, distance, res, node.left, h + 1);
            }
        } else {
            RectHV rec1 = new RectHV(x1, y1, x2, node.y());
            RectHV rec2 = new RectHV(x1, node.y(), x2, y2);
            if (p.y() < node.y()) {
                findClosestPoint(p, rec1, distance, res, node.left, h + 1);
                findClosestPoint(p, rec2, distance, res, node.right, h + 1);
            } else {
                findClosestPoint(p, rec2, distance, res, node.right, h + 1);
                findClosestPoint(p, rec1, distance, res, node.left, h + 1);
            }
        }
    }

    private static class TreeNode {
        final Point2D point2D;
        TreeNode left, right;

        TreeNode(Point2D point2D) { this.point2D = point2D; }

        boolean samePoint(Point2D other) { return point2D.equals(other); }

        double x() { return point2D.x(); }

        double y() { return point2D.y(); }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        // KdTree kdTree = new KdTree();
        KdTree kdTree = new KdTree();
        String input = "0.7 0.2 0.5 0.4 0.2 0.3 0.4 0.7 0.9 0.6";
        String[] numbers = input.split(" ");
        for (int i = 0; i < numbers.length; i += 2) {
            kdTree.insert(new Point2D(Double.parseDouble(numbers[i]), Double.parseDouble(numbers[i+1])));
        }
        System.out.println(kdTree.nearest(new Point2D(0.97, 0.79)));
    }

}
