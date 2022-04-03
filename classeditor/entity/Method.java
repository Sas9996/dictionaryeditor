
 

package edu.kit.informatik.classeditor.entity;

import edu.kit.informatik.classeditor.database.Database;
import edu.kit.informatik.classeditor.database.Nameable;
import edu.kit.informatik.classeditor.ui.Main;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.MatchResult;

/**
 * @author Sara
 * @version 1.0
 */
public class Method extends Nameable implements Comparable<Method>, Containable {

    /**
     * The index of the construct name in the input format specified in the method.
     */
    public static final int CONSTRUCT_INDEX = 1;
    /**
     * The index of the visibilityModifier in the input format specified in the method.
     */
    public static final int VISIBILITY_INDEX = 2;
    /**
     * The index of the final state in the input format specified in the method.
     */
    public static final int FINAL_INDEX = 3;
    /**
     * The index of the method name in the input format specified in the method.
     */
    public static final int METHOD_NAME_INDEX = 4;
    /**
     * The index of the start of the parameter list in the input format specified in the method.
     */
    public static final int METHOD_PARAMETER_START_INDEX = 5;
    /**
     * The separator between types in the parameter list.
     */
    public static final String TYPE_SEPARATOR = ",";
    /**
     * The escaped start of the parameter list.
     */
    public static final String PARAMETER_LIST_START_ESCAPED = "\\(";
    /**
     * The start of the parameter list.
     */
    public static final String PARAMETER_LIST_START = "(";
    /**
     * The escaped end of the parameter list.
     */
    public static final String PARAMETER_LIST_END_ESCAPED = "\\)";
    /**
     * The end of the parameter list.
     */
    public static final String PARAMETER_LIST_END = ")";
    /**
     * The character used to separate the parameter list from the return type.
     */
    public static final String RETURN_TYPE_SEPARATOR = ":";
    /**
     * The regular expression for the method parameter list.
     */
    public static final String METHOD_PARAMETER_PATTERN = "(" + PARAMETER_LIST_START_ESCAPED + Type
            .getCaseInsensitivePattern() + "(" + TYPE_SEPARATOR + "" + Type.getCaseInsensitivePattern() + ")*"
                                                          + PARAMETER_LIST_END_ESCAPED + ")";
    /**
     * The regular expression for a method name pattern.
     */
    public static final String METHOD_NAME_PATTERN = Attribute.VARIABLE_NAME_PATTERN;
    /**
     * The complete regular expression for the signature of a method.
     */
    public static final String METHOD_SIGNATURE_PATTERN = Construct.getConstructNamePattern() + Construct
            .getConstructSeparator() + Main.SEPARATOR + VisibilityModifier.getVisibilityModifierPattern() + Final
                                                                  .getFinalPattern() + METHOD_NAME_PATTERN
                                                          + METHOD_PARAMETER_PATTERN + RETURN_TYPE_SEPARATOR + Type
                                                                  .getCaseInsensitivePattern();
    private final VisibilityModifier visibilityModifier;
    private final Final finalState;
    private final Type[] parameters;
    private final Type returnType;
    private final NameableConstruct construct;

    /**
     * Instantiates a new Method with the given parameters. No checks are performed.
     *
     * @param name the name of this method
     * @param visibilityModifier the visibilityModifier of this method
     * @param finalState the final state of this method
     * @param parameters the parameter array of this method
     * @param returnType the return type of this method
     * @param construct the construct this method is contained in
     */
    public Method(final String name, final VisibilityModifier visibilityModifier, final Final finalState,
            final Type[] parameters, final Type returnType, final NameableConstruct construct) {
        super(name);
        this.visibilityModifier = visibilityModifier;
        this.finalState = finalState;
        this.parameters = parameters;
        this.returnType = returnType;
        this.construct = construct;
    }

    /**
     * Parses the given input to a method if possible and returns it.
     *
     * @param input the input which contains the method information at the indices given with {
     *         {@link #CONSTRUCT_INDEX}}, {{@link #VISIBILITY_INDEX}}, etc.
     * @param database the database which contains the type information
     *
     * @return the parsed Method if one could be parsed, null otherwise
     */
    public static Method parseFromString(final MatchResult input, final Database database) {
        final NameableConstruct construct = database.getConstruct(input.group(Method.CONSTRUCT_INDEX));
        final VisibilityModifier visibilityModifier = VisibilityModifier
                .parseFromString(input.group(Method.VISIBILITY_INDEX));
        final Final afinal = Final.parseFromString(input.group(Method.FINAL_INDEX));
        final String methodName = input.group(Method.METHOD_NAME_INDEX);
        final String[] parameterArray = input.group(Method.METHOD_PARAMETER_START_INDEX).split(Method.TYPE_SEPARATOR);
        final Type[] parameterTypes = getParameterTypes(database, parameterArray);
        final Type returnType = checkTypes(input, database, parameterTypes);
        if (returnType == null) {
            return null;
        }
        return new Method(methodName, visibilityModifier, afinal, parameterTypes, returnType, construct);
    }

