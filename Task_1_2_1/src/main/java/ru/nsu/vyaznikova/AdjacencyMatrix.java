package ru.nsu.vyaznikova;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class AdjacencyMatrix<V> {

    private List<List<Integer>> matrix;
    private Map<V, Integer> vertexMap;
    private int numVertices;

    public AdjacencyMatrix() {
        this.numVertices = 0;
        this.matrix = new ArrayList<>();
        this.vertexMap = new HashMap<>();
    }

    // Метод для добавления вершины в граф
    public void addVertex(V vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex cannot be null");
        }

        if (!vertexMap.containsKey(vertex)) {
            vertexMap.put(vertex, numVertices);
            int newSize = numVertices + 1;
            for (List<Integer> row : matrix) {
                row.add(0);
            }
            List<Integer> newRow = new ArrayList<>(Collections.nCopies(newSize, 0));
            matrix.add(newRow);
            numVertices = newSize;
        } else {
            throw new IllegalArgumentException("Vertex already exists");
        }
    }

    // Метод для удаления вершины из графа
    public void removeVertex(V vertex) {
        if (!vertexMap.containsKey(vertex)) {
            throw new IllegalArgumentException("Vertex does not exist");
        }

        int vertexIndex = vertexMap.get(vertex);
        matrix.remove(vertexIndex); // Удаляем строку
        for (List<Integer> row : matrix) {
            row.remove(vertexIndex); // Удаляем столбец
        }
        vertexMap.remove(vertex);
        numVertices--;

        // Обновляем индексы в vertexMap после удаления
        for (Map.Entry<V, Integer> entry : vertexMap.entrySet()) {
            if (entry.getValue() > vertexIndex) {
                entry.setValue(entry.getValue() - 1);
            }
        }
    }

    // Метод для добавления ребра
    public void addEdge(V source, V destination) {
        if (!vertexMap.containsKey(source) || !vertexMap.containsKey(destination)) {
            throw new IllegalArgumentException("One or both vertices do not exist");
        }
        int sourceIndex = vertexMap.get(source);
        int destinationIndex = vertexMap.get(destination);
        matrix.get(sourceIndex).set(destinationIndex, 1); // Устанавливаем значение 1 в матрице
    }

    // Метод для удаления ребра
    public void removeEdge(V source, V destination) {
        if (!vertexMap.containsKey(source) || !vertexMap.containsKey(destination)) {
            throw new IllegalArgumentException("One or both vertices do not exist");
        }
        int sourceIndex = vertexMap.get(source);
        int destinationIndex = vertexMap.get(destination);
        matrix.get(sourceIndex).set(destinationIndex, 0); // Устанавливаем значение 0 в матрице
    }

    // Метод для получения списка соседей вершины
    public List<V> getNeighbors(V vertex) {
        List<V> neighbors = new ArrayList<>();
        if (!vertexMap.containsKey(vertex)) {
            return neighbors; // Или выбросить исключение
        }

        int vertexIndex = vertexMap.get(vertex);
        for (int i = 0; i < numVertices; i++) {
            if (matrix.get(vertexIndex).get(i) == 1) {
                for (Map.Entry<V, Integer> entry : vertexMap.entrySet()) {
                    if (entry.getValue() == i) {
                        neighbors.add(entry.getKey());
                        break;
                    }
                }
            }
        }
        return neighbors;
    }

    // Метод для чтения из файла (реализован с учетом формата файла)
//    Предположим, что файл содержит следующие данные:
//
//    Первая строка: количество вершин N.
//    Следующие N строк: имена вершин (например, строки).
//    Остальные строки: ребра, где каждая строка содержит пару вершин, разделенных пробелом (например, A B).
//    Пример файла:
//
//            4
//    A
//            B
//    C
//            D
//    A B
//    B C
//    C D
    public void readFromFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // 1. Чтение количества вершин
            int numVertices = Integer.parseInt(reader.readLine());
            this.numVertices = numVertices;
            this.matrix = new ArrayList<>(Collections.nCopies(numVertices, new ArrayList<>(Collections.nCopies(numVertices, 0))));
            this.vertexMap = new HashMap<>();

            // 2. Чтение имен вершин
            for (int i = 0; i < numVertices; i++) {
                vertexMap.put((V) reader.readLine(), i);
            }

            // 3. Чтение ребер
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                V source = (V) parts[0];
                V destination = (V) parts[1];
                addEdge(source, destination);
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    // Метод для сравнения графов
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AdjacencyMatrix<V> other = (AdjacencyMatrix<V>) obj;
        if (numVertices != other.numVertices) {
            return false;
        }
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (matrix.get(i).get(j) != other.matrix.get(i).get(j)) {
                    return false;
                }
            }
        }
        return true;
    }

    // Метод для вычисления хэшкода
    @Override
    public int hashCode() {
        int result = 17;
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                result = 31 * result + matrix.get(i).get(j);
            }
        }
        return result;
    }

    // Метод для получения строкового представления графа
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Adjacency Matrix:\n");
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                sb.append(matrix.get(i).get(j)).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // Метод для топологической сортировки (реализован алгоритмом DFS)
    public List<V> topologicalSort() {
        List<V> sorted = new ArrayList<>();
        Map<V, Integer> inDegree = new HashMap<>();
        Map<V, Boolean> visited = new HashMap<>();

        // Инициализация входящих степеней вершин
        for (Map.Entry<V, Integer> entry : vertexMap.entrySet()) {
            inDegree.put(entry.getKey(), 0);
            visited.put(entry.getKey(), false);
        }

        // Подсчет входящих степеней для каждой вершины
        for (Map.Entry<V, Integer> sourceEntry : vertexMap.entrySet()) {
            int sourceIndex = sourceEntry.getValue();
            for (int i = 0; i < numVertices; i++) {
                if (matrix.get(sourceIndex).get(i) == 1) {
                    inDegree.put((V) vertexMap.get(i), inDegree.get(vertexMap.get(i)) + 1);
                }
            }
        }


        // Добавление вершин с нулевой входящей степенью в стек
        Stack<V> stack = new Stack<>();
        for (Map.Entry<V, Integer> entry : vertexMap.entrySet()) {
            if (inDegree.get(entry.getKey()) == 0) {
                stack.push(entry.getKey());
            }
        }

        while (!stack.isEmpty()) {
            V vertex = stack.pop();
            if (!visited.get(vertex)) {
                visited.put(vertex, true);
                sorted.add(vertex);

                int vertexIndex = vertexMap.get(vertex);
                for (int i = 0; i < numVertices; i++) {
                    if (matrix.get(vertexIndex).get(i) == 1) {
                        V neighbor = (V) vertexMap.get(i);
                        inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                        if (inDegree.get(neighbor) == 0) {
                            stack.push(neighbor);
                        }
                    }
                }
            }
        }

        // Проверка на циклы
        if (sorted.size() != numVertices) {
            throw new IllegalArgumentException("Graph contains cycles.");
        }

        return sorted;
    }
}