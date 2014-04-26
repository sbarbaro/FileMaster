/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sbarbaro.filemaster.model;

/**
 *
 * @author ajb
 */
public enum FileType {
    
    Video(new String[] {"mov", "mkv", "avi", "mxf", "flv", "mp4", "m4v", "asf", "asx", "ts", "wmv"}),
    Audio(new String[] {"acc", "mp3", "wav", "ogg", "ac3", "m4a"}),
    Document(new String[]{"doc", "docx", "ppt", "pptx", "xls", "xlsx", "pages", "numbers", "keynote", "txt", "rtf", "pdf"}),
    Development(new String[] {"java", "jar", "class", "htm", "html", "css", "php"}),
    Image(new String[]{"img", "jpeg", "tiff", "bmp", "jpg", "gif", "raw", "nef"}),
    Compressed(new String[]{"z", "zip", "tar", "gz", "gzip", "rar", "7z", "tgz"}),
    DiskImage(new String[]{"dmg", "iso", "sparsedisk", "sparseimage"}),
    Application(new String[]{"exe", "app", "dll", "msi", "deb", "ini", "cmd", "bat"})
    
    ;

    
    private FileType(String[] types) {
        this.types = types;
    }
    
    public final String[] types;
}
