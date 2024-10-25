package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link AdjacencyList} class.
 */
class AdjacencyListTest {

    /**
     * Tests the {@link AdjacencyList#topologicalSort()} method.
     * Verifies that the topologicalSort method returns
     * the correct topological order of the vertices.
     */
    @Test
    void topologicalSort() {
        AdjacencyList<String> graph = new AdjacencyList<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addVertex("D");
        graph.addVertex("E");
        graph.addVertex("F");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "D");
        graph.addEdge("C", "E");
        graph.addEdge("D", "F");

        List<String> expectedOrder = Arrays.asList("A", "C", "E", "B", "D", "F");
        assertEquals(expectedOrder, graph.topologicalSort());
    }
}