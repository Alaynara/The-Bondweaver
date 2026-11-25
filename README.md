# The Bondweaver: Adaptive Directed Weighted Graph (Java)

A Java implementation of a directed, weighted graph that **automatically switches its internal storage** between an adjacency list and an adjacency matrix as the graph's density changes, and **automatically picks the right shortest-path algorithm** (Dijkstra's, Bellman-Ford, or Floyd-Warshall) based on the graph's properties.

The rest of the program works through a single facade class, `DWGraph`, so callers never need to know how the graph is stored or which algorithm is running underneath.

## Features

**Adaptive graph representation**
- Starts as an adjacency list (efficient for sparse graphs)
- Converts to an adjacency matrix when density rises above a configurable threshold (default `0.5`), and back to a list when it drops below
- Global density: `edges / (n * (n - 1))`; local (per-node) density: `outgoing edges / (n - 1)`

**Automatic shortest-path algorithm selection** (Strategy pattern)

| Condition | Algorithm | Why |
|---|---|---|
| `src` and `dest` are both `"<ALL>"` | Floyd-Warshall | All-pairs shortest paths |
| Graph contains a negative edge weight | Bellman-Ford | Handles negative weights correctly |
| Otherwise | Dijkstra's (priority queue) | Fastest for non-negative weights |

**Graph operations**
- Add and delete nodes and directed weighted edges (deleting a node removes all of its inbound and outbound edges)
- Query nodes, outgoing edges, edge weights, size, and density
- Export to readable text and JSON
- Load a graph from a JSON file

## Design

| Pattern | Where | Purpose |
|---|---|---|
| Interface | `Digraph` | Common contract shared by every graph representation |
| Facade | `DWGraph` | Hides list vs. matrix storage and algorithm selection from callers |
| Strategy | `Search` | Lets shortest-path algorithms be swapped at runtime |

**Why a 0.5 density threshold?** It's the point where half of all possible edges exist. Below it, most matrix cells would be empty and waste memory. Above it, scanning lists costs more than direct matrix lookups.

## Project Structure

```
The-Bondweaver/
├── src/
│   ├── Digraph.java        # Interface: core directed graph operations
│   ├── AdjList.java        # Adjacency list implementation
│   ├── AdjMatrix.java      # Adjacency matrix implementation
│   ├── DWGraph.java        # Facade: auto-switching storage + algorithm selection
│   ├── Search.java         # Strategy interface + Path record
│   ├── Dijkstras.java      # Dijkstra's shortest path
│   ├── BellmanFord.java    # Bellman-Ford shortest path (negative weights)
│   ├── FloydWarshall.java  # Floyd-Warshall all-pairs shortest paths
│   └── Driver.java         # Test driver
├── .gitignore
└── README.md
```

## How to Run

Requires Java 16 or later (the code uses Java `record`s).

**From the command line** (run from the project root):

```bash
javac -d out src/*.java
java -cp out Driver
```

**In IntelliJ IDEA:** open the project and run `Driver.java`.

## Example Output

```
=== Test 1: Dijkstra (A → D) ===
Source: A
Dest:   D
Cost:   4
Path:   [A, C, B, D]

=== Test 2: Bellman-Ford (S → T) ===
Source: S
Dest:   T
Cost:   2
Path:   [S, A, B, T]
```

The driver also tests Floyd-Warshall, an unreachable destination, and basic graph operations including JSON export.

## Usage Example

```java
DWGraph g = new DWGraph();          // or new DWGraph(0.6) for a custom threshold
g.add("A", "B", 4.0);               // missing nodes are added automatically
g.add("A", "C", 1.0);
g.add("C", "B", 2.0);

Search.Path p = g.search("A", "B"); // Dijkstra's is selected automatically
System.out.println(p.cost());       // 3
System.out.println(String.join(" -> ", p.path())); // A -> C -> B
```

## JSON Format

Graphs can be exported with `toJSON()` and loaded with `new DWGraph("path/to/graph.json")`:

```json
{
    "A" : { "B" : 1.0, "C" : 2.0 },
    "B" : { "C" : 3.0 },
    "C" : { }
}
```

## Future Improvements

- Detect negative-weight cycles in Bellman-Ford
- Return the full all-pairs distance matrix from Floyd-Warshall
- Add JUnit tests for each representation and algorithm

## Author

**Alayna Ra**
Cyber Operations Student, Eastern Washington University
