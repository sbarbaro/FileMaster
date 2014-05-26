package net.sbarbaro.filemaster.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

/**
 * PDFTextParser
 * <p>
 * Extracts text data from an Adobe Acrobat&#0153; document
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net) $LastChangedRevision: $
 * $LastChangedDate: $
 */
public class PDFTextParser {

    private static final Logger LOGGER = Logger.getLogger(PDFTextParser.class.getName());

    /**
     * Extract text data from a PDF document.
     * @param fileName The filename of the PDF document.
     * @return The text data extracted from the PDF.
     */
    public static String pdftoText(String fileName) {
        PDFParser parser;
        String parsedText = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;
        File file = new File(fileName);
        if (!file.isFile()) {
            LOGGER.log(Level.SEVERE, "File " + fileName + " does not exist.");
            return null;
        }
        try {
            parser = new PDFParser(new FileInputStream(file));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to open PDF Parser. " + e.getMessage());
            return null;
        }
        try {
            parser.parse();
            cosDoc = parser.getDocument();
            PDFTextStripper pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdfStripper.setStartPage(1);
            pdfStripper.setEndPage(5);
            parsedText = pdfStripper.getText(pdDoc);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An exception occured in parsing the PDF Document."
                            + e.getMessage());
        } finally {
            try {
                if (cosDoc != null) {
                    cosDoc.close();
                }
                if (pdDoc != null) {
                    pdDoc.close();
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, e.getMessage());
            }
        }
        return parsedText;
    }
}
