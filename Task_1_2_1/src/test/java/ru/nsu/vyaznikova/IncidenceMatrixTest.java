package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link IncidenceMatrix} class.
 */
class IncidenceMatrixTest {

    /**
     * Tests the {@link IncidenceMatrix#topologicalSort()} method.
     * Verifies that the topologicalSort method returns the correct topological order of the vertices.
     */
    @Test
    void topologicalSort() {
        IncidenceMatrix<String> graph = new IncidenceMatrix<>();
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