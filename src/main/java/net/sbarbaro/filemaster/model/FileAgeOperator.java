package net.sbarbaro.filemaster.model;

/**
 * FileAgeOperator
 * <p>
 * An enumeration of comparison operators (older, younger) to use for
 * assessing acceptance of files based on their age.
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net)
 */
public enum FileAgeOperator {

    OLDER("More than"),
    YOUNGER("Less than");

    @Override
    public String toString() {
        return text;
    }
    // Private constructor
    private FileAgeOperator(String text) {
        this.text = text;
    }

    private final String text;

}
