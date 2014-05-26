package net.sbarbaro.filemaster.model;

import java.io.ByteArrayInputStream;
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
 * Filters through conduct of a case-insensitive search of the content of 
 * a Document file
 * <p>
 *
 * @author Anthony J. Barbaro (tony@abarbaro.net)
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

    /**
     * Accept the pathIn if its contents contains a least this.minimumHits
     * occurrences of this.searchTerm
     * @param pathIn the Path of the file to test
     * @return true if the contents of the file meets the minimum criteria.
     */
    @Override
    public boolean accept(Path pathIn) {

        // Consider only FileType.Document files
        boolean accept = super.accept(pathIn);
        int hits = 0;

        if (accept) {

            try {
                
                /*
                Get an input stream of the file contents based on whether
                the file is a PDF, or not.
                */
                InputStream inputStream = null;

                if (pathIn.toString().toLowerCase().endsWith(FileType.pdf.name())) {

                    String text = PDFTextParser.pdftoText(pathIn.toString());

                    inputStream = new ByteArrayInputStream(text.getBytes());

                } else {

                    inputStream = Files.newInputStream(pathIn);

                }

                /**
                 * Try to locate the desired pattern in the inputStream
                 */
                int offset = 0;

                byte[] bytes = new byte[80];

                while (inputStream.read(bytes, offset, bytes.length) > 0
                        && hits < minimumHits) {

                    if (KMPMatch.indexOf(bytes, getBytePattern()) >= 0) {

                        hits++;
                    }

                }

                inputStream.close();
                
            } catch (IOException ex) {
                Logger.getLogger(FileContentFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return hits >= minimumHits;
    }

    /**
     * @return  the minimum times the searchTerm must appear in the contents
     * of the file before victory is declared
     */
    public int getMinimumHits() {
        return minimumHits;
    }
    /**
     * @return the searchTerm in bytes
     */
    public byte[] getBytePattern() {
        return searchTerm.getBytes();
    }
    /**
     * @return the searchTerm string
     */
    public String getSearchTerm() {
        return searchTerm;
    }

}
