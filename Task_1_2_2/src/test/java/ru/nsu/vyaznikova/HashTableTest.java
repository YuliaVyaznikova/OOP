package ru.nsu.vyaznikova;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Unit tests for the HashTable class to verify basic functionality of 
 * put, get, remove, update, containsKey, equals, and iterator methods.
 */
public class HashTableTest {

    private HashTable<String, Integer> hashTable;

    /**
     * Sets up a new HashTable instance before each test.
     */
    @BeforeEach
    void setUp() {
        hashTable = new HashTable<>();
    }

    /**
     * Tests adding and retrieving entries in the hash table using put and get methods.
     */
    @Test
    void testPutAndGet() {
        hashTable.put("one", 1);
        hashTable.put("two", 2);
        assertEquals(1, hashTable.get("one"));
        assertEquals(2, hashTable.get("two"));
    }

    /**
     * Tests if NoSuchElementException is thrown when trying to get a non-existing key.
     */
    @Test
    void testGetNonExistingKey() {
        assertThrows(NoSuchElementException.class, () -> hashTable.get("three"));
    }

    /**
     * Tests removing an entry by its key and verifying it's no longer in the table.
     */
    @Test
    void testRemove() {
        hashTable.put("one", 1);
        hashTable.remove("one");
        assertThrows(NoSuchElementException.class, () -> hashTable.get("one"));
    }

    /**
     * Tests if NoSuchElementException is thrown when trying to remove a non-existing key.
     */
    @Test
    void testRemoveNonExistingKey() {
        assertThrows(NoSuchElementException.class, () -> hashTable.remove("three"));
    }

    /**
     * Tests updating an existing entry's value and verifying the change.
     */
    @Test
    void testUpdate() {
        hashTable.put("one", 1);
        hashTable.update("one", 10);
        assertEquals(10, hashTable.get("one"));
    }

    /**
     * Tests if a new key-value pair is added when updating a non-existing key.
     */
    @Test
    void testUpdateNonExistingKey() {
        hashTable.update("three", 3);
        assertEquals(3, hashTable.get("three"));
    }

    /**
     * Tests if containsKey correctly identifies present and absent keys.
     */
    @Test
    void testContainsKey() {
        hashTable.put("one", 1);
        assertTrue(hashTable.containsKey("one"));
        assertFalse(hashTable.containsKey("two"));
    }

    /**
     * Tests if two HashTable instances are equal based on their content.
     */
    @Test
    void testEquals() {
        HashTable<String, Integer> otherHashTable = new HashTable<>();
        hashTable.put("one", 1);
        hashTable.put("two", 2);
        otherHashTable.put("one", 1);
        otherHashTable.put("two", 2);
        assertEquals(hashTable, otherHashTable);

        otherHashTable.put("three", 3);
        assertNotEquals(hashTable, otherHashTable);
    }

    /**
     * Tests iterator functionality by verifying it correctly iterates through entries
     * and throws ConcurrentModificationException on modification during iteration.
     */
    @Test
    void testIteratorFunctionality() {
        hashTable.put("one", 1);
        hashTable.put("two", 2);

        int count = 0;
        for (HashTable.Entry<String, Integer> entry : hashTable) {
            assertTrue(entry.getKey().equals("one") || entry.getKey().equals("two"));
            count++;
        }
        assertEquals(2, count);
    }

    /**
     * Tests ConcurrentModificationException when modifying the table during iteration.
     */
    @Test
    void testConcurrentModificationException() {
        hashTable.put("one", 1);
        hashTable.put("two", 2);

        Iterator<HashTable.Entry<String, Integer>> iterator = hashTable.iterator();
        hashTable.put("three", 3);

        assertThrows(ConcurrentModificationException.class, iterator::next);
    }

    /**
     * Tests NoSuchElementException when calling next on an exhausted iterator.
     */
    @Test
    void testIteratorNoSuchElementException() {
        Iterator<HashTable.Entry<String, Integer>> iterator = hashTable.iterator();
        assertThrows(NoSuchElementException.class, iterator::next);
    }
}