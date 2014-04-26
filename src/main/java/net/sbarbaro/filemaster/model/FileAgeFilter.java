package net.sbarbaro.filemaster.model;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;

/**
 * FileAgeFilter
 * <p>
 * {Purpose of This Class}
 * <p>
 * {Other Notes Relating to This Class (Optional)}
 *
 * @author Anthony J. Barbaro (tony@abarbaro.net) $LastChangedRevision: $
 * $LastChangedDate: $
 */
public class FileAgeFilter implements FileFilter, Serializable {

    private static final long serialVersionUID = -8337515795561653463L;

    private FileAgeOperator ageOp;
    private FileAgeUnit ageUnit;
    private int ageThreshold;
    private long now;

    /**
     * Default constructor
     */
    public FileAgeFilter() {
    }

    /**
     * Construct a new Age-Based file filter
     *
     * @param ageOp Determines if files are to be accepted either older or
     * younger than a threshold.
     * @param ageThreshold Defines the threshold between accepted and reject
     * files
     * @param ageUnit The units for the ageThreshold
     */
    public FileAgeFilter(FileAgeOperator ageOp, int ageThreshold, FileAgeUnit ageUnit) {
        this.ageOp = ageOp;
        this.ageUnit = ageUnit;
        this.ageThreshold = ageThreshold;
        this.now = System.currentTimeMillis();
    }

    /**
     * Copy constructor
     *
     * @param faf The FileAgeFilter instance to copy
     */
    public FileAgeFilter(FileAgeFilter faf) {
        this.ageOp = faf.ageOp;
        this.ageUnit = faf.ageUnit;
        this.ageThreshold = faf.ageThreshold;
        this.now = System.currentTimeMillis();
    }

    public boolean accept(File pathname) {

        long diff = now - pathname.lastModified();

        int t = ageThreshold * ageUnit.m;

        switch (ageOp) {
            case OLDER:
                return diff > t;
            default:
                return diff < t;
        }

    }

    public FileAgeOperator getAgeOp() {
        return ageOp;
    }

    public void setAgeOp(FileAgeOperator ageOp) {
        this.ageOp = ageOp;
    }

    public FileAgeUnit getAgeUnit() {
        return ageUnit;
    }

    public void setAgeUnit(FileAgeUnit ageUnit) {
        this.ageUnit = ageUnit;
    }

    public int getAgeThreshold() {
        return ageThreshold;
    }

    public void setAgeThreshold(int ageThreshold) {
        this.ageThreshold = ageThreshold;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

}
