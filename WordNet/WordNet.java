import edu.princeton.cs.algs4.Digraph;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Iterator;

public class WordNet {

    private final List<List<String>> dict;
    private final Map<String, List<Integer>> nouns;
    private final Digraph G;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("Null arguments!");
        }
        nouns = new TreeMap<>();
        dict= getDictionary(synsets, nouns);
        G = createGraph(hypernyms, dict);
    }

    private void validateGraph(Digraph G) {
        Queue<Integer> queue = new LinkedList<>();
        int[] degree = new int[G.V()];
        for (int i = 0; i < G.V(); i++) {
            degree[i] = G.indegree(i);
            if (degree[i] == 0) queue.offer(i);
        }
        int root = -1, num = G.V();
        // topological sort
        while (!queue.isEmpty()) {
            int from = queue.poll();
            num--;
            // this node has no children
            if (G.outdegree(from) == 0) {
                if (root == -1) root = from;
                if (from != root) { // multiple roots
                    throw new IllegalArgumentException("Multiple roots!");
                }
            }
            for (int to : G.adj(from)) {
                degree[to]--;
                if (degree[to] == 0) {
                    queue.offer(to);
                }
            }
        }
        // check cycle
        if (num != 0) throw new IllegalArgumentException("Circle found!");
    }

    // create graph
    private Digraph createGraph(String hypernyms, List<List<String>> dict) {
        Digraph G = new Digraph(dict.size());
        InputIterator iterator = new InputIterator(hypernyms);
        while (iterator.hasNext()) {
            // format: 34,47569,48084
            String[] ids = iterator.next().split(",");
            int from = Integer.parseInt(ids[0]);
            for (int i = 1; i < ids.length; i++) {
                int to = Integer.parseInt(ids[i]);
                G.addEdge(from, to);
            }
        }
        validateGraph(G);
        return G;
    }

    // read all words form a String
    private List<List<String>> getDictionary(String synsets, Map<String, List<Integer>> nouns) {
        List<List<String>> list = new ArrayList<>();
        // format: 1,1530s,the decade from 1530 to 1539
        InputIterator iterator = new InputIterator(synsets);
        while (iterator.hasNext()) {
            String[] line = iterator.next().split(",");
            String[] words = line[1].split(" ");
            list.add(new ArrayList<>(Arrays.asList(words)));
            for (String word : words) {
                nouns.computeIfAbsent(word, l -> new ArrayList<>()).add(list.size() - 1);
            }
        }
        return list;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("Null arguments!");
        return nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Arguments not noun! [" + nounA + ", " + nounB + "]");
        }
        return getLCA(nouns.get(nounA), nouns.get(nounB))[0];
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

    private int[] shortestPath(Iterable<Integer> iterable, int n) {
        int max = Integer.MAX_VALUE;
        int[] dis = new int[n];
        Arrays.fill(dis, max);
        Queue<Integer> queue = new LinkedList<>();
        for (int i : iterable) queue.offer(i);
        int step = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int from = queue.poll();
                if (step >= dis[from]) continue;
                dis[from] = step;
                for (int to : G.adj(from)) {
                    queue.offer(to);
                }
            }
            step++;
        }
        return dis;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Arguments not noun! [" + nounA + ", " + nounB + "]");
        }
        int ancestor = getLCA(nouns.get(nounA), nouns.get(nounB))[1];
        if (ancestor == -1) return null;
        return dict.get(ancestor).get(0);
    }

    // read String line by line
    private static class InputIterator implements Iterator<String> {
        private final String input;
        private int curr;

        InputIterator(String input) {
            this.input = input;
        }

        @Override
        public boolean hasNext() {
            return curr < input.length();
        }

        @Override
        public String next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            int start = curr;
            while (curr != input.length() && input.charAt(curr) != '\n') {
                curr++;
            }
            return input.substring(start, curr++);
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}