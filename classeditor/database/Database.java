


package edu.kit.informatik.classeditor.database;

import edu.kit.informatik.classeditor.entity.Class;
import edu.kit.informatik.classeditor.entity.Construct;
import edu.kit.informatik.classeditor.entity.Enum;
import edu.kit.informatik.classeditor.entity.Interface;
import edu.kit.informatik.classeditor.entity.Method;
import edu.kit.informatik.classeditor.entity.NameableConstruct;
import edu.kit.informatik.classeditor.entity.PrimitiveType;
import edu.kit.informatik.classeditor.entity.Type;
import edu.kit.informatik.classeditor.entity.VisibilityModifier;
import edu.kit.informatik.classeditor.ui.ExecutionState;
import edu.kit.informatik.classeditor.ui.Main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * This class encapsulates the whole type hierarchy and provides methods to adapt it or query for information.
 *
 * @author Sara
 * @version 1.0
 */
public class Database {
    /**
     * A dictionary containing all constructs for this database.
     */
    private final Dictionary<NameableConstruct> constructs;
    /**
     * The execution state of this database.
     */
    private ExecutionState executionState;

    /**
     * Instantiates a new Database.
     */
    public Database() {
        executionState = ExecutionState.RUNNING;
        constructs = new Dictionary<>();
    }

    /**
     * Finds all methods in the given construct with the given name.
     *
     * @param construct the construct which contains the method
     * @param methodName the name of the method searched
     *
     * @return a String containing the signatures of all methods with that name
     */
    public static String findMethod(final NameableConstruct construct, final String methodName) {
        final String output = convertToOutputString(
                construct.listMethods().stream().filter(method -> method.getName().equals(methodName))
                        .collect(Collectors.toList()));
        return output != null
                ? output
                : Main.ERROR + "no methods with that name found";
    }

    /**
     * Adds the given method to the given construct.
     *
     * @param construct the construct to which the method shall be added
     * @param method the method to be added
     *
     * @return String containing information about the result of the addition
     */
    public static String addMethod(final NameableConstruct construct, final Method method) {
        return construct.addMethod(method);
    }

    /**
     * Executes the given listFunction and returns its result or an empty collection.
     *
     * @param listFunction a function supplying objects which shall be listed
     *
     * @return a collection with the executed listFunction
     */
    public static Collection<? extends Nameable> list(final Supplier<Collection<? extends Nameable>> listFunction) {
        final Collection<? extends Nameable> output = listFunction.get();
        return output != null
                ? output
                : Collections.emptyList();
    }

    /**
     * Produces a string representation of the given collection with the signature of the elements of the list,
     * separated by {@link Main#LINE_SEPARATOR}.
     *
     * @param list a collection containing the output that shall be converted to a string
     *
     * @return the string output of the list as stated in the assignment
     */
    private static String convertToOutputString(final Collection<? extends Nameable> list) {
        if (list.isEmpty()) {
            return null;
        }
        final StringJoiner joiner = new StringJoiner(Main.LINE_SEPARATOR);
        list.forEach(element -> joiner.add(element.getSignature()));
        return joiner.toString();
    }

    /**
     * Produces a string representation of all constructs in this database.
     *
     * @param separator the separator to use
     *
     * @return a String containing the constructs in this database
     */
    public String listConstructs(final String separator) {
        return constructs.list(separator);
    }

    /**
     * Returns whether or not this database is active.
     *
     * @return whether or not this database is active
     */
    public boolean isActive() {
        return executionState == ExecutionState.RUNNING;
    }

    /**
     * Quits this database so {@link #isActive()} will return {@code false}.
     */
    public void quit() {
        executionState = ExecutionState.EXITED;
    }

    /**
     * Adds the given class to this database.
     *
     * @param newClass the class to be added
     *
     * @return whether or not the new class could be added
     */
    public boolean addClass(final Class newClass) {
        return nameAvailable(newClass.getName()) && constructs.add(newClass) != null;
    }

    /**
     * Adds the given Interface to this database.
     *
     * @param newInterface the Interface to be added
     *
     * @return whether or not the new Interface could be added
     */
    public boolean addInterface(final Interface newInterface) {
        return nameAvailable(newInterface.getName()) && constructs.add(newInterface) != null;
    }

