package net.sbarbaro.filemaster.model;

import java.io.FileFilter;
import java.io.Serializable;

/**
* FileFilterCriterion
* <p>
* A single FileFilter implementation may support more than one FileCriterion.
* This class associates a FileFilter with a single FileCriterion.
* <p>
* @author Anthony J. Barbaro (tony@abarbaro.net)
* $LastChangedRevision: $
* $LastChangedDate: $
*/
public class FileFilterCriterion implements Serializable {
    
    private static final long serialVersionUID = -8166989630618651192L;
    
    private FileCriterion fileCriterion;
    private FileFilter fileFilter;

    /**
     * Constructor
     * @param c 
     */
    public FileFilterCriterion(FileCriterion c) {
        setCriterion(c);
    }
    /**
     * Constructor
     * @param c
     * @param f 
     */
    public FileFilterCriterion(FileCriterion c, FileFilter f) {
        this.fileCriterion = c;
        this.fileFilter = f;
    }
    /**
     * Copy constructor
     * @param c The FileFilterCriterion instance to copy
     */
    public FileFilterCriterion(FileFilterCriterion c) {
        this.fileCriterion = c.fileCriterion;
        switch (fileCriterion) {
            case NAME:
            case EXT:
                fileFilter = new FileNameFilter((FileNameFilter) c.getFilter());
                break;
            case SIZE:
                fileFilter = new FileSizeFilter((FileSizeFilter) c.getFilter());
                break;
            case CREATED:
            case MODIFED:
            case ACCESSED:
                fileFilter = new FileAgeFilter((FileAgeFilter) c.getFilter());
                break;
            case IMAGE_ASPECT_RATIO:
                fileFilter = 
                        new ImageAspectRatioFilter((ImageAspectRatioFilter) c.getFilter());
                break;
            default:
                throw new UnsupportedOperationException("Bad criterion " + fileCriterion);
        }
    }
    
    

    public FileCriterion getCriterion() {
        return fileCriterion;
    }

    public void setCriterion(FileCriterion fileCriterion) {
        this.fileCriterion = fileCriterion;
        switch (fileCriterion) {
            case NAME:
            case EXT:
                fileFilter = new FileNameFilter();
                break;
            case SIZE:
                fileFilter = new FileSizeFilter();
                break;
            case CREATED:
            case MODIFED:
            case ACCESSED:
                fileFilter = new FileAgeFilter();
                break;
            case IMAGE_ASPECT_RATIO:
                fileFilter = new ImageAspectRatioFilter();
            default:
                throw new UnsupportedOperationException("Bad criterion " + fileCriterion);
        }
    }
  

    public FileFilter getFilter() {
        return fileFilter;
    }

    public void setFilter(FileFilter filter) {
        this.fileFilter = filter;
    }
    
}
