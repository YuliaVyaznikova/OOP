package ru.nsu.vyaznikova;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        AdjacencyList graph = new AdjacencyList(5);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);

        System.out.println(graph);

        List<Integer> sorted = graph.topologicalSort();
        System.out.println("Топологическая сортировка: " + sorted);
    }
}