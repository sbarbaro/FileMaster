package net.sbarbaro.filemaster.model;

import java.io.FileFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Rule
 * <p>
 Identifies the file system paths, the file selection criteria and the fileActions
 to apply to the selected files.
 <p>
 *
 * @author sab $LastChangedRevision: $
 * $LastChangedDate: $
 */
public class Rule implements Serializable {
    
    private static final long serialVersionUID = 8339905057970397202L;

    private String description;
    private boolean active;
    private final Set<FileMonitor> fileMonitors;
    private LogicalGroup logicalGroup;
    private final List<FileFilterCriterion> fileFilterCriteria;
    private final List<FileAction> fileActions;

    /**
     * Default constructor
     */
    public Rule() {
        
        // Use LinkedHashSet to keep monitors in same order as they were added
        this.fileMonitors = new LinkedHashSet<>();
        this.fileFilterCriteria = new ArrayList<>();
        this.fileActions = new ArrayList<>();
        this.logicalGroup = LogicalGroup.OR;
    }
    /**
     * Copy constructor
     * @param rule The rule to copy 
     */
    public Rule(Rule rule) {
        
        // Copy FileMonitors
        
        this.fileMonitors = new LinkedHashSet<>();
        for(FileMonitor fileMonitorOrig : rule.getFileMonitors()) {
           FileMonitor fileMonitorCopy  = new FileMonitor(fileMonitorOrig);
           fileMonitors.add(fileMonitorCopy);
        }
        
        // Copy FileFilterCriteria
        this.fileFilterCriteria = new ArrayList<>();
        for(FileFilterCriterion fileFilterCriterionOrig : rule.getFileFilterCriteria()) {
            FileFilterCriterion fileFilterCriterionNew = 
                    new FileFilterCriterion(fileFilterCriterionOrig);
            fileFilterCriteria.add(fileFilterCriterionNew);
        }
        
        // Copy FileActions
        this.fileActions = new ArrayList<>();
        for(FileAction fileActionOrig : rule.getFileActions()) {
            FileAction fileActionNew = new FileAction(fileActionOrig);
            fileActions.add(fileActionNew);
        }
        
        // Copy the logical group
        this.logicalGroup = rule.getLogicalGroup();
        
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<FileMonitor> getFileMonitors() {
        return fileMonitors;
    }

    public List<FileFilterCriterion> getFileFilterCriteria() {
        return fileFilterCriteria;
    }

    public List<FileAction> getFileActions() {
        return fileActions;
    }

    public LogicalGroup getLogicalGroup() {
        return logicalGroup;
    }

    public void setLogicalGroup(LogicalGroup logicalGroup) {
        this.logicalGroup = logicalGroup;
    }

    /**
     * @return A List of all the FileFilters defined by this Rule
     */
    public List<FileFilter> getFileFilters() {

        List<FileFilter> fileFilters = new ArrayList<>();

        for (FileFilterCriterion condition : getFileFilterCriteria()) {

            fileFilters.add(condition.getFilter());

        }
        return fileFilters;
    }

}
