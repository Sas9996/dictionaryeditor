


package edu.kit.informatik.classeditor.entity;

/**
 * A type is the kind of data stored in a variable.
 *
 * @author Sara
 * @version 1.0
 */
public interface Type {
    /**
     * @return the pattern for a type starting with a capital letter
     */
    static String getTypePattern() {
        return "([A-Z][a-zA-Z0-9]*)";
    }

    /**
     * @return the pattern for a case insensitive type (added types and primitive ones)
     */
    static String getCaseInsensitivePattern() {
        return "([a-zA-Z0-9]+)";
    }

    /**
     * @return the type String of this type
     */
    String getType();

}
