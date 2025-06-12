package ru.nsu.vyaznikova;

import java.util.Objects;

/**
 * Класс, представляющий заказ на пиццу.
 */
public class PizzaOrder {
    private final int orderId;

    public PizzaOrder(int orderId) {
        this.orderId = orderId;
    }

    /**
     * Возвращает идентификатор заказа.
     * 
     * @return идентификатор заказа
     */
    public int getOrderId() {
        return orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PizzaOrder that = (PizzaOrder) o;
        return orderId == that.orderId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId);
    }

    @Override
    public String toString() {
        return "PizzaOrder{" 
            + "orderId=" + orderId 
            + '}';
    }
}