package ru.nsu.vyaznikova;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PizzaOrderTest {

    @Test
    void testEqualsAndHashCode() {
        PizzaOrder order1 = new PizzaOrder(1);
        PizzaOrder order2 = new PizzaOrder(1);
        PizzaOrder order3 = new PizzaOrder(2);

        assertTrue(order1.equals(order2));
        assertFalse(order1.equals(order3));

        assertTrue(order1.hashCode() == order2.hashCode());
        assertFalse(order1.hashCode() == order3.hashCode());
    }

    @Test
    void testToString() {
        PizzaOrder order = new PizzaOrder(1);
        String expectedString = "PizzaOrder{orderId=1}";
        assertTrue(expectedString.equals(order.toString()));
    }
}