package ru.nsu.vyaznikova;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HashTable<K, V> implements Iterable<HashTable.Entry<K, V>> {

    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    int capacity;
    int size;
    List<Entry<K, V>>[] table;
    private double loadFactor;

    public HashTable() {
        this(16, DEFAULT_LOAD_FACTOR);
    }

    public HashTable(int capacity, double loadFactor) {
        this.capacity = capacity;
        this.size = 0;
        this.table = new List[capacity];
        this.loadFactor = loadFactor;
    }

    public void put(K key, V value) {
        int index = hash(key) % capacity;
        if (table[index] == null) {
            table[index] = new ArrayList<>();
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
        return null;
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
        List<Entry<K, V>>[] newTable = new List[capacity];
        for (List<Entry<K, V>> list : table) {
            if (list != null) {
                for (Entry<K, V> entry : list) {
                    int index = hash(entry.key) % capacity;
                    if (newTable[index] == null) {
                        newTable[index] = new ArrayList<>();
                    }
                    newTable[index].add(entry);
                }
            }
        }
        table = newTable;
    }

    private int hash(K key) {
        return key.hashCode();
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
}