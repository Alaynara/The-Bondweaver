import java.util.*;

/**
 * Implements Dijkstra's shortest-path algorithm for directed, weighted graphs
 * with non-negative edge weights. This class computes the minimum-cost path
 * between a source and destination node using a priority queue to always expand
 * the node with the smallest known distance.
 *
 * The algorithm maintains two maps:
 * - dist: the current best-known distance from the source to each node
 * - prev: the predecessor of each node on the shortest path
 *
 * After the algorithm finishes, the shortest path is reconstructed by following
 * predecessor links from the destination back to the source. If the destination
 * is unreachable, the method returns a Path object with infinite cost and an
 * empty node list.
 */
public class Dijkstras implements Search {

    /**
     * Computes the shortest path between the given source and destination nodes
     * in the provided directed, weighted graph using Dijkstra's algorithm.
     *
     * All distances are initialized to positive infinity except for the source,
     * which is assigned a distance of zero. A priority queue is used to select
     * the node with the smallest known distance. For each node removed from the
     * queue, all outgoing edges are relaxed. If a shorter path to a neighbor is
     * found, its distance and predecessor are updated and it is reinserted into
     * the queue.
     *
     * The algorithm stops early if the destination node is removed from the
     * priority queue, since its shortest distance is then finalized.
     *
     * After the search completes, the method reconstructs the path by following
     * predecessor pointers from the destination back to the source. If the
     * destination remains at infinite distance, no path exists.
     *
     * @param graph the directed, weighted graph to search
     * @param src   the starting node
     * @param dest  the target node
     * @return a Path object containing the source, destination, total cost, and
     *         the ordered list of nodes along the shortest path. If no path
     *         exists, returns a Path with infinite cost and an empty node list.
     */
    @Override
    public Path findPath(Digraph graph, String src, String dest) {
        HashMap<String, Double> dist = new HashMap<>();
        HashMap<String, String> prev = new HashMap<>();

        for(String node : graph.nodes()){
            dist.put(node, Double.POSITIVE_INFINITY);
            prev.put(node, null);
        }
        dist.put(src, 0.0);

        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingDouble(dist::get));
        pq.add(src);

        while( !pq.isEmpty()){
            String u = pq.poll();
            if (u.equals(dest)) break;

            for(String v : graph.edges(u)){
                double alt  = dist.get(u) + graph.weight(u, v);

                if (alt < dist.get(v)){
                    dist.put(v, alt);
                    prev.put(v,u);
                    pq.add(v);
                }
            }
        }

        ArrayList<String> path = new ArrayList();
        String cur = dest;

        while(cur != null){
            path.add(cur);
            cur = prev.get(cur);
        }
        Collections.reverse(path);

        if (dist.get(dest) == Double.POSITIVE_INFINITY) {
            return new Path(src, dest, Integer.MAX_VALUE, new String[0]);
        }

        int cost = (int) Math.round(dist.get(dest));
        return new Path(src, dest, cost, path.toArray(new String[0]));
    }
}
