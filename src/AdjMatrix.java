/**
 *  A directed weighted graph implementation using an adjacency matrix
 *
 *  This structure stores edge weights in a 2D matrix, where each row and
 *  column corresponds to a node. The matrix allows constant-time edge lookups.
 *  Each node is assigned an index, and two maps are maintained to translate
 *  between node names and matrix indices. The matrix automatically expands when
 *  new nodes are added.
 *
 * Reflection:
 * When should you use a matrix to represent a directed weighted graph?
 * Why? Does this reasoning hold true for other types of graphs?
 *
 *A matrix is best for representing a directed weighted graph when the graph is dense.
 * In a dense graph, most of the possible edges actually exist, so storing every possible
 * edge position in a matrix is efficient and allows constant‑time lookups. This also makes
 * it easy to check whether an edge exists without scanning through a list.
 *
 * This same reasoning applies to other types of graphs as well. Dense graphs benefit from
 * adjacency matrices, while sparse graphs are usually better represented with adjacency lists.
 *
 */
import java.util.*;

public class AdjMatrix implements Digraph {

    /**
     * The adjacency matrix storing edge weights.
     * A null entry indicates no edge between two nodes.
     */
    private Double[][] weight;

    /**
     * Maps node names to their matrix index.
     */
    private HashMap<String, Integer> keyMap;

    /**
     * Constructs an empty adjacency matrix graph.
     */
    public AdjMatrix(){
        this.keyMap = new HashMap<>();
        this.weight = new Double[0][0];
    }

    /**
     * Adds a new node to the graph.
     *
     * @param key the node to add
     * @return true if added, false if null or already exists
     */
    @Override
    public boolean add(String key) {
        if (key == null || keyMap.containsKey(key)) {
            return false;
        }

        int newIndex = keyMap.size();
        keyMap.put(key, newIndex);

        // Expand matrix
        Double[][] newMatrix = new Double[newIndex + 1][newIndex + 1];

        for (int i = 0; i < weight.length; i++) {
            for (int j = 0; j < weight[i].length; j++) {   // ← FIXED HERE
                newMatrix[i][j] = weight[i][j];
            }
        }

        weight = newMatrix;
        return true;
    }


    /**
     * Adds a directed weighted edge from src to dest.
     * Automatically adds missing nodes.
     *
     * @return true if added, false if edge exists or params invalid
     */
    @Override
    public boolean add(String src, String dest, Double w) {
        if(src == null || dest == null || w == null){
            return false;
        }

        // add nodes
        if(!keyMap.containsKey(src)) {
            add(src);
        }
        if (!keyMap.containsKey(dest)){
            add(dest);
        }
        int i = keyMap.get(src);
        int j = keyMap.get(dest);

        if(weight[i][j] != null){
            return false;
        }

        weight[i][j] = w;
        return true;
    }

    /**
     * Deletes a node from the graph and removes all inbound and outbound edges
     * associated with it. The adjacency matrix is rebuilt to maintain correct
     * index alignment after the removal.
     *
     * @param key the node to delete
     * @return the deleted node's name, or null if the node did not exist
     */
    @Override
    public String delete(String key) {
        if (key == null || !keyMap.containsKey(key)) {
            return null;
        }

        // Get remaining nodes in sorted order
        ArrayList<String> remaining = nodes();
        remaining.remove(key);

        // Rebuild a fresh matrix
        AdjMatrix rebuilt = new AdjMatrix();

        // Add nodes
        for (String node : remaining) {
            rebuilt.add(node);
        }

        // Add edges
        for (String src : remaining) {
            for (String dest : remaining) {
                Double w = weight(src, dest);
                if (w != null) {
                    rebuilt.add(src, dest, w);
                }
            }
        }

        // Replace internal structures
        this.keyMap = rebuilt.keyMap;
        this.weight = rebuilt.weight;

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
        if (src == null || dest == null) return null;
        if (!keyMap.containsKey(src) || !keyMap.containsKey(dest)) return null;

        int i = keyMap.get(src);
        int j = keyMap.get(dest);

        Double old = weight[i][j];
        weight[i][j] = null;
        return old;
    }

    /**
     * Returns all nodes in the graph.
     */
    @Override
    public ArrayList<String> nodes() {
        ArrayList<String> list = new ArrayList<>(keyMap.keySet());
        Collections.sort(list);
        return list;
    }

    /**
     * Returns all outgoing neighbors of a node.
     */
    @Override
    public ArrayList<String> edges(String key){
        ArrayList<String> result = new ArrayList<>();

        if (key == null || !keyMap.containsKey(key)){
            return result;
        }

        int i = keyMap.get(key);
        for (String dest : keyMap.keySet()){
            int j = keyMap.get(dest);
            if(weight[i][j] != null){
                result.add(dest);
            }
        }
        Collections.sort(result);
        return result;
    }

    /**
     * Returns the weight of the edge from src to dest.
     */
    @Override
    public Double weight(String src, String dest){
        if(src == null || dest == null) return null;

        if (!keyMap.containsKey(src) || !keyMap.containsKey(dest)) return null;

        return weight[keyMap.get(src)][keyMap.get(dest)];
    }

    /**
     * Computes global graph density.
     */
    @Override
    public double density(){
        int n = keyMap.size();
        if(n <= 1) return 0.0;

        int edges = 0;
        for(int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                if (weight[i][j] != null) edges++;
            }
        }
        double maxEdges = n * (n -1);
        return edges / maxEdges;
    }

    /**
     * Computes local density of a node.
     */
    @Override
    public double density(String key){
        if(key == null || !keyMap.containsKey(key)) return 0.0;

        int n = keyMap.size();
        if (n <= 1) return 0.0;

        int i = keyMap.get(key);
        int outgoing = 0;

        for (int j =0; j < n; j++){
            if (weight[i][j] != null) outgoing ++;
        }

        return outgoing / (double)(n -1);
    }

    /**
     * Returns number of nodes.
     */
    @Override
    public int size(){
        return keyMap.size();
    }

    /**
     * Returns a readable adjacency matrix representation.
     */
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        ArrayList<String> nodes = nodes();

        sb.append("    ");
        for(String col : nodes){
            sb.append(col).append(" ");
        }
        sb.append("\n");

        for(String row : nodes){
            sb.append(row).append(" ");

            for(String col : nodes){
                Double w = weight[keyMap.get(row)][keyMap.get(col)];
                sb.append(w == null ? "." : w).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns a JSON representation matching TA formatting.
     */
    @Override
    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");

        ArrayList<String> nodes = nodes();

        for (int i = 0; i < nodes.size(); i++) {
            String src = nodes.get(i);
            sb.append("    \"").append(src).append("\" : {\n");

            ArrayList<String> neighbors = edges(src);

            for (int j = 0; j < neighbors.size(); j++) {
                String dest = neighbors.get(j);
                Double w = weight(src, dest);

                sb.append("        \"").append(dest).append("\" : ").append(w);

                if (j < neighbors.size() - 1) sb.append(",");
                sb.append("\n");
            }

            sb.append("    }");
            if (i < nodes.size() - 1) sb.append(",");
            sb.append("\n");
        }

        sb.append("}");
        return sb.toString();
    }
}

