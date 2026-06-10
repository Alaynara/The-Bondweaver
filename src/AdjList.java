/**
 * A directed weighted graph implementation that uses an adjacency list.
 *
 * This structure maps each node to a HashMap of its outgoing neighbors
 * and the weights of the edges connecting them. This structure supports fast iteration
 * over outgoing edges while minimizing memory usage.
 *
 * Reflection:
 * When should you use a list to represent a directed weighted graph?
 * why? Does this reasoning hold true for other types of graphs?
 *
 * A list is best for representing a directed weighted graph when the graph is sparse.
 * In this situation an adjacency list saves memory because it only stores the edges
 * that actually exist. It also makes it faster to look at a node's outgoing edges
 * since unlike an adjacency matric you don't have to scan through the entire thing.
 *
 * This idea can apply to other types of graphs also. Sparse graphs have more benefit from adjacency
 * lists whereas dense graphs might be better off using an adjacency matrix.
 */

import java.util.*;

public class AdjList implements Digraph {

    /**
     * Maps each node to a map of its outgoing neighbors and edge weights.
     */
    private HashMap<String, HashMap<String, Double>> connections;

    /**
     * Constructs an empty adjacency list graph.
     * Initializes the internal data structure used to store nodes and edges.
     */
    public AdjList() {
        this.connections = new HashMap<>();

    }

    /**
     * Adds a new node to the graph.
     *
     * @param key the node to add
     * @return true if the node was added, false if it was null or already exists
     */
    @Override
    public boolean add(String key) {
        if(key == null){
            return false;
        }else if (this.connections.containsKey(key)){
            return false;
        }else{
            this.connections.put(key, new HashMap<>());
            return true;
        }
    }

    /**
     * Adds a directed weighted edge from src to dest.
     * Automatically adds missing nodes if they do not already exist.
     *
     * @param src the source node
     * @param dest the destination node
     * @param weight the weight of the edge
     * @return true if the edge was added, false if parameters are null or the edge exists
     */
    @Override
    public boolean add(String src, String dest, Double weight) {
        if (src == null || dest == null || weight == null){
            return false;
        }
        //adds missing nodes (src and dest)
        if(!this.connections.containsKey(src)){
            connections.put(src, new HashMap<>());
        }
        if (!this.connections.containsKey(dest)){
            connections.put(dest, new HashMap<>());
        }
        // checks if edge exists already
        if (connections.get(src).containsKey(dest)){
            return false;
        }
        //adds edge
        this.connections.get(src).put(dest, weight);
        return true;
    }

    /**
     * Deletes a node from the graph and removes all inbound and outbound edges
     * associated with it.
     *
     * @param key the node to delete
     * @return the deleted node's name, or null if the node did not exist
     */
    @Override
    public String delete(String key) {
        if (key == null || !connections.containsKey(key)) {
            return null;
        }

        connections.remove(key);

        for (String node : connections.keySet()) {
            connections.get(node).remove(key);
        }

        return key;
    }

    /**
     * Deletes a directed edge from src to dest.
     *
     * @param src  the source node
     * @param dest the destination node
     * @return the weight of the deleted edge, or null if the edge did not exist
     */
    @Override
    public Double delete(String src, String dest) {
        if (src == null || dest == null) {
            return null;
        }
        if (!connections.containsKey(src)) {
            return null;
        }
        return connections.get(src).remove(dest);  // returns the weight or null
    }


    /**
     * Returns a list of all nodes in the graph.
     *
     * @return an ArrayList containing all node names
     */
    @Override
    public ArrayList<String> nodes() {
        return new ArrayList<>(this.connections.keySet());
    }

    /**
     * Returns all outgoing neighbors of a given node.
     *
     * @param key the node whose edges are requested
     * @return a list of destination nodes, or an empty list if the node does not exist
     */
    @Override
    public ArrayList<String> edges(String key) {
        if (key == null || !this.connections.containsKey(key)){
            return new ArrayList<>();
        }
        return new ArrayList<>(this.connections.get(key).keySet());
    }

    /**
     * Returns the weight of the edge from src to dest.
     *
     * @param src the source node
     * @param dest the destination node
     * @return the weight of the edge, or null if the edge does not exist
     */
    @Override
    public Double weight(String src, String dest) {
        if (src == null || dest == null){
            return null;
        }else if (!this.connections.containsKey(src)){
            return null;
        }
        return this.connections.get(src).get(dest);
    }

    /**
     * Computes the overall density of the graph.
     * Density = number of edges / maximum possible edges.
     *
     * @return the density of the graph as a value between 0 and 1
     */
    @Override
    public double density() {
        int n = this.connections.size();
        if(n <= 1){
            return 0.0;
        }

        int edges = 0;
        for (String node : this.connections.keySet()){
            edges += this.connections.get(node).size();
        }

        double maxEdges = n * (n - 1);
        return edges / maxEdges;
    }

    /**
     * Computes the local density of a node.
     * Local density = outgoing edges / (total nodes - 1).
     *
     * @param key the node whose density is computed
     * @return the local density, or 0.0 if the node does not exist
     */
    @Override
    public double density(String key) {
        if(key == null || !this.connections.containsKey(key)){
            return 0.0;
        }

        int n = this.connections.size();
        if (n <= 1){
            return 0.0;
        }

        int outgoing = this.connections.get(key).size();
        return outgoing / (double)(n - 1);
    }

    /**
     * Returns the number of nodes in the graph.
     *
     * @return the number of nodes
     */
    @Override
    public int size() {
        return this.connections.size();
    }

    /**
     * Returns a readable string representation of the adjacency list.
     *
     * @return a formatted adjacency list string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        //sort nodes alphabetically
        ArrayList<String> nodes = new ArrayList<>(this.connections.keySet());
        Collections.sort(nodes);

        for (String node: nodes) {
            sb.append(node).append(" -> { ");

            ArrayList<String> neighbors = new ArrayList<>(this.connections.get(node).keySet());
            Collections.sort(neighbors);

            for (int i = 0; i < neighbors.size(); i++) {
                String dest = neighbors.get(i);
                sb.append(dest).append("=").append(this.connections.get(node).get(dest));

                if (i < neighbors.size() - 1) {
                    sb.append(", ");
                }
            }

            sb.append(" }\n");
        }
        return sb.toString();
    }

    /**
     * Converts the graph into a formatted JSON string matching the TA's examples.
     *
     * @return a pretty-printed JSON representation of the graph similar to the examples
     */
    @Override
    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        // sort alphabetically
        ArrayList<String> nodes = new ArrayList<>(this.connections.keySet());
        Collections.sort(nodes);

        for (int i = 0; i < nodes.size(); i++){
            String node = nodes.get(i);
            sb.append("    \"").append(node).append("\" : {\n");

            ArrayList<String> neighbors = new ArrayList<>(this.connections.get(node).keySet());
            Collections.sort(neighbors);

            for(int j = 0; j < neighbors.size(); j++){
                String dest = neighbors.get(j);
                Double weight = this.connections.get(node).get(dest);

                sb.append("        \"").append(dest).append("\" : ").append(weight);

                if(j < neighbors.size() -1){
                    sb.append(",");
                }
                sb.append("\n");
            }
            sb.append("    }");

            if (i < nodes.size() -1) {
                sb.append(",");
            }

            sb.append("\n");
            }
        sb.append("}");
        return sb.toString();

    }

}
