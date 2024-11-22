package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link AdjacencyList} class.
 */
class AdjacencyListTest {

    /**
     * Tests the {@link AdjacencyList#addVertex(Object)} method.
     * Verifies that a new vertex is added to the graph and the number of vertices is incremented.
     */
    @Test
    void addVertex() {
        AdjacencyList<String> graph = new AdjacencyList<>();
        graph.addVertex("A");
        assertEquals(1, graph.numVertices);
        assertTrue(graph.vertices.contains("A"));
    }

    /**
     * Tests the {@link AdjacencyList#removeVertex(Object)} method.
     * Verifies that a vertex is removed from the graph and the number of vertices is decremented.
     */
    @Test
    void removeVertex() {
        AdjacencyList<String> graph = new AdjacencyList<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.removeVertex("A");
        assertEquals(1, graph.numVertices);
        assertFalse(graph.vertices.contains("A"));
    }

    /**
     * Tests the {@link AdjacencyList#addEdge(Object, Object)} method.
     * Verifies that an edge is added between
     * two vertices and the neighbors list is updated correctly.
     */
    @Test
    void addEdge() {
        AdjacencyList<String> graph = new AdjacencyList<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");
        assertTrue(graph.getNeighbors("A").contains("B"));
    }

    /**
     * Tests the {@link AdjacencyList#removeEdge(Object, Object)} method.
     * Verifies that an edge is removed between
     * two vertices and the neighbors list is updated correctly.
     */
    @Test
    void removeEdge() {
        AdjacencyList<String> graph = new AdjacencyList<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");
        graph.removeEdge("A", "B");
        assertFalse(graph.getNeighbors("A").contains("B"));
    }

    /**
     * Tests the {@link AdjacencyList#getNeighbors(Object)} method.
     * Verifies that the correct list of neighbors is returned for a given vertex.
     */
    @Test
    void getNeighbors() {
        AdjacencyList<String> graph = new AdjacencyList<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        List<String> expectedNeighbors = Arrays.asList("B", "C");
        assertEquals(expectedNeighbors, graph.getNeighbors("A"));
    }

    /**
     * Tests the {@link AdjacencyList#topologicalSort()} method.
     * Verifies that the graph is topologically sorted correctly.
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