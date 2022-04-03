
 
package edu.kit.informatik.classeditor.entity;

import edu.kit.informatik.classeditor.database.Nameable;
import edu.kit.informatik.classeditor.ui.Main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Encapsulates a class as one of the central types of the type hierarchy.
 *
 * @author Sara
 * @version 1.0
 */
public class Class extends NameableConstruct {

    /**
     * The string of a underlying type for the class.
     */
    public static final String PATTERN = "class";
    /**
     * The string of an extends relationship.
     */
    public static final String EXTENDS = "extends";
    /**
     * The string of an implements relationship.
     */
    public static final String IMPLEMENTS = "implements";
    private final Set<Interface> interfaces = new TreeSet<>();
    private Class parentClass;

    /**
     * Instantiates a new Class with the given name.
     *
     * @param name the name of this class
     */
    public Class(final String name) {
        super(name);
    }

    /**
     * Produces a string representation of the parents of this class.
     *
     * @param separator the separator used to separate the parent representations
     *
     * @return a string containing the representations of the parent structures of this class
     */
    public String listParents(final String separator) {
        final StringJoiner joiner = new StringJoiner(separator);
        final List<Interface> sortedParents = new ArrayList<>(interfaces);
        Collections.sort(sortedParents);
        for (final Interface element : sortedParents) {
            joiner.add(element.getName());
        }
        return joiner.toString();
    }

    private boolean hasInterfaces() {
        return interfaces.size() > 0;
    }

    @Override public String getFullName() {
        // @formatter:off
        return PATTERN + Main.SEPARATOR + getName()
               + ((parentClass != null)
                        ? Main.SEPARATOR + EXTENDS + Main.SEPARATOR + parentClass.getName()
                        : Main.EMPTY_STRING)
               + (hasInterfaces()
                        ? Main.SEPARATOR + IMPLEMENTS + Main.SEPARATOR + listParents(",")
                        : Main.EMPTY_STRING);
        // @formatter:on
    }

    @Override public String getUnderlyingType() {
        return PATTERN;
    }

    @Override public boolean addImplement(final NameableConstruct toBeImplemented) {
        if (!toBeImplemented.getUnderlyingType().equals(Interface.PATTERN)) {
            return false;
        }
        final Collection<Method> methods = listAllMethods(VisibilityModifier.PRIVATE);
        for (final Method method : toBeImplemented.listAllMethods(VisibilityModifier.PRIVATE)) {
            if (!methods.contains(method)) {
                return false;
            }
        }
        return interfaces.add((Interface) toBeImplemented);
    }

    @Override public boolean addExtend(final NameableConstruct toBeExtended) {
        try {
            final Collection<Construct> parents = getParents(new ArrayList<>());
            if (parents.contains(toBeExtended)) {
                return false;
            }
        } catch (final IllegalStateException e) {
            return false;
        }
        try {
            final Collection<Construct> parents = toBeExtended.getParents(new ArrayList<>());
            if (parents.contains(this)) {
                return false;
            }
        } catch (final IllegalStateException e) {
            return false;
        }
        if (parentClass != null) {
            return false;
        }
        if (!toBeExtended.getUnderlyingType().equals(Class.PATTERN)) {
            return false;
        }
        parentClass = (Class) toBeExtended;
        return true;
    }

    @Override public Collection<Construct> getParents(final Collection<Construct> constructs) {
        if (constructs.contains(this)) {
            throw new IllegalStateException("This class " + getName() + " is already part of the " + "type hierarchy");
        }
        constructs.add(this);
        for (final Interface interface1 : interfaces) {
            if (constructs.contains(interface1)) {
                throw new IllegalStateException(
                        "This interface " + interface1.getName() + " is already part of the " + "type hierarchy");
            }
            constructs.add(interface1);
        }
        if (parentClass != null) {
            parentClass.getParents(constructs);
        }
        return constructs;
    }

    @Override public Collection<Attribute> listAllAttributes(final VisibilityModifier... visibilityModifier) {
        final TreeSet<Attribute> attributes = new TreeSet<>(Comparator.comparing(Nameable::getName));
        return listAttributes(attributes, new TreeSet<>());
    }

