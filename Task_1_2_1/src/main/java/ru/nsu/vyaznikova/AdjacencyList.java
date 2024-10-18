package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class AdjacencyList implements Graph {
    private List<List<Integer>> adjacencyList;
    private int numVertices;

    public AdjacencyList(int numVertices) {
        this.numVertices = numVertices;
        this.adjacencyList = new ArrayList<>(numVertices);
        for (int i = 0; i < numVertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    @Override
    public void addVertex(int vertex) {
        if (vertex >= numVertices) {
            for (int i = numVertices; i <= vertex; i++) {
                adjacencyList.add(new ArrayList<>());
            }
            this.numVertices = vertex + 1;
        }
    }

    @Override
    public void removeVertex(int vertex) {
        if (vertex < numVertices) {
            adjacencyList.remove(vertex);
            for (int i = 0; i < numVertices; i++) {
                adjacencyList.get(i).remove((Integer) vertex);
            }
            this.numVertices--;
        }
    }

    @Override
    public void addEdge(int source, int destination) {
        if (source < numVertices && destination < numVertices) {
            adjacencyList.get(source).add(destination);
        }
    }

    @Override
    public void removeEdge(int source, int destination) {
        if (source < numVertices && destination < numVertices) {
            adjacencyList.get(source).remove((Integer) destination);
        }
    }

    @Override
    public List<Integer> getNeighbors(int vertex) {
        if (vertex < numVertices) {
            return new ArrayList<>(adjacencyList.get(vertex));
        }
        return new ArrayList<>();
    }

    @Override
    public void readFromFile(String filename) {
        // Реализация чтения из файла
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof AdjacencyList) {
            AdjacencyList otherGraph = (AdjacencyList) other;
            if (numVertices != otherGraph.numVertices) {
                return false;
            }
            for (int i = 0; i < numVertices; i++) {
                if (!adjacencyList.get(i).equals(otherGraph.adjacencyList.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Adjacency List:\n");
        for (int i = 0; i < numVertices; i++) {
            sb.append(i).append(": ").append(adjacencyList.get(i)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public List<Integer> topologicalSort() {
        List<Integer> sortedVertices = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        // Определяем вершины с нулевой входящей степенью
        int[] inDegree = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            for (int neighbor : adjacencyList.get(i)) {
                inDegree[neighbor]++;
            }
        }

        for (int i = 0; i < numVertices; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            sortedVertices.add(vertex);
            visited.add(vertex);

            for (int neighbor : adjacencyList.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    inDegree[neighbor]--;
                    if (inDegree[neighbor] == 0) {
                        queue.add(neighbor);
                    }
                }
            }
        }

        // Если не все вершины были посещены, то граф содержит цикл
        if (sortedVertices.size() != numVertices) {
            return null;
        }

        return sortedVertices;
    }
}