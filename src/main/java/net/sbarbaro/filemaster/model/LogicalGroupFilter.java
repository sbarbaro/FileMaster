package net.sbarbaro.filemaster.model;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * LogicalGroupFilter
 * <p>
 * An aggregate FileFilter that ANDs or ORs a logicalGroup of FileFilters
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net)
 */
public class LogicalGroupFilter implements DirectoryStream.Filter<Path>, Serializable {

    private static final long serialVersionUID = 7156726869221129816L;

    // "AND" or "OR"
    private final LogicalGroup logicalGroup;
    private final List<DirectoryStream.Filter<Path>> filters;

    /**
     * Constructor
     * @param group The LogicalGroup enumeration, "AND" or "OR" to apply to 
 all filters in this logicalGroup.
     */
    public LogicalGroupFilter(LogicalGroup group) {
        this(group, new ArrayList<>());
    }

    /**
     * Constructor
     * @param group The LogicalGroup enumeration, "AND" or "OR" to apply to 
 all filters in this logicalGroup.
     * @param filters The file filters that form this logicalGroup
     */
    public LogicalGroupFilter(LogicalGroup group,
            List<DirectoryStream.Filter<Path>> filters) {
        this.filters = filters;
        this.logicalGroup = group;
    }

    /**
     * Determines if the file specified by the input Path satisfy one or more 
 (OR) or all (AND) the filters defined for this logicalGroup.
     * <p>
     * @param pathIn The path of the file to test
     * @return true if the file satisfies the criteria.
     */
    @Override
    public boolean accept(Path pathIn) {

        boolean result = false;

        switch (logicalGroup) {

            case AND:

                result = true;

                for (int i = 0; i < filters.size() && result; i++) {

                    try {
                        result = filters.get(i).accept(pathIn);
                    } catch (IOException ex) {
                        Logger.getLogger(LogicalGroupFilter.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                break;
            case OR:

                result = false;

                for (int i = 0; i < filters.size() && !result; i++) {

                    try {
                        result = filters.get(i).accept(pathIn);
                    } catch (IOException ex) {
                        Logger.getLogger(LogicalGroupFilter.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                break;
            default:
                throw new UnsupportedOperationException(
                        "Bad group " + logicalGroup.name());
        }

        return result;
    }

    /**
     * @return The AND or OR binary operation to apply to file filters
     * of this group.
     */
    public LogicalGroup getLogicalGroup() {
        return logicalGroup;
    }

    public List<DirectoryStream.Filter<Path>> getFilters() {
        return filters;
    }

}
