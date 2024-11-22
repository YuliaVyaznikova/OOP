package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.List;

/**
 * abstract graph
 * @param <T>
 */
public abstract class AbstractGraph<T> implements Graph<T> {
    protected int numVertices;
    protected List<T> vertices;

    /**
     * Returns the number of vertices in the graph.
     *
     * @return The number of vertices in the graph.
     */
    public int getNumVertices() {
        return numVertices;
    }

    /**
     * Returns the number of edges in the graph.
     *
     * @return The number of edges in the graph.
     */
    public abstract int getNumEdges();

    /**
     * Checks if the graph contains the given vertex.
     *
     * @param vertex The vertex to check for.
     * @return True if the graph contains the vertex, false otherwise.
     */
    public boolean containsVertex(T vertex) {
        return vertices.contains(vertex);
    }

    /**
     * Checks if the graph contains an edge between the given source and destination vertices.
     *
     * @param source      The source vertex of the edge.
     * @param destination The destination vertex of the edge.
     * @return True if the graph contains the edge, false otherwise.
     */
    public abstract boolean hasEdge(T source, T destination);

    /**
     * Adds a new vertex to the graph.
     *
     * @param vertex The vertex to add.
     * @throws IllegalArgumentException if the vertex is null.
     */
    @Override
    public abstract void addVertex(T vertex);

    /**
     * Removes a vertex from the graph.
     *
     * @param vertex The vertex to remove.
     * @throws IllegalArgumentException if the vertex does not exist.
     */
    @Override
    public abstract void removeVertex(T vertex);

    /**
     * Adds an edge between two vertices.
     *
     * @param source      The source vertex of the edge.
     * @param destination The destination vertex of the edge.
     * @throws IllegalArgumentException if one or both vertices do not exist.
     */
    @Override
    public abstract void addEdge(T source, T destination);

    /**
     * Removes an edge between two vertices.
     *
     * @param source      The source vertex of the edge.
     * @param destination The destination vertex of the edge.
     * @throws IllegalArgumentException if one or both vertices do not exist.
     */
    @Override
    public abstract void removeEdge(T source, T destination);

    /**
     * Returns a list of neighbors of a given vertex.
     *
     * @param vertex The vertex to get neighbors for.
     * @return A list of neighbors of the vertex.
     * @throws IllegalArgumentException if the vertex does not exist.
     */
    @Override
    public abstract List<T> getNeighbors(T vertex);

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
     * @throws IllegalArgumentException if the file is invalid or cannot be read.
     */
    @Override
    public abstract void readFromFile(String filename);

    /**
     * Returns a string representation of the graph.
     *
     * @return A string representation of the graph.
     */
    @Override
    public abstract String toString();

    /**
     * Compares the edges of this graph with the edges of another graph.
     *
     * @param other The other graph to compare with.
     * @return True if the graphs have the same edges, false otherwise.
     */
    public abstract boolean compareEdges(AbstractGraph<T> other);

    /**
     * Performs a topological sort on the graph.
     *
     * @return A list of vertices in topological order.
     * @throws IllegalArgumentException if the graph contains a cycle.
     */
    @Override
    public abstract List<T> topologicalSort();

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
        AbstractGraph<T> other = (AbstractGraph<T>) obj;
        if (numVertices != other.numVertices) {
            return false;
        }
        if (vertices.size() != other.vertices.size()) {
            return false;
        }
        // Сравниваем вершины
        for (int i = 0; i < vertices.size(); i++) {
            if (!vertices.get(i).equals(other.vertices.get(i))) {
                return false;
            }
        }
        // Сравниваем ребра (реализация зависит от конкретного типа графа)
        return compareEdges(other);
    }
}