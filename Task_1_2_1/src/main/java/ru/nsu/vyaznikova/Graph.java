package ru.nsu.vyaznikova;

import java.util.List;

public interface Graph {
    void addVertex(int vertex);
    void removeVertex(int vertex);
    void addEdge(int source, int destination);
    void removeEdge(int source, int destination);
    List<Integer> getNeighbors(int vertex);
    void readFromFile(String filename);
    boolean equals(Object other);
    String toString();
    List<Integer> topologicalSort();
}