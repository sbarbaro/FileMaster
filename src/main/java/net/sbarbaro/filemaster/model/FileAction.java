package net.sbarbaro.filemaster.model;

import java.io.Serializable;

/**
 * FileAction
 * <p>
 * The FileActionOperator to perform on all files contained in a destination
 * path
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net) 
 */
public class FileAction implements Serializable {

    private static final long serialVersionUID = -5874285456737185974L;

    private FileActionOperator fileActionOperator;
    private String destinationPathname;

    /**
     * Default constructor
     */
    public FileAction() {
        this(FileActionOperator.COPY);
    }

    /**
     * Constructor
     *
     * @param op
     */
    public FileAction(FileActionOperator op) {
        this(op, null);
    }

    /**
     * Constructor
     *
     * @param op
     * @param destinationPathname
     */
    public FileAction(FileActionOperator op, String destinationPathname) {
        this.fileActionOperator = op;
        this.destinationPathname = destinationPathname;
    }

    /**
     * Copy constructor
     *
     * @param fileAction
     */
    public FileAction(FileAction fileAction) {
        this.fileActionOperator = fileAction.getFileAction();
        this.destinationPathname = fileAction.destinationPathname;
    }

    /**
     * Gets the FileActionOperator for this FileAction
     *
     * @return
     */
    public FileActionOperator getFileAction() {
        return fileActionOperator;
    }

    /**
     * Sets the FileActionOperator for this FileAction
     *
     * @param fileActionOperator
     */
    public void setFileAction(FileActionOperator fileActionOperator) {
        this.fileActionOperator = fileActionOperator;
    }

    /**
     * Gets the destinationPathname for this FileAction
     *
     * @return
     */
    public String getDestinationDirectoryName() {
        return destinationPathname;
    }

    /**
     * Sets the destinationPathname for this FileAction
     *
     * @param destinationPathname
     */
    public void setDestinationPathname(String destinationPathname) {
        this.destinationPathname = destinationPathname;
    }

}
