package net.sbarbaro.filemaster.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;
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
 * @author Steven A. Barbaro (steven@abarbaro.net)
 */
public final class FileMonitorUI extends RuleEditorSubpanel<FileMonitor> {

    private static final long serialVersionUID = -381916214784162260L;

    private static final Logger LOGGER = Logger.getLogger(FileMonitorUI.class.getName());


    // Cache the lastDirectory visited.  Used to configre FileChooser.
    private File lastDirectory;

    /**
     * Constructor
     *
     * @param rule The rule for which FileMonitors need to be configured
     */
    public FileMonitorUI(Rule rule) {

        super(rule.getFileMonitors());

        if (super.ruleItems.isEmpty()) {
            super.ruleItems.add(new FileMonitor());
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

        ruleItems.stream().map((monitor) -> {
            c.gridy++;
            return monitor;
        }).forEach((monitor) -> {
            layoutRow(monitor);
        });

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
        if (fileMonitor.getDirectoryName() != null) {
            directoryField.setText(fileMonitor.getDirectoryName());
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

        ruleItems.clear();

        Iterator<Component> cIter = Arrays.asList(getComponents()).iterator();

        FileMonitor fileMonitor = null;

        while (cIter.hasNext()) {

            Component fileMonitorComponent = cIter.next();

            if (fileMonitorComponent instanceof JTextField) {

                JTextField tf = (JTextField) fileMonitorComponent;
                fileMonitor = new FileMonitor();
                fileMonitor.setDirectoryName(tf.getText());

            } else if (fileMonitorComponent instanceof JCheckBox) {

                JCheckBox cb = (JCheckBox) fileMonitorComponent;

                if(null == fileMonitor) {
                   LOGGER.log(Level.WARNING, "Null FileMonitor?");
                } else {
                    fileMonitor.setRecurse(cb.isSelected());
                }

                ruleItems.add(fileMonitor);
            }
        }

    }
}
