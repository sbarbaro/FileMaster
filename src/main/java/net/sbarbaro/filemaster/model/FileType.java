package net.sbarbaro.filemaster.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An enumeration of all file types supported by FileMaster
 *  <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net)
 */
public enum FileType {
    /**
     * Root type
     */
    Root(null),
    /**
     * Video files
     */
    Video(Root),
    mov(Video),
    mkv(Video),
    avi(Video),
    mxf(Video),
    flv(Video),
    mp4(Video),
    m4v(Video),
    asf(Video),
    asx(Video),
    ts(Video),
    wmv(Video),
    /**
     * Audio
     */
    Audio(Root),
    acc(Audio),
    mp3(Audio),
    wav(Audio),
    ogg(Audio),
    ac3(Audio),
    m4a(Audio),
    /**
     * Document
     */
    Document(Root),
    doc(Document),
    docx(Document),
    ppt(Document),
    pptx(Document),
    xls(Document),
    xlsx(Document),
    pages(Document),
    numbers(Document),
    keynote(Document),
    txt(Document),
    rtf(Document),
    pdf(Document),
    /**
     * Development
     */
    Development(Root),
    java(Development),
    jar(Development),
    htm(Development),
    html(Development),
    xhtml(Development),
    css(Development),
    php(Development),
    /**
     * Image
     */
    Image(Root),
    img(Image),
    jpeg(Image),
    tiff(Image),
    bmp(Image),
    jpg(Image),
    gif(Image),
    raw(Image),
    nef(Image),
    /**
     * Compressed
     */
    Compressed(Root),
    z(Compressed),
    zip(Compressed),
    tar(Compressed),
    gz(Compressed),
    gzip(Compressed),
    rar(Compressed),
    _7z(Compressed),
    tgz(Compressed),
    /**
     * Disk Image
     */
    DiskImage(Root),
    dmg(DiskImage),
    iso(DiskImage),
    sparsedisk(DiskImage),
    sparseimage(DiskImage),
    /**
     * Application
     */
    Application(Root),
    exe(Application),
    app(Application),
    dll(Application),
    msi(Application),
    deb(Application),
    ini(Application),
    cmd(Application),
    bat(Application);

    /**
     * @return The parent FileType, or Root if no more parents
     */
    public FileType getParent() {
        return parent;
    }

    /**
     * @return all child FileType for this FileType enum value
     */
    public FileType[] getChildren() {

        return children.toArray(new FileType[0]);

    }
    
    // Private constructor
    private FileType(FileType parent) {
        this.parent = parent;
        this.children = new ArrayList<>();
        if (null == parent) {

        } else {
            parent.addChild(this);
        }
    }

    // Adds the given file type as a child to this FileType enum
    private void addChild(FileType fileType) {
        this.children.add(fileType);
    }
    /*
     Parent enum
     */
    private final FileType parent;

    /*
     Child enums
    */
    private final List<FileType> children;
}
