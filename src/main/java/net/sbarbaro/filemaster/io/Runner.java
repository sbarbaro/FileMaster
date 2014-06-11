package net.sbarbaro.filemaster.io;

import java.io.IOException;
import static java.nio.file.StandardCopyOption.*;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sbarbaro.filemaster.model.FileAction;
import net.sbarbaro.filemaster.model.FileMaster;
import net.sbarbaro.filemaster.model.LogicalGroupFilter;
import net.sbarbaro.filemaster.model.Rule;

/**
 * Runner
 * <p>
 * Runs all active rules
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net)
 */
public class Runner extends SimpleFileVisitor<Path> {

    private static final Logger LOGGER = Logger.getLogger(Runner.class.getName());

    private final Map<String, List<Rule>> activePathMap;

    // True if the walk is recursive; otherwise false
    private boolean isRecurse;
    // Used with isRecurse to determine if subtrees should be skipped
    private boolean isStartingPath = true;

    // The starting directory
    private Path sourcePath;
    private DirectoryStream.Filter<Path> fileFilter;
    private Rule rule;

    /**
     * Constructor
     *
     * @param fileMaster
     */
    public Runner(FileMaster fileMaster) {
        this.activePathMap = fileMaster.getActivePathMap();
    }

    /**
     * Run all active Rules
     */
    public void run() {

        /*
         Iterate over all of the paths specified by the active sourcePath map
         */
        for (String directoryName : activePathMap.keySet()) {

            sourcePath = Paths.get(directoryName);

            /*
             Iterate over all the rules for the current sourcePath
             */
            for (Rule rule : activePathMap.get(directoryName)) {

                this.rule = rule;

                isRecurse = rule.getFileMonitor(directoryName).isRecurse();
                /*
                 Combine all FileFilters into a LogicalGroupFilter based on
                 the current rule
                 */
                fileFilter = new LogicalGroupFilter(
                        rule.getLogicalGroup(), rule.getFileFilters());

                try {

                    Files.walkFileTree(sourcePath, this);

                } catch (IOException ex) {
                    Logger.getLogger(
                            Rule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Limit non-recursive walks to the starting directory
     *
     * @param dir A directory
     * @param attrs The attributes of a directory
     * @return A FileVisitResult.TERMINATE if this is a non-recursive walk and
     * the walk is about to deviate from the starting directory. Otherwise
     * returns FileVisitResult
     */
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {

        FileVisitResult result = super.preVisitDirectory(dir, attrs);

        if (result.equals(CONTINUE)) {

            if (!isRecurse && !isStartingPath) {
                result = SKIP_SUBTREE;
            }
        }

        isStartingPath = false;

        return result;
    }

    /**
     * Check each regular sourceFile with this FileFilter. Run all FileActions
     * on each matched sourceFile.
     *
     * @param sourceFile the sourceFile to check
     * @param attr the attributes of the sourceFile
     * @return a FileVisitResult.TERMINATE if the number of files accepted so
     * far is about to exceed MAX_ROWS. Otherwise, returns
     * FileVisitResult.CONTINUE.
     */
    @Override
    public FileVisitResult visitFile(Path sourceFile,
            BasicFileAttributes attr) {

        if (attr.isSymbolicLink()) {

            // Skip symlinks
            LOGGER.log(Level.FINE, "Symbolic link: {0}", sourceFile.toString());

        } else if (attr.isDirectory()) {

            // Skip directories
            LOGGER.log(Level.FINE, "Directory: {0}", sourceFile.toString());

        } else if (attr.isRegularFile()) {

            try {
                // Try to match each sourceFile according to combined FileFilter criteria
                LOGGER.log(Level.FINE, "Regular file: {0}", sourceFile.toString());

                if (fileFilter.accept(sourceFile)) {

                    LOGGER.log(Level.FINE, "Accept file: {0}", sourceFile.toString());

                    for (FileAction fileAction : rule.getFileActions()) {

                        // Get the path for the destination directory
                        Path destinationPath
                                = Paths.get(fileAction.getDestinationPathname());

                        // Create the destination directory if it doesn't exist
                        if (!Files.exists(destinationPath)) {
                            Files.createDirectories(destinationPath);
                        }

                        // Append the file to the destination path                        
                        destinationPath
                                = Paths.get(destinationPath.toString(),
                                        sourceFile.getFileName().toString());

                        Logger logger = Logger.getLogger(rule.getDescription());
                        switch (fileAction.getFileAction()) {
                            case COPY:
                                Files.copy(sourceFile, destinationPath,
                                        REPLACE_EXISTING, COPY_ATTRIBUTES
                                );
                                logger.log(Level.INFO, "Copied "
                                        + sourceFile.getFileName().toString()
                                        + " to " + destinationPath.toString(),
                                        Runner.class);
                                break;
                            case MOVE:
                            case RENAME:
                                logger.log(Level.INFO, "Moved "
                                        + sourceFile.getFileName().toString()
                                        + " to " + destinationPath.toString(),
                                        Runner.class);
                                Files.move(sourceFile, destinationPath, REPLACE_EXISTING);
                                break;
                            case DELETE:
                                logger.log(Level.INFO, "Deleted "
                                        + sourceFile.getFileName().toString()
                                        + " from " + sourceFile.getParent().toString(),
                                        Runner.class);
                                Files.delete(sourceFile);
                                break;
                            default:
                                throw new UnsupportedOperationException("Bad FileAction " + fileAction);
                        }

                    }

                }

            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        } else {
            LOGGER.log(Level.FINE, "Other: {0}", sourceFile.toString());
        }
        LOGGER.log(Level.FINE, "({0} bytes)", attr.size());

        return CONTINUE;

    }
}
