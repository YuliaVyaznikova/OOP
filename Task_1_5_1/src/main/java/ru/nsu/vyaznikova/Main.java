package ru.nsu.vyaznikova;

public class Main {
    public static void main(String[] args) {
        Table.Builder tableBuilder = new Table.Builder()
            .withAlignments(Table.ALIGN_RIGHT, Table.ALIGN_LEFT)
            .withRowLimit(8)
            .addRow("Index", "Random");

        for (int i = 1; i <= 5; i++) {
            final var value = (int) (Math.random() * 10);
            if (value > 5) {
                tableBuilder.addRow(i, new Text.Builder()
                    .setContent(String.valueOf(value))
                    .setBold(true)
                    .build());
            } else {
                tableBuilder.addRow(i, value);
            }
        }

        System.out.println(tableBuilder.build());
    }
}