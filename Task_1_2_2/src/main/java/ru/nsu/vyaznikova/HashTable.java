package ru.nsu.vyaznikova;

import java.util.*;

public class HashTable<K, V> implements Iterable<HashTable.Entry<K, V>> {

    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int capacity;
    private int size;
    private LinkedList<Entry<K, V>>[] table; // Изменили на LinkedList
    private double loadFactor;

    private static final int FNV_prime = 16777619;
    private static final int FNV_offset_basis = 2166136261;

    public HashTable() {
        this(16, DEFAULT_LOAD_FACTOR);
    }

    public HashTable(int capacity, double loadFactor) {
        this.capacity = capacity;
        this.size = 0;
        this.table = new LinkedList[capacity];
        this.loadFactor = loadFactor;
    }

    public void put(K key, V value) {
        int index = hash(key) % capacity;
        if (table[index] == null) {
            table[index] = new LinkedList<>();
        }
        table[index].add(new Entry<>(key, value));
        size++;
        if (size > capacity * loadFactor) {
            resize();
        }
    }

    public V get(K key) {
        int index = hash(key) % capacity;
        if (table[index] != null) {
            for (Entry<K, V> entry : table[index]) {
                if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
        }
        throw new NoSuchElementException("Key not found: " + key);
    }

    public void remove(K key) {
        int index = hash(key) % capacity;
        if (table[index] != null) {
            for (int i = 0; i < table[index].size(); i++) {
                if (table[index].get(i).key.equals(key)) {
                    table[index].remove(i);
                    size--;
                    return;
                }
            }
        }
    }

    public void update(K key, V value) {
        int index = hash(key) % capacity;
        if (table[index] != null) {
            for (Entry<K, V> entry : table[index]) {
                if (entry.key.equals(key)) {
                    entry.value = value;
                    return;
                }
            }
        }
        put(key, value);
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashTableIterator<>(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HashTable)) {
            return false;
        }
        HashTable<K, Object> other = (HashTable<K, Object>) obj;
        if (size != other.size) {
            return false;
        }
        for (Entry<K, V> entry : this) {
            if (!other.containsKey(entry.key) || !other.get(entry.key).equals(entry.value)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        Iterator<Entry<K, V>> iterator = iterator();
        if (iterator.hasNext()) {
            sb.append(iterator.next());
            while (iterator.hasNext()) {
                sb.append(", ").append(iterator.next());
            }
        }
        sb.append("}");
        return sb.toString();
    }

    private void resize() {
        capacity *= 2;
        LinkedList<Entry<K, V>>[] newTable = new LinkedList[capacity];
        for (List<Entry<K, V>> list : table) {
            if (list != null) {
                for (Entry<K, V> entry : list) {
                    int index = hash(entry.key) % capacity;
                    if (newTable[index] == null) {
                        newTable[index] = new LinkedList<>();
                    }
                    newTable[index].add(entry);
                }
            }
        }
        table = newTable;
    }

    private int hash(K key) {
        byte[] bytes = key.toString().getBytes();
        int hash = FNV_offset_basis;
        for (byte b : bytes) {
            hash = (hash * FNV_prime) ^ b;
        }
        return hash;
    }

    public static class Entry<K, V> {
        private final K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private static class HashTableIterator<K, V> implements Iterator<HashTable.Entry<K, V>> {
        private final HashTable<K, V> table;
        private int currentIndex;
        private int currentEntryIndex;
        private int expectedSize; // Хранит ожидаемое количество элементов в таблице

        public HashTableIterator(HashTable<K, V> table) {
            this.table = table;
            this.currentIndex = 0;
            this.currentEntryIndex = -1;
            this.expectedSize = table.size;
        }

        @Override
        public boolean hasNext() {
            if (expectedSize != table.size) { // Проверка изменений таблицы
                throw new ConcurrentModificationException("HashTable was modified during iteration.");
            }
            while (currentIndex < table.capacity) {
                if (table.table[currentIndex] != null) {
                    if (currentEntryIndex < table.table[currentIndex].size() - 1) {
                        return true;
                    } else {
                        currentEntryIndex = -1;
                        currentIndex++;
                    }
                } else {
                    currentIndex++;
                }
            }
            return false;
        }

        @Override
        public Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            currentEntryIndex++;
            return table.table[currentIndex].get(currentEntryIndex);
        }
    }
}