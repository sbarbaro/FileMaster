package net.sbarbaro.filemaster.model;

import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;

/**
 * FileTypeFilter
 * <p>
 * FileFilter implementation used to select files based on general file fileType
 * @author Steven A. Barbaro (steven@abarbaro.net)
 */
public class FileTypeFilter implements DirectoryStream.Filter<Path>, Serializable {

    private static final long serialVersionUID = 4216782757984465913L;

    private FileType fileType;

    /**
     * Default constructor
     */
    public FileTypeFilter() {
        this(FileType.Document);
    }

    /**
     * Constructor
     *
     * @param type
     */
    public FileTypeFilter(FileType type) {
        this.fileType = type;
    }

    /**
     * Copy constructor
     *
     * @param fileTypeFilter
     */
    public FileTypeFilter(FileTypeFilter fileTypeFilter) {
        this.fileType = fileTypeFilter.fileType;
    }

    /**
     * Accepts the file specified by the input Path based on this FileType
     * <p>
     * @param pathIn The path of the file to check
     * @return true if the fileType of the input file matches this fileType;
 otherwise returns false.
     */
    @Override
    public boolean accept(Path pathIn) {

        for (FileType childType : fileType.getChildren()) {
            if (pathIn.getFileName().toString().toLowerCase().endsWith(
                    childType.name())) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return this fileType
     */
    public FileType getFileType() {
        return fileType;
    }

    /**
     * Sets this fileType
     * @param fileType The fileType to set
     */
    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

}
