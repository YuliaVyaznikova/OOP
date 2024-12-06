package ru.nsu.vyaznikova;

public abstract class Element {
    public abstract String toMarkdown();

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return toMarkdown().equals(((Element) obj).toMarkdown());
    }
}