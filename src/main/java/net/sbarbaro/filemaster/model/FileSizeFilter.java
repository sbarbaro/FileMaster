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
 *
 * @author Anthony J. Barbaro (tony@abarbaro.net) $LastChangedRevision: $
 * $LastChangedDate: $
 */
public class FileSizeFilter implements DirectoryStream.Filter<Path>, Serializable {

    private static final long serialVersionUID = -8761208791537238473L;

    private FileSizeOperator op;
    private long target;
    private FileSizeUnit unit;

    /**
     * Constructor
     *
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
     *
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
    public boolean accept(Path pathIn) {

        final Path path = Paths.get(pathIn.getParent().toString());
        final Path file = path.resolve(pathIn.getFileName());
        BasicFileAttributes attrs;
        
        long size = 0;
        
        try {
            attrs = Files.readAttributes(file, BasicFileAttributes.class);
            size = unit.scale(attrs.size());
        } catch (IOException ex) {
            Logger.getLogger(FileSizeFilter.class.getName()).log(Level.SEVERE, null, ex);
        }

        switch (op) {
            case LARGER:
                return size > target;

            default:
                return size <= target;
        }

    }
}
