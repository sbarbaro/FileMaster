package net.sbarbaro.filemaster.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import net.sbarbaro.filemaster.model.FileFilterCriterion;
import net.sbarbaro.filemaster.model.FileAgeFilter;
import net.sbarbaro.filemaster.model.FileAgeOperator;
import net.sbarbaro.filemaster.model.FileAgeUnit;
import net.sbarbaro.filemaster.model.FileContentFilter;
import net.sbarbaro.filemaster.model.FileCriterion;
import net.sbarbaro.filemaster.model.FileNameFilter;
import net.sbarbaro.filemaster.model.FileNameOperator;
import net.sbarbaro.filemaster.model.FileSizeFilter;
import net.sbarbaro.filemaster.model.FileSizeOperator;
import net.sbarbaro.filemaster.model.FileSizeUnit;
import net.sbarbaro.filemaster.model.FileType;
import net.sbarbaro.filemaster.model.FileTypeFilter;
import net.sbarbaro.filemaster.model.ImageAspectRatio;
import net.sbarbaro.filemaster.model.ImageAspectRatioFilter;
import net.sbarbaro.filemaster.model.LogicalGroup;
import net.sbarbaro.filemaster.model.Rule;

/**
 * FileFilterUI
 * <p>
 * This is a UI subpanel that allows the user configure one or more file filters
 * for a given rule.
 * <p>
 * {Other Notes Relating to This Class (Optional)}
 *
 * @author Anthony J. Barbaro (tony@abarbaro.net) $LastChangedRevision: $
 * $LastChangedDate: $
 */
public final class FileFilterUI extends RuleEditorSubpanel {

    private static final long serialVersionUID = 2675101729248592535L;

    private static final Logger LOGGER = Logger.getLogger(FileFilterUI.class.getName());

    // This panel allows user configuration of one or more file filters
    private final Rule rule;
    private final FileFilterStatusUI statusUI;
    protected final JButton testButton;

    public FileFilterUI(Rule rule) {

        super();

        this.rule = rule;

        this.statusUI = new FileFilterStatusUI(rule);

        if (rule.getFileFilterCriteria().isEmpty()) {
            add();
        }

        this.testButton = new JButton("Test");

    }

    /**
     * Layout the overall FileFilterUI
     */
    @Override
    public void layoutPanel() {

        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 1;
        c.gridheight = 1;

        c.gridx = 0;
        c.gridy = 0;

        JComboBox groupCombo = new JComboBox(LogicalGroup.values());
        groupCombo.setEditable(true);
        groupCombo.setSelectedItem(rule.getLogicalGroup());
        groupCombo.setEditable(false);
        add(groupCombo, c);

        c.gridx = 1;
        super.fillHorizontal(3);

        for (FileFilterCriterion condition : rule.getFileFilterCriteria()) {
            c.gridy++;
            layoutRow(condition);
        }

        c.gridx = 0;
        c.gridy++;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);

        buttonPanel.add(statusUI.getTestButton());
        add(buttonPanel, c);

        c.gridwidth = 5;
        c.gridy++;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;

        JScrollPane scrollPane = new JScrollPane(statusUI.getTable());
        scrollPane.setPreferredSize(new Dimension(700, 120));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        add(scrollPane, c);

