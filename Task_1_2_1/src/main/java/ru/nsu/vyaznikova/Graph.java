package ru.nsu.vyaznikova;

import java.util.List;

public interface Graph<T> {
    void addVertex(T vertex);
    void removeVertex(T vertex);
    void addEdge(T source, T destination);
    void removeEdge(T source, T destination);
    List<T> getNeighbors(T vertex);
    void readFromFile(String filename);
    boolean equals(Object other);
    String toString();
    List<T> topologicalSort();
}