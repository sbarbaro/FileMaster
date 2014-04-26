package net.sbarbaro.filemaster.model;

/**
 *
 * @author ajb
 */
public enum FileNameOperator {
    
    EQUAL("Is"),
    NOT_EQUAL("Is not"),
    CONTAINS("Contains"),
    NOT_CONTAINS("Does not contain"),
    MATCHES("Matches reqular expression");
    
    FileNameOperator(String text) {
        this.text = text;
    }
    
    private final String text;

    @Override
    public String toString() {
        return text;
    }
    
}
