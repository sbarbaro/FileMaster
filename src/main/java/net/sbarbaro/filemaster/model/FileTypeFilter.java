package net.sbarbaro.filemaster.model;

import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;

/**
 * FileTypeFilter
 * <p>
 * FileFilter implementation used to select files based on general file type
 *
 * @author Anthony J. Barbaro (tony@abarbaro.net) $LastChangedRevision: $
 * $LastChangedDate: $
 */
public class FileTypeFilter implements DirectoryStream.Filter<Path>, Serializable {

    private static final long serialVersionUID = 4216782757984465913L;

    private FileType type;

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
        this.type = type;
    }

    /**
     * Copy constructor
     *
     * @param fileTypeFilter
     */
    public FileTypeFilter(FileTypeFilter fileTypeFilter) {
        this.type = fileTypeFilter.type;
    }

    @Override
    public boolean accept(Path pathIn) {

        for (FileType childType : type.getChildren()) {
            if (pathIn.getFileName().toString().toLowerCase().endsWith(
                    childType.name())) {
                return true;
            }
        }

        return false;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

}
