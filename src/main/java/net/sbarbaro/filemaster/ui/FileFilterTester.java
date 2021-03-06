package net.sbarbaro.filemaster.ui;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.SKIP_SUBTREE;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 * FileFilterTester
 * <p>
 * Walks one or more file system directory specified in a rule, recursively or
 * not, and attempts to match discovered files based on criteria captured in the
 * specified FileFilter. Adds accepted files to a table model. Continues until a
 * preset limit of files have been accepted, or until there are no more files to
 * explore.
 * <p>
 * Usage:<br>
 * Construct a walker <br>
 * Files.walkFileTree(startingDir, walker);
 * <p>
 * @author Steven A. Barbaro (steven@abarbaro.net)
 */
public class FileFilterTester extends SimpleFileVisitor<Path> {

    private static final Logger LOGGER = Logger.getLogger(FileFilterTester.class.getName());
    // The string format of the file modification date column
    private static final DateFormat DF = new SimpleDateFormat("dd-MMM-yy  hh:mm:ss");
    // The file filter that implements the file acceptance criteria
    private final DirectoryStream.Filter<Path> fileFilter;
    // The table model to be updated with hits.
    private final DefaultTableModel model;
    // The maximum number of hits allowed.
    private final int maxRows;
    // The current number of hits
    private int rowCount;
    // True if the walk is recursive; otherwise false
    private final boolean isRecurse;
    // Used with isRecurse to determine if subtrees should be skipped
    private boolean isStartingPath = true;

    /**
     * Constructor
     *
     * @param fileFilter The FileFilter that implements the acceptance criteria
     * @param model The table model to be updated with hits
     * @param maxRows The maximum numbers of hits to allow. This is intended to
     * maintain a reasonable response time
     * @param isRecurse
     */
    public FileFilterTester(DirectoryStream.Filter<Path> fileFilter,
            DefaultTableModel model, int maxRows, boolean isRecurse) {
        this.fileFilter = fileFilter;
        this.maxRows = maxRows;
        this.model = model;
        this.rowCount = 0;
        this.isRecurse = isRecurse;
    }

    /**
     * Check each regular file with this FileFilter. Add each acceptable file to
     * this model.
     *
     * @param file the file to check
     * @param attr the attributes of the file
     * @return a FileVisitResult.TERMINATE if the number of files accepted so
     * far is about to exceed MAX_ROWS. Otherwise, returns
     * FileVisitResult.CONTINUE.
     */
    @Override
    public FileVisitResult visitFile(Path file,
            BasicFileAttributes attr) {

        if (attr.isSymbolicLink()) {

            // Skip symlinks
            LOGGER.log(Level.FINE, "Symbolic link: {0}", file.toString());

        } else if (attr.isDirectory()) {

            // Skip directories
            LOGGER.log(Level.FINE, "Directory: {0}", file.toString());

        } else if (attr.isRegularFile()) {

            try {
                // Try to match each file according to combined FileFilter criteria
                LOGGER.log(Level.FINE, "Regular file: {0}", file.toString());

                if (fileFilter.accept(file) && rowCount < maxRows) {
                    LOGGER.log(Level.FINE, "Accept file: {0}", file.toString());
                    model.addRow(
                            new Object[]{String.format("%,d", attr.size()),
                                DF.format(new Date(attr.lastModifiedTime().toMillis())),
                                file.getFileName(),
                                file.getParent()});
                    model.setRowCount(++rowCount);

                } else if (rowCount >= maxRows) {

                    // Limit the number of hits returned to the UI
                    LOGGER.log(Level.FINE, "Limit reached on file: {0}", file.toString());
                    return FileVisitResult.TERMINATE;
                }
            } catch (IOException ex) {
                Logger.getLogger(FileFilterTester.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            LOGGER.log(Level.FINE, "Other: {0}", file.toString());
        }
        LOGGER.log(Level.FINE, "({0} bytes)", attr.size());

        return CONTINUE;
    }

    /**
     * Limit non-recursive walks to the starting directory
     *
     * @param dir A directory
     * @param attrs The attributes of a directory
     * @return A FileVisitResult.TERMINATE if this is a non-recursive walk and
     * the walk is about to deviate from the starting directory. Otherwise
     * returns FileVisitResult
     * @throws java.io.IOException
     */
    @Override
    public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs) throws IOException {

        FileVisitResult result = super.preVisitDirectory(dir, attrs);

        if (result.equals(CONTINUE)) {

            if (!isRecurse && !isStartingPath) {
                result = SKIP_SUBTREE;
            }
        }

        isStartingPath = false;

        return result;
    }

}
