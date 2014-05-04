package net.sbarbaro.filemaster.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import net.sbarbaro.filemaster.model.FileMonitor;
import net.sbarbaro.filemaster.model.Rule;

/**
 * FileMonitorUI
 * <p>
 * Allows user configuration of one or more FileMonitors for a Rule
 * <p>
 *
 * @author Anthony J. Barbaro (tony@abarbaro.net) $LastChangedRevision: $
 * $LastChangedDate: $
 */
public class FileMonitorUI extends RuleEditorSubpanel {

    private static final long serialVersionUID = -381916214784162260L;

    private static final Logger LOGGER = Logger.getLogger(FileMonitorUI.class.getName());

    // The Rule that has zero or more FileMonitors to configure
    private final Rule rule;

    // Cache the lastDirectory visited.  Used to configre FileChooser.
    private File lastDirectory;

    /**
     * Constructor
     *
     * @param rule The rule for which FileMonitors need to be configured
     */
    public FileMonitorUI(Rule rule) {

        super();

        this.rule = rule;

        if (rule.getFileMonitors().isEmpty()) {
            add();
        }

        layoutPanel();
    }

    /**
     * Layout the overall FileMonitorUI panel
     */
    @Override
    public void layoutPanel() {

        removeAll();
        deleteButtons.clear();

        c.insets.left = 0;
        c.insets.right = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;

        for (FileMonitor monitor : rule.getFileMonitors()) {
            c.gridy++;
            layoutRow(monitor);
        }

        c.gridx = 0;
        c.gridy++;
        add(addButton, c);

        revalidate();
        repaint();
    }

    /**
     * Layout a single FileMonitor
     *
     * @param fileMonitor The FileMonitor to render
     */
    public void layoutRow(FileMonitor fileMonitor) {

        c.gridx = 0;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.HORIZONTAL;

        final JTextField directoryField = new JTextField(60);
        if (fileMonitor.getDirectory() != null) {
            directoryField.setText(fileMonitor.getDirectory().getAbsolutePath());
        }
        add(directoryField, c);

        c.gridx = 2;
        c.gridwidth = 1;
        c.fill = GridBagConstraints.NONE;

        JButton browseButton = ComponentFactory.createBrowseButton();
        browseButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -5644390861803492172L;

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser(lastDirectory);
                fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fc.showOpenDialog(App.INSTANCE);
                File directory = fc.getSelectedFile();
                directoryField.setText(directory.getAbsolutePath());
                lastDirectory = directory;
            }

        });
        add(browseButton, c);

        JButton deleteButton = ComponentFactory.createDeleteButton();
        deleteButton.addActionListener(this);
        deleteButtons.add(deleteButton);

        c.gridx = 3;
        add(deleteButton, c);

        c.gridx = 0;
        c.gridy++;
        JCheckBox checkBox = new JCheckBox("Include subfolders");
        checkBox.setSelected(fileMonitor.isRecurse());
        add(checkBox, c);
    }

    /**
     * Update this Rule based on the entries made by the user
     */
    @Override
    protected void harvest() {

        rule.getFileMonitors().clear();

        Iterator<Component> cIter = Arrays.asList(getComponents()).iterator();

        FileMonitor fileMonitor = null;

        while (cIter.hasNext()) {

            Component fileMonitorComponent = cIter.next();

            if (fileMonitorComponent instanceof JTextField) {

                JTextField tf = (JTextField) fileMonitorComponent;
                fileMonitor = new FileMonitor();
                fileMonitor.setDirectory(new File(tf.getText()));

            } else if (fileMonitorComponent instanceof JCheckBox) {

                JCheckBox cb = (JCheckBox) fileMonitorComponent;

                fileMonitor.setRecurse(cb.isSelected());

                rule.getFileMonitors().add(fileMonitor);
            }
        }

    }

    /**
     * Add a new FileMonitor to the FileMonitors belonging to this Rule
     */
    @Override
    protected void add() {
        rule.getFileMonitors().add(new FileMonitor());
    }

    /**
     * Delete the FileMonitor identified by the given index value.
     *
     * @param index The index of the FileMonitor to delete.
     */
    @Override
    protected void delete(int index) {
        Iterator<FileMonitor> fmIter = rule.getFileMonitors().iterator();

        int i = 0;

        while (fmIter.hasNext() && i < index) {
            fmIter.next();
            i++;
        }
        if (fmIter.hasNext()) {
            fmIter.next();
            fmIter.remove();
        }
    }

}
