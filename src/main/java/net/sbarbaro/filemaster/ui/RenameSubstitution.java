package net.sbarbaro.filemaster.ui;

 /**
  *
  * RenameSubstitution
  * <p>
  * Substitution variable names used to configure FileAction.RENAME actions
  * @author Anthony J. Barbaro (tony@abarbaro.net)
  * */
public enum RenameSubstitution {
    /**
     * The extension part of the filename
     */
    EXTENSION,
    /**
     * The filename.  Will include the extension if the EXTENSION is not
     * separately specified by the FileActionFilter
     */
    FILENAME,
    /**
     * Add date stamp to include in the filename
     */
    YYYYMMDD,
    /**
     * Initial substitution value
     */
    RESET
}
