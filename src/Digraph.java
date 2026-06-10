/**
 * This interface defines the core operations that any directed weighted
 * graph must support, regardless of how it is internally represented.
 * Both AdjList and AdjMatrix implement this interface, and DWGraph uses it
 * to delegate all graph operations to the active representation.
 *
 * Reflection:
 * When did you choose to swap between the matrix and the list?
 * Explain and defend your threshold selection process.
 *
 * I chose to swap between the adjacency list and adjacency matrix based on the
 * overall density of the graph. When the density rises above the threshold,
 * the graph becomes dense enough that a matrix provides faster lookups and
 * predictable memory usage. When the density falls below the threshold, the
 * graph is sparse and an adjacency list is more efficient because it only
 * stores edges that actually exist.
 *
 * I selected a threshold of 0.5 because it represents the point where half of
 * all possible edges are present. Below this point, most entries in a matrix
 * would be empty, wasting memory. Above this point, the cost of scanning
 * through lists becomes larger than the cost of maintaining a full matrix.
 * This threshold balances memory efficiency with lookup performance and works
 * well for general directed weighted graphs.
 */
import java.util.*;

public interface Digraph {

    /**
     * Adds a new node to the graph.
     *
     * @param key the node to add
     * @return true if the node was added, false if it already exists or is null
     */
    boolean add(String key);

    /**
     * Adds a directed weighted edge from src to dest.
     * Missing nodes are automatically added.
     *
     * @param src    the source node
     * @param dest   the destination node
     * @param weight the weight of the edge
     * @return true if the edge was added, false otherwise
     */
    boolean add(String src, String dest, Double weight);

    /**
     * Deletes a node from the graph and removes all inbound and outbound edges
     * associated with it.
     *
     * @param key the node to delete
     * @return the name of the deleted node, or null if the node did not exist
     */
    String delete(String key);

    /**
     * Deletes a directed edge from src to dest.
     *
     * @param src  the source node
     * @param dest the destination node
     * @return the weight of the deleted edge, or null if the edge did not exist
     */
    Double delete(String src, String dest);

    /**
     * Returns a list of all nodes in the graph.
     *
     * @return an ArrayList of node names
     */
    ArrayList<String> nodes();

    /**
     * Returns all outgoing neighbors of a given node.
     *
     * @param key the node whose edges are requested
     * @return a list of destination nodes
     */
    ArrayList<String> edges(String key);

    /**
     * Returns the weight of the edge from src to dest.
     *
     * @param src  the source node
     * @param dest the destination node
     * @return the weight, or null if no edge exists
     */
    Double weight(String src, String dest);

    /**
     * Computes the overall density of the graph.
     * Density = edges / (n * (n - 1)).
     *
     * @return the global density
     */
    double density();

    /**
     * Computes the local density of a node.
     * Local density = outgoing edges / (n - 1).
     *
     * @param key the node whose density is computed
     * @return the local density, or 0.0 if the node does not exist
     */
    double density(String key);

    /**
     * Returns the number of nodes in the graph.
     *
     * @return the number of nodes
     */
    int size();

    /**
     * Returns a readable string representation of the graph.
     *
     * @return a formatted adjacency structure
     */
    String toString();

    /**
     * Returns a JSON representation of the graph.
     *
     * @return a JSON-formatted string
     */
    String toJSON();
}