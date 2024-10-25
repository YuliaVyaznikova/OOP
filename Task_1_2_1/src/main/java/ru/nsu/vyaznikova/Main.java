package ru.nsu.vyaznikova;

public class Main {

    public static void main(String[] args) {
        // Создаем граф с помощью матрицы смежности
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

        List<String> sortedAM = adjacencyMatrixGraph.topologicalSort();
        System.out.println("Topological Sort (Adjacency Matrix): " + sortedAM);

        // Создаем граф с помощью матрицы инцидентности
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

        List<String> sortedIM = incidenceMatrixGraph.topologicalSort();
        System.out.println("Topological Sort (Incidence Matrix): " + sortedIM);

        // Создаем граф с помощью списка смежности
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

        List<String> sortedAL = adjacencyListGraph.topologicalSort();
        System.out.println("Topological Sort (Adjacency List): " + sortedAL);

        // Чтение графа из файла
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