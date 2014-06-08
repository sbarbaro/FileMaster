package net.sbarbaro.filemaster.ui;

import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * ComponentFactory
 * <p>
 * Factory of common UI components.
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net)
 */
public class ComponentFactory {

    /**
     * Creates a JLabel for use on list and table column headers
     *
     * @param text The text to for the label
     * @return a JLabel
     */
    public static JLabel createHeaderLabel(String text) {
        JLabel bigLabel = new JLabel(text);
        Font oldFont = bigLabel.getFont();
        Font newFont = oldFont.deriveFont(34);
        bigLabel.setFont(new Font("Arial", Font.BOLD, 14));
        return bigLabel;
    }

    /**
     * @return a new button annotated with the word, "Add"
     */
    public static JButton createAddButton() {
        JButton addButton = new JButton("Add");
        addButton.setToolTipText("Add a new row");
        return addButton;
    }

    /**
     * @return a new button annotated with the word, "Edit"
     */
    public static JButton createEditButtion() {
        JButton editButton = new JButton("Edit");
        editButton.setToolTipText("Edit this row");
        return editButton;
    }

    /**
     * @return a new button annotated with the word, "Delete"
     */
    public static JButton createDeleteButton() {
        JButton deleteButton = new JButton("Delete");
        deleteButton.setToolTipText("Delete this row");
        return deleteButton;
    }

    /**
     * @return a new button annotated with the word, "Browse"
     */
    public static JButton createBrowseButton() {
        JButton browseButton = new JButton("Browse");
        browseButton.setToolTipText("Browse the file system");
        return browseButton;
    }

    /**
     * @return a new button annotated with the word, "Run"
     */
    public static JButton createRunButton() {
        JButton runButton
                = new JButton("Run all Active Rules");
        runButton.setToolTipText("Execute all Active rules right now");
        return runButton;
    }
}
