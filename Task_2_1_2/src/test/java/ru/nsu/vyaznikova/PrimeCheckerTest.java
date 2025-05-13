package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class PrimeCheckerTest {
    @Test
    void testNegativeNumbers() {
        assertFalse(PrimeChecker.isPrime(-7));
        assertFalse(PrimeChecker.isPrime(-1));
        assertFalse(PrimeChecker.isPrime(Integer.MIN_VALUE));
        assertFalse(PrimeChecker.isPrime(-Integer.MAX_VALUE));
    }

    @Test
    void testZeroAndOne() {
        assertFalse(PrimeChecker.isPrime(0));
        assertFalse(PrimeChecker.isPrime(1));
    }

    @ParameterizedTest
    @ValueSource(ints = {
        2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37,
        41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
    })
    void testSmallPrimeNumbers(int number) {
        assertTrue(PrimeChecker.isPrime(number));
    }

    @ParameterizedTest
    @ValueSource(ints = {
        4, 6, 8, 9, 10, 12, 14, 15, 16, 18, 20, 21, 22,
        24, 25, 26, 27, 28, 30, 32, 33, 34, 35, 36, 38, 39, 40
    })
    void testSmallNonPrimeNumbers(int number) {
        assertFalse(PrimeChecker.isPrime(number));
    }

    @ParameterizedTest
    @CsvSource({
        "6997901, true",
        "6997927, true",
        "6997937, true",
        "7919, true",
        "7907, true",
        "104729, true",
        "104723, true",
        "104717, true",
        "999983, true",
        "999979, true"
    })
    void testLargePrimeNumbers(int number, boolean expected) {
        assertEquals(expected, PrimeChecker.isPrime(number));
    }

    @ParameterizedTest
    @CsvSource({
        "6997900, false",
        "6997926, false",
        "6997936, false",
        "7918, false",
        "7906, false",
        "104728, false",
        "104722, false",
        "104716, false",
        "999982, false",
        "999978, false"
    })
    void testLargeNonPrimeNumbers(int number, boolean expected) {
        assertEquals(expected, PrimeChecker.isPrime(number));
    }

    static Stream<Integer> squaresOfPrimes() {
        return Stream.of(2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31)
                     .map(n -> n * n);
    }

    @ParameterizedTest
    @MethodSource("squaresOfPrimes")
    void testSquaresOfPrimes(int number) {
        assertFalse(PrimeChecker.isPrime(number));
    }

    static Stream<Integer> productsOfPrimes() {
        return Stream.of(
            new int[]{2, 3},
            new int[]{3, 5},
            new int[]{5, 7},
            new int[]{7, 11},
            new int[]{11, 13},
            new int[]{13, 17},
            new int[]{17, 19},
            new int[]{19, 23},
            new int[]{23, 29},
            new int[]{29, 31},
            new int[]{31, 37}
        ).map(arr -> arr[0] * arr[1]);
    }

    @ParameterizedTest
    @MethodSource("productsOfPrimes")
    void testProductOfPrimes(int number) {
        assertFalse(PrimeChecker.isPrime(number));
    }

    @Test
    void testConsecutiveNumbers() {
        // Test ranges of consecutive numbers
        IntStream.rangeClosed(90, 100).forEach(i -> 
            assertEquals(isPrimeReference(i), PrimeChecker.isPrime(i),
                "Failed for number: " + i));

        IntStream.rangeClosed(190, 200).forEach(i -> 
            assertEquals(isPrimeReference(i), PrimeChecker.isPrime(i),
                "Failed for number: " + i));

        IntStream.rangeClosed(990, 1000).forEach(i -> 
            assertEquals(isPrimeReference(i), PrimeChecker.isPrime(i),
                "Failed for number: " + i));
    }

    // Reference implementation for verification
    private boolean isPrimeReference(int n) {
        if (n <= 1) {
            return false;
        }
        if (n <= 3) {
            return true;
        }
        if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    @Test
    void testSpecialCases() {
        // Powers of 2 minus 1 (some are Mersenne primes)
        assertTrue(PrimeChecker.isPrime(3));     // 2^2 - 1
        assertTrue(PrimeChecker.isPrime(7));     // 2^3 - 1
        assertTrue(PrimeChecker.isPrime(31));    // 2^5 - 1
        assertFalse(PrimeChecker.isPrime(15));   // 2^4 - 1
        
        // Numbers close to powers of 2
        assertFalse(PrimeChecker.isPrime(64));
        assertTrue(PrimeChecker.isPrime(67));
        assertFalse(PrimeChecker.isPrime(128));
        assertTrue(PrimeChecker.isPrime(131));
        assertFalse(PrimeChecker.isPrime(256));
        assertTrue(PrimeChecker.isPrime(257));
    }

    @Test
    void testEdgeCases() {
        // Test numbers around Integer.MAX_VALUE/2
        int halfMaxInt = Integer.MAX_VALUE / 2;
        assertFalse(PrimeChecker.isPrime(halfMaxInt - 1));
        assertFalse(PrimeChecker.isPrime(halfMaxInt));
        assertFalse(PrimeChecker.isPrime(halfMaxInt + 1));

        // Test some large composite numbers
        assertFalse(PrimeChecker.isPrime(Integer.MAX_VALUE - 1));
        assertFalse(PrimeChecker.isPrime(Integer.MAX_VALUE - 2));
    }

    @Test
    void testHasNonPrimeEmpty() {
        assertFalse(PrimeChecker.hasNonPrime(new int[]{}));
    }

    @Test
    void testHasNonPrimeAllPrime() {
        assertFalse(PrimeChecker.hasNonPrime(new int[]{2, 3, 5, 7, 11, 13}));
    }

    @Test
    void testHasNonPrimeAllNonPrime() {
        assertTrue(PrimeChecker.hasNonPrime(new int[]{4, 6, 8, 9, 10}));
    }

    @Test
    void testHasNonPrimeMixed() {
        assertTrue(PrimeChecker.hasNonPrime(new int[]{2, 3, 4, 5, 6}));
    }

    @Test
    void testHasNonPrimeSinglePrime() {
        assertFalse(PrimeChecker.hasNonPrime(new int[]{17}));
    }

    @Test
    void testHasNonPrimeSingleNonPrime() {
        assertTrue(PrimeChecker.hasNonPrime(new int[]{16}));
    }

    @Test
    void testCheckArrayResultEmpty() {
        assertEquals("All numbers are prime", PrimeChecker.checkArrayResult(new int[]{}));
    }

    @Test
    void testCheckArrayResultAllPrime() {
        assertEquals("All numbers are prime", 
            PrimeChecker.checkArrayResult(new int[]{2, 3, 5, 7, 11}));
    }

    @Test
    void testCheckArrayResultFirstNonPrime() {
        assertEquals("Found non-prime number: 4", 
            PrimeChecker.checkArrayResult(new int[]{4, 2, 3, 5}));
    }

    @Test
    void testCheckArrayResultMiddleNonPrime() {
        assertEquals("Found non-prime number: 6", 
            PrimeChecker.checkArrayResult(new int[]{2, 3, 6, 5, 7}));
    }

    @Test
    void testCheckArrayResultLastNonPrime() {
        assertEquals("Found non-prime number: 9", 
            PrimeChecker.checkArrayResult(new int[]{2, 3, 5, 7, 9}));
    }

    @Test
    void testCheckArrayResultSinglePrime() {
        assertEquals("All numbers are prime", 
            PrimeChecker.checkArrayResult(new int[]{17}));
    }

    @Test
    void testCheckArrayResultSingleNonPrime() {
        assertEquals("Found non-prime number: 16", 
            PrimeChecker.checkArrayResult(new int[]{16}));
    }
}
