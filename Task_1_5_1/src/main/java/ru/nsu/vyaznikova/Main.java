package ru.nsu.vyaznikova;

public class Main {
    public static void main(String[] args) {
        Table.Builder tableBuilder = new Table.Builder()
            .withAlignments(Table.Alignment.RIGHT, Table.Alignment.LEFT)
            .withRowLimit(8)
            .addRow("Index", "Random");

        for (int i = 1; i <= 5; i++) {
            final var value = (int) (Math.random() * 10);
            if (value > 5) {
                tableBuilder.addRow(String.valueOf(i), new Text.Bold(String.valueOf(value)));
            } else {
                tableBuilder.addRow(String.valueOf(i), String.valueOf(value));
            }
        }

        System.out.println(tableBuilder.build());
    }
}