    @Override public Collection<Attribute> listShadowingAttributes() {
        return listAttributes(new TreeSet<>(), new TreeSet<>());
    }

    private Collection<Attribute> listAttributes(final Collection<Attribute> collectCollection,
            final Collection<Attribute> sortCollection) {
        if (super.listAllAttributes() != null) {
            collectCollection.addAll(super.listAllAttributes());
        }
        for (final Interface interface1 : interfaces) {
            if (interface1.listAllAttributes() != null) {
                collectCollection.addAll(interface1.listAllAttributes());
            }
        }
        if (parentClass != null) {
            collectCollection.addAll(parentClass.listAllAttributes(VisibilityModifier.PRIVATE).stream()
                    .filter(method -> method.getVisibilityModifier() != VisibilityModifier.PRIVATE)
                    .collect(Collectors.toSet()));

        }
        sortCollection.addAll(collectCollection);
        return sortCollection;
    }

    private Collection<Method> listMethods(final Collection<Method> collectCollection,
            final Collection<Method> sortCollection) {
        if (super.listAllMethods() != null) {
            collectCollection.addAll(super.listAllMethods());
        }
        for (final Interface interface1 : interfaces) {
            if (interface1.listAllMethods(VisibilityModifier.PRIVATE) != null) {
                collectCollection.addAll(interface1.listAllMethods(VisibilityModifier.PRIVATE));
            }
        }
        if (parentClass != null) {
            collectCollection.addAll(parentClass.listAllMethods(VisibilityModifier.PRIVATE).stream()
                    .filter(method -> method.getVisibilityModifier() != VisibilityModifier.PRIVATE)
                    .collect(Collectors.toSet()));
        }
        sortCollection.addAll(collectCollection);
        return sortCollection;
    }

    @Override public Collection<Method> listAllMethods(final VisibilityModifier... visibilityModifier) {
        final Collection<Method> methods = listMethods(new HashSet<>(), new HashSet<>());
        return methods.stream().filter(method -> !(!method.getConstruct().equals(this)
                                                   && method.getVisibilityModifier() == VisibilityModifier.PRIVATE))
                .collect(Collectors.toSet());
    }

    @Override public Collection<Method> findMethodOverride(final Method method, final List<Method> occurrences) {
        final List<Method> modifiedList = new ArrayList<Method>() {
            private static final long serialVersionUID = 8100373402642634732L;

            @Override public boolean add(final Method method) {
                // add the first method, no matter what it is
                if (size() == 0) {
                    return super.add(method);
                } else {
                    // only add subsequent methods if the are equal to the first one (refer to the equals
                    // implementation of Method)
                    if (get(0).equals(method)) {
                        return super.add(method);
                    }
                }
                return false;
            }

            @Override public boolean addAll(final Collection<? extends Method> c) {
                boolean result = true;
                for (final Method method : c) {
                    result = add(method) && result;
                }
                return result;
            }
        };
        modifiedList.add(method);
        final Collection<Method> methods = listMethods(modifiedList, new ArrayList<>());
        // remove the first one cause it added twice, once the line above and then while searching for others
        methods.remove(method);
        return methods;
    }

    @Override public boolean equals(final Object o) {
        return super.equals(o);
    }

    @Override public int hashCode() {
        return super.hashCode();
    }

    @Override public String addMethod(final Method method) {
        final Collection<Method> methods = listAllMethods(VisibilityModifier.PRIVATE);
        if (methods.contains(method)) {
            for (final Method availableMethod : methods) {
                if (availableMethod.equals(method)) {
                    if (availableMethod.getFinalState() == Final.FINAL) {
                        return Main.ERROR + "method to override is final";
                    }
                }
            }
        }
        final String output = super.addMethod(method);
        if (methods.contains(method)) {
            if (output.equals("OK")) {
                for (final Method method1 : methods) {
                    if (!method1.getConstruct().equals(this) && method1.equals(method)) {
                        return "Override " + method1.getSignature();
                    }
                }
            }
        }
        return output;
    }

}
