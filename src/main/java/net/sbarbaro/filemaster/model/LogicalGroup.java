package net.sbarbaro.filemaster.model;

/**
 * LogicalGroup
 * <p>
 * {Purpose of This Class}
 * <p>
 * {Other Notes Relating to This Class (Optional)}
 * @author Anthony J. Barbaro (tony@abarbaro.net)
 * $LastChangedRevision: $
 * $LastChangedDate: $
 */
 public enum LogicalGroup {
    AND("All of the following"), OR("Any of the following");

    LogicalGroup(String text) {
        this.text = text;
    }
    private final String text;

    @Override
    public String toString() {
        return text;
    }
    
}
