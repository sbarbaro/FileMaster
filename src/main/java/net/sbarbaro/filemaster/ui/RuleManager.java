package net.sbarbaro.filemaster.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sbarbaro.filemaster.model.FileMaster;
import net.sbarbaro.filemaster.model.Rule;

/**
 * RuleManager
 * <p>
 * Allows user to view, add or delete, modify, and activate or deactivate Rules
 *
 * @author Anthony J. Barbaro (tony@abarbaro.net) $LastChangedRevision: $
 * $LastChangedDate: $
 */
public class RuleManager extends RuleEditorSubpanel {

    private static final long serialVersionUID = -2572061129607341877L;

    private final FileMaster fileMaster;
    private final JTabbedPane tabbedPane;
    private boolean isEditing;

    public RuleManager(FileMaster fileMaster, final JTabbedPane tabbedPane) {
        super();
        this.fileMaster = fileMaster;
        this.tabbedPane = tabbedPane;

        ChangeListener l = new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {

                if (isEditing && tabbedPane.getSelectedIndex() == 0) {

                    isEditing = false;
                    layoutPanel();
                    tabbedPane.setEnabledAt(0, true);
                    tabbedPane.setEnabledAt(1, true);
                    revalidate();
                    repaint();

                }

            }
        };
        tabbedPane.addChangeListener(l);

        isEditing = false;
    }

    @Override
    protected void add() {

        isEditing = true;
        tabbedPane.setEnabledAt(0, false);
        tabbedPane.setEnabledAt(1, false);

        Rule rule = new Rule();

        fileMaster.getRules().add(rule);

        final RuleEditor ruleEditor = new RuleEditor(fileMaster, rule);

        tabbedPane.add("New Rule", ruleEditor);
        tabbedPane.setSelectedComponent(ruleEditor);

    }

    @Override
    protected void delete(int index) {
        fileMaster.getRules().remove(index);
    }

    @Override
    protected void layoutPanel() {
        removeAll();
        deleteButtons.clear();
        c.insets.left = 4;
        c.insets.right = 2;
        c.insets.bottom = 4;

        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;

        c.anchor = GridBagConstraints.EAST;
        c.gridx = 0;
        c.gridy = 0;

        // Create header
        JLabel activeLabel = new JLabel("Active");
        add(activeLabel, c);

        c.anchor = GridBagConstraints.WEST;
        c.gridx = 1;
        c.gridwidth = 2;
        JLabel descLabel = new JLabel("Description");
        add(descLabel, c);

        for (Rule rule : fileMaster.getRules()) {

            c.anchor = GridBagConstraints.EAST;
            c.gridx = 0;
            c.gridwidth = 1;
            c.gridy++;

            JCheckBox activeCheck = new JCheckBox();
            activeCheck.setSelected(rule.isActive());
            activeCheck.addActionListener(this);
            add(activeCheck, c);

            c.anchor = GridBagConstraints.WEST;
            c.gridx++;
            c.gridwidth = 2;
            JTextField descField = new JTextField(40);
            descField.setText(rule.getDescription());
            descField.setEditable(false);
            add(descField, c);

            c.gridx += c.gridwidth;
            c.gridwidth = 1;
            final Rule _rule = rule;
            JButton editButton = ComponentFactory.createEditButtion();
            editButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {

                    // Disable all components until the New Rule tab is closed
                    tabbedPane.setEnabledAt(0, false);
                    tabbedPane.setEnabledAt(1, false);

                    final RuleEditor ruleEditor = new RuleEditor(fileMaster, _rule);

                    tabbedPane.add("Edit Rule", ruleEditor);
                    tabbedPane.setSelectedComponent(ruleEditor);

                    isEditing = true;

                }
            });

            add(editButton, c);

            JButton deleteButton = ComponentFactory.createDeleteButton();
            deleteButton.addActionListener(this);
            deleteButtons.add(deleteButton);
            c.gridx += c.gridwidth;
            c.gridwidth = 1;
            add(deleteButton, c);

        }
        c.gridx = 0;
        c.gridy++;
        add(addButton, c);

        // Disable all components until the New Rule tab is closed
        if (isEditing) {
            super.setEnabled(false);
        }

        revalidate();
        repaint();

    }

    @Override
    protected void harvest() {

        Iterator<Rule> rIter = fileMaster.getRules().iterator();
        Iterator<Component> cIter = Arrays.asList(getComponents()).iterator();

        while (cIter.hasNext() && rIter.hasNext()) {

            Component component = cIter.next();

            // Each rule is delimited in the RuleManager UI by a check box
            if (component instanceof JCheckBox) {

                Rule rule = rIter.next();

                rule.setActive(((JCheckBox) component).isSelected());

            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e
    ) {
        super.actionPerformed(e);
        if (e.getSource() instanceof Component) {

            harvest();

            try {
                fileMaster.serialize(App.FILE);
            } catch (IOException ex) {
                Logger.getLogger(RuleManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
