package net.sbarbaro.filemaster.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ajb
 */
public enum FileType {

    /**
     * Video
     */
    Video(null),
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
    Audio(null),
    acc(Audio),
    mp3(Audio),
    wav(Audio),
    ogg(Audio),
    ac3(Audio),
    m4a(Audio),
    /**
     * Document
     */
    Document(null),
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
    Development(null),
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
    Image(null),
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
    Compressed(null),
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
    DiskImage(null),
    dmg(DiskImage),
    iso(DiskImage),
    sparsedisk(DiskImage),
    sparseimage(DiskImage),
    /**
     * Application
     */
    Application(null),
    exe(Application),
    app(Application),
    dll(Application),
    msi(Application),
    deb(Application),
    ini(Application),
    cmd(Application),
    bat(Application);

    /*
     Constructor
     */
    private FileType(FileType parent) {
        this.parent = parent;
        this.children = new ArrayList<>();
        if (null == parent) {

        } else {
            parent.addChild(this);
        }
    }

    public FileType getParent() {
        return parent;
    }

    public FileType[] getChildren() {

        return children.toArray(new FileType[0]);

    }

    private void addChild(FileType fileType) {
        this.children.add(fileType);
    }
    /*
     Parent enum
     */
    private final FileType parent;

    private final List<FileType> children;
}
