package net.sbarbaro.filemaster.model;

import java.io.Serializable;

/**
 * FileMonitor
 * <p>
 * {Purpose of This Class}
 * <p>
 * {Other Notes Relating to This Class (Optional)}
 *
 * @author Anthony J. Barbaro (tony@abarbaro.net) $LastChangedRevision: $
 * $LastChangedDate: $
 */
public class FileMonitor implements Serializable {

    private static final long serialVersionUID = 2419888412171079364L;

    private String directoryName;
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

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public void setRecurse(boolean recurse) {
        this.recurse = recurse;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public boolean isRecurse() {
        return recurse;
    }

    public boolean hasData() {
        return !(null == directoryName);
    }

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
