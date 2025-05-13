package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

@Timeout(value = 1, unit = TimeUnit.SECONDS)
class MainTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeAll
    static void setUpClass() {
        Main.setTestMode(true);
    }

    @BeforeEach
    void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testMainWithNoArgs() {
        assertThrows(RuntimeException.class, () -> Main.main(new String[]{}));
        assertTrue(errContent.toString().contains("Usage:"));
    }

    @Test
    void testMainWithInvalidNodeType() {
        assertThrows(RuntimeException.class, () -> 
            Main.main(new String[]{"invalid", "1", "2", "3"}));
        assertTrue(errContent.toString().contains("Unknown node type"));
    }

    @Test
    void testMainWithWorkerNoId() {
        assertThrows(RuntimeException.class, () -> 
            Main.main(new String[]{"worker"}));
        assertTrue(errContent.toString().contains("Worker ID is required"));
    }

    @Test
    void testParseNumbersWithNegatives() {
        String[] args = {"master", "-1", "-2", "3"};
        int[] numbers = Main.parseNumbers(args);
        assertArrayEquals(new int[]{-1, -2, 3}, numbers);
    }

    @Test
    void testParseNumbersWithZero() {
        String[] args = {"master", "0", "1", "0"};
        int[] numbers = Main.parseNumbers(args);
        assertArrayEquals(new int[]{0, 1, 0}, numbers);
    }

    @Test
    void testParseNumbersEmpty() {
        String[] args = {"master"};
        assertThrows(IllegalArgumentException.class, () -> Main.parseNumbers(args));
    }

    @Test
    void testParseNumbersInvalid() {
        String[] args = {"master", "1", "abc", "3"};
        assertThrows(NumberFormatException.class, () -> Main.parseNumbers(args));
    }

    @Test
    void testArrayToStringEmpty() {
        assertEquals("[]", Main.arrayToString(new int[]{}));
    }

    @Test
    void testArrayToStringSingle() {
        assertEquals("[42]", Main.arrayToString(new int[]{42}));
    }

    @Test
    void testArrayToStringMultiple() {
        assertEquals("[1, 2, 3]", Main.arrayToString(new int[]{1, 2, 3}));
    }

    @Test
    void testArrayToStringWithNegatives() {
        assertEquals("[-1, 0, 1]", Main.arrayToString(new int[]{-1, 0, 1}));
    }

    @Test
    void testMasterWithDefaultNumbers() throws Exception {
        Thread masterThread = new Thread(() -> {
            try {
                Main.main(new String[]{"master"});
            } catch (Exception e) {
                // Expected in test mode
            }
        });
        masterThread.start();
        Thread.sleep(100); // Give time for master to start
        assertTrue(outContent.toString().contains("Starting master node"));
        assertTrue(outContent.toString().contains("Input array: [6, 8, 7, 13, 5, 9, 4]"));
    }

    @Test
    void testMasterWithCustomNumbers() throws Exception {
        Thread masterThread = new Thread(() -> {
            try {
                Main.main(new String[]{"master", "2", "3", "5"});
            } catch (Exception e) {
                // Expected in test mode
            }
        });
        masterThread.start();
        Thread.sleep(100); // Give time for master to start
        assertTrue(outContent.toString().contains("Starting master node"));
        assertTrue(outContent.toString().contains("Input array: [2, 3, 5]"));
    }

    @Test
    void testWorkerWithValidId() throws Exception {
        Thread workerThread = new Thread(() -> {
            try {
                Main.main(new String[]{"worker", "test-worker"});
            } catch (Exception e) {
                // Expected in test mode
            }
        });
        workerThread.start();
        Thread.sleep(100); // Give time for worker to start
        assertTrue(outContent.toString().contains("Starting worker node: test-worker"));
    }

    @Test
    void testArrayToStringLarge() {
        int[] large = new int[1000];
        for (int i = 0; i < large.length; i++) {
            large[i] = i;
        }
        String result = Main.arrayToString(large);
        assertTrue(result.startsWith("["));
        assertTrue(result.endsWith("]"));
        assertTrue(result.contains("0, 1, 2"));
        assertTrue(result.contains("998, 999"));
    }

    @Test
    void testParseNumbersLargeNumbers() {
        String[] args = {"master", "2147483647", "-2147483648"};
        int[] numbers = Main.parseNumbers(args);
        assertArrayEquals(new int[]{2147483647, -2147483648}, numbers);
    }
}
