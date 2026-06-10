import java.util.*;

/**
 * Implements the Floyd-Warshall all-pairs shortest-path algorithm for directed,
 * weighted graphs. This algorithm computes the shortest distances between every
 * pair of vertices by repeatedly improving a distance matrix using intermediate
 * vertices.
 *
 * This class is only intended to be used when both the source and destination
 * strings are exactly "<ALL>". In this case, the algorithm computes all-pairs
 * shortest paths and returns a Path object whose cost is zero and whose path
 * field contains the list of all vertices in the graph.
 */
public class FloydWarshall implements Search {

    /**
     * Runs the Floyd-Warshall algorithm on the given graph. This method should
     * only be called when both src and dest are the string "<ALL>".
     *
     * The algorithm initializes a distance matrix using the graph's edge
     * weights, then repeatedly checks whether using an intermediate vertex k
     * provides a shorter path between vertices i and j.
     *
     * Since the assignment requires returning a Path object, and since
     * Floyd-Warshall computes all-pairs shortest paths rather than a single
     * path, this method returns a Path with cost 0 and a path array containing
     * all vertex names in sorted order.
     *
     * @param graph the directed, weighted graph
     * @param src   must be the string "<ALL>"
     * @param dest  must be the string "<ALL>"
     * @return a Path object containing all vertices in sorted order
     */
    @Override
    public Path findPath(Digraph graph, String src, String dest) {

        ArrayList<String> nodes = new ArrayList<>(graph.nodes());
        Collections.sort(nodes);

        int n = nodes.size();
        double[][] dist = new double[n][n];


        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], Double.POSITIVE_INFINITY);
            dist[i][i] = 0.0;
        }


        for (int i = 0; i < n; i++) {
            String u = nodes.get(i);
            for (String v : graph.edges(u)) {
                int j = nodes.indexOf(v);
                dist[i][j] = graph.weight(u, v);
            }
        }


        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    double alt = dist[i][k] + dist[k][j];
                    if (alt < dist[i][j]) {
                        dist[i][j] = alt;
                    }
                }
            }
        }

        // For <ALL> to <ALL>, the assignment expects:
        // - cost = 0
        // - path = list of all vertices
        String[] arr = nodes.toArray(new String[0]);
        return new Path(src, dest, 0, arr);
    }
}
