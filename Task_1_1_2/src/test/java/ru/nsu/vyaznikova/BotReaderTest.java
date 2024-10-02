package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BotReaderTest {

    @Test
    void testRead_AI_ThresholdExceeded() {
        BotReader.autotestEnable(18, 10);
        BotReader.setPscore(20);
        int result = BotReader.read();
        assertEquals(0, result);
    }

    @Test
    void testRead_AI_MaxRoundsExceeded() {
        BotReader.autotestEnable(15, 2);
        BotReader.setPscore(10);
        BotReader.read();
        int result = BotReader.read();
        assertEquals(1, result);
    }
}