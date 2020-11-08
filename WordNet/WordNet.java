import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class WordNet {

    private final List<String> dict;
    private final Map<String, List<Integer>> nouns;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("Null arguments!");
        }
        nouns = new HashMap<>();
        dict = getDictionary(synsets, nouns);
        sap = new SAP(createGraph(hypernyms, dict.size()));
    }

    private void validateGraph(Digraph G) {
        // check if graph is DAG
        Topological topological = new Topological(G);
        if (!topological.hasOrder()) {
            throw new IllegalArgumentException("Not DAG!");
        }
        // check if the graph has multiple roots
        int num = 0;
        for (int i = 0; i < G.V(); i++) {
            if (G.outdegree(i) == 0) num++;
            if (num > 1) {
                throw new IllegalArgumentException("Multiple roots!");
            }
        }
    }

    // create graph
    private Digraph createGraph(String hypernyms, int n) {
        Digraph G = new Digraph(n);
        In in = new In(hypernyms);
        while (in.hasNextLine()) {
            // format: 34,47569,48084
            String[] ids = in.readLine().split(",");
            int from = Integer.parseInt(ids[0]);
            for (int i = 1; i < ids.length; i++) {
                int to = Integer.parseInt(ids[i]);
                G.addEdge(from, to);
            }
        }
        in.close();
        validateGraph(G);
        return G;
    }

    // read all words form a file
    private List<String> getDictionary(String synsets, Map<String, List<Integer>> nouns) {
        List<String> dict = new ArrayList<>();
        // format: 1,1530s,the decade from 1530 to 1539
        In in = new In(synsets);
        while (in.hasNextLine()) {
            String[] line = in.readLine().split(",");
            dict.add(line[1]);
            String[] words = line[1].split(" ");
            for (String word : words) {
                nouns.computeIfAbsent(word, l -> new ArrayList<>()).add(dict.size() - 1);
            }
        }
        in.close();
        return dict;
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
        validateNoun(nounA, nounB);
        return sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validateNoun(nounA, nounB);
        int ancestor = sap.ancestor(nouns.get(nounA), nouns.get(nounB));
        return dict.get(ancestor);
    }

    private void validateNoun(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Arguments not noun! [" + nounA + ", " + nounB + "]");
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");
        StdOut.println(wordNet.sap("a", "o"));
    }
}