/**
 * A facade class that hides whether the underlying graph is stored as an
 * adjacency list or an adjacency matrix. DWGraph automatically switches
 * between the two representations based on the current density of the graph.
 *
 * Reflection:
 * What benefit does using a facade provide?
 * What would happen if this file was omitted from the assignment specification?
 *
 * A facade makes the project easier to use because it hides whether the graph is stored as
 * a list or a matrix. The rest of the program only talks to DWGraph, so it doesn’t need to
 * worry about how the graph is actually implemented. If this file were removed, every other
 * part of the assignment would have to choose between AdjList and AdjMatrix manually, which
 * would make the code messy, repetitive, and harder to maintain.
 */

import java.util.*;

public class DWGraph implements Digraph {

    /** The active graph representation (AdjList or AdjMatrix). */
    private Digraph graph;

    /** Density threshold for switching representations. */
    private double threshold;

    /** The current shortest-path strategy selected at runtime. */
    private Search strategy;


    /**
     * Creates a DWGraph using an adjacency list as the initial representation
     * and a default density threshold of 0.5. The graph will automatically
     * switch between an adjacency list and an adjacency matrix as its density
     * changes during runtime.
     */
    public DWGraph() {
        this.graph = new AdjList();
        this.threshold = 0.5; // you can adjust this later
    }

    /**
     * Creates a DWGraph using an adjacency list as the initial representation
     * and a custom density threshold. The threshold determines when the graph
     * should switch between an adjacency list and an adjacency matrix.
     *
     * @param threshold the density value at which the graph switches
     * representations (0.0–1.0)
     */
    public DWGraph(double threshold) {
        this.graph = new AdjList();
        this.threshold = threshold;
    }

    /**
     * Creates a DWGraph and populates it from a JSON file.
     *
     * @param filepath the path to the JSON file
     */
    public DWGraph(String filepath) {
        this.graph = new AdjList();
        this.threshold = 0.5;

        DWGraph loaded = DWGraph.load(filepath);

        // Copy loaded graph into this one
        for (String node : loaded.nodes()) {
            this.add(node);
        }
        for (String src : loaded.nodes()) {
            for (String dest : loaded.edges(src)) {
                this.add(src, dest, loaded.weight(src, dest));
            }
        }
    }

    /**
     * Checks whether the graph should switch between list and matrix.
     * Converts based on the current density and threshold.
     */
    private void convert() {
        double d = graph.density();

        if (d > threshold && graph instanceof AdjList) {
            graph = convertToMatrix((AdjList) graph);
        }
        else if (d <= threshold && graph instanceof AdjMatrix) {
            graph = convertToList((AdjMatrix) graph);
        }
    }

    // Digraph methods

    @Override
    public boolean add(String key) {
        boolean result = graph.add(key);
        convert();
        return result;
    }

    @Override
    public boolean add(String src, String dest, Double weight) {
        boolean result = graph.add(src, dest, weight);
        convert();
        return result;
    }

    @Override
    public String delete(String key) {
        String result = graph.delete(key);
        convert();
        return result;
    }

    @Override
    public Double delete(String src, String dest) {
        Double result = graph.delete(src, dest);
        convert();
        return result;
    }

    @Override
    public ArrayList<String> nodes() {
        return graph.nodes();
    }

    @Override
    public ArrayList<String> edges(String key) {
        return graph.edges(key);
    }

    @Override
    public Double weight(String src, String dest) {
        return graph.weight(src, dest);
    }

    @Override
    public double density() {
        return graph.density();
    }

    @Override
    public double density(String key) {
        return graph.density(key);
    }

    @Override
    public int size() {
        return graph.size();
    }

    @Override
    public String toString() {
        return graph.toString();
    }

    @Override
    public String toJSON() {
        return graph.toJSON();
    }

    /**
     * Loads a DWGraph from a JSON file.
     * Format:
     * {
     *     "A" : { "B" : 1.0, "C" : 2.0 },
     *     "B" : { "C" : 3.0 }
     * }
     *
     * @param filepath the path to the JSON file
     * @return a populated DWGraph instance
     */
    public static DWGraph load(String filepath) {
        DWGraph g = new DWGraph();
        try {
            // Read entire file into a single string
            Scanner sc = new Scanner(new java.io.File(filepath));
            StringBuilder json = new StringBuilder();
            while (sc.hasNextLine()) {
                json.append(sc.nextLine().trim());
            }
            sc.close();

            // Remove outer braces
            String content = json.toString();
            content = content.substring(1, content.length() - 1).trim();

            // Split top-level nodes
            String[] entries = content.split("},");
            for (String entry : entries) {
                entry = entry.trim();
                if (entry.endsWith("}")) entry = entry.substring(0, entry.length() - 1);

                // Split "Node" : { ... }
                String[] parts = entry.split(":", 2);
                String src = parts[0].trim().replace("\"", "");
                g.add(src);

                // Extract inner edges
                String inner = parts[1].trim();
                if (inner.startsWith("{")) inner = inner.substring(1);
                if (inner.endsWith("}")) inner = inner.substring(0, inner.length() - 1);

                if (inner.isEmpty()) continue;

                // Split edges
                String[] edges = inner.split(",");
                for (String e : edges) {
                    String[] kv = e.split(":");
                    String dest = kv[0].trim().replace("\"", "");
                    Double weight = Double.parseDouble(kv[1].trim());
                    g.add(src, dest, weight);
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading JSON: " + e.getMessage());
        }

        return g;
    }



    /**
     * Converts an adjacency list into an adjacency matrix.
     *
     * @param list the AdjList to convert
     * @return a new AdjMatrix containing the same nodes and edges
     */
    private AdjMatrix convertToMatrix(AdjList list) {
        AdjMatrix matrix = new AdjMatrix();

        for (String node : list.nodes()) {
            matrix.add(node);
        }

        for (String src : list.nodes()) {
            for (String dest : list.edges(src)) {
                Double w = list.weight(src, dest);
                matrix.add(src, dest, w);
            }
        }

        return matrix;
    }

    /**
     * Converts an adjacency matrix into an adjacency list.
     *
     * @param matrix the AdjMatrix to convert
     * @return a new AdjList containing the same nodes and edges
     */
    private AdjList convertToList(AdjMatrix matrix) {
        AdjList list = new AdjList();

        for (String node : matrix.nodes()) {
            list.add(node);
        }

        for (String src : matrix.nodes()) {
            for (String dest : matrix.edges(src)) {
                Double w = matrix.weight(src, dest);
                list.add(src, dest, w);
            }
        }

        return list;
    }



    /**
     * Computes the shortest path between two nodes.
     * DWGraph analyzes the graph and selects the appropriate algorithm:
     *
     * - Floyd-Warshall if src and dest are both "<ALL>"
     * - Bellman-Ford if any negative edge exists
     * - Dijkstra's otherwise
     *
     * @param src  the starting node or "<ALL>"
     * @param dest the destination node or "<ALL>"
     * @return a Path record representing the shortest path
     */
    public Search.Path search(String src, String dest) {

        boolean hasNegative = false;

        // Scan for negative
        for (String u : nodes()) {
            for (String v : edges(u)) {
                if (weight(u, v) < 0) {
                    hasNegative = true;
                    break;
                }
            }
        }


        if (src.equals("<ALL>") && dest.equals("<ALL>")) {
            strategy = new FloydWarshall();
        }
        else if (hasNegative) {
            strategy = new BellmanFord();
        }
        else {
            strategy = new Dijkstras();
        }

        return strategy.findPath(this, src, dest);
    }

}
