package net.sbarbaro.filemaster.model;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private FileCriterion fileCriterion;
    private FileAgeOperator ageOp;
    private int age;
    private FileAgeUnit ageUnit;

    /**
     * Default constructor
     */
    public FileAgeFilter() {
    }

    /**
     * Construct a new Age-Based file filter
     *
     * @param fileCriterion
     * @param ageOp Determines if files are to be accepted either older or
     * younger than a threshold.
     * @param age
     * @param ageUnit The units for the age
     */
    public FileAgeFilter(FileCriterion fileCriterion, FileAgeOperator ageOp, int age, FileAgeUnit ageUnit) {

        this.fileCriterion = fileCriterion;
        this.ageOp = ageOp;
        this.ageUnit = ageUnit;
        this.age = age;
    }

    /**
     * Copy constructor
     *
     * @param faf The FileAgeFilter instance to copy
     */
    public FileAgeFilter(FileAgeFilter faf) {
        this.fileCriterion = faf.fileCriterion;
        this.ageOp = faf.ageOp;
        this.ageUnit = faf.ageUnit;
        this.age = faf.getAge();
    }

    @Override
    public boolean accept(File pathname) {

        boolean accept = false;

        try {
            final Path path = Paths.get(pathname.getParent());
            final Path file = path.resolve(pathname.getAbsolutePath());
            BasicFileAttributes attrs = Files.readAttributes(file, BasicFileAttributes.class);

            long diff = System.currentTimeMillis();

            switch (fileCriterion) {

                case CREATED:
                    diff -= attrs.creationTime().toMillis();
                    break;
                case MODIFED:
                    diff -= attrs.lastModifiedTime().toMillis();
                    break;
                case ACCESSED:
                    diff -= attrs.lastAccessTime().toMillis();
                    break;
                default:
                    throw new UnsupportedOperationException("Unexpected criterion " + fileCriterion);
            }

            switch (ageOp) {
                case OLDER:
                    accept = diff > ageUnit.getMillis(age);
                    break;
                default:
                    accept = diff <= ageUnit.getMillis(age);
            }
        } catch (IOException ex) {
            Logger.getLogger(FileAgeFilter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return accept;
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

    public int getAge() {
        return age;
    }

    public void setAgeThreshold(int ageThreshold) {
        this.age = ageThreshold;
    }
}
