

package edu.kit.informatik.classeditor.entity;

import edu.kit.informatik.classeditor.database.Nameable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Encapsulates a nameable construct as super type for any concrete types in the hierarchy as a class or an enum.
 *
 * @author Sara
 * @version 1.0
 */
public abstract class NameableConstruct extends Nameable implements Construct, Comparable<NameableConstruct> {

    private final Set<Attribute> attributes = new TreeSet<>();
    private final Set<Method> methods = new TreeSet<>();

    /**
     * Instantiates a new NameableConstruct with the given name.
     *
     * @param name the name of this class
     */
    public NameableConstruct(final String name) {
        super(name);
    }

    /**
     * Returns the full name of this construct, opposing to the simple name with {@link #getName()}.
     *
     * @return the full name of this nameable construct
     */
    public abstract String getFullName();

    @Override public String getSignature() {
        return getFullName();
    }

    @Override public boolean addAttribute(final Attribute attribute) {
        return attributes.add(attribute);
    }

    @Override public String addMethod(final Method method) {
        return methods.add(method)
                ? "OK"
                : "Error, could not add method";
    }

    @Override public Collection<Attribute> listAttributes() {
        return attributes.size() > 0
                ? Collections.unmodifiableSet(attributes)
                : Collections.emptySet();
    }

    @Override public Collection<Method> listMethods() {
        final Set<Method> sortedMethods = new TreeSet<>(Method.lexicographicallySortingComparator());
        sortedMethods.addAll(methods);
        return sortedMethods.size() > 0
                ? Collections.unmodifiableSet(sortedMethods)
                : Collections.emptySet();
    }

    @Override public int compareTo(final NameableConstruct o) {
        return getName().compareTo(o.getName());
    }

    @Override public String getType() {
        return getName();
    }

    @Override public Collection<Attribute> listAllAttributes(final VisibilityModifier... modifier) {
        final Collection<Attribute> attributes = listAttributes();
        final Collection<Attribute> availableAttributes = new TreeSet<>();
        for (final Attribute attribute : attributes) {
            boolean available = true;
            for (final VisibilityModifier forbiddenModifier : modifier) {
                available = available && attribute.getVisibilityModifier() != forbiddenModifier;
            }
            if (available) {
                availableAttributes.add(attribute);
            }
        }
        return availableAttributes;
    }

    @Override public Collection<Attribute> listShadowingAttributes() {
        return listAttributes();
    }

    @Override public Collection<Construct> getParents(final Collection<Construct> constructs) {
        return constructs;
    }

    @Override public Collection<Method> listAllMethods(final VisibilityModifier... modifier) {
        final Collection<Method> methods = listMethods();
        final Collection<Method> availableMethods = new TreeSet<>();
        for (final Method method : methods) {
            boolean available = true;
            for (final VisibilityModifier forbiddenModifier : modifier) {
                available = available && method.getVisibilityModifier() != forbiddenModifier;
            }
            if (available) {
                availableMethods.add(method);
            }
        }
        return availableMethods;
    }

    @Override public Collection<Method> findMethodOverride(final Method method, final List<Method> occurrences) {
        return listMethods();
    }

}
