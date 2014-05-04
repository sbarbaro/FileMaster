
package net.sbarbaro.filemaster.model;

/**
 *
 * @author steven
 */
public enum FileSizeUnit {
    
    BYTES("Bytes", 1), 
    KB("KB", 1024), 
    MB("MB",1024*1024), 
    GB("GB", (int) Math.pow(1024, 3));
    
   
    public long scale(long size) {
        return size/d;
    } 
    private FileSizeUnit(String text, int d) {
        this.d = d;
        this.text = text;
    }
    
    private final long d;
    private final String text;

    @Override
    public String toString() {
        return text;
    }
    
}
