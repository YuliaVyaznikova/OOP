package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

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