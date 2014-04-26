package net.sbarbaro.filemaster.model;

/**
 * Enumeration of all supported file operations
 * @author ajb
 */
public enum FileActionOperator {
    
    MOVE("Move file to"),
    COPY("Copy file to"),
    RENAME("Rename file to"),
    RECYCLE("Send to recycle bin"),
    DELETE("Delete file");

    @Override
    public String toString() {
        return text;
    }
    
    private FileActionOperator(String text) {
        this.text = text;
    }
    public final String text;
}
