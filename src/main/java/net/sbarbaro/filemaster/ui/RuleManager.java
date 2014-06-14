package net.sbarbaro.filemaster.ui;

import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sbarbaro.filemaster.io.Runner;
import net.sbarbaro.filemaster.model.FileMaster;
import net.sbarbaro.filemaster.model.Rule;

/**
 * RuleManager
 * <p>
 * Allows user to view, add or delete, modify, and activate or deactivate Rules
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net)
 */
public final class RuleManager extends RuleEditorSubpanel {

    private static final long serialVersionUID = -2572061129607341877L;

    private final FileMaster fileMaster;
    private final JTabbedPane tabbedPane;
    private boolean isEditing;
    private final JButton runButton;

    /**
     * Constructor
     *
     * @param fileMaster
     * @param tabbedPane
     */
    public RuleManager(FileMaster fileMaster, final JTabbedPane tabbedPane) {
        super();
        this.fileMaster = fileMaster;
        this.tabbedPane = tabbedPane;

        ChangeListener l;
        l = (ChangeEvent e) -> {
            if (isEditing && tabbedPane.getSelectedIndex() == 0) {
                
                isEditing = false;
                layoutPanel();
                tabbedPane.setEnabledAt(0, true);
                tabbedPane.setEnabledAt(1, true);
                revalidate();
                repaint();
                
            }
        };

        isEditing = false;
        
        tabbedPane.addChangeListener(l);
        this.runButton = ComponentFactory.createRunButton();
        runButton.addActionListener(this);
    }

    /**
     * Creates and adds a new Rule to this FileMaster
     */
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

    /**
     * Deletes an existing Rule from this FileMaster
     *
     * @param index
     */
    @Override
    protected void delete(int index) {
        fileMaster.getRules().remove(index);
    }

    /**
     * Populates the components on this panel based on the current Rule
     */
    @Override
    protected void layoutPanel() {
        removeAll();
        deleteButtons.clear();
        c.insets.left = 4;
        c.insets.right = 4;
        c.insets.bottom = 4;

        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;

        c.anchor = GridBagConstraints.EAST;
        c.gridx = 0;
        c.gridy = 0;

        // Create header
        add(ComponentFactory.createHeaderLabel("Active"), c);

        c.anchor = GridBagConstraints.WEST;
        c.gridx = 1;
        c.gridwidth = 2;
        add(ComponentFactory.createHeaderLabel("Description"), c);

        fileMaster.getRules().stream().map((rule) -> {
            c.anchor = GridBagConstraints.EAST;
            return rule;
        }).map((Rule rule) -> {
            c.gridx = 0;
            return rule;
        }).map((rule) -> {
            c.gridwidth = 1;
            return rule;
        }).map((rule) -> {
            c.gridy++;
            return rule;
        }).map((rule) -> {
            JCheckBox activeCheck = new JCheckBox();
            activeCheck.setSelected(rule.isActive());
            activeCheck.addActionListener(this);
            add(activeCheck, c);
            c.anchor = GridBagConstraints.WEST;
            return rule;
        }).map((rule) -> {
            c.gridx++;
            return rule;
        }).map((rule) -> {
            c.gridwidth = 2;
            return rule;
        }).map((rule) -> {
            JLabel descField = new JLabel();
            descField.setText(rule.getDescription());
            descField.setPreferredSize(new Dimension(360, 14));
            add(descField, c);
            c.gridx += c.gridwidth;
            return rule;
        }).map((rule) -> {
            c.gridwidth = 1;
            return rule;
        }).map((rule) -> rule).map((_rule) -> {
            JButton editButton = ComponentFactory.createEditButtion();
            editButton.addActionListener((ActionEvent e) -> {
                tabbedPane.setEnabledAt(0, false);
                tabbedPane.setEnabledAt(1, false);
                
                final RuleEditor ruleEditor = new RuleEditor(fileMaster, _rule);
                
                tabbedPane.add("Edit Rule", ruleEditor);
                tabbedPane.setSelectedComponent(ruleEditor);
                
                isEditing = true;
            });
            return editButton;
        }).map((editButton) -> {
            add(editButton, c);
            return editButton;
        }).map((JButton _item) -> {
            return ComponentFactory.createDeleteButton();
        }).map((deleteButton) -> {
            deleteButton.addActionListener(this);
            return deleteButton;
        }).map((deleteButton) -> {
            deleteButtons.add(deleteButton);
            return deleteButton;
        }).map((deleteButton) -> {
            c.gridx += c.gridwidth;
            return deleteButton;
        }).map((deleteButton) -> {
            c.gridwidth = 1;
            return deleteButton;
        }).forEach((deleteButton) -> {
            add(deleteButton, c);
        });
        c.gridx = 0;
        c.gridy++;
        add(addButton, c);

        c.gridx = 1;
        add(runButton, c);

        // Disable all components until the New Rule tab is closed
        if (isEditing) {
            super.setEnabled(false);
        }

        revalidate();
        repaint();

    }

    /**
     * Transfers the contents of the panel into each individual Rule
     */
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

    /**
     * Harvests the contents of the UI. Serializes the result.
     *
     * @param e A UI event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (e.getSource() instanceof Component) {

            harvest();

            try {
                fileMaster.serialize();
            } catch (IOException ex) {
                Logger.getLogger(RuleManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (e.getSource().equals(runButton)) {

            Runner runner = new Runner(fileMaster);
            runner.run();

        }

    }

}
