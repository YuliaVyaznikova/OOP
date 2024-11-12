package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

/**
 * Test class for the Main class, ensuring its methods work as expected.
 */
public class MainTest {
    @Test
    void testDoAll() {
        BotReader.autotestEnable(15, 5);
        Main.doAll();
    }
}