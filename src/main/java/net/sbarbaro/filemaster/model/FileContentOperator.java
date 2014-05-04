package net.sbarbaro.filemaster.model;

/**
 * 
 * @author ajb
 */
public enum FileContentOperator {
    

    CONTAINS("Contains"),
    NOT_CONTAINS("Does not contain"),
    MATCHES("Matches reqular expression");
    
    FileContentOperator(String text) {
        this.text = text;
    }
    
    private final String text;
    
    @Override
    public String toString() {
        return text;
    }
}
