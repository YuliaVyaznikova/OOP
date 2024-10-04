package ru.nsu.vyaznikova;

import java.util.HashMap;
import java.util.Map;

public abstract class Expression {

    public abstract String toString();

    public abstract Expression derivative(String var);

    public abstract int eval(String assign);

    public abstract Expression simplify();

    public static Map<String, Integer> parseAssignments(String assign) {
        Map<String, Integer> variables = new HashMap<>();
        if (assign != null) {
            String[] assignments = assign.split(";");
            for (String assignment : assignments) {
                String[] parts = assignment.trim().split("=");
                if (parts.length == 2) {
                    variables.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
                }
            }
        }
        return variables;
    }
}