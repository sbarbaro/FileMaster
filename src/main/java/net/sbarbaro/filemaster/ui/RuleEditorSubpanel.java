package net.sbarbaro.filemaster.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * RuleEditorSubpanel
 * <p>
 * Common subpanel design
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net)
 * @param <T>
 */
public abstract class RuleEditorSubpanel<T> extends JPanel
        implements ActionListener, ItemListener {

    private static final long serialVersionUID = -508811170914637929L;

    private static final Logger LOGGER
            = Logger.getLogger(RuleEditorSubpanel.class.getName());

    /**
     * Button to add a new ruleItem to the list of ruleItems
     * and update the UI to provide a data entry placeholder
     * for the new ruleItem.  The name property of the
     * button is used to hold the class name of the 
     * type to dynamically create and add to the ruleItems
     * using the Java Reflection API
     */
    protected final JButton addButton;

    protected final List<JButton> deleteButtons;

    protected final GridBagConstraints c;
    protected final GridBagLayout gbl;
    
    protected final List<T> ruleItems;

    /**
     * Default constructor
     */
    public RuleEditorSubpanel() {

       this(new ArrayList<>());

    }
    public RuleEditorSubpanel(List<T> ruleItems) {
         super();
        gbl = new GridBagLayout();
        setLayout(gbl);

        this.deleteButtons = new ArrayList<>();
        this.c = new GridBagConstraints();

        c.insets.left = 3;
        c.insets.right = 5;
        c.insets.top = 1;
        c.insets.bottom = 1;

        this.addButton = ComponentFactory.createAddButton();
        
        /*
        Use the Java Reflection API to set the name property of
        the addButton to be the generic parameter bound to 
        the RuleEditorSubpanel subclass.  Note that the JButton
        name property is not displayed.
        */
        Class genericParameter0OfThisClass = 
        (Class)
        ((ParameterizedType)
            getClass()
                .getGenericSuperclass())
                    .getActualTypeArguments()[0];
        
        this.addButton.setName(genericParameter0OfThisClass.getName());
        this.addButton.addActionListener(this);
        this.ruleItems = ruleItems;
    }
    

    /**
     * Lays out components for the editor
     */
    protected abstract void layoutPanel();

    /**
     * Saves the data from the components to the object
     */
    protected abstract void harvest();

    /**
     * Responds to button clicks
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        LOGGER.log(Level.INFO, e.toString());

        harvest();

        if (addButton.equals(e.getSource())) {

            String className = addButton.getName();
            Class clazz;
            try {
                clazz = Class.forName(className);
                ruleItems.add((T) clazz.newInstance());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(RuleEditorSubpanel.class.getName()).log(Level.SEVERE, null, ex);
            }
            

        } else if (e.getSource() instanceof JButton) {

            for (int i = 0; i < deleteButtons.size(); i++) {

                JButton deleteButton = deleteButtons.get(i);

                if (deleteButton == e.getSource()) {

                    ruleItems.remove(i);

                    break;

                }
            }

        }
        // Remove all components from the form
        removeAll();
        // Clear all the buttons
        deleteButtons.clear();
        // Relayout the panel with a fresh set of components
        layoutPanel();
        // Repaint the result
        revalidate();
        repaint();

    }

    /**
     * The user has made a selection from a JComboBox
     *
     * @param e
     */
    @Override
    public void itemStateChanged(ItemEvent e) {

        LOGGER.log(Level.INFO, e.toString());

        if (ItemEvent.DESELECTED == e.getStateChange()) {

            harvest();

        } else {

            removeAll();
            deleteButtons.clear();
            layoutPanel();

        }
    }

    /**
     * Fills empty space in the given number of columns based on current c.gridx
     * and c.gridy values
     *
     * @param columns
     */
    protected void fillHorizontal(int columns) {

        double weightx = c.weightx;

        int fill0 = c.fill;

        c.fill = GridBagConstraints.HORIZONTAL;

        for (int x = 0; x < columns - 1; x++) {

            add(Box.createHorizontalStrut(200), c);

            c.gridx += x;

        }

        c.fill = fill0;
    }

    /**
     * Gets all the rule editor subpanel subclass instances for this root pane
     *
     * @param container
     * @param ruleEditorSubpanels
     */
    public static void getRuleEditorSubpanels(Container container,
            List<RuleEditorSubpanel> ruleEditorSubpanels) {

        Component[] components = container.getComponents();
        for (Component com : components) {
            if (com instanceof RuleEditorSubpanel) {
                ruleEditorSubpanels.add((RuleEditorSubpanel) com);
            } else if (com instanceof Container) {
                getRuleEditorSubpanels((Container) com, ruleEditorSubpanels);
            }
        }
    }

}
