

package edu.kit.informatik.classeditor.entity;

import edu.kit.informatik.classeditor.ui.Main;

/**
 * Encapsulates an enum as one of the central types of the type hierarchy.
 *
 * @author Sara
 * @version 1.0
 */
public class Enum extends NameableConstruct {
    /**
     * The string of a underlying type for the enum.
     */
    public static final String PATTERN = "enum";

    /**
     * Instantiates a new Enum with the given name.
     *
     * @param name the name of this Enum
     */
    public Enum(final String name) {
        super(name);
    }

    @Override public boolean addImplement(final NameableConstruct toBeImplemented) {
        return false;
    }

    @Override public boolean addExtend(final NameableConstruct toBeExtended) {
        return false;
    }

    @Override public String getFullName() {
        return PATTERN + Main.SEPARATOR + getName();
    }

    @Override public String getUnderlyingType() {
        return PATTERN;
    }

}
