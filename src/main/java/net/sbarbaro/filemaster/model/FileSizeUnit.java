package net.sbarbaro.filemaster.model;

/**
 * Enumeration of units for specifying file sizes
 * <p>
 * @author steven
 */
public enum FileSizeUnit {
    
    BYTES("Bytes", 1), 
    KB("KB", 1024), 
    MB("MB",1024*1024), 
    GB("GB", (int) Math.pow(1024, 3));
    
   
    /**
     * Scales the given absolute size of a file in bytes based on
     * FileSizeUnit.  For example, a file size of 2048 bytes will be scaled
     * to 2KB.
     * @param size the absolute size of the file
     * @return The scaled file size
     */
    public long scale(long size) {
        return size/d;
    } 
    
    
    @Override
    public String toString() {
        return text;
    }
    
    // Private constructor
    private FileSizeUnit(String text, int d) {
        this.d = d;
        this.text = text;
    }
    
    private final long d;
    private final String text;

}
