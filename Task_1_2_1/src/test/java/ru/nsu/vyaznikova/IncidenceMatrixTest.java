package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link IncidenceMatrix} class.
 */
class IncidenceMatrixTest {

    /**
     * Tests the {@link IncidenceMatrix#addVertex(Object)} method.
     * Verifies that a new vertex is added to the graph and the number of vertices is incremented.
     */
    @Test
    void addVertex() {
        IncidenceMatrix<String> graph = new IncidenceMatrix<>();
        graph.addVertex("A");
        assertEquals(1, graph.numVertices);
        assertTrue(graph.vertices.contains("A"));
    }

    /**
     * Tests the {@link IncidenceMatrix#removeVertex(Object)} method.
     * Verifies that a vertex is removed from the graph and the number of vertices is decremented.
     */
    @Test
    void removeVertex() {
        IncidenceMatrix<String> graph = new IncidenceMatrix<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.removeVertex("A");
        assertEquals(1, graph.numVertices);
        assertFalse(graph.vertices.contains("A"));
    }

    /**
     * Tests the {@link IncidenceMatrix#addEdge(Object, Object)} method.
     * Verifies that an edge is added between
     * two vertices and the incidence matrix is updated correctly.
     */
    @Test
    void addEdge() {
        IncidenceMatrix<String> graph = new IncidenceMatrix<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B");
        assertEquals(1, graph.matrix.get(0).get(0));
        assertEquals(-1, graph.matrix.get(0).get(1));
    }

    /**
     * Tests the {@link IncidenceMatrix#getNeighbors(Object)} method.
     * Verifies that the correct list of neighbors is returned for a given vertex.
     */
    @Test
    void getNeighbors() {
        IncidenceMatrix<String> graph = new IncidenceMatrix<>();
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        List<String> expectedNeighbors = Arrays.asList("B", "C");
        assertEquals(expectedNeighbors, graph.getNeighbors("A"));
    }

    /**
     * Tests the {@link IncidenceMatrix#topologicalSort()} method.
     * Verifies that the graph is topologically sorted correctly.
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