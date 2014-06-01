package net.sbarbaro.filemaster.model;

import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;

/**
* FileNameFilter
* <p>
* Filters file based on their names
* <p>
 * @author Steven A. Barbaro (steven@abarbaro.net)
*/
public class FileNameFilter implements DirectoryStream.Filter<Path>, Serializable {
    
    private static final long serialVersionUID = -7541346502064658807L;

    private FileCriterion fileCriterion;
    private FileNameOperator fileNameOperator;
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
        this.fileNameOperator = op;
        this.target = target.toLowerCase();
        this.fileCriterion = fc;

    }
    /**
     * Copy constructor
     * @param fnf The FileNameFilter to copy
     */
    public FileNameFilter(FileNameFilter fnf) {
        this.fileNameOperator = fnf.getFileNameOperator();
        this.target = fnf.getTarget();
        this.fileCriterion = fnf.fileCriterion;
    }


    /**
     * Accepts file based on name
     * @param pathname the pathname of the file to check
     * @return true if the file identified by the pathname matches/not matches
     * this target value based on this fileNameOperator
     */
    @Override
    public boolean accept(Path pathname) {
        
        String name = pathname.getFileName().toString().toLowerCase();
        
        if(FileCriterion.EXT == fileCriterion && name.contains(".")) {
            name = name.substring(name.lastIndexOf(".")+1);
        }
        
        switch(fileNameOperator) {
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
            default: throw new UnsupportedOperationException("Bad op " + fileNameOperator.name());
        }
    }

    /**
     * @return this fileNameOperator
     */
    public FileNameOperator getFileNameOperator() {
        return fileNameOperator;
    }

    /**
     * Sets this fileNameOperator
     * @param op The FileNameOperator to set
     */
    public void setFileNameOperator(FileNameOperator op) {
        this.fileNameOperator = op;
    }

    /**
     * @return this target
     */
    public String getTarget() {
        return target;
    }

    /**
     * Sets this target to the lower case of the input target value
     * @param target The string search target to set 
     */
    public void setTarget(String target) {
        this.target = target.toLowerCase();
    }

}
