package net.sbarbaro.filemaster.model;

import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;

/**
* FileFilterCriterion
* <p>
* A single FileFilter implementation may support more than one FileCriterion.
* This class associates a FileFilter with a single FileCriterion.
* <p>
* @author Anthony J. Barbaro (tony@abarbaro.net)
*/
public class FileFilterCriterion implements Serializable {
    
    private static final long serialVersionUID = -8166989630618651192L;
    
    private FileCriterion fileCriterion;
    private DirectoryStream.Filter<Path> fileFilter;

    /**
     * Default constructor
     */
    public FileFilterCriterion() {
        this(FileCriterion.EXT);
    }
    /**
     * Constructor
     * @param c 
     */
    public FileFilterCriterion(FileCriterion c) {
        setCriterion(c);
    }
    /**
     * Constructor
     * @param criterion
     * @param filter 
     */
    public FileFilterCriterion(FileCriterion criterion, DirectoryStream.Filter<Path> filter) {
        this.fileCriterion = criterion;
        this.fileFilter = filter;
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
            case TYPE:
                fileFilter = new FileTypeFilter((FileTypeFilter) c.getFilter());
                break;
            case CONTENTS:
                fileFilter = new FileContentFilter((FileContentFilter) c.getFilter());
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
  

    public DirectoryStream.Filter<Path> getFilter() {
        return fileFilter;
    }

    public void setFilter(DirectoryStream.Filter<Path>  filter) {
        this.fileFilter = filter;
    }
    
}
