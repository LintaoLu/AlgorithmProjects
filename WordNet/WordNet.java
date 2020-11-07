import java.util.*;

public class WordNet {

    // directed graph
    private final Map<String, List<String>> graph;
    private final Set<String> nounSet;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("Null arguments!");
        }
        graph = new HashMap<>();
        nounSet = new HashSet<>();
        constructGraph(synsets, hypernyms);
    }

    // check if the graph is a single root DAG by using topological sort
    private void checkGraph(Map<String, Integer> degree) {
        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : degree.entrySet()) {
            if (entry.getValue() != 0) continue;
            queue.offer(entry.getKey());
        }
        String root = null;
        int num = degree.size();
        while (!queue.isEmpty()) {
            String from = queue.poll();
            num--;
            List<String> neighbours = graph.get(from);
            // check if the graph is single source
            if (neighbours.size() == 0) {
                if (root == null) root = from;
                    // different roots, return false
                else if (!from.equals(root)) {
                    throw new IllegalArgumentException("Multiple roots!");
                }
            }
            for (String to : neighbours) {
                int val = degree.get(to) - 1;
                degree.put(to, val);
                if (val == 0) queue.offer(to);
            }
        }
        // check cycle
        if (num != 0) throw new IllegalArgumentException("Circle found!");
    }

    // create a grapg and check if this graph is valid (single root DAG)
    private void constructGraph(String synsets, String hypernyms) {
        List<List<String>> dictionary = constructDictionary(synsets);
        Map<String, Integer> degree = new HashMap<>();
        for (List<String> list : dictionary) {
            for (String word : list) {
                degree.put(word, 0);
                graph.put(word, new ArrayList<>());
            }
        }
        // format: 34,47569,48084
        InputIterator iterator = new InputIterator(hypernyms);
        while (iterator.hasNext()) {
            String[] ids = iterator.next().split(",");
            List<String> children = dictionary.get(Integer.parseInt(ids[0]));
            for (int i = 1; i < ids.length; i++) {
                List<String> parents = dictionary.get(Integer.parseInt(ids[i]));
                for (String from : children) {
                    for (String to : parents) {
                        // degree (in-degree) counter
                        degree.put(to, degree.getOrDefault(to, 0) + 1);
                        // create graph relations
                        graph.get(from).add(to);
                        // add hypernyms to noun set
                        nounSet.add(to);
                    }
                }
            }
        }
        checkGraph(degree);
    }

    // read all words form a String
    private List<List<String>> constructDictionary(String synsets) {
        List<List<String>> list = new ArrayList<>();
        // format: 1,1530s,the decade from 1530 to 1539
        InputIterator iterator = new InputIterator(synsets);
        while (iterator.hasNext()) {
            String[] line = iterator.next().split(",");
            String[] words = line[1].split(" ");
            list.add(new ArrayList<>(Arrays.asList(words)));
        }
        return list;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounSet;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("Null arguments!");
        return nounSet.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Arguments not noun!");
        }
        return Integer.parseInt(getLCA(nounA, nounB)[1]);
    }

    // BFS shortest distance from a word to other words
    private Map<String, Integer> shortestPath(String word) {
        Map<String, Integer> dis = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        queue.offer(word);
        int step = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String from = queue.poll();
                if (dis.getOrDefault(from, Integer.MAX_VALUE) < step) continue;
                dis.put(from, step);
                for (String to : graph.get(from)) {
                    queue.offer(to);
                }
            }
            step++;
        }
        return dis;
    }

    private String[] getLCA(String nounA, String nounB) {
        Map<String, Integer> map1 = shortestPath(nounA);
        Map<String, Integer> map2 = shortestPath(nounB);
        // a small optimization, compare smaller set to bigger set
        if (map1.size() > map2.size()) {
            Map<String, Integer> map3 = map1;
            map1 = map2;
            map2 = map3;
        }
        int min = Integer.MAX_VALUE;
        String ancestor = null;
        for (Map.Entry<String, Integer> entry : map1.entrySet()) {
            String key = entry.getKey();
            int val = entry.getValue();
            if (map2.containsKey(key)) {
                int path = map2.get(key) + val;
                if (path < min) {
                    min = path;
                    ancestor = key;
                }
            }
        }
        return new String[] { ancestor, String.valueOf(min) };
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("Arguments not noun!");
        }
        return getLCA(nounA, nounB)[0];
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
            if (!hasNext()) throw new NoSuchElementException();
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
        String str1 = "0,A,...\n1,B C,...\n2,D,...\n3,E,...\n4,F,...\n5,G,...\n";
        String str2 = "1,0\n2,0\n3,1\n4,3\n5,2\n";
        WordNet wordNet = new WordNet(str1, str2);
        System.out.println(wordNet.graph);
        System.out.println(wordNet.nounSet);
        System.out.println(wordNet.sap("E", "D"));
        System.out.println(wordNet.distance("E", "D"));
    }

}