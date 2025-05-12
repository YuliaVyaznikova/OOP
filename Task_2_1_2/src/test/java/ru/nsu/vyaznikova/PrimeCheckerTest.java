package ru.nsu.vyaznikova;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class PrimeCheckerTest {
    @Test
    void testNegativeNumbers() {
        assertFalse(PrimeChecker.isPrime(-7));
        assertFalse(PrimeChecker.isPrime(-1));
    }

    @Test
    void testZeroAndOne() {
        assertFalse(PrimeChecker.isPrime(0));
        assertFalse(PrimeChecker.isPrime(1));
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31})
    void testSmallPrimeNumbers(int number) {
        assertTrue(PrimeChecker.isPrime(number));
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20})
    void testSmallNonPrimeNumbers(int number) {
        assertFalse(PrimeChecker.isPrime(number));
    }

    @Test
    void testLargePrimeNumbers() {
        assertTrue(PrimeChecker.isPrime(6997901));
        assertTrue(PrimeChecker.isPrime(6997927));
        assertTrue(PrimeChecker.isPrime(6997937));
    }

    @Test
    void testLargeNonPrimeNumbers() {
        assertFalse(PrimeChecker.isPrime(6997900));
        assertFalse(PrimeChecker.isPrime(6997926));
        assertFalse(PrimeChecker.isPrime(6997936));
    }

    @Test
    void testSquaresOfPrimes() {
        assertFalse(PrimeChecker.isPrime(4));  // 2^2
        assertFalse(PrimeChecker.isPrime(9));  // 3^2
        assertFalse(PrimeChecker.isPrime(25)); // 5^2
        assertFalse(PrimeChecker.isPrime(49)); // 7^2
    }
}
