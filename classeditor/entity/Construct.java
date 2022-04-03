


package edu.kit.informatik.classeditor.entity;

import java.util.Collection;
import java.util.List;

/**
 * A construct is the extendable point of this type system and describes neccessary methods for a construct.
 *
 * @author Sara
 * @version 1.0
 */
public interface Construct extends Type {

    /**
     * The regular expression pattern for a construct, consisting of a class, an interface or an enum pattern.
     *
     * @return the Pattern for a construct
     */
    static String getConstructPattern() {
        return "(" + Class.PATTERN + "|" + Interface.PATTERN + "|" + Enum.PATTERN + ")";
    }

    /**
     * The regular expression pattern for a construct name.
     *
     * @return the pattern of a construct name (the same as the pattern for a type)
     */
    static String getConstructNamePattern() {
        return Type.getTypePattern();
    }

    /**
     * The separator used in in- and output to separate a construct from contained elements.
     *
     * @return the separator for a construct
     */
    static String getConstructSeparator() {
        return "::";
    }

    /**
     * Returns a collection of all parent constructs (Interfaces or classes) of this construct. Filter the list with
     * the collection given.
     *
     * @param constructs a list that stores parents
     *
     * @return the list of all parents of this construct
     */
    Collection<Construct> getParents(Collection<Construct> constructs);

    /**
     * Checks if this construct can implement the given NameableConstruct and implements it if possible.
     *
     * @param toBeImplemented the NameableConstruct that should be implemented
     *
     * @return whether or not the NameableConstruct could be implemented by this construct
     */
    boolean addImplement(NameableConstruct toBeImplemented);

    /**
     * Checks if this construct can extend the given NameableConstruct and extends it if possible.
     *
     * @param toBeExtended the NameableConstruct that should be extended
     *
     * @return whether or not the NameableConstruct could be extended by this construct
     */
    boolean addExtend(NameableConstruct toBeExtended);

    /**
     * Tries to add the given attribute to this construct. Attributes must have a unique name in a construct.
     *
     * @param attribute the attribute to add to this construct
     *
     * @return whether or not the attribute could be added
     */
    boolean addAttribute(Attribute attribute);

    /**
     * Tries to add the given method to this construct and returns how the method was added.
     *
     * @param method the method to add
     *
     * @return a message containing information about the adding
     */
    String addMethod(Method method);

    /**
     * Returns the underlying type (a construct is a type{@link Type#getType()} and has an underlying type).
     *
     * @return the underlying type of the construct
     */
    String getUnderlyingType();

    /**
     * Returns a collection of the directly contained attributes.
     *
     * @return a collection of the attributes directly contained in this construct
     */
    Collection<Attribute> listAttributes();

    /**
     * Returns a collection of the directly contained methods.
     *
     * @return a collection of the methods directly contained in this construct
     */
    Collection<Method> listMethods();

    /**
     * Returns a collection of the attributes contained in this construct or any parental structures of this
     * construct. Only shadowing attributes are returned, for shadowed attributes use
     * {@link #listShadowingAttributes()}.
     *
     * @param modifier the visibility modifiers not considered in the list
     *
     * @return a collection of the attributes contained in this construct
     */
    Collection<Attribute> listAllAttributes(VisibilityModifier... modifier);

    /**
     * Returns a collection of the attributes contained in this construct or any parental structures of this
     * construct. Shadowing and shadowed attributes are returned.
     *
     * @return a collection of the attributes contained in this construct
     */
    Collection<Attribute> listShadowingAttributes();

    /**
     * Returns a collection of the methods contained in this construct or any parental structures of this
     * construct. Only methods found by dynamic binding are returned.
     *
     * @param modifier the visibility modifiers not considered in the list
     *
     * @return a collection of the methods contained in this construct
     */
    Collection<Method> listAllMethods(VisibilityModifier... modifier);

    /**
     * Returns a collection of the methods contained in this construct or any parental structures of this
     * construct with the given signature.
     *
     * @param method the method to search for
     * @param occurrences the already found occurrences (the method itself)
     *
     * @return a collection of the methods contained in this construct
     */
    Collection<Method> findMethodOverride(Method method, List<Method> occurrences);

}
