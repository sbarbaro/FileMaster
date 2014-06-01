package net.sbarbaro.filemaster.model;

/**
 * FileNameOperator
 * <p>
 * An enumeration of all comparison operations that can be used to determine the
 * acceptability of files
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net)
 */
public enum FileNameOperator {

    EQUAL("Is"),
    NOT_EQUAL("Is not"),
    CONTAINS("Contains"),
    NOT_CONTAINS("Does not contain"),
    MATCHES("Matches reqular expression");

    @Override
    public String toString() {
        return text;
    }

    // Private constructor
    private FileNameOperator(String text) {
        this.text = text;
    }

    private final String text;

}
