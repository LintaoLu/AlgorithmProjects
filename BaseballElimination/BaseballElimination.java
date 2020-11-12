import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Bag;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class BaseballElimination {

    // team : [id, wins, losses, remaining]
    private final Map<String, int[]> teams;
    private final Map<String, List<String>> certificates;
    private final int[][] G;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        if (filename == null)
            throw new IllegalArgumentException();
        teams = new HashMap<>();
        G = readFile(filename, teams);
        certificates = findEliminatedTeams();
    }

    private void trivialCheck(Map<String, List<String>> certificates) {
        for (String candidate : teams()) {
            int max = wins(candidate) + remaining(candidate);
            for (String team : teams()) {
                if (max < wins(team)) {
                    List<String> list = new ArrayList<>();
                    list.add(team);
                    certificates.put(candidate, list);
                    break;
                }
            }
        }
    }

    private Map<String, List<String>> findEliminatedTeams() {
        Map<String, List<String>> certificates = new HashMap<>();
        trivialCheck(certificates);
        for (String team : teams()) {
            if (certificates.containsKey(team)) continue;
            int[] capacity = new int[1];
            Bag<Pair<Integer, String>> ids = new Bag<>();
            FlowNetwork net = createFlowNetWork(team, ids, certificates, capacity);
            FordFulkerson fordFulkerson = new FordFulkerson(net, 0, net.V() - 1);
            if (fordFulkerson.value() < capacity[0]) {
                List<String> list = new ArrayList<>();
                for (Pair<Integer, String> entry : ids) {
                    if (fordFulkerson.inCut(entry.key)) {
                        list.add(entry.value);
                    }
                }
                certificates.put(team, list);
            }
        }
        return certificates;
    }

    private int[][] readFile(String filename, Map<String, int[]> teams) {
        In in = new In(filename);
        String str = in.readLine();
        if (str == null) throw new IllegalArgumentException();
        int n = Integer.parseInt(str), line = 0;
        int[][] G = new int[n][n];
        while (in.hasNextLine()) {
            str = in.readLine();
            if (str == null) throw new IllegalArgumentException();
            String[] data = str.trim().split("\\s+");
            int[] val = new int[4];
            // team id
            val[0] = line++;
            // win, lose, remain
            for (int i = 1; i < 4; i++)
                val[i] = Integer.parseInt(data[i]);
            teams.put(data[0], val);
            // update G
            for (int i = 0; i < n; i++)
                G[val[0]][i] = Integer.parseInt(data[i + 4]);
        }
        return G;
    }

    private FlowNetwork createFlowNetWork(String candidate, Bag<Pair<Integer, String>> ids,
                                          Map<String, List<String>> certificate, int[] capacity) {
        int n = numberOfTeams() - certificate.size() - 1, index = 0;
        String[] sub = new String[n];
        for (String team : teams()) {
            if (team.equals(candidate) || certificate.containsKey(team)) continue;
            sub[index++] = team;
        }
        int offset1 = 1, offset2 = n * (n - 1) / 2 + 1;
        int size = offset2 + n + 1;
        FlowNetwork net = new FlowNetwork(size);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int id1 = getTeamId(sub[i]), id2 = getTeamId(sub[j]);
                capacity[0] += G[id1][id2];
                net.addEdge(new FlowEdge(0, offset1, G[id1][id2]));
                net.addEdge(new FlowEdge(offset1, offset2 + i, Double.POSITIVE_INFINITY));
                net.addEdge(new FlowEdge(offset1, offset2 + j, Double.POSITIVE_INFINITY));
                offset1++;
            }
            double val = wins(candidate) + remaining(candidate) - wins(sub[i]);
            net.addEdge(new FlowEdge(offset2 + i, size - 1, val));
            ids.add(new Pair<>(offset2 + i, sub[i]));
        }
        return net;
    }

    private static class Pair<Item1, Item2> {
        Item1 key;
        Item2 value;

        Pair(Item1 key, Item2 value) {
            this.key = key;
            this.value = value;
        }
    }

    private int getTeamId(String team) {
        validateTeam(team);
        return teams.get(team)[0];
    }

    // number of teams
    public int numberOfTeams() {
        return teams.size();
    }

    // all teams
    public Iterable<String> teams() {
        return teams.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        validateTeam(team);
        return teams.get(team)[1];
    }

    // number of losses for given team
    public int losses(String team) {
        validateTeam(team);
        return teams.get(team)[2];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        validateTeam(team);
        return teams.get(team)[3];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        int id1 = getTeamId(team1);
        int id2 = getTeamId(team2);
        return G[id1][id2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        validateTeam(team);
        return certificates.containsKey(team);
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);
        return certificates.get(team);
    }

    private void validateTeam(String team) {
        if (team == null || !teams.containsKey(team))
            throw new IllegalArgumentException();
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("test1.txt");
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

}
