package net.sbarbaro.filemaster.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Root element for this application's data
 *
 * @author steven
 */
public class FileMaster implements Serializable {

    private static final long serialVersionUID = 2422882179598027217L;

    private String serializedFileName;

    /*
     The current list of Rule
     */
    private final List<Rule> rules;

    /**
     * Default constructor
     */
    public FileMaster() {
        this(getDefaultSerializedFileName());
    }

    /**
     * Constructor
     *
     * @param serializedFileName The name of the file that contains the
     * persisted state of this FileMaster
     */
    public FileMaster(String serializedFileName) {
        this.serializedFileName = serializedFileName;
        this.rules = new ArrayList<>();
    }

    /**
     * Sets this serializedFileName
     * @param serializedFileName The name of the serialized file to set
     */
    public void setSerializedFileName(String serializedFileName) {
        this.serializedFileName = serializedFileName;
    }

    /**
     * @return the name of the serialized file that contains/will contain the
     * state of this FileMaster
     */
    public String getSerializedFileName() {
        return serializedFileName;
    }

    /**
     * @return A reference to the serialized File
     */
    public File getSerializedFile() {
        return new File(serializedFileName);
    }

    /**
     * @return All rules defined for this FileMaster
     */
    public List<Rule> getRules() {
        return rules;
    }

    /**
     * @return A unique set of file system directories to monitor, and the Rules
     * that apply to each directory.
     */
    public Map<String, List<Rule>> getPathMap() {

        Map<String, List<Rule>> map = new HashMap<>();

        for (Rule rule : rules) {

            for (FileMonitor fileMonitor : rule.getFileMonitors()) {

                String directoryName = fileMonitor.getDirectoryName();

                List<Rule> _rules = null;

                if (map.containsKey(directoryName)) {

                    _rules = map.get(directoryName);

                } else {

                    _rules = new ArrayList<>();
                    map.put(directoryName, _rules);

                }

                _rules.add(rule);

            }

        }

        return map;
    }

    /**
     * @return A unique set of file system directories to monitor, and the
     * active Rules that apply to each directory.
     */
    public Map<String, List<Rule>> getActivePathMap() {

        final Map<String, List<Rule>> map = getPathMap();

        for (String directoryName : map.keySet()) {

            Iterator<Rule> ruleIter = map.get(directoryName).iterator();

            while (ruleIter.hasNext()) {

                Rule rule = ruleIter.next();

                if (rule.isActive()) {

                    continue;

                } else {

                    ruleIter.remove();

                }
            }
        }

        return map;
    }

    /**
     *
     * @return A unique file name based on this application's name, the user's
     * id, and the machines MAC address. For example,
     * FileMaster-SAB-E4-CE-8F-51-7D-87.out
     */
    public static String getDefaultSerializedFileName() {
        String name = "FileMaster-";
        name += System.getProperty("user.name");

        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            NetworkInterface net = NetworkInterface.getByInetAddress(ip);
            byte[] mac = net.getHardwareAddress();
            
            name += '-';
            for (int i = 0; i < mac.length; i++) {
                name += String.format("%02X", mac[i]);
                if (i < mac.length - 1) {
                    name += '-';
                }
            }

        } catch (UnknownHostException | SocketException ex) {
            Logger.getLogger(FileMaster.class.getName()).log(Level.WARNING, null, ex);

        }
        name += ".out";
        
        return name;
    }

    /**
     * @return A reference to the serialized file
     */
    public static File getDefaultSerializedFile() {
        return new File(getDefaultSerializedFileName());
    }

    /**
     * Constructs an instance of FileMaster based on the given serialized input
     * file
     *
     * @param fileIn A serialized FileMaster file
     * @return A FileMaster
     * @throws FileNotFoundException when fileIn cannot be found
     * @throws IOException when there is a problem accessing fileIn
     * @throws ClassNotFoundException when there is a class directoryName issue
     */
    public static FileMaster deserialize(File fileIn)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileIn);
        ObjectInputStream ois = new ObjectInputStream(fis);
        final FileMaster fileMaster = (FileMaster) ois.readObject();
        ois.close();
        fis.close();
        return fileMaster;
    }

    public void serialize() throws IOException {
        serialize(this.getSerializedFile());
    }
    /**
     * Serializes this FileMaster instance to the given output file
     *
     * @param fileOut A reference to the output file
     * @throws FileNotFoundException If the output file cannot be found
     * @throws IOException when there is a problem accessing the output file
     */
    public void serialize(File fileOut)
            throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(fileOut);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(this);
        oos.close();
        fos.close();
        Logger
                .getLogger(FileMaster.class
                        .getName()).log(Level.WARNING, "Saved");
    }
}
