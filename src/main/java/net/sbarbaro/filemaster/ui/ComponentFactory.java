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
        return new JButton("<html><b>+</b><html>");
    }
    
    public static JButton createEditButtion() {
        return new JButton("<html><b>&delta;</b></html>");
    }
    
    public static JButton createDeleteButton() {
        return new JButton("<html><b>-</b></html>");
    }
    
    public static JButton createBrowseButton() {
        JButton browseButton = 
                new JButton("<html><b>...</b></html>");
        
        return browseButton;
    }
}
