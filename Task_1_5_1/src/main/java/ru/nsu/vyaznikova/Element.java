package ru.nsu.vyaznikova;

/**
 * Abstract class representing a Markdown element.
 * This is the base class for all Markdown elements in the library.
 */
public abstract class Element {
    
    /**
     * Converts the element to its Markdown representation.
     * Each subclass must implement this method to provide its specific Markdown format.
     *
     * 
     * @return the Markdown string representation of the element
     */
    public abstract String toMarkdown();

    /**
     * Checks if this element is equal to another object.
     * Two elements are considered equal if their Markdown representations are identical.
     *
     * 
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Element)) {
            return false;
        }
        return toMarkdown().equals(((Element) obj).toMarkdown());
    }

    /**
     * Returns a hash code value for the element.
     * The hash code is based on the element's Markdown representation.
     *
     * 
     * @return a hash code value for this element
     */
    @Override
    public int hashCode() {
        return toMarkdown().hashCode();
    }
}