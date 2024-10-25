package ru.nsu.vyaznikova;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Represents a graph using an incidence matrix data structure with a growing array.
 * The graph can handle vertices of any type using generics.
 *
 * @param <T> The type of the vertices in the graph.
 */
class IncidenceMatrix<T> extends AbstractGraph<T> {
    private List<List<Integer>> matrix;
    private int numEdges;

    /**
     * Constructs an empty graph.
     */
    public IncidenceMatrix() {
        this.numVertices = 0;
        this.numEdges = 0;
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
        try {
            if (vertex == null) {
                throw new IllegalArgumentException("Vertex cannot be null");
            }
            vertices.add(vertex);
            numVertices++;
            for (List<Integer> row : matrix) {
                row.add(0);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error adding vertex: " + e.getMessage());
        }
    }

    /**
     * Removes a vertex from the graph.
     *
     * @param vertex The vertex to remove.
     * @throws IllegalArgumentException if the vertex does not exist.
     */
    @Override
    public void removeVertex(T vertex) {
        try {
            if (!vertices.contains(vertex)) {
                throw new IllegalArgumentException("Vertex does not exist");
            }

            int vertexIndex = vertices.indexOf(vertex);
            vertices.remove(vertexIndex);
            for (List<Integer> row : matrix) {
                row.remove(vertexIndex);
            }
            numVertices--;
        } catch (IllegalArgumentException e) {
            System.err.println("Error removing vertex: " + e.getMessage());
        }
    }

    /**
     * Adds an edge between two vertices.
     *
     * @param source      The source vertex of the edge.
     * @param destination The destination vertex of the edge.
     * @throws IllegalArgumentException if one or both vertices do not exist.
     */
    @Override
    public void addEdge(T source, T destination) {
        try {
            if (!vertices.contains(source) || !vertices.contains(destination)) {
                throw new IllegalArgumentException("One or both vertices do not exist");
            }
            int sourceIndex = vertices.indexOf(source);
            int destinationIndex = vertices.indexOf(destination);
            List<Integer> newRow = new ArrayList<>(Collections.nCopies(numVertices, 0));
            newRow.set(sourceIndex, 1);
            newRow.set(destinationIndex, -1);
            matrix.add(newRow);
            numEdges++;
        } catch (IllegalArgumentException e) {
            System.err.println("Error adding edge: " + e.getMessage());
        }
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
        try {
            if (!vertices.contains(source) || !vertices.contains(destination)) {
                throw new IllegalArgumentException("One or both vertices do not exist");
            }
            int sourceIndex = vertices.indexOf(source);
            int destinationIndex = vertices.indexOf(destination);
            for (int i = 0; i < matrix.size(); i++) {
                if (matrix.get(i).get(sourceIndex) ==
                        1 &&
                        matrix.get(i).get(destinationIndex) == -1) {
                    matrix.remove(i);
                    numEdges--;
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error removing edge: " + e.getMessage());
        }
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
        try {
            if (!vertices.contains(vertex)) {
                throw new IllegalArgumentException("Vertex does not exist");
            }

            int vertexIndex = vertices.indexOf(vertex);
            for (int i = 0; i < matrix.size(); i++) {
                if (matrix.get(i).get(vertexIndex) != 0) {
                    for (int j = 0; j < numVertices; j++) {
                        if (matrix.get(i).get(j) != 0 && j != vertexIndex) {
                            neighbors.add(vertices.get(j));
                        }
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error getting neighbors: " + e.getMessage());
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
     * Remaining lines: edges, where each line contains
     * a pair of vertices separated by space (e.g., A B).
     *
     * @param filename The name of the file to read from.
     */
    @Override
    public void readFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // 1. Read the number of vertices
            int numVertices = Integer.parseInt(reader.readLine());
            this.numVertices = numVertices;
            this.matrix = new ArrayList<>(Collections.nCopies(numVertices,
                    new ArrayList<>(Collections.nCopies(numVertices, 0))));
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
        } catch (NumberFormatException e) {
            System.err.println("Error reading file: Invalid number format.");
        }
    }

    /**
     * Returns a string representation of the graph.
     *
     * @return A string representation of the graph.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Incidence Matrix:\n");
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < numVertices; j++) {
                sb.append(matrix.get(i).get(j)).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    protected boolean compareEdges(AbstractGraph<T> other) {
        if (other instanceof IncidenceMatrix) {
            IncidenceMatrix<T> otherMatrix = (IncidenceMatrix<T>) other;
            if (numEdges != otherMatrix.numEdges) {
                return false;
            }
            if (matrix.size() != otherMatrix.matrix.size()) {
                return false;
            }
            for (int i = 0; i < matrix.size(); i++) {
                for (int j = 0; j < numVertices; j++) {
                    if (matrix.get(i).get(j) != otherMatrix.matrix.get(i).get(j)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

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
        for (int i = 0; i < matrix.size(); i++) {
            if (matrix.get(i).get(vertexIndex) != 0) {
                for (int j = 0; j < numVertices; j++) {
                    if (matrix.get(i).get(j) != 0 && j != vertexIndex) {
                        int neighborIndex = j;
                        if (!visited.contains(neighborIndex)) {
                            dfs(neighborIndex, visited, stack);
                        }
                    }
                }
            }
        }
        stack.push(vertices.get(vertexIndex));
        // Add the vertex to the stack after visiting all its neighbors
    }
}