package ru.nsu.vyaznikova;

import java.util.*;

/**
 * A generic HashTable implementation supporting basic operations like
 * adding, retrieving, updating, and removing key-value pairs. The table
 * uses an array of ArrayLists to handle collisions and automatically resizes
 * when the load factor threshold is exceeded.
 *
 * @param <K> The type of keys maintained by this table.
 * @param <V> The type of values associated with keys.
 */
public class HashTable<K, V> implements Iterable<HashTable.Entry<K, V>> {

    private static final double DEFAULT_LOAD_FACTOR = 0.75;
    private int size;
    private int amountOfEntries;
    private ArrayList<Entry<K, V>>[] table;
    private double loadFactor;

    private static final int FNV_prime = 16777619;
    private static final int FNV_offset_basis = 216613661;

    /**
     * Creates an empty HashTable with default initial size and load factor.
     */
    public HashTable() {
        this(16, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Creates an empty HashTable with specified initial size and load factor.
     *
     * @param size       Initial capacity of the hash table.
     * @param loadFactor Load factor threshold for resizing.
     */
    public HashTable(int size, double loadFactor) {
        this.size = size;
        this.amountOfEntries = 0;
        this.table = new ArrayList[size];
        this.loadFactor = loadFactor;
    }

    /**
     * Adds a key-value pair to the table. If the key already exists, the
     * value is updated.
     *
     * @param key   The key to add.
     * @param value The value associated with the key.
     */
    public void put(K key, V value) {
        int index = hash(key) % size;
        if (table[index] == null) {
            table[index] = new ArrayList<>();
        }
        table[index].add(new Entry<>(key, value));
        amountOfEntries++;
        if (amountOfEntries > size * loadFactor) {
            resize();
        }
    }

    /**
     * Retrieves the value associated with the specified key.
     *
     * @param key The key to search for.
     * @return The value associated with the key.
     * @throws NoSuchElementException if the key is not found.
     */
    public V get(K key) {
        int index = hash(key) % size;
        if (table[index] != null) {
            for (Entry<K, V> entry : table[index]) {
                if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
        }
        throw new NoSuchElementException("Key not found: " + key);
    }

    /**
     * Removes a key-value pair from the table.
     *
     * @param key The key to remove.
     * @throws NoSuchElementException if the key is not found.
     */
    public void remove(K key) {
        int index = hash(key) % size;
        if (table[index] != null) {
            for (int i = 0; i < table[index].size(); i++) {
                if (table[index].get(i).key.equals(key)) {
                    table[index].remove(i);
                    amountOfEntries--;
                    return;
                }
            }
        }
        throw new NoSuchElementException("Key not found: " + key);
    }

    /**
     * Updates the value associated with a specific key.
     *
     * @param key   The key to update.
     * @param value The new value.
     */
    public void update(K key, V value) {
        int index = hash(key) % size;
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

    /**
     * Checks if the table contains a specific key.
     *
     * @param key The key to check.
     * @return True if the key exists, false otherwise.
     */
    public boolean containsKey(K key) {
        try {
            get(key);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns an iterator over the entries in the table, handling
     * concurrent modification exceptions.
     *
     * @return An iterator for the HashTable.
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashTableIterator<>(this);
    }

    /**
     * Compares the current table to another table for equality. Both
     * tables are considered equal if they contain the same key-value pairs,
     * irrespective of order within the buckets.
     *
     * @param obj The object to compare.
     * @return True if the tables are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HashTable)) {
            return false;
        }
        HashTable<K, V> other = (HashTable<K, V>) obj;

        if (this.amountOfEntries != other.amountOfEntries) {
            return false;
        }

        for (int i = 0; i < this.size; i++) {
            List<Entry<K, V>> thisBucket = this.table[i];
            List<Entry<K, V>> otherBucket = other.table[i];

            if (thisBucket != null) {
                if (otherBucket == null || new HashSet<>(thisBucket).size() != new HashSet<>(otherBucket).size()) {
                    return false;
                }
                if (!new HashSet<>(thisBucket).equals(new HashSet<>(otherBucket))) {
                    return false;
                }
            } else if (otherBucket != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a string representation of the table.
     *
     * @return String representation of all entries.
     */
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

    /**
     * Resizes the table when the load factor is exceeded.
     */
    private void resize() {
        size *= 2;
        ArrayList<Entry<K, V>>[] newTable = new ArrayList[size];
        for (List<Entry<K, V>> list : table) {
            if (list != null) {
                for (Entry<K, V> entry : list) {
                    int index = hash(entry.key) % size;
                    if (newTable[index] == null) {
                        newTable[index] = new ArrayList<>();
                    }
                    newTable[index].add(entry);
                }
            }
        }
        table = newTable;
    }

    /**
     * Hashes the key using FNV-1a hashing algorithm.
     *
     * @param key The key to hash.
     * @return The hash code for the key.
     */
    private int hash(K key) {
        byte[] bytes = key.toString().getBytes();
        int hash = FNV_offset_basis;
        for (byte b : bytes) {
            hash = (hash * FNV_prime) ^ b;
        }
        return hash;
    }

    /**
     * Represents a key-value pair entry in the hash table.
     *
     * @param <K> The type of the key.
     * @param <V> The type of the value.
     */
    public static class Entry<K, V> {
        private final K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    /**
     * An iterator for the HashTable that handles concurrent modification.
     *
     * @param <K> The type of keys in the table.
     * @param <V> The type of values in the table.
     */
    private static class HashTableIterator<K, V> implements Iterator<HashTable.Entry<K, V>> {
        private final HashTable<K, V> table;
        private int currentIndex;
        private int currentEntryIndex;
        private int expectedAmountOfEntries;

        public HashTableIterator(HashTable<K, V> table) {
            this.table = table;
            this.currentIndex = 0;
            this.currentEntryIndex = -1;
            this.expectedAmountOfEntries = table.amountOfEntries;
        }

        @Override
        public boolean hasNext() {
            if (expectedAmountOfEntries != table.amountOfEntries) {
                throw new ConcurrentModificationException("HashTable was modified during iteration.");
            }
            while (currentIndex < table.size) {
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
        public HashTable.Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            currentEntryIndex++;
            return table.table[currentIndex].get(currentEntryIndex);
        }
    }
}