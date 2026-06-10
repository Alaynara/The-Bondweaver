import java.util.*;

/**
 * Implements the Bellman-Ford shortest-path algorithm for directed weighted
 * graphs that may contain negative edge weights.
 *
 * The algorithm relaxes all edges to get the shortest
 * distances from the source to all reachable vertices. After,
 * the shortest path is reconstructed by following previous links from
 * the destination back to the source.
 *
 * If the destination is unreachable, this method returns a Path object with
 * infinite cost and an empty node list.
 */
public class BellmanFord implements Search {

    /**
     * Computes the shortest path between the given source and destination nodes
     * using the Bellman-Ford algorithm. Distances are initialized to positive
     * infinity except for the source, which is assigned a distance of zero.
     * <p>
     * All edges are relaxed and if a shorter path to a vertex is
     * found, its distance and predecessor are updated. After relaxation, the
     * path is reconstructed by following predecessor pointers from the
     * destination back to the source.
     *
     * @param graph the directed, weighted graph to search
     * @param src   the starting node
     * @param dest  the target node
     * @return a Path object containing the source, destination, total cost,
     * and the ordered list of nodes along the shortest path. If no
     * path exists, returns a Path with infinite cost and an empty list.
     */
    @Override
    public Path findPath(Digraph graph, String src, String dest) {

        HashMap<String, Double> dist = new HashMap<>();
        HashMap<String, String> prev = new HashMap<>();

        for (String node : graph.nodes()) {
            dist.put(node, Double.POSITIVE_INFINITY);
            prev.put(node, null);
        }
        dist.put(src, 0.0);

        ArrayList<Edge> edges = new ArrayList<>();
        for (String u : graph.nodes()) {
            for (String v : graph.edges(u)) {
                edges.add(new Edge(u, v, graph.weight(u, v)));
            }
        }

        int n = graph.nodes().size();

        for (int i = 0; i < n - 1; i++) {
            boolean updated = false;

            for (Edge e : edges) {
                if (dist.get(e.u) != Double.POSITIVE_INFINITY) {
                    double alt = dist.get(e.u) + e.w;

                    if (alt < dist.get(e.v)) {
                        dist.put(e.v, alt);
                        prev.put(e.v, e.u);
                        updated = true;
                    }
                }
            }

            if (!updated) break;
        }

        ArrayList<String> path = new ArrayList<>();
        String cur = dest;

        while (cur != null) {
            path.add(cur);
            cur = prev.get(cur);
        }
        Collections.reverse(path);

        // No path exists
        if (dist.get(dest) == Double.POSITIVE_INFINITY) {
            return new Path(src, dest, Integer.MAX_VALUE, new String[0]);
        }

        int cost = (int) Math.round(dist.get(dest));

        return new Path(src, dest, cost, path.toArray(new String[0]));
    }

    /**
     * Simple helper class to store a directed edge with a weight.
     */
    private static class Edge {
        String u;
        String v;
        double w;

        Edge(String u, String v, double w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
    }
}