import edu.princeton.cs.algs4.Digraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

public class SAP {

    private final Digraph G;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        List<Integer> list1 = new ArrayList<>(), list2 = new ArrayList<>();
        list1.add(v);
        list2.add(w);
        validateVertex(list1);
        validateVertex(list2);
        return length(list1, list2);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        List<Integer> list1 = new ArrayList<>(), list2 = new ArrayList<>();
        list1.add(v);
        list2.add(w);
        validateVertex(list1);
        validateVertex(list2);
        return ancestor(list1, list2);
    }

    private int[] shortestPath(Iterable<Integer> iterable, int n) {
        int[] dis = new int[n];
        Arrays.fill(dis, Integer.MAX_VALUE);
        Queue<Integer> queue = new LinkedList<>();
        for (int i : iterable) queue.offer(i);
        int step = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i  = 0; i < size; i++) {
                int from = queue.poll();
                if (dis[from] <= step) continue;
                dis[from] = step;
                for (int to : G.adj(from)) {
                    queue.offer(to);
                }
            }
            step++;
        }
        return dis;
    }

    private int[] getLCA(Iterable<Integer> v, Iterable<Integer> w) {
        int max = Integer.MAX_VALUE;
        int[] dis1 = shortestPath(v, G.V());
        int[] dis2 = shortestPath(w, G.V());
        // distance, ancestor
        int[] res = { max, -1 };
        for (int i = 0; i < G.V(); i++) {
            if (dis1[i] == max || dis2[i] == max) continue;
            if (res[0] <= dis1[i] + dis2[i]) continue;
            res[0] = dis1[i] + dis2[i];
            res[1] = i;
        }
        if (res[0] == max) res[0] = -1;
        return res;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertex(v);
        validateVertex(w);
        return getLCA(v, w)[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertex(v);
        validateVertex(w);
        return getLCA(v, w)[1];
    }

    private void validateVertex(Iterable<Integer> iterable) {
        if (iterable == null) throw new IllegalArgumentException();
        for (Integer v : iterable) {
            if (v == null || v < 0 || v >= G.V()) {
                throw new IllegalArgumentException();
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        Digraph G = new Digraph(13);
        String input = "7 3 8 3 3 1 4 1 5 1 9 5 10 5 11 10 12 10 1 0 2 0";
        String[] arr = input.split(" ");
        for (int i = 0; i < arr.length; i += 2) {
            G.addEdge(Integer.parseInt(arr[i]), Integer.parseInt(arr[i+1]));
        }
        SAP sap = new SAP(G);
        System.out.println("a=" + sap.ancestor(3, 11) + " l=" + sap.length(3, 11));
        System.out.println("a=" + sap.ancestor(9, 12) + " l=" + sap.length(9, 12));
        System.out.println("a=" + sap.ancestor(7, 2) + " l=" + sap.length(7, 2));
        System.out.println("a=" + sap.ancestor(1, 6) + " l=" + sap.length(1, 6));
    }

}
