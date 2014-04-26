package net.sbarbaro.filemaster.model;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;

/**
* FileNameFilter
* <p>
* {Purpose of This Class}
* <p>
* {Other Notes Relating to This Class (Optional)}
* @author Anthony J. Barbaro (tony@abarbaro.net)
* $LastChangedRevision: $
* $LastChangedDate: $
*/
public class FileNameFilter implements FileFilter, Serializable {
    
    private static final long serialVersionUID = -7541346502064658807L;

    private FileCriterion fc;
    private FileNameOperator op;
    private String target;
    
    /**
     * Default constructor
     */
    public FileNameFilter() {
        this(FileCriterion.EXT, FileNameOperator.EQUAL, "");
    }
    /**
     * Constructor
     * @param fc
     * @param op 
     */
    public FileNameFilter(FileCriterion fc, FileNameOperator op) {
        this(fc, op, "");
    }
    /**
     * Constructor
     * @param fc
     * @param op
     * @param target 
     */
    public FileNameFilter(FileCriterion fc, FileNameOperator op, String target) {
        this.op = op;
        this.target = target.toLowerCase();
        this.fc = fc;

    }
    /**
     * Copy constructor
     * @param fnf The FileNameFilter to copy
     */
    public FileNameFilter(FileNameFilter fnf) {
        this.op = fnf.getOp();
        this.target = fnf.getTarget();
        this.fc = fnf.fc;
    }

    public boolean accept(File pathname) {
        
        String name = pathname.getName().toLowerCase();
        
        if(FileCriterion.EXT == fc && name.contains(".")) {
            name = name.substring(name.lastIndexOf(".")+1);
        }
        
        switch(op) {
            case EQUAL:
                return name.equals(target);
            case NOT_EQUAL:
                return !name.equals(target);
            case CONTAINS:
                return name.contains(target);
            case NOT_CONTAINS:
                return !name.contains(target);
            case MATCHES:
                return name.matches(target);
            default: throw new UnsupportedOperationException("Bad op " + op.name());
        }
    }

    public FileNameOperator getOp() {
        return op;
    }

    public void setOp(FileNameOperator op) {
        this.op = op;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target.toLowerCase();
    }

    public boolean hasData() {
        return !(null == target || target.length() == 0 );
    }
}
