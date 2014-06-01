package net.sbarbaro.filemaster.model;

import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Rule
 * <p>
 * Identifies the file system paths, the file selection criteria and the
 * fileActions to apply to the selected files.
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net)
 */
public class Rule implements Serializable {

    private static final long serialVersionUID = 8339905057970397202L;

    private String description;
    private boolean active;
    private final List<FileMonitor> fileMonitors;
    private LogicalGroup logicalGroup;
    private final List<FileFilterCriterion> fileFilterCriteria;
    private final List<FileAction> fileActions;

    /**
     * Default constructor
     */
    public Rule() {

        // Use LinkedHashSet to keep monitors in same order as they were added
        this.fileMonitors = new ArrayList<>();
        this.fileFilterCriteria = new ArrayList<>();
        this.fileActions = new ArrayList<>();
        this.logicalGroup = LogicalGroup.OR;
    }

    /**
     * Copy constructor
     *
     * @param rule The rule to copy
     */
    public Rule(Rule rule) {

        // Copy FileMonitors
        this.fileMonitors = new ArrayList<>();
        for (FileMonitor fileMonitorOrig : rule.getFileMonitors()) {
            FileMonitor fileMonitorCopy = new FileMonitor(fileMonitorOrig);
            fileMonitors.add(fileMonitorCopy);
        }

        // Copy FileFilterCriteria
        this.fileFilterCriteria = new ArrayList<>();
        for (FileFilterCriterion fileFilterCriterionOrig : rule.getFileFilterCriteria()) {
            FileFilterCriterion fileFilterCriterionNew
                    = new FileFilterCriterion(fileFilterCriterionOrig);
            fileFilterCriteria.add(fileFilterCriterionNew);
        }

        // Copy FileActions
        this.fileActions = new ArrayList<>();
        for (FileAction fileActionOrig : rule.getFileActions()) {
            FileAction fileActionNew = new FileAction(fileActionOrig);
            fileActions.add(fileActionNew);
        }

        // Copy the logical group
        this.logicalGroup = rule.getLogicalGroup();

    }

    /**
     * @return this Rule description 
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for this Rule
     * @param description The description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return true if this Rule is active; otherwise false.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Sets this Rule to active
     * @param active true is active; false is inactive
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return All the FileMonitors (directories) monitored by this Rule. 
     */
    public List<FileMonitor> getFileMonitors() {
        return fileMonitors;
    }

    /**
     * Gets the FileMonitor corresponding with the given directory name
     * @param directoryName The name of the desired directory
     * @return The FileMonitor corresponding with the directoryName.
     */
    public FileMonitor getFileMonitor(String directoryName) {

        FileMonitor result = null;

        for (FileMonitor fileMonitor : getFileMonitors()) {
            if (fileMonitor.getDirectoryName().equals(directoryName)) {
                result = fileMonitor;
                break;
            }
        }
        return result;
    }

    /**
     * @return The FileFilterCriteria belonging to this Rule
     */
    public List<FileFilterCriterion> getFileFilterCriteria() {
        return fileFilterCriteria;
    }

    /**
     * 
     * @return The FileActions belonging to this Rule
     */
    public List<FileAction> getFileActions() {
        return fileActions;
    }

    /**
     * @return  LogicalGroup enumerated value to apply to all FileFilter criteria
     * specified by this Rule

     */
    public LogicalGroup getLogicalGroup() {
        return logicalGroup;
    }

    /**
     * Set the LogicalGroup value for this Rule
     * @param logicalGroup The LogicalGroup enumerated value to set
     */
    public void setLogicalGroup(LogicalGroup logicalGroup) {
        this.logicalGroup = logicalGroup;
    }

    /**
     * @return A List of all the FileFilters defined by this Rule
     */
    public List<DirectoryStream.Filter<Path>> getFileFilters() {

        List<DirectoryStream.Filter<Path>> fileFilters = new ArrayList<>();

        for (FileFilterCriterion condition : getFileFilterCriteria()) {

            fileFilters.add(condition.getFilter());

        }
        return fileFilters;
    }

}
