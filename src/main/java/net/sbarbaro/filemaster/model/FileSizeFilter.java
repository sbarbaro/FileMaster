package net.sbarbaro.filemaster.model;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FileSizeFilter
 * <p>
 * FileFilter implementation used to select files based on file size
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net) 
 */
public class FileSizeFilter implements DirectoryStream.Filter<Path>, Serializable {

    private static final long serialVersionUID = -8761208791537238473L;

    private FileSizeOperator fileSizeOperator;
    private long target;
    private FileSizeUnit fileSizeUnit;

    /**
     * Constructor
     *
     * @param op
     * @param target
     * @param unit
     */
    public FileSizeFilter(FileSizeOperator op, long target, FileSizeUnit unit) {
        this.fileSizeOperator = op;
        this.target = target;
        this.fileSizeUnit = unit;
    }
    /**
     * Default constructor
     */
    public FileSizeFilter() {
        this(FileSizeOperator.LARGER);
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
     *
     * @param fsf The FileSizeFilter to copy
     */
    public FileSizeFilter(FileSizeFilter fsf) {
        this.fileSizeOperator = fsf.fileSizeOperator;
        this.target = fsf.target;
        this.fileSizeUnit = fsf.fileSizeUnit;
    }

    /**
     * Sets this fileSizeOperator
     * @param op The FileSizeOperator to set
     */
    public void setFileSizeOperator(FileSizeOperator op) {
        this.fileSizeOperator = op;
    }

    /**
     * Sets this target
     * @param target  The size threshold to set
     */
    public void setTarget(long target) {
        this.target = target;
    }

    /**
     * Sets this fileSizeUnit
     * @param unit the unit for this target threshold
     */
    public void setFileSizeUnit(FileSizeUnit unit) {
        this.fileSizeUnit = unit;
    }

    /**
     * @return this fileSizeOperator 
     */
    public FileSizeOperator getFileSizeOperator() {
        return fileSizeOperator;
    }

    /**
     * @return this target size threshold 
     */
    public long getTarget() {
        return target;
    }

    /**
     * @return the unit value for this target threshold
     */
    public FileSizeUnit getFileSizeUnit() {
        return fileSizeUnit;
    }

    /**
     * Determines if the file specified by the given path satisfies the size
     * criterion for selection
     * @param pathIn The Path of the file to check
     * @return true if fileSizeOperator is LARGER and the size of the file is
     * greater than the target threshold, or the fileSizeOperator is SMALLER
     * and the size of the file is less than or equals to the target threshold; 
     *  otherwise returns false.
     */
    @Override
    public boolean accept(Path pathIn) {

        final Path path = Paths.get(pathIn.getParent().toString());
        final Path file = path.resolve(pathIn.getFileName());
        BasicFileAttributes attrs;
        
        long size = 0;
        
        try {
            attrs = Files.readAttributes(file, BasicFileAttributes.class);
            size = fileSizeUnit.scale(attrs.size());
        } catch (IOException ex) {
            Logger.getLogger(FileSizeFilter.class.getName()).log(Level.SEVERE, null, ex);
        }

        switch (fileSizeOperator) {
            case LARGER:
                return size > target;

            default:
                return size <= target;
        }

    }
}
