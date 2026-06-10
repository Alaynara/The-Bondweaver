import java.util.*;

public class Driver {

    public static void main(String[] args) {

        System.out.println("===== GRAPH SEARCH TEST DRIVER =====\n");

        // ---------------------------------------------------------
        // Test 1: Dijkstra (no negative edges)
        // ---------------------------------------------------------
        DWGraph g1 = new DWGraph();
        g1.add("A");
        g1.add("B");
        g1.add("C");
        g1.add("D");

        g1.add("A", "B", 4.0);
        g1.add("A", "C", 1.0);
        g1.add("C", "B", 2.0);
        g1.add("B", "D", 1.0);
        g1.add("C", "D", 5.0);

        System.out.println("=== Test 1: Dijkstra (A → D) ===");
        printPath(g1.search("A", "D"));


        // ---------------------------------------------------------
        // Test 2: Bellman-Ford (negative edge exists)
        // ---------------------------------------------------------
        DWGraph g2 = new DWGraph();
        g2.add("S");
        g2.add("A");
        g2.add("B");
        g2.add("T");

        g2.add("S", "A", 1.0);
        g2.add("A", "B", -2.0);   // negative edge
        g2.add("B", "T", 3.0);

        System.out.println("\n=== Test 2: Bellman-Ford (S → T) ===");
        printPath(g2.search("S", "T"));


        // ---------------------------------------------------------
        // Test 3: Floyd-Warshall (<ALL>, <ALL>)
        // ---------------------------------------------------------
        DWGraph g3 = new DWGraph();
        g3.add("X");
        g3.add("Y");
        g3.add("Z");

        g3.add("X", "Y", 7.0);
        g3.add("Y", "Z", 2.0);
        g3.add("X", "Z", 10.0);

        System.out.println("\n=== Test 3: Floyd-Warshall (<ALL>, <ALL>) ===");
        printPath(g3.search("<ALL>", "<ALL>"));


        // ---------------------------------------------------------
        // Test 4: Unreachable path
        // ---------------------------------------------------------
        DWGraph g4 = new DWGraph();
        g4.add("A");
        g4.add("B");

        // No edges at all

        System.out.println("\n=== Test 4: Unreachable Path (A → B) ===");
        printPath(g4.search("A", "B"));


        // ---------------------------------------------------------
        // Test 5: Ensure DWGraph still works normally
        // ---------------------------------------------------------
        System.out.println("\n=== Test 5: DWGraph Basic Operations ===");

        DWGraph g5 = new DWGraph();
        g5.add("N1");
        g5.add("N2");
        g5.add("N3");

        g5.add("N1", "N2", 5.0);
        g5.add("N2", "N3", 6.0);

        System.out.println("Nodes: " + g5.nodes());
        System.out.println("Edges from N1: " + g5.edges("N1"));
        System.out.println("Weight N1→N2: " + g5.weight("N1", "N2"));
        System.out.println("Density: " + g5.density());
        System.out.println("JSON:\n" + g5.toJSON());
    }


    // ---------------------------------------------------------
    // Helper to print a Path cleanly
    // ---------------------------------------------------------
    private static void printPath(Search.Path p) {
        System.out.println("Source: " + p.src());
        System.out.println("Dest:   " + p.dest());
        System.out.println("Cost:   " + p.cost());
        System.out.println("Path:   " + Arrays.toString(p.path()));
    }
}
