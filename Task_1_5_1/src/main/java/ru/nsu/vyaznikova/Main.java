package ru.nsu.vyaznikova;

/**
 * Main class that demonstrates the usage of Markdown table generation.
 * Creates a table with two columns: Index and Random.
 * The Random column contains random numbers from 0 to 9,
 * with numbers greater than 5 being displayed in bold.
 */
public class Main {
    /**
     * Main method that creates and prints a Markdown table.
     * The table has right alignment for the Index column
     * and left alignment for the Random column.
     * Numbers greater than 5 in the Random column are displayed in bold.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        Table.Builder tableBuilder = new Table.Builder()
                .withAlignments(Table.ALIGN_RIGHT, Table.ALIGN_LEFT)
                .withRowLimit(8)
                .addRow("Index", "Random");

        for (int i = 1; i <= 5; i++) {
            final var value = (int) (Math.random() * 10);
            if (value > 5) {
                tableBuilder.addRow(
                        String.valueOf(i),
                        "**" + value + "**"
                );
            } else {
                tableBuilder.addRow(
                        String.valueOf(i),
                        String.valueOf(value)
                );
            }
        }

        System.out.print(tableBuilder.build());
    }
}