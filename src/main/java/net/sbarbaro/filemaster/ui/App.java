package net.sbarbaro.filemaster.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.swing.*;
import net.sbarbaro.filemaster.model.FileMaster;
import net.sbarbaro.filemaster.model.Rule;

/**
 * App
 * <p>
 * The main application for FileMaster
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net)
 */
public class App extends JFrame {

    private static final long serialVersionUID = 7809545267336562627L;

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    private static JPanel rulesPane, logPane, newPane;

    public static App INSTANCE;
    public static final String FILE_NAME = "FileMaster.out";
    public static final File FILE = new File(FILE_NAME);

    /*
    Private constructor
    */
    private App(FileMaster fileMaster) {

        super("JFileMaster");

        ImageIcon imageIcon = new ImageIcon("folder_out.png");
        super.setIconImage(imageIcon.getImage());

        final JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);

        // Rules tab
        rulesPane = new RuleManager(fileMaster, tabbedPane);

        tabbedPane.addTab("Rules", rulesPane);
        ((RuleManager) rulesPane).layoutPanel();

        // Log tab
        Logger log = LogManager.getLogManager().getLogger("");
        for (Handler h : log.getHandlers()) {
            if (h instanceof RuleLoggingHandler) {
                tabbedPane.addTab("Log", ((RuleLoggingHandler) h).getScrollPane());
                break;
            }
        }

        LOGGER.fine("Logger Initialized");

        Rule defaultRule = null;
        if (fileMaster.getRules().isEmpty()) {
            defaultRule = new Rule();
            fileMaster.getRules().add(defaultRule);
            newPane = new RuleEditor(fileMaster, defaultRule);
            tabbedPane.addTab("New Rule", newPane);

        }

        App.this.addWindowStateListener(new WindowStateListener() {

            @Override
            public void windowStateChanged(WindowEvent e) {
                System.out.println(App.this.getSize());
            }
        });
        this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(932, 600));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    /**
     * The main method for FileMaster
     * @param args no args are needed
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {

        // Initialize logger
        System.setProperty(App.class.getName(), Level.ALL.getName());
        LogManager.getLogManager().readConfiguration();

        // Initialize UI look and feel
        String[] lnfNames = {"Nimbus", "Seaglass"};
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (lnfNames[0].equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | 
                InstantiationException | 
                IllegalAccessException | 
                UnsupportedLookAndFeelException e) {
            LOGGER.warning(e.getMessage());
        }

        // Instantiate FileMaster from serialezed output file
        try {

            final FileMaster fileMaster = FileMaster.deserialize(FILE);

            // invokeLater required by OSX
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {

                    INSTANCE = new App(fileMaster);

                }
            });
            
        } catch (IOException | ClassNotFoundException t) {

            // Serialized output file is not available or is corrupted.
            // Start over with a new FileMaster instance
            
            Logger.getLogger(App.class.getName()).log(Level.WARNING, null, t);
            // invokeLater required by OSX
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {

                    INSTANCE = new App(new FileMaster());

                }
            });
        }

    }
}
