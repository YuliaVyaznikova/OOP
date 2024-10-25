package ru.nsu.vyaznikova;

import java.util.List;

/**
 * Abstract class representing a graph with common operations.
 *
 * @param <T> The type of the vertices in the graph.
 */
public abstract class AbstractGraph<T> implements Graph<T> {
    public int numVertices;
    public List<T> vertices;

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

    /**
     * Abstract method for comparing edges between two graphs.
     * Implementations should compare edges based on their specific data structures.
     *
     * @param other The other graph to compare with.
     * @return true if the edges are equal, false otherwise.
     */
    protected abstract boolean compareEdges(AbstractGraph<T> other);

    /**
     * Returns a string representation of the graph.
     *
     * @return A string representation of the graph.
     */
    @Override
    public abstract String toString();

    @Override
    public abstract List<T> topologicalSort();
}