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
 * FileAgeFilter
 * <p>
 * Filters files based on age
 * <p>
 *
 * @author Anthony J. Barbaro (tony@abarbaro.net)
 */
public class FileAgeFilter implements DirectoryStream.Filter<Path>, Serializable {

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
        this.age = faf.getAgeThreshold();
    }

    /**
     * Accepts a file based on being younger or older than this age and this
     * ageOp
     *
     * @param pathIn The path of the file to check
     * @return true if the file satisfies the age criteria
     */
    @Override
    public boolean accept(Path pathIn) {

        boolean accept = false;

        try {
            final Path path = Paths.get(pathIn.getParent().toString());
            final Path file = path.resolve(pathIn.getFileName());
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
            final long threshold = ageUnit.getMillis(age);
            switch (ageOp) {
                case OLDER:
                    accept = diff > threshold;
                    break;
                default:
                    accept = diff <= threshold;
            }
        } catch (IOException ex) {
            Logger.getLogger(FileAgeFilter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return accept;
    }

    /**
     * @return this ageOp
     */
    public FileAgeOperator getAgeOp() {
        return ageOp;
    }

    /**
     * Sets this ageOP
     *
     * @param ageOp The FileAgeOperator to set
     */
    public void setAgeOp(FileAgeOperator ageOp) {
        this.ageOp = ageOp;
    }

    /**
     * @return this ageUnit
     */
    public FileAgeUnit getAgeUnit() {
        return ageUnit;
    }

    /**
     * Sets this ageUnit
     *
     * @param ageUnit The FileAgeUnit to set
     */
    public void setAgeUnit(FileAgeUnit ageUnit) {
        this.ageUnit = ageUnit;
    }

    /**
     *
     * @return this age
     */
    public int getAgeThreshold() {
        return age;
    }

    /**
     * Sets this age
     *
     * @param ageThreshold The age value to set
     */
    public void setAgeThreshold(int ageThreshold) {
        this.age = ageThreshold;
    }

}
