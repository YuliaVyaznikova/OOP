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
public class AdjacencyMatrix<T> extends AbstractGraph<T> {
    private List<List<Integer>> matrix;

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
        try {
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
        } catch (IllegalArgumentException e) {
            System.err.println("Error removing vertex: " + e.getMessage());
        }
    }

    /**
     * Adds an edge between two vertices.
     *
     * @param source      The source vertex of the edge.
     * @param destination The destination vertex of the edge.
     * @throws IllegalArgumentException if one or both vertices
     *     do not exist or if the edge already exists.
     */
    @Override
    public void addEdge(T source, T destination) {
        try {
            if (!vertices.contains(source) || !vertices.contains(destination)) {
                throw new IllegalArgumentException("One or both vertices do not exist");
            }
            int sourceIndex = vertices.indexOf(source);
            int destinationIndex = vertices.indexOf(destination);
            if (matrix.get(sourceIndex).get(destinationIndex) == 1) {
                throw new IllegalArgumentException("Edge already exists");
            }
            matrix.get(sourceIndex).set(destinationIndex, 1); // Set value 1 in the matrix
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
            matrix.get(sourceIndex).set(destinationIndex, 0); // Set value 0 in the matrix
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
            for (int i = 0; i < numVertices; i++) {
                if (matrix.get(vertexIndex).get(i) == 1) {
                    neighbors.add(vertices.get(i));
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
        sb.append("Adjacency Matrix:\n");
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                sb.append(matrix.get(i).get(j)).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public List<T> topologicalSort() {
        // 1. Проверяем, не пуст ли граф
        if (numVertices == 0) {
            return new ArrayList<>(); // Возвращаем пустой список, если граф пуст
        }

        // 2. Создаем структуру данных для хранения результата сортировки
        List<T> sorted = new ArrayList<>();

        // 3. Создаем структуру данных для отслеживания посещенных вершин
        List<Integer> visited = new ArrayList<>(); // List for tracking visited vertices

        // 4. Создаем стек для хранения вершин в порядке обхода
        Stack<T> stack = new Stack<>();

        // 5. Проходим по всем вершинам графа
        for (int i = 0; i < numVertices; i++) {
            // 6. Проверяем, не была ли вершина уже посещена
            if (!visited.contains(i)) {
                // 7. Вызываем рекурсивную функцию DFS для текущей вершины
                dfs(i, visited, stack);
            }
        }

        // 8. Достаем вершины из стека в обратном порядке
        while (!stack.isEmpty()) {
            sorted.add(stack.pop());
        }

        // 9. Возвращаем отсортированный список вершин
        return sorted;
    }

    private void dfs(int vertexIndex, List<Integer> visited, Stack<T> stack) {
        // 1. Помечаем текущую вершину как посещенную
        visited.add(vertexIndex);

        // 2. Проходим по всем соседям текущей вершины
        for (int i = 0; i < numVertices; i++) {
            // 3. Проверяем, есть ли ребро между текущей вершиной и соседом
            if (matrix.get(vertexIndex).get(i) == 1) {
                // 4. Получаем индекс соседа
                int neighborIndex = i;
                // 5. Проверяем, не была ли эта вершина уже посещена
                if (!visited.contains(neighborIndex)) {
                    // 6. Вызываем рекурсивно dfs для соседа
                    dfs(neighborIndex, visited, stack);
                }
            }
        }

        // 7. Добавляем текущую вершину в стек
        stack.push(vertices.get(vertexIndex)); // Add the vertex to the stack after visiting all its neighbors
    }

    @Override
    protected boolean compareEdges(AbstractGraph<T> other) {
        if (other instanceof AdjacencyMatrix) {
            AdjacencyMatrix<T> otherMatrix = (AdjacencyMatrix<T>) other;
            if (matrix.size() != otherMatrix.matrix.size()) {
                return false;
            }
            for (int i = 0; i < matrix.size(); i++) {
                for (int j = 0; j < matrix.size(); j++) {
                    if (matrix.get(i).get(j) != otherMatrix.matrix.get(i).get(j)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}