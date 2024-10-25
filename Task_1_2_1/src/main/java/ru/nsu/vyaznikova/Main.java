package ru.nsu.vyaznikova;

import java.util.List;

/**
 * The main class for demonstrating the graph implementations and topological sort.
 */
public class Main {

    /**
     * Main method of the program.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Create a graph using an adjacency matrix
        AdjacencyMatrix<String> adjacencyMatrixGraph = new AdjacencyMatrix<>();
        adjacencyMatrixGraph.addVertex("A");
        adjacencyMatrixGraph.addVertex("B");
        adjacencyMatrixGraph.addVertex("C");
        adjacencyMatrixGraph.addVertex("D");
        adjacencyMatrixGraph.addVertex("E");
        adjacencyMatrixGraph.addVertex("F");
        adjacencyMatrixGraph.addEdge("A", "B");
        adjacencyMatrixGraph.addEdge("A", "C");
        adjacencyMatrixGraph.addEdge("B", "D");
        adjacencyMatrixGraph.addEdge("C", "E");
        adjacencyMatrixGraph.addEdge("D", "F");

        System.out.println("Adjacency Matrix Graph:");
        System.out.println(adjacencyMatrixGraph);

        List<String> sortedAm = adjacencyMatrixGraph.topologicalSort();
        System.out.println("Topological Sort (Adjacency Matrix): " + sortedAm);

        // Create a graph using an incidence matrix
        IncidenceMatrix<String> incidenceMatrixGraph = new IncidenceMatrix<>();
        incidenceMatrixGraph.addVertex("A");
        incidenceMatrixGraph.addVertex("B");
        incidenceMatrixGraph.addVertex("C");
        incidenceMatrixGraph.addVertex("D");
        incidenceMatrixGraph.addVertex("E");
        incidenceMatrixGraph.addVertex("F");
        incidenceMatrixGraph.addEdge("A", "B");
        incidenceMatrixGraph.addEdge("A", "C");
        incidenceMatrixGraph.addEdge("B", "D");
        incidenceMatrixGraph.addEdge("C", "E");
        incidenceMatrixGraph.addEdge("D", "F");

        System.out.println("\nIncidence Matrix Graph:");
        System.out.println(incidenceMatrixGraph);

        List<String> sortedIm = incidenceMatrixGraph.topologicalSort();
        System.out.println("Topological Sort (Incidence Matrix): " + sortedIm);

        // Create a graph using an adjacency list
        AdjacencyList<String> adjacencyListGraph = new AdjacencyList<>();
        adjacencyListGraph.addVertex("A");
        adjacencyListGraph.addVertex("B");
        adjacencyListGraph.addVertex("C");
        adjacencyListGraph.addVertex("D");
        adjacencyListGraph.addVertex("E");
        adjacencyListGraph.addVertex("F");
        adjacencyListGraph.addEdge("A", "B");
        adjacencyListGraph.addEdge("A", "C");
        adjacencyListGraph.addEdge("B", "D");
        adjacencyListGraph.addEdge("C", "E");
        adjacencyListGraph.addEdge("D", "F");

        System.out.println("\nAdjacency List Graph:");
        System.out.println(adjacencyListGraph);

        List<String> sortedAl = adjacencyListGraph.topologicalSort();
        System.out.println("Topological Sort (Adjacency List): " + sortedAl);

        // Read a graph from a file
        AdjacencyMatrix<String> graphFromFile = new AdjacencyMatrix<>();
        try {
            graphFromFile.readFromFile("graph.txt");
            System.out.println("\nGraph from file:");
            System.out.println(graphFromFile);
            List<String> sortedFromFile = graphFromFile.topologicalSort();
            System.out.println("Topological Sort (Graph from file): " + sortedFromFile);
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }
}