package net.sbarbaro.filemaster.model;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;

/**
 * FileSizeFilter
 * <p>
 * FileFilter implementation used to select files based on file size
 *
 * @author Anthony J. Barbaro (tony@abarbaro.net) $LastChangedRevision: $
 * $LastChangedDate: $
 */
public class FileSizeFilter implements FileFilter, Serializable {
    
    private static final long serialVersionUID = -8761208791537238473L;

    private FileSizeOperator op;
    private long target;
    private FileSizeUnit unit;

    /**
     * Constructor
     * @param op
     * @param target
     * @param unit 
     */
    public FileSizeFilter(FileSizeOperator op, long target, FileSizeUnit unit) {
        this.op = op;
        this.target = target;
        this.unit = unit;
    }
    
    /**
     * Constructor
     * @param op 
     */
    public FileSizeFilter(FileSizeOperator op) {
        this(op, 0, FileSizeUnit.BYTES);
    }
    /**
     * Copy constructor
     * @param fsf The FileSizeFilter to copy
     */
    public FileSizeFilter(FileSizeFilter fsf) {
        this.op = fsf.op;
        this.target = fsf.target;
        this.unit = fsf.unit;
    }
    public void setOp(FileSizeOperator op) {
        this.op = op;
    }

    public void setTarget(long target) {
        this.target = target;
    }

    public void setUnit(FileSizeUnit unit) {
        this.unit = unit;
    }

    public FileSizeFilter() {
        this(FileSizeOperator.LARGER);
    }


    public FileSizeOperator getOp() {
        return op;
    }

    public long getTarget() {
        return target;
    }

    public FileSizeUnit getUnit() {
        return unit;
    }

    @Override
    public boolean accept(File pathname) {

        long size = unit.scale(pathname.length());

        switch (op) {
            case LARGER:
                return size > target;

            default:
                return size <= target;
        }

    }
}
