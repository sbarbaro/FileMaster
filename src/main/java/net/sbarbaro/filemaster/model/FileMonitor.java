package net.sbarbaro.filemaster.model;

import java.io.Serializable;

/**
 * FileMonitor
 * <p>
 * Identifies a file system directory to monitor
 * <p>
 *
 * @author Anthony J. Barbaro (tony@abarbaro.net) 
 */
public class FileMonitor implements Serializable {

    private static final long serialVersionUID = 2419888412171079364L;

    // The name of the directory to monitor
    private String directoryName;
    // Tells when subdirectories of directoryName are to be considered
    // when filtering
    private boolean recurse;

    /**
     * Default constructor
     */
    public FileMonitor() {
    }

    /**
     * Constructor
     *
     * @param directoryName
     * @param recurse
     */
    public FileMonitor(String directoryName, boolean recurse) {
        this.directoryName = directoryName;
        this.recurse = recurse;
    }

    /**
     * Copy constructor
     *
     * @param fileMonitor The FileMonitor instance to copy
     */
    public FileMonitor(FileMonitor fileMonitor) {
        this.directoryName = fileMonitor.directoryName;
        this.recurse = fileMonitor.recurse;
    }

    /**
     * Sets this directoryName
     * @param directoryName The directoryName string to set
     */
    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }
    /**
     * Sets this recurse indicator
     * @param recurse true when directory recursion is desired; otherwise false
     */
    public void setRecurse(boolean recurse) {
        this.recurse = recurse;
    }

    /**
     * 
     * @return this directoryName 
     */
    public String getDirectoryName() {
        return directoryName;
    }

    /**
     * 
     * @return true when the monitored directory is to be recursed when
     * filtering; otherwise, false.
     */
    public boolean isRecurse() {
        return recurse;
    }


    /**
     * Compares this FileMonitor with the given object input parameter
     * @param objectIn An instance of FileMonitor
     * @return true when the input object is equal to this FileMonitor based 
     * on an exact comparison of directoryName
     */
    @Override
    public boolean equals(Object objectIn) {
        
        boolean isEqual = false;
        
        if(objectIn instanceof FileMonitor) {
            
            FileMonitor fileMonitorIn = (FileMonitor) objectIn;
            
            String dirIn = fileMonitorIn.getDirectoryName();
            
            String dirThis = this.getDirectoryName();
            
            isEqual = dirIn.equals(dirThis);
            
        }
        return isEqual;
    }

    /**
     * @return the hashCode of this FileMonitor based on directoryName
     */
    @Override
    public int hashCode() {
        
        int hashCode = super.hashCode();
        
        if(this.directoryName == null) {
            
        } else {
            
            hashCode =  this.getDirectoryName().hashCode();
        }
        return hashCode;
    }
    

}
