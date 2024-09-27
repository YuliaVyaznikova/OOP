package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BotReaderTest {

    @Test
    void testRead_ai_enabled() {
        BotReader.autotestEnable(17, 5);
        BotReader.setPscore(15);
        assertEquals(1, BotReader.read());

        BotReader.setPscore(18);
        assertEquals(0, BotReader.read());

        BotReader.setPscore(17);
        assertEquals(1, BotReader.read());

        // Проверка ограничения по количеству раундов
        for (int i = 0; i < 4; i++) {
            BotReader.read();
        }
        assertEquals(2, BotReader.read());
    }

    @Test
    void testRead_ai_disabled() {
        BotReader.aiEnable = false;
        BotReader.console.reset();
        BotReader.console.next("1");  // Имитируем ввод "1"
        assertEquals(1, BotReader.read());

        BotReader.console.next("0");  // Имитируем ввод "0"
        assertEquals(0, BotReader.read());
    }
}