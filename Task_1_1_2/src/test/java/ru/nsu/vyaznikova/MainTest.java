package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;

public class MainTest {
    @Test
    void testDoAll() {
        BotReader.autotestEnable(15, 5);
        Main.doAll();
    }
}