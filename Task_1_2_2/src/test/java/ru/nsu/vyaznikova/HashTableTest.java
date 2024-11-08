package ru.nsu.vyaznikova;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the HashTable class.
 */
public class HashTableTest {

    private HashTable<String, String> hashTable;

    @BeforeEach
    public void setUp() {
        hashTable = new HashTable<>(10, 0.75);  // Assuming the table size and load factor are provided
    }

    /**
     * Test putting and getting values.
     */
    @Test
    public void testPutAndGet() {
        hashTable.put("key1", "value1");
        assertEquals("value1", hashTable.get("key1"));
    }

    /**
     * Test retrieving a value for a non-existent key.
     */
    @Test
    public void testGetNonExistentKey() {
        assertThrows(NoSuchElementException.class, () -> hashTable.get("nonexistent"));
    }

    /**
     * Test adding a new key-value pair when the key does not exist.
     */
    @Test
    public void testPutNewKey() {
        hashTable.put("key2", "value2");
        assertEquals("value2", hashTable.get("key2"));
    }

    /**
     * Test removing an existing key.
     */
    @Test
    public void testRemoveKey() {
        hashTable.put("key1", "value1");
        hashTable.remove("key1");
        assertThrows(NoSuchElementException.class, () -> hashTable.get("key1"));
    }

    /**
     * Test removing a non-existent key.
     */
    @Test
    public void testRemoveNonExistentKey() {
        assertThrows(NoSuchElementException.class, () -> hashTable.remove("nonexistent"));
    }

    /**
     * Test checking if a key exists.
     */
    @Test
    public void testContainsKey() {
        hashTable.put("key1", "value1");
        assertTrue(hashTable.containsKey("key1"));
        assertFalse(hashTable.containsKey("nonexistent"));
    }

    /**
     * Test the size after adding and removing entries.
     */
    @Test
    public void testSizeAfterOperations() {
        hashTable.put("key1", "value1");
        assertEquals(1, hashTable.getAmountOfEntries());
        hashTable.put("key2", "value2");
        assertEquals(2, hashTable.getAmountOfEntries());
        hashTable.remove("key1");
        assertEquals(1, hashTable.getAmountOfEntries());
    }

    /**
     * Test that an exception is thrown if modification occurs during iteration.
     */
    @Test
    public void testConcurrentModification() {
        hashTable.put("key1", "value1");
        hashTable.put("key2", "value2");
        Iterator<HashTable.Entry<String, String>> iterator = hashTable.iterator();
        hashTable.put("key3", "value3");

        assertThrows(ConcurrentModificationException.class, iterator::next);
    }

    /**
     * Test iterating through the table.
     */
    @Test
    public void testIterator() {
        hashTable.put("key1", "value1");
        hashTable.put("key2", "value2");
        StringBuilder result = new StringBuilder();
        for (HashTable.Entry<String, String> entry : hashTable) {
            result.append(entry.getKey()).append("=").append(entry.getValue()).append(" ");
        }
        assertTrue(result.toString().contains("key1=value1"));
        assertTrue(result.toString().contains("key2=value2"));
    }

    /**
     * Test equality between two HashTables.
     */
    @Test
    public void testEquality() {
        HashTable<String, String> anotherTable = new HashTable<>(10, 0.75);
        hashTable.put("key1", "value1");
        hashTable.put("key2", "value2");

        anotherTable.put("key1", "value1");
        anotherTable.put("key2", "value2");

        assertEquals(hashTable, anotherTable);
    }

    /**
     * Test equality with hash tables of different sizes but same content.
     */
    @Test
    public void testEqualityDifferentSizes() {
        HashTable<String, String> largerTable = new HashTable<>(20, 0.75);
        hashTable.put("key1", "value1");
        hashTable.put("key2", "value2");

        largerTable.put("key1", "value1");
        largerTable.put("key2", "value2");

        assertEquals(hashTable, largerTable);
    }

    /**
     * Test toString method output format.
     */
    @Test
    public void testToString() {
        hashTable.put("key1", "value1");
        hashTable.put("key2", "value2");

        String result = hashTable.toString();
        assertTrue(result.contains("key1=value1"));
        assertTrue(result.contains("key2=value2"));
    }

    /**
     * Test that an exception is thrown if a null key is used.
     */
    @Test
    public void testNullKey() {
        assertThrows(IllegalArgumentException.class, () -> hashTable.put(null, "value"));
        assertThrows(IllegalArgumentException.class, () -> hashTable.get(null));
        assertThrows(IllegalArgumentException.class, () -> hashTable.remove(null));
    }
}