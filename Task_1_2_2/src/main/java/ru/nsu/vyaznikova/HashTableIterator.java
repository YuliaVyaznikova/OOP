package ru.nsu.vyaznikova;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashTableIterator<K, V> implements Iterator<HashTable.Entry<K, V>> {
    private final HashTable<K, V> table;
    private int currentIndex;
    private int currentEntryIndex;
    private int expectedSize;

    public HashTableIterator(HashTable<K, V> table) {
        this.table = table;
        this.currentIndex = 0;
        this.currentEntryIndex = -1;
        this.expectedSize = table.size;
    }

    @Override
    public boolean hasNext() {
        if (expectedSize != table.size) {
            throw new ConcurrentModificationException();
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
    public HashTable.Entry<K, V> next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        currentEntryIndex++;
        return table.table[currentIndex].get(currentEntryIndex);
    }
}