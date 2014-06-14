package net.sbarbaro.filemaster.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import net.sbarbaro.filemaster.model.FileMonitor;
import net.sbarbaro.filemaster.model.LogicalGroupFilter;
import net.sbarbaro.filemaster.model.Rule;

/**
 * FileFilterStatusUI
 * <p>
 * Displays a selection of files based on a given set of FileMonitors and
 * FileFilters
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net)
 */
public class FileFilterStatusUI implements ActionListener {

    private static final Logger LOGGER = Logger.getLogger(FileFilterStatusUI.class.getName());

    // The maximum number of files to be displayed in the table
    private static final int MAX_ROWS = 4;

    // The string format of the file modification date column
    private static final DateFormat DF = new SimpleDateFormat("dd-MMM-yy  hh:mm:ss");

    // The file attributes to be displayed
    private static final String[] COLS = {"Size", "Date", "Name", "Path"};
    // The table model to display the attributes for the sampled files
    private final DefaultTableModel model;
    // The table to display the model
    private final JTable table;

    // Triggers population of this table from this rule
    private final JButton sampleButton;

    private final Rule rule;

    /**
     * Constructor
     *
     * @param rule The Rule to status
     */
    public FileFilterStatusUI(Rule rule) {

        super();
        this.rule = rule;

        model = new DefaultTableModel();
        for (String col : COLS) {
            model.addColumn(col);
        }
        table = new JTable(model);
        table.getTableHeader().setResizingAllowed(false);
        table.setAutoCreateColumnsFromModel(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);
        table.getColumnModel().getColumn(3).setPreferredWidth(1000);
        model.setRowCount(MAX_ROWS);

        sampleButton = new JButton("Sample");
        sampleButton.setToolTipText("Loads the table with a sampling of files that match the filter criteria and that exist in the specified directories.");
        sampleButton.addActionListener(this);
    }

    public JButton getTestButton() {
        return sampleButton;
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public JTable getTable() {
        return table;
    }

    public Rule getRule() {
        return rule;
    }

    /**
     * The user his pressed this Test button. Walk the directory tree(s)
     * specified by this Rule's Monitor(s). Collect files matching this Rule's
     * filters and add to this model. Stop when the number of files collected is
     * about to exceed MAX_ROWS.
     *
     * @param e The event resulting from the pressing of the Test button
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (sampleButton.equals(e.getSource())) {

            model.setRowCount(0);
            model.fireTableDataChanged();

            /*
             * Update this Rule based on the current contents of all
             * all RuleEditorSubpanels.
             */
            LinkedList<RuleEditorSubpanel> result = new LinkedList<>();

            RuleEditorSubpanel.getRuleEditorSubpanels(table.getRootPane(), result);

            result.stream().forEach((ruleEditor) -> ruleEditor.harvest());

            /*
             Combine all FileFilters into a LogicalGroupFilter based on
             the current rule
             */
            LogicalGroupFilter groupFilter
                    = new LogicalGroupFilter(
                            rule.getLogicalGroup(), rule.getFileFilters());

            rule.getFileMonitors().stream().forEach((FileMonitor fileMonitor) -> {
                FileFilterTester fileFilterTester
                        = new FileFilterTester(
                                groupFilter, model, MAX_ROWS,
                                fileMonitor.isRecurse());

                Path startingDir = Paths.get(fileMonitor.getDirectoryName());

                try {

                    if (fileMonitor.isRecurse()) {
                        Files.walkFileTree(startingDir, fileFilterTester);
                    } else {
                        Files.walkFileTree(
                                startingDir, EnumSet.noneOf(FileVisitOption.class),
                                1, fileFilterTester);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(
                            Rule.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }
}
