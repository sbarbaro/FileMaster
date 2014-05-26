package net.sbarbaro.filemaster.model;

/**
 *
 * An enumeration of operators to use by the FileContentFilter
 *
* @author Anthony J. Barbaro (tony@abarbaro.net)
 */
public enum FileContentOperator {

    CONTAINS("Contains"),
    NOT_CONTAINS("Does not contain"),
    MATCHES("Matches reqular expression");

    @Override
    public String toString() {
        return text;
    }

    // Private constructor
    private FileContentOperator(String text) {
        this.text = text;
    }

    private final String text;

}
