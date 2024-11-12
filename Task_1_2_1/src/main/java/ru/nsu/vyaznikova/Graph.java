package ru.nsu.vyaznikova;

import java.util.List;

/**
 * Represents a graph data structure.
 *
 * @param <T> The type of the vertices in the graph.
 */
public interface Graph<T> {

    /**
     * Adds a new vertex to the graph.
     *
     * @param vertex The vertex to add.
     * @throws IllegalArgumentException if the vertex is null.
     */
    void addVertex(T vertex);

    /**
     * Removes a vertex from the graph.
     *
     * @param vertex The vertex to remove.
     * @throws IllegalArgumentException if the vertex does not exist.
     */
    void removeVertex(T vertex);

    /**
     * Adds an edge between two vertices.
     *
     * @param source      The source vertex of the edge.
     * @param destination The destination vertex of the edge.
     * @throws IllegalArgumentException if one or both vertices
     *     do not exist or if the edge already exists.
     */
    void addEdge(T source, T destination);

    /**
     * Removes an edge between two vertices.
     *
     * @param source      The source vertex of the edge.
     * @param destination The destination vertex of the edge.
     * @throws IllegalArgumentException if one or both vertices do not exist.
     */
    void removeEdge(T source, T destination);

    /**
     * Returns a list of neighbors of a given vertex.
     *
     * @param vertex The vertex to get neighbors for.
     * @return A list of neighbors of the vertex.
     * @throws IllegalArgumentException if the vertex does not exist.
     */
    List<T> getNeighbors(T vertex);

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
    void readFromFile(String filename);

    /**
     * Checks if this graph is equal to another object.
     *
     * @param other The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    boolean equals(Object other);

    /**
     * Returns a string representation of the graph.
     *
     * @return A string representation of the graph.
     */
    String toString();

    /**
     * Performs topological sort of the graph using Depth-First Search (DFS).
     *
     * @return A list of vertices in topological order,
     *     or null if the graph contains cycles.
     */
    List<T> topologicalSort();
}