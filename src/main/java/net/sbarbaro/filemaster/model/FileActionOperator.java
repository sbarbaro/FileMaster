package net.sbarbaro.filemaster.model;

/**
 * An enumeration of all supported file operations
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net) $LastChangedRevision: $
 * $LastChangedDate: $
 */
public enum FileActionOperator {

    MOVE("Move file to"),
    COPY("Copy file to"),
    RENAME("Rename file to"),
    RECYCLE("Send to recycle bin"),
    DELETE("Delete file");

    /**
     * @return the text assigned to this enumerated value
     */
    @Override
    public String toString() {
        return text;
    }

    /*
     Private constructor
     */
    private FileActionOperator(String text) {
        this.text = text;
    }
    /*
     * The text assigned to this enumerated value
     */
    private final String text;
}
