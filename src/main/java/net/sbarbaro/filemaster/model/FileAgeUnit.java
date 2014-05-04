package net.sbarbaro.filemaster.model;

/**
 *
 * @author ajb
 */
public enum FileAgeUnit {

    SECONDS(1000),
    MINUTES(60),
    HOURS(60),
    DAYS(24),
    WEEKS(7),
    YEARS(52);

    private final int multiplier;

    /*
     Constructor
     */
    FileAgeUnit(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getMillis(int value) {

        int millis = 1;
        for (FileAgeUnit unit : values()) {
            millis *= unit.multiplier;
            if (unit == this) {
                break;
            }
        }
        return millis *= value;
    }

    @Override
    public String toString() {

        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();

    }
}
