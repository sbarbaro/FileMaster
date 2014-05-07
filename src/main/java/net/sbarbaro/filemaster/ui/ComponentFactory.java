package net.sbarbaro.filemaster.ui;

import javax.swing.JButton;

/**
* ComponentFactory
* <p>
* Factory of common UI components.
* <p>
* {Other Notes Relating to This Class (Optional)}
* @author Anthony J. Barbaro (tony@abarbaro.net)
* $LastChangedRevision: $
* $LastChangedDate: $
*/
public class ComponentFactory {

    public static JButton createAddButton() {
        JButton addButton = new JButton("<html><b>+</b><html>");
        addButton.setToolTipText("Add a new row");
        return addButton;
                
    }
    
    public static JButton createEditButtion() {
        JButton editButton = new JButton("<html><b>&#8710;</b></html>");
        editButton.setToolTipText("Edit this row");
        return editButton;
    }
    
    public static JButton createDeleteButton() {
        JButton deleteButton = new JButton("<html><b>-</b></html>");
        deleteButton.setToolTipText("Delete this row");
        return deleteButton;
    }
    
    public static JButton createBrowseButton() {
        JButton browseButton = 
                new JButton("<html><b>...</b></html>");
        browseButton.setToolTipText("Browse the file system");
        return browseButton;
    }
    public static JButton createRunButton() {
        JButton runButton = 
                new JButton("<html><b>&#187;</b></html>");
        runButton.setToolTipText("Execute all Active rules right now");
       return runButton;
    }
}
