package net.sbarbaro.filemaster.model;

/**
 * LogicalGroup
 * <p>
 * Enumerates the binary operation, "And" or "Or", to apply over two or more
 * FileCriterion
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net)
 */
 public enum LogicalGroup {
    AND("All of the following"), OR("Any of the following");

    @Override
    public String toString() {
        return text;
    }
    
    // Private constructor
    private LogicalGroup(String text) {
        this.text = text;
    }
    private final String text;


    
}
