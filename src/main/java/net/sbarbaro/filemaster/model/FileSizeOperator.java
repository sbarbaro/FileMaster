package net.sbarbaro.filemaster.model;

/**
 * Valid operators for comparing files based on size.
 *
 * @author ajb
 */
public enum FileSizeOperator {

    LARGER("Larger than"),
    SMALLER("Smaller than");

    @Override
    public String toString() {
        return text;
    }

    FileSizeOperator(String text) {
        this.text = text;
    }

    private final String text;

}
