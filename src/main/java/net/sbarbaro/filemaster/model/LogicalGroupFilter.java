package net.sbarbaro.filemaster.model;

import java.io.FileFilter;
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
 * An aggregate FileFilter that ANDs or ORs a group of FileFilters
 *
 * @author Anthony J. Barbaro (tony@abarbaro.net) $LastChangedRevision: $
 * $LastChangedDate: $
 */
public class LogicalGroupFilter implements DirectoryStream.Filter<Path>, Serializable {

    private static final long serialVersionUID = 7156726869221129816L;

    private final LogicalGroup group;
    private final List<DirectoryStream.Filter<Path>> filters;

    public LogicalGroupFilter(LogicalGroup group) {
        this(group, new ArrayList<DirectoryStream.Filter<Path>>());
    }

    public LogicalGroupFilter(LogicalGroup group,
            List<DirectoryStream.Filter<Path>> filters) {
        this.filters = filters;
        this.group = group;
    }

    @Override
    public boolean accept(Path pathIn) {

        boolean result = false;

        switch (group) {

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
                        "Bad group " + group.name());
        }

        return result;
    }

    public LogicalGroup getGroup() {
        return group;
    }

    public List<DirectoryStream.Filter<Path>> getFilters() {
        return filters;
    }

}
