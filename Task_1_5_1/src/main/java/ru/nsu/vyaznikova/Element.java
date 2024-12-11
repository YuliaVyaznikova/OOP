package ru.nsu.vyaznikova;

/**
 * Abstract base class for all Markdown elements.
 * This class provides the foundation for creating various Markdown elements
 * such as text formatting, tables, lists, headers, etc.
 */
public abstract class Element {
    
    /**
     * Converts the element to its Markdown representation.
     * @return A string containing the Markdown representation of the element
     */
    public abstract String toMarkdown();

    /**
     * Compares this element to the specified object for equality.
     * Two elements are considered equal if they are of the same class
     * and their Markdown representations are identical.
     * @param obj the object to compare this element against
     * @return true if the given object is equal to this element, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return toMarkdown().equals(((Element) obj).toMarkdown());
    }
}