    /**
     * Parses the given array to a type array with the given database, returns null if at least one of the types
     * could not be parsed.
     *
     * @param database the database which contains the type information
     * @param parameterArray an array containing the string representation of the types to be parsed
     *
     * @return an array containing the parsed types if possible, null if not
     */
    private static Type[] getParameterTypes(final Database database, final String[] parameterArray) {
        final Type[] parameterTypes = new Type[parameterArray.length];
        for (int index = 0; index < parameterTypes.length; index++) {
            if (database.typeAvailable(parameterArray[index]) == null) {
                return null;
            }
            // add the return type in the list and filter it later
            parameterTypes[index] = database.typeAvailable(parameterArray[index]);

        }
        return parameterTypes;
    }

    /**
     * Parses the given input to a method if possible and returns it. This method uses other indices than
     * {@link #parseFromString(MatchResult, Database)}, as this one uses the format provided by the
     * {@link edu.kit.informatik.classeditor.ui.Command#FIND_METHOD_OVERRIDE} method.
     *
     * @param input the input which contains the method information at the indices given with {
     *         {@link #CONSTRUCT_INDEX}}, {{@link #VISIBILITY_INDEX}}, etc.
     * @param database the database which contains the type information
     *
     * @return the parsed Method if one could be parsed, null otherwise
     */
    public static Method parseFromStringOverrideFormat(final MatchResult input, final Database database) {
        final NameableConstruct construct = database.getConstruct(input.group(Method.CONSTRUCT_INDEX));
        final String methodName = input.group(Method.VISIBILITY_INDEX);
        final String[] parameterArray = input.group(Method.FINAL_INDEX).split(Method.TYPE_SEPARATOR);
        final Type[] parameterTypes = getParameterTypes(database, parameterArray);
        final Type returnType = checkTypes(input, database, parameterTypes);
        if (returnType == null) {
            return null;
        }
        return new Method(methodName, null, null, parameterTypes, returnType, construct);
    }

    /**
     * Checks the given type array and returns the extracted return parameter type.
     *
     * @param input the input containing the method information
     * @param database the database used to get the types
     * @param parameterTypes the array containing the types of the parameters
     *
     * @return the type of the return type if all types are valid, null otherwise
     */
    private static Type checkTypes(final MatchResult input, final Database database, final Type[] parameterTypes) {
        if (parameterTypes == null) {
            return null;
        }
        for (final Type type : parameterTypes) {
            if (type == null) {
                return null;
            }
        }
        return database.typeAvailable(input.group(input.groupCount()));
    }

    /**
     * @return a {@link Comparator} which sorts the Methods lexicographically by their {getSignature}.
     */
    public static Comparator<Method> lexicographicallySortingComparator() {
        return Comparator.comparing(Method::getSignature);
    }

    /**
     * @return the return type of this method
     */
    public Type getReturnType() {
        return returnType;
    }

    /**
     * Returns an array with the parameters of this method. Not directly modifiable.
     *
     * @return the parameters of this method
     */
    public Type[] getParameters() {
        return Arrays.copyOf(parameters, parameters.length);
    }

    /**
     * @return the final state of this method
     */
    public Final getFinalState() {
        return finalState;
    }

    /**
     * @return the visibilityModifier of this method
     */
    public VisibilityModifier getVisibilityModifier() {
        return visibilityModifier;
    }

    @Override public String getSignature() {
        // @formatter:off
        return (construct != null ? construct.getName() + Construct.getConstructSeparator() + Main.SEPARATOR : "")
               + (visibilityModifier != VisibilityModifier.DEFAULT && visibilityModifier != null
                        ? visibilityModifier.getPattern() + Main.SEPARATOR
                        : Main.EMPTY_STRING)
               + (finalState != Final.NOT_FINAL && finalState != null
                        ? finalState.getPattern() + Main.SEPARATOR
                        : Main.EMPTY_STRING)
               + getShortenedSignature()
               + RETURN_TYPE_SEPARATOR
               + returnType.getType();
        // @formatter:on
    }

    private String getShortenedSignature() {
        final StringJoiner parameterTypes = getParameterList();
        return getName() + PARAMETER_LIST_START + parameterTypes.toString() + PARAMETER_LIST_END;
    }

    private StringJoiner getParameterList() {
        final StringJoiner parameterTypes = new StringJoiner(TYPE_SEPARATOR);
        for (final Type type : parameters) {
            parameterTypes.add(type.getType());
        }
        return parameterTypes;
    }

    @Override public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Method)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final Method method = (Method) o;
        return Arrays.equals(parameters, method.parameters) && Objects.equals(returnType, method.returnType)
               && getName().equals(method.getName());
    }

    @Override public int hashCode() {
        int result = Objects.hash(super.hashCode(), returnType);
        result = 31 * result + Arrays.hashCode(parameters) + getName().hashCode();
        return result;
    }

    @Override public int compareTo(final Method o) {
        return getShortenedSignature().compareTo(o.getShortenedSignature());
    }

    /**
     * @return the containing construct of this attribute
     */
    public NameableConstruct getConstruct() {
        return construct;
    }

    @Override public NameableConstruct getContainer() {
        return getConstruct();
    }
}
