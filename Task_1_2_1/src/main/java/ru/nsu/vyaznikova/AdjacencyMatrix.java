package ru.nsu.vyaznikova;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Represents a graph using an adjacency matrix data structure with a growing array.
 * The graph can handle vertices of any type using generics.
 *
 * @param <T> The type of the vertices in the graph.
 */
public class AdjacencyMatrix<T> implements Graph<T> {
    private List<List<Integer>> matrix;
    private int numVertices;
    private List<T> vertices;

    /**
     * Constructs an empty graph.
     */
    public AdjacencyMatrix() {
        this.numVertices = 0;
        this.matrix = new ArrayList<>();
        this.vertices = new ArrayList<>();
    }

    /**
     * Adds a new vertex to the graph.
     *
     * @param vertex The vertex to add.
     * @throws IllegalArgumentException if the vertex is null.
     */
    @Override
    public void addVertex(T vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex cannot be null");
        }
        vertices.add(vertex);
        numVertices++;
        int newSize = numVertices;
        for (List<Integer> row : matrix) {
            row.add(0);
        }
        List<Integer> newRow = new ArrayList<>(Collections.nCopies(newSize, 0));
        matrix.add(newRow);
    }

    /**
     * Removes a vertex from the graph.
     *
     * @param vertex The vertex to remove.
     * @throws IllegalArgumentException if the vertex does not exist.
     */
    @Override
    public void removeVertex(T vertex) {
        if (!vertices.contains(vertex)) {
            throw new IllegalArgumentException("Vertex does not exist");
        }

        int vertexIndex = vertices.indexOf(vertex);
        vertices.remove(vertexIndex);
        matrix.remove(vertexIndex); // Remove the row
        for (List<Integer> row : matrix) {
            row.remove(vertexIndex); // Remove the column
        }
        numVertices--;
    }

    /**
     * Adds an edge between two vertices.
     *
     * @param source      The source vertex of the edge.
     * @param destination The destination vertex of the edge.
     * @throws IllegalArgumentException if one or both vertices do not exist or if the edge already exists.
     */
    @Override
    public void addEdge(T source, T destination) {
        if (!vertices.contains(source) || !vertices.contains(destination)) {
            throw new IllegalArgumentException("One or both vertices do not exist");
        }
        int sourceIndex = vertices.indexOf(source);
        int destinationIndex = vertices.indexOf(destination);
        if (matrix.get(sourceIndex).get(destinationIndex) == 1) {
            throw new IllegalArgumentException("Edge already exists");
        }
        matrix.get(sourceIndex).set(destinationIndex, 1); // Set value 1 in the matrix
    }

    /**
     * Removes an edge between two vertices.
     *
     * @param source      The source vertex of the edge.
     * @param destination The destination vertex of the edge.
     * @throws IllegalArgumentException if one or both vertices do not exist.
     */
    @Override
    public void removeEdge(T source, T destination) {
        if (!vertices.contains(source) || !vertices.contains(destination)) {
            throw new IllegalArgumentException("One or both vertices do not exist");
        }
        int sourceIndex = vertices.indexOf(source);
        int destinationIndex = vertices.indexOf(destination);
        matrix.get(sourceIndex).set(destinationIndex, 0); // Set value 0 in the matrix
    }

    /**
     * Returns a list of neighbors of a given vertex.
     *
     * @param vertex The vertex to get neighbors for.
     * @return A list of neighbors of the vertex.
     */
    @Override
    public List<T> getNeighbors(T vertex) {
        List<T> neighbors = new ArrayList<>();
        if (!vertices.contains(vertex)) {
            return neighbors; // Или выбросить исключение
        }

        int vertexIndex = vertices.indexOf(vertex);
        for (int i = 0; i < numVertices; i++) {
            if (matrix.get(vertexIndex).get(i) == 1) {
                neighbors.add(vertices.get(i));
            }
        }
        return neighbors;
    }

    /**
     * Reads the graph from a file.
     * The file format is as follows:
     * <br>
     * First line: number of vertices N.
     * <br>
     * Next N lines: vertex names (strings).
     * <br>
     * Remaining lines: edges, where each line contains a pair of vertices separated by space (e.g., A B).
     *
     * @param filename The name of the file to read from.
     */
    @Override
    public void readFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // 1. Read the number of vertices
            int numVertices = Integer.parseInt(reader.readLine());
            this.numVertices = numVertices;
            this.matrix = new ArrayList<>(Collections.nCopies(numVertices, new ArrayList<>(Collections.nCopies(numVertices, 0))));
            this.vertices = new ArrayList<>();

            // 2. Read vertex names
            for (int i = 0; i < numVertices; i++) {
                vertices.add((T) reader.readLine());
            }

            // 3. Read edges
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                T source = (T) parts[0];
                T destination = (T) parts[1];
                addEdge(source, destination);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Checks if this graph is equal to another object.
     *
     * @param obj The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AdjacencyMatrix<T> other = (AdjacencyMatrix<T>) obj;
        if (numVertices != other.numVertices) {
            return false;
        }
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (matrix.get(i).get(j) != other.matrix.get(i).get(j)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns a string representation of the graph.
     *
     * @return A string representation of the graph.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Adjacency Matrix:\n");
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                sb.append(matrix.get(i).get(j)).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Performs topological sort of the graph using Depth-First Search (DFS).
     *
     * @return A list of vertices in topological order, or null if the graph contains cycles.
     */
    @Override
    public List<T> topologicalSort() {
        List<T> sorted = new ArrayList<>();
        List<Integer> visited = new ArrayList<>(); // List for tracking visited vertices
        Stack<T> stack = new Stack<>();

        for (int i = 0; i < numVertices; i++) {
            if (!visited.contains(i)) {
                dfs(i, visited, stack);
            }
        }

        while (!stack.isEmpty()) {
            sorted.add(stack.pop());
        }

        return sorted;
    }

    private void dfs(int vertexIndex, List<Integer> visited, Stack<T> stack) {
        visited.add(vertexIndex); // Mark the vertex as visited
        for (int i = 0; i < numVertices; i++) {
            if (matrix.get(vertexIndex).get(i) == 1) {
                int neighborIndex = i;
                if (!visited.contains(neighborIndex)) {
                    dfs(neighborIndex, visited, stack);
                }
            }
        }
        stack.push(vertices.get(vertexIndex)); // Add the vertex to the stack after visiting all its neighbors
    }
}