package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.List;

public class IncidenceMatrix implements Graph {
    private int[][] matrix;
    private int numVertices;
    private int numEdges;

    public IncidenceMatrix(int numVertices, int numEdges) {
        this.numVertices = numVertices;
        this.numEdges = numEdges;
        this.matrix = new int[numVertices][numEdges];
    }

    @Override
    public void addVertex(int vertex) {
        if (vertex >= numVertices) {
            int[][] newMatrix = new int[vertex + 1][numEdges];
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numEdges; j++) {
                    newMatrix[i][j] = matrix[i][j];
                }
            }
            this.matrix = newMatrix;
            this.numVertices = vertex + 1;
        }
    }

    @Override
    public void removeVertex(int vertex) {
        if (vertex < numVertices) {
            for (int i = 0; i < numEdges; i++) {
                matrix[vertex][i] = 0;
            }
            this.numVertices--;
            int[][] newMatrix = new int[numVertices][numEdges];
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numEdges; j++) {
                    if (i < vertex) {
                        newMatrix[i][j] = matrix[i][j];
                    } else {
                        newMatrix[i][j] = matrix[i + 1][j];
                    }
                }
            }
            this.matrix = newMatrix;
        }
    }

    @Override
    public void addEdge(int source, int destination) {
        if (source < numVertices && destination < numVertices) {
            for (int i = 0; i < numEdges; i++) {
                if (matrix[source][i] == 0 && matrix[destination][i] == 0) {
                    matrix[source][i] = 1;
                    matrix[destination][i] = 1;
                    break;
                }
            }
        }
    }

    @Override
    public void removeEdge(int source, int destination) {
        if (source < numVertices && destination < numVertices) {
            for (int i = 0; i < numEdges; i++) {
                if (matrix[source][i] == 1 && matrix[destination][i] == 1) {
                    matrix[source][i] = 0;
                    matrix[destination][i] = 0;
                    break;
                }
            }
        }
    }

    @Override
    public List<Integer> getNeighbors(int vertex) {
        List<Integer> neighbors = new ArrayList<>();
        if (vertex < numVertices) {
            for (int i = 0; i < numEdges; i++) {
                if (matrix[vertex][i] == 1) {
                    for (int j = 0; j < numVertices; j++) {
                        if (j != vertex && matrix[j][i] == 1) {
                            neighbors.add(j);
                            break;
                        }
                    }
                }
            }
        }
        return neighbors;
    }

    @Override
    public void readFromFile(String filename) {
        // Реализация чтения из файла
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof IncidenceMatrix) {
            IncidenceMatrix otherGraph = (IncidenceMatrix) other;
            if (numVertices != otherGraph.numVertices || numEdges != otherGraph.numEdges) {
                return false;
            }
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numEdges; j++) {
                    if (matrix[i][j] != otherGraph.matrix[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Incidence Matrix:\n");
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numEdges; j++) {
                sb.append(matrix[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public List<Integer> topologicalSort() {
        // Реализация алгоритма топологической сортировки для матрицы инцидентности
        return new ArrayList<>();
    }
}