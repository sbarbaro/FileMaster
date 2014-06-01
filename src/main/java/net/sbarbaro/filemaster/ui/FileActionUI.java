package net.sbarbaro.filemaster.ui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import net.sbarbaro.filemaster.model.FileAction;
import net.sbarbaro.filemaster.model.FileActionOperator;
import net.sbarbaro.filemaster.model.Rule;

/**
 * FileActionUI
 * <p>
 * RuleEditorSubpanel implementation that allows the user to configure one or
 * more FileAction for a Rule
 * <p>
 * @author Steven A. Barbaro (steven@abarbaro.net)
 */
public class FileActionUI extends RuleEditorSubpanel {

    private static final long serialVersionUID = 8149169721657166185L;
    private static Logger LOGGER = Logger.getLogger(FileActionUI.class.getName());

    private final Rule rule;

    /**
     * Constructor
     * @param rule The Rule for which FileActions are to be configured
     */
    public FileActionUI(Rule rule) {

        super();

        this.rule = rule;

        if (rule.getFileActions().isEmpty()) {
            add();
        }

    }

    @Override
    public void layoutPanel() {

        removeAll();
        deleteButtons.clear();

        c.insets.left = 4;
        c.insets.right = 2;
        c.insets.bottom = 4;

        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;

        c.gridx = 0;
        c.gridy = 0;

        for (FileAction action : rule.getFileActions()) {
            c.gridy++;
            layoutRow(action);
        }

        c.gridx = 0;
        c.gridy++;
        add(addButton, c);

        revalidate();
        repaint();
    }

    /**
     * Layout a row on this panel based on the given FileAction
     * @param action  The FileAction to layout
     */
    public void layoutRow(FileAction action) {
        c.fill = GridBagConstraints.NONE;
        c.gridwidth = 1;
        c.gridx = 0;

        JComboBox actionCombo = new JComboBox(FileActionOperator.values());
        actionCombo.setSelectedItem(action.getFileAction());
        actionCombo.addItemListener(this);
        add(actionCombo, c);

        switch (action.getFileAction()) {
            case MOVE:
            case COPY: {
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx += c.gridwidth;
                c.gridwidth = 3;
                final JTextField destField = new JTextField(30);
                destField.setText(action.getDestinationPathname());
                add(destField, c);

                c.fill = GridBagConstraints.NONE;
                c.gridx += c.gridwidth;
                c.gridwidth = 1;
                JButton browseButton = ComponentFactory.createBrowseButton();
                browseButton.addActionListener(new AbstractAction() {
                    private static final long serialVersionUID = -5644390861803492172L;

                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fc = new JFileChooser();
                        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        fc.showOpenDialog(App.INSTANCE);
                        File file = fc.getSelectedFile();
                        if (null == file) {

                        } else {
                            destField.setText(file.getAbsolutePath());
                        }

                    }

                });
                add(browseButton, c);

                break;
            }

            case RENAME: {

                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx += c.gridwidth;
                c.gridwidth = 2;
                final JTextField destField = new JTextField(20);
                destField.setText(action.getDestinationPathname());
                add(destField, c);

                c.fill = GridBagConstraints.NONE;
                c.gridx += c.gridwidth;
                c.gridwidth = 1;
                JComboBox comboBox = new JComboBox(RenameSubstitution.values());

                if(destField.getText() == null 
                        || destField.getText().trim().length() == 0) {
                    comboBox.setSelectedItem(RenameSubstitution.RESET);
                }
                comboBox.addItemListener(new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {

                        if (ItemEvent.SELECTED == e.getStateChange()) {

                            String item = e.getItem().toString();

                            if (RenameSubstitution.RESET.toString().equals(item)) {
                                destField.setText(null);
                            } else if (null == destField.getText()
                                    || destField.getText().trim().length() == 0) {
                                destField.setText(item);
                            } else {
                                destField.setText(destField.getText() + '-' + item);
                            }

                        }

                    }

                });

                add(comboBox, c);

                c.gridx += c.gridwidth;
                c.gridwidth = 1;
                add(new JLabel(""), c);

                break;
            }
            case RECYCLE:
            case DELETE:
                c.fill = GridBagConstraints.NONE;
                c.gridx += c.gridwidth;
                c.gridwidth = 1;
                super.fillHorizontal(4);
                break;
            default:
                throw new UnsupportedOperationException(
                        "Bad criterion " + action.getFileAction().name());

        }

        JButton deleteButton = ComponentFactory.createDeleteButton();
        deleteButton.addActionListener(this);
        deleteButtons.add(deleteButton);
        c.gridx += c.gridwidth;
        c.gridwidth = 1;
        add(deleteButton, c);

    }

    /**
     * Update this Rule based on the user-entered values
     */
    @Override
    protected void harvest() {

        rule.getFileActions().clear();

        Iterator<Component> cIter = Arrays.asList(getComponents()).iterator();

        while (cIter.hasNext()) {

            Component component = cIter.next();

            // Locate the ComboBox for the CollectionGroup
            if (component instanceof JComboBox) {

                JComboBox comboBox = (JComboBox) component;

                Object selectedItem = comboBox.getSelectedItem();

                if (selectedItem instanceof FileActionOperator) {

                    FileActionOperator op = (FileActionOperator) selectedItem;

                    FileAction fileAction = new FileAction(op);
                    rule.getFileActions().add(fileAction);

                    switch (op) {
                        case MOVE:
                        case COPY:
                        case RENAME: {

                            component = cIter.next();

                            if (component instanceof JTextField) {

                                JTextField pathnameField = (JTextField) component;

                                fileAction.setDestinationPathname(
                                        pathnameField.getText());
                            }

                        }
                        break;
                        case RECYCLE:
                        case DELETE:

                            break;
                        default:
                            throw new UnsupportedOperationException(
                                    "Bad file action " + fileAction);
                    }

                } 

            }

        }

    }

    /**
     * Add a new FileAction to this Rule
     */
    @Override
    protected void add() {

        rule.getFileActions().add(new FileAction());
    }

    /**
     * Delete the FileAction specified by this given index value from this Rule
     * @param index The index of the FileAction to delete from this Rule.
     */
    @Override
    protected void delete(int index) {
        
        rule.getFileActions().remove(index);
        
    }

}
