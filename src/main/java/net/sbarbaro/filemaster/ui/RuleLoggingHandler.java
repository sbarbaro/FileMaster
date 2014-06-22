package net.sbarbaro.filemaster.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Filter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import net.sbarbaro.filemaster.io.Runner;

/**
 * RuleLoggingHandler
 * <p>
 * JTable-based handler for java.util.Logger events
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net) 
 */
public class RuleLoggingHandler extends Handler {

    private static final int MAX_ROWS = 30;
    private static final DateFormat DF = new SimpleDateFormat("dd-MMM-yy  hh:mm:ss");
    private static final String[] COLS = {"Time", "Rule", "Message"};
    private final DefaultTableModel model;
    private final JTable table;
    private final JScrollPane scrollPane;
    private final Filter filter;

    public RuleLoggingHandler() {
        model = new DefaultTableModel(){
            private static final long serialVersionUID = 2793957061432039023L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        for (String col : COLS) {
            model.addColumn(col);
        }
        table = new JTable(model);
        table.getTableHeader().setResizingAllowed(false);
        table.setAutoCreateColumnsFromModel(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(550);

        scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        filter = new RuntimeFilter();

    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    @Override
    public void publish(LogRecord record) {

        if (isLoggable(record)) {

            while (model.getRowCount() > MAX_ROWS) {
                model.removeRow(0);
            }

            model.addRow(new Object[]{
                DF.format(new Date(record.getMillis())),
                record.getLoggerName(),
                record.getMessage()});
            
            model.fireTableRowsInserted(
                    model.getRowCount()-1, model.getRowCount()-1);
        }

    }

    @Override
    public void flush() {
        System.out.println("In flush");
    }

    @Override
    public void close() throws SecurityException {
        System.out.println("In close");
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    class RuntimeFilter implements Filter {

        private final Class[] LOGGER_CLASS = {Runner.class};

        @Override
        public boolean isLoggable(LogRecord record) {
            boolean isLoggable = false;

            if (record.getParameters() != null && 
                    record.getParameters().length > 0 &&
                    record.getParameters()[0] instanceof Class) {
                
                Class loggerClassIn = (Class) record.getParameters()[0];

                
                for (Class loggerClass : LOGGER_CLASS) {

                    if (loggerClass.equals(loggerClassIn)) {
                        isLoggable = true;
                        break;
                    }
                }
            }

            return isLoggable;
        }

    }
}
