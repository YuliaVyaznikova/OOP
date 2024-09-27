package ru.nsu.vyaznikova;

import java.util.Scanner;

/**
 * This class provides functionality for reading user input,
 * with an option to simulate an AI player for testing purposes.
 */
public class BotReader {

    /**
     * Flag indicating whether AI mode is enabled.
     */
    static boolean aiEnable = false;

    /**
     * Scanner for reading user input from the console.
     */
    static Scanner console = new Scanner(System.in);

    /**
     * Stores the last read value.
     */
    static int lastreaded;

    /**
     * Stores the current player's score.
     */
    static int pscore;

    /**
     * Threshold score for the AI to stop taking cards.
     */
    static int threshold;

    /**
     * Maximum number of rounds the AI will play before exiting.
     */
    static int maxreads;

    /**
     * Current round number in AI mode.
     */
    static int curround;

    /**
     * Enables the AI mode with specified parameters.
     *
     * @param thr The threshold score for the AI to stop taking cards.
     * @param mxr The maximum number of rounds the AI will play before exiting.
     */
    public static void autoTestEnable(int thr, int mxr) {
        aiEnable = true;
        threshold = thr;
        maxreads = mxr;
        curround = 0;
    }

    /**
     * Returns the last read value.
     *
     * @return The last read value.
     */
    public static int getLastreaded() {
        return lastreaded;
    }

    /**
     * Sets the player's score.
     *
     * @param npscore The player's score.
     */
    public static void setPscore(int npscore) {
        pscore = npscore;
    }

    /**
     * Reads input from the user or AI, based on the current mode.
     *
     * @return The read value (0 or 1).
     */
    public static int read() {
        if (aiEnable) {
            curround++;
            if (curround > maxreads) {
                return 2;
            }
            if (pscore > threshold) {
                System.out.printf("Бот ходит: 0\n", pscore);
                lastreaded = 0;
                return 0;
            } else {
                System.out.printf("Бот ходит: 1\n", pscore);
                lastreaded = 1;
                return 1;
            }
        } else {
            lastreaded = console.nextInt();
            return lastreaded;
        }
    }
}