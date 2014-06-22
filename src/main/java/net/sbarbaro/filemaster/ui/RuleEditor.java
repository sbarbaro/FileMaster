package net.sbarbaro.filemaster.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import net.sbarbaro.filemaster.model.FileMaster;
import net.sbarbaro.filemaster.model.Rule;

/**
 * RuleEditor
 * <p>
 * Provides user entry of a new or modified Rule
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net)
 */
public final class RuleEditor extends JPanel implements ActionListener {

    private static final long serialVersionUID = -4300593870589613998L;

    // The new or existing rule to edit
    private final Rule rule;

    // Maintain a copy of the rule to support reset
    private final Rule originalRule;

    // The parent FileMaster
    private final FileMaster fileMaster;

    // A toolbar of rule actions
    private final JToolBar toolbar;
    private final JButton saveButton, saveAndCloseButton, closeButton,
            copyButton, resetButton;

    /**
     * Default constructor. Creates a new Rule
     * @throws java.net.UnknownHostException
     * @throws java.net.SocketException
     */
    public RuleEditor() throws UnknownHostException, SocketException {
        this(new FileMaster(), new Rule());
    }

    /**
     * Constructor Constructs a RuleEditor based upon the given rule
     *
     * @param fileMaster
     * @param rule The Rule to render in the RuleEditor
     */
    public RuleEditor(final FileMaster fileMaster, final Rule rule) {

        super(new BorderLayout());
        this.rule = rule;
        this.originalRule = new Rule(rule);
        this.fileMaster = fileMaster;

        toolbar = new JToolBar();
        add(toolbar, BorderLayout.NORTH);

        saveButton = new JButton("Save");
        saveButton.addActionListener(this);
        toolbar.add(saveButton);

        saveAndCloseButton = new JButton("Save and Close");
        saveAndCloseButton.addActionListener(this);
        toolbar.add(saveAndCloseButton);

        closeButton = new JButton("Close");
        closeButton.addActionListener(this);
        toolbar.add(closeButton);

        toolbar.add(new JToolBar.Separator());

        copyButton = new JButton(new AbstractAction("Copy") {

            private static final long serialVersionUID = 4377386270269629176L;

            @Override
            public void actionPerformed(ActionEvent e) {

                Container container = RuleEditor.this.getParent();

                if (container instanceof JTabbedPane) {

                    JTabbedPane tabbedPane = (JTabbedPane) container;

                    tabbedPane.remove(RuleEditor.this);

                    Rule _rule = new Rule(rule);
                    _rule.setDescription("Change me");
                    fileMaster.getRules().add(_rule);

                    RuleEditor ruleEditorCopy
                            = new RuleEditor(fileMaster, _rule);

                    tabbedPane.add(ruleEditorCopy, "Copy rule");
                    tabbedPane.setSelectedComponent(ruleEditorCopy);
                }

            }

        });
        toolbar.add(copyButton);

        resetButton = new JButton(new AbstractAction("Reset") {

            private static final long serialVersionUID = 4377386270269629176L;

            @Override
            public void actionPerformed(ActionEvent e) {

                Container container = RuleEditor.this.getParent();

                Rule _rule = null;

                for (Rule rule : fileMaster.getRules()) {

                    if (rule == RuleEditor.this.rule) {

                        _rule = rule;

                        fileMaster.getRules().remove(rule);

                        fileMaster.getRules().add(originalRule);

                        break;
                    }

                }

                JTabbedPane tabbedPane = (JTabbedPane) container;
                tabbedPane.remove(RuleEditor.this);
                RuleEditor ruleEditorOriginal
                        = new RuleEditor(fileMaster, originalRule);

                tabbedPane.add(ruleEditorOriginal, "Reset rule");
                tabbedPane.setSelectedComponent(ruleEditorOriginal);

            }

        });
        toolbar.add(resetButton);

        final Box box = Box.createVerticalBox();

        RuleDescriptionUI ruleDescriptionUI = new RuleDescriptionUI(rule);

        ruleDescriptionUI.setBorder(
                BorderFactory.createTitledBorder("Rule description:"));
        ruleDescriptionUI.layoutPanel();
        box.add(ruleDescriptionUI);

        FileMonitorUI fileMonitorUI = new FileMonitorUI(rule);
        fileMonitorUI.setBorder(
                BorderFactory.createTitledBorder("Monitor these folders:"));
        box.add(fileMonitorUI);

        FileFilterUI fileFilterUI = new FileFilterUI(rule);
        fileFilterUI.setBorder(
                BorderFactory.createTitledBorder(
                        "For files matching these conditions:"));
        fileFilterUI.layoutPanel();
        box.add(fileFilterUI);

        FileActionUI fileActionUI = new FileActionUI(rule);
        fileActionUI.setBorder(
                BorderFactory.createTitledBorder(
                        "Then do these actions:"));
        fileActionUI.layoutPanel();
        box.add(fileActionUI);

        JScrollPane scroll = new JScrollPane(box);

        this.add(scroll, BorderLayout.CENTER);

    }

    /**
     * Harvests the user's entries from the editor and persists the Rule to
     * the serialized file
     * @param e The UI event
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (saveButton.getActionCommand().equalsIgnoreCase(e.getActionCommand())
                || saveAndCloseButton.getActionCommand().equalsIgnoreCase(
                        e.getActionCommand())) {

            /*
             * Update this Rule based on the current contents of all
             * all RuleEditorSubpanels.
             */
            java.util.List<RuleEditorSubpanel> result  = new ArrayList<>();

            RuleEditorSubpanel.getRuleEditorSubpanels(RuleEditor.this, result);

            result.stream().forEach((ruleEditor) ->  ruleEditor.harvest()); 
            
            try {
                fileMaster.serialize();
            } catch (IOException i) {
                Logger.getLogger(RuleEditor.class.getName()).log(Level.WARNING, null, i);
            }
        }
        if (saveAndCloseButton.getActionCommand().equalsIgnoreCase(e.getActionCommand())
                || closeButton.getActionCommand().equalsIgnoreCase(e.getActionCommand())) {

            if(closeButton.getActionCommand().equalsIgnoreCase(e.getActionCommand())) {
                resetButton.setSelected(true);
            }
            JTabbedPane tabbedPane = (JTabbedPane) getParent();
            tabbedPane.remove(this);
            tabbedPane.setSelectedIndex(0);

        }
    }

}
