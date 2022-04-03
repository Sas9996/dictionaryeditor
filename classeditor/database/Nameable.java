

package edu.kit.informatik.classeditor.database;

import java.util.Objects;

/**
 * Encapsulates an item that has a name and a signature.
 *
 * @author Sara
 * @version 1.0
 */
public abstract class Nameable {
    /**
     * The name of this Nameable.
     */
    private final String name;

    /**
     * Instantiates a new Nameable with the given name string.
     *
     * @param name the name of this Nameable
     */
    public Nameable(final String name) {
        this.name = name;
    }

    /**
     * Returns the name of this Nameable.
     *
     * @return the name of this Nameable
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a signature for this Nameable object.
     *
     * @return the signature of this Nameable
     */
    public abstract String getSignature();

    @Override public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Nameable)) {
            return false;
        }
        final Nameable nameable = (Nameable) o;
        return Objects.equals(name, nameable.name);
    }

    @Override public int hashCode() {
        return Objects.hash(name);
    }

    @Override public String toString() {
        return name;
    }

}
