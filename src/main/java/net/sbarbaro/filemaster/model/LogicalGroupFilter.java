package net.sbarbaro.filemaster.model;

import java.io.File;
import java.io.FileFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * LogicalGroupFilter
 * <p>
 * An aggregate FileFilter that ANDs or ORs a group of FileFilters
 *
 * @author Anthony J. Barbaro (tony@abarbaro.net) $LastChangedRevision: $
 * $LastChangedDate: $
 */
public class LogicalGroupFilter implements FileFilter, Serializable {
    
    private static final long serialVersionUID = 7156726869221129816L;

    private final LogicalGroup group;
    private final List<FileFilter> filters;

    public LogicalGroupFilter(LogicalGroup group) {
        this(group, new ArrayList<FileFilter>());
    }

    public LogicalGroupFilter(LogicalGroup group, List<FileFilter> filters) {
        this.filters = filters;
        this.group = group;
    }

    public boolean accept(File pathname) {

        boolean result = false;

        switch (group) {

            case AND:

                result = true;

                for (int i = 0; i < filters.size() && result; i++) {

                    result = filters.get(i).accept(pathname);

                }
                break;
            case OR:

                result = false;

                for (int i = 0; i < filters.size() && !result; i++) {

                    result = filters.get(i).accept(pathname);

                }
                break;
            default:
                throw new UnsupportedOperationException(
                        "Bad group " + group.name());
        }

        return result;
    }

    public LogicalGroup getGroup() {
        return group;
    }

    public List<FileFilter> getFilters() {
        return filters;
    }

}
