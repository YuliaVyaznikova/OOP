package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Test class for the BotReader class, ensuring its methods work as expected.
 */
public class BotReaderTest {

    @Test
    void testreadaihresholdexceeded() {
        BotReader.autotestEnable(18, 10);
        BotReader.setPscore(20);
        int result = BotReader.read();
        assertEquals(0, result);
    }

    @Test
    void testreadaimaxroundsexceeded() {
        BotReader.autotestEnable(15, 2);
        BotReader.setPscore(10);
        BotReader.read();
        int result = BotReader.read();
        assertEquals(1, result);
    }
}