package net.sbarbaro.filemaster.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ParserDecorator;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * FileContentFilter
 * <p>
 * Filters through conduct of a case-insensitive search of the content of a
 * Document file
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net)
 */
public class FileContentFilter extends FileTypeFilter implements Serializable {

    private static final long serialVersionUID = -6799295657477975712L;
    private static final Logger LOGGER
            = Logger.getLogger(FileContentFilter.class.getName());
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
     *
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
                Parser parser = new RecursiveMetadataParser(new AutoDetectParser());
                ParseContext parseContext = new ParseContext();
                parseContext.set(Parser.class, parser);
                final ContentHandler contentHandler = new DefaultHandler();
                final Metadata metadata = new Metadata();

                InputStream inputStream = TikaInputStream.get(pathIn.toFile());

                try {
                    parser.parse(inputStream, contentHandler, metadata, parseContext);
                } catch (SAXException | TikaException ex) {
                    Logger.getLogger(FileContentFilter.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    inputStream.close();
                }

                inputStream = 
                        new ByteArrayInputStream(
                                ((RecursiveMetadataParser)parser).getContent().toString().getBytes());

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
     * @return the minimum times the searchTerm must appear in the contents of
     * the file before victory is declared
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

    private static class RecursiveMetadataParser extends ParserDecorator {

        private static final long serialVersionUID = 8670420653065016532L;

        // Holds the parsed content        
        private final ContentHandler content;
        
        
        public RecursiveMetadataParser(Parser parser) {
            super(parser);
            this.content = new BodyContentHandler();
        }

        public ContentHandler getContent() {
            return content;
        }

        /**
         * Populates this content from the input stream
         * @param stream The input stream containing the content to parse
         * @param ignore not used
         * @param metadata Holds the metadata extracted from the input stream
         * @param context The parse context
         * @throws IOException
         * @throws SAXException
         * @throws TikaException 
         */
        @Override
        public void parse(
                InputStream stream, ContentHandler ignore,
                Metadata metadata, ParseContext context)
                throws IOException, SAXException, TikaException {
         
            super.parse(stream, content, metadata, context);

        }
        
    }
}
