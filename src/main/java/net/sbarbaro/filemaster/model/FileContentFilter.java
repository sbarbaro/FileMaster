package net.sbarbaro.filemaster.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sbarbaro.filemaster.io.*;

/**
 * FileContentFilter
 * <p>
 * Searches the contents of a Document file for a case-insensitive word or
 * phrase
 * <p>
 * {Other Notes Relating to This Class (Optional)}
 *
 * @author Anthony J. Barbaro (tony@abarbaro.net) $LastChangedRevision: $
 * $LastChangedDate: $
 */
public class FileContentFilter extends FileTypeFilter implements Serializable {

    private static final long serialVersionUID = -6799295657477975712L;

    private final String searchTerm;
    private final int minimumHits;

    /**
     * Default constructor
     */
    public FileContentFilter() {
        this("", 0);
    }

    /**
     * Constructor
     *
     * @param searchTerm The text string to search within the file
     * @param minimumHits The minimum number of times that the search term must
     * be present in the file to satisfy the search
     */
    public FileContentFilter(String searchTerm, int minimumHits) {
        super(FileType.Document);
        this.searchTerm = searchTerm;
        this.minimumHits = minimumHits;

    }

    /**
     * Copy constructor
     *
     * @param fcf
     */
    public FileContentFilter(FileContentFilter fcf) {
        this.searchTerm = fcf.searchTerm;
        this.minimumHits = fcf.minimumHits;

    }

    @Override
    public boolean accept(Path pathIn) {

        // Consider only FileType.Document files
        boolean accept = super.accept(pathIn);
        int hits = 0;

        if (accept) {

            if (pathIn.toString().toLowerCase().endsWith("pdf")) {

                String text = PDFTextParser.pdftoText(pathIn.toString());
                
                if(KMPMatch.indexOf(text.getBytes(), getBytePattern()) > 0) {
                    hits = 1;
                }
                
            } else {

                byte[] bytes = new byte[80];

                int offset = 0;
                try (InputStream inputStream = Files.newInputStream(pathIn)) {

                    while (inputStream.read(bytes, offset, bytes.length) > 0
                            && hits < minimumHits) {

                        if (KMPMatch.indexOf(bytes, getBytePattern()) >= 0) {

                            hits++;
                        }

                    }

                    inputStream.close();

                } catch (IOException ex) {
                    Logger.getLogger(FileContentFilter.class.getName()).log(Level.SEVERE, pathIn.toString(), ex);
                }
            }
        }

        return hits >= minimumHits;
    }

    public int getMinimumHits() {
        return minimumHits;
    }

    public byte[] getBytePattern() {
        return searchTerm.getBytes();
    }

    public String getSearchTerm() {
        return searchTerm;
    }

}