        revalidate();
        repaint();
    }

    /**
     * Layout a single FileFilterCriterion within the overall FileFilterUI
     * layout.
     *
     * @param condition
     */
    public void layoutRow(FileFilterCriterion condition) {

        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;

        JComboBox criteriaCombo = new JComboBox(FileCriterion.values());
        criteriaCombo.setSelectedItem(condition.getCriterion());
        criteriaCombo.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {

                if (ItemEvent.DESELECTED == e.getStateChange()) {

                    harvest();

                } else {

                    removeAll();
                    deleteButtons.clear();
                    layoutPanel();

                }
                revalidate();
                repaint();
            }
        });

        add(criteriaCombo, c);

        switch (condition.getCriterion()) {
            case NAME:
            case EXT: {

                FileNameFilter fnf = (FileNameFilter) condition.getFilter();

                c.gridx = 1;
                c.gridwidth = 1;
                JComboBox opCombo = new JComboBox(FileNameOperator.values());
                opCombo.setSelectedItem(fnf.getFileNameOperator());
                add(opCombo, c);

                c.gridx += c.gridwidth;
                c.gridwidth = 2;
                JTextField nmField = new JTextField(30);
                nmField.setText(fnf.getTarget());
                add(nmField, c);

                c.gridx += c.gridwidth;
                c.gridwidth = 1;
                super.fillHorizontal(1);

            }
            break;
            case SIZE: {

                FileSizeFilter fsf = (FileSizeFilter) condition.getFilter();

                c.gridx = 1;
                c.gridwidth = 1;
                JComboBox opCombo = new JComboBox(FileSizeOperator.values());
                opCombo.setSelectedItem(fsf.getOp());
                add(opCombo, c);

                c.gridx += c.gridwidth;
                JTextField numField = new JTextField(4);
                numField.setText(String.valueOf(fsf.getTarget()));
                add(numField, c);

                c.gridx += c.gridwidth;
                JComboBox unCombo = new JComboBox(FileSizeUnit.values());
                unCombo.setSelectedItem(fsf.getUnit());
                add(unCombo, c);

                c.gridx += c.gridwidth;
                super.fillHorizontal(1);

            }
            break;
            case TYPE: {

                FileTypeFilter ftf = (FileTypeFilter) condition.getFilter();
                c.gridx = 1;
                c.gridwidth = 2;

                JComboBox typeCombo = new JComboBox(FileType.Root.getChildren());
                typeCombo.setSelectedItem(ftf.getType());
                add(typeCombo, c);

                c.gridx += c.gridwidth;
                c.gridwidth = 1;
                super.fillHorizontal(3);

            }
            break;
            case IMAGE_ASPECT_RATIO:

                ImageAspectRatioFilter arf
                        = (ImageAspectRatioFilter) condition.getFilter();
                c.gridx = 1;
                c.gridwidth = 1;

                JComboBox aspectRatioCombo
                        = new JComboBox(ImageAspectRatio.values());
                aspectRatioCombo.setSelectedItem(
                        arf.getImageAspectRatioTarget());
                add(aspectRatioCombo, c);

                c.gridx = 2;
                super.fillHorizontal(4);

                break;
            case CONTENTS:

                FileContentFilter fcf = (FileContentFilter) condition.getFilter();

                JPanel searchPanel = new JPanel(new FlowLayout());
                searchPanel.add(new JLabel("Search for"));

                JTextField searchTermField = new JTextField(10);
                searchTermField.setText(fcf.getSearchTerm());
                searchPanel.add(searchTermField);

                c.gridx = 1;
                c.gridwidth = 2;
                add(searchPanel, c);

                JPanel hitsPanel = new JPanel(new FlowLayout());
                hitsPanel.add(new JLabel("Minimum hits"));
                
                Integer[] minumumHits = {1, 2, 3, 4, 5};
                JComboBox hitsCombo = new JComboBox(minumumHits);
                hitsCombo.setSelectedItem(fcf.getMinimumHits());
                hitsPanel.add(hitsCombo);
                
                c.gridx += c.gridwidth;
                add(hitsPanel, c);
                
                break;
            case ACCESSED:
            case CREATED:
            case MODIFED:

                FileAgeFilter faf = (FileAgeFilter) condition.getFilter();
                c.gridx = 1;
                c.gridwidth = 2;
                JComboBox ageOpCombo = new JComboBox(FileAgeOperator.values());
                ageOpCombo.setSelectedItem(faf.getAgeOp());
                add(ageOpCombo, c);

                c.gridx += c.gridwidth;
                c.gridwidth = 1;
                JTextField ageValueField = new JTextField(4);
                ageValueField.setText(String.valueOf(faf.getAgeThreshold()));
                add(ageValueField, c);

                c.gridx += c.gridwidth;
                c.gridwidth = 1;
                JComboBox ageUnitCombo = new JComboBox(FileAgeUnit.values());
                ageUnitCombo.setSelectedItem(faf.getAgeUnit());
                add(ageUnitCombo, c);

                break;
            default:
                throw new UnsupportedOperationException(
                        "Bad criterion " + condition.getCriterion().name());

        }

        JButton deleteButton = ComponentFactory.createDeleteButton();
        deleteButtons.add(deleteButton);
        deleteButton.addActionListener(this);

        c.fill = GridBagConstraints.NONE;
        c.gridx += c.gridwidth;
        c.gridwidth = 1;
        add(deleteButton, c);

    }

    @Override
    protected void harvest() {

        rule.getFileFilterCriteria().clear();

        // Locate the ComboBox for the CollectionGroup
        Iterator<Component> cIter = Arrays.asList(getComponents()).iterator();

        while (cIter.hasNext()) {

            Component component = cIter.next();

            if (component instanceof JComboBox) {

                JComboBox aComboBox = (JComboBox) component;

                Object selectedItem = aComboBox.getSelectedItem();

                if (selectedItem instanceof LogicalGroup) {

                    rule.setLogicalGroup((LogicalGroup) selectedItem);

                } else if (selectedItem instanceof FileCriterion) {

                    harvestRow((FileCriterion) selectedItem, cIter);

                }

            }

        }

    }

    private void harvestRow(FileCriterion fileCriterion, Iterator<Component> cIter) {

        DirectoryStream.Filter<Path> fileFilter = null;

        Component firstComponent = cIter.next();

        switch (fileCriterion) {
            case NAME:
            case EXT: {

                if (firstComponent instanceof JComboBox) {

                    Object selectedOp = ((JComboBox) firstComponent).getSelectedItem();

                    if (selectedOp instanceof FileNameOperator) {

                        String target = ((JTextField) cIter.next()).getText();

                        fileFilter = new FileNameFilter(fileCriterion,
                                (FileNameOperator) selectedOp, target);

                    }
                }

                if (null == fileFilter) {
                    fileFilter = new FileNameFilter();
                }
            }

            break;
            case TYPE: {

                if (firstComponent instanceof JComboBox) {

                    Object target = ((JComboBox) firstComponent).getSelectedItem();

                    if (target instanceof FileType) {

                        FileType type = (FileType) target;
                        fileFilter = new FileTypeFilter(type);

                    }
                }
                if (null == fileFilter) {

                    fileFilter = new FileTypeFilter();

                }
            }
            break;
            case SIZE: {

                if (firstComponent instanceof JComboBox) {

                    Object selectedOp = ((JComboBox) firstComponent).getSelectedItem();

                    if (selectedOp instanceof FileSizeOperator) {

                        FileSizeOperator op = (FileSizeOperator) selectedOp;

                        long target = 0;

                        try {
                            target = Long.parseLong(((JTextField) cIter.next()).getText());
                        } catch (NumberFormatException e) {
                            switch (op) {
                                case LARGER:
                                    target = Long.MIN_VALUE;
                                    break;
                                default:
                                    target = Long.MAX_VALUE;
                            }

                        }

                        FileSizeUnit unit
                                = (FileSizeUnit) ((JComboBox) cIter.next()).getSelectedItem();

                        fileFilter = new FileSizeFilter(op, target, unit);

                    }
                }

                if (null == fileFilter) {

                    fileFilter = new FileSizeFilter();

                }
            }
            break;
            case IMAGE_ASPECT_RATIO: {

                if (firstComponent instanceof JComboBox) {

                    Object selection = ((JComboBox) firstComponent).getSelectedItem();

                    if (selection instanceof ImageAspectRatio) {

                        ImageAspectRatio imageAspectRatio = (ImageAspectRatio) selection;

                        fileFilter = new ImageAspectRatioFilter(imageAspectRatio);

                    }
                }

                if (null == fileFilter) {
                    fileFilter = new ImageAspectRatioFilter();
                }
            }
            break;
            case CREATED:
            case ACCESSED:
            case MODIFED: {

                if (firstComponent instanceof JComboBox) {

                    Object selectedOp = ((JComboBox) firstComponent).getSelectedItem();

                    if (selectedOp instanceof FileAgeOperator) {

                        FileAgeOperator op = (FileAgeOperator) selectedOp;

                        String ageIn = ((JTextField) cIter.next()).getText();

                        if (null == ageIn || ageIn.length() == 0) {

                        } else {

                            int target = Integer.parseInt(ageIn);

                            FileAgeUnit unit
                                    = (FileAgeUnit) ((JComboBox) cIter.next()).getSelectedItem();

                            fileFilter = new FileAgeFilter(fileCriterion, op, target, unit);
                        }

                    }
                }

                if (null == fileFilter) {
                    fileFilter = new FileAgeFilter();
                }
            }

            break;
            case CONTENTS:

                if (firstComponent instanceof JPanel) {

                    
                    JPanel searchPanel = (JPanel) firstComponent;
                    
                    Component[] searchPanelComponents = searchPanel.getComponents();
 

                    // Search Term 
                    JTextField searchTermField;
                    searchTermField = (JTextField) searchPanelComponents[1];

                    JPanel hitsPanel = (JPanel) cIter.next();

                    Component[] hitsPanelComponents = hitsPanel.getComponents();
                    
                    // Get minimum hits value
                    JComboBox hitsCombo = (JComboBox) hitsPanelComponents[1];
                    Integer minimumHits = (Integer) hitsCombo.getSelectedItem();

                    fileFilter
                            = new FileContentFilter(searchTermField.getText(), minimumHits);

                } else {

                    fileFilter = new FileContentFilter();

                }
                break;
            default:
                throw new UnsupportedOperationException("Bad file criterion " + fileCriterion);
        }

        rule.getFileFilterCriteria().add(new FileFilterCriterion(fileCriterion, fileFilter));

    }

    @Override
    protected void add() {
        rule.getFileFilterCriteria().add(new FileFilterCriterion(FileCriterion.NAME));

    }

    @Override
    protected void delete(int index) {
        rule.getFileFilterCriteria().remove(index);
    }

}
