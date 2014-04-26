package net.sbarbaro.filemaster.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * RuleEditorSubpanel
 * <p>
 * Common subpanel design for use by the RuleEditor
 * <p>
 * {Other Notes Relating to This Class (Optional)}
 *
 * @author Anthony J. Barbaro (tony@abarbaro.net) $LastChangedRevision: $
 * $LastChangedDate: $
 */
public abstract class RuleEditorSubpanel extends JPanel
        implements ActionListener, ItemListener {

    private static final long serialVersionUID = -508811170914637929L;

    private static final Logger LOGGER
            = Logger.getLogger(RuleEditorSubpanel.class.getName());

    protected final JButton addButton;

    protected final List<JButton> deleteButtons;

    protected final GridBagConstraints c;
    protected final GridBagLayout gbl;

    public RuleEditorSubpanel() {

        super();
        gbl = new GridBagLayout();
        setLayout(gbl);

        this.deleteButtons = new ArrayList<JButton>();

        this.addButton = ComponentFactory.createAddButton();
        this.addButton.addActionListener(this);

        this.c = new GridBagConstraints();

        c.insets.left = 3;
        c.insets.right = 5;
        c.insets.top = 3;
        c.insets.bottom = 3;

    }

    protected abstract void add();

    protected abstract void delete(int index);

    protected abstract void layoutPanel();

    protected abstract void harvest();

    public void actionPerformed(ActionEvent e) {

        LOGGER.log(Level.INFO, e.toString());

        harvest();

        if (addButton.equals(e.getSource())) {

            add();

        } else if (e.getSource() instanceof JButton) {

            for (int i = 0; i < deleteButtons.size(); i++) {

                JButton deleteButton = deleteButtons.get(i);

                if (deleteButton == e.getSource()) {

                    delete(i);

                    break;

                }
            }

        }
        removeAll();
        deleteButtons.clear();
        layoutPanel();

        revalidate();
        repaint();

    }

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
