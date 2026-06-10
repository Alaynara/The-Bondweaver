/**
 * Reflection:
 * How did you implement your negative edge weights?
 * What are the benefits and detriments of your design decision?
 *
 * I implemented negative edge weights by allowing the graph to store any
 * Double value as an edge weight, including negatives, without enforcing
 * restrictions at insertion time. Instead, DWGraph determines at runtime
 * whether the graph contains negative edges and selects Bellman-Ford when
 * appropriate. This keeps the graph structure simple and shifts the
 * responsibility to the algorithm-selection logic.
 *
 * The benefit is flexibility: the graph can represent any weighted structure
 * without special cases. The drawback is that the system must always check
 * for negative edges before choosing an algorithm, and running Dijkstra’s on
 * a graph with negative weights would produce incorrect results.
 */

/**
 * The Search interface defines the behavior of all shortest-path algorithms
 * used in the DWGraph system. Implementations include Dijkstra's algorithm,
 * Bellman-Ford, and Floyd-Warshall. DWGraph selects the appropriate strategy
 * at runtime based on the graph's properties.
 */
public interface Search {

    /**
     * A record representing the result of shortest-path.
     * It contains the source node, destination node, total cost, and the
     * sequence of nodes along the shortest path.
     *
     * @param src  the starting node
     * @param dest the ending node
     * @param cost the total cost of the path
     * @param path the ordered list of nodes on the path
     */
    public static record Path(String src, String dest, int cost, String[] path) {}

    /**
     * Computes the shortest path between two nodes in the given graph.
     *
     * @param graph the graph to search
     * @param src   the starting node
     * @param dest  the ending node
     * @return a Path record containing the result of the search
     */
    Path findPath(Digraph graph, String src, String dest);
}
