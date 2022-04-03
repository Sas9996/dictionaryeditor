

package edu.kit.informatik.classeditor.entity;

/**
 * Contains the possible final states of an item.
 *
 * @author Sara
 * @version 1.0
 */
public enum Final {
    /**
     * The item is final.
     */
    FINAL("final"),
    /**
     * The item is not final.
     */
    NOT_FINAL("");
    private final String pattern;

    /**
     * Instantiates a new Final element with the given pattern.
     *
     * @param pattern the pattern of this element
     */
    Final(final String pattern) {
        this.pattern = pattern;
    }

    /**
     * Returns the pattern for a final input.
     *
     * @return the pattern for a final input
     */
    public static String getFinalPattern() {
        return "(" + FINAL.pattern + " " + ")?";
    }

    /**
     * Parses the given String to a Final.
     *
     * @param modifier the String containing the modifier
     *
     * @return the parsed Final or null
     */
    public static Final parseFromString(final String modifier) {
        if (modifier == null) {
            return NOT_FINAL;
        }
        for (final Final availableFinal : Final.values()) {
            if (modifier.startsWith(availableFinal.pattern)) {
                return availableFinal;
            }
        }
        return null;
    }

    /**
     * @return the pattern of this Final
     */
    public String getPattern() {
        return pattern;
    }
}
