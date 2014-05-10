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
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import net.sbarbaro.filemaster.model.FileAction;
import net.sbarbaro.filemaster.model.FileActionOperator;
import net.sbarbaro.filemaster.model.Rule;

/**
 * ConditionUI
 * <p>
 * {Purpose of This Class}
 * <p>
 * {Other Notes Relating to This Class (Optional)}
 *
 * @author Anthony J. Barbaro (tony@abarbaro.net) $LastChangedRevision: $
 * $LastChangedDate: $
 */
public class FileActionUI extends RuleEditorSubpanel {

    private static final long serialVersionUID = 8149169721657166185L;
    private static Logger LOGGER = Logger.getLogger(FileActionUI.class.getName());

    private final Rule rule;

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
            case COPY:
            case RENAME: {
                c.fill = GridBagConstraints.HORIZONTAL;
                c.gridx += c.gridwidth;
                c.gridwidth = 3;
                final JTextField destField = new JTextField(30);
                destField.setText(action.getDestinationDirectoryName());
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

                } else {

                    add();
                }

            }

        }

    }

    @Override
    protected void add() {

        rule.getFileActions().add(new FileAction());
    }

    @Override
    protected void delete(int index) {
        rule.getFileActions().remove(index);
    }

}
