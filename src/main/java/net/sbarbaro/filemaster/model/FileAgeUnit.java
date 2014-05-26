package net.sbarbaro.filemaster.model;

/**
 * FileAgeUnit
 * <p>
 * An enumeration of file age time units to use for assessing acceptance of
 * files based on their age.
 * <p>
 * @author Anthony J. Barbaro (tony@abarbaro.net) 
 */
public enum FileAgeUnit {

    SECONDS(1000),
    MINUTES(60),
    HOURS(60),
    DAYS(24),
    WEEKS(7),
    YEARS(52);

    public long getMillis(int value) {

        long millis = value;

        for (FileAgeUnit unit : values()) {
            millis *= unit.multiplier;
            if (unit == this) {
                break;
            }
        }
        return millis;
    }

    @Override
    public String toString() {

        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();

    }

    private final long multiplier;

    /*
     Privaet constructor
     */
    private FileAgeUnit(int multiplier) {
        this.multiplier = multiplier;
    }
}
