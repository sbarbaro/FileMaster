package net.sbarbaro.filemaster.model;

import java.io.File;
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

    private File directory;
    private boolean recurse;

    /**
     * Default constructor
     */
    public FileMonitor() {

    }

    /**
     * Constructor
     *
     * @param directory
     * @param recurse
     */
    public FileMonitor(File directory, boolean recurse) {
        this.directory = directory;
        this.recurse = recurse;
    }

    /**
     * Copy constructor
     *
     * @param fileMonitor The FileMonitor instance to copy
     */
    public FileMonitor(FileMonitor fileMonitor) {
        this.directory = fileMonitor.directory;
        this.recurse = fileMonitor.recurse;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public void setRecurse(boolean recurse) {
        this.recurse = recurse;
    }

    public File getDirectory() {
        return directory;
    }

    public boolean isRecurse() {
        return recurse;
    }

    public boolean hasData() {
        return !(null == directory);
    }

    @Override
    public boolean equals(Object objectIn) {
        
        boolean isEqual = false;
        
        if(objectIn instanceof FileMonitor) {
            
            FileMonitor fileMonitorIn = (FileMonitor) objectIn;
            
            File dirIn = fileMonitorIn.getDirectory();
            
            File dirThis = this.getDirectory();
            
            isEqual = (dirIn.getAbsolutePath().equals(dirThis.getAbsolutePath()));
            
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        
        int hashCode = super.hashCode();
        
        if(this.directory == null) {
            
        } else {
            
            hashCode =  this.getDirectory().getAbsolutePath().hashCode();
        }
        return hashCode;
    }
    

}
