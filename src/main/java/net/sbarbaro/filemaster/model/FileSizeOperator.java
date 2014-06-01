package net.sbarbaro.filemaster.model;

/**
 * Valid operators for comparing files based on size.
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net) 
 */
public enum FileSizeOperator {

    LARGER("Larger than"),
    SMALLER("Smaller than");

    @Override
    public String toString() {
        return text;
    }

    // Private constructor
    private FileSizeOperator(String text) {
        this.text = text;
    }

    private final String text;

}
