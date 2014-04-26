package net.sbarbaro.filemaster.model;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;

/**
* FileTypeFilter
* <p>
* FileFilter implementation used to select files based on general file type
* @author Anthony J. Barbaro (tony@abarbaro.net)
* $LastChangedRevision: $
* $LastChangedDate: $
*/
public class FileTypeFilter implements FileFilter, Serializable {
    
    private static final long serialVersionUID = 4216782757984465913L;

    private FileType type;
    
    public FileTypeFilter() {
        this(FileType.Document);
    }
    public FileTypeFilter(FileType type) {
        this.type = type;
    }

    @Override
    public boolean accept(File pathname) {
        
        for(String ext : type.types) {
            if(pathname.getName().toLowerCase().endsWith(ext)) {
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