    /**
     * Adds the given Enum to this database.
     *
     * @param newEnum the Enum to be added
     *
     * @return whether or not the new Enum could be added
     */
    public boolean addEnum(final Enum newEnum) {
        return nameAvailable(newEnum.getName()) && constructs.add(newEnum) != null;
    }

    /**
     * Checks if the given name is available as construct name.
     *
     * @param name the name to be checked
     *
     * @return whether or not the given name is available as a construct name
     */
    private boolean nameAvailable(final String name) {
        return !constructs.containsKey(name);
    }

    /**
     * Returns the construct with the given name if possible, null otherwise.
     *
     * @param name the name of the construct
     *
     * @return the construct or null
     */
    public NameableConstruct getConstruct(final String name) {
        return constructs.get(name);
    }

    /**
     * Checks if a type with the given type string is available in this database. Ignores parenthesis.
     *
     * @param typeString the String containing the type to be checked
     *
     * @return the type if one exists or null
     */
    public Type typeAvailable(final String typeString) {
        final String parsedType = typeString
                .replaceAll("[" + Method.PARAMETER_LIST_START + Method.PARAMETER_LIST_END + "]", Main.EMPTY_STRING);
        if (PrimitiveType.parseFromString(parsedType) != null) {
            return PrimitiveType.parseFromString(parsedType);
        }
        if (constructs.containsKey(parsedType)) {
            return constructs.get(parsedType);
        }
        return null;
    }

    /**
     * Produces a string containing the representations of all directly contained attributes of the construct with the
     * given name. {@link Construct#listAttributes()}
     *
     * @param constructName the name of the construct
     *
     * @return a string representation of all attributes of this construct
     */
    public String listAttributes(final String constructName) {
        return getConstruct(constructName) != null
                ? convertToOutputString(list(getConstruct(constructName)::listAttributes))
                : null;
    }

    /**
     * Produces a string containing the representations of all attributes of the construct with the given name.
     * {@link Construct#listAllAttributes(VisibilityModifier...)}
     *
     * @param constructName the name of the construct
     *
     * @return a string representation of all attributes of this construct
     */
    public String listAllAttributes(final String constructName) {
        return getConstruct(constructName) != null
                ? convertToOutputString(list(getConstruct(constructName)::listAllAttributes))
                : null;
    }

    /**
     * Produces a string containing the representations of all directly contained methods of the construct with the
     * given name. {@link Construct#listMethods()}
     *
     * @param constructName the name of the construct
     *
     * @return a string representation of all methods of this construct
     */
    public String listMethods(final String constructName) {
        return getConstruct(constructName) != null
                ? convertToOutputString(list(getConstruct(constructName)::listMethods))
                : null;
    }

    /**
     * Produces a string containing the representations of all methods of the construct with the given name.
     * {@link Construct#listAllMethods(VisibilityModifier...)}
     *
     * @param constructName the name of the construct
     *
     * @return a string representation of all methods of this construct
     */
    public String listAllMethods(final String constructName) {
        final NameableConstruct construct = getConstruct(constructName);
        if (construct == null) {
            return null;
        }
        final TreeSet<Method> sortedMethods = new TreeSet<>(Method.lexicographicallySortingComparator());
        sortedMethods.addAll(construct.listAllMethods(VisibilityModifier.PRIVATE));
        return convertToOutputString(sortedMethods);
    }

    /**
     * Produces a string containing the representations of all shadowing attributes of the construct with the
     * given name.{@link Construct#listShadowingAttributes()}
     *
     * @param constructName the name of the construct
     *
     * @return a string representation of all attributes of this construct
     */
    public String listShadowingAttributes(final String constructName) {
        return getConstruct(constructName) != null
                ? convertToOutputString(list(getConstruct(constructName)::listShadowingAttributes))
                : null;
    }

    /**
     * Produces a string containing the representations of all methods which are overridden by the given one in the
     * construct with the given name. {@link Construct#findMethodOverride(Method, List)}
     *
     * @param constructName the name of the construct
     * @param method the method to search for
     *
     * @return a string representation of all methods
     */
    public String findMethodOverride(final String constructName, final Method method) {
        final NameableConstruct construct = getConstruct(constructName);
        if (construct == null) {
            return null;
        }
        final Collection<Method> output = construct.findMethodOverride(method, new ArrayList<>());
        if (output != null) {
            return convertToOutputString(output);
        }
        return Main.ERROR + "nothing found";
    }

}
