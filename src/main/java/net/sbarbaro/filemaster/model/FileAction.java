package net.sbarbaro.filemaster.model;

import java.io.Serializable;

/**
 * FileAction
 * <p>
 * {Purpose of This Class}
 * <p>
 * {Other Notes Relating to This Class (Optional)}
 *
 * @author Anthony J. Barbaro (tony@abarbaro.net) $LastChangedRevision: $
 * $LastChangedDate: $
 */
public class FileAction implements Serializable {

    private static final long serialVersionUID = -5874285456737185974L;

    private FileActionOperator fileAction;
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
        this.fileAction = op;
        this.destinationPathname = destinationPathname;
    }

    public FileAction(FileAction fileAction) {
        this.fileAction = fileAction.getFileAction();
        this.destinationPathname = fileAction.destinationPathname;
    }

    public FileActionOperator getFileAction() {
        return fileAction;
    }

    public void setFileAction(FileActionOperator fileAction) {
        this.fileAction = fileAction;
    }

    public String getDestinationPathname() {
        return destinationPathname;
    }

    public void setDestinationPathname(String destinationPathname) {
        this.destinationPathname = destinationPathname;
    }

}
