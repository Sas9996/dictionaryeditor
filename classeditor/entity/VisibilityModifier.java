
 

package edu.kit.informatik.classeditor.entity;

/**
 * Contains the possible visibility states of an item.
 *
 * @author Sara
 * @version 1.0
 */
public enum VisibilityModifier {
    /**
     * The item has private access.
     */
    PRIVATE("private"),
    /**
     * The item has protected access.
     */
    PROTECTED("protected"),
    /**
     * The item has default access.
     */
    DEFAULT(""),
    /**
     * The item has public access.
     */
    PUBLIC("public");

    private final String pattern;

    /**
     * Instantiates a new {@link VisibilityModifier} element with the given pattern.
     *
     * @param pattern the pattern of this element
     */
    VisibilityModifier(final String pattern) {
        this.pattern = pattern;
    }

    /**
     * Returns the pattern for a {@link VisibilityModifier} input.
     *
     * @return the pattern for a {@link VisibilityModifier} input
     */
    public static String getVisibilityModifierPattern() {
        return "(" + PRIVATE.pattern + " |" + PROTECTED.pattern + " |" + PUBLIC.pattern + " )?";
    }

    /**
     * Parses the given String to a {@link VisibilityModifier}.
     *
     * @param modifier the String containing the modifier
     *
     * @return the parsed {@link VisibilityModifier} or null
     */
    public static VisibilityModifier parseFromString(final String modifier) {
        if (modifier == null) {
            return DEFAULT;
        }
        for (final VisibilityModifier availableType : VisibilityModifier.values()) {
            if (modifier.trim().equals(availableType.pattern)) {
                return availableType;
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
